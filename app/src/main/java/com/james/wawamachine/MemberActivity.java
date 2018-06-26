package com.james.wawamachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class MemberActivity extends AppCompatActivity {

    ImageView img_myList , img_newmatchine;
    Bundle bundle;
    String userId, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img_myList = (ImageView) findViewById(R.id.img_myList);
        img_newmatchine = (ImageView) findViewById(R.id.img_newmatchine);
        bundle = getIntent().getExtras();
        userId = bundle.getString("uid");
        email = bundle.getString("email");


        img_myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MemberActivity.this,ListMyActivity.class);
                startActivity(intent);
            }
        });
        img_newmatchine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MemberActivity.this,SetDataActivity.class);
                intent.putExtra("uid", userId);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

    }

}
