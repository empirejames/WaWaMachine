package com.james.wawamachine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SetDataActivity extends AppCompatActivity {
    Bundle bundle;
    String userId, email;
    String latitude, longitude;
    String TAG = SetDataActivity.class.getSimpleName();
    GifView giv_location, giv_pciture;
    TextView tv_getPic, email_content ;
    EditText edit_location, edit_name, edit_price;
    Button btn_send;
    String img_name;

    private LocationManager mLocationManager;
    private StorageReference mStorageRef , imagesRef;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PICK_CONTACT_REQUEST = 1;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bundle = getIntent().getExtras();
        userId = bundle.getString("uid");
        email = bundle.getString("email");
        imageView = (ImageView) findViewById(R.id.img_downlaod);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        giv_location = (GifView)findViewById(R.id.gifView_location);
        giv_pciture = (GifView)findViewById(R.id.gifView_camera);
        tv_getPic = (TextView)findViewById(R.id.tv_getPic);
        email_content = (TextView)findViewById(R.id.email_content);
        edit_location = (EditText)findViewById(R.id.edit_location);
        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_price = (EditText)findViewById(R.id.edit_price);
        btn_send = (Button)findViewById(R.id.btn_send);
        email_content.setText(email);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imagesRef = mStorageRef.child("images");
//        giv_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCurrentLocation();
//            }
//        });
        giv_pciture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeData(email,edit_location.getText().toString(),edit_name.getText().toString(),edit_price.getText().toString());
                finish();
            }
        });
    }


    private void writeData(final String email,final String location,final String name,final String price){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("user").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               userRef.push().setValue(new User(email,location,name,price,img_name));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    private void getCurrentLocation() {
//        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        Log.e(TAG,"getCurrentLocation");
//        Location location = null;
//        if (!(isGPSEnabled || isNetworkEnabled)){
//            //Toast.makeText(getActivity().getApplicationContext(), "這是一個Toast......", Toast.LENGTH_LONG).show();
//        }
//        // Snackbar.make(R.layout.activity_maps, "error_location_provider", Snackbar.LENGTH_LONG).show();
//        else {
//            if (isNetworkEnabled) {
//                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
//                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
//
//            if (isGPSEnabled) {
//                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
//                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            }
//        }
//        if (location != null){
//            edit_location.setText(location+"");
//            Log.e(TAG,location+"");
//        }
//            //drawMarker(location);
//    }
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e(TAG, String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                edit_location.setText(location.getLatitude()+","+location.getLongitude());
                latitude = location.getLatitude()+"";
                longitude = location.getLongitude()+"";
                mLocationManager.removeUpdates(mLocationListener);
            } else {
                Log.e(TAG,"Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocalImg();
                } else {
                    Toast.makeText(SetDataActivity.this, "DoNothing ...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void checkPermission(){
        int permission = ActivityCompat.checkSelfPermission(SetDataActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE
            );
        } else {
            getLocalImg();
        }

    }
    private void getLocalImg(){
        Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
        picker.setType("image/*");
        picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        Intent destIntent = Intent.createChooser(picker, null);
        startActivityForResult(destIntent, PICK_CONTACT_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String path = getPath(SetDataActivity.this, uri);
                final Uri file = Uri.fromFile(new File(path));
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    bitmap = Bitmap.createScaledBitmap(bitmap,  600 ,600, true);
                    //imageView.setImageBitmap(bitmap);
                    tv_getPic.setText(file.getLastPathSegment());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bitmapData = baos.toByteArray();
                    final StorageReference riversRef = mStorageRef.child("images/" +file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putBytes(bitmapData);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            tv_getPic.setText(exception.getMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            img_name = file.getLastPathSegment();
                            tv_getPic.setText(file.getLastPathSegment());
                            tv_getPic.setText("上傳完成");
                            giv_pciture.setVisibility(View.INVISIBLE);
                            //downloadImg(riversRef);
                        }
                    });
                }catch (Exception e){

                }

            }
        }
    }
    private void downloadImg(final StorageReference ref){
        final ImageView imageView = (ImageView) findViewById(R.id.img_downlaod);
        if(ref == null){
            Toast.makeText(SetDataActivity.this, "請先上傳照片", Toast.LENGTH_SHORT).show();
            return;
        }
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(SetDataActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(ref)
                        .into(imageView);
                tv_getPic.setText("上傳完成");
                //downloadInfoText.setText(R.string.download_success);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, exception.getMessage());
                //downloadInfoText.setText(exception.getMessage());
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
