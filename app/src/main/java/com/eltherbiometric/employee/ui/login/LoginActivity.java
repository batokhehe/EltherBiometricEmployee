package com.eltherbiometric.employee.ui.login;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eltherbiometric.employee.MainActivity;
import com.eltherbiometric.employee.R;
import com.eltherbiometric.employee.ui.ocr.OcrActivity;
import com.eltherbiometric.employee.utils.Config;
import com.orhanobut.hawk.Hawk;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private String insert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Hawk.init(LoginActivity.this).build();

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

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.etUsername);
        final EditText passwordEditText = findViewById(R.id.etPassword);
        final Button loginButton = findViewById(R.id.btnLogin);
        final Button regisButton = findViewById(R.id.btnRegistration);
        final ProgressBar loadingProgressBar = findViewById(R.id.btnLoading);
        final TextView tvVersion = findViewById(R.id.apps_version);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText(String.format("Version %s", versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        usernameEditText.setText(Config.UserName);
//        passwordEditText.setText(Config.Password);

        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(LoginActivity.this);
                View view = layoutInflaterAndroid.inflate(R.layout.dialog_password, null);

                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(LoginActivity.this);
                alertDialogBuilderUserInput.setView(view);

                EditText etSettingPassword = view.findViewById(R.id.txtSettingPassword);
                etSettingPassword.requestFocus();

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Access", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
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

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String settingPassword = etSettingPassword.getText().toString();
                        if (settingPassword.equals("12345")) {
                            Intent intent = new Intent(LoginActivity.this, OcrActivity.class);
                            startActivity(intent);
                            alertDialog.dismiss();
                        } else {
                            // Check for a valid password, if the user entered one.
                            if (TextUtils.isEmpty(settingPassword)) {
                                etSettingPassword.setError("Required");
                            } else {
                                etSettingPassword.setError("Salah Password");
                            }
                            return;
                        }
                    }
                });
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK);

                    //Complete and destroy login activity once successful
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        };
//        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), LoginActivity.this);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), LoginActivity.this);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
