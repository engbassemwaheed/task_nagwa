package com.waheed.bassem.nagwa.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.Constants;
import com.waheed.bassem.nagwa.data.MediaItem;

public class FileOpenUtils {

    public static Intent getIntent(Context context, MediaItem mediaItem) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(NagwaFileUtils.getDataFile(mediaItem).getAbsolutePath());
        intent.setDataAndType(uri, FileOpenUtils.getFileMIME(mediaItem.getExtension()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (canBeOpened(context, intent)) {
            return intent;
        } else {
            return null;
        }
    }

    private static String getFileMIME(String extension) {
        switch (extension) {
            case Constants.FileExtension.VIDEO:
                return "video/mp4";
            case Constants.FileExtension.PDF:
                return "application/*";
//            return "application/pdf";
            default:
                return "*/*";
        }
    }

    private static boolean canBeOpened(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return intent.resolveActivity(packageManager) != null;
    }

    public static String getErrorMessage(Context context, String extension) {
        return context.getString(R.string.cant_find_app)
                + " " + extension + ", "
                + context.getString(R.string.download_from_store);
    }


}
