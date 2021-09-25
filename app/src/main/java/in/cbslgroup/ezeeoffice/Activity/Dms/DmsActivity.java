package in.cbslgroup.ezeeoffice.Activity.Dms;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeeoffice.Adapters.FileViewAdapter;
import in.cbslgroup.ezeeoffice.Adapters.FileViewHorizontalAdapter;
import in.cbslgroup.ezeeoffice.Adapters.MetaViewBottomSheetAdapter;
import in.cbslgroup.ezeeoffice.Interface.CustomItemClickListener;
import in.cbslgroup.ezeeoffice.Model.FileViewList;
import in.cbslgroup.ezeeoffice.Model.Foldername;
import in.cbslgroup.ezeeoffice.Model.MetaBottomSheet;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.ConnectivityReceiver;
import in.cbslgroup.ezeeoffice.Utils.Initializer;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class DmsActivity
        extends AppCompatActivity implements View.OnLongClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    public static String dynamicFileSlid;
    public static String foldernameDyanamic;
    public static String slid_Session, userid;
    public static String roleid;

    public static String copy_storage;
    public static String move_storage;
    public static String ip;



    AlertDialog alertDialog, alertDialogSuccess;

    RecyclerView rvHierarcy, rvHorizontal, rvFileView;
    String rootStorage1;
    ProgressBar progressBar, progressBar2;
    SessionManager session;
    TextView tvTotalFolders, tvTotalFiles, tvTotalFileSize, tvHeading;
    LinearLayout llDmsRoot, llNoFileFound, llDmsNavLeft;
    CardView cvBtnMetadata;
    DrawerLayout drawer;
    View navView;
    ImageButton btnAddMetaForm, btnRemoveMetaForm;
    Button btnMetaSearch;
    List<String> spinnerConditionlist = new ArrayList<>();
    String data;
    EditText etRootMetadataText;
    String rootStorage;
    ArrayAdapter<String> dynamicSpinnerAdapter;
    FileViewHorizontalAdapter fileViewHorizontalAdapter;
    ArrayList<String> spinnerMetaList = new ArrayList<>();

    String rootSpinnerMetadata, rootSpinnerCondition, rootEditTextMetaDataText;
    CardView cvMetaFormStatic;
    TextView tvNoMetaData;
    JSONArray jsonArrayMetadata;
    int counterAddButton = 1;
    String dynamicFoldername;
    ArrayList<String> metadata = new ArrayList<>();
    ArrayList<String> condition = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();
    CardView cvNavLeftMetaFormDyanamic;
    View rowView;
    int count;
    ImageView ivMetaFolder;
    String session_userName = null, session_userEmail = null, session_designation = null, session_contact = null;
    RelativeLayout relativeLayout;
    LinearLayout llnointernetfound;
    ScrollView scrollViewmain;
    int counter = 0;
    boolean onlongclick = false;
    boolean rootfolder = false;
    Toolbar toolbar;
    LinearLayout llDrawer;
    NavigationView navigationView;
    ImageView ivCloseDrawer;
    private int lastPosition = -1;
    private List<Foldername> horizontalist = new ArrayList<>();
    private List<FileViewList> fileViewList = new ArrayList<>();
    private List<Foldername> foldernames = new ArrayList<>();
    private List<Foldername> onclickfoldernames = new ArrayList<>();
    private List<View> myLayouts = new ArrayList<>();
    private Spinner staticSpinnerMetaData, staticSpinnerCondition;
    private FileViewAdapter fileViewAdapter;

    List<MetaBottomSheet> metaDataViewBottomsheetList = new ArrayList<>();

    BottomSheetDialog bottomSheetDialog;
    RecyclerView rvBottomSheetMetaview;

   ArrayAdapter<String> conditionListAdapter;
   Intent intentMultiMeta;
   Boolean jumptoMetaSearch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dms);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // userid
        userid = user.get(SessionManager.KEY_USERID);
        session_userName = user.get(SessionManager.KEY_NAME);
        session_userEmail = user.get(SessionManager.KEY_EMAIL);
        session_designation = user.get(SessionManager.KEY_DESIGNATION);
        session_contact = user.get(SessionManager.KEY_CONTACT);

        //  Log.e("username",session_userName+" "+session_userEmail);

        tvTotalFiles = findViewById(R.id.tv_dms_heading_total_files);
        tvTotalFileSize = findViewById(R.id.tv_dms_heading_total_size);
        tvTotalFolders = findViewById(R.id.tv_dms_heading_total_folder);
        tvHeading = findViewById(R.id.tv_dms_heading);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        ivMetaFolder = findViewById(R.id.iv_meta_icon);
        ivMetaFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showBottomSheetDialog();

                 //assignedMetaData(dynamicFileSlid);



            }
        });

        relativeLayout = findViewById(R.id.rl_dms_main);
        llnointernetfound = findViewById(R.id.ll_dmsactivity_nointernetfound);
        scrollViewmain = findViewById(R.id.dms_scrollview_main);


        toolbar = findViewById(R.id.toolbar_storage_management);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        drawer = findViewById(R.id.dmsactivity_drawer_layout_left);
        navigationView = findViewById(R.id.dmsactivity_nav_view_left);
        navView = navigationView.getHeaderView(0);


        llDmsRoot = findViewById(R.id.ll_root_dmsActivity);
        llNoFileFound = findViewById(R.id.ll_dmsactivity_nofilefound);
        llDmsNavLeft = navView.findViewById(R.id.ll_dms_nav_left_drawer);
        btnAddMetaForm = navView.findViewById(R.id.iv_nav_left_add_btn);

        staticSpinnerMetaData = navView.findViewById(R.id.spinner_metadata_nav_left_dmsactvity);
        staticSpinnerCondition = navView.findViewById(R.id.spinner_condition_nav_left_dmsactvity);


        cvBtnMetadata = navView.findViewById(R.id.cv_nav_left_form_metadatasearch_button);
        cvMetaFormStatic = navView.findViewById(R.id.cv_dmsactivity_nav_left);
        cvNavLeftMetaFormDyanamic = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);

        ivCloseDrawer = navView.findViewById(R.id.iv_close_drawer_nav_drawer_dms);
        ivCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Load the animation like this
                Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                        android.R.anim.fade_out);

                // Start the animation like this
                navigationView.startAnimation(animSlide);

                navigationView.setVisibility(View.GONE);


            }
        });

        //ScrollView scrollViewnavleft = navView.findViewById(R.id.scrollview_dms_nav_left);

