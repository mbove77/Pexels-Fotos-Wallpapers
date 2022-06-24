package com.bove.martin.pexel.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bove.martin.pexel.AppConstants
import com.bove.martin.pexel.AppConstants.AppErrors
import com.bove.martin.pexel.R
import com.bove.martin.pexel.databinding.ActivityFullFotoBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullFotoActivity : AppCompatActivity() {
    private lateinit var photoUrl: String
    private lateinit var largePhotoUrl: String
    private var photographerUrl: String? = null
    private var downloadForSharing = false

    private val viewModel by viewModels<FullFotoActivityViewModel>()
    private lateinit var binding: ActivityFullFotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadContentFormBundle()
        initUi()
        initObservables()
    }

    private fun initUi() {
        binding.buttonSetWallPapper.setOnClickListener { setWallpaper(false) }
        binding.buttonSetWallPapperLock.setOnClickListener { setWallpaper(true) }
        binding.pexelLogo.setOnClickListener { startUrlIntent(AppConstants.PEXELS_URL) }
        binding.textViewPhotgraperName.setOnClickListener { photographerUrl?.let { url -> startUrlIntent(url) } }
    }

    private fun initObservables() {
        viewModel.haveStoragePermission.observe(this) { havePermission ->
            if (havePermission != null) {
                if (!havePermission) {
                    Toast.makeText(
                        this@FullFotoActivity,
                        AppErrors.SHARE_PERMISSION_ERROR.getErrorMessage(),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    downloadFoto(downloadForSharing)
                }
            }
        }

        viewModel.operationResult.observe(this) { operationResult ->
            Toast.makeText(this@FullFotoActivity, operationResult.resultMensaje, Toast.LENGTH_SHORT).show()
            enableDisableUI(true)
            showHideProgressBar(false)
            if (operationResult.operationResult) {
                showSuccessAnim()
            }
        }

        viewModel.savedFoto.observe(this) { uri ->
            showHideProgressBar(false)
            shareBitmap(uri)
        }
    }

    private fun loadContentFormBundle() {
        if (intent.hasExtra(AppConstants.PHOTO_URL) && intent.hasExtra(AppConstants.LARGE_FOTO_URL)) {
            photoUrl = intent.getStringExtra(AppConstants.PHOTO_URL).toString()
            largePhotoUrl = intent.getStringExtra(AppConstants.LARGE_FOTO_URL).toString()
            Glide.with(this).load(photoUrl).centerInside().into(binding.imageViewLargeFoto)
        } else {
            showErrors(AppErrors.LOAD_IMAGE_ERROR.getErrorMessage())
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

    private fun showErrors(errorMessage: String) {
        Snackbar.make(
            this.findViewById(android.R.id.content),
            errorMessage,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.back)) {
            startActivity(Intent(this, MainActivity::class.java))
        }.show()
    }

    private fun showHideProgressBar(isVisible: Boolean) {
        if(isVisible) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun enableDisableUI(isEnable: Boolean) {
        binding.buttonSetWallPapper.isEnabled = isEnable
        binding.buttonSetWallPapperLock.isEnabled = isEnable
    }

    private fun showSuccessAnim() {
        binding.successAnimation.playAnimation()
    }

    private fun setWallpaper(isLockScreen: Boolean) {
        enableDisableUI(false)
        showHideProgressBar(true)
        viewModel.setWallpaper(largePhotoUrl, isLockScreen)
    }

    private fun downloadFoto(isForSharing: Boolean) {
        downloadForSharing = isForSharing
        if (viewModel.haveStoragePermission.value != null && viewModel.haveStoragePermission.value == true) {
            showHideProgressBar(true)
            viewModel.downloadFoto(largePhotoUrl)
        } else {
            checkStoragePermission()
        }
    }

    private fun startUrlIntent(Url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(Url)
        startActivity(i)
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