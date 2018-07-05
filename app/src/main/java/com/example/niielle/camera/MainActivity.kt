package com.example.niielle.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth




class MainActivity : AppCompatActivity() {

    var currentPath:String?=null
    val TAKE_PICTURE=1
    val SELECT_PICTURE=2

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        btn_camera.setOnClickListener {
            opencamera()
        }

        btn_gallery.setOnClickListener {
            opengalley()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==TAKE_PICTURE&&resultCode==Activity.RESULT_OK){
            try {
                val file=File(currentPath)
                val uri= Uri.fromFile(file)
                imageView.setImageURI(uri)

            }catch (e:IOException){e.printStackTrace()}
        }
        if(requestCode==SELECT_PICTURE&&resultCode==Activity.RESULT_OK){
            try {
                val uri=data!!.data
                imageView.setImageURI(uri)

            }catch (e:IOException){e.printStackTrace()}
        }



    }
    fun opengalley() {
        val intent=Intent()
        intent.type="imge/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select image"), SELECT_PICTURE)
    }
    fun opencamera() {

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        val fileName = intent.getStringExtra(MediaStore.EXTRA_OUTPUT)
        startActivityForResult(Intent.createChooser(intent,"Capture"), TAKE_PICTURE)


    }

    @SuppressLint("SimpleDateFormat")
    fun createimg():File{
        val timestamp=SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imgName="JPEG_"+timestamp+"_"
        val storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val img=File.createTempFile(imgName,"jpg",storageDir)
        currentPath=img.absolutePath
        return img

    }



    private fun setPic() {
        // Get the dimensions of the View
        val targetW = imageView.getWidth()
        val targetH = imageView.getHeight()

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(currentPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(currentPath, bmOptions)
        imageView.setImageBitmap(bitmap)
    }
}


