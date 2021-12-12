package com.zeroonelab.www.shelter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ContactActivity extends AppCompatActivity {

    Button btnSave;

    DatabaseReference dbRootRef , dbUsersPersonalInfoRef;
    FirebaseAuth auth;
    ImageButton imgBtn1,imgBtn2,imgBtn3,imgBtn4,imgBtn5;

    String strUserID ;
    String strContactNumber, strName ;
    String strContactNumber1, strName1 ;
    String strContactNumber2, strName2 ;
    String strContactNumber3, strName3 ;
    String strContactNumber4, strName4 ;
    String strContactNumber5, strName5 ;

    Toolbar toolbar ;
    EditText tvContactNumber ;
    EditText tvContactNumber1,tvContactNumber2,tvContactNumber3,tvContactNumber4,tvContactNumber5 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_contact );

        fireBaseInitialization() ;
        toolbarInitialization();
        uiObjectsInitialization();
        getDataFromDatabase() ;
        uiObjectsClickListener() ;
    }

    private void getDataFromDatabase() {

        dbUsersPersonalInfoRef.child( strUserID )
                .child( "contactNumber" )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            WomenAndChildrensPersonalInfo.ContactNumber contactNumber =
                                    dataSnapshot.getValue(  WomenAndChildrensPersonalInfo.ContactNumber.class);


                            if( contactNumber != null && !contactNumber.contactNumber1.equals( "empty" ))
                            {
                                strContactNumber1 = contactNumber.contactNumber1 ;
                                strContactNumber2 = contactNumber.contactNumber2 ;
                                strContactNumber3 = contactNumber.contactNumber3 ;
                                strContactNumber4 = contactNumber.contactNumber4 ;
                                strContactNumber5 = contactNumber.contactNumber5 ;

                                tvContactNumber1.setText( strContactNumber1 );
                                tvContactNumber2.setText( strContactNumber2 );
                                tvContactNumber3.setText( strContactNumber3 );
                                tvContactNumber4.setText( strContactNumber4 );
                                tvContactNumber5.setText( strContactNumber5 );
                            }
                        }

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

        if(auth.getCurrentUser() != null)
        {
            strUserID = auth.getCurrentUser().getUid() ;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Uri contactData = data.getData();
            Cursor c =  managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {


                String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {

                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                            null, null);
                    if( phones != null)
                    {
                        phones.moveToFirst();

                        strContactNumber = phones.getString(phones.getColumnIndex("data1"));
                        tvContactNumber.setText( strContactNumber );

                        if( reqCode == 1 )
                        {
                            strContactNumber1 = strContactNumber ;
                        }
                        else if( reqCode == 2)
                        {
                            strContactNumber2 = strContactNumber ;
                        }
                        else if( reqCode == 3)
                        {
                            strContactNumber3 = strContactNumber ;
                        }
                        else if( reqCode == 4)
                        {
                            strContactNumber4 = strContactNumber ;
                        }
                        else
                        {
                            strContactNumber5 = strContactNumber ;
                        }
                    }
                    else
                    {
                        Toast.makeText( this, "Sorry, something is wrong.", Toast.LENGTH_SHORT ).show();
                    }
                }
                strName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            }
        }
    }

    private void uiObjectsClickListener() {

        imgBtnClickListener(imgBtn1, tvContactNumber1,1) ;
        imgBtnClickListener(imgBtn2, tvContactNumber2,2) ;
        imgBtnClickListener(imgBtn3, tvContactNumber3,3) ;
        imgBtnClickListener(imgBtn4, tvContactNumber4,4) ;
        imgBtnClickListener(imgBtn5, tvContactNumber5,5) ;
        btnSaveClickListener() ;
    }

    private void btnSaveClickListener() {

        btnSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strContactNumber1 = tvContactNumber1.getText().toString();
                strContactNumber2 = tvContactNumber2.getText().toString();
                strContactNumber3 = tvContactNumber3.getText().toString();
                strContactNumber4 = tvContactNumber4.getText().toString();
                strContactNumber5 = tvContactNumber5.getText().toString();

                WomenAndChildrensPersonalInfo.ContactNumber contactNumber =
                        new WomenAndChildrensPersonalInfo.ContactNumber( strContactNumber1,
                                strContactNumber2,
                                strContactNumber3,
                                strContactNumber4,
                                strContactNumber5) ;

                if( strUserID != null)
                {
                    dbUsersPersonalInfoRef.child( strUserID ).child( "contactNumber" )
                            .setValue( contactNumber )
                            .addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText( ContactActivity.this, "Saved Successfully."+"\n"+"Thank you.", Toast.LENGTH_SHORT ).show();
                                    }

                                }
                            } ) ;
                }
            }
        } );
    }

    private void imgBtnClickListener(ImageButton imageButton, final EditText textView, final int PICK_NUMBER ) {

        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvContactNumber = textView ;
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_NUMBER);
            }
        } );

    }

    private void uiObjectsInitialization() {

        tvContactNumber1 = findViewById( R.id.ContactActivityTvContactNumber1 ) ;
        tvContactNumber2 = findViewById( R.id.ContactActivityTvContactNumber2 ) ;
        tvContactNumber3 = findViewById( R.id.ContactActivityTvContactNumber3 ) ;
        tvContactNumber4 = findViewById( R.id.ContactActivityTvContactNumber4 ) ;
        tvContactNumber5 = findViewById( R.id.ContactActivityTvContactNumber5 ) ;

        imgBtn1 = findViewById( R.id.ContactActivityImgBtnContactNumber1 ) ;
        imgBtn2 = findViewById( R.id.ContactActivityImgBtnContactNumber2 ) ;
        imgBtn3 = findViewById( R.id.ContactActivityImgBtnContactNumber3 ) ;
        imgBtn4 = findViewById( R.id.ContactActivityImgBtnContactNumber4 ) ;
        imgBtn5 = findViewById( R.id.ContactActivityImgBtnContactNumber5 ) ;

        btnSave = findViewById( R.id.ContactActivityBtnSave ) ;
    }

    private void toolbarInitialization() {

        toolbar = findViewById( R.id.ContactActivityToolbar ) ;
        setSupportActionBar( toolbar );

        TextView tvTitle = toolbar.findViewById( R.id.ToolbarTvTitleName ) ;
        tvTitle.setText( "CONTACT  NUMBER" );

    }
}
