package in.cbslgroup.ezeeoffice.Activity.Appointment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.ManageAppointmentAdapter;
import in.cbslgroup.ezeeoffice.Model.AppointmentList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.CustomSpinner;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class ViewAppointmentActivity extends AppCompatActivity {

    RecyclerView rvApt;
    ArrayList<AppointmentList> appointmentList = new ArrayList<>();
    CustomSpinner spSelectDate;

    ArrayList<String> datelist = new ArrayList<>();

    Toolbar toolbar;
    ManageAppointmentAdapter manageAptAdapter;
    ProgressBar pb;
    LinearLayout llNoAptFound;

    public static String PERMISSION_ARCHIEVE_APPOINTMENT,PERMISSION_VIEW_APPOINTMENT,PERMISSION_ADD_APPOINTMENT,PERMISSION_EDIT_APPOINTMENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        toolbar = findViewById(R.id.toolbar_manage_appointment);
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

        rvApt = findViewById(R.id.rv_manage_appointment);
        rvApt.setHasFixedSize(true);
        pb = findViewById(R.id.pb_manage_appointment);
        llNoAptFound = findViewById(R.id.ll_no_todo_found_manage_appointment);
        manageAptAdapter = new ManageAppointmentAdapter(ViewAppointmentActivity.this, appointmentList);


        datelist.add("Today");
        datelist.add("Tomorrow");
        datelist.add("This week");
        datelist.add("All");
        datelist.add("Select Date");

        spSelectDate = findViewById(R.id.sp_manage_appointment);

        //spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, datelist) {

            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }

        };

        spSelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }
                //spSelectDate.setSelection(position);
                String itemselected = spSelectDate.getSelectedItem().toString();

                if (!itemselected.equalsIgnoreCase("Select Date")) {
                    toolbar.setSubtitle(itemselected + " Appointment");
                }

                switch (itemselected) {

                    case "Today":
                        getUserPermission(MainActivity.userid, "getTodaysApt");
                        break;
                    case "Tomorrow":
                        getUserPermission(MainActivity.userid, "getTomorrowApt");
                        break;
                    case "This week":
                        getUserPermission(MainActivity.userid, "getWeeksApt");
                        break;
                    case "All":
                        getUserPermission(MainActivity.userid, "getAllApt");
                        break;

                  /*  case "Select Date":
                        Toast.makeText(TodoActivity.this, "Select Date for search", Toast.LENGTH_SHORT).show();
                        break;*/
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.e("No Todo filter","Selected");

            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectDate.setAdapter(adapter);
        spSelectDate.setSelection(adapter.getCount());

         Intent intent = getIntent();
         String action = intent.getStringExtra("action");

         switch (action){

             case "All":
                 Log.e("here","1");
                 //getUserPermission(MainActivity.userid, "getAllApt");
                 toolbar.setSubtitle(action + " Appointment");
                 spSelectDate.setSelection(3);//All
                 break;
             case "Specific":
                 getUserPermission(MainActivity.userid, "getSpecificDayApt");
                 toolbar.setSubtitle(action + " day Appointment");
                 break;

         }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_apt, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_manage_todo_add_appointment) {

            Intent intent = new Intent(ViewAppointmentActivity.this, AddAppointmentActivity.class);
            intent.putExtra("aptid", "null2");
            startActivityForResult(intent, 123);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String subtitle = toolbar.getSubtitle().toString();

        String action = subtitle.substring(0, subtitle.indexOf("Appointment")).trim();
        Log.e("action in onacres", action);

        switch (action) {

            case "Today":
                getUserPermission(MainActivity.userid, "getTodaysApt");
                toolbar.setSubtitle(action + " Appointment");
                break;
            case "Tomorrow":
                getUserPermission(MainActivity.userid, "getTomorrowApt");
                toolbar.setSubtitle(action + " Appointment");
                break;
            case "This week":
                getUserPermission(MainActivity.userid, "getWeeksApt");
                toolbar.setSubtitle(action + " Appointment");
                break;
            case "All":
                Log.e("here","2");
                getUserPermission(MainActivity.userid, "getAllApt");
                toolbar.setSubtitle(action + " Appointment");
                break;


        }

    }

    void getAppointmentList(String userid, String action) {


        appointmentList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response Todo " + action, response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject item = jsonArray.getJSONObject(i);

                            Log.e("item appointment", item.toString());

                            AppointmentList aptlist = new AppointmentList();
                            aptlist.setId(jsonArray.getJSONObject(i).getString("id"));///
                            aptlist.setEmpId(jsonArray.getJSONObject(i).getString("user_id"));///
                            aptlist.setTitle(jsonArray.getJSONObject(i).getString("title"));//
                            aptlist.setContact(jsonArray.getJSONObject(i).getString("contact"));//
                            aptlist.setContactEmail(jsonArray.getJSONObject(i).getString("contact_email"));//
                            aptlist.setAgenda(jsonArray.getJSONObject(i).getString("agenda"));//
                            aptlist.setNotifyFrequency(getNotiFreqName(jsonArray.getJSONObject(i).getString("notify_frequency")));//
                            aptlist.setNotifyTime(jsonArray.getJSONObject(i).getString("notify_time"));//
                            aptlist.setAppDate(jsonArray.getJSONObject(i).getString("app_date"));///
                            aptlist.setAppTime(jsonArray.getJSONObject(i).getString("app_time"));//
                            aptlist.setIsArcheived(jsonArray.getJSONObject(i).getString("is_archived"));///
                            aptlist.setAppNotes(jsonArray.getJSONObject(i).getString("app_notes"));//


                            appointmentList.add(aptlist);


                        }


                        llNoAptFound.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        rvApt.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {

                        // Toast.makeText(TodoActivity.this, msg, Toast.LENGTH_SHORT).show();

                        pb.setVisibility(View.GONE);
                        rvApt.setVisibility(View.GONE);
                        llNoAptFound.setVisibility(View.VISIBLE);


                    }

                    rvApt.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
                    rvApt.setAdapter(manageAptAdapter);
                    manageAptAdapter.setOnButtonClickListener(new ManageAppointmentAdapter.OnButtonClickListener() {
                        @Override
                        public void OnArchiveButtonListener(String aptid, int pos) {


                            archiveTodo("archiveApt", aptid, pos);

                        }

                        @Override
                        public void OnEditButtonListener(String aptid, String taskname, String taskdate, String taskdesc, String tasktime, String tasknotifreq, String tasknotitime, String agenda, String contactname, String contactemail) {

                            Intent intent = new Intent(ViewAppointmentActivity.this, AddAppointmentActivity.class);
                            //Toast.makeText(TodoActivity.this, tdid, Toast.LENGTH_SHORT).show();
                            intent.putExtra("aptid", aptid);
                            intent.putExtra("title", taskname);
                            intent.putExtra("date", taskdate);
                            intent.putExtra("notes", taskdesc);
                            intent.putExtra("time", tasktime);
                            intent.putExtra("tasknotifreq", tasknotifreq);
                            intent.putExtra("tasknotitime", tasknotitime);
                            intent.putExtra("agenda", agenda);
                            intent.putExtra("contactname", contactname);
                            intent.putExtra("contactemail", contactemail);
                            startActivityForResult(intent, 123);
                        }
                    });
                    manageAptAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewAppointmentActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();


                if (action.equalsIgnoreCase("getTodaysApt")) {

                    params.put("userid", userid);
                    params.put("action", action);

                } else if (action.equalsIgnoreCase("getTomorowApt")) {

                    params.put("userid", userid);
                    params.put("action", action);

                } else if (action.equalsIgnoreCase("getWeeksApt")) {

                    params.put("userid", userid);
                    params.put("action", action);

                } else if (action.equalsIgnoreCase("getAllApt")) {

                    params.put("userid", userid);
                    params.put("action", action);

                }

                else if (action.equalsIgnoreCase("getSpecificDayApt")) {

                    String aid = getIntent().getStringExtra("aid");
                    params.put("aid", aid);
                    params.put("action", action);

                }


                Util.printParams(params,"Here Apt");

                return params;
            }
        };


        VolleySingelton.getInstance(ViewAppointmentActivity.this).addToRequestQueue(stringRequest);


    }

    String getNotiFreqName(String noti) {

        if (noti.equalsIgnoreCase("10")) {
            return "Select Appointment Notification Frequency";
        } else if (noti.equalsIgnoreCase("0")) {
            return "Same day";

        } else {
            return noti + " Day Before";
        }

    }

    void archiveTodo(String action, String aid, int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(ViewAppointmentActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //getAppointmentList(MainActivity.userid,"getAllApt");
                        manageAptAdapter.removeItem(position);
                        appointmentList.remove(position);

                        if (appointmentList.size() == 0) {

                            showNoFileFound();
                        }


                    } else if (error.equalsIgnoreCase("true")) {


                        Toast.makeText(ViewAppointmentActivity.this, msg, Toast.LENGTH_SHORT).show();

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
                params.put("aid", aid);
                params.put("action", action);
                return params;
            }
        };

        VolleySingelton.getInstance(ViewAppointmentActivity.this).addToRequestQueue(stringRequest);

    }

    void showNoFileFound() {

        rvApt.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        llNoAptFound.setVisibility(View.VISIBLE);


    }

    void getUserPermission(final String userid,final String action) {

        appointmentList.clear();
        rvApt.setVisibility(View.GONE);
        llNoAptFound.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);


                    //Todo permissions
                    String todo_view = jsonObject.getString("appoint_view");
                    String todo_add = jsonObject.getString("appoint_add");
                    String todo_edit = jsonObject.getString("appoint_edit");
                    String todo_archieve = jsonObject.getString("appoint_archive");


                    if (todo_view.equalsIgnoreCase("1")) {

                        PERMISSION_VIEW_APPOINTMENT = "1";
                    } else {

                        PERMISSION_VIEW_APPOINTMENT = "0";

                    }

                    if (todo_add.equalsIgnoreCase("1")) {

                        PERMISSION_ADD_APPOINTMENT = "1";
                        toolbar.getMenu().findItem(R.id.action_manage_todo_add_appointment).setVisible(true);


                    } else {

                        PERMISSION_ADD_APPOINTMENT = "0";
                        toolbar.getMenu().findItem(R.id.action_manage_todo_add_appointment).setVisible(false);


                    }

                    if (todo_edit.equalsIgnoreCase("1")) {

                        PERMISSION_EDIT_APPOINTMENT = "1";
                    } else {

                        PERMISSION_EDIT_APPOINTMENT = "0";

                    }

                    if (todo_archieve.equalsIgnoreCase("1")) {

                        PERMISSION_ARCHIEVE_APPOINTMENT= "1";
                    } else {

                        PERMISSION_ARCHIEVE_APPOINTMENT = "0";

                    }



                    getAppointmentList(userid,action);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                // getUserPermission(userid);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");
                return params;
            }
        };


        VolleySingelton.getInstance(ViewAppointmentActivity.this).addToRequestQueue(stringRequest);


    }

    private String setRoles() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("appoint_add").append(",")
                .append("appoint_edit").append(",")
                .append("appoint_archive").append(",")
                .append("appoint_view");

        return stringBuilder.toString();
    }


}
