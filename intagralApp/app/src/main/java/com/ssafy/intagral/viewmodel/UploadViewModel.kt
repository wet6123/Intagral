package com.ssafy.intagral.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.intagral.data.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// TODO : tag 업로드
// 업로드 뷰모델
@HiltViewModel
class UploadViewModel @Inject constructor(private val service: PostService): ViewModel() {

    // 파일
    private var imageBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    private var uploadStep: MutableLiveData<UploadStep> = MutableLiveData()

    enum class UploadStep{
        PHOTO_PICKER,
        TAG_RESULT,
        PREVIEW,
        COMPLETE
    }

    fun getImageBitmap(): MutableLiveData<Bitmap> {
        return imageBitmap
    }

    fun getuploadStep(): MutableLiveData<UploadStep>{
        return uploadStep
    }

    fun publishPost(filePath: String){
        viewModelScope.launch {
            val file = File(filePath, "${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}.jpeg")
            FileOutputStream(file).use { out ->
                imageBitmap.value?.let { bitmap ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                var hashtags: List<String> = arrayListOf()
                tagMap.value?.let {
                    hashtags = it.filter {
                        it.value
                    }.keys.toList()
                }
                val response = service.publishPost(file, hashtags)
                if(response.isSuccessful){
                    Log.d("RETROFIT /api/post/publish", "${file.name} 업로드 성공")
                    uploadStep.value = UploadStep.COMPLETE
                } else {
                    Log.d("RETROFIT /api/post/publish", "응답 에러 : ${response.code()}")
                }
            }
        }
    }

    // 인식된 클래스 리스트
    private var detectedClassList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun getDetectedClassList(): MutableLiveData<ArrayList<String>> {
        return detectedClassList
    }

    // 태그 맵
    private var tagMap: MutableLiveData<HashMap<String, Boolean>> = MutableLiveData()

    fun getTagMap(): MutableLiveData<HashMap<String, Boolean>> {
        return tagMap
    }

}