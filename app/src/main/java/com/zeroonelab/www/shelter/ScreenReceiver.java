package com.zeroonelab.www.shelter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    MediaPlayer player;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        player = MediaPlayer.create(context, R.raw.siren);
        player.setLooping( true );

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {


            player.start();

            wasScreenOn = false;

            SmsManager sms = SmsManager.getDefault();

            String message = "Please, help me.";

            String number = "01521433784" ;

            //for(String number : numbers) {
            sms.sendTextMessage( number , null, message, null, null);
            //}

            /*Intent intent1 = new Intent( Intent.ACTION_CALL );
            intent.setData( Uri.parse( "tel:121" ) );
            if (ActivityCompat.checkSelfPermission( context, android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity( intent );*/

        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            player.start();

            wasScreenOn = true;
            SmsManager sms = SmsManager.getDefault();

            String message = "Please, Help me.";

            String number = "01521433784" ;

            //for(String number : numbers) {
            sms.sendTextMessage( number , null, message, null, null);
            //}

            Intent intent1 = new Intent( Intent.ACTION_CALL );
            intent.setData( Uri.parse( "tel:121" ) );
            if (ActivityCompat.checkSelfPermission( context, android.Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            context.startActivity( intent );

        }else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){

        }
    }
}
