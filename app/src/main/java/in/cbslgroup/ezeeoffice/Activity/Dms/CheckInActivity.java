package in.cbslgroup.ezeeoffice.Activity.Dms;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.FileViewActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeeoffice.Adapters.CheckinFieldAdapter;
import in.cbslgroup.ezeeoffice.Model.CheckinFields;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
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

public class CheckInActivity extends AppCompatActivity {

    List<View> myLayouts = new ArrayList<>();
    TextView tv_star;
    EditText etMeta;
    LinearLayout llDescribe, llMetadata;
    TextView tvNoMetaFound, tvFilePath;
    ProgressBar progressBar;
    Button btnChooseFile, btnCancel, btnSaveChanges;
    String filePath;
    ImageView ivDatePicker;
    ArrayList<String> metalabellist = new ArrayList<>();
    ArrayList<String> metaEnteredlist = new ArrayList<>();
    MultipartUploadRequest multipartUploadRequest;
    JSONObject jsonobject;
    String filename;
    PdfReader reader;
    String pageCount;
    String docid, slid, foldername, mode;
    Boolean startUpload = false;
    CheckBox checkBoxUploadDoc;
    LinearLayout llChooseFile;
    Boolean withupload = false;
    Boolean noerror = true;
    private int mYear, mMonth, mDay, mHour, mMinute;

    BottomSheetDialog bottomSheetDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String TAG = "photo";
    ImageView ivFileType, ivCameraImg;
    CardView cvCameraImg;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    CustomDateTimePicker dateTimePicker;


    //-----------------------//
    int etMetaDatePos = 0;
    List<CheckinFields> checkinFieldsList = new ArrayList<>();
    RecyclerView rvFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        requestPermission();

