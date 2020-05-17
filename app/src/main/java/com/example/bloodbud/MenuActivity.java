package com.example.bloodbud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MenuActivity extends AppCompatActivity {

    ArrayList<String> dateArray = new ArrayList<>();

    private ListView listView;
    private TextView profileName;
    private FirebaseAuth mAuth;
    private Button addDonation, viewProfile, logOutBtn;
    private ProgressBar aPlus, bPlus, abPlus, oPlus, aMinus, bMinus, abMinus, oMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        profileName = findViewById(R.id.name);
        addDonation = findViewById(R.id.add_date);
        viewProfile = findViewById(R.id.view_profile);
        logOutBtn = findViewById(R.id.logout);

        listView = findViewById(R.id.date_list);

        aPlus = findViewById(R.id.aplus_progress);
        bPlus = findViewById(R.id.bplus_progress);
        abPlus = findViewById(R.id.abplus_progress);
        oPlus = findViewById(R.id.oplus_progress);
        aMinus = findViewById(R.id.aminus_progress);
        bMinus = findViewById(R.id.bminus_progress);
        abMinus = findViewById(R.id.abminus_progress);
        oMinus = findViewById(R.id.ominus_progress);

        displayUser();
        createNotificationChannel();
        showBloodLevels();

        addDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDonationDb();
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewUserProfile();
            }
        });

        //log out of app button listener
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUserAccount();
            }
        });

    }

    private void logOutUserAccount()
    {
        //intent back to login activity if user is logged in and clicks log out
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), user.getEmail() + " odjavljen!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUser()
    {
        String userIds = mAuth.getCurrentUser().getUid();

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, dateArray);

        DatabaseReference currentUserCheck = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("name");
        currentUserCheck.addListenerForSingleValueEvent(new ValueEventListener() {
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("date");

        class StringDateComparator implements Comparator<String>
        {
            public int compare(String arg0, String arg1) {
                SimpleDateFormat format = new SimpleDateFormat(
                        "dd-MM-yyyy");
                int compareResult = 0;
                try {
                    Date arg0Date = format.parse(arg0);
                    Date arg1Date = format.parse(arg1);
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    compareResult = arg0.compareTo(arg1);
                }
                return compareResult;
            }
        }

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String value = (String) dataSnapshot.getValue();
                dateArray.add(value);
                Collections.sort(dateArray, new StringDateComparator());
                Collections.reverse(dateArray);
                listView.setAdapter(adapter);
                String lastDate = dateArray.get(0);
                notifyDonation(lastDate);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        databaseReference.addChildEventListener(childEventListener);

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
                animateBloodLevel(bloodValue, groupId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateBloodLevel(int bloodValue, @NotNull String bloodGroupRh)
    {
        if (bloodGroupRh.equals("A+"))
        {
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

    private void editDonationDb()
    {
        Intent intent = new Intent(MenuActivity.this,
                DonationsActivity.class);
        startActivity(intent);
        finish();
    }

    private void viewUserProfile()
    {
        Intent intent = new Intent(MenuActivity.this,
                ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void createNotificationChannel()
    {
        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "donationChannel";
            String description = "Notifications for blood donating dates";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notifChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notifyDonation(String lastDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(lastDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 90);  // number of days to add
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String output = sdf1.format(c.getTime());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notifChannel")
                .setSmallIcon(R.drawable.notifi)
                .setContentTitle("Podsjetnik za darivanje:")
                .setContentText("Posljednje: "+lastDate+" Sljedeće: "+output)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, builder.build());

        checkDonationDay(output);

    }

    private void checkDonationDay(String donationDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long currentDate = cal.getTimeInMillis();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(donationDate);
            long timeInMilliseconds = date.getTime();

            if (timeInMilliseconds == currentDate)
            {
                notifyOnDonationDay();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void notifyOnDonationDay()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notifChannel")
                .setSmallIcon(R.drawable.notifi)
                .setContentTitle("Podsjetnik za darivanje:")
                .setContentText("Od danas možete ponovo dati krv!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(3, builder.build());
    }
}