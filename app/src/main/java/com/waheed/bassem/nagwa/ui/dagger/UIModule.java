package com.waheed.bassem.nagwa.ui.dagger;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.waheed.bassem.nagwa.ui.ActionInterface;
import com.waheed.bassem.nagwa.ui.view_model.MainViewModel;
import com.waheed.bassem.nagwa.ui.dialog.AcceptanceDialog;
import com.waheed.bassem.nagwa.ui.recycler_view.MainAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class UIModule {

    private final Context context;
    private final ActionInterface actionInterface;
    private final ViewModelStoreOwner viewModelStoreOwner;

    public UIModule (Context context, ViewModelStoreOwner viewModelStoreOwner, ActionInterface actionInterface) {
        this.context = context;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.actionInterface = actionInterface;
    }

    @Provides
    public MainViewModel provideMainViewModel () {
        return new ViewModelProvider(viewModelStoreOwner).get(MainViewModel.class);
    }


    @Provides
    public MainAdapter provideMainAdapter () {
        return new MainAdapter(context, actionInterface);
    }

    @Provides
    public AcceptanceDialog provideAcceptanceDialog () {
        return new AcceptanceDialog(context);
    }


}
