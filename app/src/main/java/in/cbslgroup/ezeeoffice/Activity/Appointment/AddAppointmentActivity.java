package in.cbslgroup.ezeeoffice.Activity.Appointment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Chip.FilterAdapter;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class  AddAppointmentActivity extends AppCompatActivity {

    //checking subversion

    Spinner spTaskNotiFreq;
    List<String> freqList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView tvTitle, tvDate, tvTime, tvNotiTime, tvNotiFreq, tvNotes, tvInfo, tvContactName, tvContactEmail, tvAgenda;
    EditText etTitle, etDate, etTime, etNotiTime, etNotes, etContactName, etContactEmail, etAgenda;
    String title, date, time, notitime, notifreq, note, agenda, contacName, contactEmail;
    Button btnSubmit, btnCancel;
    DatePickerDialog dpd;
    FilterAdapter filterAdapter;
    //time picker
    int hour, minute;
    ProgressBar progressBar;
    ScrollView svMain;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {

        if (getIntent().getStringExtra("aptid").equalsIgnoreCase("null2")) {

            Intent intent = new Intent(AddAppointmentActivity.this, ViewAppointmentActivity.class);
            setResult(123, intent);
            finish();

        } else if (getIntent().getStringExtra("aptid").equalsIgnoreCase("null")) {

            super.onBackPressed();

        } else {

            Intent intent = new Intent(AddAppointmentActivity.this, ViewAppointmentActivity.class);
            setResult(123, intent);
            finish();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        toolbar = findViewById(R.id.toolbar_add_new_appointment);
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        //textviews
        tvTitle = findViewById(R.id.tv_add_appointment_title);
        tvDate = findViewById(R.id.tv_add_appointment_date);
        tvTime = findViewById(R.id.tv_add_appointment_time);
        tvNotiTime = findViewById(R.id.tv_add_appointment_noti_time);
        tvNotiFreq = findViewById(R.id.tv_add_appointment_noti_freq);
        tvNotes = findViewById(R.id.tv_add_appointment_notes);
        tvInfo = findViewById(R.id.tv_appointment_add_info);
        tvAgenda = findViewById(R.id.tv_add_appointment_agenda);
        tvContactName = findViewById(R.id.tv_add_appointment_contact_name);
        tvContactEmail = findViewById(R.id.tv_add_appointment_contact_email);

        //btn
        btnSubmit = findViewById(R.id.btn_add_appointment_submit);
        btnCancel = findViewById(R.id.btn_add_appointment_cancel);


        //editext
        etTitle = findViewById(R.id.et_add_appointment_title);
        etDate = findViewById(R.id.et_add_appointment_date);
        etTime = findViewById(R.id.et_add_appointment_time);
        etNotiTime = findViewById(R.id.et_add_appointment_noti_time);
        etNotes = findViewById(R.id.et_add_appointment_notes);
        etAgenda = findViewById(R.id.et_add_appointment_agenda);
        etContactName = findViewById(R.id.et_add_appointment_contact_name);
        etContactEmail = findViewById(R.id.et_add_appointment_contact_email);

        //progressbar
        progressBar = findViewById(R.id.pb_add_appointment);


        //Scrollviews
        svMain = findViewById(R.id.sv_add_appointment_main);

        //svMain.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);

        //setting asterisk
        Util.showTextViewsAsMandatory(tvTitle, tvDate, tvTime, tvNotiTime, tvNotiFreq, tvAgenda, tvContactName);

        String info = "Note : Required fields are marked with a (";
        tvInfo.setText(Html.fromHtml(info + "<font color=\"#ff0000\">" + "*" + "</font>" + ")"));

        //spinner
        spTaskNotiFreq = findViewById(R.id.sp_appointment_freq);
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
            etTime.setText(new SimpleDateFormat("hh:mm a").format(date));
            etNotiTime.setText(new SimpleDateFormat("hh:mm a").format(date));

        } catch (Exception e) {


        }


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                dpd = new DatePickerDialog(AddAppointmentActivity.this,
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


                                etDate.setText(new StringBuilder().append(day).append("-")
                                        .append(month).append("-").append(year));
                                etDate.setError(null);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));

                dpd.show();

                disableError();


            }
        });


        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                        try {
                            String time = selectedHour + ":" + selectedMinute;
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                            Date date = sdf.parse(time);
                            etTime.setText(new SimpleDateFormat("hh:mm a").format(date));
                            etTime.setError(null);
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
                mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {


                title = etTitle.getText().toString().trim();
                date = etDate.getText().toString().trim();
                time = etTime.getText().toString().trim();
                notitime = etNotiTime.getText().toString().trim();
                //notifreq = tieTaskNotiFreq.getText().toString().trim();
                note = etNotes.getText().toString().trim();
                contacName = etContactName.getText().toString().trim();
                contactEmail = etContactEmail.getText().toString().trim();
                agenda = etAgenda.getText().toString().trim();


                disableError();


                if (Util.isNullOrEmpty(title)) {

                    etTitle.setError("Please enter title");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(date)) {

                    etDate.setError("Please enter date");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter date", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(time)) {

                    etTime.setError("Please enter time");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter time", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(notitime)) {

                    etNotiTime.setError("Please enter notification time");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter notification time", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(note)) {

                    etNotes.setError("Please enter notes");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter notes", Toast.LENGTH_SHORT).show();


                } else if (Util.isNullOrEmpty(contacName)) {

                    etContactName.setError("Please enter contact name");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter contact name", Toast.LENGTH_SHORT).show();


                }
              /*  else if (Util.isNullOrEmpty(contactEmail)) {

                    etContactEmail.setError("Please enter contact email");

                }
*/
                else if (!Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches()) {

                    etContactEmail.setError("Please enter valid email id");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter valid email id", Toast.LENGTH_SHORT).show();

                } else if (Util.isNullOrEmpty(agenda)) {

                    etAgenda.setError("Please enter agenda");
                    Toast.makeText(AddAppointmentActivity.this, "Please enter agenda", Toast.LENGTH_SHORT).show();


                } else {

                    disableError();

                    String selecteditem = spTaskNotiFreq.getSelectedItem().toString();
                    int tasknotifreq = getNotiFreqId(selecteditem);

                    //Toast.makeText(AddAppointmentActivity.this,tasknotifreq, Toast.LENGTH_SHORT).show();

                    if (tasknotifreq == 10) {

                        Toast.makeText(AddAppointmentActivity.this, "Please select Notification Frequency ", Toast.LENGTH_SHORT).show();


                    } else {

                        String aptidIntent = getIntent().getStringExtra("aptid");
                        //Toast.makeText(AddAppointmentActivity.this,aptidIntent, Toast.LENGTH_SHORT).show();

                        if (aptidIntent.equalsIgnoreCase("null") || aptidIntent.equalsIgnoreCase("null2")) {

                               /* Log.e("tdid", "null");
                                Log.e("empid", useridMain);
                                Log.e("taskdate", taskdate);
                                Log.e("taskname", taskname);
                                Log.e("tasktime", tasktime);
                                Log.e("taskdesc", taskdesc);
                                Log.e("tasknotifreq", String.valueOf(tasknotifreq));
                                Log.e("notitime", notitime);*/

                            // Toast.makeText(AddAppointmentActivity.this,aptidIntent, Toast.LENGTH_SHORT).show();

                            addAppointment("null", MainActivity.userid, date, title, time, note, String.valueOf(tasknotifreq), notitime, agenda, contacName, contactEmail);


                        } else {


                            String aptid = getIntent().getStringExtra("aptid");

                              /*  Log.e("tdid", tdid);
                                Log.e("empid", useridMain);
                                Log.e("taskdate", taskdate);
                                Log.e("taskname", taskname);
                                Log.e("tasktime", tasktime);
                                Log.e("taskdesc", taskdesc);
                                Log.e("tasknotifreq", String.valueOf(tasknotifreq));
                                Log.e("notitime", notitime);*/


                            // Sending side
                            byte[] data = aptid.getBytes(StandardCharsets.UTF_8);
                            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                            Log.e("base64", base64);

                            addAppointment(base64, MainActivity.userid, date, title, time, note, String.valueOf(tasknotifreq), notitime, agenda, contacName, contactEmail);


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


        //fill data if it is there
        fillFormDataIfPresent();


    }


    int getNotiFreqId(String noti) {

        if (noti.equalsIgnoreCase("Select Notification Frequency")) {
            return 10;
        } else if (noti.equalsIgnoreCase("Same day")) {
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

        etTitle.setError(null);
        etDate.setError(null);
        etTime.setError(null);
        etNotiTime.setError(null);
        etNotes.setError(null);
        etContactEmail.setError(null);
        etContactName.setError(null);
        etAgenda.setError(null);

    }

    void addAppointment(String aid, String userId, String date, String title, String time, String notes, String notyfreq, String notyTime, String agenda, String contactName, String contactEmail) {

        String aptid = getIntent().getStringExtra("aptid");

        if (aptid.equalsIgnoreCase("null") || aptid.equalsIgnoreCase("null2")) {

            progressDialog.setTitle("Adding appointment");
        } else {

            progressDialog.setTitle("Updating appointment");

        }

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response addapt", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(AddAppointmentActivity.this, msg, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(AddAppointmentActivity.this, msg, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                        Intent intent = new Intent(AddAppointmentActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {

                        Toast.makeText(AddAppointmentActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddAppointmentActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                 /*
    && isset($_POST['aid'])
        && isset($_POST['user_id'])
        && isset($_POST['app_date'])
        && isset($_POST['title'])
        && isset($_POST['contact'])
        && isset($_POST['contact_email'])
        && isset($_POST['agenda'])
        && isset($_POST['app_time'])
        && isset($_POST['app_notes'])
        && isset($_POST['notification_frequency'])
        && isset($_POST['notify_time'])*/


                params.put("aid", aid);
                params.put("user_id", userId);
                params.put("app_date", date);
                params.put("title", title);
                params.put("contact", contactName);
                params.put("contact_email", contactEmail);
                params.put("app_time", time);
                params.put("agenda", agenda);
                params.put("app_notes", notes);
                params.put("notification_frequency", notyfreq);
                params.put("notify_time", notyTime);
                params.put("action", "saveApt");

                Util.printParams(params, "Add Appointment");


                return params;
            }
        };

        VolleySingelton.getInstance(AddAppointmentActivity.this).addToRequestQueue(stringRequest);
    }

    void fillFormDataIfPresent() {

        String aptid = getIntent().getStringExtra("aptid");

        if (aptid.equalsIgnoreCase("null") || aptid.equalsIgnoreCase("null2")) {


        } else {

            toolbar.setSubtitle("Edit Appointment");

            //fill form data
            String title = getIntent().getStringExtra("title");
            String date = getIntent().getStringExtra("date");
            String notes = getIntent().getStringExtra("notes");
            String time = getIntent().getStringExtra("time");
            String tasknotifreq = getIntent().getStringExtra("tasknotifreq");//spinner
            String tasknotitime = getIntent().getStringExtra("tasknotitime");
            String agenda = getIntent().getStringExtra("agenda");
            String contactname = getIntent().getStringExtra("contactname");
            String contactemail = getIntent().getStringExtra("contactemail");


            etTitle.setText(title);
            etDate.setText(date);
            etNotes.setText(notes);
            etTime.setText(time);
            etNotiTime.setText(tasknotitime);
            etAgenda.setText(agenda);
            etContactName.setText(contactname);
            etContactEmail.setText(contactemail);

            spTaskNotiFreq.setSelection(getNotiFreqId(tasknotifreq) - 1);

            Log.e("if present notifreq", tasknotifreq);
            // spTaskNotiFreq.setSelection(Integer.parseInt(tasknotifreq));


        }


    }


}
