package tpi.panicbutton;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * A broadcast receiver who listens for incoming SMS
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
//            String smsSender = "";
//            String smsBody = "";
//            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
//                smsBody += smsMessage.getMessageBody();
//            }

//            if (smsBody.startsWith(SmsHelper.SMS_CONDITION)) {
//                Log.d(TAG, "Sms with condition detected");


            Toast.makeText(context, "Mensaje recibido: ", Toast.LENGTH_LONG).show();

            
//            }
            Log.d(TAG, "SMS detected:");
//        }
    }
}
