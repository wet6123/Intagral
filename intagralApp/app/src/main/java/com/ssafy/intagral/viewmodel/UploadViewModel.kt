package com.ssafy.intagral.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

// 업로드 뷰모델
class UploadViewModel: ViewModel() {

    // 파일
    private var imageBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    fun getImageBitmap(): MutableLiveData<Bitmap> {
        return imageBitmap
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