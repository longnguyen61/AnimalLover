package com.example.pv.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignin;
    private EditText editTextPassword, editTextEmail;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
           //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
        }


        buttonSignin = (Button) findViewById(R.id.buttonSignin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);


        progressDialog = new ProgressDialog(this);

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }


    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("This is not a valid email");
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("Minimum length of password is 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        //show progressDialog
        progressDialog.setMessage("Logging...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
                    Toast.makeText(LoginActivity.this, "Login Sucessfully",Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(LoginActivity.this, "Could not login. Try again",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v == buttonSignin){
            userLogin();
        }
        if(v == textViewSignup){
            finish(); //close activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LoginAct tag", "Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LoginAct tag", "Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LoginAct tag", "Restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginAct tag", "STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LoginAct tag", "Bye");
    }
}
