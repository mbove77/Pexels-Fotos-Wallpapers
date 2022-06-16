package com.bove.martin.pexel.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bove.martin.pexel.R
import com.bove.martin.pexel.databinding.ActivityFullFotoBinding
import com.bove.martin.pexel.utils.AppConstants
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class FullFotoActivity : AppCompatActivity() {
    private var photoUrl: String? = null
    private var largePhotoUrl: String? = null
    private var photographerUrl: String? = null
    private var downloadForSharing = false

    private val viewModel by viewModels<FullFotoActivityViewModel>()
    private lateinit var binding: ActivityFullFotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadContentFormBundle()


        viewModel.haveStoragePermission.observe(this) { aBoolean ->
            if (aBoolean != null) {
                if (!aBoolean) {
                    Toast.makeText(
                        this@FullFotoActivity,
                        R.string.permissionErrorStorage,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    downloadFoto(downloadForSharing)
                }
            }
        }

        binding.buttonSetWallPapper.setOnClickListener { setWallpaper(false) }

        binding.buttonSetWallPapperLock.setOnClickListener { setWallpaper(true) }

        binding.pexelLogo.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(AppConstants.PEXELS_URL)
            startActivity(i)
        }

        binding.textViewPhotgraperName.setOnClickListener {
            if (photoUrl != null) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(photographerUrl)
                startActivity(i)
            }
        }

        viewModel.operationResult.observe(this) { operationResult ->
            Toast.makeText(this@FullFotoActivity, operationResult.resultMensaje, Toast.LENGTH_SHORT)
                .show()
            binding.buttonSetWallPapper.isEnabled = true
            binding.buttonSetWallPapperLock.isEnabled = true
            hideProgressBar()
            if (operationResult.operationResult) {
                showSuccessAnim()
            }
        }

        viewModel.savedFoto.observe(this) { uri ->
            hideProgressBar()
            shareBitmap(uri)
        }
    }


    private fun loadContentFormBundle() {
        if (intent.hasExtra(AppConstants.PHOTO_URL)) {
            photoUrl = intent.getStringExtra(AppConstants.PHOTO_URL)
            Glide.with(this).load(photoUrl).centerInside().into(binding.imageViewLargeFoto)
        }
        if (intent.hasExtra(AppConstants.LARGE_FOTO_URL)) {
            largePhotoUrl = intent.getStringExtra(AppConstants.LARGE_FOTO_URL)
        }
        if (intent.hasExtra(AppConstants.PHOTOGRAPHER_URL)) {
            photographerUrl = intent.getStringExtra(AppConstants.PHOTOGRAPHER_URL)
        }
        if (intent.hasExtra(AppConstants.PHOTOGRAPHER_NAME)) {
            val photographerName = intent.getStringExtra(AppConstants.PHOTOGRAPHER_NAME)
            binding.imageViewPhotographerIcon.visibility = View.VISIBLE
            binding.textViewPhotgraperName.text = photographerName
            binding.textViewPhotgraperName.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showSuccessAnim() {
        binding.successAnimation.playAnimation()
    }

    private fun setWallpaper(isLockScreen: Boolean) {
        binding.buttonSetWallPapper.isEnabled = false
        binding.buttonSetWallPapperLock.isEnabled = false
        showProgressBar()
        viewModel.setWallpaper(largePhotoUrl!!, isLockScreen)
    }

    private fun downloadFoto(isForSharing: Boolean) {
        downloadForSharing = isForSharing
        if (viewModel.haveStoragePermission.value != null && viewModel.haveStoragePermission.value!!) {
            showProgressBar()
            viewModel.downloadFoto(largePhotoUrl!!)
        } else {
            checkStoragePermission()
        }
    }

    private fun shareBitmap(imageUri: Uri?) {
        if (imageUri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            val intentDown = Intent(Intent.ACTION_VIEW, imageUri)
            intentDown.type = "image/*"
            intentDown.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intentDown.putExtra(Intent.EXTRA_STREAM, imageUri)
            if (downloadForSharing) {
                startActivity(intent)
            } else {
                startActivity(intentDown)
            }
            downloadForSharing = false
        }
    }

    // Share Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                downloadFoto(true)
                return false
            }
            R.id.action_download -> {
                downloadFoto(false)
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkStoragePermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        viewModel.setStoragePermission(true)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        viewModel.setStoragePermission(false)
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }
}