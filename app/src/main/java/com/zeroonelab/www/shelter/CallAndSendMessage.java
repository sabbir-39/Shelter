package com.zeroonelab.www.shelter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class CallAndSendMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_call_and_send_message );
        call();
        sendHelpMessageAndCallTo999();
    }

    private void call()
    {
        Intent intent = new Intent( Intent.ACTION_CALL );
        intent.setData( Uri.parse( "tel:121" ) );
        if (ActivityCompat.checkSelfPermission( CallAndSendMessage.this, android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
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
