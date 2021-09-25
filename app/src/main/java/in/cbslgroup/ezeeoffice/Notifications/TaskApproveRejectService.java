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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.TaskTrackStatusActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.NotificationActions;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class TaskApproveRejectService extends IntentService {


    private final static String ADMIN_CHANNEL_ID = "007";
    NotificationManager notificationManager;
    Intent intent;
    PendingIntent pendingIntent;
    MultipartUploadRequest multipartUploadRequest;


    public TaskApproveRejectService() {
        super("TaskApproveRejectService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String method = intent.getStringExtra("method");

        if (method != null) {

            if (method.equalsIgnoreCase("with_upload")) {


                String comment = intent.getStringExtra("comment");
                String taskstatus = intent.getStringExtra("taskstatus");
                String userid = intent.getStringExtra("userid");
                String username = intent.getStringExtra("username");
                String assignedDynamicUserId = intent.getStringExtra("assignedDynamicUserId");
                String taskorder = intent.getStringExtra("taskorder");
                String taskid = intent.getStringExtra("taskid");
                String alternateuserDynamicUserId = intent.getStringExtra("alternateuserDynamicUserId");
                String supervisorDynamicUserId = intent.getStringExtra("supervisorDynamicUserId");
                String assignedUserId = intent.getStringExtra("assignedUserId");
                String alternateuserUserId = intent.getStringExtra("alternateuserUserId");
                String supervisorUserId = intent.getStringExtra("supervisorUserId");
                String deadlineType = intent.getStringExtra("deadlineType");
                String dateBetween = intent.getStringExtra("dateBetween");
                String deadlineDays = intent.getStringExtra("deadlineDays");
                String deadlineHrs = intent.getStringExtra("deadlineHrs");
                String ip = intent.getStringExtra("ip");
                String filepath = intent.getStringExtra("filepath");
                String taskname = intent.getStringExtra("taskname");


                ApproveRejectTaskWithUploading(taskname,
                        filepath,
                        comment,
                        taskstatus, taskid,
                        userid, username,
                        assignedDynamicUserId, taskorder, alternateuserDynamicUserId,
                        supervisorDynamicUserId, assignedUserId, alternateuserUserId,
                        supervisorUserId, deadlineType,
                        dateBetween, deadlineDays, deadlineHrs, ip, "1"


                );


            } else if (method.equalsIgnoreCase("without_upload")) {

                String comment = intent.getStringExtra("comment");
                String taskstatus = intent.getStringExtra("taskstatus");
                String userid = intent.getStringExtra("userid");
                String username = intent.getStringExtra("username");
                String assignedDynamicUserId = intent.getStringExtra("assignedDynamicUserId");
                String taskorder = intent.getStringExtra("taskorder");
                String taskid = intent.getStringExtra("taskid");
                String alternateuserDynamicUserId = intent.getStringExtra("alternateuserDynamicUserId");
                String supervisorDynamicUserId = intent.getStringExtra("supervisorDynamicUserId");
                String assignedUserId = intent.getStringExtra("assignedUserId");
                String alternateuserUserId = intent.getStringExtra("alternateuserUserId");
                String supervisorUserId = intent.getStringExtra("supervisorUserId");
                String deadlineType = intent.getStringExtra("deadlineType");
                String dateBetween = intent.getStringExtra("dateBetween");
                String deadlineDays = intent.getStringExtra("deadlineDays");
                String deadlineHrs = intent.getStringExtra("deadlineHrs");
                String ip = intent.getStringExtra("ip");
                String taskname = intent.getStringExtra("taskname");


                ApproveRejectTaskwithOutUploading(taskname,
                        comment,
                        taskstatus, taskid,
                        userid, username,
                        assignedDynamicUserId, taskorder, alternateuserDynamicUserId,
                        supervisorDynamicUserId, assignedUserId, alternateuserUserId,
                        supervisorUserId, deadlineType,
                        dateBetween, deadlineDays, deadlineHrs, ip, "1"

                );

            }

        }


    }


    void ApproveRejectTaskwithOutUploading(final String taskname, final String comment,
                                           final String taskstatus, final String taskid, final String userid, final String username,
                                           final String assignedUser, final String taskOrder, final String alternateUser, final String supervisor,
                                           final String assignedUserAdd, final String alternateUserAdd, final String supervisorAdd, final String deadlinetype,
                                           final String daterangeAdd, final String daysAdd, final String hrsAdd, final String ip, final String pagecount
    ) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT_UPLOADING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("without uploading", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.length() == 0) {


                        popupNotification("Error in task approve/reject");
                        sendBrodcast("Server Error", "true");
                        // goToMainActvivty();
                        stopSelf();


                    }

                    else  {

                        String message = jsonObject.getString("message");
                        String error = jsonObject.getString("error");

                        if (error.equalsIgnoreCase("false")) {

                            popupNotification(taskname + " " + message);
                            sendBrodcast(message, error);
                            stopSelf();

                        } else if (error.equalsIgnoreCase("true")) {

                            popupNotification(taskname + " " + message);
                            sendBrodcast(message, error);
                            stopSelf();


                        }

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                popupNotification("Error in task approve/reject");
                sendBrodcast("Server Error", "true");
                // goToMainActvivty();
                stopSelf();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("userid", userid);
                params.put("taskid", taskid);

                if (comment == null || comment.trim().length() == 0 || comment.equalsIgnoreCase("null")) {

                    params.put("comment", "null");

                } else {

                    params.put("comment", comment);

                }


                params.put("username", username);
                params.put("taskstatus", taskstatus);
                params.put("pageCount", pagecount);


                if (assignedUser == null || assignedUser.equalsIgnoreCase("null")) {

                    params.put("asiusr", "null");

                } else {

                    params.put("asiusr", assignedUser);

                }


                params.put("taskOrder", taskOrder);


                if (alternateUser == null || alternateUser.equalsIgnoreCase("null")) {

                    params.put("altrUsr", "null");

                } else {

                    params.put("altrUsr", alternateUser);

                }

                if (supervisor == null || supervisor.equalsIgnoreCase("null")) {

                    params.put("supvsr", "null");

                } else {

                    params.put("supvsr", supervisor);

                }


                if (assignedUserAdd == null || assignedUserAdd.equalsIgnoreCase("null")) {

                    params.put("assignUsrAdd", "null");

                } else {

                    params.put("assignUsrAdd", assignedUserAdd);
                }


                if (alternateUserAdd == null || alternateUserAdd.equalsIgnoreCase("null")) {

                    params.put("altrUsrAdd", "null");
                } else {

                    params.put("altrUsrAdd", alternateUserAdd);

                }

                if (supervisorAdd == null || supervisorAdd.equalsIgnoreCase("null")) {

                    params.put("supvsrAdd", "null");
                } else {

                    params.put("supvsrAdd", supervisorAdd);

                }

                if (deadlinetype == null || deadlinetype.equalsIgnoreCase("null")) {

                    params.put("radio", "null");
                } else {

                    params.put("radio", deadlinetype);
                }

                if (daterangeAdd == null || daterangeAdd.equalsIgnoreCase("null")) {

                    params.put("daterangeAdd", "null");

                } else {

                    params.put("daterangeAdd", daterangeAdd);
                }

                if (daysAdd == null || daysAdd.equalsIgnoreCase("null")) {

                    params.put("daysAdd", "null");

                } else {


                    params.put("daysAdd", daysAdd);

                }


                if (hrsAdd == null || hrsAdd.equalsIgnoreCase("null")) {

                    params.put("hrsAdd", "null");
                } else {


                    params.put("hrsAdd", hrsAdd);
                }

                if (ip == null || ip.equalsIgnoreCase("null")) {

                    params.put("ip", "null");
                } else {

                    params.put("ip", ip);


                }


                for (Map.Entry<String, String> entry : params.entrySet()) {
                    Log.e("Map values", entry.getKey() + " : " + entry.getValue());
                }

                return params;
            }

        };

        VolleySingelton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }


    public void ApproveRejectTaskWithUploading(final String taskname, final String filepath, final String comment,
                                               final String taskstatus, final String taskid, final String userid, final String username,
                                               final String assignedUser, final String taskOrder, final String alternateUser, final String supervisor,
                                               final String assignedUserAdd, final String alternateUserAdd, final String supervisorAdd, final String deadlinetype,
                                               final String daterangeAdd, final String daysAdd, final String hrsAdd, final String ip, final String pagecount) {
        ///getting the actual path of the image


        String path = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filepath;
        }

        if (path == null) {

            Toast.makeText(this, "Please move your file to internal storage and retry", Toast.LENGTH_LONG).show();

        } else {

            //Uploading code
            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);


                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(this, uploadId, ApiUrl.PROCESS_TASK_APPROVED_REJECT_UPLOADING);
                multipartUploadRequest.addFileToUpload(path, "fileName");


                Log.e("Upload_rate", UPLOAD_RATE);

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filepath.substring(filepath.lastIndexOf("/") + 1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(this, 1, uploadId)));


                uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filepath.substring(filepath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;


                uploadNotificationConfig.getError().message = "Error while uploading";
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filepath.substring(filepath.lastIndexOf("/") + 1);


                uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
                uploadNotificationConfig.getCancelled().title = filepath.substring(filepath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;


                multipartUploadRequest.setNotificationConfig(

                        uploadNotificationConfig
                                .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                .setClearOnActionForAllStatuses(true));


                multipartUploadRequest.addParameter("userid", userid);
                multipartUploadRequest.addParameter("taskid", taskid);


                if (comment == null || comment.trim().length() == 0 || comment.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("comment", "null");

                } else {

                    multipartUploadRequest.addParameter("comment", comment);

                }


                multipartUploadRequest.addParameter("username", username);
                multipartUploadRequest.addParameter("taskstatus", taskstatus);


                if (assignedUser == null || assignedUser.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("asiusr", "null");

                } else {

                    multipartUploadRequest.addParameter("asiusr", assignedUser);

                }


                multipartUploadRequest.addParameter("taskOrder", taskOrder);


                if (alternateUser == null || alternateUser.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("altrUsr", "null");

                } else {

                    multipartUploadRequest.addParameter("altrUsr", alternateUser);

                }

                if (supervisor == null || supervisor.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("supvsr", "null");

                } else {

                    multipartUploadRequest.addParameter("supvsr", supervisor);

                }


                if (assignedUserAdd == null || assignedUserAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("assignUsrAdd", "null");

                } else {

                    multipartUploadRequest.addParameter("assignUsrAdd", assignedUserAdd);
                }


                if (alternateUserAdd == null || alternateUserAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("altrUsrAdd", "null");
                } else {

                    multipartUploadRequest.addParameter("altrUsrAdd", alternateUserAdd);

                }

                if (supervisorAdd == null || supervisorAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("supvsrAdd", "null");
                } else {

                    multipartUploadRequest.addParameter("supvsrAdd", supervisorAdd);

                }

                if (deadlinetype == null || deadlinetype.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("radio", "null");
                } else {

                    multipartUploadRequest.addParameter("radio", deadlinetype);
                }

                if (daterangeAdd == null || daterangeAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("daterangeAdd", "null");

                } else {

                    multipartUploadRequest.addParameter("daterangeAdd", daterangeAdd);
                }

                if (daysAdd == null || daysAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("daysAdd", "null");

                } else {


                    multipartUploadRequest.addParameter("daysAdd", daysAdd);

                }


                if (hrsAdd == null || hrsAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("hrsAdd", "null");
                } else {


                    multipartUploadRequest.addParameter("hrsAdd", hrsAdd);
                }

                if (ip == null || ip.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("ip", "null");
                } else {

                    multipartUploadRequest.addParameter("ip", ip);


                }

                multipartUploadRequest.addParameter("pageCount", pagecount);

                multipartUploadRequest.setMaxRetries(5)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {


                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                                Toast.makeText(context, "Upload Error", Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                try {

                                    Log.e("ser uploading", serverResponse.getBodyAsString());

                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());


                                    String message = jsonObject.getString("message");
                                    String error = jsonObject.getString("error");


                                    if (error.equalsIgnoreCase("false")) {

                                        Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));
                                        popupNotification(taskname + " " + message);
                                        sendBrodcast(message, error);
                                        stopSelf();

                                    } else if (error.equalsIgnoreCase("true")) {

                                        popupNotification(taskname + " " + message);
                                        sendBrodcast(message, error);
                                        stopSelf();
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                popupNotification("Error in " + taskname + " task approve/reject");
                                sendBrodcast("Error in " + taskname + " task approve/reject", "true");
                                stopSelf();
                                //Toast.makeText(ActivityName, "Upload Canceled", Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadId);
                            }
                        });


                multipartUploadRequest.startUpload();


            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


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
                        .fromHtml("<b>" + "Approve/Reject Task" + "</b>")) //the "title" value you sent in your notification
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

    void goToMainActvivty() {


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    void sendBrodcast(String msg, String error) {

        Intent intent = new Intent();
        intent.setAction(getPackageName() + ".CLOSE_APPROVE_REJECT_PB");
        intent.putExtra("msg", msg);
        intent.putExtra("error", error);
        sendBroadcast(intent);

    }


}
