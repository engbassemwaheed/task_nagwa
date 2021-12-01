package com.waheed.bassem.nagwa.ui.dagger;

import com.waheed.bassem.nagwa.ui.view_model.MainViewModel;
import com.waheed.bassem.nagwa.ui.dialog.AcceptanceDialog;
import com.waheed.bassem.nagwa.ui.recycler_view.MainAdapter;

import dagger.Component;

@Component(modules = UIModule.class)
public interface UIComponent {

    MainAdapter getMainAdapter();

    AcceptanceDialog getAcceptanceDialog();

    MainViewModel getMainViewModel();
}
