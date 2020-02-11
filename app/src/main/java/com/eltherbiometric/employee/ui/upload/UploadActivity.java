package com.eltherbiometric.employee.ui.upload;

import android.graphics.Color;
import android.os.Bundle;

import com.eltherbiometric.employee.R;
import com.eltherbiometric.employee.data.model.Presence;
import com.eltherbiometric.employee.data.model.Upload;
import com.eltherbiometric.employee.data.sqllite.Services;
import com.eltherbiometric.employee.retrofit.ApiInterface;
import com.eltherbiometric.employee.retrofit.RetrofitInstance;
import com.eltherbiometric.employee.ui.upload.ui.main.TabAbsenceFragment;
import com.eltherbiometric.employee.ui.upload.ui.main.TabPresenceFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eltherbiometric.employee.ui.upload.ui.main.TabAdapter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private Button btnUpload;
    private Services services;
    private ArrayList<Presence> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        btnUpload = (Button) findViewById(R.id.btnUpload);
        adapter.addFragment(new TabPresenceFragment(), "Hadir");
        adapter.addFragment(new TabAbsenceFragment(), "Absen");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void uploadData() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Uploading");
        pDialog.setCancelable(false);
        pDialog.show();

        services = new Services(UploadActivity.this);
        dataList.clear();
        dataList.addAll(services.AllDatas());
        Upload upload = new Upload(dataList);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitInstance.getRetrofitInstance(UploadActivity.this).create(ApiInterface.class);

        /*Call the method with parameter in the interface to get the employee data*/
        Call<com.eltherbiometric.employee.data.model.Response> call = service.postJson(upload);

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<com.eltherbiometric.employee.data.model.Response>() {
            @Override
            public void onResponse(Call<com.eltherbiometric.employee.data.model.Response> call, Response<com.eltherbiometric.employee.data.model.Response> response) {
                pDialog.dismiss();
                Toast.makeText(UploadActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<com.eltherbiometric.employee.data.model.Response> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(UploadActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}