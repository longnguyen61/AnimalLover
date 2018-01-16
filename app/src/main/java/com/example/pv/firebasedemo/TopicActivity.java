package com.example.pv.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TopicActivity extends AppCompatActivity {


    private TextView textViewTopicName, textViewDescription;
    private ImageView imageView;
    private Button buttonShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        textViewTopicName = (TextView) findViewById(R.id.textViewTopicName);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonShow = (Button) findViewById(R.id.buttonShow);

//        buttonShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://console.firebase.google.com/u/0/project/fir-demo-92da2/storage/fir-demo-92da2.appspot.com/files/profilepics/1515443839089.jpg";
//
//                Glide.with(TopicActivity.this).load(url).into(imageView);
//            }
//        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            textViewTopicName.setText(bundle.getString("TopicName"));
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Topic").child(bundle.getString("TopicName"));

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()) {

                        String value = userSnapshot.getValue(String.class);
                        if (userSnapshot.getKey().equals("Description") ) {
                            textViewDescription.setText(value);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

    }
}
