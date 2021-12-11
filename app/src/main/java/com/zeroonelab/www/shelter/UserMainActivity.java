package com.zeroonelab.www.shelter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {

    Button btnLOgOut ;

    DatabaseReference dbRootRef, dbContactNumberListRef, dbUsersPersonalInfoRef;

    FirebaseAuth auth ;

    ImageButton btnHelp, btnInfo;

    static List<String> contactList = new ArrayList<>();

    Toolbar toolbar;

    static WomenAndChildrensPersonalInfo personalInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_main );

        fireBaseInitialization();
        toolbarInitialization();
        uiObjectsInitialization();
        uiObjectsClickListener();
        getDataFromDatabase();
        getNameOfTheUser() ;

        startService(new Intent(getApplicationContext(), LockService.class));

    }

    private void getNameOfTheUser() {

        if( auth.getCurrentUser() != null)
        {
            dbUsersPersonalInfoRef.child(  auth.getCurrentUser().getUid() )
                    .addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            personalInfo = dataSnapshot.getValue( WomenAndChildrensPersonalInfo.class ) ;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    } ) ;
        }

    }

    private void getDataFromDatabase() {

        dbContactNumberListRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText( UserMainActivity.this, "Contact Number Found", Toast.LENGTH_SHORT ).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        dbContactNumberListRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                contactList.add( dataSnapshot.getValue(String.class) ) ;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } ) ;
    }

    private void fireBaseInitialization() {

        auth = FirebaseAuth.getInstance() ;
        dbRootRef = FirebaseDatabase.getInstance().getReference() ;
        dbUsersPersonalInfoRef = dbRootRef.child( "usersPersonalInfo" ).child( "womenAndChild" ) ;
        dbContactNumberListRef = dbRootRef.child( "contactNumber" ) ;
    }

    private void uiObjectsClickListener() {

        btnInfo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent( getApplicationContext() , InfoActivity.class ) );

            }
        } );

        btnHelp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = "01964879417" ;
                SmsManager sms = SmsManager.getDefault();

                String message = "হেল্প ! HELP! হেল্প ! \n" +
                        "আমি "+"  "
                        +", এখন আমি "  + " " + "এ আছি। দয়া করে,আমাকে সাহায্য করুন।" ;

                sms.sendTextMessage( number , null, message, null, null);


                Intent intent = new Intent( getApplicationContext() , CurrentLocation.class) ;
                intent.putExtra( "FROM","HELP" ) ;
                startActivity( intent );
            }
        } );

        btnLOgOut.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                finish();
                startActivity( new Intent( getApplicationContext() , Login.class  ));
            }
        } );
    }

    private void uiObjectsInitialization() {

        btnHelp = findViewById( R.id.UserMainActivityImgeBtnHelp ) ;
        btnInfo = findViewById( R.id.UserMainActivityImgeBtnInfo ) ;
    }

    private void toolbarInitialization() {

        toolbar = findViewById( R.id.UserMainActivityToolbar ) ;
        btnLOgOut = toolbar.findViewById( R.id.toolbarLogOutBtnLogOut ) ;

        TextView tvTitle = toolbar.findViewById( R.id.ToolbarTvTitleName ) ;
        setSupportActionBar( toolbar );

        tvTitle.setText( "HOME" );
    }

    public void CallAndMessage(View view) {

        MediaPlayer player = MediaPlayer.create(this, R.raw.siren);

        player.start();

        SmsManager sms = SmsManager.getDefault();

        String message = "Please, Help me.";

        String number = "01521433784" ;

        //for(String number : numbers) {
        sms.sendTextMessage( number , null, message, null, null);
        //}

        Intent intent = new Intent( Intent.ACTION_CALL );
        intent.setData( Uri.parse( "tel:121" ) );
        if (ActivityCompat.checkSelfPermission( UserMainActivity.this, android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        startActivity( intent );
    }

    private void call()
    {
        Intent intent = new Intent( Intent.ACTION_CALL );
        intent.setData( Uri.parse( "tel:121" ) );
        if (ActivityCompat.checkSelfPermission( UserMainActivity.this, android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity( intent );
    }

    private void sendHelpMessageAndCallTo999() {

        SmsManager sms = SmsManager.getDefault();

        String message = "হেল্প ! HELP! হেল্প ! \n" +
                "আমি "
                +", এখন আমি "  + " " + "এ আছি। দয়া করে,আমাকে সাহায্য করুন।" ;

        String strNumber = "01521433784" ;
        sms.sendTextMessage(  strNumber , null, message, null, null );

    }
}
