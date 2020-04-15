package com.example.hpprobook.photogram;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private Button logLoginBtn;
    private Button logRegBtn;

    private  FirebaseAuth mAuth;

    private ProgressBar loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.hpprobook.blogapp.R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail =(EditText) findViewById(com.example.hpprobook.blogapp.R.id.login_email);
        loginPass = (EditText) findViewById(com.example.hpprobook.blogapp.R.id.login_password);
        logLoginBtn = (Button)findViewById(com.example.hpprobook.blogapp.R.id.login_signin);
        logRegBtn = (Button) findViewById(com.example.hpprobook.blogapp.R.id.login_signup);
        loginProgress = (ProgressBar) findViewById(com.example.hpprobook.blogapp.R.id.login_progress);

        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        logLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String logEmail = loginEmail.getText().toString();
                String logPass = loginPass.getText().toString();

                if(!TextUtils.isEmpty(logEmail)&& !TextUtils.isEmpty(logPass)){

                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(logEmail,logPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                sendToMain();

                            }else{

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : "+errorMessage,Toast.LENGTH_LONG).show();

                            }
                            loginProgress.setVisibility(View.INVISIBLE);

                        }
                    });

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
