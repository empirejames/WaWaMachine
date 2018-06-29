package com.james.wawamachine.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james.wawamachine.User;
import com.james.wawamachine.database.TinyDB;

import java.util.ArrayList;


public class WawaGetList{
    String TAG = WawaGetList.class.getSimpleName();
    ArrayList<User> userLists = new ArrayList<User>();
    Context mContext;
    public WawaGetList(Context context){
        this.mContext = context;
    }


    public ArrayList<User> getLocation(final OnGetDataListener listener){
        final TinyDB tinyDB = new TinyDB(mContext);
        listener.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("user").child(user.getUid());
        userRef.keepSynced(true);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Log.e(TAG,dsp.hasChildren() + "");

                    User user = new User();
                    user.setLatitude((String)dsp.child("latitude").getValue());
                    user.setLongtitude((String)dsp.child("longtitude").getValue());
                    user.setLocation((String)dsp.child("location").getValue());
                    user.setPrice((String)dsp.child("price").getValue());
                    userLists.add(user);
                    Log.e(TAG, "userList : " + userLists.size() );
                    //getGPS.put(dsp.getKey().toString() , dsp.child("count").getValue().toString());
                    //Log.e(TAG,dsp.getKey().toString() +" :  "+ dsp.child("count").getValue().toString());
                }
                listener.onSuccess(dataSnapshot);
                Log.e(TAG,"Return UserList : " + userLists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }

        });

        return userLists;
    }

    public ArrayList<User> getData() {
        return userLists;
    }

}