        Toolbar toolbar = findViewById(R.id.toolbar_checkin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkIn(docid, "cancel");
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        Intent intent = getIntent();

        docid = intent.getStringExtra("docid");
        slid = intent.getStringExtra("slid");
        foldername = intent.getStringExtra("fname");
        mode = intent.getStringExtra("mode");

        toolbar.setSubtitle(foldername);

        llMetadata = findViewById(R.id.ll_checkin_metalist);
        progressBar = findViewById(R.id.progressbar_checkin);

        checkBoxUploadDoc = findViewById(R.id.checkbox_checkin_checkout_attach_doc);
        llChooseFile = findViewById(R.id.ll_checkin_checkout_choose_doc);

        cvCameraImg = findViewById(R.id.cv_checkin_upload_camera);
        ivFileType = findViewById(R.id.iv_checkin_filetype);
        ivCameraImg = findViewById(R.id.iv_checkin_upload_camera);

        tvNoMetaFound = findViewById(R.id.tv_checkin_no_meta_found);

        rvFields = findViewById(R.id.rv_checkin_fields);
        rvFields.setLayoutManager(new LinearLayoutManager(this));

        btnChooseFile = findViewById(R.id.btn_upload_choose_file_checkin);
        btnCancel = findViewById(R.id.btn_checkin_cancel);
        btnSaveChanges = findViewById(R.id.btn_checkin_savechanges);
        tvFilePath = findViewById(R.id.tv_upload_filepath_checkin);


        checkMandatory(slid, docid);


        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showBottomSheetDialog();

//                new MaterialFilePicker()
//                        .withActivity(CheckInActivity.this)
//                        .withRequestCode(100)
//                        //.withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$",Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
//                        .withFilterDirectories(false) // Set directories filterable (false by default)
//                        .withHiddenFiles(true) // Show hidden files and folders
//                        .start();


            }
        });
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                metalabellist.clear();
                metaEnteredlist.clear();

                //code here
                for (int i = 0; i < checkinFieldsList.size(); i++) {

                    View itemView  = rvFields.getLayoutManager().findViewByPosition(i);
                    TextView tvMetaLabel =itemView.findViewById(R.id.tv_upload_describe_metaname);
                    EditText etMetadata = itemView.findViewById(R.id.et_upload_describe);
                    TextView dataType = itemView.findViewById(R.id.tv_upload_describe_datatype);
                    TextView tv_star = itemView.findViewById(R.id.tv_upload_describes_mandatory);
                    //etMetadata.setText(list.get(i));


                    String metadata = String.valueOf(etMetadata.getText());
                    String metaname = String.valueOf(tvMetaLabel.getText());
                    String datatype = String.valueOf(dataType.getText());

                    if (datatype.equalsIgnoreCase("datetime") && TextUtils.isEmpty(metadata)) {

                        if (tv_star.getVisibility() == View.VISIBLE) {

                            metadata = "";

                        } else {
                            metadata = "0000-00-00";
                        }


                        metalabellist.add(metaname);
                        metaEnteredlist.add(metadata);

                    } else if (datatype.equalsIgnoreCase("bit") && TextUtils.isEmpty(metadata)) {

                        if (tv_star.getVisibility() == View.VISIBLE) {

                            metadata = "";

                        } else {

                            metadata = "0";
                        }

                        metalabellist.add(metaname);
                        metaEnteredlist.add(metadata);

                    } else {

                        metalabellist.add(metaname);
                        metaEnteredlist.add(metadata);

                    }


                    Log.e("metadata", metadata);
                    Log.e("metaname", metaname);


                    if (tv_star.getVisibility() == View.VISIBLE) {

                        if (metadata.equals("") || metadata.isEmpty() || metadata.length() == 0) {

                            etMetadata.setError("Please fill these are mandatory");
                            noerror = false;

                            Animation animation = AnimationUtils.loadAnimation(CheckInActivity.this, R.anim.shake);
                            etMetadata.startAnimation(animation);
                            break;

                        } else {

                            etMetadata.setError(null);
                            noerror = true;
                        }

                    } else {

                        noerror = true;

                    }

                }

                try {

                    jsonobject = makeJsonObject(metalabellist, metaEnteredlist, metalabellist.size());
                    Log.e("ankit", jsonobject.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.e("noerror", String.valueOf(noerror));
                Log.e("withupload", String.valueOf(withupload));

                if (noerror && withupload) {

                    uploadMultipart();

                    Log.e("uploadcheck", "ok");


                } else if (noerror && !withupload) {

                    checkinWithoutUpload();
                    Log.e("uploadcheckvolley", "ok");


                }


            }
        });
        checkBoxUploadDoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {


                    llChooseFile.setVisibility(View.VISIBLE);
                    withupload = true;
                    resetFileUploadWindow();

                } else {


                    llChooseFile.setVisibility(View.GONE);
                    withupload = false;
                    resetFileUploadWindow();


                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkIn(docid, "cancel");

               /* Intent intent = new Intent(CheckInActivity.this, FileViewActivity.class);
                intent.putExtra("slid",slid);
                intent.putExtra("fname",foldername);
                startActivityForResult(intent,420);




                finish();*/

            }
        });




       Log.e("slid in checkin",slid);
        //tvNoMetaFound = findViewById(R.id.tv_upload_frag_describe_nometafound);

    }

    private void checkinWithoutUpload() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("checkin without upload", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(CheckInActivity.this, msg, Toast.LENGTH_SHORT).show();


                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(CheckInActivity.this, msg, Toast.LENGTH_SHORT).show();

                        checkIn(docid, "ok");


                    } else {

                        Toast.makeText(CheckInActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
                params.put("checkin_metadata", jsonobject.toString());
                params.put("checkin_docid", docid);
                params.put("checkin_pagecount", "1");
                params.put("checkin_username", MainActivity.username);
                params.put("checkin_userid", MainActivity.userid);
                params.put("checkin_ip", MainActivity.ip);


                JSONObject js = new JSONObject(params);
                Log.e("withoutupload", js.toString());


                return params;
            }

        };

        VolleySingelton.getInstance(CheckInActivity.this).addToRequestQueue(stringRequest);


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

                tvFilePath.setText(p);


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

            switch (filetype.toLowerCase()) {

                case "pdf":

                    ivFileType.setImageResource(R.drawable.pdf_128px);
                    break;

                case "jpg":

                    ivFileType.setImageResource(R.drawable.jpg_128px);
                    break;

                case "png":

                    ivFileType.setImageResource(R.drawable.png_128px);
                    break;

                case "gif":
                    ivFileType.setImageResource(R.drawable.gif_128px);
                    //holder.ivOpenFile.setImageResource(R.drawable.png);

                    break;
                case "jpeg":

                    ivFileType.setImageResource(R.drawable.jpeg_128px);
                    break;

                case "tiff":

                    ivFileType.setImageResource(R.drawable.tiff_128px);
                    break;

                case "tif":
                    ivFileType.setImageResource(R.drawable.tif_128px);
                    break;

                case "doc":

                    ivFileType.setImageResource(R.drawable.doc_128px);
                    break;

                case "txt":

                    ivFileType.setImageResource(R.drawable.unsupported_file_128px);
                    break;

                case "docx":
                    ivFileType.setImageResource(R.drawable.docx_128px);
                    break;


                case "psd":
                    ivFileType.setImageResource(R.drawable.psd_128px);
                    break;

                case "mp4":

                    ivFileType.setImageResource(R.drawable.mp4_128px);
                    break;


                case "3gp":
                    ivFileType.setImageResource(R.drawable.threegp_128px);
                    break;

                case "mp3":
                    ivFileType.setImageResource(R.drawable.mp3_128px);
                    break;

                case "bmp":

                    ivFileType.setImageResource(R.drawable.bmp_128px);
                    break;


                default:

                    ivFileType.setImageResource(R.drawable.unsupported_file_128px);
                    break;


            }


            tvFilePath.setText(p);
            Log.e("filepath in activity", filePath);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                cvCameraImg.setVisibility(View.VISIBLE);
                ivCameraImg.setImageURI(result.getUri());

                Log.e("image uri", result.getUri().toString());

                String uri = result.getUri().toString();
                String path = uri.substring(7);

                Log.e("path", path);

                filePath = path;

                // ivCameraImg.setBackground(getResources().getDrawable(R.drawable.bg_take_pic));

                //((ImageView) findViewById(R.id.iv_crop_test)).setImageURI(result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

