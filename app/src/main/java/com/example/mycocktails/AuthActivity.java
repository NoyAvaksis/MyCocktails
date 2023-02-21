package com.example.mycocktails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword, etUserName;
    Button btnLogin;
    TextView tvSwitchCase;
    boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etUserName = findViewById(R.id.etUserName);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        tvSwitchCase = findViewById(R.id.tvSwitchCase);
        tvSwitchCase.setOnClickListener(this);

        isLogin = true;
    }

    public void switchCase(){
        isLogin = !isLogin;
        if(isLogin) {
            tvSwitchCase.setText("Haven't user? Register");
            btnLogin.setText("Login");
            etUserName.setVisibility(View.GONE);
        } else {
            tvSwitchCase.setText("Have user? Login");
            btnLogin.setText("Register");
            etUserName.setVisibility(View.VISIBLE);
        }
    }
    public boolean isThereEmptyData() {
        if(isLogin)
            return etEmail.getText().toString().trim().isEmpty() ||
                    etPassword.getText().toString().trim().isEmpty();
        else
            return etEmail.getText().toString().trim().isEmpty() ||
                    etPassword.getText().toString().trim().isEmpty() ||
                    etUserName.getText().toString().trim().isEmpty();
    }

    public void register() {
        if(!isThereEmptyData())
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString().trim(),etPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(etUserName.getText().toString().trim()).build();
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.e("user name",etUserName.getText().toString().trim());
                                    startActivity(new Intent(AuthActivity.this,MenuActivity.class));
                                    finish();
                                } else Toast.makeText(AuthActivity.this,"dont work",Toast.LENGTH_LONG).show();
                            }
                        });
                    } else Toast.makeText(AuthActivity.this,"dont work",Toast.LENGTH_LONG).show();
                }
            });
        else Toast.makeText(AuthActivity.this,"Enter All Data!",Toast.LENGTH_LONG).show();
    }
    public void login() {
        if(!isThereEmptyData())
            FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.getText().toString().trim(),etPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(AuthActivity.this,MenuActivity.class));
                        finish();
                    } else Toast.makeText(AuthActivity.this,"login dont work",Toast.LENGTH_LONG).show();
                }
            });
        else Toast.makeText(AuthActivity.this,"login dont work",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if(isLogin) login();
                else register();
                break;
            case R.id.tvSwitchCase:
                switchCase();
                break;
        }
    }
}