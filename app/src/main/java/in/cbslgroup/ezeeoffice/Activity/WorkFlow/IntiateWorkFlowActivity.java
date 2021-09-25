package in.cbslgroup.ezeeoffice.Activity.WorkFlow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lowagie.text.pdf.PdfReader;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.cbslgroup.ezeeoffice.Activity.Dms.MoveStorageActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.UploadActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.CashVocherAddMoreAdapter;
import in.cbslgroup.ezeeoffice.Adapters.FileViewHorizontalAdapter;
import in.cbslgroup.ezeeoffice.Adapters.MoveStorageListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.MultipleFileChooseAdapter;
import in.cbslgroup.ezeeoffice.Adapters.StorageAllotedAdapter;
import in.cbslgroup.ezeeoffice.Interface.CustomItemClickListener;
import in.cbslgroup.ezeeoffice.Interface.MoveStorageListListener;
import in.cbslgroup.ezeeoffice.Model.CashVocherAddMore;
import in.cbslgroup.ezeeoffice.Model.Description;
import in.cbslgroup.ezeeoffice.Model.Divison;
import in.cbslgroup.ezeeoffice.Model.Foldername;
import in.cbslgroup.ezeeoffice.Model.Location;
import in.cbslgroup.ezeeoffice.Model.MoveStorage;
import in.cbslgroup.ezeeoffice.Model.MultipleSelect;
import in.cbslgroup.ezeeoffice.Model.Project;
import in.cbslgroup.ezeeoffice.Model.StorageAlloted;
import in.cbslgroup.ezeeoffice.Notifications.FirebaseMessagingService;
import in.cbslgroup.ezeeoffice.Notifications.IntiateWorkflowService;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.DigitToRuppee;
import in.cbslgroup.ezeeoffice.Utils.NotificationActions;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

