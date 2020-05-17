package com.example.bloodbud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private Button registerBtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.login);
        registerBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });

        // Set on Click Listener on register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerUserAccount();
            }
        });

        checkUserLoggedIn();
    }

    private void loginUserAccount()
    {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Molimo upišite email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Molimo upišite lozinku!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // sign in existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Prijava uspješna!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);

                                    //get current users id and check if there is name in database
                                    String userIds = mAuth.getCurrentUser().getUid();
                                    if(userIds.equals("KZgWNaVOgmfkuqtXencplTjz0ev2"))
                                    {
                                        Intent intent = new Intent(LoginActivity.this,
                                                AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {

                                        DatabaseReference currentUserCheck = FirebaseDatabase.getInstance().getReference().child("Users").child(userIds).child("name");
                                        currentUserCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String value = (String) dataSnapshot.getValue();

                                                //intent to menu activity if name exists
                                                if (value != null) {
                                                    Intent intent = new Intent(LoginActivity.this,
                                                            MenuActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // if sign-in is successful and user doesn't have name
                                                    // intent to main activity
                                                    Intent intent = new Intent(LoginActivity.this,
                                                            MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(), "Provjerite email i lozinku", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                    }
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Prijava nije uspjela!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
    }

    // intent to registration activity
    public void registerUserAccount() {
        Intent intent
                = new Intent(LoginActivity.this,
                RegistrationActivity.class);
        startActivity(intent);
    }

    private void checkUserLoggedIn()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = mAuth.getCurrentUser().getUid();

            if(userId.equals("KZgWNaVOgmfkuqtXencplTjz0ev2"))
            {
                Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }else {
                // User is signed in
                DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("name");
                currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = (String) dataSnapshot.getValue();

                        //intent to menu activity if name exists
                        if (value != null) {
                            Intent intent = new Intent(LoginActivity.this,
                                    MenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // if sign-in is successful and user doesn't have name
                            // intent to main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Provjerite email i lozinku", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        } else {
            // User is signed out
            Toast.makeText(getApplicationContext(), "Prijavite se!", Toast.LENGTH_SHORT).show();
        }
    }
}