package com.eltherbiometric.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.eltherbiometric.employee.R;
import com.eltherbiometric.employee.ui.login.LoginActivity;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        TextView tvVersion = findViewById(R.id.tvVersion);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText(String.format("Version %s", versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 4000L); //4000 L = 4 detik
    }
}