public class IntiateWorkFlowActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final String TAG = "photo";
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    Boolean allFill = false;
    Spinner spForm;
    View llCashAddM;
    Toolbar toolbar;
    TextView tvStorageName, tvSlid;
    WebView webView;
    String form = "";
    ProgressDialog progressDialog;
    //Dyanmic layout attributes
    LinearLayout llMain;
    List<String> spinnerData = new ArrayList<>();
    int responseListSize = 0;
    ArrayAdapter<String> divisionAdapter, projectAdapter, locationAdapter;
    EditText etGetWorkFlow;
    Button btnGetWorkFlow, btnPreview, btnReset;
    int count = 1;
    int spinnerCount = 0;
    int textCount = 0;
    int edittextCount = 0;
    int dateCount = 0;
    String filePath;
    Map<String, ArrayList<String>> spList = new HashMap<>();
    List<LinearLayout> linearLayoutsList = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList<String> formvalues = new ArrayList<>();
    String workflowId, workflowName;
    AlertDialog alertDialogPreview, alertDialogSuccess, alertDialogProcessing, alertDialogError;
    BottomSheetDialog bottomSheetDialog, bottomSheetSelectStorageDialog;
    String frmVal;
    StringBuilder strFormValues = new StringBuilder();
    ProgressBar pbMain;
    RelativeLayout rlMain;
    ImageView ivAttachSelected;
    ImageButton ibAttachment;
    Intent intentChooseFile;
    ArrayList<MultipleSelect> doclist = new ArrayList<>();
    RecyclerView rvMultiSelect;
    Button btnBrowseFiles, btnClearDocList;
    LinearLayout llnofileselected;
    LinearLayout llBrowseFile;
    LinearLayout llselectStorage, llnodirectoryfound;
    CheckBox checkBoxAttachDoc;
    MultipleFileChooseAdapter adapter;
    MultipartUploadRequest multipartUploadRequest;
    List<MoveStorage> moveStorageList = new ArrayList<>();
    ArrayList<Foldername> horilist = new ArrayList<>();
    FileViewHorizontalAdapter fileViewHorizontalAdapter;
    MoveStorageListAdapter moveStorageListAdapter;
    RecyclerView rvMain, rvHori;
    ProgressBar progressBar;
    String slidStr;
    JSONArray jsonArray;
    String storagename;
    ImageView ivStorageName;
    Button btnSelectStorage;
    //cash voucher
    ArrayList<Divison> divisonList = new ArrayList<>();
    ArrayList<Location> locationList = new ArrayList<>();
    ArrayList<Project> projectList = new ArrayList<>();
    RecyclerView rvAddMore;
    List<CashVocherAddMore> addMoreList = new ArrayList<>();
    CashVocherAddMoreAdapter cashVocherAddMoreAdapter;
    ImageButton ivAddMore;
    LinearLayout llCashVoucherMain;
    List<String> projList = new ArrayList<>();
    int tagCount = 0;
    String cashVocherNo;
    List<String> divList = new ArrayList<>();
    List<String> projlist = new ArrayList<>();
    ArrayList<View> addMoreViewList = new ArrayList<>();
    LinearLayout llCashAddMore;
    int addMoreCounter = 0;
    Spinner spModeOfConvey;
    Boolean isCashVoucher = false;
    List<Spinner> spinnerList = new ArrayList<>();
    Map<String, String> spinnerIdList = new HashMap<>();
    Map<String, String> spinnerSelectedItemList = new HashMap<>();
    Map<String, String> CashVoucherAllValues = new LinkedHashMap<>();
    List<String> locList = new ArrayList<>();
    EditText etRupeesStatic, etPaisaStatic, etTotalRupess, etTotalRupessWords, etPurpose, etDescription;
    int TotalRupees = 0;
    List<Description> descriptionList = new ArrayList<>();
    List<String> finalCashVoucherValues = new ArrayList<>();
    ArrayList<String> conveyanceList = new ArrayList<>();
    String ip;
    DatePickerDialog dpd;
    String fromDate, toDate;
    //Firebase Token id
    String tokenid;
    BroadcastReceiver broadcastReceiverClosePB;
    private String mCurrentPhotoPath;

    AlertDialog alertDialog;
    List<StorageAlloted> storageAllotedList = new ArrayList<>();


 /*   @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverClosePB);
    }*/

    //counting the lines of the given string
    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intiate_work_flow);

        toolbar = findViewById(R.id.toolbar_intiateworkflow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        broadcastReceiverClosePB = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                if ((getPackageName() + ".CLOSE_INITIATE_PB").equalsIgnoreCase(arg1.getAction())) {

                    //Toast.makeText(arg0, "Yes it is working", Toast.LENGTH_LONG).show();

                    String msg = arg1.getStringExtra("msg");
                    String error = arg1.getStringExtra("error");


                    //alertProcessing("off", arg0);


                    if (error.equalsIgnoreCase("false")) {

                        //alertProcessing("off", arg0);

                        alertDialogProcessing.dismiss();
                        alertDialogProcessing.cancel();

                        alertSuccess(msg, arg0);

                    } else if (error.equalsIgnoreCase("true")) {

                        //alertProcessing("off", arg0);

                        alertDialogProcessing.dismiss();
                        alertDialogProcessing.cancel();

                        alertError(msg, arg0);

                    }

                    // alertDialogProcessing.dismiss();

                }


            }
        };

        registerReceiver(broadcastReceiverClosePB, new IntentFilter(getPackageName() + ".CLOSE_INITIATE_PB"));
        requestPermission();

        ip = getDeviceIpAddress();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntiateWorkFlowActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(IntiateWorkFlowActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.e("FCM_TOKEN_INTIATE", token);

                tokenid = token;

            }
        });


        llMain = findViewById(R.id.ll_intiateworkflow_main);
        llCashVoucherMain = findViewById(R.id.ll_cash_voucher_main);


        pbMain = findViewById(R.id.pb_intiateWorkflow_main);

        rlMain = findViewById(R.id.rl_intiateWorkflow_main);
        spModeOfConvey = findViewById(R.id.sp_mode_of_conveyance);


        conveyanceList.add("Auto");
        conveyanceList.add("Car");
        conveyanceList.add("Cab");
        conveyanceList.add("Metro");
        conveyanceList.add("Select Mode Of Conveyance");


        ArrayAdapter<String> conveyanceAdapter = new ArrayAdapter<String>(IntiateWorkFlowActivity.this, android.R.layout.simple_spinner_item, conveyanceList) {


            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }


        };


        conveyanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModeOfConvey.setAdapter(conveyanceAdapter);
        spModeOfConvey.setSelection(conveyanceAdapter.getCount());


        rvMultiSelect = findViewById(R.id.rv_intiatefile_multiple_choose);
        rvMultiSelect.setItemViewCacheSize(doclist.size());
        rvMultiSelect.setLayoutManager(new LinearLayoutManager(this));
        rvMultiSelect.hasFixedSize();

        adapter = new MultipleFileChooseAdapter(doclist, IntiateWorkFlowActivity.this);
        rvMultiSelect.setAdapter(adapter);


        etGetWorkFlow = findViewById(R.id.et_get_workflow);
        btnGetWorkFlow = findViewById(R.id.btn_get_workflow);
        btnReset = findViewById(R.id.btn_intiate_workflow_reset);
        btnPreview = findViewById(R.id.btn_intiate_workflow_preview);
       /* ivAttachSelected = findViewById(R.id.iv_attachement_selected);
        ibAttachment = findViewById(R.id.ib_intiate_workflow_attach_doc);*/
        llBrowseFile = findViewById(R.id.ll_intiateworkflow_browsefiles);
        llCashAddMore = findViewById(R.id.ll_add_more_rupee_paisa);
        etPurpose = findViewById(R.id.et_intiate_workflow_purpose);
        etDescription = findViewById(R.id.et_intiate_workflow_description);


        etRupeesStatic = findViewById(R.id.et_intiateworkflow_rupee);
        etTotalRupess = findViewById(R.id.et_intial_workflow_total_rupee);
        etTotalRupessWords = findViewById(R.id.et_intial_workflow_total_rupee_words);

        Log.e("Integer max value", String.valueOf(Integer.MAX_VALUE));


        etRupeesStatic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.toString().equalsIgnoreCase("") || s.toString().length() == 0 || TextUtils.isEmpty(s.toString())) {


                    if (addMoreViewList.size() > 0) {
                        //means there more then 1 addmore

                        for (int i = 0; i < addMoreViewList.size(); i++) {

                            EditText etRuppeDyna = addMoreViewList.get(i).findViewById(R.id.et_intiateworkflow_rupee_dynamic);

                            if (etRuppeDyna.getText().length() == 0) {

                                etTotalRupess.getText().clear();
                                etTotalRupessWords.getText().clear();


                            }

                            EditText etPaisaDyna = addMoreViewList.get(i).findViewById(R.id.et_intiateworkflow_rupee_dynamic);

                            if (etPaisaDyna.getText().length() == 0) {

                                etTotalRupess.getText().clear();
                                etTotalRupessWords.getText().clear();


                            }

                        }


                    } else if (addMoreViewList.size() == 0) {
                        //no addmore

                        etTotalRupess.getText().clear();
                        etTotalRupessWords.getText().clear();
                        etPaisaStatic.getText().clear();


                    }


                    // etTotalRupess.setText(String.valueOf(getTotalAmount()));
                    // etTotalRupessWords.setText(DigitToRuppee.convert(getTotalAmount()));

                } else {

                    etTotalRupess.setText(String.valueOf(getTotalAmount()));
                    TotalRupees = Integer.parseInt(s.toString());
                    etTotalRupessWords.setText(DigitToRuppee.convert(getTotalAmount()));

                }


                //end here


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPaisaStatic = findViewById(R.id.et_intiateworkflow_paisa);
        etPaisaStatic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvStorageName = findViewById(R.id.tv_initiate_workflow_select_storage);
        ivStorageName = findViewById(R.id.iv_initiate_workflow_select_storage);
        ivStorageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    getSlidId(MainActivity.userid);

                }

        });


        tvStorageName = findViewById(R.id.tv_initiate_workflow_select_storage);
        tvSlid = findViewById(R.id.tv_initiate_workflow_select_storage_slid);


        checkBoxAttachDoc = findViewById(R.id.checkbox_intitiate_workflow_attach_Doc);
        checkBoxAttachDoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    llBrowseFile.setVisibility(View.VISIBLE);

                } else {

                    llBrowseFile.setVisibility(View.GONE);

                }

            }
        });

        btnClearDocList = findViewById(R.id.btn_intiate_workflow_clear_list);
        llnofileselected = findViewById(R.id.ll_intiateworkflow_nofileselected);

        btnClearDocList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                doclist.clear();
                rvMultiSelect.setVisibility(View.GONE);
                llnofileselected.setVisibility(View.VISIBLE);
                btnClearDocList.setVisibility(View.GONE);
                btnClearDocList.setEnabled(false);
                btnClearDocList.setBackgroundColor(getResources().getColor(R.color.red_fade));
            }
        });
        btnBrowseFiles = findViewById(R.id.btn_intiate_workflow_browsefiles);
        btnBrowseFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnClearDocList.setEnabled(true);
                // btnClearDocList.setVisibility(View.VISIBLE);
                showBottomSheetDialog();


            }
        });

        /*ibAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showBottomSheetDialog();


            }
        });
*/

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stringBuilder.setLength(0);


                if (isCashVoucher) {

                    String modeofconvey = spModeOfConvey.getSelectedItem().toString();
                    String purpose = etPurpose.getText().toString();


                    if (modeofconvey.equalsIgnoreCase("Select mode of conveyance")) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Please select mode of conveyance", Toast.LENGTH_SHORT).show();


                    } else if (purpose.length() == 0) {

                        etPurpose.setError("Please enter purpose");
                        Toast.makeText(IntiateWorkFlowActivity.this, "Please enter purpose", Toast.LENGTH_SHORT).show();

                    } else if (etDescription.getText().length() == 0) {


                        etDescription.setError("Plese Enter Description");
                        Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();

                    } else if (etRupeesStatic.getText().length() == 0) {

                        etRupeesStatic.setError("Please Enter Rupees");
                        Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Rupees", Toast.LENGTH_SHORT).show();

                    } else if (etPaisaStatic.getText().length() == 0) {

                        etPaisaStatic.setError("Please Enter Paisa");
                        Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Paisa", Toast.LENGTH_SHORT).show();

                    } else if (doclist.size() == 0 && checkBoxAttachDoc.isChecked()) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Attach document for uploading", Toast.LENGTH_SHORT).show();
                    } else if (tvSlid.getText().toString().length() == 0 && checkBoxAttachDoc.isChecked()) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Please Select Storage", Toast.LENGTH_SHORT).show();

                    } else {

                        getCashVoucherValue();

                    }

                } else {


                    if (doclist.size() == 0 && checkBoxAttachDoc.isChecked()) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Attach document for uploading", Toast.LENGTH_SHORT).show();
                    } else if (tvSlid.getText().toString().length() == 0 && checkBoxAttachDoc.isChecked()) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Please Select Storage", Toast.LENGTH_SHORT).show();

                    } else {

                        getFormValues();
                    }


                }


            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reset();

                // getRecycleValues();

                //  alertProcessing("on",IntiateWorkFlowActivity.this);

            }
        });

        btnGetWorkFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getWorkFlowFormAttributes(etGetWorkFlow.getText().toString().trim());


            }
        });

        Intent intent = getIntent();
        workflowId = intent.getStringExtra("workflowID");
        workflowName = intent.getStringExtra("workflowName");

        toolbar.setSubtitle(workflowName);

        Log.e("workflowid", workflowId);

        getWorkFlowFormAttributes(workflowId);

        /* arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerData);*//*{
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        }*/


        ivAddMore = findViewById(R.id.ib_intiateworkflow_add_more);

        ivAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etDescription.getText().length() == 0) {


                    etDescription.setError("Plese Enter Description");
                    Toast.makeText(IntiateWorkFlowActivity.this, "Plese Enter Description", Toast.LENGTH_SHORT).show();

                } else if (etRupeesStatic.getText().length() == 0) {

                    etRupeesStatic.setError("Please Enter Rupees");
                    Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Rupees", Toast.LENGTH_SHORT).show();

                } else if (etPaisaStatic.getText().length() == 0) {

                    etPaisaStatic.setError("Please Enter Paisa");
                    Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Paisa", Toast.LENGTH_SHORT).show();

                } else if (addMoreViewList.size() > 0) {

                    for (int i = 0; i < addMoreViewList.size(); i++) {

                        View view = addMoreViewList.get(i);
                        CardView cv = (CardView) view;
                        EditText etDesc = cv.findViewById(R.id.et_intiateworkflow_desc_dynamic);
                        EditText etPaisa = cv.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
                        EditText etRupee = cv.findViewById(R.id.et_intiateworkflow_rupee_dynamic);


                        if (etDesc.getText().length() == 0) {
                            allFill = false;
                            etDesc.setError("Plese Enter Description");
                            Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();

                        } else if (etRupee.getText().length() == 0) {

                            allFill = false;
                            etRupee.setError("Plese Enter Rupees");
                            Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Rupees", Toast.LENGTH_SHORT).show();

                        } else if (etPaisa.getText().length() == 0) {
                            allFill = false;
                            etPaisa.setError("Plese Enter Paisa");
                            Toast.makeText(IntiateWorkFlowActivity.this, "Please Enter Paisa", Toast.LENGTH_SHORT).show();

                        } else {

                            allFill = true;

                        }


                    }

                    if (allFill) {

                        addMoreRupeePaisa();

                    }


                } else {


                    addMoreRupeePaisa();

                }


            }
        });

       /* rvAddMore = findViewById(R.id.rv_add_more);
        rvAddMore.hasFixedSize();
        rvAddMore.setLayoutManager(new LinearLayoutManager(this));
        rvAddMore.setItemViewCacheSize(addMoreList.size());*/

      /* cashVocherAddMoreAdapter =  new CashVocherAddMoreAdapter(addMoreList, this, new AddMoreCashVoucherListener() {
            @Override
            public void onRemoveClick(View v, int position) {

                addMoreList.remove(position);
                cashVocherAddMoreAdapter.notifyDataSetChanged();


            }
        });
        rvAddMore.setAdapter(cashVocherAddMoreAdapter);*/

        // cashVoucherNo(MainActivity.userid);


    }

    void getWorkFlowFormAttributes(final String workflowid) {

        llMain.removeAllViews();
        //spinnerData.clear();
        stringBuilder.setLength(0);
        pbMain.setVisibility(View.VISIBLE);
        rlMain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_BUILDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("workflow intiate", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    responseListSize = jsonArray.length();

                    for (int i = 0; i < jsonArray.length(); i++) {


                        String type = jsonArray.getJSONObject(i).getString("type");
                        Log.e("----Starting", "----");
                        Log.e("type", type);
                        String label = jsonArray.getJSONObject(i).getString("label");
                        Log.e("label", label);
                        String maxlength = jsonArray.getJSONObject(i).getString("maxlength");
                        Log.e("maxlength", maxlength);
                        String aid = jsonArray.getJSONObject(i).getString("aid");
                        Log.e("aid", aid);
                        String fid = jsonArray.getJSONObject(i).getString("fid");
                        Log.e("fid", fid);
                        String required = jsonArray.getJSONObject(i).getString("required");
                        Log.e("required", required);
                        String value = jsonArray.getJSONObject(i).getString("value");
                        Log.e("value", value);
                        //String dependencyId = jsonArray.getJSONObject(i).getString("dependency_id");
                        String placeholder = jsonArray.getJSONObject(i).getString("placeholder");
                        Log.e("placeholder", placeholder);
                        String classtype = jsonArray.getJSONObject(i).getString("class");
                        Log.e("classtype", classtype);

                        if (classtype.equalsIgnoreCase("form-control ccenter")) {


                            isCashVoucher = true;
                            llCashVoucherMain.setVisibility(View.GONE);


                        }

                        if (type.equalsIgnoreCase("select")) {

                            //spinnerData.clear();

                            Log.e("working", "fine");

                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("option").getJSONArray(0);


                            if (jsonArray.length() == 0) {

                                Log.e("jsonArray1", String.valueOf(jsonArray1.length()));

                            } else {

                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    String opt = jsonArray1.getJSONObject(j).getString("value");
                                    Log.e("opt", opt);


                                    spinnerData.add(opt);
                                }


                                spList.put("Spinner" + count, (ArrayList<String>) spinnerData);
                                count++;


                            }


                        }

                        Log.e("----End", "----");

                        // spinnerlist.add((ArrayList<String>) spinnerData);

                        JSONArray jsonArraySpData = jsonArray.getJSONObject(i).getJSONArray("option");

                        if (jsonArraySpData.length() == 0) {

                            createForm(type, label, maxlength, aid, fid, required, value, placeholder, classtype, new JSONArray());


                        } else {

                            createForm(type, label, maxlength, aid, fid, required, value, placeholder, classtype, jsonArraySpData.getJSONArray(0));

                        }


                        Log.e("----End2", "----");


                        //arrayAdapter.notifyDataSetChanged();

                        // spinnerData.clear();

                        if (!isCashVoucher) {

                            pbMain.setVisibility(View.GONE);
                        } else {
                            pbMain.setVisibility(View.VISIBLE);

                        }
                        rlMain.setVisibility(View.VISIBLE);


                    }


             /*       for (Map.Entry<String, ArrayList<String>> ee : spList.entrySet()) {
                        String key = ee.getKey() + "\n";
                        List<String> values = ee.getValue();
                        Log.e("key", key);
                        for (String a :
                                values) {

                            Log.e("value", a);

                        }


                    }*/
                    //Collections.rotate(spinnerData, -1);


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
                params.put("wId", workflowid);

                JSONObject js = new JSONObject(params);
                Log.e("workflow Intiate", js.toString());

                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);

    }

    @SuppressLint("ClickableViewAccessibility")
    void createForm(String type, String label, String maxlength, String aid, String fid, String required, String value, String placeholder, String classtype, JSONArray jsonArray) {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout llSpinner;
        LinearLayout llDate;
        LinearLayout llEditext;
        LinearLayout llHeader;
        LinearLayout llText;


        if (type.equalsIgnoreCase("date")) {


            llDate = (LinearLayout) inflater.inflate(R.layout.date_formbuilder, null);

            linearLayoutsList.add(llDate);


            TextView tvHeading = llDate.findViewById(R.id.tv_heading_date_formbuilder);
            tvHeading.setText(label);

            Log.e("Heading Date", tvHeading.getText().toString().trim());

            final EditText etDate = llDate.findViewById(R.id.et_heading_date_formbuilder);
            etDate.setHint(placeholder);
            etDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Calendar c = Calendar.getInstance();
                    //Toast.makeText(InTrayActivity.this, "working", Toast.LENGTH_SHORT).show();
                    dpd = new DatePickerDialog(IntiateWorkFlowActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @SuppressLint("SetTextI18n")
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

                                    //arg2 = 9+1 =10
                                    if (Integer.parseInt(month) >= 10) {

                                        month = String.valueOf(month);

                                    } else {

                                        month = "0" + (month);
                                    }


                                    etDate.setText(new StringBuilder().append(day).append("-")
                                            .append(month).append("-").append(year));

                                         /*   int month = monthOfYear+1;

                                            if(month+1>=10){ //11 12


                                            }

                                            else { //9 8 7

                                                month = Integer.parseInt("0"+month);


                                            }

                                            etDate.setText(dayOfMonth + "-"
                                                    + (month) + "-" + year);*/


                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));


                    if (tvHeading.getText().toString().equalsIgnoreCase("From Date")) {

                        Log.e("ok", "Indate");

                        etDate.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                fromDate = s.toString();
                                Log.e("fromDate", fromDate);

                            }
                        });


                    } else if (tvHeading.getText().toString().equalsIgnoreCase("To Date")) {

                        Date date = null;
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        try {

                            date = format.parse(fromDate);
                            // System.out.println(date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //from date and not cashvoucher
                        if (!isCashVoucher) {

                            Calendar cFromDate = Calendar.getInstance();
                            cFromDate.setTime(date);

                            dpd.getDatePicker().setMinDate(cFromDate.getTimeInMillis());
                            Log.e("c.getTimeInMillis()", String.valueOf(cFromDate.getTimeInMillis()));
                        }

                    }


                    dpd.show();


                }
            });




           /* etDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (etDate.getRight() - etDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {



                            return true;
                        }
                    }


                    return false;
                }
            });*/

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);
            llMain.addView(llDate, layoutParams);


        } else if (type.equalsIgnoreCase("select")) {

            llSpinner = (LinearLayout) inflater.inflate(R.layout.spinner_formbuilder, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);

            linearLayoutsList.add(llSpinner);


            TextView tvHeading = llSpinner.findViewById(R.id.tv_heading_spinner_formbuilder);
            tvHeading.setText(label);

            String Heading = tvHeading.getText().toString();
            spForm = llSpinner.findViewById(R.id.spinner_formbuilder);

            //Heading.equalsIgnoreCase("Division")&&

            if (classtype.equalsIgnoreCase("form-control ccenter")) {

                //spForm = llSpinner.findViewById(R.id.spinner_formbuilder);
                spForm.setTag("spFormDivison");

                spinnerList.add(spForm);

         /*      locationList.clear();
               projectList.clear();
               divisonList.clear();
               projlist.clear();
               divList.clear();*/

                clearAllList();


                getDivison(MainActivity.userid, spForm, llSpinner);

                //Heading.equalsIgnoreCase("Project")&&

            } else if (classtype.equalsIgnoreCase("form-control whouse")) {


                Log.e("projlist size", String.valueOf(projectList.size()));

                spinnerList.add(spForm);
                // spForm = llSpinner.findViewById(R.id.spinner_formbuilder);
                spForm.setTag("spFormProject");
                spForm.setId(R.id.spinnerProject);


            }

            //Heading.equalsIgnoreCase("Location")&&
            else if (classtype.equalsIgnoreCase("form-control locations")) {
                Log.e("Location list size", String.valueOf(locationList.size()));
                spinnerList.add(spForm);
                // spForm = llSpinner.findViewById(R.id.spinner_formbuilder);
                spForm.setTag("spFormLocation");
                spForm.setId(R.id.spinnerLocation);

                // getLocation("12",llSpinner);

            } else {

                // spForm = llSpinner.findViewById(R.id.spinner_formbuilder);

                try {

                    if (jsonArray.length() != 0) {


                        spinnerList.add(spForm);
                        ArrayList<String> purposeList = new ArrayList<>();

                        //purposeList.clear();

                        Log.e("jsonarray in spinner", String.valueOf(jsonArray.length()));

                        for (int i = 0; i < jsonArray.length(); i++) {


                            try {

                                purposeList.add(jsonArray.getJSONObject(i).getString("label"));
                                Log.e("i++", String.valueOf(i) + " " + jsonArray.getJSONObject(i).getString("label"));

                            } catch (Exception e) {

                                Log.e("Array exp", e.getMessage());

                            }


                        }

                        // purposeList.add("Select Leave Type");

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(IntiateWorkFlowActivity.this, android.R.layout.simple_spinner_item, purposeList);/* {


                           @Override
                           public int getCount() {
                               // don't display last item. It is used as hint.
                               int count = super.getCount();
                               return count > 0 ? count - 1 : count;
                           }


                       };*/


                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spForm.setAdapter(arrayAdapter);
                        //spForm.setSelection(arrayAdapter.getCount());


                    }


                } catch (Exception e) {

                    Log.e("spiner exp", e.getMessage());

                }



            /*    purposeList.add("Annual Leave");
                purposeList.add("Casual Leave");
                purposeList.add("Compensatory off(CO)");
                purposeList.add("OD");
                purposeList.add("Short Leave");*/


                // spinnerData.clear();


            }


            //spForm.setSelection(arrayAdapter.getCount());
            llMain.addView(llSpinner, layoutParams);


        } else if (type.equalsIgnoreCase("textarea")) {

            llEditext = (LinearLayout) inflater.inflate(R.layout.edittext_formbuilder, null);

            linearLayoutsList.add(llEditext);

            EditText editText = llEditext.findViewById(R.id.et_edittext_formbuilder);
            editText.setSelection(0);

            TextView textView = llEditext.findViewById(R.id.tv_heading_edittext_formbuilder);
            textView.setText(label);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);
            llMain.addView(llEditext, layoutParams);

        } else if (type.equalsIgnoreCase("header")) {

            llHeader = (LinearLayout) inflater.inflate(R.layout.header_formbuilder, null);

            TextView textView = llHeader.findViewById(R.id.tv_formbuilder);
            textView.setText(label);


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);
            llMain.addView(llHeader, layoutParams);

        } else if (type.equalsIgnoreCase("text")) {

            llText = (LinearLayout) inflater.inflate(R.layout.text_formbuilder, null);

            linearLayoutsList.add(llText);
            TextView textView = llText.findViewById(R.id.tv_text_formbuilder);
            textView.setText(label);


            EditText editText = llText.findViewById(R.id.et_text_formbuilder);
            editText.setSelection(0);


            if (label.equalsIgnoreCase("No.")) {


                cashVoucherNo(MainActivity.userid, editText);


            } else if (label.equalsIgnoreCase("Name")) {


                editText.setText(MainActivity.username);


            } else if (label.equalsIgnoreCase("Designation")) {


                editText.setText(MainActivity.designation);


            } else {

                editText.setHint(placeholder);
            }


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);
            llMain.addView(llText, layoutParams);

        }


    }

    void getFormValues() {


        Log.e("linearlayoutsize", String.valueOf(linearLayoutsList.size()));
        stringBuilder.setLength(0);


        for (int i = 0; i < linearLayoutsList.size(); i++) {


            LinearLayout linearLayout = linearLayoutsList.get(i);

            Log.e("linearlayoutsize", String.valueOf(linearLayoutsList.size()));


            //spinner
            if (linearLayout.findViewById(R.id.ll_spinner_formbuilder) != null) {


                Log.e("llspinner", "exist");


                LinearLayout layout = linearLayout.findViewById(R.id.ll_spinner_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_heading_spinner_formbuilder);
                String header = tvHeader.getText().toString();

                Log.e("header spinner", header);


                Spinner spinner = linearLayout.findViewById(R.id.spinner_formbuilder);

                String spText = "";

                if (spinner != null) {

                    spText = spinner.getSelectedItem().toString();
                } else {


                    spText = "null" + " " + header + " " + i;
                }

                stringBuilder.append(header).append(" : ").append(spText).append("\n").append("\n");

                Log.e("spinner_header" + spinnerCount, header);
                Log.e("spinnerText" + spinnerCount, spText);

                formvalues.add(spText);

                spinnerCount++;

            }/* else {

                Log.e("lleditext", "dont exist");

            }*/

            //date
            else if (linearLayout.findViewById(R.id.ll_date_formbuilder) != null) {


                Log.e("lldate", "exist");


                LinearLayout layout = linearLayout.findViewById(R.id.ll_date_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_heading_date_formbuilder);
                String header = tvHeader.getText().toString();

                EditText editText = linearLayout.findViewById(R.id.et_heading_date_formbuilder);
                String etText = editText.getText().toString();


                stringBuilder.append(header).append(" : ").append(etText).append("\n").append("\n");

                Log.e("Date_header" + dateCount, header);
                Log.e("Date" + dateCount, etText);

                formvalues.add(etText);

                dateCount++;

            }/* else {

                Log.e("lldate", "dont exist");

            }
*/
            //text
            else if (linearLayout.findViewById(R.id.ll_text_formbuilder) != null) {


                Log.e("llText", "exist");


                LinearLayout layout = linearLayout.findViewById(R.id.ll_text_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_text_formbuilder);
                String header = tvHeader.getText().toString();

                EditText editText = linearLayout.findViewById(R.id.et_text_formbuilder);
                String etText = editText.getText().toString();

                stringBuilder.append(header).append(" : ").append(etText).append("\n").append("\n");

                Log.e("Text_header" + textCount, header);
                Log.e("Text" + textCount, etText);

                formvalues.add(etText);

                textCount++;

            } /*else {

                Log.e("llText", "dont exist");

            }*/


            //editext
            else if (linearLayout.findViewById(R.id.ll_edittext_formbuilder) != null) {


                Log.e("llEdText", "exist");


                LinearLayout layout = linearLayout.findViewById(R.id.ll_edittext_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_heading_edittext_formbuilder);
                String header = tvHeader.getText().toString();

                EditText editText = linearLayout.findViewById(R.id.et_edittext_formbuilder);
                String etText = editText.getText().toString();


                StringBuilder textToHtml = new StringBuilder();

                for (int j = 0; j < countLines(etText); j++) {

                    String html = etText.split("\n")[j];

                    if (html.trim().isEmpty()) {

                        textToHtml.append("<p>&nbsp;</p>");
                        //System.out.println("<p>&nbsp;</p>");
                    } else {
                        textToHtml.append("<p>" + html.replaceAll(" ", "&nbsp;") + "</p>");
                        // System.out.println("<p>"+html.replaceAll(" ","&nbsp;")+"</p>");

                    }


                }


                stringBuilder.append(header).append(" : ").append(etText).append("\n").append("\n");

                Log.e("Text_header" + edittextCount, header);
                Log.e("Text" + edittextCount, etText);

                formvalues.add(textToHtml.toString());
                edittextCount++;


            } /*else {

                Log.e("llEdText", "dont exist");

            }*/


        }

        //making a array of field data
        strFormValues.setLength(0);


        for (int i = 0; i < formvalues.size(); i++) {

            strFormValues.append(formvalues.get(i)).append("^");

        }


        frmVal = strFormValues.toString();

        if (frmVal.endsWith("^")) {
            frmVal = frmVal.substring(0, frmVal.length() - 1);
        }

        Log.e("formvalues", frmVal);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(IntiateWorkFlowActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_workflow_preview, null);

        TextView tvPreview = dialogView.findViewById(R.id.tv_preview_workflow);
        tvPreview.setText(stringBuilder);


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_workflow_close);
        btn_cancel_ok.setOnClickListener(v -> {

            stringBuilder.setLength(0);
            alertDialogPreview.dismiss();


        });

        Button btn_submit_ok = dialogView.findViewById(R.id.btn_workflow_submit);
        btn_submit_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stringBuilder.setLength(0);

               byte[] data = workflowId.getBytes(StandardCharsets.UTF_8);

                String widBase64 = Base64.encodeToString(data, Base64.DEFAULT);

                Log.e("widBase64", widBase64);

                if (checkBoxAttachDoc.isChecked()) {

                    if (doclist.size() == 0) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Please select file for uploading", Toast.LENGTH_SHORT).show();

                    } else {


                        if (tvSlid.getText().toString().trim().length() != 0) {

                            String lastmoveid = tvSlid.getText().toString();
                            Log.e("lastmoveid", lastmoveid);

                            try {


                                Intent intWorkWithUpload = new Intent(IntiateWorkFlowActivity.this, IntiateWorkflowService.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("method", "with_upload");
                                bundle.putString("wid", widBase64);
                                bundle.putString("userid", MainActivity.userid);
                                bundle.putString("lastmoveid", lastmoveid);
                                bundle.putString("fielValues", frmVal);
                                bundle.putString("username", MainActivity.username);
                                bundle.putString("pagecount", String.valueOf(0));
                                bundle.putString("wfName", toolbar.getSubtitle().toString());
                                bundle.putString("tokenid", FirebaseMessagingService.getToken(IntiateWorkFlowActivity.this));
                                bundle.putParcelableArrayList("doclist", doclist);
                                intWorkWithUpload.putExtras(bundle);
                                startService(intWorkWithUpload);

                                //i use this method
                                // intiateWorkflowWithUpload(widBase64, MainActivity.userid, lastmoveid, frmVal, String.valueOf(0), MainActivity.username, FirebaseMessagingService.getToken(IntiateWorkFlowActivity.this));


                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }


                        } else {

                            Toast.makeText(IntiateWorkFlowActivity.this, "Please select storage", Toast.LENGTH_SHORT).show();

                        }


                    }


                } else {

                    Log.e("formvalues", frmVal);


                    Intent intWorkWithoutUpload = new Intent(IntiateWorkFlowActivity.this, IntiateWorkflowService.class);
                    intWorkWithoutUpload.putExtra("method", "without_upload");
                    intWorkWithoutUpload.putExtra("wid", widBase64);
                    intWorkWithoutUpload.putExtra("userid", MainActivity.userid);
                    intWorkWithoutUpload.putExtra("lastmoveid", "");
                    intWorkWithoutUpload.putExtra("fielValues", frmVal);
                    intWorkWithoutUpload.putExtra("username", MainActivity.username);
                    intWorkWithoutUpload.putExtra("wfName", toolbar.getSubtitle());
                    intWorkWithoutUpload.putExtra("tokenid", FirebaseMessagingService.getToken(IntiateWorkFlowActivity.this));
                    startService(intWorkWithoutUpload);


                    //intiateWorkflowWithoutUpload(widBase64, MainActivity.userid, "", frmVal, MainActivity.username,FirebaseMessagingService.getToken(IntiateWorkFlowActivity.this));


                }


                alertDialogPreview.dismiss();

                alertProcessing("On", IntiateWorkFlowActivity.this);


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialogPreview = dialogBuilder.create();
        alertDialogPreview.setCancelable(false);
        alertDialogPreview.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogPreview.show();


    }

    public void getSlidId(String userid) {

        storageAllotedList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dashboard response", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jobj = jsonObject.getJSONObject("dashboard_data");

                    JSONArray jsonArray = jobj.getJSONArray("storagealloted");

                    if (jsonArray.length() == 0) {

                        onBackPressed();
                        Toast.makeText(IntiateWorkFlowActivity.this, "No Storage Alloted", Toast.LENGTH_SHORT).show();

                    }
                    else if(jsonArray.length()>1) {



                        for (int i = 0; i < jsonArray.length(); i++) {

                            String storagename = jsonArray.getJSONObject(i).getString("storage_name");
                            String storageid = jsonArray.getJSONObject(i).getString("storage_id");

                            storageAllotedList.add(new StorageAlloted(storagename, storageid));

                        }

                        showAlertDilogSelectStoragePopup(storageAllotedList);//alertdialog

                    }

                    else{


                       selectStoragePopUp(MainActivity.slid_session);//bottomsheet


                    }




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


        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);


    }

    void intiateWorkflowWithoutUpload(final String workflowdid, final String userid, final String lastmoveid, final String fielValues, final String username, final String tokenid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.INITIATE_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                /*
                 * {"wid":"MQ==\n",
                 * "tokenid":"dkiaVGq38vc:APA91bFRalK6zqxs4bU4iwHt33W8-v5iytbTVQw2NSg_dPStVApF0p3bDPuGM4IjOnddq0o6NxflHcQHJagjYluZ3wfIjRAxxzsl-x2myZq0-zBo3TtDAHfY8LBacU_dnZ9-LNcxkSlz",
                 * "apikey":"T4b\/yAemFKeEdvC3MPKHU0RjQ+bWac\/8nCKY4NlOMRR44w0tP3Tfuv7Kb+Dv5emm",
                 * "ip":"100.118.53.211",
                 * "fieldValues":"gg",
                 * "userid":"5",
                 * "lastMoveId":"",
                 * "username":"Ankit Roy"}
                 */


                Log.e("response intiate", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");


                    if (error.equalsIgnoreCase("true")) {

                        alertProcessing("off", IntiateWorkFlowActivity.this);

                        alertError(msg, IntiateWorkFlowActivity.this);

                    } else if (error.equalsIgnoreCase("false")) {


                        alertProcessing("off", IntiateWorkFlowActivity.this);

                        alertSuccess(msg, IntiateWorkFlowActivity.this);


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

                //String tokenid = FirebaseMessagingService.getToken(IntiateWorkFlowActivity.this);

                params.put("wid", workflowdid);
                params.put("userid", userid);
                params.put("lastMoveId", lastmoveid);
                params.put("fieldValues", fielValues);
                params.put("username", username);
                params.put("ip", ip);
                params.put("tokenid", tokenid);


                JSONObject js = new JSONObject(params);
                Log.e(TAG, "getParams:" + js.toString());

                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);

    }

    //Alert dialog succcess

    void intiateWorkflowWithUpload(final String workflowdid, final String userid, final String lastmoveid, final String fielValues, final String pageCount, final String username, final String tokenid) throws Throwable {


        final String uploadId = UUID.randomUUID().toString();

        StringBuilder pathString = new StringBuilder();
        for (int i = 0; i < doclist.size(); i++) {

            pathString.append(doclist.get(i).getFilePath()).append(",");

        }


        String path = pathString.toString();

        if (pathString.toString().endsWith(",")) {

            path = path.substring(0, path.length() - 1);

        }

        String pathArray[] = new String[]{path};

        Log.e("patharray", Arrays.toString(pathArray));


        try {


            multipartUploadRequest = new MultipartUploadRequest(IntiateWorkFlowActivity.this, uploadId, ApiUrl.INITIATE_WORKFLOW);


            UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
            multipartUploadRequest.setNotificationConfig(

                    uploadNotificationConfig
                            .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                            .setClearOnActionForAllStatuses(true));

            for (int j = 0; j < doclist.size(); j++) {


                Log.e("path", doclist.get(j).getFilePath());
                multipartUploadRequest.addFileToUpload(doclist.get(j).getFilePath(), "fileName[]");
            }


            List<String> pathList = new ArrayList<>(Arrays.asList(pathArray));


            //multipartUploadRequest.addArrayParameter("file",pathList);


            Log.e("Upload_rate", UPLOAD_RATE);

            //uploadNotificationConfig.getProgress().message = PROGRESS;
            //uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

            uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(IntiateWorkFlowActivity.this, 1, uploadId)));


            uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
            // uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;


            uploadNotificationConfig.getError().message = "Error while uploading";
            uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
            //uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


            uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
            //uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;

           /* params.put("wid", workflowdid);
            params.put("userid", userid);
            params.put("lastMoveId", lastmoveid);
            params.put("fieldValues", fielValues);*/

            multipartUploadRequest.addParameter("wid", workflowdid);
            multipartUploadRequest.addParameter("userid", userid);
            multipartUploadRequest.addParameter("lastMoveId", lastmoveid);
            multipartUploadRequest.addParameter("fieldValues", fielValues);
            multipartUploadRequest.addParameter("pageCount", pageCount);
            multipartUploadRequest.addParameter("username", username);
            multipartUploadRequest.addParameter("ip", ip);
            multipartUploadRequest.addParameter("tokenid", tokenid);


            multipartUploadRequest.setMaxRetries(5);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {

                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                    Toast.makeText(IntiateWorkFlowActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                    Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));
                    Log.e("Server response error", String.valueOf(exception));
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                    Log.e("Server Response", serverResponse.getBodyAsString());

                    Log.e("ser uploading", serverResponse.getBodyAsString());


                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                        Log.e("uplaoding", "1");

                        String message = jsonObject.getString("msg");
                        String error = jsonObject.getString("error");

                        Log.e("error", error);

                        Log.e("uplaoding", "2");

                        if (error.equalsIgnoreCase("false")) {


                            alertSuccess(message, IntiateWorkFlowActivity.this);


                            Log.e("uplaoding", "3");

                            alertProcessing("off", IntiateWorkFlowActivity.this);

                            // Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {

                    Toast.makeText(IntiateWorkFlowActivity.this, "Upload Canceled", Toast.LENGTH_LONG).show();
                    UploadService.stopUpload(uploadId);

                }
            });


            //alertProcessing("off",IntiateWorkFlowActivity.this);
            multipartUploadRequest.startUpload();
            doclist.clear();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    void alertError(String message, final Context context) {





        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
        tv_error_heading.setText("Error");
        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
        tv_error_subheading.setText(message);
        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);

        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogError.dismiss();

                if(alertDialogProcessing.isShowing())
                {
                    alertDialogProcessing.dismiss();

                }

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialogError = dialogBuilder.create();
        alertDialogError.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogError.show();
    }

    void alertProcessing(String onOff, Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_alert, null);
        dialogBuilder.setView(dialogView);

        Button btnRunInBg = dialogView.findViewById(R.id.progress_alert_run_in_bg);
        btnRunInBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

                if(alertDialogProcessing.isShowing())
                {
                    alertDialogProcessing.dismiss();

                }

            }
        });

        alertDialogProcessing = dialogBuilder.create();
        alertDialogProcessing.setCancelable(false);
        alertDialogProcessing.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        if (onOff.equalsIgnoreCase("on")) {

            alertDialogProcessing.show();

        } else if (onOff.equalsIgnoreCase("off")) {

            alertDialogProcessing.dismiss();

        }


    }

    public void showBottomSheetDialog() {


        View view = getLayoutInflater().inflate(R.layout.bottomsheet_choose, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        LinearLayout llCamera = bottomSheetDialog.findViewById(R.id.ll_aproved_reject_take_photo);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i(TAG, "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {


                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

                    }
                }

                bottomSheetDialog.dismiss();
                //onSelectImageClick(v);


            }
        });
        LinearLayout llFile = bottomSheetDialog.findViewById(R.id.ll_aproved_reject_choose_file);

        llFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                intentChooseFile = new Intent();
