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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail, editTextPassword;
    private TextView textViewSignin;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity tag", "Start");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
        }


        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }


    private void registerUser(){
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
            editTextEmail.requestFocus();
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
        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       //user is successfully registered and logged in
                       finish();
                       startActivity(new Intent(getApplicationContext(),ProfileActivity.class));


                   }else{
                       if(task.getException() instanceof FirebaseAuthUserCollisionException){
                           editTextEmail.setError("This email is already registered");
                           editTextEmail.requestFocus();
                       }

                   }
                progressDialog.hide();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
        if(v == textViewSignin){
            //will open log in activity
            finish(); //close activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity tag", "Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity tag", "Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity tag", "Restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity tag", "STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity tag", "Bye");
    }
}
