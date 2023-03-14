package com.chkan.bestpractices.optimaze_image_upload

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentOptimazeImageBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.rifqimfahmi.betterimageupload.ImageOptimizer
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class OptimazeImageFragment : BaseFragment<FragmentOptimazeImageBinding>(FragmentOptimazeImageBinding::inflate) {

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var uri: Uri? = null

    // TODO: refactor use dependency injection
    private val imageUtil: ImageUtil = DefaultImageUtil()

    private val coroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.IO
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBtnChooseImg()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_SELECT_PICTURE) {
            onSuccessReturnFromChooseImage(data)
        }
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_MAKE_PHOTO) {
            uri?.let { onSuccessReturnFromMakePhoto(it) }
        }
    }

    private fun setupBtnChooseImg() {
        binding.btnChooseImage.setOnClickListener {
           requestRequiredPermission()
        }
        binding.btnMakePhoto.setOnClickListener {
            requestRequiredPhotoPermission()
        }
    }

    private fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        val chooser = Intent.createChooser(intent, "Select Picture")
        startActivityForResult(chooser, REQUEST_SELECT_PICTURE)
    }

    private fun onSuccessReturnFromMakePhoto(uri: Uri) {
        loadOriginalImage(uri)
        optimizeImageBeforeUpload(uri)
    }

    private fun onSuccessReturnFromChooseImage(data: Intent?) {
        val imageUri = data?.data ?: return
        loadOriginalImage(imageUri)
        optimizeImageBeforeUpload(imageUri)
    }

    private fun loadOriginalImage(imageUri: Uri) {
        coroutineScope.launch {
            startLoadingOriginalImage()
            val ctx: Context = requireContext()
            val fileSize = imageUtil.getImageFileSize(ctx, imageUri)
            val bitmap = imageUtil.decodeImageMetaData(ctx, imageUri)
            stopLoadingOriginalImage()
            updateOriginalInputData(fileSize, bitmap, imageUri)
        }
    }

    private fun optimizeImageBeforeUpload(imageUri: Uri) {
        coroutineScope.launch {
            startLoadingOptimizedImage()
            val ctx = requireContext()
            val MAX_PHOTO_SIZE = 1280f
            val MIN_PHOTO_SIZE = 101
            val quality = 80
            val optimizedImageUri = ImageOptimizer.optimize(
                ctx, imageUri, Bitmap.CompressFormat.JPEG,
                MAX_PHOTO_SIZE, MAX_PHOTO_SIZE,
                true, quality,
                MIN_PHOTO_SIZE, MIN_PHOTO_SIZE
            ) ?: return@launch
            val fileSize = imageUtil.getImageFileSize(ctx, optimizedImageUri)
            val bmOptions = imageUtil.decodeImageMetaData(ctx, optimizedImageUri)
            stopLoadingOptimizedImage()
            updateOptimizedOutputData(fileSize, bmOptions, optimizedImageUri)
        }
    }

    private suspend fun startLoadingOriginalImage() {
        withContext(Dispatchers.Main) {
            binding.pgOriginal.visibility = View.VISIBLE
            binding.originalSize.text = "..calculating..."
            binding.originalDimension.text = "..calculating..."
        }
    }

    private suspend fun stopLoadingOriginalImage() {
        withContext(Dispatchers.Main) {
            binding.pgOriginal.visibility = View.GONE
            binding.originalSize.text = "-"
            binding.originalDimension.text = "-"
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun updateOriginalInputData(
        fileSize: Long,
        bitmap: BitmapFactory.Options?,
        imageUri: Uri
    ) {
        bitmap ?: return
        withContext(Dispatchers.Main) {
            binding.originalSize.text = "${fileSize / 1024} KB"
            binding.originalDimension.text = "${bitmap.outWidth}x${bitmap.outHeight}"
            Glide.with(requireContext()).load(imageUri).into(binding.originalImage)
        }
    }

    private suspend fun startLoadingOptimizedImage() {
        withContext(Dispatchers.Main) {
            binding.optimizedImage.setImageDrawable(null)
            binding.pgOptimized.visibility = View.VISIBLE
            binding.optimizedSize.text = "..calculating..."
            binding.optimizedDimension.text = "..calculating..."
        }
    }

    private suspend fun stopLoadingOptimizedImage() {
        withContext(Dispatchers.Main) {
            binding.pgOptimized.visibility = View.GONE
            binding.optimizedSize.text = "-"
            binding.optimizedDimension.text = "-"
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun updateOptimizedOutputData(
        fileSize: Long,
        bmOptions: BitmapFactory.Options?,
        uri: Uri?
    ) {
        bmOptions ?: return
        withContext(Dispatchers.Main) {
            binding.optimizedSize.text = "${fileSize / 1024} KB"
            binding.optimizedDimension.text = "${bmOptions.outWidth}x${bmOptions.outHeight}"
            Glide.with(requireContext()).load(uri).into(binding.optimizedImage)
        }
    }

    private fun requestRequiredPermission() {
        if (ContextCompat.checkSelfPermission(//Если разрешение нет - запрашиваем
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
            return
        } else {
            //Если разрешение предоставлено
            chooseImage()
        }
    }

    private fun requestRequiredPhotoPermission() {
        if (ContextCompat.checkSelfPermission(//Если разрешение нет - запрашиваем
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
            return
        } else {
            //Если разрешение предоставлено
            makePhoto()
        }
    }

    private fun makePhoto() {
        val values = ContentValues()
        values.put(
            MediaStore.Images.Media.TITLE,
            CONTENT_VALUE
        )
        uri =
            requireContext().contentResolver?.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_MAKE_PHOTO)
    }

    //слушаем ответ на запрос разрешения
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                requestRequiredPermission()
            }
        }else {
            //здесь что-то делаем если не дает разрешение
        }
    }

    companion object {
        const val REQUEST_PERMISSION = 256
        const val REQUEST_SELECT_PICTURE = 1
        const val REQUEST_MAKE_PHOTO = 2
        const val CONTENT_VALUE = "clientPhoto"
    }

}