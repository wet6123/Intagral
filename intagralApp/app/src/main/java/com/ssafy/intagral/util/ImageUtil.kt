package com.ssafy.intagral.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageUtil {

    companion object{
        @JvmStatic
        fun resizeBitmap(src: Bitmap, size: Float, angle: Float): Bitmap {
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

        @JvmStatic
        fun createImageFile(context: Context): File {
            val timestamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir : File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile("JPEG_${timestamp}_",".jpeg",storageDir)
        }
    }
}