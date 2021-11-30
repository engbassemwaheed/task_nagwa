package com.waheed.bassem.nagwa.utils;

import android.os.Build;
import android.os.Environment;

import com.waheed.bassem.nagwa.data.MediaItem;

import java.io.File;

public class Utilities {

    private static final String ENCLOSURE_FOLDER = "Nagwa task downloads";

    public static File getDataFile(MediaItem mediaItem) {
        String path = "/" + mediaItem.getName() + mediaItem.getExtension();

        File containerFolder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            containerFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile(), ENCLOSURE_FOLDER);
        } else {
            containerFolder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), ENCLOSURE_FOLDER);
        }
        containerFolder.mkdir();
        return new File(containerFolder.getAbsoluteFile(), path);
    }

}
