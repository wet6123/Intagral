package com.ssafy.intagral.ui.common.profile

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
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentProfileEditBinding
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher : ActivityResultLauncher<Intent>
    private var currentPhotoFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                            binding.profileEditImage.setImageBitmap(resizeBitmap(it, 900f, 0f))
                        }
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.fromFile(it))?.let {
                            binding.profileEditImage.setImageBitmap(resizeBitmap(it, 900f, 0f))
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
                        binding.profileEditImage.setImageBitmap(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, photoUri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.profileEditImage.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)

        binding.applyProfileEditButton.setOnClickListener(ProfileEditClickListener())
        binding.cancelProfileEditButton.setOnClickListener(ProfileEditClickListener())

        binding.profileEditImage.setOnClickListener {
            it.showContextMenu()
        }
        registerForContextMenu(binding.profileEditImage)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileDetailViewModel.getProfileDetail().value?.let {
            Glide.with(requireContext()).load(it.profileImg).into(binding.profileEditImage)
            binding.profileEditNameText.setText(it.name)
            binding.profileEditIntroText.setText(it.intro)
        }
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

    inner class ProfileEditClickListener : View.OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.apply_profile_edit_button -> {
                    // TODO : 1. 이름 중복 검사
                    // TODO : 2. image 수정 요청
                    // TODO : 3. 이름, 소개글 수정 요청
                }
                R.id.cancel_profile_edit_button -> {
                    requireActivity()
                        .supportFragmentManager
                        .popBackStack()
                }
                else -> {

                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileEditFragment().apply {
                arguments = Bundle().apply {
                }
            }
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
}