package com.example.pv.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int PICK_IMAGE = 101;

    private TextView textViewProfile,textViewVerified, textViewTesting;
    private EditText editTextDisplayName, editTextDisplayAddress, editTextDisplayPhoneNumber;
    private Button buttonLogout,buttonSave, buttonUpload, buttonHomePage;
    private ImageView imageView;

    private ProgressDialog progressDialog;
    private Uri uriProfileImage;
    private String profileImageUrl;

    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceName, databaseReferenceAddress,databaseReferencePhoneNumber;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ProfileAct tag", "Start");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            //profile activity
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }




        database = FirebaseDatabase.getInstance();

        imageView = (ImageView) findViewById(R.id.imageView);

        textViewTesting = (TextView) findViewById(R.id.textViewTesting);
        textViewVerified = (TextView) findViewById(R.id.textViewVerified);
        textViewProfile = (TextView) findViewById(R.id.textViewProfile);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonHomePage = (Button) findViewById(R.id.buttonHomePage);

        editTextDisplayName = (EditText) findViewById(R.id.editTextDisplayName);
        editTextDisplayAddress = (EditText) findViewById(R.id.editTextDisplayAddress);
        editTextDisplayPhoneNumber = (EditText) findViewById(R.id.editTextDisplayPhoneNumber);


        progressDialog = new ProgressDialog(this);
        buttonLogout.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonHomePage.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        imageView.setOnClickListener(this);








        firebaseAuth = FirebaseAuth.getInstance();
//        final FirebaseUser user = ParseUser.getCurrentUser()

//        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final FirebaseUser user = firebaseAuth.getCurrentUser();







        loadUserInformation();
        textViewProfile.setText("Welcome " + user.getEmail());
        final FirebaseUser theUser = firebaseAuth.getCurrentUser();
        if(theUser.isEmailVerified()){
            textViewVerified.setText("Email Verified");

        }else {
            textViewVerified.setText("Email Not Verified (Click to verify)");
            textViewVerified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    theUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ProfileActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }



    @Override
    public void onClick(View v) {
        if(v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(v == imageView) {
            showImageChooser();
        }

        if(v == buttonSave){
            saveUserInformation();

        }

        if(v == buttonUpload){
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user.isEmailVerified()){
                finish();
                startActivity(new Intent(this, UploadActivity.class));
            }else{
                Toast.makeText(ProfileActivity.this, "This email is not verified, try again!", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == buttonHomePage) {
            finish();
            startActivity(new Intent(this, HomePageActivity.class));

        }


    }


    private void saveUserInformation(){

        String displayName = editTextDisplayName.getText().toString().trim();
        String displayAddress = editTextDisplayAddress.getText().toString().trim();
        String displayPhoneNumber = editTextDisplayPhoneNumber.getText().toString().trim();
        UserInformation userInformation = new UserInformation(displayName,displayAddress, displayPhoneNumber);

        //Empty Exception
        if(displayName.isEmpty()){
            editTextDisplayName.setError("Name required");
            editTextDisplayName.requestFocus();
            return;
        }
        if(displayAddress.isEmpty()){
            editTextDisplayAddress.setError("Address required");
            editTextDisplayAddress.requestFocus();
            return;
        }
        if(displayPhoneNumber.isEmpty()){
            editTextDisplayPhoneNumber.setError("Phone number required");
            editTextDisplayPhoneNumber.requestFocus();
            return;
        }

        databaseReferenceAddress = database.getReference("User").child(displayName);
        databaseReferencePhoneNumber = database.getReference("User").child(displayName);
        databaseReferenceName = database.getReference("User").child(displayName);

        //Add or Update user name, address, phone number
        databaseReferenceName.child("Name").setValue(displayName);
        databaseReferenceAddress.child("Address").setValue(displayAddress);
        databaseReferencePhoneNumber.child("PhoneNumber").setValue(displayPhoneNumber);




        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null && profileImageUrl !=null){
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageUrl)).build();
            databaseReferenceName.child(user.getUid()).setValue(userInformation);
            progressDialog.setMessage("Updating User Information...");
            progressDialog.show();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }




    private void loadUserInformation(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            Log.v("E", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + user.getDisplayName());
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReferenceAddress = firebaseDatabase.getReference().child("User").child(user.getDisplayName()).child("Address");
            DatabaseReference databaseReferencePhoneNumber = firebaseDatabase.getReference().child("User").child(user.getDisplayName()).child("PhoneNumber");
//            databaseReferenceAddress = database.getReference("User").child(displayName);
            databaseReferenceAddress.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    String value = dataSnapshot.getValue(String.class);
                    editTextDisplayAddress.setText(value);
                    editTextDisplayName.setText(user.getDisplayName());
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReferencePhoneNumber.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    editTextDisplayPhoneNumber.setText(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(user !=null){
                if(user.getPhotoUrl() != null){
                    String photoUrl = user.getPhotoUrl().toString();
                    Log.v("E", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + photoUrl);
                    Glide.with(this).load(user.getPhotoUrl().toString()).into(imageView);
                }
                if(user.getDisplayName()!=null){
                    editTextDisplayName.setText(user.getDisplayName());



                }





            }

        }
                }
//










    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage(){
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if(uriProfileImage != null){

            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),PICK_IMAGE);
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ProfileAct tag", "Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ProfileAct tag", "Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("ProfileAct tag", "Restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ProfileAct tag", "STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ProfileAct tag", "Bye");
    }
}




