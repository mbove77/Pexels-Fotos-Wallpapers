package com.bove.martin.pexel.activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import com.bove.martin.pexel.R;
import com.bove.martin.pexel.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;

public class FullFotoActivity extends AppCompatActivity {
    //TODO implementar MVVM
    private String photoUrl;
    private String largePhotoUrl;
    private String photographerUrl;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_foto);

        ImageView largeFoto = findViewById(R.id.imageViewLargeFoto);
        Button setWallpaperButoon = findViewById(R.id.buttonSetWallPapper);
        ImageView pexelLogo = findViewById(R.id.pexelLogo);
        ImageView photographerIcon = findViewById(R.id.imageViewPhotographerIcon);
        TextView photographerNameTextView = findViewById(R.id.textViewPhotgraperName);
        progressBar = findViewById(R.id.progressBar);

        if (getIntent().hasExtra(Util.PHOTO_URL)) {
            photoUrl = getIntent().getStringExtra(Util.PHOTO_URL);
            Glide.with(this).load(photoUrl).centerInside().into(largeFoto);
        }

        if (getIntent().hasExtra(Util.LARGE_FOTO_URL)) {
            largePhotoUrl = getIntent().getStringExtra(Util.LARGE_FOTO_URL);
        }

        if (getIntent().hasExtra(Util.PHOTOGRAPHER_URL)) {
            photographerUrl = getIntent().getStringExtra(Util.PHOTOGRAPHER_URL);
        }

        if (getIntent().hasExtra(Util.PHOTOGRAPHER_NAME)) {
            String photographerName = getIntent().getStringExtra(Util.PHOTOGRAPHER_NAME);
            photographerIcon.setVisibility(View.VISIBLE);
            photographerNameTextView.setText(photographerName);
            photographerNameTextView.setVisibility(View.VISIBLE);
        }

        setWallpaperButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });


        pexelLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Util.PEXELS_URL));
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

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}


