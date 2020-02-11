package com.eltherbiometric.employee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eltherbiometric.employee.R;
import com.eltherbiometric.employee.ui.facerecog.TinyDB;
import com.eltherbiometric.employee.ui.presence.FingerPrintActivity;
import com.eltherbiometric.employee.ui.login.LoginActivity;
import com.eltherbiometric.employee.ui.ocr.OcrActivity;
import com.eltherbiometric.employee.ui.presence.FaceRecognitionActivity;
import com.eltherbiometric.employee.ui.upload.UploadActivity;
import com.eltherbiometric.employee.utils.AndroidDatabaseManager;
import com.eltherbiometric.employee.utils.Config;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;

import org.opencv.core.Mat;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText etSetting;
    private CardView btnRegistration, btnPresensi, btnUpload;
    private CardView btnFaceRecognition, btnFingerPrintRecognition;
    private TextView tvVersion;
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;
    private String insert;
    private TinyDB tinydb;
    private Mat imageMat;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
//                    cameraView.enableView();
//                    cameraView.setOnTouchListener(FingerPrintActivity.this);
                    imageMat = new Mat();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    private String TAG = "MainActivity";
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        }
        else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        if (checkPermissions()) {
            if (!isLocationEnabled()) {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }

        // processed images
        tinydb = new TinyDB(this); // Used to store ArrayLists in the shared preferences
        if(Config.processedImages == null) Config.processedImages = new HashMap<String, Mat>();
        List<String> name_list = Hawk.get("name_list");
        if (name_list != null) {
            if (name_list.size() > 0){
                Log.d(TAG, "name List: " + name_list.size());
                for (String name: name_list) {
                    imageMat = tinydb.matFromJson(name);
                    Log.d(TAG, "initialize Image Mat " + name + " : " + imageMat);
                    Config.processedImages.put(name, imageMat);
                }
            }
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                insert = null;
            } else {
                insert = extras.getString("insert");
                if (insert.equals("success")){
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Registrasi Sukses")
                            .show();
                }
            }
        } else {
            insert = (String) savedInstanceState.getSerializable("insert");
        }

        initComponents();
        initEvents();
    }

    private void requestLocationPermission() {
        Log.w(TAG, "Location permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, 101);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        101);
            }
        };

        Snackbar.make(parentLayout, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.presence_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        btnFaceRecognition = dialogView.findViewById(R.id.btnFaceRecognition);
        btnFingerPrintRecognition = dialogView.findViewById(R.id.btnFingerPrintRecognition);

        btnFaceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaceRecognitionActivity.class);
                startActivity(intent);
            }
        });

        btnFingerPrintRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FingerPrintActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }


    private void initComponents() {
        btnRegistration = findViewById(R.id.btnRegistration);
        btnPresensi = findViewById(R.id.btnPresensi);
        btnUpload = findViewById(R.id.btnUpload);
        parentLayout = findViewById(R.id.parentLayout);
    }

    private void initEvents() {
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OcrActivity.class);
                startActivity(intent);
            }
        });

        btnPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.logout:
                Hawk.delete("user");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.setting:
                showSettingDialog();
                break;
            case R.id.db_manager:
                Intent intent1 = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_setting, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        etSetting = view.findViewById(R.id.txtSetting);
        etSetting.requestFocus();
        if (Hawk.get(Config.IpAddress) != null) etSetting.setText(Hawk.get(Config.IpAddress).toString());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String IpAddress = etSetting.getText().toString();
                        Hawk.put(Config.IpAddress, IpAddress);
                        Toast.makeText(MainActivity.this, "IP Address changed to : " + Hawk.get(Config.IpAddress), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.dismiss();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                101
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
}
