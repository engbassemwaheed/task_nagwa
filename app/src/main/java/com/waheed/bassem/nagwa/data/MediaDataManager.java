package com.waheed.bassem.nagwa.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.waheed.bassem.nagwa.data.Constants.JsonConstants;

public class MediaDataManager {


    private static MediaDataManager mediaDataManager;

    private MediaDataManager() {
    }

    public static MediaDataManager getInstance() {
        if (mediaDataManager == null) {
            mediaDataManager = new MediaDataManager();
        }
        return mediaDataManager;
    }

    public ArrayList<MediaItem> getMediaItems(Context context) {
        ArrayList<MediaItem> result = new ArrayList<>();
        String jsonString = loadJSON(context);

        if (jsonString != null) {
            result.addAll(parseMediaItems(jsonString));
        }

        return result;
    }

    private ArrayList<MediaItem> parseMediaItems(String jsonString) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.optString(JsonConstants.NAME, JsonConstants.DEFAULT_STRING);
                String url = jsonObject.optString(JsonConstants.URL, JsonConstants.DEFAULT_STRING);
                String type = jsonObject.optString(JsonConstants.TYPE, JsonConstants.DEFAULT_STRING);
                int id = jsonObject.optInt(JsonConstants.ID, JsonConstants.DEFAULT_INT);

                mediaItems.add(new MediaItem(id, type, name, url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return mediaItems;
    }

    private String loadJSON(Context context) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open(Constants.DATA_FILE_NAME);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
