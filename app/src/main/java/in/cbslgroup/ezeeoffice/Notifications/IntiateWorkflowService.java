package in.cbslgroup.ezeeoffice.Notifications;

import android.app.IntentService;
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
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.TaskTrackStatusActivity;
import in.cbslgroup.ezeeoffice.Model.MultipleSelect;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.NotificationActions;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

import static com.android.volley.VolleyLog.TAG;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class IntiateWorkflowService extends IntentService {

    private final static String ADMIN_CHANNEL_ID = "007";
    NotificationManager notificationManager;
    Intent intent;
    PendingIntent pendingIntent;
    MultipartUploadRequest multipartUploadRequest;



    public IntiateWorkflowService() {

        super("IntiateWorkflowService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        String method = intent.getStringExtra("method");

        if(method!=null){

            if(method.equalsIgnoreCase("with_upload")){


                Bundle bundle = intent.getExtras();
                ArrayList<MultipleSelect> doclist = bundle.getParcelableArrayList("doclist");
                String workflowdid = bundle.getString("wid");
                String userid = bundle.getString("userid");
                String lastmoveid = bundle.getString("lastmoveid");
                String fielValues = bundle.getString("fielValues");
                String username =bundle.getString("username");
                String tokenid = bundle.getString("tokenid");
                String wfName = bundle.getString("wfName");
                String pagecount = bundle.getString("pagecount");

                try {

                    intiateWorkflowWithUpload(workflowdid,userid,lastmoveid,fielValues,pagecount,username,tokenid,wfName,doclist);

                } catch (Throwable throwable) {

                    Log.e("error uploading",throwable.getMessage());
                    throwable.printStackTrace();
                }

            }

            else if (method.equalsIgnoreCase("without_upload")){


                String workflowdid = intent.getStringExtra("wid");
                String userid = intent.getStringExtra("userid");
                String lastmoveid = intent.getStringExtra("lastmoveid");
                String fielValues = intent.getStringExtra("fielValues");
                String username = intent.getStringExtra("username");
                String tokenid = intent.getStringExtra("tokenid");
                String wfName = intent.getStringExtra("wfName");

                intiateWorkflowWithoutUpload(workflowdid, userid, lastmoveid, fielValues, username, tokenid, wfName);


            }

        }




    }


    void intiateWorkflowWithoutUpload(final String workflowdid, final String userid, final String lastmoveid, final String fielValues, final String username, final String tokenid, String wfName) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.INITIATE_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("response intiate", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");


                    if (error.equalsIgnoreCase("true")) {


                        popupNotification("Error in intiating " + wfName);
                        sendBrodcast(msg,error);
                        //goToMainActvivty();

                        // progressDialog.dismiss();
                        stopSelf();

                    } else if (error.equalsIgnoreCase("false")) {

                        popupNotification("Workflow (" + wfName + ") Intiated Succesfully");
                        sendBrodcast(msg,error);
                        // progressDialog.dismiss();
                        //goToMainActvivty();

                        stopSelf();


                    } else {

                        popupNotification("Error in intiating " + wfName);
                        //
                        sendBrodcast("Server Error","true");
                        //goToMainActvivty();
                        stopSelf();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                popupNotification("Error in intiating " + wfName);
                sendBrodcast("Server Error","true");
               // goToMainActvivty();
                stopSelf();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                //String tokenid = FirebaseMessagingService.getToken(IntiateWorkFlowActivity.this);

                params.put("wid", workflowdid);
                params.put("userid", userid);
                params.put("lastMoveId", lastmoveid);
                params.put("fieldValues", fielValues);
                params.put("username", username);
                params.put("ip", MainActivity.ip);
                params.put("tokenid", tokenid);


                JSONObject js = new JSONObject(params);
                Log.e(TAG, "getParams:" + js.toString());

                return params;
            }
        };

        VolleySingelton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    void intiateWorkflowWithUpload(final String workflowdid, final String userid, final String lastmoveid, final String fielValues, final String pageCount, final String username, final String tokenid,String wfName, ArrayList<MultipleSelect> doclist) throws Throwable {


        final String uploadId = UUID.randomUUID().toString();
        StringBuilder pathString = new StringBuilder();
        for (int i = 0; i < doclist.size(); i++) {

            pathString.append(doclist.get(i).getFilePath()).append(",");

        }


        String path = pathString.toString();

        if (pathString.toString().endsWith(",")) {

            path = path.substring(0, path.length() - 1);

        }

        String pathArray[] = new String[]{path};

        Log.e("patharray", Arrays.toString(pathArray));


        try {


            multipartUploadRequest = new MultipartUploadRequest(this, uploadId, ApiUrl.INITIATE_WORKFLOW);


            UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
            multipartUploadRequest.setNotificationConfig(

                    uploadNotificationConfig
                            .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                            .setClearOnActionForAllStatuses(true));

            for (int j = 0; j < doclist.size(); j++) {


                Log.e("path", doclist.get(j).getFilePath());
                multipartUploadRequest.addFileToUpload(doclist.get(j).getFilePath(), "fileName[]");
            }


            List<String> pathList = new ArrayList<>(Arrays.asList(pathArray));


            //multipartUploadRequest.addArrayParameter("file",pathList);


            Log.e("Upload_rate", UPLOAD_RATE);

            //uploadNotificationConfig.getProgress().message = PROGRESS;
            //uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

            uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(this, 1, uploadId)));


            uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
            // uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;


            uploadNotificationConfig.getError().message = "Error while uploading";
            uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
            //uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


            uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
            //uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;

           /* params.put("wid", workflowdid);
            params.put("userid", userid);
            params.put("lastMoveId", lastmoveid);
            params.put("fieldValues", fielValues);*/

            multipartUploadRequest.addParameter("wid", workflowdid);
            multipartUploadRequest.addParameter("userid", userid);
            multipartUploadRequest.addParameter("lastMoveId", lastmoveid);
            multipartUploadRequest.addParameter("fieldValues", fielValues);
            multipartUploadRequest.addParameter("pageCount", pageCount);
            multipartUploadRequest.addParameter("username", username);
            multipartUploadRequest.addParameter("ip", MainActivity.ip);
            multipartUploadRequest.addParameter("tokenid", tokenid);


            multipartUploadRequest.setMaxRetries(5);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {

                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                    Toast.makeText(context, "Upload Error", Toast.LENGTH_LONG).show();

                    try{

                        Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));
                        Log.e("Server response error", String.valueOf(exception));
                    }

                    catch (Exception e){

                        Log.e("error",e.getMessage());

                    }

                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                    Log.e("Server Response", serverResponse.getBodyAsString());

                    Log.e("ser uploading", serverResponse.getBodyAsString());


                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                        Log.e("uplaoding", "1");

                        String message = jsonObject.getString("msg");
                        String error = jsonObject.getString("error");

                        Log.e("error", error);

                        Log.e("uplaoding", "2");

                        if (error.equalsIgnoreCase("false")) {

                            popupNotification("Workflow (" + wfName + ") Intiated Succesfully");
                            sendBrodcast(message,error);
                            stopSelf();

                            Log.e("uplaoding", "3");


                            // Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                        }

                        else if (error.equalsIgnoreCase("true")){

                            popupNotification("Error in intiating " + wfName);
                            sendBrodcast(message,error);
                            stopSelf();


                        }

                        else{
                            popupNotification("Error in intiating " + wfName);
                            sendBrodcast("Server error","true");
                            stopSelf();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {

                    Toast.makeText(context, "Upload Canceled", Toast.LENGTH_LONG).show();
                    UploadService.stopUpload(uploadId);
                    popupNotification("Error in intiating " + wfName);
                    sendBrodcast("Server error","true");
                    stopSelf();

                }
            });


            //alertProcessing("off",IntiateWorkFlowActivity.this);
            multipartUploadRequest.startUpload();
            doclist.clear();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


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


    void popupNotification(String msg) {

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int notificationId = new Random().nextInt(60000);

        intent = new Intent(this, TaskTrackStatusActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo_notification)  //a resource for your custom small icon
                .setContentTitle(Html
                        .fromHtml("<b>" + "Intiate Workflow" + "</b>")) //the "title" value you sent in your notification
                .setContentText(msg) //ditto
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(DEFAULT_VIBRATE_PATTERN)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ezeeiconmain))
                .setColor(this.getResources().getColor(R.color.colorPrimary));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());


    }

    void goToMainActvivty  ()
           {


               Intent intent = new Intent(this,MainActivity.class);
               startActivity(intent);


    }


    void sendBrodcast(String msg ,String error){

        Intent intent = new Intent();
        intent.setAction(getPackageName()+".CLOSE_INITIATE_PB");
        intent.putExtra("msg",msg);
        intent.putExtra("error",error);
        sendBroadcast(intent);

    }
}
