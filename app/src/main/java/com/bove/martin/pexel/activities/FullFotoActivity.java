package com.bove.martin.pexel.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bove.martin.pexel.R;
import com.bove.martin.pexel.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;

public class FullFotoActivity extends AppCompatActivity {
    //TODO implementar MVVM
    private String photoUrl;
    private String largePhotoUrl;
    private String photographerUrl;

    private ImageView largeFoto;
    private ProgressBar progressBar;
    private Button setWallpaperButoon;

    private ImageView pexelLogo;
    private ImageView photographerIcon;
    private TextView photographerNameTextView;

    private LottieAnimationView succesAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_foto);

        initVars();
        loadContentFormBundle();

        setWallpaperButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper();
            }
        });

        pexelLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.PEXELS_URL));
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
        progressBar = findViewById(R.id.progressBar);
        pexelLogo = findViewById(R.id.pexelLogo);
        photographerIcon = findViewById(R.id.imageViewPhotographerIcon);
        photographerNameTextView = findViewById(R.id.textViewPhotgraperName);
        succesAnim = findViewById(R.id.successAnimation);
    }

    private void loadContentFormBundle() {
        if (getIntent().hasExtra(Constants.PHOTO_URL)) {
            photoUrl = getIntent().getStringExtra(Constants.PHOTO_URL);
            Glide.with(this).load(photoUrl).centerInside().into(largeFoto);
        }

        if (getIntent().hasExtra(Constants.LARGE_FOTO_URL)) {
            largePhotoUrl = getIntent().getStringExtra(Constants.LARGE_FOTO_URL);
        }

        if (getIntent().hasExtra(Constants.PHOTOGRAPHER_URL)) {
            photographerUrl = getIntent().getStringExtra(Constants.PHOTOGRAPHER_URL);
        }

        if (getIntent().hasExtra(Constants.PHOTOGRAPHER_NAME)) {
            String photographerName = getIntent().getStringExtra(Constants.PHOTOGRAPHER_NAME);
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
        succesAnim.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                succesAnim.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
            }
        });
    }


    private void setWallpaper() {
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
                            wallpaperManager.setBitmap(resource);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        Toast.makeText(FullFotoActivity.this, R.string.wallpaperChange, Toast.LENGTH_SHORT).show();

                        setWallpaperButoon.setEnabled(true);
                        hideProgressBar();
                        showSuccessAnim();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Toast.makeText(FullFotoActivity.this, R.string.loadImageError, Toast.LENGTH_SHORT).show();
                        setWallpaperButoon.setEnabled(false);
                        hideProgressBar();
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        setWallpaperButoon.setEnabled(false);
                        hideProgressBar();
                    }
                });
    }
}