//    private void createDynamicView(String metaname, String length, String mandatory, String value, String datatype) {
//
//
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        // LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
//        View rowView = inflater.inflate(R.layout.upload_describes_dynamic_tv_et, null);
//
//        myLayouts.add(rowView);
//
//        TextView textView = rowView.findViewById(R.id.tv_upload_describe_metaname);
//        textView.setText(metaname);
//
//
//        ivDatePicker = rowView.findViewById(R.id.date_picker_describe);
//        etMeta = rowView.findViewById(R.id.et_upload_describe);
////        etMeta.setId(etMetaDatePos);
////        etMetaDatePos++;
//
//        Log.e("metaname", metaname);
//
//        if (datatype.equals("varchar")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//            etMeta.setInputType(InputType.TYPE_CLASS_TEXT);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//        } else if (datatype.equals("bit")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
//            etMeta.setHint("Data should " + length + " only");
//
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
//
//        } else if (datatype.equals("float")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//
//
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//        } else if (datatype.equals("Int")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//        } else if (datatype.equals("BigInt")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//
//        } else if (datatype.equals("TEXT")) {
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setHint("");
//
//        } else if (datatype.equals("char")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            //ivDatePicker.setVisibility(View.GONE);
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//            //  etMeta.setInputType(InputType.);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//
//
//        } else if (datatype.equals("datetime")) {
//
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//
//            etMeta.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
//
//            //ivDatePicker.setVisibility(View.VISIBLE);
//            etMeta.setHint("yyyy-mm-dd");
//            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
//            // etMeta.setKeyListener(null);
//            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
//            etMeta.setFocusable(false);
//            etMeta.setFocusableInTouchMode(false);
//            etMeta.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    dateTimePicker = new CustomDateTimePicker(CheckInActivity.this, new CustomDateTimePicker.ICustomDateTimeListener() {
//                        @Override
//                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
//
//                            etMeta.setText("");
//
//                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));
//
//                            String Month = "";
//
//                            if (monthNumber + 1 <= 9) {
//
//                                Month = "0" + (monthNumber + 1);
//
//                            } else {
//
//                                Month = String.valueOf(monthNumber + 1);
//                            }
//
//
//                            String secounds = String.valueOf(sec);
//                            String minutes = String.valueOf(min);
//
//                            if(min<10)
//                            {
//                                minutes = "0"+minutes;
//
//                            }
//
//                            if(sec<10){
//
//                                secounds = "0"+secounds;
//                            }
//
//                            EditText et = view.findViewWithTag(view.getTag());
//                            Log.e("etMeta getTag()", String.valueOf(view.getTag()));
//                            et.setText(year
//                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                    + " " + hour12 + ":" + minutes + ":" + secounds
//                            );
//
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                            dateTimePicker.dismissDialog();
//
//                        }
//                    });
//
//                    dateTimePicker.set24HourFormat(true);
//                    dateTimePicker.setDate(Calendar.getInstance());
//                    dateTimePicker.showDialog();
//
//
//                }
//            });
//
//
//        } else if (datatype.equals("DATETIME")) {
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//            etMeta.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
//            etMeta.setHint("yyyy-mm-dd");
//            // etMeta.setKeyListener(null);
//            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
//            etMeta.setFocusable(false);
//            etMeta.setFocusableInTouchMode(false);
//            // etMeta.setKeyListener(null);
//            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
//            etMeta.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    dateTimePicker = new CustomDateTimePicker(CheckInActivity.this, new CustomDateTimePicker.ICustomDateTimeListener() {
//                        @Override
//                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
//
//                            etMeta.setText("");
//
//                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));
//
//                            String Month = "";
//
//                            if (monthNumber + 1 <= 9) {
//
//                                Month = "0" + (monthNumber + 1);
//
//                            } else {
//
//                                Month = String.valueOf(monthNumber + 1);
//                            }
//
//                            String secounds = String.valueOf(sec);
//                            String minutes = String.valueOf(min);
//
//                            if(min<10)
//                            {
//                                minutes = "0"+minutes;
//
//                            }
//
//                            if(sec<10){
//
//                                secounds = "0"+secounds;
//                            }
//
//                            EditText et = view.findViewWithTag(view.getTag());
//                            Log.e("etMeta getTag()", String.valueOf(view.getTag()));
//                            et.setText(year
//                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                    + " " + hour12 + ":" + minutes + ":" + secounds
//                            );
//
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                            dateTimePicker.dismissDialog();
//
//                        }
//                    });
//
//                    dateTimePicker.set24HourFormat(true);
//                    dateTimePicker.setDate(Calendar.getInstance());
//                    dateTimePicker.showDialog();
//
//
//                }
//            });
//
//        } else {
//
//            //ivDatePicker.setVisibility(View.GONE);
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//        }
//
//
//        tv_star = rowView.findViewById(R.id.tv_upload_describes_mandatory);
//        if (mandatory.equals("Yes")) {
//
//
//            tv_star.setVisibility(View.VISIBLE);
//
//
//        } else {
//            tv_star.setVisibility(View.GONE);
//
//        }
//
//        //etMeta = rowView.findViewById(R.id.et_upload_describe);
//        if (value.equalsIgnoreCase("null")) {
//
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//
//
//        } else {
//
//             etMeta.setTag(etMetaDatePos);
//             etMetaDatePos++;
//
//             EditText et = etMeta.findViewWithTag(etMeta.getTag());
//             et.setText(value);
////            etMeta.setTag(etMetaDatePos);
////            etMetaDatePos++;
////
////            EditText et = rowView.findViewWithTag(rowView.getTag());
////            et.setText(value);
//
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//
//
//        }
//
//
////        ivDatePicker.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////
////                // Get Current Date
////                final Calendar c = Calendar.getInstance();
////                mYear = c.get(Calendar.YEAR);
////                mMonth = c.get(Calendar.MONTH);
////                mDay = c.get(Calendar.DAY_OF_MONTH);
////
////
////                new DatePickerDialog(CheckInActivity.this, new DatePickerDialog.OnDateSetListener() {
////                    @Override
////                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
////
////
////                        if (month <= 10) {
////
////                            etMeta.setText(year + "-" + "0" + month + "-" + dayOfMonth);
////
////
////                        } else {
////
////                            etMeta.setText(year + "-" + month + "-" + dayOfMonth);
////
////                        }
////
////                        if (dayOfMonth <= 10) {
////
////                            etMeta.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
////
////                        } else {
////
////                            etMeta.setText(year + "-" + "0" + month + "-" + dayOfMonth);
////                        }
////
////
////                    }
////                }, mYear, mMonth, mDay).show();
////
////
////            }
////        });
//
//
//        llMetadata.addView(rowView, llMetadata.getChildCount() - 1);
//
//
//    }