/*
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
        slid_Session = sharedPreferences.getString("userSlid", null);
*/


         if(getIntent().getStringExtra("slid")!=null)
         {
             String slid = getIntent().getStringExtra("slid");
             slid_Session = slid;
             //giving slid to adapter when file is clicked
             dynamicFileSlid = slid_Session;
         }

         else{

             slid_Session = MainActivity.slid_session;
             //giving slid to adapter when file is clicked
             dynamicFileSlid = slid_Session;

         }



        btnAddMetaForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (jsonArrayMetadata.length()+ 2 > counterAddButton) // jsonArrayMetadata.length()+ 2
                                                                      // (+2) is because there 2 default metadata
                {

                    addMetaForm();
                    counterAddButton++;


                    Log.e("Counteraddbutton", String.valueOf(counterAddButton));
                } else {

                    btnAddMetaForm.setVisibility(View.INVISIBLE);
                    Toast.makeText(DmsActivity.this, "No more metadata", Toast.LENGTH_LONG).show();
                }


            }
        });


        etRootMetadataText = navView.findViewById(R.id.et_static__text_search_nav_left_form);
        tvNoMetaData = navView.findViewById(R.id.tv_noMetaDataFound);

        btnMetaSearch = navView.findViewById(R.id.btn_metadatasearch_nav_left);
        btnMetaSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metadata.clear();
                condition.clear();
                text.clear();


                rootSpinnerMetadata = staticSpinnerMetaData.getSelectedItem().toString();
                rootSpinnerCondition = staticSpinnerCondition.getSelectedItem().toString();


                if (counterAddButton == 1) {

                    if (rootSpinnerMetadata.equals("Select Metadata")) {


                        ((TextView) staticSpinnerMetaData.getSelectedView()).setError("please select metadata");
                        (staticSpinnerMetaData.getSelectedView()).requestFocus();

                        Toast.makeText(DmsActivity.this, "Select Metadata", Toast.LENGTH_SHORT).show();


                    } else if (rootSpinnerCondition.equals("Select Condition")) {

                        ((TextView) staticSpinnerCondition.getSelectedView()).setError("please select condition");
                        (staticSpinnerCondition.getSelectedView()).requestFocus();

                        Toast.makeText(DmsActivity.this, "Select Condition", Toast.LENGTH_SHORT).show();


                    } else if (etRootMetadataText.getText().toString().equals("") || etRootMetadataText.getText().toString().isEmpty()) {


                        etRootMetadataText.setError("Please enter text for search");
                        etRootMetadataText.clearFocus();


                        Toast.makeText(DmsActivity.this, "Please enter text for search", Toast.LENGTH_SHORT).show();


                    } else {

                        ((TextView) staticSpinnerCondition.getSelectedView()).setError(null);
                        ((TextView) staticSpinnerMetaData.getSelectedView()).setError(null);
                        etRootMetadataText.setError(null);

                        rootSpinnerMetadata = staticSpinnerMetaData.getSelectedItem().toString();
                        rootSpinnerCondition = staticSpinnerCondition.getSelectedItem().toString();
                        rootEditTextMetaDataText = String.valueOf(etRootMetadataText.getText());

                        metadata.add(rootSpinnerMetadata);
                        condition.add(rootSpinnerCondition);
                        text.add(rootEditTextMetaDataText);

                 /*       metadata.add("ankit");
                        condition.add("Equal");
                        text.add("g");*/


                        count = metadata.size();
                        Log.e("metadata size", String.valueOf(count));


                        try {

                            JSONObject jsonObject = makeJsonObject(metadata, condition, text, count);

                            Log.e("jsonobject", String.valueOf(jsonObject));

                            Intent intent = new Intent(DmsActivity.this, MetaSearchFileViewActivity.class);
                            intent.putExtra("slid", dynamicFileSlid);
                            intent.putExtra("metadata", String.valueOf(jsonObject));
                            intent.putExtra("foldername", dynamicFoldername);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            // multiMetaSearch(String.valueOf(jsonObject),"329");

                            // Log.e("jsonobject", String.valueOf(jsonObject));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                } else if (counterAddButton > 1) {


                    EditText et = null;


                    if (rootSpinnerMetadata.equals("Select Metadata")) {


                        ((TextView) staticSpinnerMetaData.getSelectedView()).setError("please select metadata");
                        (staticSpinnerMetaData.getSelectedView()).requestFocus();
                         jumptoMetaSearch=false;
                        Toast.makeText(DmsActivity.this, "Select Metadata", Toast.LENGTH_SHORT).show();


                    } else if (rootSpinnerCondition.equals("Select Condition")) {

                        ((TextView) staticSpinnerCondition.getSelectedView()).setError("please select condition");
                        (staticSpinnerCondition.getSelectedView()).requestFocus();

                        Toast.makeText(DmsActivity.this, "Select Condition", Toast.LENGTH_SHORT).show();


                    } else if (etRootMetadataText.getText().toString().equals("") || etRootMetadataText.getText().toString().isEmpty()) {


                        etRootMetadataText.setError("Please enter text for search");
                        etRootMetadataText.clearFocus();


                        Toast.makeText(DmsActivity.this, "Please enter text for search", Toast.LENGTH_SHORT).show();


                    } else {

                        etRootMetadataText.setError(null);
                        rootEditTextMetaDataText = String.valueOf(etRootMetadataText.getText());

                        metadata.add(rootSpinnerMetadata);
                        condition.add(rootSpinnerCondition);
                        text.add(rootEditTextMetaDataText);


                        for (int i = 0; i < myLayouts.size(); i++) {

                            et = myLayouts.get(i).findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);

                            Spinner s1 = myLayouts.get(i).findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
                            Spinner s2 = myLayouts.get(i).findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);

                            String metadatatext = s1.getSelectedItem().toString();
                            String conditiontext = s2.getSelectedItem().toString();
                            String textTxt = null;


                            if (metadatatext.equals("Select Metadata")) {


                                ((TextView) s1.getSelectedView()).setError("please select metadata");
                                (s1.getSelectedView()).requestFocus();
                                jumptoMetaSearch=false;
                                Toast.makeText(DmsActivity.this, "Select Metadata", Toast.LENGTH_SHORT).show();


                            } else if (conditiontext.equals("Select Condition")) {

                                ((TextView) s2.getSelectedView()).setError("please select condition");
                                (s2.getSelectedView()).requestFocus();
                                jumptoMetaSearch=false;
                                Toast.makeText(DmsActivity.this, "Select Condition", Toast.LENGTH_SHORT).show();


                            } else if (et.getText().toString().equals("") || et.getText().toString().isEmpty()) {


                                et.setError("Please enter text for search");
                                et.clearFocus();

                                jumptoMetaSearch=false;
                                Toast.makeText(DmsActivity.this, "Please enter text for search", Toast.LENGTH_SHORT).show();


                            } else {

                                et.setError(null);
                                textTxt = String.valueOf(et.getText());

                                metadata.add(metadatatext);
                                condition.add(conditiontext);
                                text.add(textTxt);


                                count = metadata.size();
                                Log.e("metadata 2 size", String.valueOf(count));


                                try {

                                    JSONObject jsonObject = makeJsonObject(metadata, condition, text, count);

                                    Log.e("jsonobject", String.valueOf(jsonObject));

                                    intentMultiMeta = new Intent(DmsActivity.this, MetaSearchFileViewActivity.class);
                                    intentMultiMeta.putExtra("slid", dynamicFileSlid);
                                    intentMultiMeta.putExtra("metadata", String.valueOf(jsonObject));
                                    intentMultiMeta.putExtra("foldername", dynamicFoldername);

                                    jumptoMetaSearch=true;

                                    // multiMetaSearch(String.valueOf(jsonObject),"329");

                                    // Log.e("jsonobject", String.valueOf(jsonObject));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }

                        if(jumptoMetaSearch){

                            startActivity(intentMultiMeta);
                        }

                       /* count = metadata.size();
                        Log.e("metadata 2 size", String.valueOf(count));


                        try {

                            JSONObject jsonObject = makeJsonObject(metadata, condition, text, count);

                            Log.e("jsonobject", String.valueOf(jsonObject));

                            Intent intent = new Intent(DmsActivity.this, MetaSearchFileViewActivity.class);
                            intent.putExtra("slid", dynamicFileSlid);
                            intent.putExtra("metadata", String.valueOf(jsonObject));
                            intent.putExtra("foldername", dynamicFoldername);
                            startActivity(intent);

                            // multiMetaSearch(String.valueOf(jsonObject),"329");

                            // Log.e("jsonobject", String.valueOf(jsonObject));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/

                        // Toast.makeText(DmsActivity.this, textTxt + " " + metadatatext + " " + textTxt, Toast.LENGTH_LONG).show();

                    }

                }


            }


            // multiMetaSearch(rootSpinnerMetadata, rootSpinnerCondition, rootEditTextMetaDataText, "329");


            // Toast.makeText(DmsActivity.this, rootSpinnerMetadata + " " + rootSpinnerCondition + " " + rootEditTextMetaDataText, Toast.LENGTH_LONG).show();


        });


        spinnerConditionlist.add("Equal");
        spinnerConditionlist.add("Contains");
        spinnerConditionlist.add("Like");
        spinnerConditionlist.add("Not Like");
        spinnerConditionlist.add("Select Condition");



        if (staticSpinnerCondition != null) {

            conditionListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
                @Override
                public int getCount() {
                    // don't display last item. It is used as hint.
                    int count = super.getCount();
                    return count > 0 ? count - 1 : count;
                }
            };

            conditionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            staticSpinnerCondition.setAdapter(conditionListAdapter);

            staticSpinnerCondition.setSelection(conditionListAdapter.getCount());
            //conditionListAdapter.notifyDataSetChanged();


        } else {

            Log.e("staticSpinnerCondition", "null");
        }

        if (staticSpinnerMetaData != null) {

            dynamicSpinnerAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerMetaList) {
                @Override
                public int getCount() {
                    // don't display last item. It is used as hint.
                    int count = super.getCount();
                    return count > 0 ? count - 1 : count;
                }
            };

        } else {


            Log.e("staticSpinnerMetadata", "null");
        }


        //recyclerview Hierarcy (gallery view)
        rvHierarcy = findViewById(R.id.rv_storage_management);
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        // rvHierarcy.setLayoutManager(gridLayoutManager);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvHierarcy.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        } else {
            rvHierarcy.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        }
        //rvHierarcy.setHasFixedSize(true);

        //recyclerview horizontal list (listview horizontal)
        rvHorizontal = findViewById(R.id.rv_storage_management_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHorizontal.setLayoutManager(linearLayoutManager);
        rvHorizontal.setHasFixedSize(true);

        fileViewAdapter = new FileViewAdapter(DmsActivity.this, foldernames, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String slid, String fullFolderName) {

            }
        });


        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horizontalist, DmsActivity.this, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String slid, String fullFolderName) {

                counterAddButton = 1;
                onclickfoldernames.clear();
                foldernames.clear();
                //  llDmsRoot.removeAllViews();

                CardView cardView = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);
                if (cardView != null) {
                    llDmsNavLeft.removeView(cardView);

                } else {

                    Log.e("cardview", "null");
                }


                if (slid == slid_Session) {

                    horizontalist.clear();
                    String id = rootStorage + "&&" + slid_Session;
                    Foldername foldername1 = new Foldername();
                    foldername1.setFoldername(id);
                    horizontalist.add(foldername1);
                    fileViewHorizontalAdapter.notifyDataSetChanged();


                } else {

                    horizontalist.subList(position + 1, horizontalist.size()).clear();
                    fileViewHorizontalAdapter.notifyDataSetChanged();
                    // fileViewHorizontalAdapter.deleteItem(position+1);
                    recursiveGetFolder(slid);
                    getMetadata(slid);

                }

                setAnimation(v, position);

            }
        });

        rvHorizontal.setAdapter(fileViewHorizontalAdapter);

        if (isNetworkAvailable()) {

            llnointernetfound.setVisibility(View.GONE);

            //ipaddress
            ip = getDeviceIpAddress();

            //getting role_id

            Log.e("userid dmsact",userid);

           getUserRole(userid);


        } else {

            llnointernetfound.setVisibility(View.VISIBLE);
            scrollViewmain.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            Toast.makeText(DmsActivity.this, "No internet connection found ", Toast.LENGTH_LONG).show();

        }




        //userid


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dms_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_dms_grid_switch) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            supportInvalidateOptionsMenu();
            boolean isSwitched = fileViewAdapter.toggleItemViewType();
            rvHierarcy.setLayoutManager(isSwitched ? new LinearLayoutManager(DmsActivity.this) : new GridLayoutManager(DmsActivity.this, 3));
            rvHierarcy.setHasFixedSize(true);
       /*     if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                rvHierarcy.setLayoutManager(new GridLayoutManager(this, 3));
            }
            else{
                rvHierarcy.setLayoutManager(new GridLayoutManager(this, 5));
            }*/

            fileViewAdapter.notifyDataSetChanged();

            return true;
        }

        if (id == R.id.action_dms_list_switch) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            supportInvalidateOptionsMenu();
            boolean isSwitched = fileViewAdapter.toggleItemViewType();
            rvHierarcy.setLayoutManager(isSwitched ? new LinearLayoutManager(this) : new GridLayoutManager(this, 3));
            fileViewAdapter.notifyDataSetChanged();


            return true;
        }

        if (id == R.id.action_edit_profile) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();


            return true;
        }

        if (id == R.id.action_create_new_child) {

            createNewChildPopup();

            return true;
        }

        if (id == R.id.action_modify_storage) {

            modifyStorageNamePopup();

            return true;
        }

        if (id == R.id.action_move_storage) {


            String foldernameNslid = String.valueOf(horizontalist.get(horizontalist.size() - 1).getFoldername());

            Intent intent = new Intent(DmsActivity.this, MoveStorageActivity.class);
            intent.putExtra("foldername&&slid", foldernameNslid);
            startActivity(intent);

            Log.e("slid", foldernameNslid);

            return true;
        }

        if (id == R.id.action_upload_documents) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(DmsActivity.this, UploadActivity.class);
            startActivityForResult(intent,102);


            return true;
        }

        if (id == R.id.action_dms_search) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            // Load the animation like this
            Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                    android.R.anim.fade_in);

            // Start the animation like this
            navigationView.startAnimation(animSlide);


            navigationView.setVisibility(View.VISIBLE);



         /*   if (drawer.isDrawerOpen(GravityCompat.END)) {

                drawer.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.END);
            } else {

                drawer.setVisibility(View.GONE);
                drawer.openDrawer(GravityCompat.END);

            }*/


            return true;
        }


        if (id == R.id.action_assign_metadata) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(DmsActivity.this, AssignMetadataActivity.class);
            intent.putExtra("fSlid", horizontalist.get(horizontalist.size() - 1).getFoldername());
            startActivityForResult(intent,103);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFolder(final String slid, final String userid) {


        //resetMultiDialog();

        //clearing the metasearch spinner list and textfield
       // spinnerConditionlist.clear();
       // etRootMetadataText.setText("");


        rvHierarcy.setVisibility(View.GONE);
        //progressBar2.setVisibility(View.VISIBLE);
        llNoFileFound.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("folderlist", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);


                    rootStorage = jsonObject.getString("storagename");
                    tvHeading.setText(rootStorage);
                    foldernameDyanamic = rootStorage;

                    String id = rootStorage + "&&" + slid_Session;

                    Foldername foldername1 = new Foldername();
                    foldername1.setFoldername(id);
                    horizontalist.add(foldername1);


                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.e("getresponse in loop ", jsonArray.getString(i));
                        String jsondataloop = jsonArray.getString(i);
                        Foldername foldername = new Foldername();
                        foldername.setFoldername(jsondataloop);
                        foldernames.add(foldername);

                    }

                    String totalfolders = jsonObject.getString("totalfolder");
                    String totalfile = jsonObject.getString("totalfile");
                    String totalsize = jsonObject.getString("size");
                    String currentstoragefiles = jsonObject.getString("currentstoragefile");

                    final Foldername foldername = new Foldername();
                    foldername.setFoldername(currentstoragefiles);
                    foldernames.add(foldername);

                    tvTotalFileSize.setText(totalsize);
                    tvTotalFiles.setText(totalfile);
                    tvTotalFolders.setText(totalfolders);

                    progressBar.setVisibility(View.GONE);
                    llDmsRoot.setVisibility(View.VISIBLE);



                    if (totalfolders.equals("0") && totalfile.equals("0") && currentstoragefiles.equals("0")) {

                        progressBar2.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                        rvHierarcy.setVisibility(View.GONE);

                    } else {

                        progressBar2.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.GONE);
                        rvHierarcy.setVisibility(View.VISIBLE);

                    }


                    rvHierarcy.setAdapter(new FileViewAdapter(DmsActivity.this, foldernames, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, final String slid, String fullFolderName) {

                            //  llDmsRoot.removeAllViews();
                            counterAddButton = 1;
                            CardView cardView = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);
                            if (cardView != null) {
                                llDmsNavLeft.removeView(cardView);
                            } else {
                                Log.e("cardview", "null");
                            }


                            recursiveGetFolder(slid);
                            getMetadata(slid);

                            Foldername foldername = new Foldername();
                            foldername.setFoldername(fullFolderName);
                            horizontalist.add(foldername);

                            String folderName = fullFolderName.substring(0, fullFolderName.indexOf("&&"));
                            tvHeading.setText(folderName);
                            foldernameDyanamic = folderName;

                        }
                    }));

                    rvHierarcy.setItemViewCacheSize(foldernames.size());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // getFolder(slid, userid);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameter = new HashMap<String, String>();

                Log.e("Map userid slid", userid + " " + slid);
                parameter.put("userid", userid);
                parameter.put("slid", slid);//root slid


                return parameter;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);
        VolleySingelton.getInstance(DmsActivity.this).getRequestQueue().getCache().remove(ApiUrl.STORAGE_URL);

    }

    private void recursiveGetFolder(final String slid) {

        dynamicFileSlid = slid;

        onclickfoldernames.clear();

        rvHierarcy.setVisibility(View.GONE);
        progressBar2.setVisibility(View.VISIBLE);
        llNoFileFound.setVisibility(View.GONE);
        //progressBar2.setVisibility(View.VISIBLE);

        //clearing the multimeta form
        //spinnerConditionlist.clear();
       // etRootMetadataText.setText("");

        resetMultiDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    Log.e("recursive folder",response);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");
                    rootStorage1 = jsonObject.getString("storagename");
                    tvHeading.setText(rootStorage1);
                    foldernameDyanamic = rootStorage1;

                    //horizontalist.remove(horizontalist.size()-1);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        String jsondataloop = jsonArray.getString(i);
                        Foldername foldername = new Foldername();
                        foldername.setFoldername(jsondataloop);
                        onclickfoldernames.add(foldername);

                    }

                    String totalfolders = jsonObject.getString("totalfolder");
                    String totalfile = jsonObject.getString("totalfile");
                    String totalsize = jsonObject.getString("size");
                    String currentstoragefiles = jsonObject.getString("currentstoragefile");

                    //   Log.e("totalfiles", totalfile);

                    if (totalfile.equals("0")) {

                        Log.e("total files found", "0");

                    } else {

                        Foldername foldername = new Foldername();
                        foldername.setFoldername(currentstoragefiles);
                        onclickfoldernames.add(foldername);

                    }

                    if (totalfolders.equals("0") && totalfile.equals("0") && currentstoragefiles.equals("0")) {

                        progressBar2.setVisibility(View.GONE);

                        llNoFileFound.setVisibility(View.VISIBLE);
                        rvHierarcy.setVisibility(View.GONE);

                    } else {

                        progressBar2.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.GONE);
                        rvHierarcy.setVisibility(View.VISIBLE);

                    }


                    tvTotalFileSize.setText(totalsize);
                    tvTotalFiles.setText(totalfile);
                    tvTotalFolders.setText(totalfolders);

                    //progressBar2.setVisibility(View.GONE);


                } catch (JSONException e) {

                    e.printStackTrace();


                }


                //code here
                FileViewAdapter fileViewAdapter = new FileViewAdapter(DmsActivity.this, onclickfoldernames, new CustomItemClickListener() {

                    @Override
                    public void onItemClick(View v, int position, String slid, String fullFolderName) {

                        counterAddButton = 1;
                        CardView cardView = navView.findViewById(R.id.cv_dmsactivity_nav_left_with_cancel);
                        if (cardView != null) {
                            llDmsNavLeft.removeView(cardView);
                        } else {
                            Log.e("cardview", "null");
                        }


                        onclickfoldernames.clear();

                        if (slid == slid_Session) {

                            horizontalist.clear();
                            String id = rootStorage1 + "&&" + slid_Session;

                            Foldername foldername1 = new Foldername();
                            foldername1.setFoldername(id);
                            horizontalist.add(foldername1);

                            fileViewHorizontalAdapter.notifyDataSetChanged();
                            //fileViewHorizontalAdapter.addItem(position,foldername1);

                           // fileViewHorizontalAdapter.notifyDataSetChanged();

                        } else {

                            recursiveGetFolder(slid);
                            getMetadata(slid);


                            Foldername foldername = new Foldername();
                            foldername.setFoldername(fullFolderName);
                            horizontalist.add(foldername);
                            //fileViewHorizontalAdapter.addItem(position,foldername);

                            //fileViewHorizontalAdapter.notifyDataSetChanged();

                            //fileViewHorizontalAdapter.addItem(position,foldername);


                        }
                    }
                });

                rvHierarcy.setAdapter(fileViewAdapter);
                rvHierarcy.setItemViewCacheSize(onclickfoldernames.size());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


               recursiveGetFolder(slid);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("slid", slid);


                JSONObject jsonObject = new JSONObject(params);
                String json = jsonObject.toString();

                Log.e("recursive json",json);

                return params;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);
        VolleySingelton.getInstance(DmsActivity.this).getRequestQueue().getCache().remove(ApiUrl.STORAGE_URL);
    }

    private void addMetaForm() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
        rowView = inflater.inflate(R.layout.dms_nav_left_meta_form, null);

        myLayouts.add(rowView);

        Spinner spinnerdynamicCondition = rowView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdynamicCondition.setAdapter(adapter);
        spinnerdynamicCondition.setSelection(adapter.getCount());


        Spinner spinnerdynamicMetadata = rowView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerMetaList) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdynamicMetadata.setAdapter(adapter1);
        spinnerdynamicMetadata.setSelection(adapter1.getCount());


        btnRemoveMetaForm = rowView.findViewById(R.id.iv_nav_left_del_btn);
        btnRemoveMetaForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Animation animation = AnimationUtils.loadAnimation(DmsActivity.this, android.R.anim.slide_out_right);
                animation.setDuration(500);
                rowView.startAnimation(animation);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (counterAddButton == 0) {

                            Log.e("counter addbutton zero", "true");
                            handler.removeCallbacksAndMessages(null);
                        } else {




                            counterAddButton -= 1;
                            llDmsNavLeft.removeViewAt(llDmsNavLeft.getChildCount() - 2);
                            myLayouts.remove(myLayouts.size() - 1);

                            //scrollViewnavleft.removeView(rowView);

                            Log.e("counteraddButton", String.valueOf(counterAddButton));

                            Animation animation = AnimationUtils.loadAnimation(DmsActivity.this, android.R.anim.slide_in_left);
                            animation.setDuration(500);
                            cvBtnMetadata.startAnimation(animation);
                            handler.removeCallbacksAndMessages(null);


                            if (jsonArrayMetadata.length()+ 2 > counterAddButton) // jsonArrayMetadata.length()+ 2
                            // (+2) is because there 2 default metadata
                            {

                                btnAddMetaForm.setVisibility(View.VISIBLE);

                            } else {

                                btnAddMetaForm.setVisibility(View.INVISIBLE);

                            }

                        }


                    }
                }, 500);


            }
        });

        Animation animation = AnimationUtils.loadAnimation(DmsActivity.this, android.R.anim.slide_in_left);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                llDmsNavLeft.addView(rowView, llDmsNavLeft.getChildCount() - 1);


            }
        }, 500);


    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (isNetworkAvailable()) {

            if (horizontalist.size() == 1) {

                Intent intent = new Intent(DmsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {
                String slidPrevious;

                if (horizontalist.size() == 0) {

                    //  Toast.makeText(DmsActivity.this,"Network error",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(DmsActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {

                    slidPrevious = horizontalist.get(horizontalist.size() - 2).getFoldername();// 0 index slid
                    String slid = slidPrevious.substring(slidPrevious.indexOf("&&") + 2);
                    //  Log.e("Slid previous",slid);
                    fileViewList.clear();
                    onclickfoldernames.clear();

                    if (slid.isEmpty() || slidPrevious.isEmpty() || slidPrevious.equals("")) {

                        Toast.makeText(DmsActivity.this, "Network error", Toast.LENGTH_LONG).show();

                    } else {

                        recursiveGetFolder(slid);
                        getMetadata(slid);
                        horizontalist.remove(horizontalist.size() - 1);
                        fileViewHorizontalAdapter.notifyDataSetChanged();

                    }
                }


            }
        } else {

            Intent intent = new Intent(DmsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


    }

    //getting the list of metadata in dropdown of each indiviual folder dynamically
    private void getMetadata(final String slid) {

        spinnerMetaList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("metadata", response);
                try {

                    jsonArrayMetadata = new JSONArray(response);

                    if (jsonArrayMetadata.length() == 0 || jsonArrayMetadata == null) {

                        tvNoMetaData.setVisibility(View.VISIBLE);
                        cvMetaFormStatic.setVisibility(View.GONE);
                        cvBtnMetadata.setVisibility(View.GONE);
                        ivMetaFolder.setVisibility(View.INVISIBLE);

                        if (rowView != null) {

                            if (rowView.getVisibility() == View.VISIBLE) {

                                LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
                                linearLayout.removeView(rowView);

                            }

                        } else {

                            Log.e("dynamicmetaForm", "null");

                        }


                    } else {


                        cvMetaFormStatic.setVisibility(View.VISIBLE);
                        tvNoMetaData.setVisibility(View.GONE);
                        cvBtnMetadata.setVisibility(View.VISIBLE);
                        ivMetaFolder.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArrayMetadata.length(); i++) {

                            String array = jsonArrayMetadata.get(i).toString();
                            spinnerMetaList.add(array);
                            dynamicSpinnerAdapter.notifyDataSetChanged();

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinnerMetaList.add("FileName");
                spinnerMetaList.add("No Of Pages");
                spinnerMetaList.add("Select Metadata");
                dynamicSpinnerAdapter.notifyDataSetChanged();
                dynamicSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                staticSpinnerMetaData.setAdapter(dynamicSpinnerAdapter);
                staticSpinnerMetaData.setSelection(dynamicSpinnerAdapter.getCount());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getMetadata(slid);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("slid", slid);


                return params;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);
    }

/*
    void multiMetaSearch(final String metadata, final String slid) {


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_MULTI_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("multimetadata", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("metadata", metadata);
                params.put("slid", slid);

                return params;

            }


        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==101){

            if(resultCode == RESULT_OK){

                String slid = data.getStringExtra("slid_dms");
                //Toast.makeText(this, slid, Toast.LENGTH_SHORT).show();
                recursiveGetFolder(slid);

            }

        }

        if(requestCode==102){

            if(resultCode == RESULT_OK){

                String slid = data.getStringExtra("slid_dms");
                //Toast.makeText(this, slid, Toast.LENGTH_SHORT).show();
                recursiveGetFolder(slid);

            }

        }

        if(requestCode==103){

            if(resultCode == RESULT_OK){

                String slid = data.getStringExtra("slid_dms");
                //Toast.makeText(this, slid, Toast.LENGTH_SHORT).show();
                recursiveGetFolder(slid);
                getMetadata(slid);


            }

        }



    }

    //Create new Child method
    private void createNewChildPopup() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);

        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_new_child, null);
        TextView tvFname = dialogView.findViewById(R.id.tv_create_new_child_docname);
        tvFname.setText(foldernameDyanamic);

        final EditText etName = dialogView.findViewById(R.id.et_child_name);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_create_new_child_cancel);
        Button btn_createChild = dialogView.findViewById(R.id.btn_create_new_child_add);

        btn_createChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String childName = etName.getText().toString();

                if (childName.isEmpty() || childName == null) {

                    etName.setError("Enter Sub Folder Name");
                } else {

                    etName.setError(null);
                    addNewChild(dynamicFileSlid, childName);
                    alertDialog.dismiss();

                }
                //alertDialog.dismiss();

            }
        });

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }


    private JSONObject makeJsonObject(ArrayList<String> metaData, ArrayList<String> condition, ArrayList<String> text,
                                      int formcount)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();

        for (int i = 0; i < formcount; i++) {
            obj = new JSONObject();
            try {
                obj.put("metadata", metaData.get(i));
                obj.put("cond", condition.get(i));
                obj.put("searchText", text.get(i));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("multiMetaSearch", jsonArray1);
        return finalobject;
    }

    private void addNewChild(final String parentSlid, final String foldername) {


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CREATE_NEW_CHILD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create new child", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                        tv_error_heading.setText("Success");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialogSuccess.dismiss();
                                alertDialog.dismiss();
                                recursiveGetFolder(dynamicFileSlid);


                            }
                        });

                        dialogBuilder.setView(dialogView);
                        alertDialogSuccess = dialogBuilder.create();
                        alertDialogSuccess.setCancelable(false);
                        alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialogSuccess.show();


                    }

                    if (error.equals("true")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText("Error");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


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

                Map<String, String> params = new HashMap<String, String>();
                params.put("add_child", parentSlid);
                params.put("create_child", foldername);
                params.put("name", session_userName);
                params.put("ip", getDeviceIpAddress());
                params.put("userid", userid);


                return params;

            }


        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


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

    private void modifyStorage(final String parentSlid, final String foldername) {


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MODIFY_STORAGE_NAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("modify Storage", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                        tv_error_heading.setText("Success");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialogSuccess.dismiss();
                                alertDialog.dismiss();

                               /* horizontalist.remove(horizontalist.size()-1);
                                fileViewHorizontalAdapter.notifyDataSetChanged();*/

                                fileViewHorizontalAdapter.deleteItem(horizontalist.size()-1);

                                Foldername foldernme = new Foldername();
                                foldernme.setFoldername(foldername+"&&"+dynamicFileSlid);


                                horizontalist.add(foldernme);

                                recursiveGetFolder(dynamicFileSlid);


                            }
                        });

                        dialogBuilder.setView(dialogView);
                        alertDialogSuccess = dialogBuilder.create();
                        alertDialogSuccess.setCancelable(false);
                        alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialogSuccess.show();


                    }

                    if (error.equals("true")) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
                        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText("Error");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(message);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


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

                Map<String, String> params = new HashMap<String, String>();
                params.put("modi", parentSlid);
                params.put("modify_slname", foldername);
                params.put("name", session_userName);
                params.put("ip", getDeviceIpAddress());
                params.put("userid", userid);


                return params;

            }


        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }


    //Change stoarge name method
    private void modifyStorageNamePopup() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DmsActivity.this);
        LayoutInflater inflater = DmsActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.modify_storage_popup, null);

        final EditText etName = dialogView.findViewById(R.id.et_modify_storage_name);
        etName.setText(foldernameDyanamic);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_modify_storage_cancel);
        Button btn_save_changes = dialogView.findViewById(R.id.btn_modify_storage_savechanges);

        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String storageName = etName.getText().toString();

                if (storageName.isEmpty() || storageName == null) {

                    etName.setError("Enter Storage Name");
                } else {

                    etName.setError(null);
                    modifyStorage(dynamicFileSlid, storageName);
                    alertDialog.dismiss();

                }


            }
        });

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showNoInternetLayout(isConnected);


    }

    void showNoInternetLayout(Boolean isconnected) {


        if (!isconnected) {

            llnointernetfound.setVisibility(View.VISIBLE);
            scrollViewmain.setVisibility(View.GONE);


        } else {

            llnointernetfound.setVisibility(View.GONE);
            scrollViewmain.setVisibility(View.VISIBLE);


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

    void getUserRole(final String userid) {


        progressBar.setVisibility(View.VISIBLE);
        llDmsRoot.setVisibility(View.GONE);
        foldernames.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    //String userrole = jsonObject.getString("user_role");
                    String userroleid = jsonObject.getString("role_id");
                    String assign_metadata = jsonObject.getString("assign_metadata");
                    String create_storage = jsonObject.getString("create_storage");

                    String move_storage_level = jsonObject.getString("move_storage_level");
                    String copy_storage_level = jsonObject.getString("copy_storage_level");

                    String upload_doc_storage = jsonObject.getString("upload_doc_storage");
                    String metadata_quick_search = jsonObject.getString("metadata_quick_search");


                    if(metadata_quick_search.equalsIgnoreCase("1")){

                        toolbar.getMenu().findItem(R.id.action_dms_search).setVisible(true);
                    }

                    else{

                        toolbar.getMenu().findItem(R.id.action_dms_search).setVisible(false);

                    }



                    if(move_storage_level.equalsIgnoreCase("0")){

                        move_storage = "0";

                    }
                    else{

                        move_storage = "1";
                    }

                    if(copy_storage_level.equalsIgnoreCase("0")){

                        copy_storage = "0";

                    }
                    else{

                        copy_storage = "1";
                    }


                    if(upload_doc_storage.equalsIgnoreCase("0")){

                        toolbar.getMenu().findItem(R.id.action_upload_documents).setVisible(false);
                    }

                    else{

                        toolbar.getMenu().findItem(R.id.action_upload_documents).setVisible(true);
                    }




                    if(assign_metadata.equalsIgnoreCase("0")){

                        toolbar.getMenu().findItem(R.id.action_assign_metadata).setVisible(false);
                    }

                    else{

                        toolbar.getMenu().findItem(R.id.action_assign_metadata).setVisible(true);
                    }

                    if(create_storage.equalsIgnoreCase("0")){

                        toolbar.getMenu().findItem(R.id.action_create_new_child).setVisible(false);
                    }

                    else{

                        toolbar.getMenu().findItem(R.id.action_create_new_child).setVisible(true);
                    }


                    roleid = userroleid;

                    //getfoldernames and attached slid
                    getFolder(slid_Session, userid);
                    //getting metadata
                    getMetadata(slid_Session);


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

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");

                return params;
            }
        };

        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }





  /*  // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {


        } else {
            message = "No Internet Connection Found";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(relativeLayout, message, 500);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }*/

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onLongClick(View v) {

  /*      toolbar.setBackgroundColor(getResources().getColor(R.color.black_blur));
        toolbar.getMenu().clear();
        toolbar.setTitle("0 folder selected");
        toolbar.inflateMenu(R.menu.dms_multiselect_menu);

        Log.e("inactionmode","before");

        in_action_mode_dms = true;




         fileViewAdapter.notifyDataSetChanged();
          rvHierarcy.invalidate();

        Log.e("inactionmode","after");*/

        return true;
    }


    void assignedMetaData(final String slid){

        metaDataViewBottomsheetList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ASSIGN_METADATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("assignmetadata",response);


                try {
                    JSONArray jsonArray  = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){

                        String metaname = jsonArray.getJSONObject(i).getString("field_name");
                        String metaid = jsonArray.getJSONObject(i).getString("id");

                        metaDataViewBottomsheetList.add(new MetaBottomSheet(metaname));


                    }

                    if(rvBottomSheetMetaview!=null){

                        rvBottomSheetMetaview.setAdapter(new MetaViewBottomSheetAdapter(metaDataViewBottomsheetList,DmsActivity.this));
                        bottomSheetDialog.show();

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
                params.put("slid",slid);


                return params;
            }
        };


        VolleySingelton.getInstance(DmsActivity.this).addToRequestQueue(stringRequest);


    }


    public void showBottomSheetDialog() {


        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_assignedmetadata, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

       rvBottomSheetMetaview= bottomSheetDialog.findViewById(R.id.bottom_sheet_rv_metaview);
       rvBottomSheetMetaview.setLayoutManager(new LinearLayoutManager(this));
       rvBottomSheetMetaview.setHasFixedSize(true);
       rvBottomSheetMetaview.setItemViewCacheSize(metaDataViewBottomsheetList.size());

       assignedMetaData(dynamicFileSlid);


    }


    void resetMultiDialog(){

        spinnerConditionlist.clear();

        spinnerConditionlist.add("Equal");
        spinnerConditionlist.add("Contains");
        spinnerConditionlist.add("Like");
        spinnerConditionlist.add("Not Like");
        spinnerConditionlist.add("Select Condition");


        if (staticSpinnerCondition != null) {

            conditionListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
                @Override
                public int getCount() {
                    // don't display last item. It is used as hint.
                    int count = super.getCount();
                    return count > 0 ? count - 1 : count;
                }
            };

            conditionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            staticSpinnerCondition.setAdapter(conditionListAdapter);

            staticSpinnerCondition.setSelection(conditionListAdapter.getCount());
            //conditionListAdapter.notifyDataSetChanged();


        } else {

            Log.e("staticSpinnerCondition", "null");
        }

        // conditionListAdapter.notifyDataSetChanged();


        etRootMetadataText.setText("");

    }

    String setRoles(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("role_id").append(",")
                .append("assign_metadata").append(",")
                .append("create_storage").append(",")
                .append("move_storage_level").append(",")
                .append("copy_storage_level").append(",")
                .append("upload_doc_storage").append(",")
                .append("metadata_quick_search");


        return stringBuilder.toString();
    }



}
