package com.zeus.migue.notes.ui.shared;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.infrastructure.utils.Utils;

public class WebUrlPopupMeu extends PopupMenu {

    public WebUrlPopupMeu(Context context, View anchor, String url) {
        super(context, anchor, Gravity.CENTER);
        getMenuInflater().inflate(R.menu.popup_menu_url, getMenu());
        setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.open_url:
                    openLinkInBrowser(context, url);
                    return true;

                case R.id.copy_to_clipboard:
                    Utils.copyTextToClipboard(context, url);
                    return true;

                default:
                    return true;
            }
        });
    }

    private void openLinkInBrowser(Context context, String url){
        Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
        defaultBrowser.setData(Uri.parse(url));
        context.startActivity(defaultBrowser);
    }
}