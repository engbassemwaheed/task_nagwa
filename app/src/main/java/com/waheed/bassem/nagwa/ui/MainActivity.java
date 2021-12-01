package com.waheed.bassem.nagwa.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.ui.dagger.DaggerUIComponent;
import com.waheed.bassem.nagwa.ui.dagger.UIComponent;
import com.waheed.bassem.nagwa.ui.dagger.UIModule;
import com.waheed.bassem.nagwa.ui.dialog.AcceptanceDialog;
import com.waheed.bassem.nagwa.ui.dialog.AcceptanceDialogInterface;
import com.waheed.bassem.nagwa.ui.recycler_view.MainAdapter;
import com.waheed.bassem.nagwa.ui.view_model.MainViewModel;

public class MainActivity extends AppCompatActivity implements ActionInterface, ActivityInterface {

    private static final String TAG = "MainActivity";

    private MainAdapter mainAdapter;
    private LinearLayout emptyLinearLayout;
    private RecyclerView recyclerView;
    private MainViewModel mainViewModel;
    private AcceptanceDialog acceptanceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UIComponent daggerUIComponent = DaggerUIComponent.builder()
                .uIModule(new UIModule(this, this, this))
                .build();

        //view model
        mainViewModel = daggerUIComponent.getMainViewModel();
        mainViewModel.setActivityInterface(this);

        //adapter
        mainAdapter = daggerUIComponent.getMainAdapter();

        //views
        recyclerView = findViewById(R.id.items_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

        emptyLinearLayout = findViewById(R.id.empty_linear_layout);

        acceptanceDialog = daggerUIComponent.getAcceptanceDialog();

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
        mainViewModel.downloadFile(this, mediaItem);
    }

    @Override
    public void onOpenRequested(MediaItem mediaItem) {
        Log.e(TAG, "onOpenRequested: mediaItem id = " + mediaItem.getId());
        mainViewModel.openFile(this, mediaItem);
    }

    @Override
    public void onDeleteRequested(MediaItem mediaItem) {
        Log.e(TAG, "onOpenRequested: mediaItem id = " + mediaItem.getId());
        mainViewModel.deleteFile(this, mediaItem);
    }


    @Override
    public void showAcceptanceDialog(String mainString, String secondaryString, int requestCode) {
        Log.e(TAG, "showAcceptanceDialog");
        acceptanceDialog.show(mainString, secondaryString, new AcceptanceDialogInterface() {
            @Override
            public void onAccepted() {
                Log.e(TAG, "onAccepted:");
                mainViewModel.onAcceptanceDialogResult(MainActivity.this, requestCode, true);
            }

            @Override
            public void onDenied() {
                Log.e(TAG, "onDenied:");
                mainViewModel.onAcceptanceDialogResult(MainActivity.this, requestCode, false);
            }

            @Override
            public void onDismissed() {
                Log.e(TAG, "onDismissed:");
                mainViewModel.onAcceptanceDialogResult(MainActivity.this, requestCode, false);
            }
        });
    }

    @Override
    public void showSnackBar(int messageId) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.snackbar_anchor), getString(messageId), Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.snack_bar_rectangle, getTheme()));
        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mainViewModel.onRequestedPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (acceptanceDialog.isShowing()) acceptanceDialog.dismiss();
    }
}