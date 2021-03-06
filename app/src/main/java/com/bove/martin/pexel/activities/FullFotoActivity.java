package com.bove.martin.pexel.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class FullFotoActivity extends AppCompatActivity {
    private static final String IMAGES_FOLDER_NAME = "TEMP_PEXEL_FOLDER";
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

    private boolean downloadForSharing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_foto);

        initVars();
        loadContentFormBundle();

        viewModel.getStoragePermission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean != null) {
                    if (!aBoolean) {
                        Toast.makeText(FullFotoActivity.this, R.string.permissionErrorStorage, Toast.LENGTH_LONG).show();
                    } else {
                        donwloadFoto(downloadForSharing);
                    }
                }
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


    // Share Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                donwloadFoto(true);
                return false;
            case R.id.action_download:
                donwloadFoto(false);
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    private void donwloadFoto(boolean isForSharing) {
        downloadForSharing = isForSharing;

        if(viewModel.getStoragePermission().getValue() != null && viewModel.getStoragePermission().getValue()) {
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
                            shareBitmap(resource, "tempFilename", downloadForSharing) ;
                            hideProgressBar();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            Toast.makeText(FullFotoActivity.this, R.string.loadImageError, Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            hideProgressBar();
                        }
                    });

        } else {
            checkStoragePermission();
        }
    }


    private void shareBitmap (Bitmap bitmap, String fileName, boolean downloadForSharing) {

        try {
            Uri imageUri = saveImage(bitmap, fileName);

            if(imageUri != null) {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);

                final Intent intentDown = new Intent(Intent.ACTION_VIEW);
                intentDown.setType("image/*");
                intentDown.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentDown.putExtra(Intent.EXTRA_STREAM, imageUri);

                if(downloadForSharing) {
                    startActivity(intent);
                } else {
                    startActivity(intentDown);
                }
            }

        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private Uri saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;
        Uri imageUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".jpg");
            fos = new FileOutputStream(image);
            imageUri =  FileProvider.getUriForFile(this, "com.bove.martin.pexel.provider", image);
        }

        saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        return imageUri;
    }

    private void checkStoragePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        viewModel.setSoragePermission(true);
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        viewModel.setSoragePermission(false);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
}


