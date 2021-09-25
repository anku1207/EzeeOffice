package in.cbslgroup.ezeeoffice.Activity.WorkFlow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.CommentListAdapter;
import in.cbslgroup.ezeeoffice.Model.CommentTaskApproveReject;
import in.cbslgroup.ezeeoffice.Notifications.TaskApproveRejectService;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.ConnectivityReceiver;
import in.cbslgroup.ezeeoffice.Utils.CustomDateTimePicker;
import in.cbslgroup.ezeeoffice.Utils.NotificationActions;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AprrovedRejectTaskActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String TAG = "photo";

    String imagepath = "";
    String fname;
    File file;
    AlertDialog alertDialog;
    String filePath;
    Button btnChoose;
    TextView tvPath;
    ImageView ivFileType, ivCameraImg;
    CardView cvCameraImg;

    List<String> DataList = new ArrayList<>();

    List<String> assignUserList = new ArrayList<>();
    List<String> alternativeUserList = new ArrayList<>();
    List<String> selectSupervisiorList = new ArrayList<>();

    List<String> assignUserDynamicList = new ArrayList<>();
    List<String> alternativeDynamicUserList = new ArrayList<>();
    List<String> selectSupervisiorDynamicList = new ArrayList<>();

    String dateBetween;

    Spinner spAssignUser, spAlternativeUser, spSelectSupervisor;
    Spinner spAssignUserDynamic, spAlternativeUserDynamic, spSelectSupervisorDynamic;

    BottomSheetDialog bottomSheetDialog;

    TextView tvAdduser, tvtaskorder;

    LinearLayout llAdduser;
    LinearLayout llDateBetween;
    LinearLayout llEtHrs;
    LinearLayout llEtDays;

    ArrayAdapter<String> assignUserListAdapter;
    ArrayAdapter<String> alternateUserListAdapter;
    ArrayAdapter<String> selectSupervisiorListAdapter;


    ImageView ivAddUser;
    RadioButton rbProcessed, rbApproved, rbRejected, rbAborted, rbComplete, rbDone, rbDays, rbHours, rbDate, rbDaysDynamic, rbHoursDynamic, rbDateDynamic;
    RadioGroup rgTaskAction, rgDeadline, rgDeadlineDyanmic;
    CardView cvAddUser, cvAddUserDynamic;
    EditText etDeadlineHrs, etDeadlineDays, etDeadlineDynamic;
    ImageView ivcalender, ivcalenderDynamic;
    String taskid, taskname;
    LinearLayout llAdduserDynamic;
    boolean orderPresent = false;
    RecyclerView rvComments;
    List<CommentTaskApproveReject> commentList = new ArrayList<>();
    AprrovedRejectTaskActivity ActivityName = AprrovedRejectTaskActivity.this;
    MultipartUploadRequest multipartUploadRequest;
    EditText etComment;
    Button btnSubmit;

    String assigneduser = "null", alternateuser = "null", supervisor = "null";
    String assigneduserAdd = "null", alternateuserAdd = "null", supervisorAdd = "null";
    String deadline = null, deadlineType = null;
    String comment = "null", date = "null";
    String taskstatus, taskorder = "1";
    String userid = MainActivity.userid;
    String username = MainActivity.username;
    String ip = MainActivity.ip;


    AlertDialog alertProcessing;
    //hidden textviews for userid
    TextView tvHiddenAssignedUserId, tvHiddenAlterNativeUserId, tvHiddenSupervisorUserId;
    TextView tvHiddenAssignedUserIdDyanmic, tvHiddenAlterNativeUserIdDyanmic, tvHiddenSupervisorUserIdDyanmic;
    String assignUser, alterUser, sprvisor;
    String assignUserDynamic, alterUserDynamic, sprvisorDynamic;
    CustomDateTimePicker dateTimePickerStart, dateTimePickerEnd;
    EditText etStartDateTime, etEndDateTime;
    String deadlineHrs, deadlineDays;
    String startDate, endDate;
    Boolean ok = false;
    Boolean uploading = false;
    CheckBox checkBoxFileUpload;
    LinearLayout llFileUpload;
    ProgressBar pgMain;
    ScrollView svMain;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String assignedDynamicUserId = null, alternateuserDynamicUserId = null, supervisorDynamicUserId = null;
    String assignedUserId = null, alternateuserUserId = null, supervisorUserId = null;
    BroadcastReceiver broadcastReceiverClosePB;
    private ImageView ivTakePic, ivChooseFile;
    // private ConnectionDetector cd;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private Uri mCropImageUri;
    private int mYear, mMonth, mDay;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprroved_reject_task);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        broadcastReceiverClosePB = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                if ((getPackageName() + ".CLOSE_APPROVE_REJECT_PB").equalsIgnoreCase(arg1.getAction())) {

                    //Toast.makeText(arg0, "Yes it is working", Toast.LENGTH_LONG).show();

                    String msg = arg1.getStringExtra("msg");
                    String error = arg1.getStringExtra("error");

                    alertProcessing("off", arg0);

                    if (error.equalsIgnoreCase("false")) {

                        alertSuccess(msg, arg0);

                    } else if (error.equalsIgnoreCase("true")) {

                        alertError(msg, arg0);

                    }

                }


            }
        };

        registerReceiver(broadcastReceiverClosePB, new IntentFilter(getPackageName() + ".CLOSE_APPROVE_REJECT_PB"));
        requestPermission();

        Toolbar toolbar = findViewById(R.id.toolbar_task_approve_reject_tracking);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AprrovedRejectTaskActivity.this, InTrayActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        btnChoose = findViewById(R.id.btn_approve_reject_task_upload_choose_file);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheetDialog();
                //showChooseDialog();

            }
        });

        Intent intent2 = getIntent();
        taskid = intent2.getStringExtra("taskid");
        taskname = intent2.getStringExtra("taskname");

        toolbar.setTitle("Taskname : " + taskname);

        //intialize

        pgMain = findViewById(R.id.pg_task_approve_reject);
        svMain = findViewById(R.id.scrollview_main_approve_reject);

        tvPath = findViewById(R.id.tv_approve_reject_task_upload_filepath);
        ivFileType = findViewById(R.id.iv_approve_reject_task_upload_filetype);
        ivCameraImg = findViewById(R.id.iv_approve_reject_task_upload_camera);
        btnSubmit = findViewById(R.id.btn_approve_reject_task_submit);

        llEtHrs = findViewById(R.id.ll_et_hrs_days);
        llDateBetween = findViewById(R.id.ll_aproved_reject_date_between);

        etStartDateTime = findViewById(R.id.et_approve_reject_task_deadline_start);
        etEndDateTime = findViewById(R.id.et_approve_reject_task_deadline_end);


        tvHiddenAssignedUserId = findViewById(R.id.tv_sp_approve_reject_task_assign_user);
        tvHiddenAlterNativeUserId = findViewById(R.id.tv_sp_approve_reject_task_alternative_user);
        tvHiddenSupervisorUserId = findViewById(R.id.tv_sp_approve_reject_task_select_supervisior);


        tvHiddenAssignedUserIdDyanmic = findViewById(R.id.tv_sp_approve_reject_task_assign_user_dynamic);
        tvHiddenAlterNativeUserIdDyanmic = findViewById(R.id.tv_sp_approve_reject_task_alternative_user_dynamic);
        tvHiddenSupervisorUserIdDyanmic = findViewById(R.id.tv_sp_approve_reject_task_select_supervisior_dynamic);

        spAssignUser = findViewById(R.id.sp_approve_reject_task_assign_user);
        spAlternativeUser = findViewById(R.id.sp_approve_reject_task_alternative_user);
        spSelectSupervisor = findViewById(R.id.sp_approve_reject_task_select_supervisior);


        spAssignUserDynamic = findViewById(R.id.sp_approve_reject_task_assign_user_dynamic);
        spAlternativeUserDynamic = findViewById(R.id.sp_approve_reject_task_alternative_user_dynamic);
        spSelectSupervisorDynamic = findViewById(R.id.sp_approve_reject_task_select_supervisior_dynamic);

        tvAdduser = findViewById(R.id.tv_approve_reject_task_add_user);
        tvtaskorder = findViewById(R.id.tv_approve_reject_task_order);
        llAdduser = findViewById(R.id.ll_aproved_reject_add_user);

        llAdduserDynamic = findViewById(R.id.ll_aproved_reject_add_user_dynamic);
        ivAddUser = findViewById(R.id.iv_approve_reject_task_add_user);

        cvAddUser = findViewById(R.id.cv_approve_reject_task_adduser);
        cvAddUserDynamic = findViewById(R.id.cv_approve_reject_task_adduser_dynamic);

        rbApproved = findViewById(R.id.rb_approve_reject_approved);
        rbRejected = findViewById(R.id.rb_approve_reject_rejected);
        rbAborted = findViewById(R.id.rb_approve_reject_aborted);
        rbComplete = findViewById(R.id.rb_approve_reject_complete);
        rbDone = findViewById(R.id.rb_approve_reject_done);
        rbProcessed = findViewById(R.id.rb_approve_reject_processed);

        rbDate = findViewById(R.id.rb_approve_reject_date);
        rbDays = findViewById(R.id.rb_approve_reject_days);
        rbHours = findViewById(R.id.rb_approve_reject_hours);

        rbDateDynamic = findViewById(R.id.rb_approve_reject_date_dynamic);
        rbDaysDynamic = findViewById(R.id.rb_approve_reject_days_dynamic);
        rbHoursDynamic = findViewById(R.id.rb_approve_reject_hours_dynamic);

        rgTaskAction = findViewById(R.id.rg_task_action);
        rgDeadline = findViewById(R.id.rg_deadline);

        rgDeadlineDyanmic = findViewById(R.id.rg_deadline_dynamic);

        //ivcalender = findViewById(R.id.iv_approve_reject_task_calender);
        ivcalenderDynamic = findViewById(R.id.iv_approve_reject_task_calender_dynamic);

        etComment = findViewById(R.id.et_comment_task_approve_reject_comment);

        checkBoxFileUpload = findViewById(R.id.checkbox_approve_reject_fileupload);

        llFileUpload = findViewById(R.id.ll_approve_reject_task_upload_file_upload);
        checkBoxFileUpload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {


                    llFileUpload.setVisibility(View.VISIBLE);
                    btnSubmit.setEnabled(false);

                    resetFileUploadWindow();


                } else {


                    llFileUpload.setVisibility(View.GONE);
                    btnSubmit.setEnabled(true);

                    resetFileUploadWindow();

                }

            }


        });


        etStartDateTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etStartDateTime.getRight() - etStartDateTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        dateTimePickerStart.showDialog();


                        return true;
                    }
                }
                return false;
            }
        });


        etEndDateTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etEndDateTime.getRight() - etEndDateTime.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        dateTimePickerEnd.showDialog();


                        return true;
                    }
                }
                return false;
            }
        });


        //recyclerview
        rvComments = findViewById(R.id.rv_task_approve_reject_comments);
        rvComments.setItemViewCacheSize(commentList.size());
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setHasFixedSize(true);
        rvComments.hasFixedSize();


        ivcalenderDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(AprrovedRejectTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        if (month <= 10) {

                            etDeadlineDynamic.setText(year + "-" + "0" + month + "-" + dayOfMonth);


                        } else {

                            etDeadlineDynamic.setText(year + "-" + month + "-" + dayOfMonth);

                        }

                        if (dayOfMonth <= 10) {

                            etDeadlineDynamic.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);

                        } else {

                            etDeadlineDynamic.setText(year + "-" + "0" + month + "-" + dayOfMonth);
                        }


                    }
                }, mYear, mMonth, mDay).show();

            }
        });


        dateTimePickerStart = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {

                etStartDateTime.setText("");

                String Month = "";

                if (monthNumber + 1 <= 9) {

                    Month = "0" + (monthNumber + 1);

                } else {

                    Month = String.valueOf(monthNumber + 1);
                }

                etStartDateTime.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                        + "-" + Month + "-" + year
                        + " " + hour12 + ":" + min
                        + " " + AM_PM);

            }

            @Override
            public void onCancel() {


            }
        });

        dateTimePickerStart.set24HourFormat(true);
        dateTimePickerStart.setDate(Calendar.getInstance());


        dateTimePickerEnd = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {

                etEndDateTime.setText("");

                String Month = "";

                if (monthNumber + 1 <= 9) {

                    Month = "0" + (monthNumber + 1);

                } else {

                    Month = String.valueOf(monthNumber + 1);
                }


                etEndDateTime.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                        + "-" + Month + "-" + year
                        + " " + hour12 + ":" + min
                        + " " + AM_PM);


            }

            @Override
            public void onCancel() {


            }
        });

        dateTimePickerEnd.set24HourFormat(true);
        dateTimePickerEnd.setDate(Calendar.getInstance());







