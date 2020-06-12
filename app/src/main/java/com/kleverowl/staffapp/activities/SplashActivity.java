package com.kleverowl.staffapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kleverowl.staffapp.R;
import com.kleverowl.staffapp.home.views.HomeActivity;
import com.kleverowl.staffapp.login.views.LoginActivity;
import com.kleverowl.staffapp.login.views.SelectStaffActivity;
import com.kleverowl.staffapp.utils.PreferenceConnector;
import com.kleverowl.staffapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.*;

/**
 * @Created By Vinod Kulkarni
 * @Date 17/1/2017
 */


public class SplashActivity extends AppCompatActivity {

    private Handler splashHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Utils.createDirectory();

        splashHandler = new Handler();

        checkForPermissions();
    }


    private void startToShowSplashFinally() {
        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null
                        && !PreferenceConnector.readString(SplashActivity.this, PreferenceConnector.USER_DETAILS, "").isEmpty()){
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, SelectStaffActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashHandler != null) {
            splashHandler.removeCallbacksAndMessages(null);
            splashHandler = null;
        }
    }

    static final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @SuppressLint("NewApi")
    private void checkForPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();


        if (!addPermission(permissionsList, READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");
        if (!addPermission(permissionsList, WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!addPermission(permissionsList, CALL_PHONE))
            permissionsNeeded.add("Call Phone");

        if (permissionsList.size() > 0) {
           /* if (permissionsNeeded.size() > 0) {
                // Need Rationale
                StringBuilder builder = new StringBuilder("You need to grant access to ").append(permissionsNeeded.get(0));
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    builder.append(", ").append(permissionsNeeded.get(i));
                builder.append("\nOR\n").append("Enable it from App Info Settings to use this App.");

                AlertDialog alertDialog = new AlertDialog.Builder(this, android.R.style.Theme_Dialog).create();
                alertDialog.setTitle(getString(R.string.access_permissions));
                alertDialog.setMessage(builder.toString());
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(SplashActivity.this);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //showSnackBarShort(getString(R.string.some_permissions_are_denied), Color.RED);
                        finish();
                    }
                });
                alertDialog.show();
                return;
            }*/
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        } else {
            startToShowSplashFinally();
        }
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivityForResult(i, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
    }

    @SuppressLint("NewApi")
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                checkForPermissions();
            }
            break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startToShowSplashFinally();
                } else {
                    checkForPermissions();
                    //  showSnackBarShort(getString(R.string.some_permissions_are_denied), Color.RED);
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (splashHandler != null) {
            splashHandler.removeCallbacksAndMessages(null);
        }
    }
}
