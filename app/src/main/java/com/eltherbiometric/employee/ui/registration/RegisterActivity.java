package com.eltherbiometric.employee.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eltherbiometric.employee.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ArrayList<String> OcrDatas;
    private EditText etProvince, etNik, etName, etUsername, etPassword, etDivision, etBirth, etGender, etAddress, etReligion, etStatus, etWork, etNationality, etExpire;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                OcrDatas = null;
            } else {
                OcrDatas= extras.getStringArrayList("ocr_datas");
            }
        } else {
            OcrDatas = (ArrayList<String>) savedInstanceState.getSerializable("ocr_datas");
        }

        initComponents();
        initEvents();
        initDataOcr();
    }

    private void initEvents() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nik = etNik.getText().toString();
                String name = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String division = etDivision.getText().toString();
                String birth = etBirth.getText().toString();
                String gender = etGender.getText().toString();
                String address = etAddress.getText().toString();
                String religion = etReligion.getText().toString();
                String status = etStatus.getText().toString();
                String work = etWork.getText().toString();
                String nationality = etNationality.getText().toString();
                String expire = etExpire.getText().toString();

//                ArrayList<String> user = new ArrayList<>();
                Map<String, String> user = new HashMap<String, String>();
                user.put("nik", nik);
                user.put("name", name);
                user.put("date", birth);
                user.put("time", gender);
                user.put("method", address);
                user.put("religion", religion);
                user.put("status", status);
                user.put("work", work);
                user.put("nationality", nationality);
                user.put("expire", expire);

                Intent intent = new Intent(RegisterActivity.this, FaceRecognitionActivity.class);
                intent.putExtra("nik", nik);
                intent.putExtra("name", name);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("division", division);
                intent.putExtra("date", birth);
                intent.putExtra("time", gender);
                intent.putExtra("method", address);
                intent.putExtra("religion", religion);
                intent.putExtra("status", status);
                intent.putExtra("work", work);
                intent.putExtra("nationality", nationality);
                intent.putExtra("expire", expire);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initDataOcr() {
        etProvince.setText(String.format("%s %s", OcrDatas.get(0), OcrDatas.get(1)));
        etNik.setText(String.format("%s", OcrDatas.get(2)));
        etName.setText(String.format("%s", OcrDatas.get(3)));
        etBirth.setText(String.format("%s", OcrDatas.get(4)));
        etGender.setText(String.format("%s", OcrDatas.get(5)));
        etAddress.setText(String.format("%s %s %s", OcrDatas.get(6), OcrDatas.get(7), OcrDatas.get(8)));
        etReligion.setText(9 < OcrDatas.size() ? String.format("%s", OcrDatas.get(9)) : "");
        etStatus.setText(10 < OcrDatas.size() ? String.format("%s", OcrDatas.get(10)) : "");
        etWork.setText(11 < OcrDatas.size() ? String.format("%s", OcrDatas.get(11)) : "");
        etNationality.setText(12 < OcrDatas.size() ? String.format("%s", OcrDatas.get(12)) : "");
        etExpire.setText(13 < OcrDatas.size() ? String.format("%s", OcrDatas.get(13)) : "");
    }

    private void initComponents() {
        etProvince = findViewById(R.id.province);
        etNik = findViewById(R.id.nik);
        etName = findViewById(R.id.name);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etDivision = findViewById(R.id.division);
        etBirth = findViewById(R.id.birth);
        etGender = findViewById(R.id.gender);
        etAddress = findViewById(R.id.address);
        etReligion = findViewById(R.id.religion);
        etStatus = findViewById(R.id.status);
        etWork = findViewById(R.id.work);
        etNationality = findViewById(R.id.nationality);
        etExpire = findViewById(R.id.expire);

        btnNext = findViewById(R.id.btnNext);
    }
}
