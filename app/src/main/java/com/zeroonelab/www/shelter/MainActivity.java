package com.zeroonelab.www.shelter;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        startActivityAfterATimeInterval() ;
    }

    private void startActivityAfterATimeInterval() {

        Handler handler = new Handler(  ) ;
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {

                startActivity( new Intent( getApplicationContext() , Login.class ) );

            }
        } ,1000) ;

    }
}