//                intentChooseFile.setType("*/*");
//                intentChooseFile.setAction(Intent.ACTION_GET_CONTENT);
//                intentChooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(Intent.createChooser(intentChooseFile,
//                        "select multiple files"), 100);


                new MaterialFilePicker()
                        .withActivity(IntiateWorkFlowActivity.this)
                        .withRequestCode(100)
                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();


                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //intentChooseFile.getBundleExtra("data");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            // Uri uri = data.getData();
            String filepath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String filesize = String.valueOf(new File(filepath).length());
            doclist.add(new MultipleSelect(filepath, filesize, ""));

            Log.e("get files", "working");

//            File file;
//            if (null != data) { // checking empty selection
//                if (null != data.getClipData()) { // checking multiple selection or not
//                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
//                        Uri uri = data.getClipData().getItemAt(i).getUri();
//
//                        file = new File(Objects.requireNonNull(PathUtils.getPath(IntiateWorkFlowActivity.this, uri)));
//                        String filepath = PathUtils.getPath(IntiateWorkFlowActivity.this, uri);
//                        String filesize = String.valueOf(file.length());
//
//
//                        doclist.add(new MultipleSelect(filepath, filesize, uri.toString()));
//                    }
//                } else {
//
//
//                    Uri uri = data.getData();
//                    file = new File(Objects.requireNonNull(PathUtils.getPath(IntiateWorkFlowActivity.this, uri)));
//                    String filepath = PathUtils.getPath(IntiateWorkFlowActivity.this, uri);
//                    String filesize = String.valueOf(file.length());
//                    doclist.add(new MultipleSelect(filepath, filesize, uri.toString()));
//
//                }
//            }


            /*  ClipData clipData = intentChooseFile.getClipData();*/
            /*if ( clipData == null ) {
                Uri uri = data.getData();
                doclist.add(uri.toString());
            }
            else{

                for (int i = 0; i < clipData.getItemCount(); i++) {

                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();

                    //In case you need image's absolute path
                    String path= getRealPathFromURI(IntiateWorkFlowActivity.this, uri);
                    doclist.add(path);
                }



            }*/

       /*     String h = String.valueOf(clipData.getItemAt(0));
            Log.e("h",h);*/


            for (MultipleSelect path :
                    doclist) {

                Log.e("path of file", path.getFilePath());
                Log.e("size of file", path.getFilesize());
            }


            if (doclist.size() == 0) {

                llnofileselected.setVisibility(View.VISIBLE);
                rvMultiSelect.setVisibility(View.GONE);
                btnClearDocList.setVisibility(View.GONE);
                btnClearDocList.setEnabled(false);

            } else {

                btnClearDocList.setVisibility(View.VISIBLE);
                btnClearDocList.setEnabled(true);
                btnClearDocList.setBackgroundColor(getResources().getColor(R.color.red));
                llnofileselected.setVisibility(View.GONE);
                rvMultiSelect.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                // rvMultiSelect.setAdapter(new MultipleFileChooseAdapter(doclist,IntiateWorkFlowActivity.this));

            }


        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

              /*  cvCameraImg.setVisibility(View.VISIBLE);
                ivFileType.setVisibility(View.GONE);
*/

            //alertDialog.dismiss();
            // mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
            //mImageView.setImageBitmap(mImageBitmap);
            filePath = mCurrentPhotoPath;


            Uri uri = Uri.parse(filePath);

            Log.e("uri", uri.toString());

            startCropImageActivity(uri);

            String p = filePath.substring(filePath.lastIndexOf("/") + 1);
            // tvPath.setText(p);

        }

        if (requestCode == 404 && resultCode == RESULT_OK) {


            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);


        }


        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Log.e("image uri", result.getUri().toString());

                String uri = result.getUri().toString();
                String path = uri.substring(7);

                Log.e("path", path);

                filePath = path;
                File filesize = new File(filePath);

                doclist.add(new MultipleSelect(filePath, String.valueOf(filesize.length()), result.getUri().toString()));

                if (doclist.size() == 0) {

                    llnofileselected.setVisibility(View.VISIBLE);
                    rvMultiSelect.setVisibility(View.GONE);
                    btnClearDocList.setVisibility(View.GONE);
                    btnClearDocList.setEnabled(false);

                } else {

                    btnClearDocList.setVisibility(View.VISIBLE);
                    btnClearDocList.setEnabled(true);
                    btnClearDocList.setBackgroundColor(getResources().getColor(R.color.red));
                    llnofileselected.setVisibility(View.GONE);
                    rvMultiSelect.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    // rvMultiSelect.setAdapter(new MultipleFileChooseAdapter(doclist,IntiateWorkFlowActivity.this));

                }


                Log.e("doclist camera added", "working");


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";

        Log.e("path", String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)));

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);


        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",   // suffix
                storageDir      // directory
        );


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        Log.e("mcurrentpath", image.getAbsolutePath());

        return image;
    }

    void alertSuccess(String message, final Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


                if(alertDialogProcessing.isShowing())
                {
                    alertDialogProcessing.dismiss();

                }


                Intent intent = new Intent(context, IntiateWorkFlowActivity.class);
                intent.putExtra("workflowID", workflowId);
                intent.putExtra("workflowName", workflowName);
                startActivity(intent);

                finish();


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialogSuccess = dialogBuilder.create();
        alertDialogSuccess.setCancelable(false);
        alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogSuccess.show();
    }

    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)

                .setActivityMenuIconColor(getResources().getColor(R.color.white))
                .setAllowRotation(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)

                .start(this);
    }

    public void selectStoragePopUp(String slid) {


        View view = getLayoutInflater().inflate(R.layout.bottomsheet_select_storage, null);

        bottomSheetSelectStorageDialog = new BottomSheetDialog(this);
        bottomSheetSelectStorageDialog.setContentView(view);

        //LinearLayout llScroll = bottomSheetSelectStorageDialog.findViewById(R.id.ll_rv_select_storage);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
       /* bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });*/

        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        // bottomSheetBehavior.setPeekHeight(Integer.MAX_VALUE);


        llnodirectoryfound = bottomSheetSelectStorageDialog.findViewById(R.id.ll_selectstorage_nofilefound);
        btnSelectStorage = bottomSheetSelectStorageDialog.findViewById(R.id.btn_select_storage);
        btnSelectStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fNameSlid = horilist.get(horilist.size() - 1).getFoldername();
                bottomSheetSelectStorageDialog.dismiss();

                String storagename = fNameSlid.substring(0, fNameSlid.indexOf("&&"));
                String slid = fNameSlid.substring(fNameSlid.indexOf("&&") + 2);

                tvStorageName.setText(storagename);
                tvSlid.setText(slid);


                Log.e("fnameslid", fNameSlid);

            }
        });


        progressBar = bottomSheetSelectStorageDialog.findViewById(R.id.progressBar_select_storage);

        rvMain = bottomSheetSelectStorageDialog.findViewById(R.id.rv_select_storage);

        rvHori = bottomSheetSelectStorageDialog.findViewById(R.id.rv_select_storage_hori);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setItemViewCacheSize(moveStorageList.size());
        rvMain.setHasFixedSize(true);


        rvHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHori.setItemViewCacheSize(moveStorageList.size());
        rvHori.setHasFixedSize(true);

        moveStorageList.clear();
        horilist.clear();



        getFoldername(slid);


    }


    void showAlertDilogSelectStoragePopup(List<StorageAlloted> list) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(IntiateWorkFlowActivity.this);
        LayoutInflater inflater = (LayoutInflater) IntiateWorkFlowActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_select_root_storage_popup, null);

        rvMain = dialogView.findViewById(R.id.rv_mv_cpy_select_storage);
        rvMain.setLayoutManager(new LinearLayoutManager(IntiateWorkFlowActivity.this));
        rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        StorageAllotedAdapter storageAllotedAdapter = new StorageAllotedAdapter(list, IntiateWorkFlowActivity.this);
        rvMain.setAdapter(storageAllotedAdapter);

        storageAllotedAdapter.setOnClickListener(new StorageAllotedAdapter.OnClickListener() {
            @Override
            public void onStorageClicked(String slid,String storagename) {


                alertDialog.dismiss();
                horilist.clear();
                selectStoragePopUp(slid);

            }
        });

        Button btnClose = dialogView.findViewById(R.id.btn_cpy_mv_cancel_popup);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                //onBackPressed();

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }


    void getFoldername(final String slid) {


        moveStorageList.clear();
        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMain.setVisibility(View.GONE);


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("getfolname", response);

                //{"storagename":"Test-1&&731","foldername":[]}


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String str = jsonObject.getString("storagename");

                    storagename = str.substring(0, str.indexOf("&&"));
                    slidStr = str.substring(str.indexOf("&&") + 2);


                    //329

                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");

                    if (jsonArray.length() == 0) {

                       // llnodirectoryfound.setVisibility(View.VISIBLE);
                        rvMain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);


                        horilist.clear();
                        String id = storagename + "&&" + slid;
                        Foldername foldername1 = new Foldername();
                        foldername1.setFoldername(id);
                        horilist.add(foldername1);

                        //fileViewHorizontalAdapter.notifyDataSetChanged();

                        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horilist, IntiateWorkFlowActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String slid, String fullFolderName) {


                            }
                        });

                        rvHori.setAdapter(fileViewHorizontalAdapter);
                        rvHori.scrollToPosition(horilist.size() - 1);
                        bottomSheetSelectStorageDialog.show();


                    } else {

                        Log.e("test", "ok 1");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2);

                            moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));

                            // spinnerlist.add(fname);

                        }

                        Log.e("test", "ok 2");

                        String id = storagename + "&&" + slid;
                        Foldername foldername1 = new Foldername();
                        foldername1.setFoldername(id);
                        horilist.add(foldername1);


                        Log.e("test", "ok 3");

                        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horilist, IntiateWorkFlowActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String Slid, String fullFolderName) {


                                if (Slid == slid) {

                                    horilist.clear();
                                    String id = storagename + "&&" + MainActivity.slid_session;
                                    Foldername foldername1 = new Foldername();
                                    foldername1.setFoldername(id);
                                    horilist.add(foldername1);
                                    fileViewHorizontalAdapter.notifyDataSetChanged();
                                    rvHori.scrollToPosition(horilist.size() - 1);


                                } else {

                                    horilist.subList(position + 1, horilist.size()).clear();
                                    fileViewHorizontalAdapter.notifyDataSetChanged();

                                    getChildFoldername(Slid);

                                }


                            }
                        });

                        rvHori.setAdapter(fileViewHorizontalAdapter);
                        Log.e("test", "ok 4");


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, IntiateWorkFlowActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {


                                // tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);

                                fileViewHorizontalAdapter.notifyDataSetChanged();

                           /* tvpreviousFname.setText(foldername);
                            tvpreviousSlid.setText(slid);*/


                                getChildFoldername(slid);


                            }
                        });
                        rvMain.setAdapter(moveStorageListAdapter);
                        moveStorageListAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        rvMain.setVisibility(View.VISIBLE);
                        bottomSheetSelectStorageDialog.show();

                        Log.e("test", "ok 5");


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
                params.put("slid", slid);


                return params;
            }
        };


        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);


    }

    //cash voucher

    void getChildFoldername(final String slid) {

        // spinnerchildlist.clear();

        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMain.setVisibility(View.GONE);


        moveStorageList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getchildfoldernames", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("foldername");


                    if (jsonArray.length() == 0) {

                        progressBar.setVisibility(View.GONE);
                        rvMain.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.VISIBLE);


                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2);

                            moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));


                         /*   arrayListchild.add(fname);


                            mySpinnerChildlist.add(arrayListchild);
*/
                        }


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, IntiateWorkFlowActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {

                                //tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);
                                fileViewHorizontalAdapter.notifyDataSetChanged();
                                rvHori.scrollToPosition(horilist.size() - 1);

                                getChildFoldername(slid);
                            }
                        });

                        rvMain.setAdapter(moveStorageListAdapter);


                        moveStorageListAdapter.notifyDataSetChanged();


                        progressBar.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.GONE);
                        rvMain.setVisibility(View.VISIBLE);


                    }




                    /*  addSpinner();*/

                    /*    mySpinnerChildlistCounter++;
                     */


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
                params.put("slid", slid);


                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);


    }

    void getDivison(final String userid, final Spinner spinner, final LinearLayout linearLayout) {

        divisonList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CASH_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getDiv", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String id = jsonArray.getJSONObject(i).getString("Id");
                        String divName = jsonArray.getJSONObject(i).getString("division_name");
                        String companyId = jsonArray.getJSONObject(i).getString("companyId");

                        divisonList.add(new Divison(divName, id, companyId));

                    }

                    Log.e("divisionlist size", String.valueOf(divisonList.size()));

                    if (spinner.getTag().toString().equalsIgnoreCase("spFormDivison")) {


                        divList = new ArrayList<>();

                        divList.clear();

                        for (Divison dlist : divisonList
                        ) {

                            divList.add(dlist.getDivisionName());
                        }


                        divisionAdapter = new ArrayAdapter<String>(
                                IntiateWorkFlowActivity.this, android.R.layout.simple_spinner_item, divList) {
                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }
                        };

                        divList.add("Select Divison");

                        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(divisionAdapter);
                        spinner.setSelection(divisionAdapter.getCount());
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                String name = "";
                                String ID = "";

                                if (spinner.getTag().toString().equalsIgnoreCase("spFormDivison")) {

                                    name = spinner.getSelectedItem().toString();

                                }

                                for (Divison div :
                                        divisonList) {

                                    if (div.getDivisionName().contains(name)) {

                                        ID = div.getDivisionId().trim();
                                    }
                                }


                                Spinner sp = findViewById(R.id.spinnerProject);

                                spinnerIdList.put("divisionID", ID);
                                spinnerSelectedItemList.put("division", name);

                                getProject(ID, sp);



                              /* if(!findViewById(R.id.spinner_formbuilder).getTag().equals("spFormProject")){

                                    //Spinner sp = findViewById(R.id.spinner_formbuilder).getTag();

                                  Spinner sp =  spinner.findViewWithTag("spFormProject");

                                  Log.e("spinner tag",sp.getTag().toString());

                                   getProject(ID,sp);

                               }
*/

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


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

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);
    }

    void getProject(final String divisionId, final Spinner spinner) {

        llCashVoucherMain.setVisibility(View.VISIBLE);

        projectList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CASH_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getProject", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {


                        String id = jsonArray.getJSONObject(i).getString("Id");
                        String divId = jsonArray.getJSONObject(i).getString("divisionId");
                        String projectName = jsonArray.getJSONObject(i).getString("project_name");
                        String companyId = jsonArray.getJSONObject(i).getString("companyId");
                        String location = jsonArray.getJSONObject(i).getString("location");

                        projectList.add(new Project(id, divId, projectName, location, companyId));


                    }

                    // Log.e("spinner project tag",spinner.getTag().toString());


                    projlist = new ArrayList<>();

                    projlist.clear();

                    for (Project plist : projectList
                    ) {

                        projlist.add(plist.getProjectName());
                    }

                    Log.e("projectlist size", String.valueOf(projectList.size()));
                    Log.e("plist size", String.valueOf(projlist.size()));

                    projectAdapter = new ArrayAdapter<String>(
                            IntiateWorkFlowActivity.this, android.R.layout.simple_spinner_item, projlist) {
                        @Override
                        public int getCount() {
                            // don't display last item. It is used as hint.
                            int count = super.getCount();
                            return count > 0 ? count - 1 : count;
                        }
                    };

                    projlist.add("Select Project");

                    projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(projectAdapter);
                    spinner.setSelection(projectAdapter.getCount());
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String name = "";
                            String ID = "";

                            if (spinner.getTag().toString().equalsIgnoreCase("spFormProject")) {

                                name = spinner.getSelectedItem().toString();

                            }

                            for (Project prog :
                                    projectList) {

                                if (prog.getProjectName().contains(name)) {

                                    ID = prog.getId().trim();
                                }
                            }

                            Spinner sp = findViewById(R.id.spinnerLocation);

                            spinnerIdList.put("projectID", ID);
                            spinnerSelectedItemList.put("project", name);

                            getLocation(ID, sp);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


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
                params.put("divisionId", divisionId);

                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);
    }

    void getLocation(final String Id, final Spinner spinner) {

        locationList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CASH_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getLoc", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);


                    for (int i = 0; i < jsonArray.length(); i++) {

                        String locationName = jsonArray.getJSONObject(i).getString("location_name");
                        String locationId = jsonArray.getJSONObject(i).getString("Id");

                        locationList.add(new Location(locationName, locationId));


                    }

                    List<String> locList = new ArrayList<>();
                    for (Location loc :
                            locationList) {

                        locList.add(loc.getLocationName());
                    }


                    if (spinner.getTag().toString().equalsIgnoreCase("spFormLocation")) {


                        locationAdapter = new ArrayAdapter<String>(
                                IntiateWorkFlowActivity.this, android.R.layout.simple_spinner_item, locList) {
                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }
                        };

                        locList.add("Select Location");


                        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(locationAdapter);
                        spinner.setSelection(locationAdapter.getCount());
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String locId = "";
                                String selectedItem = spinner.getSelectedItem().toString();

                                for (Location loc :
                                        locationList) {

                                    if (loc.getLocationName().contains(selectedItem)) {

                                        locId = loc.getLocationId().trim();
                                    }
                                }

                                spinnerIdList.put("locationID", locId);
                                spinnerSelectedItemList.put("location", selectedItem);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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
                params.put("Id", Id);

                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);
    }

    void cashVoucherNo(final String userid, EditText editText) {

        locationList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CASH_VOUCHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("cashVoucherNo", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    cashVocherNo = jsonObject.getString("voucher_no");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                editText.setText(cashVocherNo);
                editText.setEnabled(false);
                pbMain.setVisibility(View.GONE);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("cashVoucherNo", userid);

                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);
    }

    void addMoreRupeePaisa() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        llCashAddM = inflater.inflate(R.layout.cash_voucher_add_more_layout, null);
        llCashAddM.setTag("AddMore" + addMoreCounter);
        addMoreViewList.add(llCashAddM);
        final TextView tvCounter = llCashAddM.findViewById(R.id.tv_add_more_counter);
        tvCounter.setText(String.valueOf(addMoreCounter + 1));

        EditText etRupesDynamic = llCashAddM.findViewById(R.id.et_intiateworkflow_rupee_dynamic);
        EditText etPaisaDynamic = llCashAddM.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
        EditText etDescDynamic = llCashAddM.findViewById(R.id.et_intiateworkflow_desc_dynamic);


        ImageButton ivRemove = llCashAddM.findViewById(R.id.ib_intiateworkflow_remove);
        ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // llCashAddM.removeViewAt(Integer.parseInt(tvCounter.getText().toString()));

                EditText etRupesDynamic = addMoreViewList.get(Integer.parseInt(tvCounter.getText().toString()) - 1).findViewById(R.id.et_intiateworkflow_rupee_dynamic);
                EditText etPaisaDynamic = addMoreViewList.get(Integer.parseInt(tvCounter.getText().toString()) - 1).findViewById(R.id.et_intiateworkflow_paisa_dynamic);
                etRupesDynamic.getText().clear();
                etPaisaDynamic.getText().clear();

                etTotalRupess.setText(String.valueOf(getTotalAmount()));
                etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert(getTotalAmount())));


                addMoreViewList.remove(Integer.parseInt(tvCounter.getText().toString()) - 1);
                llCashAddMore.removeViewAt(Integer.parseInt(tvCounter.getText().toString()));

                Log.e("childcount", String.valueOf(llCashAddMore.getChildCount()));
                Log.e("tvCount", String.valueOf(Integer.parseInt(tvCounter.getText().toString())));
                Log.e("count", String.valueOf(addMoreCounter));
                addMoreCounter--;


            }
        });


        etRupesDynamic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String totalcount = "";
                Log.e("start", String.valueOf(start));
                Log.e("before", String.valueOf(before));
                Log.e("count", String.valueOf(count));


                if (s.toString().equalsIgnoreCase("") || s.toString().length() == 0 || TextUtils.isEmpty(s.toString())) {

                    //totalcount = String.valueOf(TotalRupees);
                    Log.e("no text", "no text");

                    if (etRupeesStatic.getText().length() > 0 && etPaisaStatic.getText().length() > 0) {


                        etTotalRupess.setText(String.valueOf(getTotalAmount()));
                        etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert(getTotalAmount())));


                    } else if (addMoreViewList.size() > 0) {


                        for (int i = 0; i < addMoreViewList.size(); i++) {

                            EditText etRuppeDyna = addMoreViewList.get(i).findViewById(R.id.et_intiateworkflow_rupee_dynamic);
                            EditText etPaisaDyna = addMoreViewList.get(i).findViewById(R.id.et_intiateworkflow_paisa_dynamic);
                            if (etRuppeDyna.getText().length() == 0) {

                                etTotalRupess.getText().clear();
                                etTotalRupessWords.getText().clear();
                                etRuppeDyna.setText("");
                                etPaisaDyna.setText("");

                                etTotalRupess.setText(String.valueOf(getTotalAmount()));
                                etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert(getTotalAmount())));


                            } else {


                                etTotalRupess.setText(String.valueOf(getTotalAmount()));
                                etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert(getTotalAmount())));

                            }


                            if (etPaisaDyna.getText().length() == 0) {

                                etTotalRupess.getText().clear();
                                etTotalRupessWords.getText().clear();
                                etPaisaDyna.getText().clear();

                                etTotalRupess.setText(String.valueOf(getTotalAmount()));
                                etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert(getTotalAmount())));


                            }

                        }


                        etTotalRupess.setText(String.valueOf(getTotalAmount()));
                        etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert((getTotalAmount()))));


                    } else {

                        etTotalRupess.getText().clear();
                        etTotalRupessWords.getText().clear();

                        etTotalRupess.setText(String.valueOf(getTotalAmount()));
                        etTotalRupessWords.setText(String.valueOf(DigitToRuppee.convert(getTotalAmount())));

                    }


                    //etTotalRupess.setText("0");
                    //etTotalRupessWords.setText(DigitToRuppee.convert(getTotalAmount()));

                    // etRupesDynamic.getText().clear();


                } else {

                    totalcount = String.valueOf(Integer.parseInt(s.toString()) + TotalRupees);
                    TotalRupees = Integer.parseInt(totalcount);
                    etTotalRupess.setText(String.valueOf(getTotalAmount()));
                    etTotalRupessWords.setText(DigitToRuppee.convert(getTotalAmount()));


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etPaisaDynamic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        llCashAddMore.addView(llCashAddM);

        addMoreCounter++;


       /* EditText etRupee = llCashAddMore.findViewById(R.id.et_intiateworkflow_rupee_dynamic);



        EditText etPaisa = llCashAddMore.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
        */

        // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_voucher_add_more_layout, parent, false);


    }

    int getTotalAmount() {

        int totalAmount = 0;

        if (addMoreViewList.size() == 0) {

            if (etRupeesStatic.getText().toString().length() == 0) {

                etRupeesStatic.setText("0");


            } else {

                totalAmount = Integer.parseInt(etRupeesStatic.getText().toString());
            }


        } else {


            String etStaticRupeeValue = etRupeesStatic.getText().toString();

            if (etStaticRupeeValue.length() == 0) {


                etStaticRupeeValue = "0";
                //etRupeesStatic.setText("0");
                // etRupeesStatic.getText().clear();

            }


            for (int i = 0; i < addMoreViewList.size(); i++) {

                Log.e("static rupee", etStaticRupeeValue);
                View view = addMoreViewList.get(i);
                CardView cardView = (CardView) view;
                EditText etRupeeDynamic = cardView.findViewById(R.id.et_intiateworkflow_rupee_dynamic);

                String etDynamicRupee = "";

                if (etRupeeDynamic.getText().toString().length() == 0) {

                    etDynamicRupee = "0";

                } else {

                    etDynamicRupee = etRupeeDynamic.getText().toString();
                }


                totalAmount = totalAmount + Integer.parseInt(etDynamicRupee);
                Log.e("totalAmount", String.valueOf(totalAmount));

            }

            totalAmount = totalAmount + Integer.parseInt(etStaticRupeeValue);

        }


        return totalAmount;

    }

    void getCashVoucherValue() {

        CashVoucherAllValues.clear();
        String word = DigitToRuppee.convert(245);
        Log.e("word", word);

        String divison = "";
        String project = "";
        String location = "";


        Log.e("linearlayoutsize", String.valueOf(linearLayoutsList.size()));
        stringBuilder.setLength(0);


        for (int i = 0; i < linearLayoutsList.size(); i++) {


            LinearLayout linearLayout = linearLayoutsList.get(i);

            Log.e("linearlayoutsize", String.valueOf(linearLayoutsList.size()));


            //spinner
            if (linearLayout.findViewById(R.id.ll_spinner_formbuilder) != null) {


                Log.e("llspinner", "exist");


                LinearLayout layout = linearLayout.findViewById(R.id.ll_spinner_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_heading_spinner_formbuilder);
                String header = tvHeader.getText().toString();

                Log.e("header spinner", header);


                Spinner spinner = linearLayout.findViewById(R.id.spinner_formbuilder);

                String spText = "";

                if (spinner != null) {

                    spText = spinner.getSelectedItem().toString();

                } else {


                    spText = "null" + " " + header + " " + i;
                }

                stringBuilder.append(header).append(" : ").append(spText).append("\n").append("\n");


                //ids of spinner selected items
                for (Map.Entry<String, String> spList : spinnerSelectedItemList.entrySet()) {

                    if (spList.getKey().equalsIgnoreCase(header)) {

                        //project = spList.getValue();
                        CashVoucherAllValues.put(header, spList.getValue());


                    }


                }

                // CashVoucherAllValues.put(header,spText);

                Log.e("spinner_header" + spinnerCount, header);
                Log.e("spinnerText" + spinnerCount, spText);

                formvalues.add(spText);

                spinnerCount++;

            }


            //date
            else if (linearLayout.findViewById(R.id.ll_date_formbuilder) != null) {


                Log.e("lldate", "exist");
                Log.e("date", "1");


                LinearLayout layout = linearLayout.findViewById(R.id.ll_date_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_heading_date_formbuilder);
                String header = tvHeader.getText().toString();

                EditText editText = linearLayout.findViewById(R.id.et_heading_date_formbuilder);
                String etText = editText.getText().toString();


                editText.setError(null);
                stringBuilder.append(header).append(" : ").append(etText).append("\n").append("\n");
                CashVoucherAllValues.put(header, etText);


                Log.e("Date_header" + dateCount, header);
                Log.e("Date" + dateCount, etText);

                formvalues.add(etText);

                dateCount++;


            }/* else {

                Log.e("lldate", "dont exist");

            }
*/
            //text
            else if (linearLayout.findViewById(R.id.ll_text_formbuilder) != null) {


                Log.e("llText", "exist");
                Log.e("llText", "2");

                LinearLayout layout = linearLayout.findViewById(R.id.ll_text_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_text_formbuilder);
                String header = tvHeader.getText().toString();

                EditText editText = linearLayout.findViewById(R.id.et_text_formbuilder);
                String etText = editText.getText().toString();

                stringBuilder.append(header).append(" : ").append(etText).append("\n").append("\n");

                CashVoucherAllValues.put(header, etText);

                Log.e("Text_header" + textCount, header);
                Log.e("Text" + textCount, etText);

                formvalues.add(etText);

                textCount++;

            } /*else {

                Log.e("llText", "dont exist");

            }*/


            //editext
            else if (linearLayout.findViewById(R.id.ll_edittext_formbuilder) != null) {


                Log.e("llEdText", "exist");
                Log.e("llEdText", "3");

                LinearLayout layout = linearLayout.findViewById(R.id.ll_edittext_formbuilder);

                TextView tvHeader = layout.findViewById(R.id.tv_heading_edittext_formbuilder);
                String header = tvHeader.getText().toString();

                EditText editText = linearLayout.findViewById(R.id.et_edittext_formbuilder);
                String etText = editText.getText().toString();


                stringBuilder.append(header).append(" : ").append(etText).append("\n").append("\n");
                CashVoucherAllValues.put(header, etText);

                Log.e("Text_header" + edittextCount, header);
                Log.e("Text" + edittextCount, etText);

                formvalues.add(etText);
                edittextCount++;


            } /*else {

                Log.e("llEdText", "dont exist");

            }*/


        }


        //ids of spinner selected items
        for (Map.Entry<String, String> spList : spinnerSelectedItemList.entrySet()) {

            if (spList.getKey().equalsIgnoreCase("project")) {

                project = spList.getValue();
                //CashVoucherAllValues.put("Project",project);


            }

            if (spList.getKey().equalsIgnoreCase("division")) {

                divison = spList.getValue();
                //CashVoucherAllValues.put("Division",divison);

            }
            if (spList.getKey().equalsIgnoreCase("location")) {

                location = spList.getValue();
                // CashVoucherAllValues.put("Location",location);

            }

        }

        Log.e("Selected Project name", project);
        Log.e("Selected divison name", divison);
        Log.e("Selected location name", location);

        //ids of spinner selected items
        for (Map.Entry<String, String> spList : spinnerIdList.entrySet()) {

            Log.e("spinerlistselected", spList.getKey() + " " + spList.getValue());
        }

        //making a array of field data
        strFormValues.setLength(0);

        // Log.e("formvalues array", String.valueOf(formvalues.toArray()));


        for (int i = 0; i < formvalues.size(); i++) {

            strFormValues.append(formvalues.get(i)).append(",");

        }


        frmVal = strFormValues.toString();

        if (frmVal.endsWith(",")) {
            frmVal = frmVal.substring(0, frmVal.length() - 1);
        }

        Log.e("formvalues", frmVal);

        String mode = "";

        if (spModeOfConvey != null) {

            mode = spModeOfConvey.getSelectedItem().toString();
            CashVoucherAllValues.put("Mode Of Conveyance", mode);

        }

        String purpose = etPurpose.getText().toString();

        if (purpose.trim().length() != 0) {

            CashVoucherAllValues.put("Purpose", purpose);

        }


        //All amount values

        String DescriptionStatic = etDescription.getText().toString();
        String RupeeStatic = etRupeesStatic.getText().toString();
        String PaisaStatic = etPaisaStatic.getText().toString();

        descriptionList.clear();

        if (DescriptionStatic.trim().length() != 0 || RupeeStatic.trim().length() != 0 || PaisaStatic.trim().length() != 0) {

            descriptionList.add(new Description(DescriptionStatic, RupeeStatic, PaisaStatic));
            CashVoucherAllValues.put("DescStatic", "ok");

        }

        for (int i = 0; i < addMoreViewList.size(); i++) {

            View v = addMoreViewList.get(i);
            CardView cv = (CardView) v;
            EditText etDesc = cv.findViewById(R.id.et_intiateworkflow_desc_dynamic);
            EditText etPaisa = cv.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
            EditText etRupee = cv.findViewById(R.id.et_intiateworkflow_rupee_dynamic);
            descriptionList.add(new Description(etDesc.getText().toString(), etRupee.getText().toString(), etPaisa.getText().toString()));


        }

        CashVoucherAllValues.put("DescDynamic", "ok");


        String totalrupees = etTotalRupess.getText().toString();

        if (totalrupees.length() != 0) {

            CashVoucherAllValues.put("Total Rupees", totalrupees);

        }


        String totalrupeesWord = etTotalRupessWords.getText().toString();

        if (totalrupees.length() != 0) {

            CashVoucherAllValues.put("Total Amount", totalrupeesWord);

        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(IntiateWorkFlowActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_workflow_preview, null);

        LinearLayout llTableRow = dialogView.findViewById(R.id.ll_inflate_tablerow_preview_workflow);

        //ll_inflate_tablerow_preview_workflow

        for (Map.Entry<String, String> cData : CashVoucherAllValues.entrySet()) {

            Log.e("key", cData.getKey());

            if (cData.getKey().equalsIgnoreCase("DescStatic")) {

                View tableRow = inflater.inflate(R.layout.workflow_intiate_cashvoucher_desciption, null);
                TextView tvDesc = tableRow.findViewById(R.id.tv_description_desc);
                tvDesc.setText(descriptionList.get(0).getDesc());
                TextView tvRupee = tableRow.findViewById(R.id.tv_desciption_value_amount);
                tvRupee.setText(descriptionList.get(0).getRupee());
                TextView tvPaisa = tableRow.findViewById(R.id.tv_desciption_value_paisa);
                tvPaisa.setText(descriptionList.get(0).getPaisa());

                llTableRow.addView(tableRow);
            } else if (cData.getKey().equalsIgnoreCase("DescDynamic")) {

                for (int i = 1; i < descriptionList.size(); i++) {

                    View tableRow = inflater.inflate(R.layout.workflow_intiate_cashvoucher_desciption, null);
                    TextView tvDesc = tableRow.findViewById(R.id.tv_description_desc);
                    tvDesc.setText(descriptionList.get(i).getDesc());
                    TextView tvRupee = tableRow.findViewById(R.id.tv_desciption_value_amount);
                    tvRupee.setText(descriptionList.get(i).getRupee());
                    TextView tvPaisa = tableRow.findViewById(R.id.tv_desciption_value_paisa);
                    tvPaisa.setText(descriptionList.get(i).getPaisa());

                    llTableRow.addView(tableRow);

                }


            } else {

                View tableRow = inflater.inflate(R.layout.workflow_intiate_cashvoucher_preview, null);
                TextView tvKey = tableRow.findViewById(R.id.tv_key);
                TextView tvValue = tableRow.findViewById(R.id.tv_value);

                if (cData.getKey().equalsIgnoreCase("Total Rupees") || cData.getKey().equalsIgnoreCase("Total Amount")) {


                    tvKey.setBackground(getResources().getDrawable(R.drawable.shape_rect_6));
                    tvValue.setTypeface(null, Typeface.BOLD);
                    tvValue.setTextColor(Color.BLACK);
                    tvKey.setTextColor(Color.WHITE);

                    //tvValue.setText(cData.getValue());

                }


                tvKey.setText(cData.getKey());
                tvValue.setText(cData.getValue());


                llTableRow.addView(tableRow);
            }


        }

        Log.e("HashMap Values", CashVoucherAllValues.toString());

        TextView tvPreview = dialogView.findViewById(R.id.tv_preview_workflow);
        //tvPreview.setText(stringBuilder);


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_workflow_close);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stringBuilder.setLength(0);
                alertDialogPreview.dismiss();


            }
        });

        Button btn_submit_ok = dialogView.findViewById(R.id.btn_workflow_submit);
        btn_submit_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stringBuilder.setLength(0);
                String lastmoveid = "";

                byte[] data = new byte[0];

                data = workflowId.getBytes(StandardCharsets.UTF_8);

                String widBase64 = Base64.encodeToString(data, Base64.DEFAULT);

                Log.e("widBase64", widBase64);

                if (checkBoxAttachDoc.isChecked()) {


                    if (doclist.size() == 0) {

                        Toast.makeText(IntiateWorkFlowActivity.this, "Please select file for uploading", Toast.LENGTH_SHORT).show();

                    } else {

                        if (tvSlid.getText().toString().trim().length() != 0) {

                            lastmoveid = tvSlid.getText().toString();
                            Log.e("lastmoveid", lastmoveid);

                            getCashVoucherValues(widBase64, lastmoveid);
                            // intiateWorkflowWithUpload(widBase64,MainActivity.userid,lastmoveid,frmVal, String.valueOf(0),MainActivity.username);

                        } else {

                            Toast.makeText(IntiateWorkFlowActivity.this, "Please select storage", Toast.LENGTH_SHORT).show();

                        }


                    }


                } else {

                    getCashVoucherValues(widBase64, lastmoveid);

                    // intiateWorkflowWithoutUpload(widBase64, MainActivity.userid, "", frmVal,MainActivity.username);


                }


                alertDialogPreview.dismiss();

                alertProcessing("On", IntiateWorkFlowActivity.this);


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialogPreview = dialogBuilder.create();
        alertDialogPreview.setCancelable(false);
        alertDialogPreview.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogPreview.show();


    }

    void clearAllList() {

        doclist.clear();
        divList.clear();
        locationList.clear();
        locList.clear();
        projlist.clear();
        projectList.clear();
        spinnerIdList.clear();


    }

    void getCashVoucherValues(String widBase64, String lastmoveid) {

        Map<String, String> finalCashVoucherList = new LinkedHashMap<>();
        finalCashVoucherList.clear();

        for (Map.Entry<String, String> val : CashVoucherAllValues.entrySet()) {


            if (val.getKey().equalsIgnoreCase("Division")) {


                String divisionname = val.getValue();
                String divisionId = "";
                for (Divison div :
                        divisonList) {

                    if (div.getDivisionName().contains(divisionname)) {

                        divisionId = div.getDivisionId().trim();
                    }
                }


                finalCashVoucherList.put(val.getKey(), divisionId);

            } else if (val.getKey().equalsIgnoreCase("Project")) {


                String projectName = val.getValue();

                String projectId = "";
                for (Project div :
                        projectList) {

                    if (div.getProjectName().contains(projectName)) {

                        projectId = div.getId().trim();
                    }
                }


                finalCashVoucherList.put(val.getKey(), projectId);

            } else if (val.getKey().equalsIgnoreCase("Location")) {


                String LocName = val.getValue();
                String LocId = "";
                for (Location div :
                        locationList) {

                    if (div.getLocationName().contains(LocName)) {

                        LocId = div.getLocationId().trim();
                    }
                }


                finalCashVoucherList.put(val.getKey(), LocName);

            } else if (val.getKey().equalsIgnoreCase("Purpose") || val.getKey().equalsIgnoreCase("Mode Of Conveyance") || val.getKey().equalsIgnoreCase("DescStatic") || val.getKey().equalsIgnoreCase("DescDynamic") || val.getKey().equalsIgnoreCase("Total Rupees") || val.getKey().equalsIgnoreCase("Total Amount")) {//Total Rupees

                finalCashVoucherList.remove(val.getKey());
            } else {


                finalCashVoucherList.put(val.getKey(), val.getValue());
            }


        }


        Log.e("final cashvoucher list", finalCashVoucherList.toString());

        StringBuilder dynamicCashValue = new StringBuilder();

        for (Map.Entry<String, String> str : finalCashVoucherList.entrySet()
        ) {

            dynamicCashValue.append(str.getValue()).append("^");

        }


        String cashvoucherString = dynamicCashValue.toString();

        if (cashvoucherString.endsWith("^")) {

            cashvoucherString = cashvoucherString.substring(0, cashvoucherString.length() - 1);


        }

        Log.e("final voucher", cashvoucherString);


        //hitting the api

        String purpose = etPurpose.getText().toString().trim();
        String totalRupees = etTotalRupess.getText().toString();
        String totalRupeesWords = etTotalRupessWords.getText().toString();
        StringBuilder desArray = new StringBuilder();
        StringBuilder rupeeArray = new StringBuilder();
        StringBuilder paisaArray = new StringBuilder();

        for (Description val :
                descriptionList
        ) {

            desArray.append(val.getDesc()).append(",");


        }

        for (Description val :
                descriptionList
        ) {

            rupeeArray.append(val.getRupee()).append(",");


        }

        for (Description val :
                descriptionList
        ) {

            paisaArray.append(val.getPaisa()).append(",");


        }

        String descArrayString = desArray.toString();
        String paisaArrayString = paisaArray.toString();
        String rupeeArrayString = rupeeArray.toString();

        if (desArray.toString().endsWith(",")) {

            descArrayString = descArrayString.substring(0, descArrayString.length() - 1);


        }
        if (paisaArray.toString().endsWith(",")) {

            paisaArrayString = paisaArrayString.substring(0, paisaArrayString.length() - 1);


        }

        if (rupeeArray.toString().endsWith(",")) {

            rupeeArrayString = rupeeArrayString.substring(0, rupeeArrayString.length() - 1);


        }

        Log.e("descArrayString", descArrayString);
        Log.e("paisaArrayString", paisaArrayString);
        Log.e("rupeeArrayString", rupeeArrayString);

        String modeofconvey = spModeOfConvey.getSelectedItem().toString();

        Log.e("lastmOveId", lastmoveid);


        if (checkBoxAttachDoc.isChecked()) {

            cashVoucherWithUploading(widBase64, cashvoucherString, purpose, modeofconvey, descArrayString, rupeeArrayString, paisaArrayString, totalRupees, totalRupeesWords, lastmoveid);


        } else {


            cashVoucherWithoutUploading(widBase64, cashvoucherString, purpose, modeofconvey, descArrayString, rupeeArrayString, paisaArrayString, totalRupees, totalRupeesWords);
        }


    }

    void cashVoucherWithUploading(String workflowId, String formvalues, String purpose, String modeofconvey, String descriptionArray, String rupeeArray, String paisaArray, String TotalRupees, String TotalRupeesWord, String lastmoveid) {

        final String uploadId = UUID.randomUUID().toString();
        StringBuilder pathString = new StringBuilder();

        for (int i = 0; i < doclist.size(); i++) {

            pathString.append(doclist.get(i).getFilePath()).append(",");

        }


        String path = pathString.toString();

        if (pathString.toString().endsWith(",")) {

            path = path.substring(0, path.length() - 1);

        }

        String pathArray[] = new String[]{path};

        Log.e("patharray", Arrays.toString(pathArray));


        try {


            multipartUploadRequest = new MultipartUploadRequest(IntiateWorkFlowActivity.this, uploadId, ApiUrl.INITIATE_WORKFLOW);


            UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();
            multipartUploadRequest.setNotificationConfig(

                    uploadNotificationConfig
                            .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                            .setClearOnActionForAllStatuses(true));

            for (int j = 0; j < doclist.size(); j++) {


                Log.e("path", doclist.get(j).getFilePath());
                try {
                    multipartUploadRequest.addFileToUpload(doclist.get(j).getFilePath(), "fileName[]");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            List<String> pathList = new ArrayList<>(Arrays.asList(pathArray));


            //multipartUploadRequest.addArrayParameter("file",pathList);


            Log.e("Upload_rate", UPLOAD_RATE);

            //uploadNotificationConfig.getProgress().message = PROGRESS;
            //uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

            uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(IntiateWorkFlowActivity.this, 1, uploadId)));


            uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
            // uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;


            uploadNotificationConfig.getError().message = "Error while uploading";
            uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
            //uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


            uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
            //uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
            uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;

           /* params.put("wid", workflowdid);
            params.put("userid", userid);
            params.put("lastMoveId", lastmoveid);
            params.put("fieldValues", fielValues);*/



//            PdfReader reader;
//            String pagecount;
//            try {
//                if () {
//                    reader = new PdfReader(filePath);
//                    int noOfPages = reader.getNumberOfPages();
//                    String pages = String.valueOf(noOfPages);
//
//                } else {
//
//                    tvPageCount.setText("1");
//                    pagecount = "1";
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            multipartUploadRequest.addParameter("wid", workflowId);
            multipartUploadRequest.addParameter("fieldValues", formvalues);
            multipartUploadRequest.addParameter("descp", purpose);
            multipartUploadRequest.addParameter("cashvocher", descriptionArray);
            multipartUploadRequest.addParameter("amt", rupeeArray);
            multipartUploadRequest.addParameter("namt", paisaArray);
            multipartUploadRequest.addParameter("rupee", TotalRupees);
            multipartUploadRequest.addParameter("amount", TotalRupeesWord);
            multipartUploadRequest.addParameter("userid", MainActivity.userid);
            multipartUploadRequest.addParameter("username", MainActivity.username);
            multipartUploadRequest.addParameter("lastMoveId", lastmoveid);
            multipartUploadRequest.addParameter("wf_modeof_conveyance", modeofconvey);
            multipartUploadRequest.addParameter("pageCount", "1");
            multipartUploadRequest.addParameter("tokenid", tokenid);


            multipartUploadRequest.setMaxRetries(5);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {

                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                    Toast.makeText(IntiateWorkFlowActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                    // Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                    Log.e("Server Response", serverResponse.getBodyAsString());

                    Log.e("ser uploading", serverResponse.getBodyAsString());


                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                        Log.e("uplaoding", "1");

                        String message = jsonObject.getString("msg");
                        String error = jsonObject.getString("error");

                        Log.e("error", error);

                        Log.e("uplaoding", "2");

                        if (error.equalsIgnoreCase("false")) {


                            alertSuccess(message, IntiateWorkFlowActivity.this);


                            Log.e("uplaoding", "3");

                            alertProcessing("off", IntiateWorkFlowActivity.this);

                            // Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                        } else {


                            alertProcessing("off", IntiateWorkFlowActivity.this);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {

                    Toast.makeText(IntiateWorkFlowActivity.this, "Upload Canceled", Toast.LENGTH_LONG).show();
                    UploadService.stopUpload(uploadId);

                }
            });


            //alertProcessing("off",IntiateWorkFlowActivity.this);

            multipartUploadRequest.startUpload();
            doclist.clear();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    void cashVoucherWithoutUploading(String workflowId, String formvalues, String purpose, String modeofconvey, String descriptionArray, String rupeeArray, String paisaArray, String TotalRupees, String TotalRupeesWord) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.INITIATE_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response cashvoucher", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");


                    if (error.equalsIgnoreCase("true")) {

                        alertProcessing("off", IntiateWorkFlowActivity.this);

                        alertError(msg, IntiateWorkFlowActivity.this);

                    } else if (error.equalsIgnoreCase("false")) {


                        alertProcessing("off", IntiateWorkFlowActivity.this);

                        alertSuccess(msg, IntiateWorkFlowActivity.this);


                    } else {

                        alertProcessing("off", IntiateWorkFlowActivity.this);

                        alertError("There might be some issue try again later", IntiateWorkFlowActivity.this);


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
                params.put("wid", workflowId);
                params.put("fieldValues", formvalues);
                params.put("descp", purpose);
                params.put("cashvocher", descriptionArray);
                params.put("amt", rupeeArray);
                params.put("namt", paisaArray);
                params.put("rupee", TotalRupees);
                params.put("amount", TotalRupeesWord);
                params.put("userid", MainActivity.userid);
                params.put("username", MainActivity.username);
                params.put("lastMoveId", "");
                params.put("wf_modeof_conveyance", modeofconvey);
                params.put("tokenid", tokenid);


                return params;
            }
        };

        VolleySingelton.getInstance(IntiateWorkFlowActivity.this).addToRequestQueue(stringRequest);

    }

    void reset() {

       /* doclist.clear();
        projectList.clear();
        locationList.clear();*/

        etPaisaStatic.getText().clear();
        etRupeesStatic.getText().clear();
        etTotalRupessWords.getText().clear();
        etTotalRupess.getText().clear();
        etPurpose.getText().clear();
        etDescription.getText().clear();

        conveyanceList.clear();
        conveyanceList.add("Auto");
        conveyanceList.add("Car");
        conveyanceList.add("Cab");
        conveyanceList.add("Metro");
        conveyanceList.add("Select Mode Of Conveyance");


        ArrayAdapter<String> conveyanceAdapter = new ArrayAdapter<String>(IntiateWorkFlowActivity.this, android.R.layout.simple_spinner_item, conveyanceList) {


            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }


        };


        conveyanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModeOfConvey.setAdapter(conveyanceAdapter);
        spModeOfConvey.setSelection(conveyanceAdapter.getCount());


        for (int i = 0; i < linearLayoutsList.size(); i++) {

            LinearLayout linearLayout = linearLayoutsList.get(i);


            //date
            if (linearLayout.findViewById(R.id.ll_date_formbuilder) != null) {

                LinearLayout layout = linearLayout.findViewById(R.id.ll_date_formbuilder);
                EditText editText = layout.findViewById(R.id.et_heading_date_formbuilder);
                editText.getText().clear();


            } else if (linearLayout.findViewById(R.id.ll_text_formbuilder) != null) {

                LinearLayout layout = linearLayout.findViewById(R.id.ll_text_formbuilder);
                EditText editText = layout.findViewById(R.id.et_text_formbuilder);
                editText.getText().clear();


            } else if (linearLayout.findViewById(R.id.ll_edittext_formbuilder) != null) {

                LinearLayout layout = linearLayout.findViewById(R.id.ll_edittext_formbuilder);
                EditText editText = layout.findViewById(R.id.et_edittext_formbuilder);
                editText.getText().clear();


            }

        }

        for (int j = 0; j < addMoreViewList.size(); j++) {


            CardView card = (CardView) addMoreViewList.get(j);
            EditText etRupesDynamic = card.findViewById(R.id.et_intiateworkflow_rupee_dynamic);
            EditText etPaisaDynamic = card.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
            EditText etDescDynamic = card.findViewById(R.id.et_intiateworkflow_desc_dynamic);

            etRupesDynamic.getText().clear();
            etPaisaDynamic.getText().clear();
            etDescDynamic.getText().clear();


        }


    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(IntiateWorkFlowActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
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


}