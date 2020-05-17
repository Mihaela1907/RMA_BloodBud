package com.example.bloodbud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, profileSex, profileWeight, profileBirthday, profileBloodGroup;
    private FirebaseAuth mAuth;
    private Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        profileName = findViewById(R.id.profile_name);
        profileSex = findViewById(R.id.profile_sex);
        profileWeight = findViewById(R.id.profile_weight);
        profileBirthday = findViewById(R.id.profile_birthday);
        profileBloodGroup = findViewById(R.id.profile_bloodGroup);
        editProfile = findViewById(R.id.edit_profile);

        displayUser();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUserProfile();
            }
        });

    }

    private void editUserProfile()
    {
        Intent intent = new Intent(ProfileActivity.this,
                MainActivity.class);
        startActivity(intent);
    }

    private void displayUser()
    {
        String userIds = mAuth.getCurrentUser().getUid();

        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("name");
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                //get profile name from database
                    profileName.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });

        DatabaseReference currentWeight = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("weight");
        currentWeight.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                //get profile name from database
                profileWeight.setText("Težina: "+value + " kg");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });

        DatabaseReference currentBirthday = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("birthday");
        currentBirthday.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                //get profile name from database
                profileBirthday.setText("Datum rođenja: "+value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });

        DatabaseReference currentBloodGroup = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("bloodGroup");
        currentBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                //get profile name from database
                profileBloodGroup.setText("Krvna grupa: "+value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });

        DatabaseReference currentSex = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("sex");
        currentSex.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                //get profile name from database
                profileSex.setText("Spol: "+value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void onBackPressed(){
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
}