//    private void createDynamicView(String metaname, String length, String mandatory, String datatype) {
//
//
//        int etMetaDatePos = 0;
//        LayoutInflater inflater = (LayoutInflater) CheckInActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        // LinearLayout linearLayout = findViewById(R.id.ll_dms_nav_left_drawer);
//        View rowView = inflater.inflate(R.layout.upload_describes_dynamic_tv_et, null);
//
//        myLayouts.add(rowView);
//
//        TextView textView = rowView.findViewById(R.id.tv_upload_describe_metaname);
//        textView.setText(metaname);
//
//        TextView tvDatatype = rowView.findViewById(R.id.tv_upload_describe_datatype);
//        tvDatatype.setText(datatype);
//
//        RelativeLayout rlMain = rowView.findViewById(R.id.rl_upload_describe);
//        ivDatePicker = rowView.findViewById(R.id.date_picker_describe);
//        etMeta = rowView.findViewById(R.id.et_upload_describe);
//
//        Log.e("metaname", metaname);
//
//
//         /*   JSONObject j = new JSONObject(metaEnteredListHash);
//            Iterator<String> iter = j.keys();
//            while (iter.hasNext()) {
//                String key = iter.next();
//                try {
//                    Object metalabel = j.get(key);
//
//
//
//                    if(metaname.equalsIgnoreCase(String.valueOf(metalabel))){
//
//                        etMeta.setText(j.getString(String.valueOf(metalabel)));
//
//                    };
//
//
//
//                } catch (JSONException e) {
//                    // Something went wrong!
//                }
//
//
//        }
//*/
//
//
//        if (datatype.equals("varchar")) {
//
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//            etMeta.setInputType(InputType.TYPE_CLASS_TEXT);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//        } else if (datatype.equals("bit")) {
//
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
//            etMeta.setHint("Data should " + length + " only");
//
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
//
//        } else if (datatype.equals("float")) {
//
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//
//
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//        } else if (datatype.equals("Int")) {
//
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//        } else if (datatype.equals("BigInt")) {
//
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
//            etMeta.setInputType(InputType.TYPE_CLASS_NUMBER);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//
//        } else if (datatype.equals("TEXT")) {
//
//            etMeta.setHint("");
//
//        } else if (datatype.equals("char")) {
//
//            ivDatePicker.setVisibility(View.GONE);
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//            //  etMeta.setInputType(InputType.);
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//
//
//        } else if (datatype.equals("datetime")) {
//
//
//            // get Position
//            String strPosition = (String) etMeta.getTag();
//            Log.e("EtMeta"," datetime getTag : >"+strPosition);
//            // getId
//            int strId = etMeta.getId();
//            Log.e("EtMeta"," datetime getId : >"+strId);
//
//
////            int childpos = llDescribe.indexOfChild(etMeta.getRootView());
////            Log.e("EtMeta"," datetime childpos : >"+childpos);
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//
//            //ivDatePicker.setVisibility(View.VISIBLE);
//            etMeta.setHint("yyyy-mm-dd");
//            // etMeta.setKeyListener(null);
//            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
//            etMeta.setFocusable(false);
//            etMeta.setFocusableInTouchMode(false);
//            etMeta.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
//            etMeta.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    dateTimePicker = new CustomDateTimePicker((AppCompatActivity) CheckInActivity.this, new CustomDateTimePicker.ICustomDateTimeListener() {
//                        @Override
//                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
//
//                            etMeta.setText("");
//
//                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));
//
//                            String Month = "";
//
//                            if (monthNumber + 1 <= 9) {
//
//                                Month = "0" + (monthNumber + 1);
//
//                            } else {
//
//                                Month = String.valueOf(monthNumber + 1);
//                            }
//
//                            String secounds = String.valueOf(sec);
//                            String minutes = String.valueOf(min);
//
//                            if(min<10)
//                            {
//                                minutes = "0"+minutes;
//
//                            }
//
//                            if(sec<10){
//
//                                secounds = "0"+secounds;
//                            }
//
//                            EditText et = view.findViewWithTag(view.getTag());
//                            Log.e("etMeta getTag()", String.valueOf(view.getTag()));
//                            et.setText(year
//                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                    + " " + hour12 + ":" + minutes + ":" + secounds
//                            );
//
//
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                            dateTimePicker.dismissDialog();
//
//                        }
//                    });
//
//                    dateTimePicker.set24HourFormat(true);
//                    dateTimePicker.setDate(Calendar.getInstance());
//                    dateTimePicker.showDialog();
//
//
////                    // Get Current Date
////                    final Calendar c = Calendar.getInstance();
////                    mYear = c.get(Calendar.YEAR);
////                    mMonth = c.get(Calendar.MONTH);
////                    mDay = c.get(Calendar.DAY_OF_MONTH);
////
////
////                    new DatePickerDialog(CheckInActivity.this, new DatePickerDialog.OnDateSetListener() {
////                        @Override
////                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
////
////                            Log.e("date_picker", "year" + year + "\n" + "month" + (month + 1) + "\n" + "day" + dayOfMonth);
////
////                            if (month + 1 <= 10) {
////
////                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
////
////
////                            } else {
////
////                                etMeta.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
////
////                            }
////
////                            if (dayOfMonth <= 10) {
////
////                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + "0" + dayOfMonth);
////
////                            } else {
////
////                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
////                            }
////
////                        }
////                    }, mYear, mMonth, mDay).show();
//
//
//                }
//            });
//          /* if(etMeta.getText().toString().isEmpty()||etMeta.getText().toString().equalsIgnoreCase("")){
//
//               etMeta.setText("0000-00-00");
//           }*/
//
//        } else if (datatype.equals("DATETIME")) {
//
//            // ivDatePicker.setVisibility(View.VISIBLE);
//            etMeta.setHint("yyyy-mm-dd");
//            etMeta.setFocusable(false);
//
//            // get Position
//            String strPosition = (String) etMeta.getTag();
//            Log.e("EtMeta"," DATETIME getTag : >"+strPosition);
//            // getId
//            int strId = etMeta.getId();
//            Log.e("EtMeta"," DATETIME getId : >"+strId);
//
////            int childpos = llDescribe.indexOfChild(etMeta.getRootView());
////            Log.e("EtMeta"," DATETIME childpos : >"+childpos);
//
//            // etMeta.setTag(etMeta.getId());
//
//
//            etMeta.setTag(etMetaDatePos);
//            etMetaDatePos++;
//
//            etMeta.setFocusableInTouchMode(false);
//            etMeta.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
//            // etMeta.setKeyListener(null);
//            etMeta.setInputType(InputType.TYPE_CLASS_DATETIME);
//            etMeta.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    dateTimePicker = new CustomDateTimePicker((AppCompatActivity) CheckInActivity.this, new CustomDateTimePicker.ICustomDateTimeListener() {
//                        @Override
//                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
//
//                            etMeta.setText("");
//
//                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));
//
//                            String Month = "";
//
//                            if (monthNumber + 1 <= 9) {
//
//                                Month = "0" + (monthNumber + 1);
//
//                            } else {
//
//                                Month = String.valueOf(monthNumber + 1);
//                            }
//
//                            String secounds = String.valueOf(sec);
//                            String minutes = String.valueOf(min);
//
//                            if(min<10)
//                            {
//                                minutes = "0"+minutes;
//
//                            }
//
//                            if(sec<10){
//
//                                secounds = "0"+secounds;
//                            }
//
//                            EditText et = view.findViewWithTag(view.getTag());
//                            Log.e("etMeta getTag()", String.valueOf(view.getTag()));
//                            et.setText(year
//                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
//                                    + " " + hour12 + ":" + minutes + ":" + secounds
//                            );
//
//
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                            dateTimePicker.dismissDialog();
//
//                        }
//                    });
//
//                    dateTimePicker.set24HourFormat(true);
//                    dateTimePicker.setDate(Calendar.getInstance());
//                    dateTimePicker.showDialog();
//
//
////                    // Get Current Date
////                    final Calendar c = Calendar.getInstance();
////                    mYear = c.get(Calendar.YEAR);
////                    mMonth = c.get(Calendar.MONTH);
////                    mDay = c.get(Calendar.DAY_OF_MONTH);
////
////
////                    new DatePickerDialog(CheckInActivity.this, new DatePickerDialog.OnDateSetListener() {
////                        @Override
////                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
////
////                            Log.e("date_picker", "year" + year + "\n" + "month" + (month + 1) + "\n" + "day" + dayOfMonth);
////
////                            if (month + 1 <= 10) {
////
////                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
////
////
////                            } else {
////
////                                etMeta.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
////
////                            }
////
////                            if (dayOfMonth <= 10) {
////
////                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + "0" + dayOfMonth);
////
////                            } else {
////
////                                etMeta.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
////                            }
////
////
////                        }
////                    }, mYear, mMonth, mDay).show();
//
//
//                }
//            });
//
//
//
//          /* if(etMeta.getText().toString().isEmpty()||etMeta.getText().toString().equalsIgnoreCase("")){
//
//               etMeta.setText("0000-00-00");
//           }*/
//
//        } else {
//
//            ivDatePicker.setVisibility(View.GONE);
//            etMeta.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
//            if (length.equals("")) {
//
//
//            } else {
//
//                etMeta.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});
//
//            }
//            // etMeta.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
//        }
//
//
//        tv_star = rowView.findViewById(R.id.tv_upload_describes_mandatory);
//        if (mandatory.equals("Yes")) {
//
//
//            tv_star.setVisibility(View.VISIBLE);
//
//
//        } else {
//            tv_star.setVisibility(View.GONE);
//
//        }
//
////        ivDatePicker.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////
//////                // Get Current Date
//////                final Calendar c = Calendar.getInstance();
//////                mYear = c.get(Calendar.YEAR);
//////                mMonth = c.get(Calendar.MONTH);
//////                mDay = c.get(Calendar.DAY_OF_MONTH);
//////
//////
//////                new DatePickerDialog(CheckInActivity.this, new DatePickerDialog.OnDateSetListener() {
//////                    @Override
//////                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//////
//////
//////                        if (month <= 10) {
//////
//////                            etMeta.setText(year + "-" + "0" + month + "-" + dayOfMonth);
//////
//////
//////                        } else {
//////
//////                            etMeta.setText(year + "-" + month + "-" + dayOfMonth);
//////
//////                        }
//////
//////                        if (dayOfMonth <= 10) {
//////
//////                            etMeta.setText(year + "-" + "0" + month + "-" + "0" + dayOfMonth);
//////
//////                        } else {
//////
//////                            etMeta.setText(year + "-" + "0" + month + "-" + dayOfMonth);
//////                        }
//////
//////
//////                    }
//////                }, mYear, mMonth, mDay).show();
////
////
////            }
////        });
//
//        llMetadata.addView(rowView, llMetadata.getChildCount() - 1);
//
//
//    }
    //getting the list of metadata in dropdown of each indiviual folder dynamically
    private void checkMandatory(final String slid, final String docid) {

        progressBar.setVisibility(View.VISIBLE);
        tvNoMetaFound.setVisibility(View.GONE);
        rvFields.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECK_MANDATORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("mandatory", response);
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        rvFields.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        tvNoMetaFound.setVisibility(View.VISIBLE);

                    } else {

                        tvNoMetaFound.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fieldname = jsonArray.getJSONObject(i).getString("fieldname");
                            String size = jsonArray.getJSONObject(i).getString("length");
                            String mandatorystatus = jsonArray.getJSONObject(i).getString("mandatory");
                            String value = jsonArray.getJSONObject(i).getString("fieldvalue");
                            String datatype = jsonArray.getJSONObject(i).getString("data_type");


                            //5 times
                           // createDynamicView(fieldname, size, mandatorystatus,value,datatype);

                            checkinFieldsList.add(new CheckinFields(fieldname, size, mandatorystatus, value, datatype));

                        }

                        rvFields.setAdapter(new CheckinFieldAdapter(checkinFieldsList, CheckInActivity.this));
                        progressBar.setVisibility(View.GONE);
                        rvFields.setVisibility(View.VISIBLE);

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

                params.put("slidCheckin", slid);
                params.put("docidCheckin", docid);


                return params;
            }
        };

        VolleySingelton.getInstance(CheckInActivity.this).addToRequestQueue(stringRequest);
    }

    private JSONObject makeJsonObject(ArrayList<String> label, ArrayList<String> value,
                                      int count)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();


        for (int i = 0; i < count; i++) {

            obj = new JSONObject();

            try {
                obj.put("metaLabel", label.get(i));
                obj.put("metaEntered", value.get(i));


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("meta", jsonArray1);
        return finalobject;
    }


    public void uploadMultipart() {
        //getting the actual path of the image
        String path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filePath;
        }

        if (path == null) {

            Toast.makeText(CheckInActivity.this, "Please select any file for uploading", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code


            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);

                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                //pagecount of the pdf


                String ipadress = MainActivity.ip;
                String userId = MainActivity.userid;
                String pages = pageCount;
                String sliD = slid;
                String userName = MainActivity.username;


                Log.e("uploading ", ipadress + userId + pages + sliD + userName);

                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(CheckInActivity.this, uploadId, ApiUrl.VERSION);
                multipartUploadRequest.addFileToUpload(path, "checkin_file");


                Log.e("Upload_rate", UPLOAD_RATE);

          /*      uploadNotificationConfig.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                        + " at " + UPLOAD_RATE + " - " + PROGRESS;*/

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(CheckInActivity.this, 1, uploadId)));

                //uploadNotificationConfig.getProgress().iconResourceID = R.drawable.ic_upload;
                //uploadNotificationConfig.getProgress().iconColorResourceID = Color.BLUE;

                uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;

                //converting the drawable to bitmap
                // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_upload_blue_dark_24dp);
                //uploadNotificationConfig.getCompleted().largeIcon = bm;
                //uploadNotificationConfig.getCompleted().iconColorResourceID = Color.GREEN;

                uploadNotificationConfig.getError().message = "Error while uploading";
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                // uploadNotificationConfig.getError().iconColorResourceID = Color.RED;

                uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
                uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;
                // uploadNotificationConfig.getCancelled().iconColorResourceID = Color.YELLOW;

                //Adding file
                multipartUploadRequest

                        .addParameter("checkin_metadata", jsonobject.toString())
                        .addParameter("checkin_docid", docid)
                        .addParameter("checkin_pagecount", pageCount)
                        .addParameter("checkin_username", userName)
                        .addParameter("checkin_userid", userId)
                        .addParameter("checkin_ip", ipadress)

                        .setNotificationConfig(

                                uploadNotificationConfig
                                        .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                        .setClearOnActionForAllStatuses(true)


                        )

                        .setMaxRetries(5)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {


                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(CheckInActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                //progress.dismiss();

                                // Toast.makeText(CheckInActivity.this, "File upload Succesfully", Toast.LENGTH_LONG).show();

                                //{"message":"Updated Successfully !","error":"false"}
                                Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                                try {
                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                    String error = jsonObject.getString("error");
                                    String msg = jsonObject.getString("message");

                                    if (error.equalsIgnoreCase("false")) {

                                        //onBackPressed();

                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                        checkIn(docid, "ok");

                                    } else if (error.equalsIgnoreCase("true")) {


                                        Toast.makeText(CheckInActivity.this, msg, Toast.LENGTH_SHORT).show();


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                //UploadService.stopUpload(uploadId);


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Toast.makeText(CheckInActivity.this, "Upload Canceled", Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadId);
                            }
                        });


                if (isNetworkAvailable()) {

                    multipartUploadRequest.startUpload();

                    Toast.makeText(CheckInActivity.this, "Upload Started..", Toast.LENGTH_SHORT).show();


                } else {


                    Toast.makeText(CheckInActivity.this, "No Internet connection found", Toast.LENGTH_SHORT).show();

                }


            } catch (Exception exc) {
                Toast.makeText(CheckInActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
        //Toast.makeText(this,"File is uploading succesfully",Toast.LENGTH_LONG).show();

    }

    void CheckinWithOutUpload() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("checkin_metadata", jsonobject.toString());
                params.put("checkin_docid", docid);
                params.put("checkin_pagecount", pageCount);
                params.put("checkin_username", MainActivity.username);
                params.put("checkin_userid", MainActivity.userid);
                params.put("checkin_ip", MainActivity.ip);


                JSONObject k = new JSONObject(params);
                Log.e("jsonob volley", k.toString());


                return params;
            }
        };


    }

    void checkIn(final String docid, final String button) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECKIN_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response checkin", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    String error = jsonArray.getJSONObject(0).getString("error");
                    String msg = jsonArray.getJSONObject(0).getString("message");

                    if (error.equalsIgnoreCase("false")) {

                        if (button.equalsIgnoreCase("ok")) {

                            Toast.makeText(CheckInActivity.this, msg, Toast.LENGTH_SHORT).show();


                            if (mode.equalsIgnoreCase("fileviewActivity")) {

                                Intent intent = new Intent(CheckInActivity.this, FileViewActivity.class);
                                intent.putExtra("slid", slid);
                                intent.putExtra("fname", foldername);
                                startActivityForResult(intent, 420);
                                finish();
                            }

                          else if (mode.equalsIgnoreCase("metadatasearchActivity")) {
//
//                                Intent intent = new Intent(CheckInActivity.this, FileViewActivity.class);
//                                intent.putExtra("slid", slid);
//                                intent.putExtra("fname", foldername);
//                                intent.putExtra("metadata", jsonobject.toString());
//                                setResult(007,intent);
//
//                                Log.e("jsoBj in checkin",jsonobject.toString());
//
//                                finish();

                                onBackPressed();
                            }



                        } else if (button.equalsIgnoreCase("cancel")) {


                            if (mode.equalsIgnoreCase("fileviewActivity")) {

                                Intent intent = new Intent(CheckInActivity.this, FileViewActivity.class);
                                intent.putExtra("slid", slid);
                                intent.putExtra("fname", foldername);
                                startActivityForResult(intent, 420);
                                finish();
                            } else if (mode.equalsIgnoreCase("metadatasearchActivity")) {
//
//                                Intent intent = new Intent(CheckInActivity.this, MetaSearchFileViewActivity.class);
//                                intent.putExtra("slid", slid);
//                                intent.putExtra("fname", foldername);
//                                intent.putExtra("metadata", jsonobject.toString());
//
//                                Log.e("jsoBj in checkin",jsonobject.toString());
//
//                                setResult(007,intent);
//                                finish();

                                onBackPressed();

                            }


                        }


                        // checkIn(docid);

                    } else if (error.equalsIgnoreCase("true")) {


                        Toast.makeText(CheckInActivity.this, msg, Toast.LENGTH_SHORT).show();


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
                params.put("checkin", docid);


                return params;
            }
        };


        VolleySingelton.getInstance(CheckInActivity.this).addToRequestQueue(stringRequest);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                        .withActivity(CheckInActivity.this)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckInActivity.this);
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
        tvFilePath.setText("");

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


}
