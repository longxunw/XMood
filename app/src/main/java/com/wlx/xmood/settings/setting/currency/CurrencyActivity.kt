package com.wlx.xmood.settings.setting.currency

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.wlx.xmood.BaseActivity
import com.wlx.xmood.R
import com.wlx.xmood.ui.schedule.LessonAllFragment
import com.wlx.xmood.ui.schedule.ScheduleDataGet
import com.wlx.xmood.utils.DensityUtil
import com.wlx.xmood.utils.Utils
import java.io.File

class CurrencyActivity : BaseActivity() {

    private val fromPhoto = 1
    private val fromCamera = 2
    private lateinit var imageUri: Uri
    private lateinit var outputImage: File
    private lateinit var imageView: ImageView
    private val TAG = "CurrencyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)
        val back: ImageButton = findViewById(R.id.currency_back_btn)
        imageView = findViewById(R.id.test_image_view)
        back.setOnClickListener { finish() }
        val setSchedule: ConstraintLayout = findViewById(R.id.set_schedule)
        setSchedule.setOnClickListener {
            showDialog()
        }
    }

    fun showDialog() {
        val bottomDialog = Dialog(this, R.style.MyDialogTheme);
        val contentView = LayoutInflater.from(this).inflate(
            R.layout.set_schedule_dialog_content,
            null
        )
        bottomDialog.setContentView(contentView)
        val layoutParams: ViewGroup.MarginLayoutParams =
            contentView.layoutParams as ViewGroup.MarginLayoutParams;
        layoutParams.width =
            resources.displayMetrics.widthPixels - DensityUtil.dp2px(this, 16f)
        layoutParams.bottomMargin = DensityUtil.dp2px(this, 16f)
        contentView.layoutParams = layoutParams
        bottomDialog.window?.setGravity(Gravity.BOTTOM);
        bottomDialog.window?.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog.window?.findViewById<TextView>(R.id.select_schedule_bg_photo)
            ?.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, fromPhoto)
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.select_schedule_bg_camera)
            ?.setOnClickListener {
                outputImage = File(externalCacheDir, "output_image.jpg")
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
                imageUri =
                    FileProvider.getUriForFile(
                        this,
                        "com.wlx.xmood.fileprovider",
                        outputImage
                    )
                val intent = Intent("android.media.action.IMAGE_CAPTURE")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, fromCamera)
                bottomDialog.dismiss()
            }
        bottomDialog.window?.findViewById<TextView>(R.id.select_schedule_bg_cancel)
            ?.setOnClickListener {
                bottomDialog.dismiss()
            }
        bottomDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        when (requestCode) {
            fromCamera -> {
                if (resultCode == Activity.RESULT_OK) {
                    bitmap =
                        rotateIfRequired(BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri)))
                    imageView.setImageBitmap(bitmap)

                }
            }
            fromPhoto -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        bitmap = getBitmapFromUri(it)!!
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
        Log.d(TAG, "onActivityResult: Before " + ScheduleDataGet.background.toString())
        ScheduleDataGet.updateBackground(BitmapDrawable(bitmap))
        Log.d(TAG, "onActivityResult: After " + ScheduleDataGet.background.toString())
//        val bundle = Bundle()
//        bundle.putParcelable("background", bitmap)
//        LessonAllFragment.newInstance().arguments = bundle

    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

}