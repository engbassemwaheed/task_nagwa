package com.waheed.bassem.nagwa.utils;

import android.os.Build;
import android.os.Environment;

import com.waheed.bassem.nagwa.data.MediaItem;

import java.io.File;

public class NagwaFileUtils {

    private static final String ENCLOSURE_FOLDER = "Nagwa downloads";

    public static File getDataFile(MediaItem mediaItem) {
        String path = "/" + mediaItem.getName() + mediaItem.getExtension();

        return new File(getContainerFolder().getAbsoluteFile(), path);
    }


    public static File getContainerFolder() {
        File containerFolder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            containerFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile(), ENCLOSURE_FOLDER);
        } else {
            containerFolder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), ENCLOSURE_FOLDER);
        }
        containerFolder.mkdir();
        return containerFolder;
    }

    public static boolean isExist(MediaItem mediaItem) {
        File file = getDataFile(mediaItem);
        return file.exists() && file.length() != 0;
    }

    public static void deleteFile(MediaItem mediaItem) {
        File file = getDataFile(mediaItem);
        if (file.exists()) file.delete();
    }
}
