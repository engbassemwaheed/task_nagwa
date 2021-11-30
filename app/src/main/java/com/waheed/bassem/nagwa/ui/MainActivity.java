package com.waheed.bassem.nagwa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.ui.recycler_view.MainAdapter;

public class MainActivity extends AppCompatActivity implements ActionInterface {

    private static final String TAG = "MainActivity";

    private MainAdapter mainAdapter;
    private LinearLayout emptyLinearLayout;
    private RecyclerView recyclerView;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //view model
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        //adapter
        mainAdapter = new MainAdapter(this, this);

        //views
        recyclerView = findViewById(R.id.items_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

        emptyLinearLayout = findViewById(R.id.empty_linear_layout);

        //view model observers
        setViewModelObservers();

        //start getting the data
        mainViewModel.getMediaItems(this);
    }

    private void setViewModelObservers() {
        mainViewModel.getMediaItemsMutableLiveData().observe(this, mediaItems -> {
            Log.e(TAG, "onChanged: mediaItems size = " + mediaItems.size());
            if (mediaItems.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyLinearLayout.setVisibility(View.GONE);
                mainAdapter.setMediaItems(mediaItems);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        mainViewModel.getToDownloadMutableLiveData().observe(this, integer -> mainAdapter.notifyItemChanged(integer));

        mainViewModel.getProgressMutableLiveData().observe(this, integerMediaItemPair ->
                mainAdapter.notifyItemChanged(integerMediaItemPair.first, integerMediaItemPair.second));
    }

    @Override
    public void onDownloadRequested(MediaItem mediaItem) {
        Log.e(TAG, "onDownloadRequested: mediaItem = " + mediaItem.getId());
        mainViewModel.downloadFile(mediaItem);
    }

    @Override
    public void onOpenRequested(MediaItem mediaItem) {
        Log.e(TAG, "onOpenRequested: mediaItem id = " + mediaItem.getId());
        mainViewModel.openFile(this, mediaItem);
    }

}