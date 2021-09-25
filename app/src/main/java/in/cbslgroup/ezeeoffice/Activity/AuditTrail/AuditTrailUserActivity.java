package in.cbslgroup.ezeeoffice.Activity.AuditTrail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.AuditListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.SearchableSpinnerAdapter;
import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.Model.AuditTrail;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.CustomSpinner;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class AuditTrailUserActivity extends AppCompatActivity {


    protected Handler handler;
    DrawerLayout drawer;
    View navView;
    SessionManager session;
    String session_userid;
    List<AuditTrail> auditTrailList = new ArrayList<>();
    RecyclerView rvAudit;
    AuditListAdapter auditListAdapter;
    CustomSpinner spinnerMembersList;
    List<String> spinnermemberlist = new ArrayList<>();
    List<String> spinnermemberfullwithidList = new ArrayList<>();
    Button btnSearchAuditUser;
    String spinnerUserid;
    ProgressBar progressBar;
    SearchView searchView;
    LinearLayout llNoFileFound, ll_user_audit_main, ll_user_nooflogs;
    TextView tvCountPanel, tvMoveToLast;
    int startItemIndex = 0, lastItemIndex = 10;
    LinearLayoutManager linearLayoutManager;
    ProgressBar pbInfinte;
    Boolean isScrolling = false;

    //for custom toast
    Toast toast;
    View toastlayout;
    TextView tvToastmsg;
    ///

    int totalLogs, totalVisibleItems = 10;
    Toast toastResult;

    TextView tvNoResultFound;
    Button btnScrollupdown;
    Vibrator vibrator;
    Toolbar toolbar;


    List<User> userlist = new ArrayList<>();
    SearchableSpinnerAdapter searchableSpinnerAdapter;
    TextView tvDialogUsername,tvDialogUserid;

    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_trail_user);

        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // userid
        session_userid = user.get(SessionManager.KEY_USERID);

        toolbar = findViewById(R.id.toolbar_audit_user);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        llNoFileFound = findViewById(R.id.ll_audit_user_no_data_found);
        ll_user_audit_main = findViewById(R.id.ll_audit_trail_user_main);

        drawer = findViewById(R.id.user_audit_left_drawer);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));

        NavigationView navigationView = findViewById(R.id.user_audit_nav_view);
        navView = navigationView.getHeaderView(0);

        progressBar = findViewById(R.id.progressBarUserAudit);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        searchView = findViewById(R.id.searchview_audit_user);
        tvCountPanel = findViewById(R.id.tv_useraudit_resultcount);
        tvMoveToLast = findViewById(R.id.tv_useraudit_scrolllast);
        pbInfinte = findViewById(R.id.pb_user_audit_infintescroll);
        ll_user_nooflogs = findViewById(R.id.ll_audit_user_nooflogs);
        btnScrollupdown = findViewById(R.id.btn_audit_user_hover_scrollupdown);
        btnScrollupdown.setVisibility(View.GONE);
        tvNoResultFound = findViewById(R.id.rv_tv_audit_no_result_found);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        toastResult = new Toast(AuditTrailUserActivity.this);


        //recyclerview
        rvAudit = findViewById(R.id.rv_user_audit);
        linearLayoutManager = new LinearLayoutManager(this);
        rvAudit.setLayoutManager(linearLayoutManager);
        rvAudit.setHasFixedSize(true);

        //searchView.setQuery("Search text",false);
        Util.setSearchviewTextSize(searchView,12,"Search by username , action performed etc");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                List<AuditTrail> filteredList = new ArrayList<>();

                for (int i = 0; i < auditTrailList.size(); i++) {

                    final String username = auditTrailList.get(i).getUsername().toLowerCase();
                    final String ip = auditTrailList.get(i).getIp().toLowerCase();
                    //final String startdate = auditTrailList.get(i).getActionStartDate().toLowerCase();
                    //final String enddate = auditTrailList.get(i).getActionEndDate().toLowerCase();
                    final String actionperformed = auditTrailList.get(i).getActionPerformed().toLowerCase();

                    final String sno = auditTrailList.get(i).getSno().toLowerCase();
                    final String serialNum = sno.substring(sno.lastIndexOf(" ") + 1);
                    Log.e("serialNum", serialNum);

                    if (serialNum.contains(newText) || actionperformed.contains(newText) || username.contains(newText) || ip.contains(newText)) {

                        filteredList.add(auditTrailList.get(i));

                    }
                }

                rvAudit.setLayoutManager(new LinearLayoutManager(AuditTrailUserActivity.this));
                auditListAdapter = new AuditListAdapter(AuditTrailUserActivity.this, filteredList, rvAudit);

                if (filteredList.size() == 0) {

                    Log.e("audittrailistsize", String.valueOf(auditTrailList.size()));
                    tvNoResultFound.setVisibility(View.VISIBLE);

                    rvAudit.setAdapter(auditListAdapter);
                    auditListAdapter.notifyDataSetChanged();


                } else {


                    tvNoResultFound.setVisibility(View.GONE);
                    rvAudit.setAdapter(auditListAdapter);
                    auditListAdapter.notifyDataSetChanged();


                }

                // auditListAdapter.setDisplayCount(totalLogs);

                if (auditListAdapter.getItemCount() == 0) {

                    //tvCountPanel.setText("Showing 0 to 0 of 0 entries");
                    tvCountPanel.setText("0 result found");
                } else {

                    // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries");
                    tvCountPanel.setText(auditListAdapter.getItemCount() + " results found");

                }
                return false;
            }
        });




        tvMoveToLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rvAudit.scrollToPosition(auditTrailList.size() - 1);

            }
        });

       /* tvMoveToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rvAudit.smoothScrollToPosition(0);

            }
        });*/

        getAllAuditData(session_userid);

       // groupMemberList(session_userid);

        btnSearchAuditUser = navView.findViewById(R.id.btn_audit_user_search);
        LinearLayout llSp = navView.findViewById(R.id.ll_audit_user_spinner);
        tvDialogUsername = navView.findViewById(R.id.tv_audit_user_search_username);
        tvDialogUserid = navView.findViewById(R.id.tv_audit_user_search_userid);


        btnSearchAuditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

                String username = tvDialogUsername.getText().toString();
                String userid = tvDialogUserid.getText().toString();

                if(username.equalsIgnoreCase("Select Member"))
                {

                    Toast.makeText(AuditTrailUserActivity.this, "Please Select Username", Toast.LENGTH_SHORT).show();


                }

                else
                {
                    specificMemberAuditData(userid);

                }


                // specificMemberAuditData(spinnerMembersList.getSelectedItem().toString(), spinnerUserid);



            }
        });


        llSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectMemberPopup();
                // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

                // specificMemberAuditData(spinnerMembersList.getSelectedItem().toString(), spinnerUserid);



            }
        });


        // alertDialog = new AlertDialog.Builder(AuditTrailUserActivity.this).create();

       /* alertDialog.setView(R.layout.alertdialog_scrollupdown);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
*/
        LayoutInflater inflater = getLayoutInflater();
        toastlayout = inflater.inflate(R.layout.toast_custom_view,
                findViewById(R.id.cv_toast));
        tvToastmsg = toastlayout.findViewById(R.id.tv_custom_toast);


        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        rvAudit.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isScrolling = false;
                        btnScrollupdown.setVisibility(View.GONE);

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isScrolling = true;
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //isScrolling=false;
                        //btnScrollupdown.setVisibility(View.GONE);
                        break;

                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy > 0) {


                    //scroll upwards

                    btnScrollupdown.setVisibility(View.VISIBLE);
                    btnScrollupdown.setText("Move to last");
                    btnScrollupdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward_white_24dp, 0, 0, 0);


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
/*
                            Animation animation = AnimationUtils.loadAnimation(AuditTrailUserActivity.this, android.R.anim.fade_in);
                            animation.setDuration(200);
                            btnScrollupdown.startAnimation(animation);*/


                            btnScrollupdown.setVisibility(View.GONE);

                        }
                    }, 5000);


                    btnScrollupdown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           // vibrator.vibrate(100);
                            rvAudit.scrollToPosition(auditListAdapter.getItemCount() - 1);

                        }
                    });


                } else {

                    //scroll downwards

                    btnScrollupdown.setVisibility(View.VISIBLE);
                    btnScrollupdown.setText("Move to first");
                    btnScrollupdown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward_white_24dp, 0, 0, 0);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                       /*     Animation animation = AnimationUtils.loadAnimation(AuditTrailUserActivity.this, android.R.anim.fade_in);
                            animation.setDuration(200);
                            btnScrollupdown.startAnimation(animation);*/

                            //vibrator.vibrate(100);
                            btnScrollupdown.setVisibility(View.GONE);

                        }
                    }, 5000);


                    btnScrollupdown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            rvAudit.scrollToPosition(0);

                        }
                    });


                }


                // Log.e("lastpostion", String.valueOf(AuditListAdapter.lastPosition));
                //alertDialog.show();

              /*  if(isScrolling){

                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount= auditListAdapter.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    // Load more if we have reach the end to the recyclerView
                    if ( (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0){

                        //Log.e("totalogs", String.valueOf(totalLogs));
                        Log.e("itemcount", String.valueOf(auditListAdapter.getItemCount()));



                        if(String.valueOf(totalLogs).equalsIgnoreCase(String.valueOf(auditListAdapter.getItemCount()))){

                           // Toast.makeText(AuditTrailUserActivity.this, "No more logs", Toast.LENGTH_SHORT).show();

                            Log.e("no more logs","true");
                        }

                        else{

                          //  totalVisibleItems= totalVisibleItems +1;

                           // auditListAdapter.setDisplayCount(totalVisibleItems);

                           // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries ");
                            Log.e("totallogs scro", String.valueOf(totalLogs));

                            tvCountPanel.setText(totalLogs+" results found");




                        }
                    }
*/


                // toast.setText();
            /*        tvToastmsg.setText(AuditListAdapter.lastPosition+1 +" / "+totalLogs);
                    toast.setView(toastlayout);
                    toast.show();*/


                //}

               /* else{


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //alertDialog.hide();
                            toast.cancel();
                            handler.removeCallbacks(null);
                        }
                    },200);




                }
*/


            }
        });


    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.user_audit_left_drawer);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            //super.onBackPressed();
            Intent intent = new Intent(AuditTrailUserActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audit_trail_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_audit) {

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAllAuditData(final String userid) {


        totalLogs = 0;
        progressBar.setVisibility(View.VISIBLE);
        ll_user_audit_main.setVisibility(View.GONE);
        ll_user_nooflogs.setVisibility(View.GONE);


        auditTrailList.clear();
        //auditTrailListLoadMore.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUDIT_TRAIL_USERWISE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Log.e("audit userwise response", response);
                try {


                    JSONObject jsonObject1 = new JSONObject(response);
                    String error = jsonObject1.getString("error");
                    String totalLogs = jsonObject1.getString("totalLogsCount");

                    if(error.equalsIgnoreCase("false"))
                    {
                        JSONArray jsonArray = jsonObject1.getJSONArray("logs");

                        toolbar.getMenu().findItem(R.id.action_user_audit).setVisible(true);

                        Log.e("auditlist size", String.valueOf(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.getString("user_name");
                            String ip = jsonObject.getString("system_ip");
                            String actionperformed = jsonObject.getString("action_name");
                            String startDate = jsonObject.getString("start_date");
                            String endDate = jsonObject.getString("end_date");
                            String remarks = jsonObject.getString("remarks");

                            if (remarks.equals("null") || remarks.isEmpty()) {

                                remarks = "-";

                            }
                            if (endDate.equals("null") || endDate.isEmpty()) {


                                endDate = "-";
                            }
                            auditTrailList.add(new AuditTrail(username, actionperformed, startDate, endDate, ip, remarks, "user", "Sr.No. " + (i + 1)));


                        }

                        //auditTrailListLoadMore.addAll(auditTrailList.subList(startItemIndex,lastItemIndex));


                        auditListAdapter = new AuditListAdapter(AuditTrailUserActivity.this, auditTrailList, rvAudit);
                        // auditListAdapter.setDisplayCount(loadmorecount);
                        auditListAdapter.notifyDataSetChanged();
                        rvAudit.setAdapter(auditListAdapter);


                        // auditListAdapter.setDisplayCount(totalVisibleItems);
                        //tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries ");
                        tvCountPanel.setText(totalLogs + " results found");
                        progressBar.setVisibility(View.GONE);
                        ll_user_audit_main.setVisibility(View.VISIBLE);
                        llNoFileFound.setVisibility(View.GONE);
                        ll_user_nooflogs.setVisibility(View.VISIBLE);

                    }

                    else if(error.equalsIgnoreCase("true"))
                    {

                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                        toolbar.getMenu().findItem(R.id.action_user_audit).setVisible(false);

                    }

                    else{


                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                        toolbar.getMenu().findItem(R.id.action_user_audit).setVisible(false);

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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);
                parameters.put("action", "getAllLogs");
                return parameters;
            }
        };


        VolleySingelton.getInstance(AuditTrailUserActivity.this).addToRequestQueue(stringRequest);


    }

    public void groupMemberList(final String userid) {

        userlist.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUDIT_TRAIL_USERWISE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response in getmembrs", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");
                    String totalMembers = jsonObject.getString("totalMemberCount");

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AuditTrailUserActivity.this);
                    LayoutInflater inflater = (LayoutInflater) AuditTrailUserActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.searchable_spinner_layout, null);

                    RecyclerView rvMember = dialogView.findViewById(R.id.rv_searchable_spinner_alert_dialog);
                    rvMember.setLayoutManager(new LinearLayoutManager(AuditTrailUserActivity.this));

                    ProgressBar pb = dialogView.findViewById(R.id.pb_searchable_spinner);
                    LinearLayout llRv = dialogView.findViewById(R.id.ll_searchable_alert_dialog_rv);
                    LinearLayout llNoMemFound = dialogView.findViewById(R.id.ll_searchable_spinner_no_member_found);

                    EditText etFilter = dialogView.findViewById(R.id.et_searchable_alert_dialog_search_member);

                    pb.setVisibility(View.VISIBLE);
                    llRv.setVisibility(View.GONE);
                    llNoMemFound.setVisibility(View.GONE);

                    if (error.equalsIgnoreCase("false")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String username = jsonArray.getJSONObject(i).getString("user_name");
                            String userid = jsonArray.getJSONObject(i).getString("user_id");
                            userlist.add(new User(username, userid));

                        }

                        searchableSpinnerAdapter = new SearchableSpinnerAdapter(AuditTrailUserActivity.this, userlist);
                        rvMember.setAdapter(searchableSpinnerAdapter);
                        searchableSpinnerAdapter.setOnItemClickListener(new SearchableSpinnerAdapter.OnItemClickListener() {
                            @Override
                            public void onMemberClickListener(String username, String userid) {

                               // Toast.makeText(AuditTrailUserActivity.this, "working", Toast.LENGTH_SHORT).show();

                                tvDialogUsername.setText(username);
                                tvDialogUserid.setText(userid);

                                alertDialog.dismiss();


                            }
                        });

                        etFilter.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                String newText = charSequence.toString();
                                List<User> filteredList = new ArrayList<>();

                                for (int j = 0; j < userlist.size(); j++) {

                                    final String username = userlist.get(j).getUsername().toLowerCase();

                                    if (username.contains(newText)) {

                                        filteredList.add(userlist.get(j));
                                    }

                                }

                                rvMember.setLayoutManager(new LinearLayoutManager(AuditTrailUserActivity.this));
                                searchableSpinnerAdapter = new SearchableSpinnerAdapter(AuditTrailUserActivity.this, filteredList);
                                searchableSpinnerAdapter.setOnItemClickListener(new SearchableSpinnerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onMemberClickListener(String username, String userid) {

                                      //  Toast.makeText(AuditTrailUserActivity.this, "working", Toast.LENGTH_SHORT).show();

                                tvDialogUsername.setText(username);
                                tvDialogUserid.setText(userid);

                                        alertDialog.dismiss();


                                    }
                                });


                                if (filteredList.size() == 0) {

                                    llNoMemFound.setVisibility(View.VISIBLE);
                                    rvMember.setVisibility(View.GONE);
                                    rvMember.setAdapter(searchableSpinnerAdapter);
                                    searchableSpinnerAdapter.notifyDataSetChanged();


                                } else {


                                    rvMember.setVisibility(View.VISIBLE);
                                    llNoMemFound.setVisibility(View.GONE);
                                    rvMember.setAdapter(searchableSpinnerAdapter);
                                    searchableSpinnerAdapter.notifyDataSetChanged();

                                }


                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        pb.setVisibility(View.GONE);
                        llRv.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {

                        pb.setVisibility(View.GONE);
                        llRv.setVisibility(View.GONE);
                        llNoMemFound.setVisibility(View.VISIBLE);



                    } else {

                        pb.setVisibility(View.GONE);
                        llRv.setVisibility(View.GONE);
                        llNoMemFound.setVisibility(View.VISIBLE);
                        Toast.makeText(AuditTrailUserActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                    }


                    Button btnCancel = dialogView.findViewById(R.id.btn_searchable_alert_dialog_cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();

                        }
                    });


                    dialogBuilder.setView(dialogView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    alertDialog.show();


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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);
                parameters.put("action", "getAllGroupMembers");


                return parameters;
            }


        };


        VolleySingelton.getInstance(AuditTrailUserActivity.this).addToRequestQueue(stringRequest);


    }

    public void specificMemberAuditData(final String userid) {


        llNoFileFound.setVisibility(View.GONE);
        ll_user_audit_main.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ll_user_nooflogs.setVisibility(View.GONE);

        auditTrailList.clear();


        //auditTrailListLoadMore1.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUDIT_TRAIL_USERWISE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                Log.e("Res specifgetmembrs", response);


                   /* toastResult.setDuration(Toast.LENGTH_SHORT);
                    toastResult.setText(totalLogs+" result found ");
                    toastResult.show();*/


                try {



                    JSONObject jsonObject1 = new JSONObject(response);
                    String msg = jsonObject1.getString("msg");
                    String error = jsonObject1.getString("error");

                    if(error.equalsIgnoreCase("false"))
                    {


                        String totalLogsCount = jsonObject1.getString("totalLogsCount");

                        llNoFileFound.setVisibility(View.GONE);
                        ll_user_audit_main.setVisibility(View.VISIBLE);


                        JSONArray jsonArray = jsonObject1.getJSONArray("logs");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.getString("user_name");
                            String ip = jsonObject.getString("system_ip");
                            String actionperformed = jsonObject.getString("action_name");
                            String startDate = jsonObject.getString("start_date");
                            String endDate = jsonObject.getString("end_date");
                            String remarks = jsonObject.getString("remarks");

                            if (remarks.equals("null") || remarks.isEmpty()) {

                                remarks = "-";

                            }
                            if (endDate.equals("null") || endDate.isEmpty()) {


                                endDate = "-";
                            }

                            auditTrailList.add(new AuditTrail(username, actionperformed, startDate, endDate, ip, remarks, "user", "Sr.No. " + (i + 1)));

                        }


                        auditListAdapter = new AuditListAdapter(AuditTrailUserActivity.this, auditTrailList, rvAudit);
                        auditListAdapter.notifyDataSetChanged();

                        rvAudit.setAdapter(auditListAdapter);

                        tvCountPanel.setText(totalLogsCount + " results found");

                        progressBar.setVisibility(View.GONE);
                        ll_user_audit_main.setVisibility(View.VISIBLE);
                        ll_user_nooflogs.setVisibility(View.VISIBLE);

                        drawer.closeDrawer(Gravity.RIGHT);

                    }

                    else if(error.equalsIgnoreCase("true"))
                    {
                        ll_user_audit_main.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);

                        drawer.closeDrawer(Gravity.RIGHT);

                    }

                    else
                        {

                            Toast.makeText(AuditTrailUserActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            drawer.closeDrawer(Gravity.RIGHT);

                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(AuditTrailUserActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(Gravity.RIGHT);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);
                parameters.put("action", "getSpecificUserLogs");
                return parameters;
            }


        };


        VolleySingelton.getInstance(AuditTrailUserActivity.this).addToRequestQueue(stringRequest);


    }


    void selectMemberPopup() {


        groupMemberList(session_userid);


    }


}
