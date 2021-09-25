package in.cbslgroup.ezeeoffice.Activity.WorkFlow;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.TasktrackListAdapter;
import in.cbslgroup.ezeeoffice.Model.TaskTrackList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class TaskTrackStatusActivity extends AppCompatActivity {

    private static final String TAG = "TaskTrackStatusActivity";
    private static final int PAGE_START = 0;
    private static int START = 0;
    private static int PER_PAGE = 20;
    TasktrackListAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    ProgressBar progressBar,pgRv;
    List<TaskTrackList> taskTrackList = new ArrayList<>();
    Toolbar toolbar;
    RelativeLayout rlTaskTrackStatusMain;
    LinearLayout llNoTaskFound, llNofileFound;
    TextView tvFileCount;
    String taskStatus;
    int page_number = 1;
    int totalfiles= 0;
    int count = 0;
    int pageCount = 0;

    Boolean isScrolling = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_track_status);

        toolbar = findViewById(R.id.toolbar_tasktrackstatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskTrackStatusActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                START = 0;
            }
        });



        progressBar = findViewById(R.id.progressBar_task_track_list);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        pgRv = findViewById(R.id.pg_rv_task_track_status);
        rlTaskTrackStatusMain = findViewById(R.id.rl_task_track_status_main);
        tvFileCount = findViewById(R.id.tv_tasktrackstatus_nooffile);
        llNoTaskFound = findViewById(R.id.ll_task_tack_list_no_task_found);

        getAllTaskTrackList(MainActivity.userid, String.valueOf(page_number));

        rv = findViewById(R.id.rv_tasktrackstatus_list);
        rv.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setItemPrefetchEnabled(false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemViewCacheSize(taskTrackList.size());
        rv.setItemAnimator(null);


        adapter = new TasktrackListAdapter(taskTrackList,rv,TaskTrackStatusActivity.this);
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){

                    isScrolling = true;
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int currentItems = linearLayoutManager.getChildCount();
                int totalItems = adapter.getItemCount();
                int scrolledoutitems = linearLayoutManager.findFirstVisibleItemPosition();

                Log.e("currentitems", String.valueOf(currentItems));
                Log.e("totalItems", String.valueOf(totalItems));
                Log.e("scrolledoutitems", String.valueOf(scrolledoutitems));

                Log.e("isScrolling", String.valueOf(isScrolling));
                Log.e("logic", String.valueOf(isScrolling && (currentItems + totalItems == scrolledoutitems)));



                if(isScrolling && (currentItems + scrolledoutitems == totalItems )){

                    if(taskTrackList.size()!=totalfiles){

                        isScrolling = false;

                        taskTrackList.add(null);
                        adapter.notifyItemInserted(taskTrackList.size()-1);
                        taskTrackList.remove(taskTrackList.size()-1);
                        adapter.notifyItemRemoved(taskTrackList.size());

                        page_number = page_number+1;

                        // Log.e("pagenumber", String.valueOf(page_number));

                        LoadMore(MainActivity.userid, String.valueOf(page_number));

                    }

                    else{

                        Log.e("end","reached the end");

                    }

                }



            }
        });


       /* if(taskTrackList.size()!=totalfiles){


            taskTrackList.add(null);
            adapter.notifyItemInserted(taskTrackList.size()-1);
            taskTrackList.remove(taskTrackList.size()-1);
            // adapter.notifyItemRemoved(taskTrackList.size());
            rv.notifyItemRemoved(taskTrackList,taskTrackList.size());

            page_number = page_number+1;

            Log.e("page count between", String.valueOf(page_number));

                           *//*int index = contactslist.size();
                            Log.e("index", String.valueOf(index));
                            int end = index+10;
                            Log.e("end", String.valueOf(end));*//*

            LoadMore(MainActivity.userid, String.valueOf(page_number));

        }
        else{

            //Toast.makeText(TaskTrackStatusActivity.this, "All Data Loaded", Toast.LENGTH_SHORT).show();
            rv.setNoMore(true);


        }
*/
       /* adapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {

                Log.e("page count before", String.valueOf(page_number));
                Log.e("list size", String.valueOf(taskTrackList.size()));


                 if(taskTrackList.size()!=totalfiles){


                     taskTrackList.add(null);
                     adapter.notifyItemInserted(taskTrackList.size()-1);

                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {

                             taskTrackList.remove(taskTrackList.size()-1);
                             adapter.notifyItemRemoved(taskTrackList.size());

                             page_number = page_number+1;

                             Log.e("page count between", String.valueOf(page_number));

                            *//*int index = contactslist.size();
                            Log.e("index", String.valueOf(index));
                            int end = index+10;
                            Log.e("end", String.valueOf(end));*//*

                             getAllTaskTrackList(MainActivity.userid, String.valueOf(page_number));


                         }
                     },2000);



                 }





                 else{

                     Toast.makeText(TaskTrackStatusActivity.this, "No more task", Toast.LENGTH_SHORT).show();

                 }




            }

            // Log.e("page count after", String.valueOf(page_number));


        });*/

    }

    void getAllTaskTrackList(final String userid , final String page) {



        rlTaskTrackStatusMain.setVisibility(View.GONE);
        llNoTaskFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TASK_TRACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response tracklist", response);

                try {
                    //JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = new JSONObject(response);

                   totalfiles = Integer.parseInt(jsonObject.getString("totalfiles"));

                   Log.e("totalfiles", String.valueOf(totalfiles));


                   pageCount = Integer.parseInt(jsonObject.getString("pageCount"));

                    Log.e("pagecount", String.valueOf(pageCount));



                   JSONArray jsonArray = jsonObject.getJSONArray("list");

                    //totalfiles = jsonArray.length();

                    if (jsonArray.length() == 0) {

                        rlTaskTrackStatusMain.setVisibility(View.GONE);
                        llNoTaskFound.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String ticketid = jsonArray.getJSONObject(i).getString("ticket_id");
                            String ticketdate = jsonArray.getJSONObject(i).getString("start_date");
                            String taskstatus = jsonArray.getJSONObject(i).getString("task_status");
                            String actionby = jsonArray.getJSONObject(i).getString("assign_by");
                            String docid = jsonArray.getJSONObject(i).getString("doc_id");

                            //getLastStatus(ticketid);


                            taskTrackList.add(new TaskTrackList(ticketid, taskstatus, ticketdate, actionby, docid));

                        }

                        adapter = new TasktrackListAdapter(taskTrackList,rv,TaskTrackStatusActivity.this);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        //adapter.setLoaded();



                        tvFileCount.setText(totalfiles + " task found");

                        rlTaskTrackStatusMain.setVisibility(View.VISIBLE);
                        llNoTaskFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);



                      /*  if(count>1){

                            Log.e("no more data","no more data");

                        }else{

                            rlTaskTrackStatusMain.setVisibility(View.VISIBLE);
                            llNoTaskFound.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                        }*/

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
                params.put("page", page);

                // params.put("start",start);
                //  params.put("per_page",perpage);
                return params;
            }
        };

        VolleySingelton.getInstance(TaskTrackStatusActivity.this).addToRequestQueue(stringRequest);


    }

    void LoadMore(final String userid , final String page) {

       pgRv.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TASK_TRACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response tracklist", response);

                try {
                    //JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = new JSONObject(response);

                    totalfiles = Integer.parseInt(jsonObject.getString("totalfiles"));
                    pageCount = Integer.parseInt(jsonObject.getString("pageCount"));



                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if(jsonArray.length()==0){

                        pgRv.setVisibility(View.GONE);

                    }

                    else{


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String ticketid = jsonArray.getJSONObject(i).getString("ticket_id");
                            String ticketdate = jsonArray.getJSONObject(i).getString("start_date");
                            String taskstatus = jsonArray.getJSONObject(i).getString("task_status");
                            String actionby = jsonArray.getJSONObject(i).getString("assign_by");
                            String docid = jsonArray.getJSONObject(i).getString("doc_id");


                            taskTrackList.add(new TaskTrackList(ticketid, taskstatus, ticketdate, actionby, docid));

                        }



                        adapter.notifyDataSetChanged();
                        pgRv.setVisibility(View.GONE);
                        tvFileCount.setText(totalfiles + " task found");


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
                params.put("page", page);

                return params;
            }
        };

        VolleySingelton.getInstance(TaskTrackStatusActivity.this).addToRequestQueue(stringRequest);


    }

    void getLastStatus(final String ticketid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TASK_TRACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    taskStatus = jsonArray.getJSONObject(jsonArray.length() - 1).getString("task_status");

                    Log.e("taskstatus1", taskStatus);

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
                params.put("ticketid", ticketid);

                return params;
            }
        };

        VolleySingelton.getInstance(TaskTrackStatusActivity.this).addToRequestQueue(stringRequest);


    }


}
