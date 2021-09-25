package in.cbslgroup.ezeeoffice.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.text.Html;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.cbslgroup.ezeeoffice.Activity.WorkFlow.InTrayActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.TaskTrackingActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private final static String ADMIN_CHANNEL_ID = "007";
    NotificationManager notificationManager;
    Intent intent;
    PendingIntent pendingIntent;

    SessionManager sessionManager;

    public static String getToken(Context context) {

        return context.getSharedPreferences("fb_token", MODE_PRIVATE).getString("tokenid", "empty");
    }

    public static void clearToken(Context context) {

        context.getSharedPreferences("fb_token", MODE_PRIVATE).edit().clear().apply();

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e("newToken_fb", s);


        sessionManager = new SessionManager(this);


        if (sessionManager.isLoggedIn()) {

            Log.e("userid in fb", sessionManager.getUserDetails().get("user_id"));
            Log.e("tokenid fb", s);

            getSharedPreferences("fb_token", MODE_PRIVATE).edit().putString("tokenid", s).apply();

            updateToken(sessionManager.getUserDetails().get("user_id"), s);



    /*    sPfbToken = getSharedPreferences("fb_token", MODE_PRIVATE);
        editor = sPfbToken.edit();
        editor.putString("tokenid",s);

        editor.commit();*/


        }


        // getSharedPreferences("fb_token", MODE_PRIVATE).edit().commit();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = "EzeeOffice Channel";
        String adminChannelDescription = "EzeeOffice Channel Desc";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        sessionManager = new SessionManager(this);

        //if the user is logged in
        if (sessionManager.isLoggedIn()) {

            sendNotification(remoteMessage);



        }


    }

    public void updateToken(final String u, final String t) {

        Log.e("blabla", "ok");
        Log.e("updateToken check", t);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPDATE_FIREBASE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updatefbtoken service", response);


         /*       sPfbToken = getSharedPreferences("fb_token", MODE_PRIVATE);
                editor = sPfbToken.edit();
                editor.putString("tokenid",t);

                editor.commit();
*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", u);
                params.put("tokenid", t);


                JSONObject js = new JSONObject(params);
                Log.e("Login params", js.toString());

                return params;
            }
        };

        VolleySingelton.getInstance(FirebaseMessagingService.this).addToRequestQueue(stringRequest);


    }

    void sendNotification(RemoteMessage remoteMessage){

        String taskid = remoteMessage.getData().get("id");
        String taskname = remoteMessage.getData().get("taskname");
        String msg = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String actionBy = remoteMessage.getData().get("actionBy");
        String datetime = remoteMessage.getData().get("datetime");

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT_noti", object.toString());

        Log.e("getFrom", remoteMessage.getFrom());

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int notificationId = new Random().nextInt(60000);

       /* Spannable sb = new SpannableString(remoteMessage.getData().get("title"));
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/

        if (title.equalsIgnoreCase("In Tray")) {

            intent = new Intent(this, InTrayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("taskid", taskid);
//            intent.putExtra("taskname", taskname);
//            intent.putExtra("taskStatus", "Pending");
            pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                    PendingIntent.FLAG_ONE_SHOT);

//
//            intent = new Intent(this, TaskInProcessActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("taskid", taskid);
//            intent.putExtra("taskname", taskname);
//            intent.putExtra("taskStatus", "Pending");
//            pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
//                    PendingIntent.FLAG_ONE_SHOT);

        } else if (title.equalsIgnoreCase("Task Track")) {


            intent = new Intent(this, TaskTrackingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("ticket_id", taskid);
            pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }

        final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo_notification)  //a resource for your custom small icon
                .setContentTitle(Html
                        .fromHtml("<b>" + title + "</b>")) //the "title" value you sent in your notification
                .setContentText(msg) //ditto
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(DEFAULT_VIBRATE_PATTERN)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ezeeiconmain))
                .setColor(this.getResources().getColor(R.color.colorPrimary));
        // Create a BigTextStyle object.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

    /*    if (title.equalsIgnoreCase("Task Track")) {

            bigTextStyle.bigText(
                    Html.fromHtml(msg + "<br><br><b>Action By : </b>" + actionBy + "\n" + "<br><br><b>Assigned Date : </b>" + datetime));

        } else if (title.equalsIgnoreCase("In Tray")) {

            bigTextStyle.bigText(Html
                    .fromHtml(msg + "<br><br><b>Assigned Date : </b>" + datetime));

        }*/

        bigTextStyle.setBigContentTitle(msg);
        bigTextStyle.setBigContentTitle(Html
                .fromHtml("<b>" + title + "</b>"));

        // Set big text style.
        notificationBuilder.setStyle(bigTextStyle);

           /* //Vibration
            notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //LED
            notificationBuilder.setLights(Color.RED, 3000, 3000);

            //Ton
            notificationBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
*/

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());


    }





}
