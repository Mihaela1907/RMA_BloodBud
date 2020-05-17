package com.example.bloodbud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoActivity extends AppCompatActivity {

    private Button logInBtn, registerBtn, whoBtn, whyBtn, howBtn, crveniBtn, kbcBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        registerBtn = findViewById(R.id.register_view);
        logInBtn = findViewById(R.id.logIn_view);
        whoBtn = findViewById(R.id.who_info);
        whyBtn = findViewById(R.id.why_info);
        howBtn = findViewById(R.id.how_info);
        crveniBtn = findViewById(R.id.crvenikriz_info);
        kbcBtn = findViewById(R.id.kbc_info);


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInView();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerView();
            }
        });

        whoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoView();
            }
        });

        whyBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            whyView();
        }


        });

        howBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howView();
            }
        });

        crveniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.crvenikrizosijek.hr/sto-radimo/dobrovoljno-davanje-krvi/"));
                startActivity(intent);
            }
        });

        kbcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.kbco.hr/klinika/klinicki-zavod-za-transfuzijsku-medicinu/"));
                startActivity(intent);
            }
        });
    }

    private void logInView()
    {
        Intent intent
                = new Intent(InfoActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }

    private void registerView()
    {
        Intent intent
                = new Intent(InfoActivity.this,
                RegistrationActivity.class);
        startActivity(intent);
    }

    private void whoView()
    {
        Intent intent
                = new Intent(InfoActivity.this,
                WhoInfoActivity.class);
        startActivity(intent);
    }

    private void whyView()
    {
        Intent intent
                = new Intent(InfoActivity.this,
                WhyInfoActivity.class);
        startActivity(intent);
    }

    private void howView()
    {
        Intent intent
                = new Intent(InfoActivity.this,
                HowInfoActivity.class);
        startActivity(intent);
    }



}
