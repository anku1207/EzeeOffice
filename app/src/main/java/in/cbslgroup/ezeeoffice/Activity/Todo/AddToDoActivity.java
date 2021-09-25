package in.cbslgroup.ezeeoffice.Activity.Todo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Chip.FilterAdapter;
import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.Chip.UsernameCompletionView;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class AddToDoActivity extends AppCompatActivity {

    Spinner spTaskNotiFreq;
    List<String> freqList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView tvTaskName, tvTaskDate, tvTaskTime, tvNotiTime, tvTaskNotiFreq, tvSelectUser, tvTaskDesc, tvInfo;
    EditText etTaskName, etTaskDate, etTaskTime, etNotiTime, etTaskDesc;
    String taskname, taskdate, tasktime, notitime, notifreq, taskdesc;
    Button btnSubmit, btnCancel;
    DatePickerDialog dpd;
    ArrayList<User> userArrayList = new ArrayList<>();
    FilterAdapter filterAdapter;
    //time picker
    int hour, minute;
    ProgressBar progressBar;
    ScrollView svMain;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    private UsernameCompletionView autoCompleteTextView;

    @Override
    public void onBackPressed() {

        if (getIntent().getStringExtra("todoid").equalsIgnoreCase("null2")) {

            Intent intent = new Intent(AddToDoActivity.this, TodoActivity.class);
            setResult(123, intent);
            finish();

        } else if (getIntent().getStringExtra("todoid").equalsIgnoreCase("null")) {

            super.onBackPressed();

        } else {

            Intent intent = new Intent(AddToDoActivity.this, TodoActivity.class);
            setResult(123, intent);
            finish();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        toolbar = findViewById(R.id.toolbar_add_new_todo);
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

        //textviews
        tvTaskName = findViewById(R.id.tv_add_todo_task_name);
        tvTaskDate = findViewById(R.id.tv_add_todo_task_date);
        tvTaskTime = findViewById(R.id.tv_add_todo_task_time);
        tvNotiTime = findViewById(R.id.tv_add_todo_noti_time);
        tvTaskNotiFreq = findViewById(R.id.tv_add_todo_noti_freq);
        tvSelectUser = findViewById(R.id.tv_add_todo_select_user);
        tvTaskDesc = findViewById(R.id.tv_add_todo_task_desc);
        tvInfo = findViewById(R.id.tv_todo_add_info);

        //btn
        btnSubmit = findViewById(R.id.btn_add_todo_submit);
        btnCancel = findViewById(R.id.btn_add_todo_cancel);


        //editext

        etTaskName = findViewById(R.id.et_add_todo_task_name);
        etTaskDate = findViewById(R.id.et_add_todo_task_date);
        etTaskTime = findViewById(R.id.et_add_todo_task_time);
        etNotiTime = findViewById(R.id.et_add_todo_noti_time);
        etTaskDesc = findViewById(R.id.et_add_todo_task_desc);

        //progressbar

        progressBar = findViewById(R.id.pb_add_todo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        //Scrollviews
        svMain = findViewById(R.id.sv_add_todo_main);

        svMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        autoCompleteTextView = findViewById(R.id.autocomplete_tv_select_user_add_todo_activity);
        autoCompleteTextView.setThreshold(1);


        //Set the listener that will be notified of changes in the Tokenlist
        autoCompleteTextView.setTokenListener(new TokenCompleteTextView.TokenListener<User>() {
            @Override
            public void onTokenAdded(User token) {

                Log.e("Chip", "Added");

            }

            @Override
            public void onTokenRemoved(User token) {

                Log.e("Chip", "Added");
            }
        });

        //Set the action to be taken when a Token is clicked
        autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

        //setting asterisk
        Util.showTextViewsAsMandatory(tvTaskName, tvTaskDate, tvTaskTime, tvNotiTime, tvTaskNotiFreq, tvSelectUser, tvTaskDesc);

        String info = "Note : Required fields are marked with a (";
        tvInfo.setText(Html.fromHtml(info + "<font color=\"#ff0000\">" + "*" + "</font>" + ")"));

        //spinner
        spTaskNotiFreq = findViewById(R.id.sp_todo_freq);
        freqList.add("Same Day");
        freqList.add("1 Day Before");
        freqList.add("2 Day Before");
        freqList.add("3 Day Before");
        freqList.add("4 Day Before");
        freqList.add("5 Day Before");
        freqList.add("6 Day Before");
        freqList.add("7 Day Before");
        freqList.add("Select Task Notification Frequency");

        //spinner adapter
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, freqList) {

            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }

        };


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTaskNotiFreq.setAdapter(adapter);
        spTaskNotiFreq.setSelection(adapter.getCount());


        //setting current date and time to editexts

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        try {

            String time = today.hour + ":" + today.minute;
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            Date date = sdf.parse(time);
            etTaskTime.setText(new SimpleDateFormat("hh:mm a").format(date));
            etNotiTime.setText(new SimpleDateFormat("hh:mm a").format(date));

        } catch (Exception e) {


        }


        etTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                dpd = new DatePickerDialog(AddToDoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker view, int year,

                                                  int monthOfYear, int dayOfMonth) {


                                String month = String.valueOf(monthOfYear + 1);
                                String day = String.valueOf(dayOfMonth);

                                Log.e("day", day);
                                Log.e("month", month);


                                if (Integer.parseInt(day) >= 10) {

                                    day = String.valueOf((day));

                                } else {

                                    day = "0" + (day);
                                }


                                if (Integer.parseInt(month) >= 10) {

                                    month = String.valueOf(month);

                                } else {

                                    month = "0" + (month);
                                }


                                etTaskDate.setText(new StringBuilder().append(day).append("-")
                                        .append(month).append("-").append(year));
                                etTaskDate.setError(null);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                dpd.show();

                disableError();


            }
        });


        etTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddToDoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                        try {
                            String time = selectedHour + ":" + selectedMinute;
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                            Date date = sdf.parse(time);
                            etTaskTime.setText(new SimpleDateFormat("hh:mm a").format(date));
                            etTaskTime.setError(null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });


        etNotiTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddToDoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                        try {
                            String time = selectedHour + ":" + selectedMinute;
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                            Date date = sdf.parse(time);
                            etNotiTime.setText(new SimpleDateFormat("hh:mm a").format(date));
                            etNotiTime.setError(null);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                taskname = etTaskName.getText().toString().trim();
                taskdate = etTaskDate.getText().toString().trim();
                tasktime = etTaskTime.getText().toString().trim();
                notitime = etNotiTime.getText().toString().trim();
                //notifreq = tieTaskNotiFreq.getText().toString().trim();
                taskdesc = etTaskDesc.getText().toString().trim();


                disableError();


                if (Util.isNullOrEmpty(taskname)) {

                    etTaskName.setError("Please enter task name");
                    Toast.makeText(AddToDoActivity.this, "Please enter task name", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(taskdate)) {

                    etTaskDate.setError("Please enter task date");
                    Toast.makeText(AddToDoActivity.this, "Please enter task date", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(tasktime)) {

                    etTaskTime.setError("Please enter task time");
                    Toast.makeText(AddToDoActivity.this, "Please enter task time", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(notitime)) {

                    etNotiTime.setError("Please enter notification time");
                    Toast.makeText(AddToDoActivity.this, "Please enter notification time", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(taskdesc)) {

                    etTaskDesc.setError("Please enter task description");
                    Toast.makeText(AddToDoActivity.this, "Please enter task description", Toast.LENGTH_SHORT).show();


                } else {

                    disableError();

                    String selecteditem = spTaskNotiFreq.getSelectedItem().toString();
                    int tasknotifreq = getNotiFreqId(selecteditem);
                    if (tasknotifreq == 10) {

                        Toast.makeText(AddToDoActivity.this, "Please select Task Notification Frequency ", Toast.LENGTH_SHORT).show();
                    } else {

                        List<User> tokens = autoCompleteTextView.getObjects();
                        if (tokens.size() == 0) {

                            Toast.makeText(AddToDoActivity.this, "Please Select User", Toast.LENGTH_SHORT).show();

                        } else {

                            StringBuilder userids = new StringBuilder();

                            for (int i = 0; i < tokens.size(); i++) {

                                String userid = tokens.get(i).getUserid();
                                userids.append(userid).append(",");

                            }

                            Log.e("todo userids 1 ", userids.toString());

                            String useridMain = userids.toString();

                            if (useridMain.endsWith(",")) {

                                useridMain = useridMain.substring(0, useridMain.length() - 1);

                            }

                            Log.e("todo userids 2 ", useridMain);


                            ////todoid  == null because we are adding a new toodo

                            Log.e("todoid intent", getIntent().getStringExtra("todoid"));

                            if (getIntent().getStringExtra("todoid").equalsIgnoreCase("null") || getIntent().getStringExtra("todoid").equalsIgnoreCase("null2")) {

                                Log.e("tdid", "null");
                                Log.e("empid", useridMain);
                                Log.e("taskdate", taskdate);
                                Log.e("taskname", taskname);
                                Log.e("tasktime", tasktime);
                                Log.e("taskdesc", taskdesc);
                                Log.e("tasknotifreq", String.valueOf(tasknotifreq));
                                Log.e("notitime", notitime);

                                addTodo("null", useridMain, taskdate, taskname, tasktime, taskdesc, String.valueOf(tasknotifreq), notitime);

                            } else {


                                String tdid = getIntent().getStringExtra("todoid");

                                Log.e("tdid", tdid);
                                Log.e("empid", useridMain);
                                Log.e("taskdate", taskdate);
                                Log.e("taskname", taskname);
                                Log.e("tasktime", tasktime);
                                Log.e("taskdesc", taskdesc);
                                Log.e("tasknotifreq", String.valueOf(tasknotifreq));
                                Log.e("notitime", notitime);


                                // Sending side
                                byte[] data = tdid.getBytes(StandardCharsets.UTF_8);
                                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                                Log.e("base64", base64);

                                addTodo(base64, useridMain, taskdate, taskname, tasktime, taskdesc, String.valueOf(tasknotifreq), notitime);


                            }


                        }


                    }
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();


            }
        });

        getMemberList(MainActivity.userid);

        //fill form data if edit option is selected
        fillFormDataIfPresent();

    }


    void getMemberList(String userid) {

        userArrayList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("get todo mem", response);
                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String username = jsonArray.getJSONObject(i).getString("name");
                            String email = jsonArray.getJSONObject(i).getString("email");
                            String userid = jsonArray.getJSONObject(i).getString("userid");


                            User user = new User(username + "(" + email + ")", R.drawable.ic_avatar, userid);
                            userArrayList.add(user);

                        }


                        //Initializing and attaching adapter for AutocompleteTextView
                        filterAdapter = new FilterAdapter(AddToDoActivity.this, R.layout.item_user, userArrayList);
                        autoCompleteTextView.setAdapter(filterAdapter);
                        autoCompleteTextView.allowDuplicates(false);
                        //autoCompleteTextView.setShowAlways(true);
                        // spTaskNotiFreq.setSelection(4);
                        // autoCompleteTextView.addObject(new User("Ankit",R.drawable.ic_user,"23"));
                        //autoCompleteTextView.showDropDown();


                        progressBar.setVisibility(View.GONE);
                        svMain.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(AddToDoActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }


                    autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            autoCompleteTextView.showDropDown();
                        }
                    });
                    autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            autoCompleteTextView.showDropDown();
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            autoCompleteTextView.showDropDown();

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            autoCompleteTextView.showDropDown();
                        }
                    });
                    autoCompleteTextView.allowDuplicates(false);


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
                params.put("action", "getAllGroupMembers");

                return params;
            }
        };

        VolleySingelton.getInstance(AddToDoActivity.this).addToRequestQueue(stringRequest);


    }

    int getNotiFreqId(String noti) {

        if (noti.equalsIgnoreCase("Select Task Notification Frequency")) {

            return 10;
        }

        if (noti.equalsIgnoreCase("Same day")) {
            return 0;

        } else if (noti.equalsIgnoreCase("1 Day Before")) {
            return 1;
        } else if (noti.equalsIgnoreCase("2 Day Before")) {
            return 2;
        } else if (noti.equalsIgnoreCase("3 Day Before")) {
            return 3;
        } else if (noti.equalsIgnoreCase("4 Day Before")) {
            return 4;
        } else if (noti.equalsIgnoreCase("5 Day Before")) {
            return 5;
        } else if (noti.equalsIgnoreCase("6 Day Before")) {
            return 6;
        } else if (noti.equalsIgnoreCase("7 Day Before")) {
            return 7;
        }

        return 10;
    }

    void disableError() {

        etTaskName.setError(null);
        etTaskDate.setError(null);
        etTaskTime.setError(null);
        etNotiTime.setError(null);
        etTaskDesc.setError(null);

    }

    void addTodo(String tdId, String empId, String taskDate, String taskName, String taskTime, String taskDesc, String taskNotyfreq, String taskNotyTime) {

        String todoid = getIntent().getStringExtra("todoid");

        if (todoid.equalsIgnoreCase("null") || todoid.equalsIgnoreCase("null2")) {

            progressDialog.setTitle("Adding Todo");

        }

        else{


            progressDialog.setTitle("Updating Todo");


        }

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response addtodo", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(AddToDoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(AddToDoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(AddToDoActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {


                        Toast.makeText(AddToDoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(AddToDoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                 /*
    * $tdid,
    $emp_id,
    $task_date,
    $task_name,
    $task_time,
    $task_description,
    $task_notification_frequency,
    $task_notify_time
    * */

                params.put("tdid", tdId);
                params.put("emp_id", empId);
                params.put("task_date", taskDate);
                params.put("task_name", taskName);
                params.put("task_time", taskTime);
                params.put("task_description", taskDesc);
                params.put("task_notification_frequency", taskNotyfreq);
                params.put("task_notify_time", taskNotyTime);
                params.put("action", "saveTodo");

                Util.printParams(params, "Add Todo");


                return params;
            }
        };

        VolleySingelton.getInstance(AddToDoActivity.this).addToRequestQueue(stringRequest);
    }

    void fillFormDataIfPresent() {

        String todoid = getIntent().getStringExtra("todoid");

        if (todoid.equalsIgnoreCase("null") || todoid.equalsIgnoreCase("null2")) {


        } else {


            //fill form data
            toolbar.setSubtitle("Edit Todo");
            String taskname = getIntent().getStringExtra("taskname");
            String taskdate = getIntent().getStringExtra("taskdate");
            String taskdesc = getIntent().getStringExtra("taskdesc");
            String tasktime = getIntent().getStringExtra("tasktime");
            String tasknotifreq = getIntent().getStringExtra("tasknotifreq");//spinner
            String tasknotitime = getIntent().getStringExtra("tasknotitime");

            etTaskName.setText(taskname);
            etTaskDate.setText(taskdate);
            etTaskDesc.setText(taskdesc);
            etTaskTime.setText(tasktime);
            etNotiTime.setText(tasknotitime);

            spTaskNotiFreq.setSelection(getNotiFreqId(tasknotifreq));

            getSelectedMembers(todoid);

            Log.e("if present notifreq", tasknotifreq);
            // spTaskNotiFreq.setSelection(Integer.parseInt(tasknotifreq));


        }


    }

    void getSelectedMembers(String todoid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("selected mem Todo", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("true")) {


                    } else if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String email = jsonArray.getJSONObject(i).getString("email");
                            String userid = jsonArray.getJSONObject(i).getString("userid");
                            String name = jsonArray.getJSONObject(i).getString("name");

                            User user = new User(name + "(" + email + ")", R.drawable.ic_avatar, userid);
                            autoCompleteTextView.addObject(user);

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
                params.put("tdid", todoid);
                params.put("action", "getSelectedMembers");
                return params;
            }
        };

        VolleySingelton.getInstance(AddToDoActivity.this).addToRequestQueue(stringRequest);

    }


}
