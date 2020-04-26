package com.zeus.migue.notes.ui.activity_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.activity_login.LoginActivity;
import com.zeus.migue.notes.ui.activity_main.fragments.accounts.AccountsFragment;
import com.zeus.migue.notes.ui.activity_main.fragments.clipboard.ClipboardFragment;
import com.zeus.migue.notes.ui.activity_main.fragments.notes.DeletedNotesFragment;
import com.zeus.migue.notes.ui.activity_main.fragments.notes.NotesFragment;
import com.zeus.migue.notes.ui.shared.BaseActivity;

import java.util.Calendar;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MainActivityViewModel mainActivityViewModel;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private int currentFragmentId;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*checkPermissions(BaseActivity.PERMISSION_REQUEST_WRITE_EXTERNAL, Manifest.permission.WRITE_EXTERNAL_STORAGE, () -> {

        });*/
        initializeViews();
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        getFragment();
        mainActivityViewModel.getUserInformation().observe(this, userInfo -> {
            View header = navigationView.getHeaderView(0);
            TextView username = header.findViewById(R.id.header_username);
            TextView email = header.findViewById(R.id.header_email);
            if (userInfo.isLoggedIn()) {
                username.setText(userInfo.getUserName());
                email.setText(userInfo.getEmail());
                email.setTextSize(15);
                navigationView.getMenu().findItem(R.id.logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.login).setVisible(false);
            } else {
                email.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                navigationView.getMenu().findItem(R.id.logout).setVisible(false);
                navigationView.getMenu().findItem(R.id.login).setVisible(true);
            }
        });
    }

    private void getFragment() {
        MenuItem item = null;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            if (fragment instanceof NotesFragment) {
                item = navigationView.getMenu().findItem(R.id.notes);
            } else if (fragment instanceof DeletedNotesFragment) {
                item = navigationView.getMenu().findItem(R.id.deleted_notes);
            } else if (fragment instanceof ClipboardFragment) {
                item = navigationView.getMenu().findItem(R.id.clipboard);
            } else if (fragment instanceof AccountsFragment) {
                item = navigationView.getMenu().findItem(R.id.clipboard);
            }
        } else {
            switch (mainActivityViewModel.getUserPreferences().getLastSelectedFragment()) {
                case 1:
                    item = navigationView.getMenu().findItem(R.id.notes);
                    fragment = NotesFragment.newInstance();
                    break;
                case 2:
                    item = navigationView.getMenu().findItem(R.id.deleted_notes);
                    fragment = DeletedNotesFragment.newInstance();
                    break;
                case 3:
                    item = navigationView.getMenu().findItem(R.id.clipboard);
                    fragment = ClipboardFragment.newInstance();
                    break;
                case 4:
                    item = navigationView.getMenu().findItem(R.id.accounts);
                    fragment = AccountsFragment.newInstance();
                    break;
            }
        }
        item.setChecked(true);
        currentFragmentId = item.getItemId();
        getSupportActionBar().setTitle(item.getTitle());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Utils.hideKeyboard(this);
        if (item.getItemId() == currentFragmentId) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        switch (item.getItemId()) {
            case R.id.login:
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                return true;
            case R.id.notes:
                currentFragment = NotesFragment.newInstance();
                mainActivityViewModel.getUserPreferences().setLastSelectedFragment(1);
                currentFragmentId = item.getItemId();
                break;
            case R.id.deleted_notes:
                currentFragment = DeletedNotesFragment.newInstance();
                mainActivityViewModel.getUserPreferences().setLastSelectedFragment(2);
                currentFragmentId = item.getItemId();
                break;
            case R.id.clipboard:
                currentFragment = ClipboardFragment.newInstance();
                mainActivityViewModel.getUserPreferences().setLastSelectedFragment(3);
                currentFragmentId = item.getItemId();
                break;
            case R.id.accounts:
                currentFragment = AccountsFragment.newInstance();
                mainActivityViewModel.getUserPreferences().setLastSelectedFragment(4);
                currentFragmentId = item.getItemId();
                break;
            case R.id.logout:
                new AlertDialog.Builder(this).setMessage(getString(R.string.main_activity_menu_logout_confirmation))
                        .setPositiveButton(R.string.dialog_ok_message, (dialog, id) -> {
                            drawer.closeDrawer(GravityCompat.START);
                            mainActivityViewModel.logout();
                        })
                        .setNegativeButton(R.string.dialog_cancel_message, null).show();
                return true;
            case R.id.about_app:
                final BottomSheetDialog aboutAppDialog = new BottomSheetDialog(this);
                final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
                ((TextView) bottomSheetLayout.findViewById(R.id.about_app_copyright))
                        .setText(String.format(getString(R.string.about_app_copyright), Calendar.getInstance().get(Calendar.YEAR)));
                View.OnClickListener doNothingOnClickHandler = view -> aboutAppDialog.dismiss();
                bottomSheetLayout.findViewById(R.id.button_close).setOnClickListener(doNothingOnClickHandler);
                bottomSheetLayout.findViewById(R.id.button_ok).setOnClickListener(doNothingOnClickHandler);

                aboutAppDialog.setContentView(bottomSheetLayout);
                aboutAppDialog.show();
                return true;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (((SimpleSearchView) findViewById(R.id.searchView)).onBackPressed() && (currentFragment instanceof NotesFragment || currentFragment instanceof DeletedNotesFragment || currentFragment instanceof ClipboardFragment)) {
            return;
        }
        super.onBackPressed();
    }
}
