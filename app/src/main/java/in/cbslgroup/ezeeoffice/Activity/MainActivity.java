package in.cbslgroup.ezeeoffice.Activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLServerSocketFactory;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cbslgroup.ezeeoffice.Activity.Appointment.AddAppointmentActivity;
import in.cbslgroup.ezeeoffice.Activity.Appointment.ViewAppointmentActivity;
import in.cbslgroup.ezeeoffice.Activity.AuditTrail.AuditTrailStorageActivity;
import in.cbslgroup.ezeeoffice.Activity.AuditTrail.AuditTrailUserActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.Search.FrequentlyQueriesActivity;
import in.cbslgroup.ezeeoffice.Activity.Search.MetaDataSearchActivity;
import in.cbslgroup.ezeeoffice.Activity.Search.QuickSearchActivity;
import in.cbslgroup.ezeeoffice.Activity.SharedFiles.SharedFilesActivity;
import in.cbslgroup.ezeeoffice.Activity.SharedFiles.SharedWithMeActivity;
import in.cbslgroup.ezeeoffice.Activity.Todo.AddToDoActivity;
import in.cbslgroup.ezeeoffice.Activity.Todo.TodoActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.AuditTrailWorkFlowActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.InTrayActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.IntiateFileActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.TaskTrackStatusActivity;
import in.cbslgroup.ezeeoffice.Adapters.AppointmentlistAdapter;
import in.cbslgroup.ezeeoffice.Adapters.DashboardAdapter;
import in.cbslgroup.ezeeoffice.Adapters.StorageAllotedAdapter;
import in.cbslgroup.ezeeoffice.Adapters.TodolistAdapter;
import in.cbslgroup.ezeeoffice.Adapters.WorkFlowListAdapter;
import in.cbslgroup.ezeeoffice.Model.AppointmentList;
import in.cbslgroup.ezeeoffice.Model.DashBoard;
import in.cbslgroup.ezeeoffice.Model.StorageAlloted;
import in.cbslgroup.ezeeoffice.Model.TodoList;
import in.cbslgroup.ezeeoffice.Model.WorkFlowList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.ConnectivityReceiver;
import in.cbslgroup.ezeeoffice.Utils.FCMHandler;
import in.cbslgroup.ezeeoffice.Utils.Initializer;
import in.cbslgroup.ezeeoffice.Utils.PrefManager;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.TabbedDialog;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {


    static public String roleId, username, userid, ip, slid_session, designation, tasktrackstatus, intray, multiStorage;
    RecyclerView rvDashboard, rvTodo, rvAppointment;
    DashboardAdapter dashboardAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    List<DashBoard> dashBoardList = new ArrayList<>();
    SessionManager session;
    String session_userId = null, session_userName = null, session_userEmail = null, session_designation = null, session_contact = null, session_api_key;

    TextView emailInTray, fullnameInTray;
    View navView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AlertDialog alertDialog;
    CircleImageView circleImageView;
    NavigationView navigationView;


    //no internetfound layout
    LinearLayout llnointernetfound, llDashBoard, llnoAptFound, llnoTodoFound;
    NestedScrollView scrollViewdashboard;

    BottomSheetDialog bottomSheetDialog;
    Button btnEditProfile;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout llAudtrailMain, llSearchMain, llStorageManegmentMain, llStorageMangerMain, llWorkflowManagementMain, llIntiateWorkMain, lltodoMain, llAppointMain;
    //linear layouts of navbar
    LinearLayout llAppointmentLevel0, llTodoLevel0, llWorkFlowManagementLevel0, llWorkFlowManagementLevel1, llStorageManagementLevel0, llStorageManagementLevel1, llSearchLevel0, llAuditTrailLevel0, llAuthlevel0, llUserMangerlevel0, llUserManagementlevel0;

    //recycler view workflow list
    RecyclerView rvWorkFLowlist;
    List<WorkFlowList> workFlowList = new ArrayList<>();
    WorkFlowListAdapter workFlowListAdapter;


    //recycler view storage alloted list
    RecyclerView rvStorageAlloted;
    List<StorageAlloted> storageAllotedList = new ArrayList<>();
    StorageAllotedAdapter storageAllotedAdapter;

    ProgressBar progressBar, pbTodo, pbAptment;
    ProgressDialog pDLogout;

    ImageView ivAppointmentArrow, ivTodoArrow, ivIntiateWorkFlowArrow, ivWorkFlowManagementArrow, ivStorageManagementArrow, ivSearcArrow, ivAuditTrailArrow, ivStorageManagerArrow, ivAuthArrow, ivUserManagerArrow, ivUserManagementArrow;

    TextView tvNoWfFoundNoWfFound, tvFreqQueries, tvAbout,
            tvIntiateFile, tvWorkflowAudit, tvTaskTrackStatus, tvUserProfile, tvSupport, tvGroupManager, tvShareFilesWithMe,
            tvShareFiles, tvUserAudit, tvStorageAudit, tvQuickSearch, tvMetaSearch, tvRecycleBin, tvLogout, tvDashBoard,
            tvStorageAlloted, tvAddTodo, tvTodayTodo, tvTommorowTodo, tvThisWeekTodo, tvAllTodo, tvAddAppointment, tvViewAppointment;


    PrefManager prefManager;
    ActionBarDrawerToggle toggle;


    //permissions

    MaterialShowcaseSequence sequence;

    Toolbar toolbar;


    //Notification badge
    TextView tvNotiBadgeCount;
    int mNotiCount = 10;
    //EditText etQuickSearch;
    SearchView searchViewQuickSearch;
    ArrayList<TodoList> todoLists = new ArrayList<>();
    ArrayList<AppointmentList> appointmentLists = new ArrayList<>();
    ArrayList<String> todoAppointDatesList = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    ProgressBar pbDmsReport, pbWorkflowStatus, pbWorkflowPriority, pbMemoryUsage, pbUserReport;


    //cardviews of graphs
    CardView cvDmsReport, cvWorkflowStatusGraph, cvWfPriorGraph, cvMemoryUsageGraph, cvTodoDash, cvAptDash;
    ConstraintLayout clCalenderDash;
    private PieChart dmsReportChart, workflowStatusChart, workflowPriorityChart;
    private LineChart memoryStatusChart;
    private BarChart userReportGraph;
    private CalendarView calendarView;

    EditText etFilterWfList;
    HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // inside your activity (if you did not enable transitions in your theme)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Session Manager
        session = new SessionManager(getApplicationContext());

        //Check wheather the person is logged in or not
        session.checkLogin();

        //Getting userdata from session
        HashMap<String, String> user = session.getUserDetails();
        session_userId = user.get(SessionManager.KEY_USERID);
        session_userName = user.get(SessionManager.KEY_NAME);
        session_userEmail = user.get(SessionManager.KEY_EMAIL);
        session_designation = user.get(SessionManager.KEY_DESIGNATION);
        session_contact = user.get(SessionManager.KEY_CONTACT);

        //making these variable global for easy usage
        userid = session_userId;
        designation = session_designation;
        username = session_userName;
        ip = getDeviceIpAddress();


        Log.e("userid sent on mail", userid);


        //intializing the views
        initViews();


        //loading json requests
        loadJson();


    }


    void initViews() {

        //Graphs cardviews
        cvDmsReport = findViewById(R.id.cv_dms_report_graph);
        cvWorkflowStatusGraph = findViewById(R.id.cv_workflow_status_graph);
        cvWfPriorGraph = findViewById(R.id.cv_workflow_priority_graph);
        cvMemoryUsageGraph = findViewById(R.id.cv_memory_usage_chart);
        cvTodoDash = findViewById(R.id.cv_todo_dash);
        cvAptDash = findViewById(R.id.cv_apt_dash);
        clCalenderDash = findViewById(R.id.cl_calender_dash);

        horizontalScrollView = findViewById(R.id.hori_scroll_view_dashboard);


        rvDashboard = findViewById(R.id.rv_dashboard);
        rvTodo = findViewById(R.id.rv_todo);
        rvAppointment = findViewById(R.id.rv_appointment);


        calendarView = findViewById(R.id.calender_view);
        calendarView.setHeaderColor(R.color.colorPrimary);
        calendarView.setHeaderLabelColor(R.color.white);


        progressBar = findViewById(R.id.progressBar_mainactivity);
        pbAptment = findViewById(R.id.progressbar_apt_dashboard);
        pbTodo = findViewById(R.id.progressbar_todo_dashboard);

        searchViewQuickSearch = findViewById(R.id.sv_quick_search_in_list);
        scrollViewdashboard = findViewById(R.id.scrollview_main_dashboard);

        llnointernetfound = findViewById(R.id.ll_mainactivity_nointernetfound);
        llnoTodoFound = findViewById(R.id.ll_no_todo_found_dashboard);
        llnoAptFound = findViewById(R.id.ll_no_apt_found_dashboard);


        //llDashBoard = findViewById(R.id.ll_mainactivity_dashboard);
        swipeRefreshLayout = findViewById(R.id.swipelayout_main_acitivity);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }


        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);

        navView = navigationView.getHeaderView(0);

        circleImageView = navView.findViewById(R.id.iv_navheader_profile);
        fullnameInTray = navView.findViewById(R.id.tv_navheader_username);
        emailInTray = navView.findViewById(R.id.tv_navheader_email);
        btnEditProfile = navView.findViewById(R.id.btn_navheader_dashboard_edit_profile);

        //nav drawer views
        //imageviews
        ivStorageManagementArrow = navView.findViewById(R.id.iv_nav_drawer_storage_management_arrow);
        ivSearcArrow = navView.findViewById(R.id.iv_nav_drawer_search_arrow);
        ivAuditTrailArrow = navView.findViewById(R.id.iv_nav_drawer_audit_trail_arrow);
        ivStorageManagerArrow = navView.findViewById(R.id.iv_nav_drawer_storage_manager_arrow);
        ivAuthArrow = navView.findViewById(R.id.iv_nav_drawer_authorization_arrow);
        ivUserManagerArrow = navView.findViewById(R.id.iv_nav_drawer_user_manager_arrow);
        ivUserManagementArrow = navView.findViewById(R.id.iv_nav_drawer_user_management_arrow);
        ivWorkFlowManagementArrow = navView.findViewById(R.id.iv_nav_drawer_workflow_management_arrow);
        ivIntiateWorkFlowArrow = navView.findViewById(R.id.iv_nav_drawer_intiate_workflow_arrow);
        ivTodoArrow = navView.findViewById(R.id.iv_nav_drawer_todo_arrow);
        ivAppointmentArrow = navView.findViewById(R.id.iv_nav_drawer_apt_arrow);
        //linearlayouts

        llAudtrailMain = navView.findViewById(R.id.ll_Audit_trail_panel);
        llSearchMain = navView.findViewById(R.id.ll_Searchmodule_main);
        llStorageManegmentMain = navView.findViewById(R.id.ll_nav_bar_storage_management_main);
        llStorageMangerMain = navView.findViewById(R.id.ll_nav_bar_storage_manager_main);
        llWorkflowManagementMain = navView.findViewById(R.id.ll_nav_bar_workflow_management_main);
        llIntiateWorkMain = navView.findViewById(R.id.ll_nav_bar_intiate_workflow_main);
        lltodoMain = navView.findViewById(R.id.ll_todo_panel);
        llAppointMain = navView.findViewById(R.id.ll_apt_panel);

        llTodoLevel0 = navView.findViewById(R.id.ll_nav_bar_todo_level_0);
        llAppointmentLevel0 = navView.findViewById(R.id.ll_nav_bar_apt_level_0);
        llStorageManagementLevel0 = navView.findViewById(R.id.ll_nav_bar_storage_management_level_0);
        llSearchLevel0 = navView.findViewById(R.id.ll_nav_bar_search_level_0);
        llAuditTrailLevel0 = navView.findViewById(R.id.ll_nav_bar_Audit_trail_level_0);
        llAuthlevel0 = navView.findViewById(R.id.ll_nav_bar_Authorization_level_0);
        llStorageManagementLevel1 = navView.findViewById(R.id.ll_nav_bar_storage_management_level_1);
        llUserMangerlevel0 = navView.findViewById(R.id.ll_nav_bar_user_manager_level_0);
        llUserManagementlevel0 = navView.findViewById(R.id.ll_nav_bar_user_management_level_0);
        llWorkFlowManagementLevel0 = navView.findViewById(R.id.ll_nav_bar_workflow_management_level_0);
        llWorkFlowManagementLevel1 = navView.findViewById(R.id.ll_nav_bar_workflow_management_level_1);

        //recyclerview workflow list
        rvWorkFLowlist = navView.findViewById(R.id.rv_workflow_list_nav_bar);
        rvWorkFLowlist.setHasFixedSize(true);
        rvWorkFLowlist.setItemViewCacheSize(workFlowList.size());
        rvWorkFLowlist.setLayoutManager(new LinearLayoutManager(this));
        rvWorkFLowlist.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        rvStorageAlloted = navView.findViewById(R.id.rv_nav_bar_storage_manager_storage_assigned);
        rvStorageAlloted.setLayoutManager(new LinearLayoutManager(this));
        rvStorageAlloted.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        //textview in nav drawer
        tvStorageAlloted = navView.findViewById(R.id.tv_nav_bar_storage_manager_storage_assigned);


        //Todo
        tvAddTodo = navView.findViewById(R.id.tv_nav_bar_add_to_todo);
        tvTodayTodo = navView.findViewById(R.id.tv_nav_bar_todo_today);
        tvTommorowTodo = navView.findViewById(R.id.tv_nav_bar_todo_tommorow);
        tvThisWeekTodo = navView.findViewById(R.id.tv_nav_bar_todo_this_week);
        tvAllTodo = navView.findViewById(R.id.tv_nav_bar_todo_all);

        //Appointment
        tvAddAppointment = navView.findViewById(R.id.tv_nav_bar_add_to_apt);
        tvViewAppointment = navView.findViewById(R.id.tv_nav_bar_appointment_view_all);

        tvWorkflowAudit = navView.findViewById(R.id.tv_nav_bar_workflow_audit);
        tvAbout = navView.findViewById(R.id.tv_nav_bar_about);
        tvIntiateFile = navView.findViewById(R.id.tv_nav_bar_intiate_file);
        tvTaskTrackStatus = navView.findViewById(R.id.tv_nav_bar_task_track_status);
        tvUserProfile = navView.findViewById(R.id.tv_nav_bar__Authorization_user_profile);
        tvGroupManager = navView.findViewById(R.id.tv_nav_bar_group_manager);
        tvSupport = navView.findViewById(R.id.tv_nav_bar_support);

        tvUserAudit = navView.findViewById(R.id.tv_nav_bar_user_audit);
        tvStorageAudit = navView.findViewById(R.id.tv_nav_bar_storage_audit);
        tvQuickSearch = navView.findViewById(R.id.tv_nav_bar_quick_search);
        tvShareFiles = navView.findViewById(R.id.tv_nav_bar_shared_files);
        tvShareFilesWithMe = navView.findViewById(R.id.tv_nav_bar_shared_files_withme);
        tvMetaSearch = navView.findViewById(R.id.tv_nav_bar_meta_search);
        tvRecycleBin = navView.findViewById(R.id.tv_nav_bar_recyclebin);
        tvDashBoard = navView.findViewById(R.id.tv_nav_bar_dashboard);
        tvLogout = navView.findViewById(R.id.tv_nav_bar_logoout);
        tvFreqQueries = navView.findViewById(R.id.tv_nav_bar_freq_queries);
        tvNoWfFoundNoWfFound = navView.findViewById(R.id.tv_wf_list_no_workflow_found);
        etFilterWfList = navView.findViewById(R.id.et_nav_bar_workflow_list_filter);
        rvWorkFLowlist.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        etFilterWfList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String newText = s.toString();
                newText = newText.toLowerCase();
                final List<WorkFlowList> filteredList = new ArrayList<>();

                for (int i = 0; i < workFlowList.size(); i++) {

                    final String username = workFlowList.get(i).getWorkFlowName().toLowerCase();
                    Log.e("newText", newText);


                    if (username.contains(newText)) {

                        filteredList.add(workFlowList.get(i));
                    }


                }


                workFlowListAdapter = new WorkFlowListAdapter(filteredList, MainActivity.this);

                if (filteredList.size() == 0) {

                    //Log.e("audittrailistsize", String.valueOf(auditTrailList.size()));
                    tvNoWfFoundNoWfFound.setVisibility(View.VISIBLE);


                    rvWorkFLowlist.setAdapter(workFlowListAdapter);
                    workFlowListAdapter.notifyDataSetChanged();


                } else {


                    tvNoWfFoundNoWfFound.setVisibility(View.GONE);
                    rvWorkFLowlist.setAdapter(workFlowListAdapter);
                    workFlowListAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //piechartview

        dmsReportChart = findViewById(R.id.dms_report_chart);
        workflowStatusChart = findViewById(R.id.workflow_status_chart);
        workflowPriorityChart = findViewById(R.id.workflow_priority_chart);
        memoryStatusChart = findViewById(R.id.memory_status_chart);
        userReportGraph = findViewById(R.id.user_report_chart);


        //progrss bars for charts
        pbDmsReport = findViewById(R.id.pb_dms_report_chart);
        pbWorkflowStatus = findViewById(R.id.pb_workflow_status_chart);
        pbWorkflowPriority = findViewById(R.id.pb_workflow_priority_chart);
        pbMemoryUsage = findViewById(R.id.pb_memory_status_chart);
        pbUserReport = findViewById(R.id.pb_user_report_chart);

        // test();

        //for dashborad rv
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvDashboard.setLayoutManager(staggeredGridLayoutManager);
        rvDashboard.setItemViewCacheSize(todoLists.size());
        //rvDashboard.setLayoutFrozen(true);
        // rvDashboard.setNestedScrollingEnabled(false);
        rvDashboard.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // Stop only scrolling.
                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });




        // for todolist rv
        rvTodo.setLayoutManager(new LinearLayoutManager(this));
        rvTodo.hasFixedSize();
        rvTodo.setItemViewCacheSize(todoLists.size());


        //for appointment list rv

        rvAppointment.setLayoutManager(new LinearLayoutManager(this));
        rvAppointment.hasFixedSize();
        rvAppointment.setItemViewCacheSize(appointmentLists.size());


        fullnameInTray.setText(session_userName);
        emailInTray.setText(session_userEmail);


        Util.setSearchviewTextSize(searchViewQuickSearch, 12, "Quick Search : files , documents ...");

        searchViewQuickSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainActivity.this, QuickSearchActivity.class);
                intent.putExtra("text", query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red), getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

             /*   //Getting dashboard data
                if (session_userId == null || session_userId.isEmpty()) {

                    Log.e("session userid ", "null");

                } else

                {
                    getSlidId(session_userId);
                }*/

                loadJson();


            }
        });


        scrollViewdashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollViewdashboard.getScrollY(); //for verticalScrollView
                if (scrollY == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });


        //onclicks
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });


        tvAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddToDoActivity.class);
                intent.putExtra("todoid", "null");
                startActivity(intent);


            }
        });

        tvAllTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("action", "All");
                startActivity(intent);

            }
        });

        tvTodayTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("action", "Today");
                startActivity(intent);

            }
        });

        tvTommorowTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("action", "Tomorrow");
                startActivity(intent);

            }
        });

        tvThisWeekTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("action", "This week");
                startActivity(intent);
            }
        });

        tvViewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ViewAppointmentActivity.class);
                intent.putExtra("action", "All");
                intent.putExtra("aid", "null");
                startActivity(intent);

            }
        });

        tvAddAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, AddAppointmentActivity.class);
                intent.putExtra("aptid", "null");
                startActivity(intent);


            }
        });


        tvWorkflowAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AuditTrailWorkFlowActivity.class);
                startActivity(intent);

            }
        });


        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);

            }
        });


        tvIntiateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, IntiateFileActivity.class);
                startActivity(intent);

            }
        });


        tvTaskTrackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, TaskTrackStatusActivity.class);
                startActivity(intent);


            }
        });


        tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FaqActivity.class);
                startActivity(intent);

            }
        });


        tvUserAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AuditTrailUserActivity.class);
                startActivity(intent);


            }
        });


        tvStorageAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AuditTrailStorageActivity.class);
                startActivity(intent);

            }
        });


        tvQuickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, QuickSearchActivity.class);
                intent.putExtra("text", "");
                startActivity(intent);

            }
        });


        tvShareFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SharedFilesActivity.class);
                startActivity(intent);
            }
        });


        tvShareFilesWithMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SharedWithMeActivity.class);
                startActivity(intent);
            }
        });


        tvMetaSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MetaDataSearchActivity.class);
                startActivity(intent);

            }
        });


        tvFreqQueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FrequentlyQueriesActivity.class);
                startActivity(intent);

            }
        });


        tvRecycleBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RecycleBinActivity.class);
                startActivity(intent);

            }
        });


        tvDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //logout();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_logout, null);

                Button btn_yes = dialogView.findViewById(R.id.btn_yes_logout_popup);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isNetworkAvailable()) {

                            logout();
                        } else {

                            Toast.makeText(MainActivity.this, "No internet connection found", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

                Button btn_no = dialogView.findViewById(R.id.btn_no_logout_popup);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

            }
        });


        //Arrows

        //workflow

        //ivIntiateWorkFlowArrow
        // llIntiateWorkMain

        llAppointMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_SHORT).show();

                if (llAppointmentLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llAppointmentLevel0.startAnimation(animation);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llAppointmentLevel0.setVisibility(View.GONE);
                            ivAppointmentArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);

                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llAppointmentLevel0.startAnimation(animation);


                    llAppointmentLevel0.setVisibility(View.VISIBLE);
                    ivAppointmentArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }
            }
        });


        lltodoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llTodoLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llTodoLevel0.startAnimation(animation);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llTodoLevel0.setVisibility(View.GONE);
                            ivTodoArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);

                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llTodoLevel0.startAnimation(animation);


                    llTodoLevel0.setVisibility(View.VISIBLE);
                    ivTodoArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        llIntiateWorkMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llWorkFlowManagementLevel1.getVisibility() == View.VISIBLE) {

                  /*  Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel1.startAnimation(animation);
*/
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llWorkFlowManagementLevel1.setVisibility(View.GONE);
                            ivIntiateWorkFlowArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);

                        }
                    }, 300);


                } else {


                   /* Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel1.startAnimation(animation);*/


                    llWorkFlowManagementLevel1.setVisibility(View.VISIBLE);
                    ivIntiateWorkFlowArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });

        //ivWorkFlowManagementArrow
        // llWorkflowManagementMain
        llWorkflowManagementMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llWorkFlowManagementLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llWorkFlowManagementLevel0.setVisibility(View.GONE);
                            ivWorkFlowManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llWorkFlowManagementLevel0.startAnimation(animation);


                    llWorkFlowManagementLevel0.setVisibility(View.VISIBLE);
                    ivWorkFlowManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });


        //workflow end

        ivUserManagementArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llUserManagementlevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llUserManagementlevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llUserManagementlevel0.setVisibility(View.GONE);
                            ivUserManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llUserManagementlevel0.startAnimation(animation);


                    llUserManagementlevel0.setVisibility(View.VISIBLE);
                    ivUserManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        //llStorageManegmentMain
        //ivStorageManagementArrow

        llStorageManegmentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llStorageManagementLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llStorageManagementLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llStorageManagementLevel0.setVisibility(View.GONE);
                            ivStorageManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llStorageManagementLevel0.startAnimation(animation);


                    llStorageManagementLevel0.setVisibility(View.VISIBLE);
                    ivStorageManagementArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });

        ivUserManagerArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llUserMangerlevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llUserMangerlevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llUserMangerlevel0.setVisibility(View.GONE);
                            ivUserManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llUserMangerlevel0.startAnimation(animation);


                    llUserMangerlevel0.setVisibility(View.VISIBLE);
                    ivUserManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        ivAuthArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (llAuthlevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llAuthlevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llAuthlevel0.setVisibility(View.GONE);
                            ivAuthArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llAuthlevel0.startAnimation(animation);


                    llAuthlevel0.setVisibility(View.VISIBLE);
                    ivAuthArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });


        //llSearchMain
        //ivSearcArrow

        llSearchMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llSearchLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llSearchLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llSearchLevel0.setVisibility(View.GONE);
                            ivSearcArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llSearchLevel0.startAnimation(animation);


                    llSearchLevel0.setVisibility(View.VISIBLE);
                    ivSearcArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });

        //ivAuditTrailArrow
        //llAudtrailMain

        llAudtrailMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llAuditTrailLevel0.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llAuditTrailLevel0.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llAuditTrailLevel0.setVisibility(View.GONE);
                            ivAuditTrailArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llAuditTrailLevel0.startAnimation(animation);


                    llAuditTrailLevel0.setVisibility(View.VISIBLE);
                    ivAuditTrailArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }


            }
        });

        //ivStorageManagerArrow
        //llStorageMangerMain
        llStorageMangerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llStorageManagementLevel1.getVisibility() == View.VISIBLE) {

                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right);
                    animation.setDuration(300);
                    llStorageManagementLevel1.startAnimation(animation);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            llStorageManagementLevel1.setVisibility(View.GONE);
                            ivStorageManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                            handler.removeCallbacks(null);
                        }
                    }, 300);


                } else {


                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left);
                    animation.setDuration(300);
                    llStorageManagementLevel1.startAnimation(animation);


                    llStorageManagementLevel1.setVisibility(View.VISIBLE);
                    ivStorageManagerArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                }

            }
        });


        navigationView.setNavigationItemSelectedListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        final MenuItem menuItem = menu.findItem(R.id.action_noti_bell);
        View actionView = MenuItemCompat.getActionView(menuItem);
        tvNotiBadgeCount = actionView.findViewById(R.id.bell_badge);
        //tvNotiBadgeCount.setVisibility(View.GONE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onOptionsItemSelected(menuItem);

            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {

            case R.id.action_noti_bell: {
                // Do something


                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);

                /*Intent intent = new Intent(MainActivity.this,VideoPlayerActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Bell Count : " + tvNotiBadgeCount.getText().toString(), Toast.LENGTH_SHORT).show();
*/

                //showAudioPlayerDialog(MainActivity.this);


                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


    public void getSlidId(String userid) {


        scrollViewdashboard.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        storageAllotedList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dashboard response", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jobj = jsonObject.getJSONObject("dashboard_data");

                    String totalfolder = jobj.getString("totalfolder");
                    String totalfile = jobj.getString("totalfile");
                    String size = jobj.getString("size");
                    String slId = jobj.getString("slid");
                    String inTray = jobj.getString("in_tray");
                    String numpages = jobj.getString("numpages");

                    slid_session = slId;

                    Log.e("sess id", slId);

                    //String storagealloted = jobj.getString("storagealloted");

                    JSONArray jsonArray = jobj.getJSONArray("storagealloted");

                    rvStorageAlloted.setVisibility(View.VISIBLE);
                    tvStorageAlloted.setVisibility(View.GONE);

                    if (jsonArray.length() == 0) {

                        rvStorageAlloted.setVisibility(View.GONE);
                        tvStorageAlloted.setVisibility(View.VISIBLE);
                        tvStorageAlloted.setText("No Storage Alloted");

                    } else {

                        if (jsonArray.length() > 1) {
                            multiStorage = "yes";

                        } else {

                            multiStorage = "no";
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String storagename = jsonArray.getJSONObject(i).getString("storage_name");
                            String storageid = jsonArray.getJSONObject(i).getString("storage_id");

                            storageAllotedList.add(new StorageAlloted(storagename, storageid));

                        }

                        rvStorageAlloted.setVisibility(View.VISIBLE);
                        tvStorageAlloted.setVisibility(View.GONE);


                    }

                    StorageAllotedAdapter storageAllotedAdapter = new StorageAllotedAdapter(storageAllotedList, MainActivity.this);
                    rvStorageAlloted.setAdapter(storageAllotedAdapter);
                    storageAllotedAdapter.setOnClickListener(new StorageAllotedAdapter.OnClickListener() {
                        @Override
                        public void onStorageClicked(String slid, String storagename) {

                            Intent intent = new Intent(MainActivity.this, DmsActivity.class);
                            intent.putExtra("slid", slid);
                            startActivity(intent);

                        }
                    });


//                    if (storagealloted != null) {
//
//                        tvStorageAlloted.setText(storagealloted);
//
//                    } else {
//                        Log.e("storage alloacted  ", "null");
//
//                    }


//                    tvStorageAlloted.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//
//                            Intent intent = new Intent(MainActivity.this, DmsActivity.class);
//                            startActivity(intent);
//
//
//                        }
//                    });

                    Log.e("slid in json loop ", slId);
                    Log.e("total file json loop", totalfile);
                    Log.e("total size loop ", size);
                    Log.e("total folder json loop", totalfolder);

                    sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("userSlid", slId);
                    editor.apply();


                    //pie piechart
                    generateReportChart(dmsReportChart, totalfile, totalfolder, numpages);

                    scrollViewdashboard.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.GONE);

                    getProfilePic(userid);
                    getUserPermission(userid, inTray, totalfile, totalfolder, size);
                    getMemoryUsed(session_userId, size);


                    // generateUserReportGraph(userReportGraph, barEntries);
                    //generateCalender();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);

                // getSlidId(userid);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                Log.e("userid before response", userid);
                parameters.put("userid", userid);
                parameters.put("action", "getDashBoardData");


                return parameters;
            }
        };


        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    void getUserReport(final String userid) {

        pbUserReport.setVisibility(View.VISIBLE);
        userReportGraph.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getUserReport", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray reportArray = jsonObject.getJSONArray("user_report_data");
                        ArrayList<BarEntry> barEntries = new ArrayList<>();

                        for (int i = 0; i < reportArray.length(); i++) {
                            float val = Float.parseFloat(reportArray.get(i).toString());
                            barEntries.add(new BarEntry(i, val));
                        }

                        generateUserReportGraph(userReportGraph, barEntries);

                    } else {


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
                params.put("action", "getUserReport");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getWorkflowStatus(final String userid) {

        pbWorkflowStatus.setVisibility(View.VISIBLE);
        workflowStatusChart.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getWorkflowStatus", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject workStatusObj = jsonObject.getJSONObject("workflow_status");
                        String completed = workStatusObj.getString("completed");
                        String processed = workStatusObj.getString("processed");
                        String pending = workStatusObj.getString("pending");
                        String approved = workStatusObj.getString("done");
                        String done = workStatusObj.getString("pending");
                        String rejected = workStatusObj.getString("rejected");

                        generateWorkflowStatusChart(workflowStatusChart, pending, processed, completed, done, approved, rejected);


                    } else {


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
                params.put("action", "getWorkflowStatus");

                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getMemoryUsed(final String userid, String totalmemory) {

        memoryStatusChart.setVisibility(View.GONE);
        pbMemoryUsage.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("getMemoryUsed", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    // String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject jObj = jsonObject.getJSONObject("memory_used");

                        JSONArray umArray = jObj.getJSONArray("upload_memory");
                        ArrayList<Entry> umXaxis = new ArrayList<>();
                        Log.e("umArray", String.valueOf(umArray.length()));
                        for (int i = 0; i < umArray.length(); i++) {

                            float val = Float.parseFloat(umArray.get(i).toString());
                            Log.e("xaxis", String.valueOf(val));
                            umXaxis.add(new Entry(i + 1, val));

                        }

                        JSONArray dmArray = jObj.getJSONArray("download_memory");
                        ArrayList<Entry> dmYaxis = new ArrayList<>();
                        Log.e("dmArray", String.valueOf(dmArray.length()));
                        for (int j = 0; j < dmArray.length(); j++) {

                            float val = Float.parseFloat(dmArray.get(j).toString());
                            Log.e("yaxis", String.valueOf(val));
                            dmYaxis.add(new Entry(j + 1, val));

                        }

                        generateMemoryUsageChart(memoryStatusChart, umXaxis, dmYaxis, totalmemory);


                    } else {

                        memoryStatusChart.setVisibility(View.VISIBLE);
                        pbMemoryUsage.setVisibility(View.GONE);

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
                params.put("action", "getMemoryUsed");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getWorkflowPriority(final String userid) {

        pbWorkflowPriority.setVisibility(View.VISIBLE);
        workflowPriorityChart.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("workflow_priority_res", response);

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject workPriorObj = jsonObject.getJSONObject("workflow_priority");
                        String urgent = workPriorObj.getString("up");
                        String medium = workPriorObj.getString("mp");
                        String normal = workPriorObj.getString("np");


                        generateWorkflowPriorityChart(workflowPriorityChart, urgent, medium, normal);


                    } else {


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
                params.put("action", "getWorkflowPriority");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    void getProfilePic(final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String profilepic = jsonObject.getString("profilepic");

                    if (profilepic.isEmpty() || profilepic.equals("")) {

                        circleImageView.setImageResource(R.drawable.ic_avatar);

                    } else {
                        byte[] decodedString = Base64.decode(profilepic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        circleImageView.setImageBitmap(decodedByte);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getProfilePic(userid);
                System.out.println("i am here: " + new Exception().getStackTrace()[0]);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);


                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                JSONObject js = new JSONObject(params);
                Log.e("profile params", js.toString());


                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    void getUserPermission(final String userid, String intrayNum, String numoffiles, String numoffolders, String memoryused) {

        String SHOWCASE_ID = String.valueOf(new Random().nextInt(60000));

        dashBoardList.clear();

        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setMaskColor(getResources().getColor(R.color.black_blur));
        //config.setShapePadding(96);
        config.setDelay(200); // half second between each showcase view

        sequence = new MaterialShowcaseSequence(MainActivity.this, SHOWCASE_ID);
        sequence.setConfig(config);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    //  "metadata_search": "1",
                    //        "metadata_quick_search": "1",

                    //String userrole = jsonObject.getString("user_role");
                    String roleID = jsonObject.getString("role_id");
                    String recyclebin = jsonObject.getString("view_recycle_bin");
                    String auditTrailUserAudit = jsonObject.getString("view_user_audit");
                    String auditTrailStorageAudit = jsonObject.getString("view_storage_audit");
                    String sharefileswithme = jsonObject.getString("share_with_me");
                    String sharedfiles = jsonObject.getString("shared_file");
                    String metadatasearch = jsonObject.getString("metadata_search");
                    String metadataquicksearch = jsonObject.getString("metadata_quick_search");
                    String saveQuery = jsonObject.getString("save_query");


                    //String view_workflow_list = jsonObject.getString("view_workflow_list");
                    String dashboard_edit_profile = jsonObject.getString("dashboard_edit_profile");
                    String dashboard_mydms = jsonObject.getString("dashboard_mydms");
                    String dashboard_mytask = jsonObject.getString("dashboard_mytask");

                    String num_of_folder = jsonObject.getString("num_of_folder");
                    String num_of_file = jsonObject.getString("num_of_file");
                    String memory_used = jsonObject.getString("memory_used");


                    //workflow management
                    String workflow_audit = jsonObject.getString("workflow_audit");
                    String workflow_task_track = jsonObject.getString("workflow_task_track");
                    String view_workflow_list = jsonObject.getString("view_workflow_list");


                    //Graphs
                    String workflow_status_chart = jsonObject.getString("status_wf");
                    String workflow_prior_chart = jsonObject.getString("priority_wf");
                    String workflow_user_report_chart = jsonObject.getString("user_graph");

                    /* "todo_add":"1",
      "todo_edit":"1",
      "todo_archive":"1",
      "todo_view":"1",*/

                    /*

                    *  "appoint_add":"1",
      "appoint_edit":"1",
      "appoint_archive":"1",
      "appoint_view":"1",*/

                    //Todo permissions
                    String todo_dash_chart = jsonObject.getString("todo_view");
                    String todo_view = jsonObject.getString("todo_view");
                    String todo_add = jsonObject.getString("todo_add");


                    //Appointment permission
                    String apt_dash_chart = jsonObject.getString("appoint_view");
                    String apt_view = jsonObject.getString("appoint_view");
                    String apt_add = jsonObject.getString("appoint_add");


                    //calender graph
                    String calender_graph = jsonObject.getString("calendar_wf");


                    roleId = roleID;


                    rvDashboard.setAdapter(new DashboardAdapter(dashBoardList, MainActivity.this));


                    if (calender_graph.equalsIgnoreCase("1")) {

                        clCalenderDash.setVisibility(View.VISIBLE);

                    } else {

                        clCalenderDash.setVisibility(View.GONE);

                    }


                    if (todo_dash_chart.equalsIgnoreCase("1")) {

                        cvTodoDash.setVisibility(View.VISIBLE);

                    } else {

                        cvTodoDash.setVisibility(View.GONE);

                    }

                    if (apt_dash_chart.equalsIgnoreCase("1")) {

                        cvAptDash.setVisibility(View.VISIBLE);

                    } else {

                        cvAptDash.setVisibility(View.GONE);

                    }


                    if (workflow_user_report_chart.equalsIgnoreCase("1")) {

                        cvDmsReport.setVisibility(View.VISIBLE);

                    } else {

                        cvDmsReport.setVisibility(View.GONE);

                    }


                    if (workflow_status_chart.equalsIgnoreCase("1")) {

                        cvWorkflowStatusGraph.setVisibility(View.VISIBLE);

                    } else {

                        cvWorkflowStatusGraph.setVisibility(View.GONE);

                    }


                    if (workflow_prior_chart.equalsIgnoreCase("1")) {

                        cvWfPriorGraph.setVisibility(View.VISIBLE);

                    } else {

                        cvWfPriorGraph.setVisibility(View.GONE);

                    }


                    if (dashboard_mydms.equalsIgnoreCase("0")) {

                        llStorageMangerMain.setVisibility(View.GONE);

                    } else {

                        dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_layers), "My Dms", "Explore the dms"));
                        llStorageMangerMain.setVisibility(View.VISIBLE);


                    }


                    if (dashboard_mytask.equalsIgnoreCase("0")) {


                        intray = "0";

                    } else {

                        intray = "1";

                        dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_intray), "In Tray", intrayNum + " Tasks"));

                    }


                    if (num_of_folder.equalsIgnoreCase("1") || num_of_file.equalsIgnoreCase("1")) {

                        cvDmsReport.setVisibility(View.VISIBLE);

                    } else {

                        cvDmsReport.setVisibility(View.GONE);

                        //dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_no_of_folders), "No. of Folders", numoffolders + " Folders"));
                        //sequence.addSequenceItem(ivNoOfFolders,"Total number of folder alloted to the user according to the rights given. ", "Next");


                    }


                    if (memory_used.equalsIgnoreCase("1")) {


                        cvMemoryUsageGraph.setVisibility(View.VISIBLE);

                    } else {

                        cvMemoryUsageGraph.setVisibility(View.GONE);


                    }


                    if (dashboard_edit_profile.equalsIgnoreCase("0")) {

                        btnEditProfile.setVisibility(View.GONE);

                    } else {

                        btnEditProfile.setVisibility(View.VISIBLE);
                    }

                    //search modules
                    if (metadataquicksearch.equalsIgnoreCase("1") || saveQuery.equalsIgnoreCase("1") || metadatasearch.equalsIgnoreCase("1")) {

                        llSearchMain.setVisibility(View.VISIBLE);

                        if (saveQuery.equalsIgnoreCase("1")) {

                            tvFreqQueries.setVisibility(View.VISIBLE);
                        } else {

                            tvFreqQueries.setVisibility(View.GONE);

                        }


                        if (metadataquicksearch.equals("0") && metadatasearch.equals("0")) {

                            llSearchMain.setVisibility(View.GONE);

                        } else {

                            llSearchMain.setVisibility(View.VISIBLE);

                        }

                        if (metadataquicksearch.equals("0")) {

                            tvQuickSearch.setVisibility(View.GONE);


                        } else {

                            tvQuickSearch.setVisibility(View.VISIBLE);

                        }

                        if (metadatasearch.equals("0")) {

                            tvMetaSearch.setVisibility(View.GONE);

                        } else {

                            tvMetaSearch.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.meta_search), "Metadata Search", "Search files using metadata"));

                        }


                    } else {

                        llSearchMain.setVisibility(View.GONE);

                    }


                    //storage mangement
                    if (sharedfiles.equalsIgnoreCase("1")
                            || sharefileswithme.equalsIgnoreCase("1")
                            || recyclebin.equalsIgnoreCase("1") || dashboard_mydms.equalsIgnoreCase("1")) {

                        llStorageMangerMain.setVisibility(View.VISIBLE);

                        if (dashboard_mydms.equalsIgnoreCase("1")) {

                            llStorageMangerMain.setVisibility(View.VISIBLE);
                            rvStorageAlloted.setVisibility(View.VISIBLE);
                            tvStorageAlloted.setVisibility(View.GONE);


                        } else {

                            llStorageMangerMain.setVisibility(View.GONE);
                            rvStorageAlloted.setVisibility(View.GONE);
                            tvStorageAlloted.setVisibility(View.VISIBLE);


                        }


                        if (sharefileswithme.equals("0")) {

                            tvShareFilesWithMe.setVisibility(View.GONE);

                        } else {

                            tvShareFilesWithMe.setVisibility(View.VISIBLE);
                        }

                        if (sharedfiles.equals("0")) {

                            tvShareFiles.setVisibility(View.GONE);

                        } else {

                            tvShareFiles.setVisibility(View.VISIBLE);

                        }

                        if (recyclebin.equals("0")) {

                            tvRecycleBin.setVisibility(View.GONE);


                        } else {

                            tvRecycleBin.setVisibility(View.VISIBLE);

                        }


                    } else {

                        llStorageMangerMain.setVisibility(View.GONE);

                    }


                    //Audit trail
                    if (auditTrailUserAudit.equals("1") || auditTrailStorageAudit.equals("1")) {

                        llAudtrailMain.setVisibility(View.VISIBLE);

                        if (auditTrailUserAudit.equals("0")) {

                            tvUserAudit.setVisibility(View.GONE);

                        } else {

                            tvUserAudit.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_user), "User Report", "Userwise audit trail"));


                        }

                        if (auditTrailStorageAudit.equals("0")) {

                            tvStorageAudit.setVisibility(View.GONE);

                        } else {

                            tvStorageAudit.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.ic_storage_blue), "Storage Report", "Storagewise audit trail"));

                        }


                    } else {

                        llAudtrailMain.setVisibility(View.GONE);
                    }


                    //workflow
                    //here it should be and operator && instead of or ||

                    if (workflow_task_track.equals("1") || view_workflow_list.equals("1") || workflow_audit.equals("1")) {

                        llWorkflowManagementMain.setVisibility(View.VISIBLE);

                        if (workflow_task_track.equals("0")) {


                            tasktrackstatus = "0";
                            tvTaskTrackStatus.setVisibility(View.GONE);


                        } else {

                            tasktrackstatus = "1";
                            tvTaskTrackStatus.setVisibility(View.VISIBLE);

                        }


                        if (view_workflow_list.equals("0")) {

                            llIntiateWorkMain.setVisibility(View.GONE);

                        } else {

                            llIntiateWorkMain.setVisibility(View.VISIBLE);

                        }


                        if (workflow_audit.equals("0")) {

                            tvWorkflowAudit.setVisibility(View.GONE);


                        } else {

                            tvWorkflowAudit.setVisibility(View.VISIBLE);
                            dashBoardList.add(new DashBoard(getResources().getDrawable(R.drawable.workflow_report), "Workflow Report", "Workflow audit trail"));

                        }


                    } else {

                        llWorkflowManagementMain.setVisibility(View.GONE);


                    }

                    //todo
                    if (todo_view.equalsIgnoreCase("1") || todo_add.equalsIgnoreCase("1")) {


                        lltodoMain.setVisibility(View.VISIBLE);

                        if (todo_view.equalsIgnoreCase("1")) {

                            tvAllTodo.setVisibility(View.VISIBLE);
                            tvThisWeekTodo.setVisibility(View.VISIBLE);
                            tvTodayTodo.setVisibility(View.VISIBLE);
                            tvTommorowTodo.setVisibility(View.VISIBLE);

                        } else {

                            tvAllTodo.setVisibility(View.GONE);
                            tvThisWeekTodo.setVisibility(View.GONE);
                            tvTodayTodo.setVisibility(View.GONE);
                            tvTommorowTodo.setVisibility(View.GONE);

                        }

                        if (todo_add.equalsIgnoreCase("1")) {

                            tvAddTodo.setVisibility(View.VISIBLE);

                        } else {

                            tvAddTodo.setVisibility(View.GONE);

                        }

                    } else {

                        lltodoMain.setVisibility(View.GONE);


                    }


                    //Appointment perimissions

                    if (apt_view.equalsIgnoreCase("1") || apt_add.equalsIgnoreCase("1")) {


                        llAppointMain.setVisibility(View.VISIBLE);

                        if (apt_view.equalsIgnoreCase("1")) {

                            tvViewAppointment.setVisibility(View.VISIBLE);

                        } else {


                            tvViewAppointment.setVisibility(View.GONE);

                        }

                        if (apt_add.equalsIgnoreCase("1")) {

                            tvAddAppointment.setVisibility(View.VISIBLE);

                        } else {

                            tvAddAppointment.setVisibility(View.GONE);

                        }

                    } else {

                        llAppointMain.setVisibility(View.GONE);


                    }


                    dashboardAdapter = new DashboardAdapter(dashBoardList, MainActivity.this);
                    //dashboardAdapter.notifyDataSetChanged();

                    //Tour guide for the mainactivity
                    // Checking for first time launch
                    prefManager = new PrefManager(MainActivity.this);
                    if (prefManager.isFirstTimeLaunch()) {

                        showTourGuide();

                    }

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


                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                JSONObject js = new JSONObject(params);
                Log.e("getuserper params", js.toString());

                return params;
            }
        };

        swipeRefreshLayout.setRefreshing(false);

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    private void addNotification(String filecount) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_logo_notification)
                        .setContentTitle("New File Shared")
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setContentText("You have received " + filecount + " new files.");

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);

        builder.setAutoCancel(true);

        Intent notificationIntent = new Intent(this, SharedFilesActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @NonNull
    private String getDeviceIpAddress() {
        String actualConnectedToNetwork = null;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                actualConnectedToNetwork = getWifiIp();
            }
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = "127.0.0.1";
        }
        return actualConnectedToNetwork;
    }

    @Nullable
    private String getWifiIp() {
        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            int ip = mWifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                    + ((ip >> 24) & 0xFF);
        }
        return null;
    }

    @Nullable
    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();


                        /* Log.e("Hostname",host);*/
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


        showNoInternetLayout(isConnected);

    }

    void showNoInternetLayout(Boolean isconnected) {


        if (!isconnected) {

            llnointernetfound.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);


        } else {

            llnointernetfound.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);


        // register connection status listener
        Initializer.getInstance().setConnectivityListener(this);
    }


    void logout() {

        alertDialog.dismiss();
        pDLogout = new ProgressDialog(this);
        pDLogout.setTitle("Logging out");
        pDLogout.setMessage("Please wait");
        pDLogout.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGOUT, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(String response) {

                Log.e("logout", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("false")) {

                        session.logoutUser();


                        FCMHandler.disableFCM();


                        //Util.clearApplicationData(MainActivity.this);


                        // FirebaseMessagingService.clearToken(MainActivity.this);
                        //stopService(inTrayServiceIntent);
                        finishAffinity();

                        pDLogout.dismiss();


                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        pDLogout.dismiss();

                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                pDLogout.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", session_userId);

                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //getting the work flow list

    void getWorkFlowList(final String userid) {

        workFlowList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("workflowlist res", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {

                        etFilterWfList.setVisibility(View.GONE);
                        rvWorkFLowlist.setVisibility(View.GONE);
                        tvNoWfFoundNoWfFound.setVisibility(View.VISIBLE);


                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            String workflowid = jsonArray.getJSONObject(i).getString("workflow_id");
                            String workflowname = jsonArray.getJSONObject(i).getString("workflow_name");

                            workFlowList.add(new WorkFlowList(workflowname, workflowid));


                        }

                        workFlowListAdapter = new WorkFlowListAdapter(workFlowList, MainActivity.this);
                        workFlowListAdapter.notifyDataSetChanged();
                        rvWorkFLowlist.setAdapter(workFlowListAdapter);

                        etFilterWfList.setVisibility(View.VISIBLE);
                        rvWorkFLowlist.setVisibility(View.VISIBLE);
                        tvNoWfFoundNoWfFound.setVisibility(View.GONE);

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

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }


    void showTourGuide() {

        prefManager.setFirstTimeLaunch(false);



      /*  // single example
        new MaterialShowcaseView.Builder(this)
                .setTarget(ivMyDms)
                .setDismissText("GOT IT")
                .setContentText("This is some amazing feature you should know about")
                .setDelay(1500) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse(String.valueOf(SHOWCASE_ID)) // provide a unique ID used to ensure it is only shown once
                .show();*/



       /* DrawerLayout drawer = findViewById(R.id.drawer_layout);

        sequence.addSequenceItem( new MaterialShowcaseView.Builder(this)
                .setTarget(ivWorkFlowManagementArrow)
                .setDismissText("Next")
                .setContentText("Workflow Management")
                .setMaskColour(getResources().getColor(R.color.black_blur))
                .withRectangleShape(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                        drawer.openDrawer(Gravity.START);
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {

                    }
                })
                .build()
        );*/


      /*  sequence.addSequenceItem(quickSearch,
                "Quick search documents,files in the entire “DMS”.", "Close");
*/

        sequence.start();


    }


    void generateReportChart(PieChart chart, String numfiles, String numfolder, String numpages) {

        pbDmsReport.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);


        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 5, 5, 10);

        // chart.setDragDecelerationFrictionCoef(0.95f);

        // chart.setCenterTextTypeface(tfLight);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(80);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(70f);
        chart.setTouchEnabled(false);
        chart.setDrawCenterText(true);
        /*chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL",pe.getLabel());


            }

            @Override
            public void onNothingSelected() {

            }
        });*/
 /*       chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(3000, Easing.EaseInOutQuad);*/


        LegendEntry folder = new LegendEntry();
        folder.label = numfolder + " Folders";
        folder.formColor = getResources().getColor(R.color.colorPrimary);

        LegendEntry files = new LegendEntry();
        files.label = numfiles + " Files";
        files.formColor = getResources().getColor(R.color.purple);

        LegendEntry pages = new LegendEntry();
        pages.label = numpages + " Pages";
        pages.formColor = getResources().getColor(R.color.yellow_dark);


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(5f);
        l.setTextSize(13f);
        l.setCustom(Arrays.asList(folder, files, pages));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(10f);


        float nfld = Float.parseFloat(numfolder);
        float nfiles = Float.parseFloat(numfiles);
        float npages = Float.parseFloat(numpages);
        float total = nfld + nfiles + npages;


        if (total != 0) {

            SpannableString s = new SpannableString("DMS Report");
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 10, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, 0);
            chart.setCenterText(s);

        } else {

            SpannableString s = new SpannableString("No Data Found");
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 13, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, 0);
            chart.setCenterText(s);
        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();


        if (nfld != 0) {
            entries.add(new PieEntry((nfld / total) * 100, "Folders"));
            colors.add(getResources().getColor(R.color.colorPrimary));

        }


        if (nfiles != 0) {
            entries.add(new PieEntry((nfiles / total) * 100, "Files"));
            colors.add(getResources().getColor(R.color.purple));
        }

        if (npages != 0) {
            entries.add(new PieEntry((npages / total) * 100, "Pages"));
            colors.add(getResources().getColor(R.color.orange));


        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //data.setValueTypeface(tfLight);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    case MotionEvent.EDGE_RIGHT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }
                    case MotionEvent.EDGE_LEFT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }


                    //case MotionEvent.ACTION_CANCEL:

                    case MotionEvent.ACTION_UP: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }

                return false;
            }
        });


        chart.invalidate();


        pbDmsReport.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);


    }

    void generateWorkflowStatusChart(PieChart chart, String pending, String processed, String complete, String done, String approved, String rejected) {


        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 5, 5, 10);
        // chart.setDragDecelerationFrictionCoef(0.95f);

        // chart.setCenterTextTypeface(tfLight);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(80);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(70f);

        chart.setTouchEnabled(true);

        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL", pe.getLabel());

                String label = pe.getLabel();

                if (label.equalsIgnoreCase("Pending")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskStatus", "Pending");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Processed")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskStatus", "Processed");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Complete")) {

                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskStatus", "Complete");
                    startActivity(intent);

                }


            }

            @Override
            public void onNothingSelected() {

            }
        });


        LegendEntry folder = new LegendEntry();
        folder.label = pending + " Pending";
        folder.formColor = getResources().getColor(R.color.red);

        LegendEntry files = new LegendEntry();
        files.label = processed + " Processed";
        files.formColor = getResources().getColor(R.color.yellow);

        LegendEntry pages = new LegendEntry();
        pages.label = complete + " Completed";
        pages.formColor = getResources().getColor(R.color.green);

       /* LegendEntry approv = new LegendEntry();
        approv.label = complete + " Appr.";
        approv.formColor = getResources().getColor(R.color.green_dark);

        LegendEntry rejec = new LegendEntry();
        rejec.label = complete + " Appr.";
        rejec.formColor = getResources().getColor(R.color.red);

        LegendEntry dne = new LegendEntry();
        dne.label = complete + " Done.";
        dne.formColor = getResources().getColor(R.color.colorPrimary);*/


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(5f);
        l.setTextSize(13f);
        l.setCustom(Arrays.asList(folder, files, pages));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(10f);


        float nPen = Float.parseFloat(pending);
        float nPros = Float.parseFloat(processed);
        float nCmpl = Float.parseFloat(complete);
        float nApr = Float.parseFloat(approved);
        float nDone = Float.parseFloat(done);
        float nRej = Float.parseFloat(rejected);

