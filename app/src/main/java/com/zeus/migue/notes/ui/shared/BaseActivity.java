package com.zeus.migue.notes.ui.shared;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.Utils;

public abstract  class BaseActivity extends AppCompatActivity {
    private boolean userClickedNeverAskMeAgain = false;
    protected AlertDialog currentPermissionDialogDenied;
    private PermissionRequest currentPermissionRequest;

    public static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    public static final int PERMISSION_REQUEST_WRITE_EXTERNAL = 2;
    private ILogger logger;
    public abstract void initializeViews();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger = Logger.getInstance(this);
        Thread.UncaughtExceptionHandler androidExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> {
            logger.log(paramThrowable.getMessage());
            assert androidExceptionHandler != null;
            androidExceptionHandler.uncaughtException(paramThread, paramThrowable);
        });
    }
    protected void checkPermissions(int requestCode, String permission, PermissionRequest permissionRequest){
        this.currentPermissionRequest = permissionRequest;
        userClickedNeverAskMeAgain = false;
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            currentPermissionRequest.onPermissionGranted();
        }else{
            switch (requestCode){
                case PERMISSION_REQUEST_FINE_LOCATION:
                    currentPermissionDialogDenied = new AlertDialog.Builder(this).
                            setTitle(R.string.permission_access_location_title).
                            setMessage(R.string.main_activity_request_location_permission).
                            setNegativeButton(R.string.close_app_message, (dialog, which) -> this.finish()).
                            setPositiveButton(R.string.dialog_ok_message, (d, w) -> {
                                if (userClickedNeverAskMeAgain) {
                                    Utils.openAppSettings(this);
                                } else {
                                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
                                }
                            }).
                            setCancelable(false).create();
                    break;
                case PERMISSION_REQUEST_WRITE_EXTERNAL:
                    currentPermissionDialogDenied = new AlertDialog.Builder(this).
                            setTitle(R.string.permission_write_external_title).
                            setMessage(R.string.main_activity_write_external_permission).
                            setNegativeButton(R.string.close_app_message, (dialog, which) -> this.finish()).
                            setPositiveButton(R.string.dialog_ok_message, (d, w) -> {
                                if (userClickedNeverAskMeAgain) {
                                    Utils.openAppSettings(this);
                                } else {
                                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                                }
                            }).
                            setCancelable(false).create();
                    break;
            }
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }
    public interface PermissionRequest{
        void onPermissionGranted();
    }
    public interface ExtendedPermissionRequest extends PermissionRequest{
        void onPermissionDenied();
        void onPermissionDeniedForever();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String permissionRequested = permissions[0];
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            currentPermissionDialogDenied.dismiss();
            if (currentPermissionRequest != null) currentPermissionRequest.onPermissionGranted();
        }
        //User clicked never ask me again
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                permissionRequested)) {
            userClickedNeverAskMeAgain = true;
            //if (currentPermissionRequest != null) currentPermissionRequest.onPermissionDeniedForever();
            if (currentPermissionRequest instanceof ExtendedPermissionRequest){
                ((ExtendedPermissionRequest) currentPermissionRequest).onPermissionDeniedForever();
            }else {
                currentPermissionDialogDenied.show();
            }

        } else {
            // permission denied, boo!
            //if (currentPermissionRequest != null) currentPermissionRequest.onPermissionDenied();
            if (currentPermissionRequest instanceof ExtendedPermissionRequest){
                ((ExtendedPermissionRequest) currentPermissionRequest).onPermissionDenied();
            }else {
                currentPermissionDialogDenied.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userClickedNeverAskMeAgain) {
            currentPermissionDialogDenied.show();
        }
    }
}
