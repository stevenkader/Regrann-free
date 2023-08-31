package com.jaredco.regrann.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.jaredco.regrann.R;
import com.jaredco.regrann.util.OverlayPermissionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckPermissions2 extends AppCompatActivity {

    private SwitchCompat nameSwitch, readSwitch;
    private EditText namePrefixEdit, msgPrefixEdit;
    SharedPreferences sharedPreferences;
    private CheckPermissions2 _this;
    private final boolean openSettings = false;
    Bundle _savedInstanceState;
    private boolean allNeededApproved = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _savedInstanceState = savedInstanceState;
        _this = this;


        setContentView(R.layout.activity_checkpermissions2);


        if (checkPermissions(true))
            finish();

        final Button button = findViewById(R.id.goBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkPermissions(false);
                // Code here executes on main thread after user presses button
            }
        });


        //   startActivity(new Intent(this, showEULA.class));

        //    checkPermissions();
        //     startPowerSaverIntent(_this) ;


        Spanned policy = Html.fromHtml(getString(R.string.eula_text));
        TextView termsOfUse = findViewById(R.id.txtEULA);
        termsOfUse.setText(policy);
        termsOfUse.setMovementMethod(LinkMovementMethod.getInstance());

    }


    @Override
    public void onBackPressed() {
        if (allNeededApproved) {
            super.onBackPressed();
        } else {


            new AlertDialog.Builder(this)
                    .setMessage("Not all of the permissions have been approved. Approve them now?")
                    .setTitle("App Permissions Need Approval!")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    checkPermissions(false);

                                }
                            })

                    .create().show();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (checkPermissions(true) && Settings.canDrawOverlays(this))
            showConfirmDialog();


    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private boolean checkPermissions(boolean quicktest) {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();


        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE");

        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("READ_PHONE_STATE");


        addPermission(permissionsList, Manifest.permission.ANSWER_PHONE_CALLS);
        addPermission(permissionsList, Manifest.permission.CALL_PHONE);

        addPermission(permissionsList, Manifest.permission.SYSTEM_ALERT_WINDOW);


        addPermission(permissionsList, Manifest.permission.RECEIVE_BOOT_COMPLETED);
        //   addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION);

        if (quicktest) {
            if (permissionsNeeded.size() == 0) {
                //  showConfirmDialog();
                //  finish();
                return true;
            }


        }


        if (!quicktest && permissionsNeeded.size() > 0) {

            //   addPermission(permissionsList, Manifest.permission.BLUETOOTH_CONNECT);
            RegrannApp.sendEvent("cp_request_permissions6");
            ActivityCompat.requestPermissions(_this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
        return false;


    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            return false;
        } else
            return true;


    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(_this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial


            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

            perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);


            boolean userClickedDeny = false;
            // Fill with results
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
                String permission = permissions[i];
                Log.d("tag", "permission i : " + permission);

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        boolean showRationale = false;
                        showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            userClickedDeny = true;
                        }
                    }


                }
            }


            if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    &&
                    perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted
                // insertDummyContact();
                allNeededApproved = true;


                Log.d("tag", "allpermissionsgranted");
                RegrannApp.sendEvent("cp_permission_granted6");


                sharedPreferences = _this.getSharedPreferences("prefs", MODE_PRIVATE);

                boolean active = sharedPreferences.getBoolean("app_active", true);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("app_active", true);

                editor.commit();


                if (Build.BRAND.equalsIgnoreCase("xiaomi")) {


                    new AlertDialog.Builder(this)
                            .setMessage("On Xiaomi devices you need to give the app 'Startup Permission'")
                            .setTitle("App Permissions Need Approval!")
                            .setIconAttribute(android.R.attr.alertDialogIcon)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                finish();
                                            }

                                        }
                                    })

                            .create().show();


                }


                if (!Settings.canDrawOverlays(this)) {

                    try {
                        new AlertDialog.Builder(this)
                                .setMessage("The app requires the [Display over other Apps] permission.  On the next screen click on the Repost app and then toggle the switch.")
                                .setTitle("Need one more permission")

                                .setCancelable(false)
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                                OverlayPermissionManager overlayPermissionManager = new OverlayPermissionManager(_this);
                                                if (!overlayPermissionManager.isGranted()) {
                                                    overlayPermissionManager.requestOverlay();
                                                }

                                                //       Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                //              Uri.parse("package:" + getPackageName()));
                                                //    startActivity(intent);
                                                //  finish();

                                            }
                                        })

                                .create().show();
                    } catch (Exception e) {
                    }

                    // ask for setting

                }
                /**
                 Calldorado.requestOverlayPermission(this, new Calldorado.OverlayCallback() {
                 public void onPermissionFeedback(boolean overlayIsGranted) {
                 // Calldorado.start(MainActivity._this);





                 }
                 });
                 **/


            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void showConfirmDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setMessage("All Permissions Now Granted.  Thanks.")
                    .setTitle("All Done")

                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();

                                }
                            })

                    .create().show();
        } catch (Exception e) {
        }
    }


}
