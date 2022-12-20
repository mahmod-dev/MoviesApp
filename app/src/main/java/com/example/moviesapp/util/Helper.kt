package com.example.moviesapp.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import com.example.moviesapp.data.Categories
import com.example.moviesapp.data.entity.User
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


fun View.showSnackbar( text: String?) {
    Snackbar.make(this, text!!, Snackbar.LENGTH_LONG).show()

}

fun MyPreferences.saveUserInfo(user: User?){
    this.setStr(Constant.EMAIL, user?.email)
    this.setStr(Constant.FIRST_NAME, user?.firstName)
    this.setStr(Constant.LAST_NAME, user?.lastName)
    this.setStr(Constant.PASSWORD, user?.password)
    this.setInt(Constant.ID, user?.id!!)
}

 fun spinnerArr() =
    arrayOf(
        Categories.ACTION.toString(),
        Categories.COMEDY.toString(),
        Categories.DRAMA.toString(),
        Categories.HORROR.toString()
    )


fun bitmapToFile(bmp: Bitmap?): File? {

    var outStream: OutputStream? = null
    // String temp = null;
    val file = File.createTempFile("temp", "${System.currentTimeMillis()}.jpeg")
    try {
        /*  val ratio: Float = Math.min(
              10 as Float / bmp.getWidth(),
              maxImageSize as Float / bmp.getHeight()
          )
          val width = Math.round(ratio as Float * bmp.getWidth()).toInt()
          val height = Math.round(ratio as Float * bmp.getHeight()).toInt()
          val newBitmap = Bitmap.createScaledBitmap(
              bmp, width,
              height,false
          )*/
        outStream = FileOutputStream(file)

        bmp?.compress(Bitmap.CompressFormat.JPEG, 20, outStream)


        outStream.flush()
        outStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return file
}

fun Uri.toBitmap(contentResolver: ContentResolver): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(contentResolver, this)
        } else {
            val source: ImageDecoder.Source =
                ImageDecoder.createSource(contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return bitmap
}
