package in.cbslgroup.ezeeoffice.Upload;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.Activity.Dms.UploadActivity;
import in.cbslgroup.ezeeoffice.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFileFragment extends Fragment {

    public static TextView tvFilePath,tvFilePathFull;

    public  static ImageView ivFileType;
    Button btnChoose;
    String filetype;

    public static Button btnNxt,btnBack;
    BottomSheetDialog bottomSheetDialog;
    static final String TAG = "photo";
    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static CardView cvCameraImg;
    public static ImageView ivCameraPreview;
    public static String filePath;

    RelativeLayout rlmain;

    public UploadFileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        rlmain = container.findViewById(R.id.rl_snackbar);

        View v = inflater.inflate(R.layout.fragment_upload_file, container, false);
        tvFilePath = v.findViewById(R.id.tv_upload_filepath);
        ivFileType = v.findViewById(R.id.iv_uploadfrag_dynamic_filetype);
        cvCameraImg = v.findViewById(R.id.cv_upload_fragment_upload_camera);
        ivCameraPreview = v.findViewById(R.id.iv_upload_fragment_upload_camera);

        btnChoose = v.findViewById(R.id.btn_upload_choose_file);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withRequestCode(123)
                        .withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png)$",Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();*/

              showBottomSheetDialog();


            }
        });

        UploadActivity.stepView.done(false);

        btnBack = v.findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                UploadActivity.stepView.done(false);


         /*       ArrayList<String> list = getArguments().getStringArrayList("values");

                for (String names:
                        list) {

                    Log.e("metaaentered",names);

                }




                Bundle bundle = new Bundle();
                bundle.putStringArrayList("values",list);*/

                DescribesFragment fragment = new DescribesFragment();
                //fragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right,R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, fragment);
                ft.commit();

                UploadActivity.stepView.go(0,true);

            }
        });

        btnNxt = v.findViewById(R.id.next);
        btnNxt.setBackground(getResources().getDrawable(R.drawable.selector_btn_enable_disable));
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String filepath = String.valueOf(tvFilePath.getText());
                if(filepath.isEmpty()|| filepath.equals("")){

                    tvFilePath.setError("No file choosen");

                }
                else{

                    tvFilePath.setError(null);
                    UploadActivity.stepView.go(2,true);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.left_to_right,R.anim.right_to_left);
                    ft.replace(R.id.fl_upload_root, new VerifyAndCompleteFragment());
                    ft.commit();

                }



            }
        });

        return v;
    }


    public void showBottomSheetDialog() {


        View view = getLayoutInflater().inflate(R.layout.bottomsheet_choose, null);

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(view);

        LinearLayout llCamera = bottomSheetDialog.findViewById(R.id.ll_aproved_reject_take_photo);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                        startActivityForResult(cameraIntent, 100);

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
                        .withActivity(getActivity())
                        .withRequestCode(123)
                        .withFilter(Pattern.compile(".*\\.(pdf|jpg|jpeg|png|mp4|3gp|mp3|doc|docx|tiff|tif|psd|txt|bmp)$", Pattern.CASE_INSENSITIVE))// Filtering files and directories by file name using regexp
                        .withFilterDirectories(false) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();

                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {


            cvCameraImg.setVisibility(View.VISIBLE);
            ivFileType.setVisibility(View.GONE);

            filePath = mCurrentPhotoPath;

            Uri uri = Uri.parse(filePath);

            Log.e("uri", uri.toString());

            startCropImageActivity(uri);

            String p = filePath.substring(filePath.lastIndexOf("/") + 1);
            tvFilePath.setText(p);


        }

      /*  // handle result of CropImageActivity
        if (requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {


                Toast.makeText(getActivity(), "working", Toast.LENGTH_SHORT).show();

                cvCameraImg.setVisibility(View.VISIBLE);
                ivCameraPreview.setImageURI(result.getUri());

                Log.e("image uri crop", result.getUri().toString());

                String uri = result.getUri().toString();
                String path = uri.substring(7, uri.length());

                Log.e("path crop", path);

                filePath = path;

            }

        }
*/
    }


    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
                .setActivityMenuIconColor(getResources().getColor(R.color.white))
                .setAllowRotation(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity());
    }






}
