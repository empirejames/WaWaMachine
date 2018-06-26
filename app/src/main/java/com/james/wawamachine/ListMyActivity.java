package com.james.wawamachine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.james.wawamachine.adapter.ListAdapter;
import com.james.wawamachine.network.WawaGetList;

import java.util.ArrayList;

public class ListMyActivity extends AppCompatActivity {
    ArrayList<User> userList = new ArrayList<User>();
    String TAG = ListMyActivity.class.getSimpleName();
    ListView listView;
    ListAdapter listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView)findViewById(R.id.listItem);
        getListWaWa();
    }

    public ArrayList<User> getListWaWa(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        final DatabaseReference userRef = database.getReference("user").child(user.getUid());
        userRef.keepSynced(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = new User();
                    user.setLineID((String)dsp.child("lineID").getValue());
                    user.setImgName((String)dsp.child("imgName").getValue());
                    user.setPrice((String)dsp.child("price").getValue());
                    user.setContanter((String)dsp.child("contanter").getValue());
                    user.setName((String)dsp.child("name").getValue());
                    user.setImgName((String)dsp.child("imgName").getValue());
                    userList.add(user);
                    listAdapter = new ListAdapter(getApplicationContext(), userList);
                    listView.invalidateViews();
                    listView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.e(TAG, userList.size()+"");
        return userList;

    }
}
