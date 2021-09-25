package in.cbslgroup.ezeeoffice.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Adapters.NotificationAdapter;
import in.cbslgroup.ezeeoffice.Model.Notification;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class NotificationActivity extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView rvmain;
    NotificationAdapter notificationAdapter;
    List<Notification> notificationList = new ArrayList<>();
    ProgressBar pbMain;
    LinearLayout llNoNotiFound;
    int totalCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        toolbar = findViewById(R.id.toolbar_notification);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();


            }
        });

        rvmain = findViewById(R.id.rv_notification_main);
        pbMain = findViewById(R.id.pb_notification);
        pbMain.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        llNoNotiFound = findViewById(R.id.ll_no_notification_found);

        rvmain.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(notificationList, NotificationActivity.this);
        rvmain.setAdapter(notificationAdapter);
        getTodoNotification("getTodoNotify", MainActivity.userid);


    }

    void getTodoNotification(String action, String userid) {

        totalCount = 0;

        rvmain.setVisibility(View.GONE);
        llNoNotiFound.setVisibility(View.GONE);
        pbMain.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("todo noti", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("todoNotyList");

                        totalCount = jsonArray.length();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String taskname = jsonArray.getJSONObject(i).getString("task_name");
                            String taskdate = jsonArray.getJSONObject(i).getString("task_date");
                            String tasktime = jsonArray.getJSONObject(i).getString("task_time");
                            String tid = jsonArray.getJSONObject(i).getString("id");

                            Notification notification = new Notification();
                            notification.setDate(taskdate + " " + tasktime);
                            notification.setIcon(getResources().getDrawable(R.drawable.todo));
                            notification.setId(tid);
                            notification.setName(taskname);
                            notification.setType("todo");

                            notificationList.add(notification);


                        }


                    } else if (error.equalsIgnoreCase("true")) {

                        //Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();

                        Log.e("todo log", msg);

                    } else {

                        Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                    }


                    notificationAdapter.notifyDataSetChanged();

                    getAptNotification("getAppointNotify", MainActivity.userid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("action", action);
                return params;
            }
        };

        VolleySingelton.getInstance(NotificationActivity.this).addToRequestQueue(stringRequest);


    }

    void getAptNotification(String action, String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("apt noti", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("aptNotyList");

                        totalCount = totalCount + jsonArray.length();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String taskname = jsonArray.getJSONObject(i).getString("title");
                            String taskdate = jsonArray.getJSONObject(i).getString("app_date");
                            String tasktime = jsonArray.getJSONObject(i).getString("app_time");
                            String tid = jsonArray.getJSONObject(i).getString("id");

                            Notification notification = new Notification();
                            notification.setDate(taskdate + " " + tasktime);
                            notification.setIcon(getResources().getDrawable(R.drawable.appointment));
                            notification.setId(tid);
                            notification.setName(taskname);
                            notification.setType("Apt");

                            notificationList.add(notification);


                        }


                    } else if (error.equalsIgnoreCase("true")) {

                        Log.e("apt log", msg);

                    } else {

                        Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                    }

                    notificationAdapter.notifyDataSetChanged();

                    if (totalCount == 0) {

                        rvmain.setVisibility(View.GONE);
                        llNoNotiFound.setVisibility(View.VISIBLE);
                        pbMain.setVisibility(View.GONE);

                    } else {

                        llNoNotiFound.setVisibility(View.GONE);
                        pbMain.setVisibility(View.GONE);
                        rvmain.setVisibility(View.VISIBLE);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("action", action);
                return params;
            }
        };

        VolleySingelton.getInstance(NotificationActivity.this).addToRequestQueue(stringRequest);


    }
}
