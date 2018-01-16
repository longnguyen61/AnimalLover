package com.example.pv.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<ImageUpload> imgList;
    private ListView listView;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
//    BottomBar bottomBar;
    TextView textViewGenre;

    Button buttonProfile, buttonUpload, buttonLogout;
    ArrayList<String> topicArray = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Homepage tag", "Start");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        firebaseAuth = FirebaseAuth.getInstance();
        imgList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference(UploadActivity.DATABASE_PATH);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }
                adapter = new ImageListAdapter(HomePageActivity.this, R.layout.image_item, imgList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference = databaseReference.child("Topic");



//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(HomePageActivity.this, TopicActivity.class);
//                intent.putExtra("TopicName", listView.getItemAtPosition(position).toString());
//                listView.getItemAtPosition(position);
//                Log.v("E", "HAHAHAHHAAHAHAHAHAHHAAHHAAHAHAHAHAHAHHA" + listView.getItemAtPosition(position).toString());
////                ImageUpload img = new ImageUpload("name","haha","yo");
////                Log.v("E", "HAHAHAHHAAHAHAHAHAHHAAHHAAHAHAHAHAHAHHA" + img.getName());
//                startActivity(intent);
//            }});


        buttonProfile = (Button) findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(this);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(this);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        if (v == buttonProfile) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (v == buttonUpload) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user.isEmailVerified()){
                finish();
                startActivity(new Intent(this, UploadActivity.class));
            }else{
                Toast.makeText(HomePageActivity.this, "This email is not verified, try again!", Toast.LENGTH_SHORT).show();
            }
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Homepage tag", "Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Homepage tag", "Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Homepage tag", "Restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Homepage tag", "STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Homepage tag", "Bye");
    }


}
