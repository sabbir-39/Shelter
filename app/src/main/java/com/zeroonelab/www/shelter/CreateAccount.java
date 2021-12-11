package com.zeroonelab.www.shelter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    Button btnCreateAC ;

    DatabaseReference dbRootRef, dbUsersPersonalInfoRef ;

    EditText etEmail, etPassword, etMobileNumber, etName ;

    FirebaseAuth auth ;


    String strEmail, strPassword, strMobileNumber, strName, strUserID ;

    Toolbar toolbar ;

    WomenAndChildrensPersonalInfo personalInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_account );
        toolbarInitialization() ;
        uiObjectInitialization() ;
        fireBaseInitialization() ;
        uiObjectsClickListener() ;

    }

    private void fireBaseInitialization() {

        auth = FirebaseAuth.getInstance() ;
        dbRootRef = FirebaseDatabase.getInstance().getReference() ;
        dbUsersPersonalInfoRef = dbRootRef.child( "usersPersonalInfo" ).child( "womenAndChild" ) ;

    }

    private void uiObjectsClickListener() {

        btnCreateAC.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTextFromUI() ;
                signInUsingEmailAndPassword() ;
            }
        } );

    }

    private void signInUsingEmailAndPassword() {

        auth.createUserWithEmailAndPassword( strEmail, strPassword )
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            auth.signInWithEmailAndPassword( strEmail, strPassword )
                                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(auth.getCurrentUser() != null)
                                            {
                                                strUserID = auth.getCurrentUser().getUid() ;
                                                setValueToClass() ;
                                                dbUsersPersonalInfoRef.child( strUserID )
                                                        .setValue( personalInfo )
                                                        .addOnCompleteListener( new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful())
                                                                {
                                                                    startActivity( new Intent( getApplicationContext() , UserMainActivity.class ) );
                                                                }

                                                            }
                                                        } )
                                                        .addOnFailureListener( new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                                Toast.makeText( CreateAccount.this, e.toString().substring( 20 ), Toast.LENGTH_SHORT ).show();

                                                            }
                                                        } ) ;

                                            }

                                        }
                                    } )
                                    .addOnFailureListener( new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText( CreateAccount.this, ""+e.toString().substring( 20 ), Toast.LENGTH_SHORT ).show();

                                        }
                                    } ) ;
                        }

                    }
                } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText( CreateAccount.this, e.toString().substring( 20 ), Toast.LENGTH_SHORT ).show();

            }
        } ) ;

    }

    private void setValueToClass() {

        WomenAndChildrensPersonalInfo.ContactNumber contactNumber =

                new WomenAndChildrensPersonalInfo.ContactNumber( "empty","empty","empty","empty","empty");

        personalInfo = new WomenAndChildrensPersonalInfo( strName,
                strMobileNumber,
                strEmail,
                strPassword,
                strUserID,
                contactNumber ) ;

    }

    private void getTextFromUI() {

        strEmail = etEmail.getText().toString().trim() ;
        strPassword = etPassword.getText().toString().trim() ;
        strMobileNumber = etMobileNumber.getText().toString().trim() ;
        strName = etName.getText().toString() ;

    }

    private void toolbarInitialization() {

        toolbar = findViewById( R.id.CreateAccountToolbar ) ;
        setSupportActionBar( toolbar );

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle( "SIGN UP" );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setDisplayShowHomeEnabled( true );
        }
    }

    private void uiObjectInitialization() {

        etEmail = findViewById( R.id.CreateAccountEtEmail ) ;
        etPassword = findViewById( R.id.CreateAccountEtPassword ) ;
        etMobileNumber = findViewById( R.id.CreateAccountEtContactNumber ) ;
        etName = findViewById( R.id.CreateAccountEtName ) ;
        btnCreateAC = findViewById( R.id.CreateAccountBtnCreateAc ) ;
    }
}