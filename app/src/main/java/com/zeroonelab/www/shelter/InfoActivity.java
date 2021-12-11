package com.zeroonelab.www.shelter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class InfoActivity extends AppCompatActivity {

    Button btnLogOut ;

    FirebaseAuth auth ;

    ImageButton btnContact, btnCurrentLocation, btnEmergency,
            btnRAB, btnHospitals, btnTHANA ;

    Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_info );

        fireBaseInitialization() ;
        toolbarInitialization() ;
        uiObjectsInitialization() ;
        uiObjectsClickListener() ;
    }

    private void fireBaseInitialization() {

        auth = FirebaseAuth.getInstance() ;

    }

    private void uiObjectsClickListener() {

        btnLogOut.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                startActivity( new Intent( getApplicationContext() , Login.class ) );
                finish();

            }
        } );

        btnContact.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent( getApplicationContext() , ContactActivity.class ) );

            }
        } );

        btnCurrentLocation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( getApplicationContext() , CurrentLocation.class ) ;

                intent.putExtra( "FROM","CURRENT" ) ;

                startActivity( intent );

            }
        } );

        btnTHANA.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        btnRAB.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );

        btnHospitals.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );



    }

    private void uiObjectsInitialization() {

        btnContact = findViewById( R.id.InfoActivityContact ) ;
        btnCurrentLocation = findViewById( R.id.InfoActivityLocation ) ;
        btnRAB = findViewById( R.id.InfoActivityRAB ) ;
        btnHospitals = findViewById( R.id.InfoActivityHospitals ) ;
        btnTHANA = findViewById( R.id.InfoActivityPoliceStation ) ;

    }

    private void toolbarInitialization() {

        toolbar = findViewById( R.id.InfoActivityToolbar ) ;
        btnLogOut = toolbar.findViewById( R.id.toolbarLogOutBtnLogOut ) ;
        setSupportActionBar( toolbar );

        TextView tvTitle = toolbar.findViewById( R.id.ToolbarTvTitleName ) ;
        tvTitle.setText( "INFORMATION" );
    }
}
