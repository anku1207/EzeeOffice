package in.cbslgroup.ezeeoffice.Utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class FCMHandler {

    public static void  enableFCM(Context context){

       // FirebaseApp.initializeApp(context);
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        Log.e("FCM","enabled");
    }

    public static void disableFCM(){
        // Disable auto init
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(() -> {
            try {
                // Remove InstanceID initiate to unsubscribe all topic
                // TODO: May be a better way to use FirebaseMessaging.getInstance().unsubscribeFromTopic()
                FirebaseInstanceId.getInstance().deleteInstanceId();
                Log.e("FCM","disabled");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
