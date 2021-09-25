package in.cbslgroup.ezeeoffice.Activity.AuditTrail;

import android.content.Context;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Adapters.AuditListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.SearchableSpinnerAdapter;
import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.Model.AuditTrail;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class AuditTrailStorageActivity extends AppCompatActivity {

    DrawerLayout drawer;
    View navView;
    SessionManager session;
    String session_userid;
    List<AuditTrail> auditTrailList = new ArrayList<>();

    RecyclerView rvAudit;
    AuditListAdapter auditListAdapter;


    Button btnSearchAuditUser;

    String spinnerUserid;

    ProgressBar progressBar;

    SearchView searchView;

    LinearLayout llNoFileFound, llauditStoragemain, ll_user_nooflogs, ll_storage_audit_main;


    TextView tvCountPanel;
    TextView tvNoResultFound;

    Button btnScrollupdown;
    Boolean isScrolling = false;

    LinearLayoutManager linearLayoutManager;
    int totalLogs;
    Vibrator vibrator;
    Toolbar toolbar;

    AlertDialog alertDialog = null;

    List<User> userlist = new ArrayList<>();
    SearchableSpinnerAdapter searchableSpinnerAdapter;
    TextView tvDialogUsername, tvDialogUserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_trail_storage);


        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // userid
        session_userid = user.get(SessionManager.KEY_USERID);

        toolbar = findViewById(R.id.toolbar_audit_storage);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        drawer = findViewById(R.id.storage_audit_left_drawer);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        NavigationView navigationView = findViewById(R.id.storage_audit_nav_view);
        navView = navigationView.getHeaderView(0);

        llNoFileFound = findViewById(R.id.ll_audit_storage_no_data_found);
        llauditStoragemain = findViewById(R.id.ll_audit_trail_storage_main);

        progressBar = findViewById(R.id.progressBarStorageAudit);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        searchView = findViewById(R.id.searchview_audit_storage);
        Util.setSearchviewTextSize(searchView, 12, "Search by username , action performed etc");

        tvCountPanel = findViewById(R.id.tv_storageaudit_resultcount);
        ll_user_nooflogs = findViewById(R.id.ll_audit_stoarge_user_nooflogs);
        btnScrollupdown = findViewById(R.id.btn_audit_storage_hover_scrollupdown);
        btnScrollupdown.setVisibility(View.GONE);
        tvNoResultFound = findViewById(R.id.rv_tv_audit_storage_no_result_found);
        tvNoResultFound.setVisibility(View.GONE);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //searchView.setQuery("Search text",false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                final List<AuditTrail> filteredList = new ArrayList<>();

                for (int i = 0; i < auditTrailList.size(); i++) {

                    final String username = auditTrailList.get(i).getUsername().toLowerCase();
                    final String ip = auditTrailList.get(i).getIp().toLowerCase();
                    final String startdate = auditTrailList.get(i).getActionStartDate().toLowerCase();
                    final String enddate = auditTrailList.get(i).getActionEndDate().toLowerCase();
                    final String sno = auditTrailList.get(i).getSno().toLowerCase();
                    final String serialNum = sno.substring(sno.lastIndexOf(" ") + 1);

                    Log.e("newText", newText);
                    Log.e("serialNum", serialNum);

                    if (serialNum.contains(newText) || enddate.contains(newText) || username.contains(newText) || username.contains(newText) || ip.contains(newText) || startdate.contains(newText)) {

                        filteredList.add(auditTrailList.get(i));
                    }


                }

                rvAudit.setLayoutManager(new LinearLayoutManager(AuditTrailStorageActivity.this));
                auditListAdapter = new AuditListAdapter(AuditTrailStorageActivity.this, filteredList, rvAudit);

                if (filteredList.size() == 0) {

                    //Log.e("audittrailistsize", String.valueOf(auditTrailList.size()));
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
                    tvCountPanel.setText("0 results found");

                } else {


                    // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries");
                    tvCountPanel.setText(auditListAdapter.getItemCount() + " results found ");
                }


                return false;
            }


        });


        rvAudit = findViewById(R.id.rv_storage_audit);
        linearLayoutManager = new LinearLayoutManager(this);


        rvAudit.setLayoutManager(linearLayoutManager);
        rvAudit.setHasFixedSize(true);

        getAllAuditData(session_userid);


        btnSearchAuditUser = navView.findViewById(R.id.btn_audit_storage_search);
        LinearLayout llSp = navView.findViewById(R.id.ll_audit_storage_spinner);
        tvDialogUsername = navView.findViewById(R.id.tv_audit_storage_search_username);
        tvDialogUserid = navView.findViewById(R.id.tv_audit_storage_search_userid);


        btnSearchAuditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

                String username = tvDialogUsername.getText().toString();
                String userid = tvDialogUserid.getText().toString();

                if (username.equalsIgnoreCase("Select Member")) {

                    Toast.makeText(AuditTrailStorageActivity.this, "Please Select Username", Toast.LENGTH_SHORT).show();


                } else {


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
                            // Toast.makeText(AuditTrailStorageActivity.this, "working last", Toast.LENGTH_SHORT).show();

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

                            btnScrollupdown.setVisibility(View.GONE);

                        }
                    }, 5000);


                    btnScrollupdown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //  vibrator.vibrate(100);
                            rvAudit.scrollToPosition(0);
                            //Toast.makeText(AuditTrailStorageActivity.this, "working 0", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


                // Log.e("lastpostion", String.valueOf(AuditListAdapter.lastPosition));
                //alertDialog.show();

             /*   if (isScrolling) {


                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = auditListAdapter.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    // Load more if we have reach the end to the recyclerView
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {

                        //Log.e("totalogs", String.valueOf(totalLogs));
                        Log.e("itemcount", String.valueOf(auditListAdapter.getItemCount()));


                        if (String.valueOf(totalLogs).equalsIgnoreCase(String.valueOf(auditListAdapter.getItemCount()))) {

                            // Toast.makeText(AuditTrailUserActivity.this, "No more logs", Toast.LENGTH_SHORT).show();

                            Log.e("no more logs", "true");
                        } else {

                            //  totalVisibleItems= totalVisibleItems +1;

                            // auditListAdapter.setDisplayCount(totalVisibleItems);

                            // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries ");


                            // Toast.makeText(AuditTrailStorageActivity.this, totalLogs, Toast.LENGTH_SHORT).show();

                            Log.e("totallogsScrolling", String.valueOf(totalLogs));

                            //tvCountPanel.setText( totalLogs + " result found scrolling");


                        }
                    }
*/

                // toast.setText();
            /*        tvToastmsg.setText(AuditListAdapter.lastPosition+1 +" / "+totalLogs);
                    toast.setView(toastlayout);
                    toast.show();*/


            }

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


        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.storage_audit_left_drawer);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audit_trail_storage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_storage_audit) {


            //selectMemberPopup();


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


        progressBar.setVisibility(View.VISIBLE);
        llauditStoragemain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ll_user_nooflogs.setVisibility(View.GONE);

        auditTrailList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUDIT_TRAIL_STORAGEWISE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("audit userwise response", response);
                try {


                    JSONObject jsonObject1 = new JSONObject(response);
                    //String msg = jsonObject1.getString("msg");
                    String error = jsonObject1.getString("error");
                    String totalLogs = jsonObject1.getString("totalLogsCount");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject1.getJSONArray("logs");

                        toolbar.getMenu().findItem(R.id.action_storage_audit).setVisible(true);

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


                            auditTrailList.add(new AuditTrail(username, actionperformed, startDate, endDate, ip, remarks, "storage", "Sr.No. " + (i + 1)));


                        }

                        auditListAdapter = new AuditListAdapter(AuditTrailStorageActivity.this, auditTrailList, rvAudit);
                        auditListAdapter.notifyDataSetChanged();

                        rvAudit.setAdapter(auditListAdapter);

                        progressBar.setVisibility(View.GONE);
                        llauditStoragemain.setVisibility(View.VISIBLE);


                        tvCountPanel.setText(totalLogs + " results found ");
                        progressBar.setVisibility(View.GONE);

                        llNoFileFound.setVisibility(View.GONE);
                        ll_user_nooflogs.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {

                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                        toolbar.getMenu().findItem(R.id.action_storage_audit).setVisible(false);

                    } else {

                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                        toolbar.getMenu().findItem(R.id.action_storage_audit).setVisible(false);

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


        VolleySingelton.getInstance(AuditTrailStorageActivity.this).addToRequestQueue(stringRequest);


    }

    public void groupMemberList(final String userid) {

        userlist.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUDIT_TRAIL_STORAGEWISE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response in getmembrs", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");
                    String totalMembers = jsonObject.getString("totalMemberCount");

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AuditTrailStorageActivity.this);
                    LayoutInflater inflater = (LayoutInflater) AuditTrailStorageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.searchable_spinner_layout, null);

                    RecyclerView rvMember = dialogView.findViewById(R.id.rv_searchable_spinner_alert_dialog);
                    rvMember.setLayoutManager(new LinearLayoutManager(AuditTrailStorageActivity.this));
                    rvMember.setNestedScrollingEnabled(true);

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

                        searchableSpinnerAdapter = new SearchableSpinnerAdapter(AuditTrailStorageActivity.this, userlist);
                        rvMember.setAdapter(searchableSpinnerAdapter);
                        searchableSpinnerAdapter.setOnItemClickListener(new SearchableSpinnerAdapter.OnItemClickListener() {
                            @Override
                            public void onMemberClickListener(String username, String userid) {

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

                                //rvMember.setLayoutManager(new LinearLayoutManager(AuditTrailStorageActivity.this));
                                searchableSpinnerAdapter = new SearchableSpinnerAdapter(AuditTrailStorageActivity.this, filteredList);
                                searchableSpinnerAdapter.setOnItemClickListener(new SearchableSpinnerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onMemberClickListener(String username, String userid) {

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
                        Toast.makeText(AuditTrailStorageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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

                Toast.makeText(AuditTrailStorageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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

        VolleySingelton.getInstance(AuditTrailStorageActivity.this).addToRequestQueue(stringRequest);


    }

    public void specificMemberAuditData(final String userid) {

        llauditStoragemain.setVisibility(View.GONE);
        llNoFileFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ll_user_nooflogs.setVisibility(View.GONE);

        auditTrailList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.AUDIT_TRAIL_STORAGEWISE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Res specifgetmembrs", response);


                try {

                    JSONObject jsonObject1 = new JSONObject(response);
                    String msg = jsonObject1.getString("msg");
                    String error = jsonObject1.getString("error");


                    if (error.equalsIgnoreCase("false")) {

                        llNoFileFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                        String totalLogsCount = jsonObject1.getString("totalLogsCount");

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

                            auditTrailList.add(new AuditTrail(username, actionperformed, startDate, endDate, ip, remarks, "storage", "Sr.No " + (i + 1)));

                        }


                        auditListAdapter = new AuditListAdapter(AuditTrailStorageActivity.this, auditTrailList, rvAudit);
                        auditListAdapter.notifyDataSetChanged();

                        rvAudit.setAdapter(auditListAdapter);

                        tvCountPanel.setText(totalLogsCount + " results found ");

                        progressBar.setVisibility(View.GONE);
                        llauditStoragemain.setVisibility(View.VISIBLE);
                        ll_user_nooflogs.setVisibility(View.VISIBLE);

                        drawer.closeDrawer(Gravity.RIGHT);

                    } else if (error.equalsIgnoreCase("true")) {

                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);

                        drawer.closeDrawer(Gravity.RIGHT);

                    } else {

                        Toast.makeText(AuditTrailStorageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                        drawer.closeDrawer(Gravity.RIGHT);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AuditTrailStorageActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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


        VolleySingelton.getInstance(AuditTrailStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void selectMemberPopup() {


        groupMemberList(session_userid);


    }


}
