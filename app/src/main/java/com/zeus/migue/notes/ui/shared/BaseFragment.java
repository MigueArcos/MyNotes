package com.zeus.migue.notes.ui.shared;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected abstract void initializeViews(View rootView);
    protected Context getAppContext(){
        return getContext() != null ? getContext().getApplicationContext() : getContext();
    }
}
