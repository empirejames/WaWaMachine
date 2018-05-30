package com.james.wawamachine;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class FirstFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGooglemap;
    MapView mMapView;
    View mView;
    String TAG = FirstFragment.class.getSimpleName();

    private LocationManager mLocationManager;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION};
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       mView =  inflater.inflate(R.layout.first_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGooglemap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247,-74.044502)).title("Statue of Lib").snippet("I hope go there"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247,-74.044502)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

    }
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)){
            //Toast.makeText(getActivity().getApplicationContext(), "這是一個Toast......", Toast.LENGTH_LONG).show();
        }
        // Snackbar.make(R.layout.activity_maps, "error_location_provider", Snackbar.LENGTH_LONG).show();
        else {
            if (isNetworkEnabled) {

                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null)
            drawMarker(location);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e(TAG, String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                drawMarker(location);
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

    private void drawMarker(Location location) {
        if (mGooglemap != null) {
            mGooglemap.clear();
            try{
                KmlLayer layer = new KmlLayer(mGooglemap, getActivity().getAssets().open("taipei.kml"), getActivity().getApplicationContext());
                KmlLayer layer1 = new KmlLayer(mGooglemap, getActivity().getAssets().open("taichung.kml"), getActivity().getApplicationContext());
                KmlLayer layer2 = new KmlLayer(mGooglemap, getActivity().getAssets().open("taichung-wuzi.kml"), getActivity().getApplicationContext());
                KmlLayer layer3 = new KmlLayer(mGooglemap, getActivity().getAssets().open("tainan.kml"), getActivity().getApplicationContext());
                layer.addLayerToMap();
                layer1.addLayerToMap();
                layer2.addLayerToMap();
                layer3.addLayerToMap();

            }catch (Exception e){

            }

            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mGooglemap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("我在這"));
            mGooglemap.moveCamera(CameraUpdateFactory.newLatLng(gps));
            mGooglemap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 16));
            mGooglemap.getUiSettings().setZoomControlsEnabled(true);  // 右下角的放大縮小功能
            mGooglemap.getUiSettings().setCompassEnabled(true);       // 左上角的指南針，要兩指旋轉才會出現
            mGooglemap.getUiSettings().setMapToolbarEnabled(true);    // 右下角的導覽及開啟 Google Map功能

        }
    }
}