//+nApr+nDone+nRej
        float total = nPen + nPros + nCmpl;

        //&& nApr == 0 && nDone == 0 && nRej == 0

        if (nPen == 0 && nPros == 0 && nCmpl == 0) {

            SpannableString s = new SpannableString("No Data Found");
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 13, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, 0);
            chart.setCenterText(s);

        } else {

            SpannableString s = new SpannableString("Workflow Status");
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 15, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, 0);
            chart.setCenterText(s);

        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();


        if (nPen != 0) {

            entries.add(new PieEntry((nPen / total) * 100, "Pending", 0));
            colors.add(getResources().getColor(R.color.red));
        }

        if (nPros != 0) {


            entries.add(new PieEntry((nPros / total) * 100, "Processed", 1));
            colors.add(getResources().getColor(R.color.yellow));
        }

        if (nCmpl != 0) {

            entries.add(new PieEntry((nCmpl / total) * 100, "Complete", 2));
            colors.add(getResources().getColor(R.color.green));
        }

       /* if (nApr != 0) {

            entries.add(new PieEntry((nApr / total) * 100, "Approved", 3));
            colors.add(getResources().getColor(R.color.green_dark));
        }  if (nDone != 0) {

            entries.add(new PieEntry((nDone / total) * 100, "Done", 4));
            colors.add(getResources().getColor(R.color.colorPrimary));
        }
        if (nRej != 0) {

            entries.add(new PieEntry((nRej / total) * 100, "Rejected", 5));
            colors.add(getResources().getColor(R.color.red));
        }*/

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //data.setValueTypeface(tfLight);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    case MotionEvent.EDGE_RIGHT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }
                    case MotionEvent.EDGE_LEFT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }


                    //case MotionEvent.ACTION_CANCEL:

                    case MotionEvent.ACTION_UP: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }

                return false;
            }
        });


        chart.invalidate();

        pbWorkflowStatus.setVisibility(View.GONE);
        workflowStatusChart.setVisibility(View.VISIBLE);


    }

    void generateWorkflowPriorityChart(PieChart chart, String Urgent, String Medium, String Normal) {


        chart.setUsePercentValues(false);
        // chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 5, 5, 10);

        // chart.setDragDecelerationFrictionCoef(0.95f);

        // chart.setCenterTextTypeface(tfLight);


        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(80);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(70f);

        chart.setTouchEnabled(true);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        // no description text
        chart.getDescription().setEnabled(false);

        chart.setDrawCenterText(true);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL", pe.getLabel());

                String label = pe.getLabel();

                if (label.equalsIgnoreCase("Urgent")) {

                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskPrior", "1");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Medium")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskPrior", "2");
                    startActivity(intent);

                } else if (label.equalsIgnoreCase("Normal")) {


                    Intent intent = new Intent(MainActivity.this, InTrayActivity.class);
                    intent.putExtra("taskPrior", "3");
                    startActivity(intent);


                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        //chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        //chart.setRotationEnabled(true);
        // chart.setHighlightPerTapEnabled(true);

        // chart.animateY(1400, Easing.EaseInOutQuad);


        LegendEntry folder = new LegendEntry();
        folder.label = Urgent + " Urgent";
        folder.formColor = Color.RED;

        LegendEntry files = new LegendEntry();
        files.label = Medium + " Medium";
        files.formColor = getResources().getColor(R.color.yellow);

        LegendEntry pages = new LegendEntry();
        pages.label = Normal + " Normal";
        pages.formColor = getResources().getColor(R.color.green);


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(5f);
        l.setTextSize(13f);
        l.setCustom(Arrays.asList(folder, files, pages));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(10f);

        float nUrg = Float.parseFloat(Urgent);
        float nMed = Float.parseFloat(Medium);
        float nNrml = Float.parseFloat(Normal);
        float total = nUrg + nMed + nNrml;


        if (nUrg == 0 && nMed == 0 && nNrml == 0) {

            SpannableString s = new SpannableString("No Data Found");
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 13, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, 0);
            chart.setCenterText(s);

        } else {


            SpannableString s = new SpannableString("Workflow Priority");
            s.setSpan(new RelativeSizeSpan(1.1f), 0, 17, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, 17, 0);
            chart.setCenterText(s);

        }


        ArrayList<PieEntry> entries = new ArrayList<>();
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();


        if (nUrg != 0) {

            entries.add(new PieEntry((nUrg / total) * 100, "Urgent"));
            colors.add(Color.RED);
        }


        if (nMed != 0) {

            entries.add(new PieEntry((nMed / total) * 100, "Medium"));
            colors.add(getResources().getColor(R.color.yellow));

        }
        if (nNrml != 0) {

            entries.add(new PieEntry((nNrml / total) * 100, "Normal"));
            colors.add(getResources().getColor(R.color.green));

        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        //data.setValueTypeface(tfLight);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    case MotionEvent.EDGE_RIGHT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }
                    case MotionEvent.EDGE_LEFT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }


                    //case MotionEvent.ACTION_CANCEL:

                    case MotionEvent.ACTION_UP: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }

                return false;
            }
        });


        chart.invalidate();

        pbWorkflowPriority.setVisibility(View.GONE);
        workflowPriorityChart.setVisibility(View.VISIBLE);


    }

    void generateMemoryUsageChart(LineChart chart, ArrayList<Entry> values1, ArrayList<Entry> values2, String totalMemory) {

        Log.e("error", "1");

      /*  values1.add(new Entry(1,(float) 0));
        values1.add(new Entry(2,(float) 0));
        values1.add(new Entry(3,(float) 0));
        values1.add(new Entry(4,(float) 0));
        values1.add(new Entry(5,(float) 0));
        values1.add(new Entry(6, (float) 2.08));
        values1.add(new Entry(7,(float) 0.01));
        values1.add(new Entry(8,(float) 0));
        values1.add(new Entry(9,(float) 0));
        values1.add(new Entry(10,(float) 0));
        values1.add(new Entry(11,(float) 0));
        values1.add(new Entry(12,(float) 0));

        values2.add(new Entry(1,(float) 0));
        values2.add(new Entry(2,(float) 0));
        values2.add(new Entry(3,(float) 0));
        values2.add(new Entry(4,(float) 0));
        values2.add(new Entry(5,(float) 0));
        values2.add(new Entry(6, (float)0));
        values2.add(new Entry(7,(float) 10.02));
        values2.add(new Entry(8,(float) 0));
        values2.add(new Entry(9,(float) 0.49));
        values2.add(new Entry(10,(float) 2.08));
        values2.add(new Entry(11,(float) 6.07));
        values2.add(new Entry(12,(float) 0));*/

        TextView tvTotal = findViewById(R.id.tv_heading_memory_status_chart);
        tvTotal.setText("Memory usage in ( " + totalMemory + " )");

        LineDataSet set1, set2;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

            Log.e("error", "2");

            pbMemoryUsage.setVisibility(View.GONE);
            memoryStatusChart.setVisibility(View.VISIBLE);

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values1, "Upload Memory in MB");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.graph_blue));
            set1.setCircleColor(getResources().getColor(R.color.graph_blue));
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(getResources().getColor(R.color.graph_blue));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(true);
            set1.setDrawFilled(true);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = new LineDataSet(values2, "Download Memory in MB");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(getResources().getColor(R.color.graph_pink));
            set2.setCircleColor(getResources().getColor(R.color.graph_pink));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(getResources().getColor(R.color.graph_pink));
            set2.setDrawCircleHole(true);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawFilled(true);
            //set2.setFillFormatter(new MyFillFormatter(900f));

            // create a data object with the data sets
            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);


            // set data
            chart.getDescription().setEnabled(false);
            chart.animate();
            //chart.setMaxVisibleValueCount(100);
            chart.setData(data);

            chart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                            break;
                        }
                        case MotionEvent.EDGE_RIGHT: {
                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                            break;

                        }
                        case MotionEvent.EDGE_LEFT: {
                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                            break;

                        }


                        //case MotionEvent.ACTION_CANCEL:

                        case MotionEvent.ACTION_UP: {
                            horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                            break;
                        }
                    }

                    return false;
                }
            });

            Log.e("error", "2");

            pbMemoryUsage.setVisibility(View.GONE);
            memoryStatusChart.setVisibility(View.VISIBLE);
        }
    }

    void generateUserReportGraph(BarChart barChart, ArrayList<BarEntry> entries) {

       /* List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));
*/
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Jan");
        xAxisLabel.add("Feb");
        xAxisLabel.add("Mar");
        xAxisLabel.add("Apr");
        xAxisLabel.add("May");
        xAxisLabel.add("Jun");
        xAxisLabel.add("Jul");
        xAxisLabel.add("Aug");
        xAxisLabel.add("Sept");
        xAxisLabel.add("Oct");
        xAxisLabel.add("Nov");
        xAxisLabel.add("Dec");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int) value);
            }
        });


        int year = Calendar.getInstance().get(Calendar.YEAR);
        BarDataSet set = new BarDataSet(entries, "Active User Analytics " + year);
        //set.setColor(getResources().getColor(R.color.graph_blue));
        //set.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate();
        barChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    case MotionEvent.EDGE_RIGHT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }
                    case MotionEvent.EDGE_LEFT: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                        break;

                    }


                    //case MotionEvent.ACTION_CANCEL:

                    case MotionEvent.ACTION_UP: {
                        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }

                return false;
            }
        });


        // refresh

        pbUserReport.setVisibility(View.GONE);
        userReportGraph.setVisibility(View.VISIBLE);


    }

    void generateCalender() {

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar calendar = eventDay.getCalendar();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Log.e("selected date", dateFormat.format(calendar.getTime()));


                if (checkContainsDate(eventDay, events).equalsIgnoreCase("yes")) {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    // Create and show the dialog.
                    String dateSelected = dateFormat.format(calendar.getTime());
                    TabbedDialog dialogFragment = TabbedDialog.createInstance(dateSelected);
                    /*dialogFragment..getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                    dialogFragment.show(ft, "dialog");
                    dialogFragment.setOnButtonClickListener(new TabbedDialog.OnButtonClickListener() {
                        @Override
                        public void onClickCancelButton() {

                            dialogFragment.dismiss();
                        }
                    });


                }


            }
        });


       /* for (EventDay ev : events
                ) {

            Log.e("event day", String.valueOf(ev.getCalendar().getTime()));

        }*/

        // Calendar calendar = Calendar.getInstance();
        //events.add(new EventDay(calendar,Util.getCircleDrawableWithText(MainActivity.this,"Ankit")));

        calendarView.setEvents(events);

        //calendarView.setDisabledDays();


    }


    void getTodoList(String userid) {

        todoLists.clear();
        pbTodo.setVisibility(View.VISIBLE);
        rvTodo.setVisibility(View.GONE);
        llnoTodoFound.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("tooddoo list", response);

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");


                    if (error.equals("true")) {

                        pbTodo.setVisibility(View.GONE);
                        rvTodo.setVisibility(View.GONE);
                        llnoTodoFound.setVisibility(View.VISIBLE);


                    } else if (error.equals("false")) {

/*
                        "id": "1",
                         "emp_id": "1",
                                "task_name": "ded",
                                "task_description": "e",
                                "task_date": "2018-11-28",
                                "task_time": "12:09 PM",
                                "task_notification_frequency": "0",
                                "task_notify_time": "12:09 PM",
                                "is_archived": "0"
*/

                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String todoid = jsonArray.getJSONObject(i).getString("id");
                            String empid = jsonArray.getJSONObject(i).getString("emp_id");
                            String taskname = jsonArray.getJSONObject(i).getString("task_name");
                            String taskdate = jsonArray.getJSONObject(i).getString("task_date");
                            String tasktime = jsonArray.getJSONObject(i).getString("task_time");


                            TodoList todoList = new TodoList();
                            todoList.setTaskName(taskname);
                            todoList.setEmpId(empid);
                            todoList.setTaskDate(taskdate);
                            todoList.setTaskTime(tasktime);
                            todoList.setTodoId(todoid);
                            todoLists.add(todoList);


                        }

                        rvTodo.setAdapter(new TodolistAdapter(todoLists, MainActivity.this));

                        pbTodo.setVisibility(View.GONE);
                        llnoTodoFound.setVisibility(View.GONE);
                        rvTodo.setVisibility(View.VISIBLE);


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
                params.put("action", "getAllTodo");
                return params;

            }
        };


        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    void getAppointmentList(String userid) {

        appointmentLists.clear();
        pbAptment.setVisibility(View.VISIBLE);
        llnoAptFound.setVisibility(View.GONE);
        rvAppointment.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("response apt dash", response);

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");

                    if (error.equals("true")) {
                        pbAptment.setVisibility(View.GONE);
                        rvAppointment.setVisibility(View.GONE);
                        llnoAptFound.setVisibility(View.VISIBLE);

                    } else if (error.equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        //Toast.makeText(MainActivity.this, String.valueOf(jsonArray.length()), Toast.LENGTH_SHORT).show();


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String aptUserid = jsonArray.getJSONObject(i).getString("user_id");
                            String aptid = jsonArray.getJSONObject(i).getString("id");
                            String aptTitle = jsonArray.getJSONObject(i).getString("title");
                            String aptDate = jsonArray.getJSONObject(i).getString("app_date");
                            String aptTime = jsonArray.getJSONObject(i).getString("app_time");

                            AppointmentList appointmentList = new AppointmentList();
                            appointmentList.setTitle(aptTitle);
                            appointmentList.setUserId(aptUserid);
                            appointmentList.setAppDate(aptDate);
                            appointmentList.setAppTime(aptTime);
                            appointmentList.setId(aptid);

                            appointmentLists.add(appointmentList);


                        }


                        // Toast.makeText(MainActivity.this,   "Size of string" + String.valueOf(appointmentLists.size()), Toast.LENGTH_LONG).show();

                        rvAppointment.setAdapter(new AppointmentlistAdapter(appointmentLists, MainActivity.this));
                        pbAptment.setVisibility(View.GONE);
                        llnoAptFound.setVisibility(View.GONE);
                        rvAppointment.setVisibility(View.VISIBLE);

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
                params.put("action", "getAllApt");
                return params;

            }
        };


        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    void getEventsDates(String userid) {

        events.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    events.clear();
                    Log.e("eventlist_response", response);
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray todoarray = jsonObject.getJSONArray("tododates");
                    JSONArray aptarray = jsonObject.getJSONArray("aptdates");
                    JSONArray todoaptarray = jsonObject.getJSONArray("commondates");

                    for (int i = 0; i < todoarray.length(); i++) {


                        try {


                            String tDate = todoarray.getString(i);
                            String pattern = "yyyy-MM-dd";
                            DateFormat df = new SimpleDateFormat(pattern);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(df.parse(tDate));

                            //calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                            // calendar.add(Calendar.MONTH,-1);

                            Log.e("todyear", String.valueOf(calendar.get(Calendar.YEAR)));
                            Log.e("todmonth", String.valueOf(calendar.get(Calendar.MONTH)));
                            Log.e("todday", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                            Log.e("todDate modi", String.valueOf(calendar.getTime()));
                            events.add(new EventDay(calendar, R.drawable.todo));


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                    for (int j = 0; j < aptarray.length(); j++) {


                        try {

                            String tDate = aptarray.getString(j);
                            Log.e("apt time", tDate);
                            String pattern = "yyyy-MM-dd";

                            DateFormat df = new SimpleDateFormat(pattern);
                            // SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            //Date date = df.parse(tDate);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(df.parse(tDate));
                            //calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                            // calendar.add(Calendar.MONTH,-1);

                            Log.e("aptyear", String.valueOf(calendar.get(Calendar.YEAR)));
                            Log.e("aptmonth", String.valueOf(calendar.get(Calendar.MONTH)));
                            Log.e("aptday", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                            Log.e("aptDate modi", String.valueOf(calendar.getTime()));

                            events.add(new EventDay(calendar, R.drawable.appointment));


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }


                    for (int k = 0; k < todoaptarray.length(); k++) {

                        String tDate = todoaptarray.getString(k);
                        String pattern = "yyyy-MM-dd";
                        DateFormat df = new SimpleDateFormat(pattern);
                        Date date = null;
                        try {
                            date = df.parse(tDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        //calendar.add(Calendar.MONTH,-1);
                        Log.e("bothyear", String.valueOf(calendar.get(Calendar.YEAR)));
                        Log.e("bothmonth", String.valueOf(calendar.get(Calendar.MONTH)));
                        Log.e("bothday", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                        Log.e("bothDate modi", String.valueOf(calendar.getTime()));
                        events.add(new EventDay(calendar, R.drawable.multi_event));


                    }

                    generateCalender();


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
                params.put("action", "getEvents");
                return params;
            }
        };

        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

/*
        Log.e("todolistsize", String.valueOf(todoAppointDatesList.size()));

        for (int i = 0; i < todoAppointDatesList.size(); i++) {

            String todoAptDates = todoAppointDatesList.get(i);

            String item = todoAptDates.substring(todoAptDates.lastIndexOf("&&"), todoAptDates.length());
            String dte = todoAptDates.substring(0, todoAptDates.lastIndexOf("&&"));

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            Date date = null;

            try {
                date = simpleDateFormat.parse(dte);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("Cut DAte", dte);

            Calendar calendar = Calendar.getInstance();
            calendar.set(date.getYear(), date.getMonth(), date.getDay());
            calendar.add(Calendar.MONTH, -1);
            Log.e("Date modi", String.valueOf(calendar.getTime()));


            events.add(new EventDay(calendar, R.drawable.multi_event));


        }*/


        generateCalender();


    }

    void loadJson() {


        //getting the workflow list
        getWorkFlowList(session_userId);

        getSlidId(session_userId);

        //getting all the todolist data
        getTodoList(session_userId);

        //getting all the appointment data
        getAppointmentList(session_userId);

        //getting all the events data
        getEventsDates(session_userId);

        //pie charts methods
        getUserReport(session_userId);
        getWorkflowStatus(session_userId);
        getWorkflowPriority(session_userId);

        getNotiCount("getNotificationCount", session_userId);

    }

    private void setupBadge(String count) {


        if (tvNotiBadgeCount != null) {

            if (Integer.parseInt(count) == 0) {
                if (tvNotiBadgeCount.getVisibility() != View.GONE) {
                    tvNotiBadgeCount.setVisibility(View.GONE);

                }
            } else {
                //tvNotiBadgeCount.setText(String.valueOf(Math.min(mNotiCount, 99)));
                tvNotiBadgeCount.setText(count);
                mNotiCount = Integer.parseInt(count);

                if (tvNotiBadgeCount.getVisibility() != View.VISIBLE) {
                    tvNotiBadgeCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    //check if the event list contain a specific date
    String checkContainsDate(EventDay eventDay, List<EventDay> events) {


        if (events.contains(eventDay)) {

            return "yes";

        } else {

            return "no";

        }


        //another logic
      /*  try {

            Calendar calendar = eventDay.getCalendar();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String selectedDate = format.format(calendar.getTime());
            Date evntDay = format.parse(selectedDate);
            Log.e("selected date 1", format.format(calendar.getTime()));

            ArrayList<Date> dates = new ArrayList<>();
            for (int i = 0; i < events.size(); i++) {

                String eventDate = format.format(events.get(i).getCalendar().getTime());
                Log.e("event date",eventDate);
                Date d = format.parse(eventDate);
                dates.add(d);

            }

            if (dates.contains(evntDay)) {

                Log.e("date present", "yes");

                return "yes";

            } else {

                Log.e("date present", "no");

                return "no";
            }


        } catch (Exception e) {


        }
*/


    }

    void getNotiCount(String action, String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        String count = jsonObject.getString("count");

                       /* if (count.equalsIgnoreCase("0")) {

                            toolbar.getMenu().findItem(R.id.action_noti_bell).setVisible(false);

                        } else {
                            toolbar.getMenu().findItem(R.id.action_noti_bell).setVisible(true);

                        }*/


                        setupBadge(count);


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


        VolleySingelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


    }

    String setRoles() {


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("role_id").append(",")
                .append("view_recycle_bin").append(",")
                .append("view_user_audit").append(",")
                .append("view_storage_audit").append(",")
                .append("share_with_me").append(",")
                .append("shared_file").append(",")
                .append("metadata_search").append(",")
                .append("metadata_quick_search").append(",")
                .append("save_query").append(",")
                .append("dashboard_edit_profile").append(",")
                .append("dashboard_mydms").append(",")
                .append("dashboard_mytask").append(",")
                .append("num_of_folder").append(",")
                .append("num_of_file").append(",")
                .append("memory_used").append(",")
                .append("workflow_audit").append(",")
                .append("workflow_task_track").append(",")
                .append("view_workflow_list").append(",")
                .append("status_wf").append(",")
                .append("priority_wf").append(",")
                .append("user_graph").append(",")
                .append("todo_view").append(",")
                .append("todo_add").append(",")
                .append("appoint_view").append(",")
                .append("appoint_add").append(",")
                .append("calendar_wf");


        return stringBuilder.toString();


    }


}


