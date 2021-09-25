package in.cbslgroup.ezeeoffice.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.TaskInProcessActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class InTrayNotificationService extends Service {

    private Handler mHandler;
    private long taskidPrevious = 0;
    Boolean frstTym = true;


    public InTrayNotificationService() {
        super();
    }

    @Override
    public void onDestroy() {
        Log.e("Service","Service stopped");
        mHandler.removeCallbacks(runnableService);
        super.onDestroy();

    }


    // task to be run here
    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {

            //login("ankit.roy@ams-espl.com","cbsl@123");

            getInTray(MainActivity.userid);
            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, 1000);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandler = new Handler();
        // Execute a runnable task as soon as possible
        mHandler.post(runnableService);

        return START_STICKY;
    }


    void getInTray(final String userid ){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.NOTIFICATION_IN_TRAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("notification response",response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArraySuccess = jsonObject.getJSONArray("success");


                    if(jsonArraySuccess.length()!=0){


                        for(int i = 0;i<jsonArraySuccess.length();i++){

                            String msg = jsonArraySuccess.getJSONObject(i).getString("msg");
                            String remainingTime = jsonArraySuccess.getJSONObject(i).getString("remaining_time");
                            String taskId = jsonArraySuccess.getJSONObject(i).getString("task_id");
                            String taskname = jsonArraySuccess.getJSONObject(i).getString("taskName");

                          /* int random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                           addNotification(msg ,random);*/

                            taskidPrevious = Long.parseLong(taskId);

                            if(frstTym){

                                sendNotification(msg,"In Tray",taskId,taskname,remainingTime);
                                frstTym= false;

                            }

                            if(Long.parseLong(taskId)>taskidPrevious){

                                sendNotification(msg,"In Tray",taskId,taskname,remainingTime);

                            }


                            //successTask.add(new NotiModel(msg,taskId,remainingTime));

                        }

                    }




                    JSONArray jsonArrayError = jsonObject.getJSONArray("error");

                    if(jsonArrayError.length()!=0){


                        for(int j = 0;j<jsonArrayError.length();j++){

                            String msg = jsonArraySuccess.getJSONObject(j).getString("msg");
                            String remainingTime = jsonArraySuccess.getJSONObject(j).getString("remaining_time");
                            String taskId = jsonArraySuccess.getJSONObject(j).getString("task_id");
                            String taskname = jsonArraySuccess.getJSONObject(j).getString("taskName");

                           // errorTask.add(new NotiModel(msg,taskId,remainingTime));
                           /*int random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                           addNotification(msg,random);*/

                            sendNotification(msg,"In Tray",taskId,taskname,remainingTime);


                        }

                        JSONArray jsonArrayWarning = jsonObject.getJSONArray("warning");

                        if(jsonArrayWarning.length()!=0){

                            for(int k = 0;k<jsonArrayWarning.length();k++){

                                String msg = jsonArraySuccess.getJSONObject(k).getString("msg");
                                String remainingTime = jsonArraySuccess.getJSONObject(k).getString("remaining_time");
                                String taskId = jsonArraySuccess.getJSONObject(k).getString("task_id");
                                String taskname = jsonArraySuccess.getJSONObject(k).getString("taskName");

                               // warningTask.add(new NotiModel(msg,taskId,remainingTime));
                               /*int random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                               addNotification(msg,random);*/

                                sendNotification(msg,"In Tray",taskId,taskname,remainingTime);

                            }

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {

                /*Long timeStampLong = System.currentTimeMillis()/1000;
                String timestamp = timeStampLong.toString();*/

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date()); // Find todays date

               Log.e("timestamp",currentDateTime);


                Map<String,String> params = new HashMap<>();
                params.put("userid",userid);
                params.put("timestamp",currentDateTime);

                return params;
            }
        };


        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void sendNotification(String message, String title , String taskid, String taskname , String remainingTime) throws JSONException {

        Intent intent = new Intent(this, TaskInProcessActivity.class);
        intent.putExtra("taskid",taskid);
        intent.putExtra("taskname",taskname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int notificationId = new Random().nextInt(60000);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.drawable.ic_launcher_custom)
                .setContentTitle(title)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.pending_icon))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(this.getResources().getColor(R.color.colorPrimary));

        // Create a BigTextStyle object.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        // bigTextStyle.bigText("Hello, this is the large screen size notification example");
        bigTextStyle.bigText(message+"\n\n"+"Remaining Time : "+remainingTime);


        bigTextStyle.setBigContentTitle(title);
        // Set big text style.
        notificationBuilder.setStyle(bigTextStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }
}
