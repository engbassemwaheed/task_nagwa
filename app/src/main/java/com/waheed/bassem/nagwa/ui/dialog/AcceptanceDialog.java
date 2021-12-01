package com.waheed.bassem.nagwa.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.waheed.bassem.nagwa.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class AcceptanceDialog extends BottomSheetDialog {
    private final TextView mainTextView;
    private final TextView secondaryTextView;
    private final MaterialButton acceptedButton;
    private final MaterialButton deniedButton;

    public AcceptanceDialog(@NonNull Context context) {
        super(context, R.style.TransparentBottomSheetDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.acceptance_dialog, null);
        mainTextView = view.findViewById(R.id.dialog_main_text_view);
        secondaryTextView = view.findViewById(R.id.dialog_secondary_text_view);
        acceptedButton = view.findViewById(R.id.dialog_accepted_material_button);
        deniedButton = view.findViewById(R.id.dialog_denied_material_button);

        setContentView(view);
    }

    public void show(String mainString, String secondaryString, AcceptanceDialogInterface acceptanceDialogInterface) {
        mainTextView.setText(mainString);
        secondaryTextView.setText(secondaryString);

        AtomicBoolean shouldActivateDismiss = new AtomicBoolean(false);
        acceptedButton.setOnClickListener(v -> {
            shouldActivateDismiss.set(true);
            acceptanceDialogInterface.onAccepted();
            dismiss();
        });
        deniedButton.setOnClickListener(v -> {
            shouldActivateDismiss.set(true);
            acceptanceDialogInterface.onDenied();
            dismiss();
        });
        setOnDismissListener(dialog -> {
            if (!shouldActivateDismiss.get()) {
                acceptanceDialogInterface.onDismissed();
            }
        });

        super.show();
    }
}
