package com.example.bloodbud;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText nameInput, weightInput;
    private RadioButton mfBtn, bloodGroup, rhFactor;
    private RadioGroup maleFemaleRadio, bloodGroupRadio, rhRadio;
    private DatePicker datePickerBirthday;

    private Button saveInfo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.name_input);
        weightInput = findViewById(R.id.weight_input);
        datePickerBirthday = findViewById(R.id.datePicker_birthday);
        saveInfo = findViewById(R.id.save_info);

        mAuth = FirebaseAuth.getInstance();

        maleFemaleRadio = findViewById(R.id.male_female_radio);
        bloodGroupRadio  = findViewById(R.id.bloodGroup_radio);
        rhRadio = findViewById(R.id.rh_radio);

        //save user info button listener
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveUserInfo();
            }
        });

    }

    private void saveUserInfo()
    {
        //get logged in user id
        String userId = mAuth.getCurrentUser().getUid();
        //in database create users and put user id
        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        //get name from user input
        String name = nameInput.getText().toString();
        //name input empty
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(),
                    "Upišite ime!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //get weight from user input
        String weight = weightInput.getText().toString();
        //weight input empty
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(),
                    "Upišite težinu!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //get sex from radio group
        int selectedId = maleFemaleRadio.getCheckedRadioButtonId();
        // no radio buttons are checked
        if (maleFemaleRadio.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),
                    "Odaberite spol!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // find the radiobutton by returned id
        mfBtn = findViewById(selectedId);
        String sex = mfBtn.getText().toString();

        //get blood group from radio group
        int selectedId2 = bloodGroupRadio.getCheckedRadioButtonId();
        // no radio buttons are checked
        if (bloodGroupRadio.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),
                    "Odaberite krvnu grupu!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // find the radiobutton by returned id
        bloodGroup = findViewById(selectedId2);
        String bloodGroups = bloodGroup.getText().toString();

        //get rh factor from radio group
        int selectedId3 = rhRadio.getCheckedRadioButtonId();
        // no radio buttons are checked
        if (rhRadio.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),
                    "Odaberite Rh faktor!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // find the radiobutton by returned id
        rhFactor = findViewById(selectedId3);
        String rhFactors = rhFactor.getText().toString();

        datePickerBirthday.setMaxDate(System.currentTimeMillis());
        //get date from date picker
        int day = datePickerBirthday.getDayOfMonth();
        int month = datePickerBirthday.getMonth();
        int year = datePickerBirthday.getYear();
        //format date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
        String Birthday = format.format(calendar.getTime());

        //maps keys to values
        Map newPost = new HashMap();
        newPost.put("name", name);
        newPost.put("sex", sex);
        newPost.put("weight", weight);
        newPost.put("bloodGroup", bloodGroups+rhFactors);

        //sets values at the same time
        currentUser.setValue(newPost);
        currentUser.child("birthday").setValue(Birthday);

        //intent to menu
        Intent intent = new Intent(MainActivity.this,
                MenuActivity.class);
        startActivity(intent);
        finish();
    }
}