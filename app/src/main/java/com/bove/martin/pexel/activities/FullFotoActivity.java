package com.bove.martin.pexel.activities;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bove.martin.pexel.utils.AppConstants;

import com.airbnb.lottie.LottieAnimationView;
import com.bove.martin.pexel.R;
import com.bove.martin.pexel.viewmodels.FullFotoActivityViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

//TODO implement sharing functionality

public class FullFotoActivity extends AppCompatActivity {
    private String photoUrl;
    private String largePhotoUrl;
    private String photographerUrl;

    private ImageView largeFoto;
    private ProgressBar progressBar;
    private Button setWallpaperButoon;
    private Button setWallpaperLockButoon;

    private ImageView pexelLogo;
    private ImageView photographerIcon;
    private TextView photographerNameTextView;

    private LottieAnimationView succesAnim;

    private FullFotoActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_foto);

        initVars();
        loadContentFormBundle();
        checkPermission();

        viewModel.getHavePermission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setWallpaperButoon.setEnabled(aBoolean);
            }
        });

        setWallpaperButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(false);
            }
        });

        setWallpaperLockButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper(true);
            }
        });

        pexelLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(AppConstants.PEXELS_URL));
                startActivity(i);
            }
        });

        photographerNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoUrl != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(photographerUrl));
                    startActivity(i);
                }
            }
        });
    }

    private void initVars() {
        largeFoto = findViewById(R.id.imageViewLargeFoto);
        setWallpaperButoon = findViewById(R.id.buttonSetWallPapper);
        setWallpaperLockButoon = findViewById(R.id.buttonSetWallPapperLock);
        progressBar = findViewById(R.id.progressBar);
        pexelLogo = findViewById(R.id.pexelLogo);
        photographerIcon = findViewById(R.id.imageViewPhotographerIcon);
        photographerNameTextView = findViewById(R.id.textViewPhotgraperName);
        succesAnim = findViewById(R.id.successAnimation);
        viewModel = ViewModelProviders.of(this).get(FullFotoActivityViewModel.class);
    }

    private void loadContentFormBundle() {
        if (getIntent().hasExtra(AppConstants.PHOTO_URL)) {
            photoUrl = getIntent().getStringExtra(AppConstants.PHOTO_URL);
            Glide.with(this).load(photoUrl).centerInside().into(largeFoto);
        }

        if (getIntent().hasExtra(AppConstants.LARGE_FOTO_URL)) {
            largePhotoUrl = getIntent().getStringExtra(AppConstants.LARGE_FOTO_URL);
        }

        if (getIntent().hasExtra(AppConstants.PHOTOGRAPHER_URL)) {
            photographerUrl = getIntent().getStringExtra(AppConstants.PHOTOGRAPHER_URL);
        }

        if (getIntent().hasExtra(AppConstants.PHOTOGRAPHER_NAME)) {
            String photographerName = getIntent().getStringExtra(AppConstants.PHOTOGRAPHER_NAME);
            photographerIcon.setVisibility(View.VISIBLE);
            photographerNameTextView.setText(photographerName);
            photographerNameTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showSuccessAnim() {
        succesAnim.playAnimation();
    }

    private void setWallpaper(Boolean isLockScreen) {
        setWallpaperLockButoon.setEnabled(false);
        setWallpaperButoon.setEnabled(false);
        showProgressBar();

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(largePhotoUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // resource is your loaded Bitmap
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullFotoActivity.this);
                        try {
                            if(isLockScreen) {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK);
                                } else {
                                    Toast.makeText(FullFotoActivity.this, R.string.wallpaperLockError, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                wallpaperManager.setBitmap(resource);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        Toast.makeText(FullFotoActivity.this, R.string.wallpaperChange, Toast.LENGTH_SHORT).show();

                        setWallpaperButoon.setEnabled(true);
                        setWallpaperLockButoon.setEnabled(true);
                        hideProgressBar();
                        showSuccessAnim();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Toast.makeText(FullFotoActivity.this, R.string.loadImageError, Toast.LENGTH_SHORT).show();
                        setWallpaperButoon.setEnabled(true);
                        setWallpaperLockButoon.setEnabled(true);
                        hideProgressBar();
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        setWallpaperButoon.setEnabled(true);
                        setWallpaperLockButoon.setEnabled(true);
                        hideProgressBar();
                    }
                });
    }

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.SET_WALLPAPER)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        viewModel.setHavePermission(true);
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        viewModel.setHavePermission(false);
                        Toast.makeText(FullFotoActivity.this, R.string.permissionError, Toast.LENGTH_LONG).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}


