package in.cbslgroup.ezeeoffice.Activity.WorkFlow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Adapters.TimeLineViewAdapter;
import in.cbslgroup.ezeeoffice.Model.TimelineView;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class TaskTrackingActivity extends AppCompatActivity {

    ListView lvTaskTrack;
    RecyclerView rvTaskTrack;
    // Create Timeline rows List
    ArrayList<TimelineView> timelineRowsList = new ArrayList<>();
    ProgressBar progressBar;

    LinearLayout llmain,llNoTaskFound;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_tracking);

        toolbar = findViewById(R.id.toolbar_task_tracking);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });


        Intent intent = getIntent();
        String ticketid = intent.getStringExtra("ticket_id");


        Log.e("ticketid",ticketid);


        rvTaskTrack = findViewById(R.id.rv_taskTracking);
        rvTaskTrack.setLayoutManager(new LinearLayoutManager(this));
        rvTaskTrack.setItemAnimator(new DefaultItemAnimator());
        rvTaskTrack.setHasFixedSize(true);
        rvTaskTrack.setItemViewCacheSize(timelineRowsList.size());

        progressBar = findViewById(R.id.progressBar_task_track);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        llmain = findViewById(R.id.ll_task_track_main);
        llNoTaskFound = findViewById(R.id.ll_task_tack_no_task_found);


        /* for(int i = 0 ;i<5;i++)
         {

             timelineRowsList.add(new TimelineView("23-09-2018","new titile","status ok","T1",Color.argb(255, 0, 0, 0),5,BitmapFactory.decodeResource(getResources(), R.drawable.doc),"HOD approval","Approved by : Mayank"));



         }
              rvTaskTrack.setAdapter(new TimeLineViewAdapter(this,timelineRowsList));
*/

          //getting task track for dynamic ui

        getTaskTrack(ticketid);

       // getTaskTrack("LFSJ_54_1528202978");


    /*    // Create new timeline row (Row Id)
        TimelineRow myRow = new TimelineRow(0);
        // To set the row Date (optional)
        myRow.setDate(new Date());
       // To set the row Title (optional)
        myRow.setTitle("Title");
         // To set the row Description (optional)
        myRow.setDescription("Description");
        // To set the row bitmap image (optional)
        myRow.setImage(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        // To set row Below Line Color (optional)
        myRow.setBellowLineColor(Color.argb(255, 0, 0, 0));
        // To set row Below Line Size in dp (optional)
        myRow.setBellowLineSize(6);
        // To set row Image Size in dp (optional)
        myRow.setImageSize(40);
        // To set background color of the row image (optional)
        myRow.setBackgroundColor(Color.argb(255, 0, 0, 0));
        // To set the Background Size of the row image in dp (optional)
        myRow.setBackgroundSize(60);
        // To set row Date text color (optional)
        myRow.setDateColor(Color.argb(255, 0, 0, 0));
        // To set row Title text color (optional)
        myRow.setTitleColor(Color.argb(255, 0, 0, 0));
        // To set row Description text color (optional)
        myRow.setDescriptionColor(Color.argb(255, 0, 0, 0));

        timelineRowsList.add(myRow);
        // Create the Timeline Adapter
        ArrayAdapter<TimelineRow> myAdapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);

        // Get the ListView and Bind it with the Timeline Adapter
        lvTaskTrack = (ListView) findViewById(R.id.timeline_task_tracking_listview);
        lvTaskTrack.setAdapter(myAdapter);*/



    }


    void getTaskTrack(final String ticketid){

        timelineRowsList.clear();
        llmain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        llNoTaskFound.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TASK_TRACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("task tracking",response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0){

                        llmain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llNoTaskFound.setVisibility(View.VISIBLE);

                    }

                    else{

                        for(int i = 0; i<jsonArray.length();i++){

                           // String taskid = jsonArray.getJSONObject(i).getString("task_id");
                            String taskname = jsonArray.getJSONObject(i).getString("task_name");

                            Log.e("-------task tracking","-----------");
                            Log.e("taskname",taskname);

                            String actionbyname = jsonArray.getJSONObject(i).getString("action_by_name");
                            Log.e("actionbyname",actionbyname);
                           // String ticketid = jsonArray.getJSONObject(i).getString("ticket_id");
                            String actiontime = jsonArray.getJSONObject(i).getString("action_time");
                            Log.e("actionbytime",actiontime);

                            String taskstatus = jsonArray.getJSONObject(i).getString("task_status");
                            Log.e("taskstatus",taskstatus);

                            //String taskorder = jsonArray.getJSONObject(i).getString("task_order");
                            String comment =  jsonArray.getJSONObject(i).getString("comment");
                            Log.e("comment",comment);

                            String profilepic =  jsonArray.getJSONObject(i).getString("profile_pic");
                            Log.e("profilepic",profilepic);

                            Log.e("----------","---------");


                            int color = 0;
                            int belowLinecolor = 0;
                            String onOrBy = "by";


                            if(taskstatus.equalsIgnoreCase("Approved")){

                                color = Color.GREEN;
                                belowLinecolor = getResources().getColor(R.color.green_normal);


                            }

                            else if (taskstatus.equalsIgnoreCase("Rejected")){

                                color = Color.RED;
                                belowLinecolor = Color.RED;

                            }
                            else if (taskstatus.equalsIgnoreCase("Aborted")){

                                color = Color.RED;
                                belowLinecolor = Color.RED;

                            }
                            else if (taskstatus.equalsIgnoreCase("Processed")){

                                color = Color.GREEN;
                                belowLinecolor = getResources().getColor(R.color.green_normal);

                            }
                            else if (taskstatus.equalsIgnoreCase("Complete")){

                                color = Color.GREEN;
                                belowLinecolor = getResources().getColor(R.color.green_normal);

                            }

                            else if (taskstatus.equalsIgnoreCase("Done")){

                                color = Color.GREEN;
                                belowLinecolor = getResources().getColor(R.color.green_normal);

                            }
                            else if (taskstatus.equalsIgnoreCase("Pending")){

                                color = Color.YELLOW;
                                belowLinecolor = getResources().getColor(R.color.light_yellow);

                            }

                            if(taskstatus.equalsIgnoreCase("Pending")){

                                //on or by
                                onOrBy = "on";


                            }

                            timelineRowsList.add(new TimelineView(actiontime,actionbyname,comment,"T"+(i+1),belowLinecolor,5,profilepic,taskname,taskstatus +" "+ onOrBy +" : "+actionbyname,taskstatus));



                        }

                        rvTaskTrack.setAdapter(new TimeLineViewAdapter(TaskTrackingActivity.this,timelineRowsList));

                        llmain.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        llNoTaskFound.setVisibility(View.GONE);



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

                Map<String,String> params = new HashMap<>();
                params.put("ticketid",ticketid);

                return params;
            }
        };

        VolleySingelton.getInstance(TaskTrackingActivity.this).addToRequestQueue(stringRequest);

    }
}
