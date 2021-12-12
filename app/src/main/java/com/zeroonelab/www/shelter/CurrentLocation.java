package com.zeroonelab.www.shelter;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

public class CurrentLocation extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    FirebaseAuth auth;
    DatabaseReference dbRootRef, dbUsersPersonalInfoRef;
    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    boolean siren_play = true; // Just testing
    MediaPlayer player = null;
    boolean check = true;

    String strLocationName, strCompleteLOcationName, strThanaName, strZillaName, strCityName, strPostalCode;

    String strFrom, strUserID;

    String[] strArrayPostalCode = {"1360", "1362", "1361", "1206", "1350", "1351", "1209", "1213", "1212", "1232", "1330", "1332", "1331", "1312", "1311", "1313", "1310", "1219", "1229", "1211", "1216", "1207", "1225", "1222", "1223", "1323", "1325", "1322", "1321", "1324", "1320", "1205", "1000", "1217", "1214", "1348", "1341", "1349", "1342", "1346", "1347", "1340", "1344", "1343", "1345", "1100", "1204", "1203", "1215", "1208", "1230"};
    String[] strArrayThanaName = {"Demra", "Matuai", "Saruli", "Dhaka CantonmentTSO", "Dhamrai", "Kamalpur", "Jigatala TSO", "Banani TSO", "Gulshan Model Town", "Dhania TSO", "Joypara", "Narisha", "Palamganj", "Ati", "Dhaka Jute Mills", "Kalatia", "Keraniganj", "KhilgaonTSO", "KhilkhetTSO", "Posta TSO", "Mirpur TSO", "Mohammadpur Housing", "Sangsad BhabanTSO", "BangabhabanTSO", "DilkushaTSO", "Agla", "Churain", "Daudpur", "Hasnabad", "Khalpar", "Nawabganj", "New Market TSO", "Dhaka GPO", "Shantinagr TSO", "Basabo TSO", "Amin Bazar", "Dairy Farm", "EPZ", "Jahangirnagar Univer", "Kashem Cotton Mills", "Rajphulbaria", "Savar", "Savar Canttonment", "Saver P.A.T.C", "Shimulia", "Dhaka Sadar HO", "Gendaria TSO", "Wari TSO", "Tejgaon TSO", "Dhaka Politechnic", "Uttara Model TwonTSO"};

    List<String> contact_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fireBaseInitialization();
        list_of_contact_numbers();
        //getDataFromPrevActivity() ;

    }

    private void fireBaseInitialization() {

        auth = FirebaseAuth.getInstance();
        dbRootRef = FirebaseDatabase.getInstance().getReference();
        dbUsersPersonalInfoRef = dbRootRef.child("usersPersonalInfo").child("womenAndChild");

        if (auth.getCurrentUser() != null) {
            strUserID = auth.getCurrentUser().getUid();
        }
    }

    private void list_of_contact_numbers() {
        dbUsersPersonalInfoRef.child(strUserID).child("contactNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WomenAndChildrensPersonalInfo.ContactNumber contact = snapshot.getValue(WomenAndChildrensPersonalInfo.ContactNumber.class);
                contact_list.add(contact.contactNumber1);
                contact_list.add(contact.contactNumber2);
                contact_list.add(contact.contactNumber3);
                contact_list.add(contact.contactNumber4);
                contact_list.add(contact.contactNumber5);

                Toast.makeText(CurrentLocation.this, contact_list.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataFromPrevActivity() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            strFrom = bundle.getString("FROM");

            if (strFrom != null && !strFrom.equals("CURRENT")) {
                check = true;
            }
        }

    }

    private void sendMessageAfter5Minutes() {
        sendHelpMessageAndCallTo999();
    }


    private void sendHelpMessageAndCallTo999() {

        if (siren_play) {
            player = MediaPlayer.create(CurrentLocation.this, R.raw.siren);
            player.setLooping(true);
            player.start();
            siren_play = false;

        }

        SmsManager sms = SmsManager.getDefault();
        //UserMainActivity.personalInfo.getName()
//        String message = "হেল্প ! HELP! হেল্প ! \n" +
//                "আমি "+"  "+ "My Name"
//                +", এখন আমি " + strCompleteLOcationName + " " + "এ আছি। দয়া করে,আমাকে সাহায্য করুন।" ;
        String message = "Help!! " + strCompleteLOcationName;

        for (String strNumber : contact_list)
        {
            sms.sendTextMessage(strNumber, null, message, null, null);
        }

        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        callTo999();
                        t.cancel();
                    }
                },
                50000
        );
    }

    private void callTo999() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:121"));
        if (ActivityCompat.checkSelfPermission(CurrentLocation.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        buildGoogleClient();
        mMap.setMyLocationEnabled(true);

    }

    private synchronized void buildGoogleClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        try {

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.isEmpty()) {

                Toast.makeText(this, "Waiting..", Toast.LENGTH_SHORT).show();
            } else {


                if (addresses != null && addresses.size() > 0) {

                    strLocationName = addresses.get(0).getFeatureName();
                    strPostalCode = "" + addresses.get(0).getPostalCode();
                    int position = new ArrayList<String>(Arrays.asList(strArrayPostalCode)).indexOf(strPostalCode);
//                    if(check && position < strArrayPostalCode.length)
//                    {
//                        strCompleteLOcationName = strLocationName+" , " + strArrayThanaName[position-1]  + " , " + addresses.get( 0 ).getAdminArea();
//
//                        //Toast.makeText( this, "Code: "+strPostalCode, Toast.LENGTH_SHORT ).show();
//                        Toast.makeText( this, "Code: "+addresses.get( 0 ).getLocality() +addresses.get( 0 ).getSubLocality() , Toast.LENGTH_SHORT ).show();
//
//                        // Toast.makeText( this, "Thana name : "+strArrayThanaName[position-1], Toast.LENGTH_SHORT ).show();
//                        sendHelpMessageAndCallTo999();
//                    }

                    strCompleteLOcationName = "maps.google.com/?q=" + addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude();
                    // +",Lat: " + String.valueOf(addresses.get(0).getLatitude()) + "Long: "+ String.valueOf(addresses.get(0).getLongitude());

                    //Toast.makeText( this, "Code: "+strPostalCode, Toast.LENGTH_SHORT ).show();
                    Toast.makeText(this, "Code: " + addresses.get(0).getLocality() + addresses.get(0).getSubLocality(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText( this, "Thana name : "+strArrayThanaName[position-1], Toast.LENGTH_SHORT ).show();
                    sendHelpMessageAndCallTo999();
                }


//                if (addresses != null && addresses.size() > 0) {
//
//                    strLocationName = addresses.get(0).getFeatureName() ;
//                    strPostalCode = ""+addresses.get( 0 ).getPostalCode() ;
//                    int position = new ArrayList<String>(Arrays.asList( strPostalCode )).indexOf( strPostalCode );
//
//                    if(check && position < strArrayPostalCode.length)
//                    {
//                        strCompleteLOcationName = strLocationName+" , " + strArrayThanaName[position]  + " , " + addresses.get( 0 ).getAdminArea();
//                        sendMessageAfter5Minutes();
//                        callTo999();
//                    }
//                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setFastestInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        if (player != null)
        {
            player.stop();
            player.release();
            player = null;
        }
        super.onStop();
    }
}
