package com.waheed.bassem.nagwa.ui;

public interface ActivityInterface {

    void showAcceptanceDialog(String mainString, String secondaryString, int requestCode);

    void showSnackBar(int messageId);
    void showSnackBar(String message);

    void notifyDataChanged();
}
