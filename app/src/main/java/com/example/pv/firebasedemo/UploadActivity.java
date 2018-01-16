package com.example.pv.firebasedemo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonAdd, buttonGetImage,buttonHomePage,buttonProfile;
    private Spinner spinnerStatus;
    private static final int CAMERA_REQUEST_CODE = 1;
    private FirebaseDatabase database;
    private String profileImageUrl;
    DatabaseReference databaseReference;
    private EditText editTextTopicName,editTextDesciption;
    private ImageView imageView;
    private StorageReference storageReference;
    private DatabaseReference databaseReferenceTopicName,databaseReferenceDescription;
    private Uri uriProfileImage;
    public static final String STORAGE_PATH = "image/";
    public static final String DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        imageView = (ImageView) findViewById(R.id.imageView);
        editTextTopicName = (EditText) findViewById(R.id.editTextTopicName);
        editTextDesciption = (EditText) findViewById(R.id.editTextDesciption);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonHomePage = (Button) findViewById(R.id.buttonHomePage);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);

        buttonHomePage.setOnClickListener(this);
        buttonProfile.setOnClickListener(this);
    }

    //Browse Image From Gallery
    public void buttonBrowseClick(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"),REQUEST_CODE);
    }

    //After choose image, the image will be shown on the screen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to return image type
    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void buttonAddClick(View v){
        if (uriProfileImage !=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading Image");
            dialog.show();

            StorageReference ref = storageReference.child(STORAGE_PATH + System.currentTimeMillis() +"." +getImageExt(uriProfileImage));
            ref.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();

                    ImageUpload imageUpload = new ImageUpload(editTextTopicName.getText().toString(), taskSnapshot.getDownloadUrl().toString(),editTextDesciption.getText().toString());
                    String uploadId = databaseReference.push().getKey();
                    databaseReference.child(uploadId).setValue(imageUpload);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) /taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });

        }
    }



    @Override
    public void onClick(View v) {
        if(v == buttonHomePage) {
            finish();
            startActivity(new Intent(this, HomePageActivity.class));

        }
        if(v == buttonProfile) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("UploadAct tag", "Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("UploadAct tag", "Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("UploadAct tag", "Restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("UploadAct tag", "STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("UploadAct tag", "Bye");
    }
}
