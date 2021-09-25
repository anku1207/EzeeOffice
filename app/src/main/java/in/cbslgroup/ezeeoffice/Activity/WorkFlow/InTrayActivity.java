package in.cbslgroup.ezeeoffice.Activity.WorkFlow;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.InTrayAdapter;
import in.cbslgroup.ezeeoffice.Model.InTray;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class InTrayActivity extends AppCompatActivity {

    RecyclerView rvInTray;
    List<InTray> inTrayList = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;

    List<String> spinnerStatusList = new ArrayList<>();
    List<String> spinnerPriorityList = new ArrayList<>();
    List<String> spinnerAssignByList = new ArrayList<>();

    Spinner spinnerStatus, spinnerPriority, spinnerAssignBy;
    EditText etFrom, etTo;

    LinearLayout llNoFileFound;
    ProgressBar progressBar;

    TextView tvNoOfTask;

    RelativeLayout rlMain;

    List<String> memberlist = new ArrayList<>();
    Toolbar toolbar;

    String camefromGraph = "0";

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(InTrayActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_tray);

        toolbar = findViewById(R.id.toolbar_in_tray_workflow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InTrayActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        rvInTray = findViewById(R.id.rv_in_tray_workflow_list);
        rvInTray.setLayoutManager(new LinearLayoutManager(this));
        rvInTray.setHasFixedSize(true);
        rvInTray.setItemViewCacheSize(inTrayList.size());

        llNoFileFound = findViewById(R.id.ll_in_tray_nofilefound);
        progressBar = findViewById(R.id.progressBar_in_tray);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        rlMain = findViewById(R.id.rl_in_tray_main);
        tvNoOfTask = findViewById(R.id.tv_in_tray_total_task);


        Intent intent = getIntent();
        if (intent.getStringExtra("taskStatus") != null) {



            camefromGraph = "1";
            String taskstatus = intent.getStringExtra("taskStatus");
            Log.e("taskstatus", taskstatus);
            ApplyFilter(MainActivity.userid, "", "", "", "", taskstatus, "");


        } else if (intent.getStringExtra("taskPrior") != null) {

            camefromGraph = "1";
            String taskPriority = intent.getStringExtra("taskPrior");
            Log.e("taskPriority", taskPriority);
            ApplyFilter(MainActivity.userid, "", "", "", "", "", taskPriority);


        } else {

            camefromGraph = "0";
            getInTray(MainActivity.userid);
            getMembers(MainActivity.userid);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intray_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_intray_filter) {

            showFilterDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showFilterDialog() {

        spinnerPriorityList.clear();
        spinnerStatusList.clear();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottomsheet_in_tray_filter, null);

        bottomSheetDialog = new BottomSheetDialog(InTrayActivity.this);
        bottomSheetDialog.setContentView(view);

        Button btnReset = bottomSheetDialog.findViewById(R.id.in_tray_filter_btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                showFilterDialog();

            }
        });


        Button btnApplyFilter = bottomSheetDialog.findViewById(R.id.in_tray_filter_btn_apply_filter);
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = MainActivity.userid;
                String ticketid = "";
                String assignBy = "";
                String startDate = "";
                String endDate = "";
                String taskPrioty = "";
                String taskStatus = "";


                String spStrStatus = spinnerStatus.getSelectedItem().toString();
                String spStrPriority = spinnerPriority.getSelectedItem().toString();
                String spStrAssignBy = spinnerAssignBy.getSelectedItem().toString();

                if (spStrAssignBy.equalsIgnoreCase("Select Assign By")) {

                    assignBy = "";

                } else {

                    assignBy = searchUserid(spStrAssignBy);

                }

                if (spStrStatus.equalsIgnoreCase("Select Status")) {

                    taskStatus = "";

                } else {

                    taskStatus = spStrStatus;

                }

                if (spStrPriority.equalsIgnoreCase("Select Priority")) {

                    taskPrioty = "";

                } else {

                    taskPrioty = spStrPriority;

                }

                if (etFrom.getText().toString().trim().length() == 0) {

                    startDate = "";
                } else {

                    startDate = etFrom.getText().toString();

                }
                if (etTo.getText().toString().trim().length() == 0) {

                    endDate = "";
                } else {

                    endDate = etTo.getText().toString();

                }

                Log.e("-----", "-----");
                Log.e("ticketid", ticketid);
                Log.e("assignBy", assignBy);
                Log.e("startDate", startDate);
                Log.e("endDate", endDate);
                Log.e("taskStatus", taskStatus);
                Log.e("taskPrioty", taskPrioty);
                Log.e("-----", "-----");


                if (taskPrioty.equalsIgnoreCase("Normal")) {

                    taskPrioty = "3";

                } else if (taskPrioty.equalsIgnoreCase("Medium")) {

                    taskPrioty = "2";

                } else if (taskPrioty.equalsIgnoreCase("Urgent")) {

                    taskPrioty = "1";

                }

                ApplyFilter(userid, ticketid, assignBy, startDate, endDate, taskStatus, taskPrioty);

            }
        });

        //Edittext

        etFrom = bottomSheetDialog.findViewById(R.id.in_tray_filter_et_from);
        etTo = bottomSheetDialog.findViewById(R.id.in_tray_filter_et_to);
        etFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etFrom.getRight() - etFrom.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        // Toast.makeText(InTrayActivity.this, "working", Toast.LENGTH_SHORT).show();

                        //Calendar now = Calendar.getInstance();
                        final Calendar c = Calendar.getInstance();

                        DatePickerDialog dpd = new DatePickerDialog(InTrayActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        etFrom.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                        etTo.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);

                                    }
                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));


                        dpd.show();


                        return true;
                    }
                }
                return false;
            }
        });


        etTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etTo.getRight() - etTo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        //Calendar now = Calendar.getInstance();
                        final Calendar c = Calendar.getInstance();
                        //Toast.makeText(InTrayActivity.this, "working", Toast.LENGTH_SHORT).show();
                        DatePickerDialog dpd = new DatePickerDialog(InTrayActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override

                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        etTo.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);

                                    }
                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));


                        dpd.show();

                        return true;
                    }
                }
                return false;
            }
        });

        //Spinners
        spinnerStatus = bottomSheetDialog.findViewById(R.id.in_tray_filter_spinner_select_status);
        spinnerPriority = bottomSheetDialog.findViewById(R.id.in_tray_filter_spinner_select_priority);
        spinnerAssignBy = bottomSheetDialog.findViewById(R.id.in_tray_filter_spinner_select_assignby);


        //spinner list adding data

        spinnerStatusList.add("Pending");
        spinnerStatusList.add("Processed");
        spinnerStatusList.add("Approved");
        spinnerStatusList.add("Rejected");
        spinnerStatusList.add("Aborted");
        spinnerStatusList.add("Complete");
        spinnerStatusList.add("Done");
        spinnerStatusList.add("Select Status");

        spinnerPriorityList.add("Normal");
        spinnerPriorityList.add("Medium");
        spinnerPriorityList.add("Urgent");
        spinnerPriorityList.add("Select Priority");


        ArrayAdapter<String> statusAadapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerStatusList) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };

        statusAadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAadapter);
        spinnerStatus.setSelection(statusAadapter.getCount());


        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerPriorityList) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };

        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);
        spinnerPriority.setSelection(priorityAdapter.getCount());


        getAssignByUsername(MainActivity.userid);

        /*
     "id": "1288",
        "task_name": "for annotation test",
        "doc_id": "7734",
        "task_status": "Pending",
        "task_remarks": "<p>for annotation sign testing</p>",
        "start_date": "2018-03-12 10:35:08",
        "end_date": "2018-03-10 10:10:49",
        "deadline": "2",
        "deadline_type": "Days",
        "priority_id": "3",
        "assign_by": "56",
        "NextTask": "4"*/


    }

    void getInTray(final String userid) {

        inTrayList.clear();

        rlMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        llNoFileFound.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.IN_TRAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getInTray", response);

                    JSONArray jsonArray = new JSONArray(response);


                    if (jsonArray.length() == 0) {


                        rlMain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                        // toolbar.getMenu().findItem(R.id.action_intray_filter).setVisible(false);


                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String taskname = jsonArray.getJSONObject(i).getString("task_name");
                            String docid = jsonArray.getJSONObject(i).getString("doc_id");
                            Log.e("docid", docid);
                            String taskstatus = jsonArray.getJSONObject(i).getString("task_status");
                            String priority = jsonArray.getJSONObject(i).getString("priority");

                            if (priority.length() == 0 || TextUtils.isEmpty(priority) || priority.equalsIgnoreCase("")) {

                                priority = "No Task Priority";
                            }

                            String startdate = jsonArray.getJSONObject(i).getString("start_date");
                            String deadline = jsonArray.getJSONObject(i).getString("deadline");
                            String assignby = jsonArray.getJSONObject(i).getString("assign_by");
                            String taskid = jsonArray.getJSONObject(i).getString("id");
                            String warning = jsonArray.getJSONObject(i).getString("warning");

                            inTrayList.add(new InTray(taskname, priority, taskstatus, startdate, deadline, assignby, docid, taskid, warning));

                        }

                        rvInTray.setAdapter(new InTrayAdapter(inTrayList, InTrayActivity.this));
                        // toolbar.getMenu().findItem(R.id.action_intray_filter).setVisible(true);
                        tvNoOfTask.setText(jsonArray.length() + " task found");
                        rlMain.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.GONE);

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

                return params;
            }
        };

        VolleySingelton.getInstance(InTrayActivity.this).addToRequestQueue(stringRequest);


    }

    void getAssignByUsername(final String userid) {

        spinnerAssignByList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.IN_TRAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("AssignByname", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String assignByname = jsonArray.getString(i);

                        spinnerAssignByList.add(assignByname);

                    }

                    spinnerAssignByList.add("Select Assign By");


                    ArrayAdapter<String> assignByAdapter = new ArrayAdapter<String>(
                            InTrayActivity.this, android.R.layout.simple_spinner_item, spinnerAssignByList) {
                        @Override
                        public int getCount() {
                            // don't display last item. It is used as hint.
                            int count = super.getCount();
                            return count > 0 ? count - 1 : count;
                        }
                    };

                    assignByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerAssignBy.setAdapter(assignByAdapter);
                    spinnerAssignBy.setSelection(assignByAdapter.getCount());

                    bottomSheetDialog.show();

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
                params.put("useridAssignBy", userid);

                return params;
            }
        };


        VolleySingelton.getInstance(InTrayActivity.this).addToRequestQueue(stringRequest);

    }


    void ApplyFilter(final String userid, final String ticketid, final String asinBy, final String startDate, final String endDate, final String taskStats, final String taskPrioty) {

        inTrayList.clear();

        rlMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        llNoFileFound.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.IN_TRAY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Apply filter", response);

                try {

                   // Log.e("intray", response);

                    JSONArray jsonArray = new JSONArray(response);


                    if (jsonArray.length() == 0) {


                        rlMain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);

                        if (camefromGraph.equalsIgnoreCase("0")) {

                            bottomSheetDialog.dismiss();

                        }

                        if(bottomSheetDialog!=null&&bottomSheetDialog.isShowing()){

                            bottomSheetDialog.dismiss();
                        }


                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String taskname = jsonArray.getJSONObject(i).getString("task_name");
                            String docid = jsonArray.getJSONObject(i).getString("doc_id");
                            String taskstatus = jsonArray.getJSONObject(i).getString("task_status");
                            String priority = jsonArray.getJSONObject(i).getString("priority");

                            if (priority.length() == 0 || TextUtils.isEmpty(priority) || priority.equalsIgnoreCase("")) {

                                priority = "No Task Priority";
                            }

                            String startdate = jsonArray.getJSONObject(i).getString("start_date");
                            String deadline = jsonArray.getJSONObject(i).getString("deadline");
                            String assignby = jsonArray.getJSONObject(i).getString("assign_by");
                            String taskid = jsonArray.getJSONObject(i).getString("id");
                            String warning = jsonArray.getJSONObject(i).getString("warning");

                            inTrayList.add(new InTray(taskname, priority, taskstatus, startdate, deadline, assignby, docid, taskid, warning));

                        }

                        rvInTray.setAdapter(new InTrayAdapter(inTrayList, InTrayActivity.this));
                        tvNoOfTask.setText(jsonArray.length() + " task found");
                        rlMain.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.GONE);

                        if (camefromGraph.equalsIgnoreCase("0")) {

                            bottomSheetDialog.dismiss();
                        }

                        if(bottomSheetDialog!=null&&bottomSheetDialog.isShowing()){

                            bottomSheetDialog.dismiss();
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
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("userId", userid);
                params.put("ticketid", ticketid);
                params.put("asinBy", asinBy);
                params.put("startDate", startDate);
                params.put("endDate", endDate);
                params.put("taskStats", taskStats);
                params.put("taskPrioty", taskPrioty);

                Util.printParams(params, "Applyfilter");

                return params;


            }
        };

        VolleySingelton.getInstance(InTrayActivity.this).addToRequestQueue(stringRequest);


    }

    void getMembers(final String userid) {

        memberlist.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String username = jsonArray.getJSONObject(i).getString("user_name");

                        memberlist.add(username);


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
                params.put("useridGM", userid);

                return params;
            }

        };

        VolleySingelton.getInstance(InTrayActivity.this).addToRequestQueue(stringRequest);
    }


    //get userid of the specific name
    String searchUserid(String name) {

        String userid = null;

        //Log.e("name",name);

        for (String n : memberlist
                ) {

            Log.e("names", n);

            if (n.contains(name)) {

                userid = n.substring(n.lastIndexOf("&&") + 2);
                // System.out.println("yes");
                return userid;

            } else {

                userid = "null";
                //  System.out.println("no");

            }


        }

        return userid;

    }


}
