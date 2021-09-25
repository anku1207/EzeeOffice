package in.cbslgroup.ezeeoffice.Activity.Dms;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shuhart.stepview.StepView;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Upload.DescribesFragment;
import in.cbslgroup.ezeeoffice.Upload.UploadFileFragment;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class UploadActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static String filePath;
    public static int currentStep = 0;
    public static StepView stepView;
    public static String filetypedynamic;
    public static String fileName;
    public static int noOfPages;
    public static long filesize;
    Fragment fragment = null;
    Button btn1, btn2, btn3, btn4, btnChooseFile, btnUpload;

    SessionManager sessionManager;
    String session_product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        //AllowRunTimePermission();
        requestPermission();

        sessionManager = new SessionManager(UploadActivity.this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Toolbar toolbar = findViewById(R.id.toolbar_upload);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();


            }
        });


        JSONObject js = new JSONObject(sessionManager.getUserDetails());


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_upload_root, new DescribesFragment());
        ft.commit();

        stepView = findViewById(R.id.step_view);


        checkPermission(MainActivity.userid);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {

            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.e("filepath in activity", filePath);

            UploadFileFragment.ivFileType.setVisibility(View.VISIBLE);
            UploadFileFragment.cvCameraImg.setVisibility(View.GONE);


            File f = new File(filePath);
            filesize = f.length() / 1024;

            Log.e("filesize in upload", String.valueOf(filesize));
            Log.e("pages", String.valueOf(noOfPages));

            String filename = f.getName();
            UploadFileFragment.tvFilePath.setText(filename);
            UploadFileFragment.tvFilePath.setError(null);

            fileName = filename;
            filetypedynamic = filename.substring(filename.lastIndexOf(".") + 1);

            Log.e("filetypedyanmic", filetypedynamic);

            switch (filetypedynamic.toLowerCase()) {
                case "pdf":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.pdf);
                    break;

                case "jpg":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.jpg);
                    break;

                case "png":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.png);
                    break;

                case "jpeg":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.jpeg);
                    break;

                case "gif":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.gif);

                    break;

                case "tiff":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.tiff);
                    break;

                case "tif":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.tif);
                    break;

                case "bmp":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.bmp);
                    break;

                case "doc":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.doc);
                    break;


                case "docx":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.docx);
                    break;


                case "txt":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.unsupported_file);
                    break;


                case "psd":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.psd);
                    break;


                case "mp4":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.mp4);
                    break;

                case "3gp":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.threegp);
                    break;

                case "mp3":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.mp3);
                    break;


                default:
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.unsupported_file);
                    break;
            }


        }


        // handle result of CropImageActivity
        else if (requestCode == 203) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                //Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();

                UploadFileFragment.cvCameraImg.setVisibility(View.VISIBLE);
                UploadFileFragment.ivCameraPreview.setImageURI(result.getUri());

                Log.e("image uri crop", result.getUri().toString());

                String uri = result.getUri().toString();
                String path = uri.substring(7);

                Log.e("path crop", path);

                UploadFileFragment.filePath = path;

                filePath = path;

                File f = new File(filePath);
                filesize = f.length() / 1024;

                Log.e("filesize in upload", String.valueOf(filesize));
                Log.e("pages", String.valueOf(noOfPages));

                String filename = f.getName();
                UploadFileFragment.tvFilePath.setText(filename);
                UploadFileFragment.tvFilePath.setError(null);

                fileName = filename;
                filetypedynamic = filename.substring(filename.lastIndexOf(".") + 1);
                Log.e("filetypedyanmic", filetypedynamic);


            }

            // btnChooseFile.setText("PDF is Selected");
            // btnChooseFile.setBackgroundColor(Color.RED);

        }

    }


    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(UploadActivity.this, "READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
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


    void checkPermission(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String intiate_file = jsonObject.getString("initiate_file");
                    String workflow_initiate_file = jsonObject.getString("workflow_initiate_file");


                    List<String> steps = new ArrayList<>();
                    steps.add("DESCRIBES");
                    steps.add("UPLOAD");
                    steps.add("VERIFY" + "\n" + "&" + "\n" + "COMPLETE");


                    if (intiate_file.equalsIgnoreCase("1") || workflow_initiate_file.equals("1")) {

                        Log.e("new step added", "EzeeProcess");
                        steps.add("UPLOAD" + "\n" + "IN" + "\n" + "WORKFLOW");

                    }

                    stepView.setSteps(steps);

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
                params.put("action","getSpecificRoles");


                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                JSONObject js = new JSONObject(params);
                Log.e("getuserper params", js.toString());

                return params;
            }
        };


        VolleySingelton.getInstance(UploadActivity.this).addToRequestQueue(stringRequest);


    }


    String setRoles() {

        //checking the specific roles
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("initiate_file").append(",")
                .append("workflow_initiate_file");

        return stringBuilder.toString();
    }


}
