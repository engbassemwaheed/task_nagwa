package com.waheed.bassem.nagwa.ui.recycler_view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.Constants;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.ui.ActionInterface;

class MainViewHolder extends RecyclerView.ViewHolder {

    private final ActionInterface actionInterface;
    private final AdapterInterface adapterInterface;

    private final MaterialCardView mainCardView;
    private final View stateIndicatorView;
    private final TextView nameTextView;
    private final TextView typeTextView;
    private final TextView stateTextView;
    private final ProgressBar downloadProgressBar;
    private final LinearLayout controlLinearLayout;
    private final ImageButton downloadImageButton;
    private final ImageButton openImageButton;

    MainViewHolder(@NonNull View itemView, AdapterInterface adapterInterface, ActionInterface actionInterface) {
        super(itemView);
        this.actionInterface = actionInterface;
        this.adapterInterface = adapterInterface;

        mainCardView = itemView.findViewById(R.id.main_card_view);
        stateIndicatorView = itemView.findViewById(R.id.indicator_view);
        nameTextView = itemView.findViewById(R.id.item_name_text_view);
        typeTextView = itemView.findViewById(R.id.item_type_text_view);
        stateTextView = itemView.findViewById(R.id.download_state_text_view);
        downloadProgressBar = itemView.findViewById(R.id.download_progress_bar);
        controlLinearLayout = itemView.findViewById(R.id.control_linear_layout);
        downloadImageButton = itemView.findViewById(R.id.download_image_button);
        openImageButton = itemView.findViewById(R.id.open_image_button);
    }

    void displayItem(MediaItem mediaItem) {

        nameTextView.setText(mediaItem.getName());

        displayType(mediaItem.getType());

        displayDownloadState(mediaItem);

        if (mediaItem.isExpanded()) {
            controlLinearLayout.setVisibility(View.VISIBLE);
        } else {
            controlLinearLayout.setVisibility(View.GONE);
        }
    }

    void setListeners(MediaItem mediaItem, int position) {
        mainCardView.setOnClickListener(v -> {
            mediaItem.invertExpanded();
            adapterInterface.setItemChanged(mediaItem, position);
        });

        downloadImageButton.setOnClickListener(v -> actionInterface.onDownloadRequested(mediaItem));

        openImageButton.setOnClickListener(v -> actionInterface.onOpenRequested(mediaItem));
    }

    private void displayType(String type) {
        switch (type) {
            case Constants.FileType.VIDEO:
                nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_file_video, 0);
                typeTextView.setText(type);
                break;
            case Constants.FileType.PDF:
                nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_file_pdf, 0);
                typeTextView.setText(type);
                break;
            default:
                nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_file_unknown, 0);
                typeTextView.setText(R.string.unknown);
                break;
        }
    }

    private void displayDownloadState (MediaItem mediaItem) {
        switch (mediaItem.getDownloadState()) {
            case Constants.DownloadState.PENDING_DOWNLOAD:
                setDownloadPending();
                break;
            case Constants.DownloadState.DOWNLOADING:
                setDownloading(mediaItem.getDownloadProgress());
                break;
            case Constants.DownloadState.DOWNLOADED:
                setDownloaded();
                break;
            case Constants.DownloadState.ERROR_DOWNLOAD:
                setDownloadError();
                break;
            default: // Constants.DownloadState.NOT_DOWNLOADED:
                setDownloadNot();
                break;
        }
    }

    private void setDownloading(int progress) {
        String progressString = progress + "%";
        stateTextView.setText(progressString);
        stateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_downloading, 0, 0, 0);

        downloadProgressBar.setVisibility(View.VISIBLE);
        downloadProgressBar.setIndeterminate(false);
        downloadProgressBar.setProgress(progress);

        stateIndicatorView.setBackgroundResource(R.color.yellow);
    }

    private void setDownloaded() {
        stateTextView.setText(R.string.downloaded);
        stateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_download_done, 0, 0, 0);
        downloadProgressBar.setVisibility(View.GONE);

        stateIndicatorView.setBackgroundResource(R.color.green);
    }

    private void setDownloadPending() {
        stateTextView.setText(R.string.download_pending);
        stateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_downloading, 0, 0, 0);
        downloadProgressBar.setVisibility(View.VISIBLE);
        downloadProgressBar.setIndeterminate(true);

        stateIndicatorView.setBackgroundResource(R.color.yellow);
    }

    private void setDownloadNot() {
        stateTextView.setText(R.string.not_downloaded);
        stateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_not_downloaded, 0, 0, 0);
        downloadProgressBar.setVisibility(View.GONE);

        stateIndicatorView.setBackgroundResource(R.color.theme_gray);
    }

    private void setDownloadError() {
        stateTextView.setText(R.string.download_error);
        stateTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
        downloadProgressBar.setVisibility(View.GONE);

        stateIndicatorView.setBackgroundResource(R.color.error_red);
    }


}
