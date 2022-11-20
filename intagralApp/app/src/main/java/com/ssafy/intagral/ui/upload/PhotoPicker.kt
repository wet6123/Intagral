package com.ssafy.intagral.ui.upload

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.intagral.MainMenuActivity
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentPhotoPickerBinding
import com.ssafy.intagral.util.ImageUtil
import com.ssafy.intagral.viewmodel.PresetViewModel
import com.ssafy.intagral.viewmodel.UploadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * TODO
 *  - 수행 결과 클래스 1개? 여러개?
 */
@AndroidEntryPoint
class PhotoPicker : Fragment(), CoroutineScope {

    // 코루틴 비동기를 위한 정의 : pytorch 모델을 비동기로..
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Default + job

    // 프로그레스 바
    private lateinit var progressDialog: Dialog
    private lateinit var progressBar: View

    private lateinit var binding: FragmentPhotoPickerBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher : ActivityResultLauncher<Intent>
    private var currentPhotoFile : File? = null
    private lateinit var imageView: ImageView

    private val uploadViewModel: UploadViewModel by activityViewModels()
    private val presetViewModel: PresetViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        // 프로그레스 다이얼로그
        progressBar = layoutInflater.inflate(R.layout.view_progress_model, null)
        progressDialog = Dialog(requireContext())
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경을 투명하게
        progressDialog.setContentView(progressBar) // ProgressBar 위젯 생성
        progressDialog.setCanceledOnTouchOutside(false) // 외부 터치 막음
        progressDialog.setCancelable(false)
        (progressBar.findViewById(R.id.model_process_cancel_button) as Button).setOnClickListener {
            job.cancel()
            progressDialog.dismiss()
        }

        uploadViewModel.getImageBitmap().value = null
        uploadViewModel.getDetectedClassList().value = null
        uploadViewModel.getTagMap().value = null
        uploadViewModel.getuploadStep().value = UploadViewModel.UploadStep.PHOTO_PICKER
        presetViewModel.getPresetList().value

        arguments?.let {
        }

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                currentPhotoFile?.also {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, Uri.fromFile(it))
                        ImageDecoder.decodeBitmap(source)?.let {
                            uploadViewModel.getImageBitmap().value = ImageUtil.resizeBitmap(it, 900f, 0f)
                        }
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.fromFile(it))?.let {
                            uploadViewModel.getImageBitmap().value = ImageUtil.resizeBitmap(it, 900f, 0f)
                        }
                    }
                }
            }
        }
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == Activity.RESULT_OK){
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoPickerBinding.inflate(inflater, container, false)
        binding.root.id
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
                if(it==null){
                    binding.button.isEnabled = false;
                }else {
                    binding.button.isEnabled = true;
                }
            }
        }
        uploadViewModel.getDetectedClassList().observe(
            viewLifecycleOwner
        ){
            it?.also{
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(
                        R.id.menu_frame_layout,
                        ResultTagListFragment.newInstance()
                    ).commit()
            }
        }
        uploadViewModel.getuploadStep().observe(
            viewLifecycleOwner
        ){
            when(it){
                UploadViewModel.UploadStep.PHOTO_PICKER -> {
                    uploadViewModel.getDetectedClassList().value = null
                    uploadViewModel.getTagMap().value = null
                    progressDialog.dismiss()
                    (progressBar.findViewById(R.id.model_process_cancel_button) as Button).visibility = View.VISIBLE
                }
                UploadViewModel.UploadStep.COMPLETE -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.menu_frame_layout,
                            UploadCompleteFragment.newInstance()
                        ).commit()
                    progressDialog.dismiss()
                    (progressBar.findViewById(R.id.model_process_cancel_button) as Button).visibility = View.VISIBLE
                }
                UploadViewModel.UploadStep.UPLOADING -> {
                    (progressBar.findViewById(R.id.model_process_cancel_button) as Button).visibility = View.GONE
                    progressDialog.show()
                }
                else -> {

                }
            }
        }

        binding.button.setOnClickListener {
            uploadViewModel.getImageBitmap().value?.also {
                job = Job()
                launch {
                    withContext(Main){
                        progressDialog.show()
                    }
                    val output = runModel()

                    delay(1000) // 취소를 위해 살짝 딜레이

                    output?.let {
                        val result = parseScore(output)
                        progressDialog.dismiss()

                        withContext(Main){
                            uploadViewModel.getDetectedClassList().value = result
                        }
                    }
                }
            }
        }
        return binding.root
    }

    // View 생성이 완료 되었으며 UI 초기화 작업 진행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    // 프레그먼트 없어지면 비동기 작업 취소
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        uploadViewModel.getImageBitmap().value = null
        uploadViewModel.getDetectedClassList().value = null
        uploadViewModel.getTagMap().value = null
        uploadViewModel.getuploadStep().value = null
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

    // 카메라 실행
    private fun capture(){
        context?.also{
            val file = ImageUtil.createImageFile(it)
            currentPhotoFile = file
            val photoUri : Uri = FileProvider.getUriForFile(
                it,
                "com.ssafy.intagral.provider",
                file
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

    // object detection
    @RequiresApi(Build.VERSION_CODES.O)
    private fun runModel(): FloatArray? {
        uploadViewModel.getImageBitmap().value?.also {
            val module: Module = (requireActivity() as MainMenuActivity).mModule
            val mutableBitmap = it.copy(Bitmap.Config.RGBA_F16, true)
            val resizedBitmap = Bitmap.createScaledBitmap(
                mutableBitmap,
                640,
                640,
                true
            )
            val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
                resizedBitmap,
                floatArrayOf(0.0f, 0.0f, 0.0f),
                floatArrayOf(1.0f, 1.0f, 1.0f)
            )

            val outputTuple = module.forward(IValue.from(inputTensor)).toTuple()
            val outputTensor = outputTuple[0].toTensor()
            val outputs = outputTensor.dataAsFloatArray

            return outputs
        }
        return null
    }

    // model output is of size 25200*(num_of_class+5)
    private val mOutputRow = 25200 // as decided by the YOLOv5 model for input image of size 640*640

//    private val mOutputColumn = 85 // left, top, right, bottom, score and 80 class probability

    private val mThreshold = 0.10f // score above which a detection is generated

    private val mNmsLimit = 15
    // parse detection score
    private fun parseScore(outputs: FloatArray) : ArrayList<String>{
        val classTargetList = (requireActivity() as MainMenuActivity).classList
        val mOutputColumn = classTargetList.size + 5
        val detectedSet: HashSet<String> = HashSet()
        detectedSet.add("default")
        for (i in 0 until mOutputRow){

            if (outputs[i* mOutputColumn +4] > mThreshold) {

                var max = outputs[i* mOutputColumn +5];
                var cls = 0
                for(j in 0 until mOutputColumn - 5){
                    if (outputs[i* mOutputColumn +5+j] > max) {
                        max = outputs[i* mOutputColumn +5+j]
                        cls = j
                    }
                }
                detectedSet.add(classTargetList[cls])
            }
        }

        return ArrayList(detectedSet)
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