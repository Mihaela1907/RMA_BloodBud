package com.example.bloodbud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DonationsActivity extends AppCompatActivity {

    ArrayList<String> editDateArray = new ArrayList<String>();
    ArrayList<String> keysList = new ArrayList<>();

    private Button saveDate;
    private DatePicker datePickerAdd;
    private FirebaseAuth mAuth;
    private ListView listViewEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        mAuth = FirebaseAuth.getInstance();

        saveDate = findViewById(R.id.save_date);
        datePickerAdd = findViewById(R.id.datePicker_add);
        listViewEdit = findViewById(R.id.date_list_edit);

        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDateToDb();
            }
        });

        listDates();
    }

    private void listDates()
    {
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, editDateArray);

        String userIds = mAuth.getCurrentUser().getUid();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("date");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String previousChildName) {
                String value = (String) dataSnapshot.getValue();
                editDateArray.add(value);
                listViewEdit.setAdapter(adapter);
                keysList.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                editDateArray.remove(string);
                keysList.remove(dataSnapshot.getKey());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        databaseReference.addChildEventListener(childEventListener);

        listViewEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String key = keysList.get(position);
                databaseReference.child(key).removeValue();
                //Delete item form Firebase database
            }
        });

    }

    private void addDateToDb()
    {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference();

        datePickerAdd.setMaxDate(System.currentTimeMillis());
        //get date from date picker
        int aDay = datePickerAdd.getDayOfMonth();
        int aMonth = datePickerAdd.getMonth();
        int aYear = datePickerAdd.getYear();
        //format date
        Calendar calendar = Calendar.getInstance();
        calendar.set(aYear, aMonth, aDay);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String strAddDate = format.format(calendar.getTime());



        currentUser.child("Users").child(userId).child("date").child(strAddDate).setValue(strAddDate);
    }

    public void onBackPressed(){
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
}
