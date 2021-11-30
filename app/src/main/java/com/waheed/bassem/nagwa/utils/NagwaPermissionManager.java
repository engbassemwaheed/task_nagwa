package com.waheed.bassem.nagwa.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.waheed.bassem.nagwa.R;

public class NagwaPermissionManager {

    public static boolean checkStoragePermission(AppCompatActivity activity) {
        boolean isGranted = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return isGranted && Build.VERSION.SDK_INT < Build.VERSION_CODES.R;
    }

    public static void askForStoragePermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCodes.STORAGE_PERMISSION);
    }

    public static Pair<String, String> getDescriptionDialogTexts(AppCompatActivity appCompatActivity) {
        String mainString = appCompatActivity.getString(R.string.why_storage_permission);
        String secondaryString = appCompatActivity.getString(R.string.storing_location);
        secondaryString += " \"" + NagwaFileManager.getContainerFolder().getAbsoluteFile() + "\"";
        return new Pair<>(mainString, secondaryString);
    }

    public static int getDeniedPermissionMessage() {
        return R.string.denied_permission_cant_download_file;
    }

    public static Boolean isPermissionGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCodes.STORAGE_PERMISSION) {
            return grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        } else {
            return null;
        }
    }
}
