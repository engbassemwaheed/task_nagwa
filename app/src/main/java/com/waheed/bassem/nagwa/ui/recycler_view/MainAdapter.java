package com.waheed.bassem.nagwa.ui.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.ui.ActionInterface;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> implements AdapterInterface {

    private final Context context;
    private final ActionInterface actionInterface;
    private final ArrayList<MediaItem> mediaItems;

    public MainAdapter(Context context, ActionInterface actionInterface) {
        this.actionInterface = actionInterface;
        this.context = context;
        this.mediaItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
        return new MainViewHolder(view, this, actionInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.displayItem(mediaItems.get(position));
        holder.setListeners(mediaItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public void setMediaItems (ArrayList<MediaItem> mediaItems) {
        this.mediaItems.clear();
        this.mediaItems.addAll(mediaItems);
        notifyDataSetChanged();
    }

    public void updateItem (MediaItem mediaItem) {
        //TODO: search for a specific item and update it's state only
    }

    @Override
    public void setItemChanged(MediaItem mediaItem, int position) {
        mediaItems.remove(position);
        mediaItems.add(position, mediaItem);
        notifyItemChanged(position);
    }
}
