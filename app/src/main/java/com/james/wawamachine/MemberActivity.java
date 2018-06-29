package com.james.wawamachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.james.wawamachine.network.OnGetDataListener;
import com.james.wawamachine.network.WawaGetList;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity {
    String TAG = MemberActivity.class.getSimpleName();
    ImageView img_myList , img_newmatchine, img_myProfile;
    Bundle bundle;
    String userId, email;
    ArrayList<User> userList = new ArrayList<User>();
    WawaGetList wawaGet = new WawaGetList(MemberActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img_myList = (ImageView) findViewById(R.id.img_myList);
        img_newmatchine = (ImageView) findViewById(R.id.img_newmatchine);
        img_myProfile = (ImageView)findViewById(R.id.img_profile);
        bundle = getIntent().getExtras();
        userId = bundle.getString("uid");
        email = bundle.getString("email");
        img_myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCheckInforInServer();

                Log.e(TAG,userList.size() + ":  Size");




                //Log.e(TAG,userList.size() + " : "+ userList.get(0).getLongtitude() + " : " +userList.get(0).getName());
            }
        });

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
    private void mCheckInforInServer() {
        wawaGet.getLocation(new OnGetDataListener() {
            @Override
            public void onStart() {
                Log.e(TAG,"On Start");
            }

            @Override
            public void onSuccess(DataSnapshot data) {
                Log.e(TAG,"On onSuccess");
                userList = wawaGet.getData();
                Log.e(TAG, userList.size() + " ........");
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.e(TAG,"On onFailed");
            }
        });

    }

}
