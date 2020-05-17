package com.example.bloodbud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    private Button logout, addBloodLevel;
    private EditText bloodLevelInput;
    private FirebaseAuth mAuth;
    private ProgressBar aPlus, bPlus, abPlus, oPlus, aMinus, bMinus, abMinus, oMinus;
    private RadioButton groupRadio, rhRadio;
    private RadioGroup bloodBtn, rhBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blood_levels);
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout_admin);
        addBloodLevel = findViewById(R.id.add_blood);
        bloodLevelInput = findViewById(R.id.bloodLevel_input);
        aPlus = findViewById(R.id.admin_aplus_progress);
        bPlus = findViewById(R.id.admin_bplus_progress);
        abPlus = findViewById(R.id.admin_abplus_progress);
        oPlus = findViewById(R.id.admin_oplus_progress);
        aMinus = findViewById(R.id.admin_aminus_progress);
        bMinus = findViewById(R.id.admin_bminus_progress);
        abMinus = findViewById(R.id.admin_abminus_progress);
        oMinus = findViewById(R.id.admin_ominus_progress);
        bloodBtn = findViewById(R.id.admin_bloodGroup_radio);
        rhBtn = findViewById(R.id.admin_rh_radio);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutAdmin();
            }
        });
        addBloodLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBloodToDb();
            }
        });

        showBloodLevels();
    }

    private void addBloodToDb()
    {
        String userId = mAuth.getCurrentUser().getUid();
        //get liters from user input
        String bloodLiter = bloodLevelInput.getText().toString();
        //liter input empty
        if (TextUtils.isEmpty(bloodLiter)) {
            Toast.makeText(getApplicationContext(),
                    "Unesite vrijednost!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        int selectedId = bloodBtn.getCheckedRadioButtonId();
        if (bloodBtn.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),
                    "Odaberite krvnu grupu!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // find the radiobutton by returned id
        groupRadio = findViewById(selectedId);
        String group = groupRadio.getText().toString();

        //get rh factor from radio group
        int selectedId3 = rhBtn.getCheckedRadioButtonId();
        // no radio buttons are checked
        if (rhBtn.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),
                    "Odaberite Rh faktor!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // find the radiobutton by returned id
        rhRadio = findViewById(selectedId3);
        String rhFactors = rhRadio.getText().toString();
        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        currentUser.child(group+rhFactors).setValue(bloodLiter);
    }

    private void showBloodLevels()
    {
        getBloodLevelFromDb("A+");
        getBloodLevelFromDb("B+");
        getBloodLevelFromDb("AB+");
        getBloodLevelFromDb("O+");
        getBloodLevelFromDb("A-");
        getBloodLevelFromDb("B-");
        getBloodLevelFromDb("AB-");
        getBloodLevelFromDb("O-");
    }


    private void getBloodLevelFromDb(final String groupId)
    {
        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("KZgWNaVOgmfkuqtXencplTjz0ev2").child(groupId);
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                int bloodValue = Integer.parseInt(value);
                changeBloodLevel(bloodValue, groupId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeBloodLevel(int bloodValue, String bloodGroupRh)
    {
        if (bloodGroupRh.equals("A+")) {
            ObjectAnimator animation = ObjectAnimator.ofInt(aPlus, "progress", 0, bloodValue);
            animation.setDuration(1000);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }else if (bloodGroupRh.equals("B+")){
             ObjectAnimator animation = ObjectAnimator.ofInt(bPlus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }else if (bloodGroupRh.equals("AB+")){
             ObjectAnimator animation = ObjectAnimator.ofInt(abPlus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }else if (bloodGroupRh.equals("O+")){
             ObjectAnimator animation = ObjectAnimator.ofInt(oPlus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }else if (bloodGroupRh.equals("A-")){
             ObjectAnimator animation = ObjectAnimator.ofInt(aMinus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }else if (bloodGroupRh.equals("B-")){
             ObjectAnimator animation = ObjectAnimator.ofInt(bMinus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }else if (bloodGroupRh.equals("AB-")){
             ObjectAnimator animation = ObjectAnimator.ofInt(abMinus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }else if (bloodGroupRh.equals("O-")){
             ObjectAnimator animation = ObjectAnimator.ofInt(oMinus, "progress", 0, bloodValue);
             animation.setDuration(1000);
             animation.setInterpolator(new DecelerateInterpolator());
             animation.start();
        }
    }

    private void logOutAdmin()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Admin odjavljen!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}