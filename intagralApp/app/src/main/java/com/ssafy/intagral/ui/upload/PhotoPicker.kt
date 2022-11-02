package com.ssafy.intagral.ui.upload

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentPhotoPickerBinding
import com.ssafy.intagral.viewmodel.UploadViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * TODO
 *  - model embedding
 */
class PhotoPicker : Fragment() {

    private lateinit var binding: FragmentPhotoPickerBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher : ActivityResultLauncher<Intent>
    private var currentPhotoFile : File? = null
    private lateinit var imageView: ImageView

    private val uploadViewModel: UploadViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadViewModel.getImageBitmap().value = null
        uploadViewModel.getDetectedClassList().value = null
        uploadViewModel.getTagMap().value = null

        arguments?.let {
        }

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                Toast.makeText(context, "captured!!!", Toast.LENGTH_SHORT).show();
                currentPhotoFile?.also {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, Uri.fromFile(it))
                        ImageDecoder.decodeBitmap(source)?.let {
                            uploadViewModel.getImageBitmap().value = resizeBitmap(it, 900f, 0f)
                        }
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.fromFile(it))?.let {
                            uploadViewModel.getImageBitmap().value = resizeBitmap(it, 900f, 0f)
                        }
                    }
                }
            }
        }
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == Activity.RESULT_OK){
                Toast.makeText(context, "get photo from gallery", Toast.LENGTH_SHORT).show();
                val photoUri : Uri? = it.data?.data
                photoUri?.also{
                    if(Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            photoUri
                        )
                        uploadViewModel.getImageBitmap().value = bitmap
                    } else {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, photoUri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        uploadViewModel.getImageBitmap().value = bitmap
                    }
                }
            }
        }
    }

    // layout을 inflate 하는 단계로 뷰 바인딩 진행, UI에 대한 작업은 진행하지 않음
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoPickerBinding.inflate(inflater, container, false)
        imageView = binding.imageView2
        imageView.setOnClickListener{
            it.showContextMenu()
        }
        registerForContextMenu(imageView)


        uploadViewModel.getImageBitmap().observe(
            viewLifecycleOwner
        ) {
            it?.also {
                binding.imageView2.setImageBitmap(it)
            }
        }

        binding.button.setOnClickListener {
            uploadViewModel.getImageBitmap().value?.also {
                uploadViewModel.getDetectedClassList().value = arrayListOf("default")
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.menu_frame_layout,
                        ResultTagListFragment.newInstance()
                    ).commit()
            }
            if(uploadViewModel.getImageBitmap().value == null){
                Toast.makeText(requireContext(), "사진을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.photo_picker_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.photo_picker_capture -> {
                capture()
            }
            R.id.photo_picker_album -> {
                getImageFromGallery()
            }
            else -> {

            }
        }
        return super.onContextItemSelected(item)
    }

    // View 생성이 완료 되었으며 UI 초기화 작업 진행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //이미지 파일 생성
    private fun createImageFile(): File {
        val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_",".jpeg",storageDir).apply {
            currentPhotoFile = this
        }
    }

    private fun resizeBitmap(src: Bitmap, size: Float, angle: Float): Bitmap {
        val width = src.width
        val height = src.height
        var newWidth = 0f
        var newHeight = 0f
        if(width > height) {
            newWidth = size
            newHeight = height.toFloat() * (newWidth / width.toFloat())
        } else {
            newHeight = size
            newWidth = width.toFloat() * (newHeight / height.toFloat())
        }
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postRotate(angle);
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true)
        return resizedBitmap
    }

    // 카메라 실행
    private fun capture(){
        context?.also{
            val photoUri : Uri = FileProvider.getUriForFile(
                it,
                "com.ssafy.intagral.provider",
                createImageFile()
            )
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        }
    }

    // 갤러리 실행
    private fun getImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    // 프레그먼트가 생성돼서 해당 프레그먼트가 액티비티에 추가되기 전에 인자를 첨부하기 위해 companion object 사용
    companion object {
        @JvmStatic
        fun newInstance() =
            PhotoPicker().apply {
                arguments = Bundle().apply {
                }
            }
    }
}