/*
        ivcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 custom.showDialog();


                */
/*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityName);
                LayoutInflater inflater = (LayoutInflater) ActivityName.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.custom_date_time_picker, null);

                 final EditText etDateTime = dialogView.findViewById(R.id.et_custom_datepicker);

                 ImageView ivTimePicker = dialogView.findViewById(R.id.iv_custom_datepicker_time);
                 ivTimePicker.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         // TODO Auto-generated method stub
                         Calendar mcurrentTime = Calendar.getInstance();
                         int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                         int minute = mcurrentTime.get(Calendar.MINUTE);
                         TimePickerDialog timePicker;

                         timePicker = new TimePickerDialog(ActivityName, new TimePickerDialog.OnTimeSetListener() {
                             @Override
                             public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                 etDateTime.setText( selectedHour + ":" + selectedMinute);
                             }
                         }, hour, minute, true);//Yes 24 hour time
                         timePicker.show();
                     }
                 });


                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

*//*





         */
/*  // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(AprrovedRejectTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        if (month <= 10) {

                            etDeadline.setText(year + "-" + "0" + month + "-" + dayOfMonth);


                        } else {

                            etDeadline.setText(year + "-" + month + "-" + dayOfMonth);

                        }

                        if (dayOfMonth <= 10) {

                            etDeadline.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);

                        } else {

                            etDeadline.setText(year + "-" + "0" + month + "-" + dayOfMonth);
                        }


                    }
                }, mYear, mMonth, mDay).show();
*//*




            }
        });

*/

        etDeadlineHrs = findViewById(R.id.et_approve_reject_task_deadline_hrs);
        etDeadlineDays = findViewById(R.id.et_approve_reject_task_deadline_days);


        rgDeadlineDyanmic.clearCheck();
        rgDeadlineDyanmic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {

                    String label = rb.getText().toString();

                    if (label.equalsIgnoreCase("Date")) {

                        ivcalenderDynamic.setVisibility(View.VISIBLE);


                    } else if (label.equalsIgnoreCase("Hours")) {


                        ivcalenderDynamic.setVisibility(View.GONE);

                    } else if (label.equalsIgnoreCase("Days")) {


                    }

                }

            }
        });


        rgDeadline.clearCheck();
        rgDeadline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {

                    String label = rb.getText().toString();

                    if (label.equalsIgnoreCase("Date")) {

                        //ivcalender.setVisibility(View.VISIBLE);

                        llDateBetween.setVisibility(View.VISIBLE);
                        etDeadlineHrs.setVisibility(View.GONE);
                        etDeadlineDays.setVisibility(View.GONE);


                    } else if (label.equalsIgnoreCase("Hours")) {


                        llDateBetween.setVisibility(View.GONE);
                        etDeadlineHrs.setVisibility(View.VISIBLE);
                        etDeadlineDays.setVisibility(View.GONE);


                        // ivcalender.setVisibility(View.GONE);

                    } else if (label.equalsIgnoreCase("Days")) {

                        llDateBetween.setVisibility(View.GONE);
                        etDeadlineHrs.setVisibility(View.GONE);
                        etDeadlineDays.setVisibility(View.VISIBLE);


                    }

                }

            }
        });


        rgTaskAction.clearCheck();
        rgTaskAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    String label = rb.getText().toString();

                    if (label.equalsIgnoreCase("Processed") || label.equalsIgnoreCase("Approved")) {

                        cvAddUser.setVisibility(View.VISIBLE);

                        if (orderPresent) {

                            cvAddUserDynamic.setVisibility(View.VISIBLE);

                        }


                    } else if (label.equalsIgnoreCase("Rejected") || label.equalsIgnoreCase("Aborted") || label.equalsIgnoreCase("Complete") || label.equalsIgnoreCase("Done")) {


                        cvAddUser.setVisibility(View.GONE);
                        cvAddUserDynamic.setVisibility(View.GONE);


                    }

                    //  Toast.makeText(AprrovedRejectTaskActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }


            }
        });


        ivAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (llAdduser.getVisibility() == View.VISIBLE) {

                    llAdduser.setVisibility(View.GONE);
                    ivAddUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_box_white_24dp));

                } else {


                    llAdduser.setVisibility(View.VISIBLE);
                    ivAddUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_white_24dp));

                }


            }

        });


        ivCameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(mCurrentPhotoPath);

                Log.e("uri", uri.toString());

                startCropImageActivity(uri);

            }
        });


        cvCameraImg = findViewById(R.id.cv_approve_reject_task_upload_camera);

        mImageView = findViewById(R.id.iv_aproved_reject_take_photo);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /* Intent cameraintent = new Intent(
                       android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(cameraintent, 101);*/

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        Log.e("Create Image", "Succesfully");
                    } catch (IOException ex) {

                        Log.i(TAG, "IOException");
                    }

                    if (photoFile != null) {

                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

                    }
                }

            }
        });


        //get all group members
        getGroupMembers(MainActivity.userid);

        //get popup data
        getTaskApproved(MainActivity.userid, taskid);

        //get Comments
        getComments(MainActivity.userid, taskid);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* File file = new File(filePath);
                String filename = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());*/


                if (rgTaskAction.getCheckedRadioButtonId() == -1) {

                    Toast.makeText(ActivityName, "Please Select Task Action", Toast.LENGTH_SHORT).show();


                } else {

                    deadlineHrs = etDeadlineHrs.getText().toString().trim();

                    if (etDeadlineHrs.getVisibility() == View.VISIBLE) {

                        if (etDeadlineHrs.getText().toString().trim().length() == 0) {


                            ok = false;
                            etDeadlineHrs.setError("Please select hours for deadline");

                        } else {


                            ok = true;
                            etDeadlineHrs.setError(null);

                        }

                    }


                    deadlineDays = etDeadlineDays.getText().toString().trim();

                    if (etDeadlineDays.getVisibility() == View.VISIBLE) {

                        if (etDeadlineDays.getText().toString().trim().length() == 0) {


                            ok = false;
                            etDeadlineDays.setError("Please select days for deadline");

                        } else {

                            ok = true;
                            etDeadlineDays.setError(null);
                        }

                    }


                    startDate = etStartDateTime.getText().toString().trim();
                    endDate = etEndDateTime.getText().toString().trim();

                    if (deadlineHrs.length() == 0 || TextUtils.isEmpty(deadlineHrs) || deadlineHrs.equalsIgnoreCase("")) {

                        deadlineHrs = "null";

                    }

                    if (deadlineDays.length() == 0 || TextUtils.isEmpty(deadlineDays) || deadlineDays.equalsIgnoreCase("")) {

                        deadlineDays = "null";

                    }


                    deadlineType = getDeadlineCheckboxData();
                    taskstatus = getCheckboxData();
                    comment = etComment.getText().toString();


                    if (llAdduser.getVisibility() == View.VISIBLE) {

                        if (spAssignUser.getSelectedItem() != null && spAlternativeUser != null && spSelectSupervisor != null) {

                            assigneduser = spAssignUser.getSelectedItem().toString();
                            alternateuser = spAlternativeUser.getSelectedItem().toString();
                            supervisor = spSelectSupervisor.getSelectedItem().toString();

                            Log.e("assigneduser static", assigneduser);
                            Log.e("alternateuser static", alternateuser);
                            Log.e("supervisorstatic", supervisor);


                            if (assigneduser.equalsIgnoreCase("Select Assign User")) {

                                TextView errorText = (TextView) spAssignUser.getSelectedView();
                                errorText.setError("Please Select Assign User");
                                errorText.setTextColor(Color.RED);
                                errorText.requestFocus();
                                // errorText.setText("my actual error text");
                                ok = false;

                            }


                            //Todo here there is to code as the alternate user is not required now

//                            if (alternateuser.equalsIgnoreCase("Select Alternate User")) {
//
//                                TextView errorText = (TextView) spAlternativeUser.getSelectedView();
//                                errorText.setError("Please Select Alternate User");
//                                errorText.setTextColor(Color.RED); //just to highlight that this is an error
//                                // errorText.setText("my actual error text");
//                                errorText.requestFocus();
//
//                                ok = false;
//
//                            }

                            if (supervisor.equalsIgnoreCase("Select Supervisor")) {

                                TextView errorText = (TextView) spSelectSupervisor.getSelectedView();
                                errorText.setError("Please select supervisor");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                // errorText.setText("my actual error text");
                                errorText.requestFocus();

                                ok = false;

                            }

                            if (assigneduser.equalsIgnoreCase(MainActivity.username)) {

                                assignedUserId = MainActivity.userid;

                            } else {

                                assignedUserId = searchUserid(assigneduser);
                            }


                            if (alternateuser.equalsIgnoreCase(MainActivity.username)) {

                                alternateuserUserId = MainActivity.userid;

                            }

                            /////
                            else if (alternateuser.equalsIgnoreCase("Select Alternate User")) {

                                alternateuserUserId = "null";

                            } else {

                                alternateuserUserId = searchUserid(alternateuser);

                            }


                            if (supervisor.equalsIgnoreCase(MainActivity.username)) {


                                supervisorUserId = MainActivity.userid;


                            } else {

                                supervisorUserId = searchUserid(supervisor);

                            }





                           /* assignedUserId = searchUserid(assigneduser);
                            alternateuserUserId = searchUserid(alternateuser);
                            supervisorUserId = searchUserid(supervisor);
*/

                            if (llDateBetween.getVisibility() == View.VISIBLE) {

                                if (TextUtils.isEmpty(startDate) || startDate.length() == 0) {

                                    dateBetween = "null";

                                    etStartDateTime.setError("Please select start date");

                                    ok = false;

                                } else if (endDate.length() == 0 || TextUtils.isEmpty(endDate)) {


                                    dateBetween = "null";

                                    etEndDateTime.setError("Please select end date");

                                    ok = false;

                                } else {


                                    ok = true;
                                    dateBetween = etStartDateTime.getText().toString().trim() + " " + "To" + " " + etEndDateTime.getText().toString().trim();

                                }

                                Log.e("datebetween", dateBetween);

                            }


                        }


                    } else {

                        ok = true;
                        assignedUserId = "null";
                        alternateuserUserId = "null";
                        supervisorUserId = "null";
                        dateBetween = "null";
                        Log.e("datebetween", dateBetween);


                    }


                    if (spAssignUserDynamic.getSelectedItem() != null && spAlternativeUserDynamic != null && spSelectSupervisorDynamic != null) {

                        String assigneduserDynamic = spAssignUserDynamic.getSelectedItem().toString();
                        String alternateuserDynamic = spAlternativeUserDynamic.getSelectedItem().toString();
                        String supervisorDynamic = spSelectSupervisorDynamic.getSelectedItem().toString();

                        Log.e("assigneduser dyna", assigneduserDynamic);
                        Log.e("alternateuser dyna", alternateuserDynamic);
                        Log.e("supervisor dyna", supervisorDynamic);


                        if (assigneduserDynamic.equalsIgnoreCase("Select Assign User")) {

                            TextView errorText = (TextView) spAssignUserDynamic.getSelectedView();
                            errorText.setError("Please Select Assign User");
                            errorText.setTextColor(Color.RED);

                            // errorText.setText("my actual error text");
                            ok = false;

                        }

                        //Todo here there is to code as the alternate user is not required now


                      /* else if (alternateuserDynamic.equalsIgnoreCase("Select Alternate User")) {

                            TextView errorText = (TextView) spAlternativeUserDynamic.getSelectedView();
                            errorText.setError("Please Select Alternate User");
                            errorText.setTextColor(Color.RED); //just to highlight that this is an error
                            // errorText.setText("my actual error text");

                            ok = false;

                        }*/

                        else if (alternateuserDynamic.equalsIgnoreCase("Select Supervisor")) {

                            TextView errorText = (TextView) spSelectSupervisorDynamic.getSelectedView();
                            errorText.setError("Please select supervisor");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            // errorText.setText("my actual error text");

                            ok = false;

                        } else {


                            if (assigneduserDynamic.equalsIgnoreCase(MainActivity.username)) {

                                assignedDynamicUserId = MainActivity.userid;

                            } else {

                                assignedDynamicUserId = searchUserid(assigneduserDynamic);
                            }


                            if (alternateuserDynamic.equalsIgnoreCase(MainActivity.username)) {

                                alternateuserDynamicUserId = MainActivity.userid;

                            } else if (alternateuserDynamic.equalsIgnoreCase("Select Alternate User")) {


                                alternateuserDynamicUserId = "null";


                            } else {

                                alternateuserDynamicUserId = searchUserid(alternateuserDynamic);

                            }


                            if (supervisorDynamic.equalsIgnoreCase(MainActivity.username)) {


                                supervisorDynamicUserId = MainActivity.userid;


                            } else {

                                supervisorDynamicUserId = searchUserid(supervisorDynamic);

                            }


                        }


                        Log.e("userid assigned", assignedDynamicUserId);
                        Log.e("userid alternative", alternateuserDynamicUserId);
                        Log.e("userid supervisor", supervisorDynamicUserId);


                    } else {


                        Log.e("spiner", "last order");

                        assignedDynamicUserId = "null";
                        alternateuserDynamicUserId = "null";
                        supervisorDynamicUserId = "null";

                    }


//                     if(ok && uploading){
//
//
//                         ApproveRejectTaskwithOutUploading(
//
//                                 comment,
//                                 taskstatus, taskid,
//                                 userid, username,
//                                 assignedDynamicUserId, taskorder, alternateuserDynamicUserId,
//                                 supervisorDynamicUserId, assignedUserId, alternateuserUserId,
//                                 supervisorUserId, deadlineType,
//                                 dateBetween, deadlineDays, deadlineHrs, ip,"0"
//
//
//                         );
//
//                     }

                    Boolean checkFileUpload = checkBoxFileUpload.isChecked();

                    if (ok && checkFileUpload) {

                        alertProcessing("on", AprrovedRejectTaskActivity.this);

                        Intent apRjWoutUp = new Intent(AprrovedRejectTaskActivity.this, TaskApproveRejectService.class);
                        apRjWoutUp.putExtra("method", "with_upload");
                        apRjWoutUp.putExtra("filepath", filePath);
                        apRjWoutUp.putExtra("comment", comment);
                        apRjWoutUp.putExtra("taskstatus", taskstatus);
                        apRjWoutUp.putExtra("taskid", taskid);
                        apRjWoutUp.putExtra("taskname", taskname);
                        apRjWoutUp.putExtra("userid", userid);
                        apRjWoutUp.putExtra("username", MainActivity.username);
                        apRjWoutUp.putExtra("assignedDynamicUserId", assignedDynamicUserId);
                        apRjWoutUp.putExtra("taskorder", taskorder);
                        apRjWoutUp.putExtra("alternateuserDynamicUserId", alternateuserDynamicUserId);
                        apRjWoutUp.putExtra("supervisorDynamicUserId", supervisorDynamicUserId);
                        apRjWoutUp.putExtra("assignedUserId", assignedUserId);
                        apRjWoutUp.putExtra("alternateuserUserId", alternateuserUserId);
                        apRjWoutUp.putExtra("supervisorUserId", supervisorUserId);
                        apRjWoutUp.putExtra("deadlineType", deadlineType);
                        apRjWoutUp.putExtra("dateBetween", dateBetween);
                        apRjWoutUp.putExtra("deadlineDays", deadlineDays);
                        apRjWoutUp.putExtra("deadlineHrs", deadlineHrs);
                        apRjWoutUp.putExtra("ip", ip);
                        startService(apRjWoutUp);


//                        ApproveRejectTaskWithUploading(
//                                filePath,
//                                comment,
//                                taskstatus, taskid,
//                                userid, username,
//                                assignedDynamicUserId, taskorder, alternateuserDynamicUserId,
//                                supervisorDynamicUserId, assignedUserId, alternateuserUserId,
//                                supervisorUserId, deadlineType,
//                                dateBetween, deadlineDays, deadlineHrs, ip, "1"
//
//
//                        );


                    } else if (ok && !checkFileUpload) {


                        alertProcessing("on", AprrovedRejectTaskActivity.this);

                        Intent apRjWoutUp = new Intent(AprrovedRejectTaskActivity.this, TaskApproveRejectService.class);
                        apRjWoutUp.putExtra("method", "without_upload");
                        apRjWoutUp.putExtra("comment", comment);
                        apRjWoutUp.putExtra("taskstatus", taskstatus);
                        apRjWoutUp.putExtra("userid", userid);
                        apRjWoutUp.putExtra("username", MainActivity.username);
                        apRjWoutUp.putExtra("assignedDynamicUserId", assignedDynamicUserId);
                        apRjWoutUp.putExtra("taskorder", taskorder);
                        apRjWoutUp.putExtra("taskid", taskid);
                        apRjWoutUp.putExtra("taskname", taskname);
                        apRjWoutUp.putExtra("alternateuserDynamicUserId", alternateuserDynamicUserId);
                        apRjWoutUp.putExtra("supervisorDynamicUserId", supervisorDynamicUserId);
                        apRjWoutUp.putExtra("assignedUserId", assignedUserId);
                        apRjWoutUp.putExtra("alternateuserUserId", alternateuserUserId);
                        apRjWoutUp.putExtra("supervisorUserId", supervisorUserId);
                        apRjWoutUp.putExtra("deadlineType", deadlineType);
                        apRjWoutUp.putExtra("dateBetween", dateBetween);
                        apRjWoutUp.putExtra("deadlineDays", deadlineDays);
                        apRjWoutUp.putExtra("deadlineHrs", deadlineHrs);
                        apRjWoutUp.putExtra("ip", ip);
                        startService(apRjWoutUp);

//                        ApproveRejectTaskwithOutUploading(
//                                comment,
//                                taskstatus, taskid,
//                                userid, username,
//                                assignedDynamicUserId, taskorder, alternateuserDynamicUserId,
//                                supervisorDynamicUserId, assignedUserId, alternateuserUserId,
//                                supervisorUserId, deadlineType,
//                                dateBetween, deadlineDays, deadlineHrs, ip, "1"
//
//                        );

                    }


                }


            }
        });

        //without uploading

       /* withOutUploading("null",
                "Approved", "1003", "54", "1004", "Ankit", "3",
                "deven", "mayank", "2", "3", "3", "5", "f", "f", "e"
        );*/

    /* uploadMultipart("null","hey",
             "Approved", "1003", "54", "1004", "Ankit", "3",
             "deven", "mayank", "2", "3", "3", "5", "f", "f", "e");*/





       /*



       ivChooseFile = findViewById(R.id.iv_aproved_reject_choose_file);
       ivChooseFile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //saveFile(bitmap,file);

               String compress =  compressImage(mCurrentPhotoPath);

               Log.e("compress",compress);


           }
       });
*/


    }

    public void onClear(View v) {
        rgTaskAction.clearCheck();
    }

    public String getCheckboxData() {

        RadioButton rb = rgTaskAction.findViewById(rgTaskAction.getCheckedRadioButtonId());

        String radioValue = null;
        if (rb != null) {

            radioValue = rb.getText().toString().trim();

        } else {


            Toast.makeText(ActivityName, "Please select Task Action", Toast.LENGTH_SHORT).show();
        }

        return radioValue;
    }

    public String getDeadlineCheckboxData() {

        RadioButton rb = rgDeadline.findViewById(rgDeadline.getCheckedRadioButtonId());

        String radioValue = null;
        if (rb != null) {

            radioValue = rb.getText().toString().trim();

        } else {

            if (llAdduser.getVisibility() == View.VISIBLE) {

                Toast.makeText(ActivityName, "Please select Deadline", Toast.LENGTH_SHORT).show();
            }


        }

        return radioValue;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {

                cvCameraImg.setVisibility(View.VISIBLE);
                ivFileType.setVisibility(View.GONE);


                //alertDialog.dismiss();
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                //mImageView.setImageBitmap(mImageBitmap);
                filePath = mCurrentPhotoPath;


                Uri uri = Uri.parse(filePath);

                Log.e("uri", uri.toString());

                startCropImageActivity(uri);

                String p = filePath.substring(filePath.lastIndexOf("/") + 1);

                tvPath.setText(p);
                btnSubmit.setEnabled(true);


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 404 && resultCode == RESULT_OK) {

            // alertDialog.dismiss();

            cvCameraImg.setVisibility(View.GONE);
            ivFileType.setVisibility(View.VISIBLE);


            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String p = filePath.substring(filePath.lastIndexOf("/") + 1);

            String filetype = p.substring(p.lastIndexOf(".") + 1);

            switch (filetype) {

                case "pdf":

                    ivFileType.setImageResource(R.drawable.pdf);
                    break;

                case "jpg":

                    ivFileType.setImageResource(R.drawable.jpg);
                    break;

                case "png":

                    ivFileType.setImageResource(R.drawable.png);
                    break;

                case "gif":
                    ivFileType.setImageResource(R.drawable.gif);
                    //holder.ivOpenFile.setImageResource(R.drawable.png);

                    break;
                case "jpeg":

                    ivFileType.setImageResource(R.drawable.jpeg);
                    break;

                case "tiff":

                    ivFileType.setImageResource(R.drawable.tiff);
                    break;

                case "tif":
                    ivFileType.setImageResource(R.drawable.tif);
                    break;

                case "doc":

                    ivFileType.setImageResource(R.drawable.doc);
                    break;

                case "txt":

                    ivFileType.setImageResource(R.drawable.unsupported_file);
                    break;

                case "docx":
                    ivFileType.setImageResource(R.drawable.docx);
                    break;


                case "psd":
                    ivFileType.setImageResource(R.drawable.psd);
                    break;

                case "mp4":

                    ivFileType.setImageResource(R.drawable.mp4);
                    break;


                case "3gp":
                    ivFileType.setImageResource(R.drawable.threegp);
                    break;

                case "mp3":
                    ivFileType.setImageResource(R.drawable.mp3);
                    break;

                default:

                    ivFileType.setImageResource(R.drawable.unsupported_file);
                    break;


            }


            tvPath.setText(p);
            Log.e("filepath in activity", filePath);
            btnSubmit.setEnabled(true);


        }

      /*  // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity

                Log.e("mCropImageUri", String.valueOf(imageUri));

                String filepath = imageUri.toString().substring(imageUri.toString().lastIndexOf("/")+1,imageUri.toString().length());

                String filetype = filepath.substring(filepath.lastIndexOf(".")+1,filepath.length());

                Log.e("filetype",filetype);


             *//*   if(     filetype.equalsIgnoreCase("jpeg") ||
                        filetype.equalsIgnoreCase("jpg")||
                        filetype.equalsIgnoreCase("png")||
                        filetype.equalsIgnoreCase("bmp")||
                        filetype.equalsIgnoreCase("tiff")
                        ){

                    startCropImageActivity(imageUri);

                    Log.e("mCropImageUri", String.valueOf(imageUri));

                }

                else{

                    Toast.makeText(this, "File Not Supported", Toast.LENGTH_SHORT).show();


                }*//*

                startCropImageActivity(imageUri);

                Log.e("mCropImageUri", String.valueOf(imageUri));



            }
        }*/

        // handle result of CropImageActivity
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                cvCameraImg.setVisibility(View.VISIBLE);
                ivCameraImg.setImageURI(result.getUri());

                Log.e("image uri", result.getUri().toString());

                String uri = result.getUri().toString();
                String path = uri.substring(7);

                Log.e("path", path);

                filePath = path;

                btnSubmit.setEnabled(true);

                // ivCameraImg.setBackground(getResources().getDrawable(R.drawable.bg_take_pic));

                //((ImageView) findViewById(R.id.iv_crop_test)).setImageURI(result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }


    }


    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)

                .setActivityMenuIconColor(getResources().getColor(R.color.white))
                .setAllowRotation(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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

    void showChooseDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AprrovedRejectTaskActivity.this);

        LayoutInflater inflater = AprrovedRejectTaskActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_click_pic_or_choose_file, null);

        LinearLayout llCamera = dialogView.findViewById(R.id.ll_aproved_reject_take_photo);
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

                //onSelectImageClick(v);


            }
        });
        LinearLayout llFile = dialogView.findViewById(R.id.ll_aproved_reject_choose_file);
        llFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialFilePicker()
                        .withActivity(AprrovedRejectTaskActivity.this)
                        .withRequestCode(404)
                        .withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_ad_choose_pic_file_cancel);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }

    void getTaskApproved(final String userid, final String taskid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("ApprovedRejectPopUp", response);


                String assignedUser = null, alternateUser = null, superVisor = null;


                JSONArray actionArray;


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray taskinfoArray = jsonObject.getJSONArray("task_info");
                    JSONArray userlistArray = jsonObject.getJSONArray("userlist");
                    JSONArray actinArray = jsonObject.getJSONArray("actions");


                    for (int j = 0; j < actinArray.length(); j++) {

                        Log.e("array size", String.valueOf(actinArray.length()));

                        String taskStatus = actinArray.get(j).toString();
                        Log.e("taskstatus", taskStatus);

                        if (taskStatus.equalsIgnoreCase("Processed")) {

                            rbProcessed.setVisibility(View.VISIBLE);

                        } else if (taskStatus.equalsIgnoreCase("Approved")) {

                            rbApproved.setVisibility(View.VISIBLE);

                        } else if (taskStatus.equalsIgnoreCase("Rejected")) {


                            rbRejected.setVisibility(View.VISIBLE);

                        } else if (taskStatus.equalsIgnoreCase("Aborted")) {

                            rbAborted.setVisibility(View.VISIBLE);

                        } else if (taskStatus.equalsIgnoreCase("Complete")) {

                            rbComplete.setVisibility(View.VISIBLE);

                        } else if (taskStatus.equalsIgnoreCase("Done")) {

                            rbDone.setVisibility(View.VISIBLE);

                        }


                    }


                    for (int k = 0; k < userlistArray.length(); k++) {

                        //  String userid = userlistArray.getJSONObject(k).getString("user_id");
                        String username = userlistArray.getJSONObject(k).getString("user_name");
                        String names = username.substring(0, username.lastIndexOf("&&"));

                        Log.e("username in ", names);

                        assignUserDynamicList.add(names);
                        alternativeDynamicUserList.add(names);
                        selectSupervisiorDynamicList.add(names);


                    }

                    for (int i = 0; i < taskinfoArray.length(); i++) {

                        // taskid = taskinfoArray.getJSONObject(i).getString("task_id");
                        assignedUser = taskinfoArray.getJSONObject(i).getString("assign_user");
                        alternateUser = taskinfoArray.getJSONObject(i).getString("alternate_user");
                        superVisor = taskinfoArray.getJSONObject(i).getString("supervisor");
                        taskorder = taskinfoArray.getJSONObject(i).getString("task_order");

                        if (alternateUser.equalsIgnoreCase("null") && assignedUser.equalsIgnoreCase("null") && superVisor.equalsIgnoreCase("null")) {

                            cvAddUserDynamic.setVisibility(View.GONE);

                        } else {

                            assignUser = assignedUser.substring(0, assignedUser.lastIndexOf("&&"));
                            alterUser = alternateUser.substring(0, alternateUser.lastIndexOf("&&"));
                            sprvisor = superVisor.substring(0, superVisor.lastIndexOf("&&"));


                            Log.e("taskorder", taskorder);
                            Log.e("aluser", alternateUser);
                            Log.e("asUser", assignedUser);
                            Log.e("supervisor", superVisor);

                            int order = Integer.parseInt(taskorder);

                            //actionArray = taskinfoArray.getJSONObject(i).getJSONArray("actions");


                            if (order > 1) {


                                cvAddUserDynamic.setVisibility(View.VISIBLE);
                                tvAdduser.setText("Edit / Add User");
                                orderPresent = true;
                                tvtaskorder.setText(taskorder);


                                //assign user adpter
                                if (spAssignUserDynamic != null) {

                                    assignUserListAdapter = new ArrayAdapter<String>(
                                            AprrovedRejectTaskActivity.this, android.R.layout.simple_spinner_item, assignUserDynamicList) {
                                        @Override
                                        public int getCount() {
                                            // don't display last item. It is used as hint.
                                            int count = super.getCount();
                                            return count > 0 ? count - 1 : count;
                                        }
                                    };

                                    assignUserListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spAssignUserDynamic.setAdapter(assignUserListAdapter);

                                    spAssignUserDynamic.setSelection(assignUserListAdapter.getCount());
                                    //conditionListAdapter.notifyDataSetChanged();


                                } else {

                                    Log.e("staticSpinnerCondition", "null");
                                }

                                //assign user adpter
                                if (spAlternativeUserDynamic != null) {

                                    alternateUserListAdapter = new ArrayAdapter<String>(
                                            AprrovedRejectTaskActivity.this, android.R.layout.simple_spinner_item, alternativeDynamicUserList) {
                                        @Override
                                        public int getCount() {
                                            // don't display last item. It is used as hint.
                                            int count = super.getCount();
                                            return count > 0 ? count - 1 : count;
                                        }
                                    };

                                    alternateUserListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spAlternativeUserDynamic.setAdapter(alternateUserListAdapter);
                                    spAlternativeUserDynamic.setSelection(alternateUserListAdapter.getCount());
                                    //conditionListAdapter.notifyDataSetChanged();


                                } else {

                                    Log.e("staticSpinnerCondition", "null");
                                }

                                //assign user adpter
                                if (spSelectSupervisorDynamic != null) {

                                    selectSupervisiorListAdapter = new ArrayAdapter<String>(
                                            AprrovedRejectTaskActivity.this, android.R.layout.simple_spinner_item, selectSupervisiorDynamicList) {
                                        @Override
                                        public int getCount() {
                                            // don't display last item. It is used as hint.
                                            int count = super.getCount();
                                            return count > 0 ? count - 1 : count;
                                        }
                                    };

                                    selectSupervisiorListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spSelectSupervisorDynamic.setAdapter(selectSupervisiorListAdapter);

                                    spSelectSupervisorDynamic.setSelection(selectSupervisiorListAdapter.getCount());
                                    //conditionListAdapter.notifyDataSetChanged();


                                } else {

                                    Log.e("staticSpinnerCondition", "null");
                                }

                                if (alterUser.equalsIgnoreCase("null")) {

                                    alternativeDynamicUserList.add("Select Alternate User");

                                } else {

                                    alternativeDynamicUserList.add(alterUser);
                                }

                                if (assignUser.equalsIgnoreCase("null")) {

                                    assignUserDynamicList.add("Select Assigned User");

                                } else {

                                    assignUserDynamicList.add(assignUser);

                                }


                                if (sprvisor.equalsIgnoreCase("null")) {

                                    selectSupervisiorDynamicList.add("Select Supervisior");

                                } else {

                                    selectSupervisiorDynamicList.add(sprvisor);
                                }

                           /* assignUserDynamicList.add(assignedUser);
                            alternativeDynamicUserList.add(alternateUser);
                            selectSupervisiorDynamicList.add(superVisor);
*/
                                spAssignUserDynamic.setAdapter(assignUserListAdapter);
                                spSelectSupervisorDynamic.setAdapter(selectSupervisiorListAdapter);
                                spAlternativeUserDynamic.setAdapter(alternateUserListAdapter);

                                spAssignUserDynamic.setSelection(assignUserListAdapter.getCount());
                                spAlternativeUserDynamic.setSelection(alternateUserListAdapter.getCount());
                                spSelectSupervisorDynamic.setSelection(selectSupervisiorListAdapter.getCount());


                            } else {


                                cvAddUserDynamic.setVisibility(View.GONE);


                            }


                        }


                    }
                    //bracket end here


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
                params.put("taskid", taskid);

                return params;
            }
        };


        VolleySingelton.getInstance(AprrovedRejectTaskActivity.this).addToRequestQueue(stringRequest);

    }


    void getGroupMembers(final String userid) {

        pgMain.setVisibility(View.VISIBLE);
        svMain.setVisibility(View.GONE);

        assignUserList.clear();
        alternativeUserList.clear();
        selectSupervisiorList.clear();
        DataList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("spinner response memb", response);

                //alternativeUserList.add("Select Alternate User");


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String username = jsonArray.getJSONObject(i).getString("user_name");

                        String uName = username.substring(0, username.lastIndexOf("&&"));


                        Log.e("username before", username);

                        DataList.add(username);

                        for (String name : DataList
                        ) {

                            Log.e("names", name);
                        }


                        assignUserList.add(uName);
                        alternativeUserList.add(uName);
                        selectSupervisiorList.add(uName);

                    }

                    //assign user adpter
                    if (spAssignUser != null) {

                        assignUserListAdapter = new ArrayAdapter<String>(
                                AprrovedRejectTaskActivity.this, android.R.layout.simple_spinner_item, assignUserList) {
                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }
                        };

                        assignUserListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spAssignUser.setAdapter(assignUserListAdapter);

                        spAssignUser.setSelection(assignUserListAdapter.getCount());
                        //conditionListAdapter.notifyDataSetChanged();


                    } else {

                        Log.e("staticSpinnerCondition", "null");
                    }

                    //assign user adpter
                    if (spAlternativeUser != null) {

                        alternateUserListAdapter = new ArrayAdapter<String>(
                                AprrovedRejectTaskActivity.this, android.R.layout.simple_spinner_item, alternativeUserList) {
                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }
                        };

                        alternateUserListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spAlternativeUser.setAdapter(alternateUserListAdapter);
                        spAlternativeUser.setSelection(alternateUserListAdapter.getCount());
                        //conditionListAdapter.notifyDataSetChanged();


                    } else {

                        Log.e("staticSpinnerCondition", "null");
                    }

                    //assign user adpter
                    if (spSelectSupervisor != null) {

                        selectSupervisiorListAdapter = new ArrayAdapter<String>(
                                AprrovedRejectTaskActivity.this, android.R.layout.simple_spinner_item, selectSupervisiorList) {
                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }
                        };

                        selectSupervisiorListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSelectSupervisor.setAdapter(selectSupervisiorListAdapter);
                        spSelectSupervisor.setSelection(selectSupervisiorListAdapter.getCount());
                        //conditionListAdapter.notifyDataSetChanged();


                    } else {

                        Log.e("staticSpinnerCondition", "null");
                    }

                    assignUserList.add("Select Assign User");
                    alternativeUserList.add("Select Alternate User");
                    selectSupervisiorList.add("Select Supervisor");

                    spAssignUser.setAdapter(assignUserListAdapter);
                    spSelectSupervisor.setAdapter(selectSupervisiorListAdapter);
                    spAlternativeUser.setAdapter(alternateUserListAdapter);

                    spAssignUser.setSelection(assignUserListAdapter.getCount());
                    spAlternativeUser.setSelection(alternateUserListAdapter.getCount());
                    spSelectSupervisor.setSelection(selectSupervisiorListAdapter.getCount());

                    for (String n :
                            alternativeUserList) {

                        Log.e("Alternate_user", n);
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


        VolleySingelton.getInstance(AprrovedRejectTaskActivity.this).addToRequestQueue(stringRequest);


    }

    void getComments(final String userid, final String taskid) {


        Log.e("taskid", taskid);
        uploading = false;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("comments api", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String username = jsonArray.getJSONObject(i).getString("username");
                        Log.e("username", username);
                        String commenttime = jsonArray.getJSONObject(i).getString("comment_time");
                        Log.e("date", commenttime);
                        String comment = jsonArray.getJSONObject(i).getString("comment");
                        Log.e("comment", comment);


                        String taskname = jsonArray.getJSONObject(i).getString("taskname");
                        Log.e("taskname", taskname);
                        String profilepic = jsonArray.getJSONObject(i).getString("profile_picture");
                        Log.e("profilepic", profilepic);

                        commentList.add(new CommentTaskApproveReject(username, taskname, comment, commenttime, profilepic));

                    }


                    rvComments.setAdapter(new CommentListAdapter(commentList, AprrovedRejectTaskActivity.this));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pgMain.setVisibility(View.GONE);
                svMain.setVisibility(View.VISIBLE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("useridComment", userid);
                params.put("taskidComment", taskid);

                return params;
            }

        };

        VolleySingelton.getInstance(AprrovedRejectTaskActivity.this).addToRequestQueue(stringRequest);


    }

    void ApproveRejectTaskwithOutUploading(final String comment,
                                           final String taskstatus, final String taskid, final String userid, final String username,
                                           final String assignedUser, final String taskOrder, final String alternateUser, final String supervisor,
                                           final String assignedUserAdd, final String alternateUserAdd, final String supervisorAdd, final String deadlinetype,
                                           final String daterangeAdd, final String daysAdd, final String hrsAdd, final String ip, final String pagecount
    ) {


        alertProcessing("on", this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT_UPLOADING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("without uploading", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.length() == 0) {

                        alertProcessing("off", AprrovedRejectTaskActivity.this);

                        alertError("There might be some issue", AprrovedRejectTaskActivity.this);


                    } else {

                        String message = jsonObject.getString("message");
                        String error = jsonObject.getString("error");

                        if (error.equalsIgnoreCase("false")) {

                            alertProcessing("off", AprrovedRejectTaskActivity.this);

                            alertSuccess(message, AprrovedRejectTaskActivity.this);

                        } else if (error.equalsIgnoreCase("true")) {

                            alertProcessing("off", AprrovedRejectTaskActivity.this);

                            alertSuccess(message, AprrovedRejectTaskActivity.this);


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: ", error);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

/*
                Log.e("volley result", "date :" + daterangeAdd + "\n" +
                        "taskstatus :" + taskstatus + "\n" + "comment :" + comment + "\n" + "assigned user :" + assignedUser + "\n" +
                        "alternate user :" + alternateUser + "\n" + "supervisor :" + supervisor + "\n" + "assignedUser Add : " + assignedUserAdd + "\n"
                        + "alteruserAdd :" + alternateUserAdd + "\n" + "supervisor add : " + supervisorAdd + "\n"
                        + "task order : " + taskOrder + "taskid  : " + taskid + "\n");*/


                Map<String, String> params = new HashMap<>();

                params.put("userid", userid);
                params.put("taskid", taskid);

                if (comment == null || comment.trim().length() == 0 || comment.equalsIgnoreCase("null")) {

                    params.put("comment", "null");

                } else {

                    params.put("comment", comment);

                }


                params.put("username", username);
                params.put("taskstatus", taskstatus);
                params.put("pageCount", pagecount);


                if (assignedUser == null || assignedUser.equalsIgnoreCase("null")) {

                    params.put("asiusr", "null");

                } else {

                    params.put("asiusr", assignedUser);

                }


                params.put("taskOrder", taskOrder);


                if (alternateUser == null || alternateUser.equalsIgnoreCase("null")) {

                    params.put("altrUsr", "null");

                } else {

                    params.put("altrUsr", alternateUser);

                }

                if (supervisor == null || supervisor.equalsIgnoreCase("null")) {

                    params.put("supvsr", "null");

                } else {

                    params.put("supvsr", supervisor);

                }


                if (assignedUserAdd == null || assignedUserAdd.equalsIgnoreCase("null")) {

                    params.put("assignUsrAdd", "null");

                } else {

                    params.put("assignUsrAdd", assignedUserAdd);
                }


                if (alternateUserAdd == null || alternateUserAdd.equalsIgnoreCase("null")) {

                    params.put("altrUsrAdd", "null");
                } else {

                    params.put("altrUsrAdd", alternateUserAdd);

                }

                if (supervisorAdd == null || supervisorAdd.equalsIgnoreCase("null")) {

                    params.put("supvsrAdd", "null");
                } else {

                    params.put("supvsrAdd", supervisorAdd);

                }

                if (deadlinetype == null || deadlinetype.equalsIgnoreCase("null")) {

                    params.put("radio", "null");
                } else {

                    params.put("radio", deadlinetype);
                }

                if (daterangeAdd == null || daterangeAdd.equalsIgnoreCase("null")) {

                    params.put("daterangeAdd", "null");

                } else {

                    params.put("daterangeAdd", daterangeAdd);
                }

                if (daysAdd == null || daysAdd.equalsIgnoreCase("null")) {

                    params.put("daysAdd", "null");

                } else {


                    params.put("daysAdd", daysAdd);

                }


                if (hrsAdd == null || hrsAdd.equalsIgnoreCase("null")) {

                    params.put("hrsAdd", "null");
                } else {


                    params.put("hrsAdd", hrsAdd);
                }

                if (ip == null || ip.equalsIgnoreCase("null")) {

                    params.put("ip", "null");
                } else {

                    params.put("ip", ip);


                }


                for (Map.Entry<String, String> entry : params.entrySet()) {
                    Log.e("Map values", entry.getKey() + " : " + entry.getValue());
                }

                return params;
            }

        };

        VolleySingelton.getInstance(AprrovedRejectTaskActivity.this).addToRequestQueue(stringRequest);


    }


    public void ApproveRejectTaskWithUploading(final String filepath, final String comment,
                                               final String taskstatus, final String taskid, final String userid, final String username,
                                               final String assignedUser, final String taskOrder, final String alternateUser, final String supervisor,
                                               final String assignedUserAdd, final String alternateUserAdd, final String supervisorAdd, final String deadlinetype,
                                               final String daterangeAdd, final String daysAdd, final String hrsAdd, final String ip, final String pagecount) {
        ///getting the actual path of the image

        alertProcessing("on", ActivityName);

        String path = null;

        uploading = true;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filePath;
        }

        if (path == null) {

            Toast.makeText(ActivityName, "Please move your file to internal storage and retry", Toast.LENGTH_LONG).show();

        } else {

            //Uploading code
            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);
                final ProgressDialog progress = new ProgressDialog(ActivityName);


                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(ActivityName, uploadId, ApiUrl.PROCESS_TASK_APPROVED_REJECT_UPLOADING);
                multipartUploadRequest.addFileToUpload(path, "fileName");


                Log.e("Upload_rate", UPLOAD_RATE);

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(ActivityName, 1, uploadId)));


                uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;


                uploadNotificationConfig.getError().message = "Error while uploading";
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);


                uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
                uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;


              /*  if (filepath.trim().length() != 0 && TextUtils.isEmpty(filepath)) {




                    );

                }
*/
                multipartUploadRequest.setNotificationConfig(

                        uploadNotificationConfig
                                .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                .setClearOnActionForAllStatuses(true));


                multipartUploadRequest.addParameter("userid", userid);
                multipartUploadRequest.addParameter("taskid", taskid);


                if (comment == null || comment.trim().length() == 0 || comment.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("comment", "null");

                } else {

                    multipartUploadRequest.addParameter("comment", comment);

                }


                multipartUploadRequest.addParameter("username", username);
                multipartUploadRequest.addParameter("taskstatus", taskstatus);


                if (assignedUser == null || assignedUser.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("asiusr", "null");

                } else {

                    multipartUploadRequest.addParameter("asiusr", assignedUser);

                }


                multipartUploadRequest.addParameter("taskOrder", taskOrder);


                if (alternateUser == null || alternateUser.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("altrUsr", "null");

                } else {

                    multipartUploadRequest.addParameter("altrUsr", alternateUser);

                }

                if (supervisor == null || supervisor.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("supvsr", "null");

                } else {

                    multipartUploadRequest.addParameter("supvsr", supervisor);

                }


                if (assignedUserAdd == null || assignedUserAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("assignUsrAdd", "null");

                } else {

                    multipartUploadRequest.addParameter("assignUsrAdd", assignedUserAdd);
                }


                if (alternateUserAdd == null || alternateUserAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("altrUsrAdd", "null");
                } else {

                    multipartUploadRequest.addParameter("altrUsrAdd", alternateUserAdd);

                }

                if (supervisorAdd == null || supervisorAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("supvsrAdd", "null");
                } else {

                    multipartUploadRequest.addParameter("supvsrAdd", supervisorAdd);

                }

                if (deadlinetype == null || deadlinetype.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("radio", "null");
                } else {

                    multipartUploadRequest.addParameter("radio", deadlinetype);
                }

                if (daterangeAdd == null || daterangeAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("daterangeAdd", "null");

                } else {

                    multipartUploadRequest.addParameter("daterangeAdd", daterangeAdd);
                }

                if (daysAdd == null || daysAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("daysAdd", "null");

                } else {


                    multipartUploadRequest.addParameter("daysAdd", daysAdd);

                }


                if (hrsAdd == null || hrsAdd.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("hrsAdd", "null");
                } else {


                    multipartUploadRequest.addParameter("hrsAdd", hrsAdd);
                }

                if (ip == null || ip.equalsIgnoreCase("null")) {

                    multipartUploadRequest.addParameter("ip", "null");
                } else {

                    multipartUploadRequest.addParameter("ip", ip);


                }

                multipartUploadRequest.addParameter("pageCount", pagecount);

                multipartUploadRequest.setMaxRetries(5)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {


                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                                Toast.makeText(AprrovedRejectTaskActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                //progress.dismiss();

                                /*  Toast.makeText(ActivityName, "File upload Succesfully", Toast.LENGTH_LONG).show();*/


                                try {

                                    Log.e("ser uploading", serverResponse.getBodyAsString());

                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());

                                    Log.e("uplaoding", "1");

                                    String message = jsonObject.getString("message");
                                    String error = jsonObject.getString("error");

                                    Log.e("error", error);

                                    Log.e("uplaoding", "2");

                                    if (error.equalsIgnoreCase("false")) {


                                        alertSuccess(message, ActivityName);

                                       /* AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityName);
                                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View dialogView = inflater.inflate(R.layout.alertdialog_success, null);

                                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                                        tv_error_heading.setText("Success");
                                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                                        tv_error_subheading.setText(message);


                                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v)  {


                                                alertDialog.dismiss();

                                            }
                                        });

                                       *//* Toast toast = new Toast(getApplicationContext());
                                        toast.setGravity(Gravity.FILL_HORIZONTAL,0,0);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(dialogView);
                                        toast.show();*//*

                                        dialogBuilder.setView(dialogView);
                                        alertDialog = dialogBuilder.create();
                                        alertDialog.setCancelable(false);
                                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        alertDialog.show();
*/
                                        Log.e("uplaoding", "3");

                                        alertProcessing("off", ActivityName);

                                        Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                                    } else if (error.equalsIgnoreCase("true")) {

                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Toast.makeText(ActivityName, "Upload Canceled", Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadId);
                            }
                        });


                if (isNetworkAvailable()) {

                    multipartUploadRequest.startUpload();

                    Toast.makeText(ActivityName, "Upload Started..", Toast.LENGTH_SHORT).show();


                } else {


                    Toast.makeText(ActivityName, "No Internet connection found", Toast.LENGTH_SHORT).show();

                }


                // UploadService.stopUpload(uploadId);
                //UploadService.stop(getActivity());


                //UploadService.stopAllUploads();


            } catch (Exception exc) {
                Toast.makeText(AprrovedRejectTaskActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


            //Toast.makeText(this,"File is uploading succesfully",Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


        if (!isConnected) {

            Toast.makeText(this, "No Internet Connection found", Toast.LENGTH_SHORT).show();

            UploadService.stopAllUploads();

        }

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //Alert dialog succcess

    void alertError(String message, Context context) {


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

                alertDialog.dismiss();


                Intent intent = new Intent(ActivityName, InTrayActivity.class);
                startActivity(intent);

                finish();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    void alertProcessing(String onOff, Context context) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progress_alert, null);
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnRunInBg = dialogView.findViewById(R.id.progress_alert_run_in_bg);
        btnRunInBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AprrovedRejectTaskActivity.this, InTrayActivity.class);
                startActivity(intent);

            }
        });


        if (onOff.equalsIgnoreCase("on")) {

            alertDialog.show();
        } else if (onOff.equalsIgnoreCase("off")) {

            alertDialog.dismiss();

        }


    }


    void alertSuccess(String message, Context context) {


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


                alertDialog.dismiss();


                Intent intent = new Intent(ActivityName, InTrayActivity.class);
                startActivity(intent);

                finish();


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }


    //get userid of the specific name
    String searchUserid(String name) {

        String userid = null;

        //Log.e("name",name);

        for (String n : DataList
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

                new MaterialFilePicker()
                        .withActivity(AprrovedRejectTaskActivity.this)
                        .withRequestCode(404)
                        .withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();


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
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AprrovedRejectTaskActivity.this);
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


    void resetFileUploadWindow() {

        ivFileType.setImageDrawable(getResources().getDrawable(R.drawable.unsupported_file));
        tvPath.setText("");

    }

    void checkClientMemory(String clientid, String filesize) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECK_CLIENT_MEMORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString("msg");
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {

                        Toast.makeText(AprrovedRejectTaskActivity.this, msg, Toast.LENGTH_LONG).show();

                        // Util.alertError(msg,UploadActivity.this);
                       /*toast.setGravity(Gravity.CENTER, 0, 0);

                       toast.show();*/

                        //  btnSubmit.setEnabled(true);


                    } else if (status.equalsIgnoreCase("false")) {

                        //Util.alertError(msg,UploadActivity.this);
                        //btnSubmit.setEnabled(false);
                        Toast.makeText(AprrovedRejectTaskActivity.this, msg, Toast.LENGTH_LONG).show();


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
                params.put("clientid", clientid);
                params.put("size", filesize);


                return params;
            }
        };

        VolleySingelton.getInstance(AprrovedRejectTaskActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiverClosePB);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiverClosePB);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiverClosePB, new IntentFilter(getPackageName() + ".CLOSE_APPROVE_REJECT_PB"));
    }
}












