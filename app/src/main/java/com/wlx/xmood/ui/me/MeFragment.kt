package com.wlx.xmood.ui.me

import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wlx.xmood.ActivityCollector
import com.wlx.xmood.R
import com.wlx.xmood.sign.SignActivity
import com.wlx.xmood.utils.DensityUtil
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.util.*

class MeFragment : Fragment() {
    private var menuList = arrayListOf<MeMenuItem>()

    private lateinit var adapter: MeMenuAdapter

    private lateinit var myContext: Context

    private val fromPhoto = 1
    private val fromCamera = 2
    private lateinit var imageUri: Uri
    private lateinit var outputImage: File
    private lateinit var userFaceImg: CircleImageView

    private val TAG = "MeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        myContext = requireContext()
        val root = inflater.inflate(R.layout.fragment_me, container, false)
        Log.d(TAG, "onCreateView: ${ActivityCollector.isLogin}")
        val meHeaderLogged: ConstraintLayout = root.findViewById(R.id.me_header_logged)
        val meHeaderNotLogged: RelativeLayout = root.findViewById(R.id.me_header_not_logged)
        if (!ActivityCollector.isLogin) {
            meHeaderLogged.visibility = View.GONE
            meHeaderNotLogged.visibility = View.VISIBLE
        } else {
            meHeaderLogged.visibility = View.VISIBLE
            meHeaderNotLogged.visibility = View.GONE
        }
        val toSignBtn: TextView = root.findViewById(R.id.to_sign_btn)
        toSignBtn.setOnClickListener {
            val intent = Intent(context, SignActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        }
        val recyclerView: RecyclerView = root.findViewById(R.id.me_menu_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = MeMenuAdapter(this, menuList)
        recyclerView.adapter = adapter
        val exitBtn: ConstraintLayout = root.findViewById(R.id.me_menu_exit)
        exitBtn.setOnClickListener {
            ActivityCollector.finishAll()
        }
        userFaceImg = root.findViewById(R.id.me_face_img)
        userFaceImg.setOnClickListener {
            showDialog()
        }
        val alarmTest: Button = root.findViewById(R.id.alarm_test_btn)
        alarmTest.setOnClickListener {
            setAlarm(Date().time + 1000)
        }
        return root
    }

    private fun init() {
        menuList.add(
            MeMenuItem(
                R.drawable.ic_me_theme_24,
                R.string.me_menu_theme, R.drawable.ic_me_arrow_24, "ThemeSelectActivity"
            )
        )
        menuList.add(
            MeMenuItem(
                R.drawable.ic_feedback_24, R.string.me_menu_feedback,
                R.drawable.ic_me_arrow_24, "FeedBackActivity"
            )
        )
        menuList.add(
            MeMenuItem(
                R.drawable.ic_share_24, R.string.me_menu_share,
                R.drawable.ic_me_arrow_24, "ShareActivity"
            )
        )
        menuList.add(
            MeMenuItem(
                R.drawable.ic_settings_24, R.string.me_menu_setting,
                R.drawable.ic_me_arrow_24, "SettingActivity"
            )
        )
        Log.d(TAG, "isLogin: ${ActivityCollector.isLogin}")
        if (ActivityCollector.isLogin) {
            menuList.add(
                MeMenuItem(
                    R.drawable.ic_exit_24, R.string.me_menu_logout,
                    R.drawable.ic_me_arrow_24, "Logout"
                )
            )
        }
    }

    fun showDialog() {
        val bottomDialog = Dialog(myContext, R.style.MyDialogTheme);
        val contentView = LayoutInflater.from(myContext).inflate(
            R.layout.set_schedule_dialog_content,
            null
        )
        bottomDialog.setContentView(contentView)
        val layoutParams: ViewGroup.MarginLayoutParams =
            contentView.layoutParams as ViewGroup.MarginLayoutParams;
        layoutParams.width =
            resources.displayMetrics.widthPixels - DensityUtil.dp2px(myContext, 16f)
        layoutParams.bottomMargin = DensityUtil.dp2px(myContext, 16f)
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
                outputImage = File(myContext.externalCacheDir, "output_image.jpg")
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
                imageUri =
                    FileProvider.getUriForFile(
                        myContext,
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
        when (requestCode) {
            fromCamera -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap =
                        BitmapFactory.decodeStream(
                            myContext.contentResolver.openInputStream(
                                imageUri
                            )
                        )
                    userFaceImg.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
            fromPhoto -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        val bitmap = getBitmapFromUri(it)
                        userFaceImg.setImageBitmap(bitmap)
                    }

                }
            }
        }
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

    private fun getBitmapFromUri(uri: Uri) =
        myContext.contentResolver.openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }

    private fun setAlarm(time: Long) {
        val alarm: AlarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent("android.xmood.daily.alarm")
        intent.setPackage("com.wlx.xmood")
        val sender = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarm.set(AlarmManager.RTC_WAKEUP, time, sender)
        Log.d(TAG, "setAlarm: ")
    }
}