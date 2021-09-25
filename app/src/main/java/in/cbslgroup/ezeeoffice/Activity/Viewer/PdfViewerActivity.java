package in.cbslgroup.ezeeoffice.Activity.Viewer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Fragments.ActivityLogFragment;
import in.cbslgroup.ezeeoffice.Fragments.ReviewLogFragment;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.NonSwipeableViewPager;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class PdfViewerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PDF_Url = ApiUrl.BASE_URL;
    PDFView pdfView;
    TextView pdftextview;
    Integer pageNumber = 0;
    ProgressBar progressBar = null;
    String pdfName, filepath, docid;
    ImageView ivDownloadbtn, ivBack;
    TextView tvpdfname;
    NonSwipeableViewPager viewPager;
    TabLayout tabLayout;
    NavigationView navigationView;
    View navView;
    DrawerLayout drawer;
    DownloadManager downloadManager;
    Toolbar toolbar;

    //
    String extension;
    // String onlyFileName;
    String pdfPathConverted = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        AllowRunTimePermission();

        toolbar = findViewById(R.id.toolbar_pdf_viewer);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();


            }
        });


        ivBack = findViewById(R.id.iv_pdfview_backbutton);
        ivDownloadbtn = findViewById(R.id.iv_pdfview_downloadbutton);
        tvpdfname = findViewById(R.id.tv_pdfview_filename);


        drawer = findViewById(R.id.drawer_layout_pdf_viewer);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        navigationView = findViewById(R.id.nav_view_pdf_viewer);
        navigationView.setNavigationItemSelectedListener(this);
        navView = navigationView.getHeaderView(0);


        viewPager = navView.findViewById(R.id.pdf_viewer_viewpager);
        //setupViewPager(viewPager);

        tabLayout = navView.findViewById(R.id.pdf_viewer_tablayout);
        // tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        pdfName = intent.getStringExtra("filename");
        filepath = intent.getStringExtra("filepath");
        docid = intent.getStringExtra("docid");

        tvpdfname.setText(pdfName);
        toolbar.setSubtitle(pdfName);


        viewPager = navView.findViewById(R.id.pdf_viewer_viewpager);
        tabLayout = navView.findViewById(R.id.pdf_viewer_tablayout);
        pdfView = findViewById(R.id.pdfView);


        pdfView.isBestQuality();
        pdfView.fitToWidth(1);
        pdfView.enableAnnotationRendering(true);
        pdftextview = findViewById(R.id.pdftextview);

        extension = pdfName.substring(pdfName.lastIndexOf(".") + 1);
        // onlyFileName = pdfName.substring(0, pdfName.lastIndexOf("."));


        Log.e("Extension : ", extension);
        //Log.e("Name : ", onlyFileName);

        if (extension.equalsIgnoreCase("doc")
                || extension.equalsIgnoreCase("docx")
                || extension.equalsIgnoreCase("tif")
                || extension.equalsIgnoreCase("tiff")
                || extension.equalsIgnoreCase("psd")
                || extension.equalsIgnoreCase("txt")) {

            toolbar.setTitle(extension.toUpperCase() + " Viewer");
            convertToPdf(filepath);

        } else {

            //in case of pdf
            toolbar.setTitle("PDF Viewer");
            new RetrivePDFstream().execute(PDF_Url + filepath);


        }


        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int availableHeight = navigationView.getMeasuredHeight();
                int availableWidth = Util.dpToPx(330);
                if (availableHeight > 0) {
                    navigationView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    //save height here and do whatever you want with it
                    if (navigationView.getHeaderView(0) != null) {

                        LinearLayout llPagerMain = navView.findViewById(R.id.ll_view_pager_logs);
                        Log.e("widht 1", String.valueOf(availableWidth));
                        Log.e("height 2", String.valueOf(availableHeight));
                        llPagerMain.setLayoutParams(new LinearLayout.LayoutParams(availableWidth, availableHeight));


                    }
                }
            }
        });

        //create log
        createViewFileLog(MainActivity.userid, docid, MainActivity.ip, DmsActivity.dynamicFileSlid, pdfName);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_pdf_viewer);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (extension.equals("doc")
                || extension.equals("docx")
                || extension.equalsIgnoreCase("tif")
                || extension.equalsIgnoreCase("tiff")
                || extension.equalsIgnoreCase("psd")
                || extension.equalsIgnoreCase("txt")) {

            if (pdfPathConverted == null) {

                super.onBackPressed();
                finish();

            } else {

                if (pdfPathConverted.equals("null")) {

                    super.onBackPressed();
                    finish();
                } else {

                    delConvertedFile(pdfPathConverted);
                }

            }


        } else {

            super.onBackPressed();
            finish();

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_pdf_viewer_download: {


                AlertDialog alertDialog = new AlertDialog.Builder(PdfViewerActivity.this).create();

                switch (extension.toLowerCase()) {

                    case "pdf":

                        alertDialog.setTitle("Download " + extension);
                        alertDialog.setIcon(R.drawable.pdf);
                        break;

                    case "doc":
                        alertDialog.setTitle("Download " + extension);
                        alertDialog.setIcon(R.drawable.doc);
                        break;

                    case "tif":
                        alertDialog.setTitle("Download " + extension);
                        alertDialog.setIcon(R.drawable.tif);
                        break;

                    case "tiff":
                        alertDialog.setTitle("Download " + extension);
                        alertDialog.setIcon(R.drawable.tiff);
                        break;

                    case "docx":
                        alertDialog.setTitle("Download " + extension);
                        alertDialog.setIcon(R.drawable.docx);
                        break;

                    case "psd":
                        alertDialog.setTitle("Download " + extension);
                        alertDialog.setIcon(R.drawable.psd);
                        break;


                }
                alertDialog.setMessage("Are you sure you want to download" + " " + tvpdfname.getText() + " " + "?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Downloadpdf();


                            }
                        });
                alertDialog.show();


                return true;
            }

            case R.id.action_pdf_viewer_log: {


                //To Open drawer by button click:
                drawer.openDrawer(GravityCompat.END);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

               /*Intent intent = new Intent(PdfViewerActivity.this,OtpActivity.class);
               startActivity(intent);*/


                return true;
            }


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(PdfViewerActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(PdfViewerActivity.this, "Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(PdfViewerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(PdfViewerActivity.this, "WRITE_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(PdfViewerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }

    void createViewFileLog(String userid, String docid, String ip, String slid, String filename) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("log_viewed", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid_log", userid);
                params.put("docid_log", docid);
                params.put("ip_log", ip);
                params.put("slid_log", slid);
                params.put("filename_log", filename);

                Util.printParams(params, "log_params");

                return params;
            }
        };

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);


    }

    void getUserRole(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String pdf_download = jsonObject.getString("pdf_download");
                    String actvity_log = jsonObject.getString("wf_log");
                    String review_log = jsonObject.getString("review_log");


                    if (pdf_download.equalsIgnoreCase("0")) {


                        ivDownloadbtn.setVisibility(View.GONE);
                        toolbar.getMenu().findItem(R.id.action_pdf_viewer_download).setVisible(false);

                    } else {

                        ivDownloadbtn.setVisibility(View.VISIBLE);
                        toolbar.getMenu().findItem(R.id.action_pdf_viewer_download).setVisible(true);

                    }


                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                    Bundle bundle = new Bundle();
                    bundle.putString("docid", docid);


                    if (review_log.equalsIgnoreCase("1") || actvity_log.equalsIgnoreCase("1")) {

                        toolbar.getMenu().findItem(R.id.action_pdf_viewer_log).setVisible(true);

                        if (review_log.equalsIgnoreCase("1")) {
                            ActivityLogFragment activityLogFragment = new ActivityLogFragment();
                            activityLogFragment.setArguments(bundle);
                            adapter.addFragment(activityLogFragment, "Actvity Logs");

                        }

                        if (actvity_log.equalsIgnoreCase("1")) {

                            ReviewLogFragment reviewLogFragment = new ReviewLogFragment();
                            reviewLogFragment.setArguments(bundle);
                            adapter.addFragment(reviewLogFragment, "Review Logs");

                        }


                    } else {

                        toolbar.getMenu().findItem(R.id.action_pdf_viewer_log).setVisible(false);

                    }


                    viewPager.setAdapter(adapter);

                    Log.e("view_count", String.valueOf(adapter.getCount()));


                    // setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);


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

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pdf_viewer, menu);
        return true;
    }

    public void Downloadpdf() {

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(PDF_Url + filepath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeOffice Downloads");

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        String PATH = "/EzeeOffice Downloads/";
        request.setDestinationInExternalPublicDir(PATH, uri.getLastPathSegment());
        downloadManager.enqueue(request);


    }

    class RetrivePDFstream extends AsyncTask<String, Integer, InputStream> {

//        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar = findViewById(R.id.progressbarpdf);
            progressBar.setVisibility(View.VISIBLE);


        }


        @SuppressLint("WrongThread")
        @Override
        protected InputStream doInBackground(String... strings) {
            progressBar.setVisibility(View.VISIBLE);
            InputStream inputStream = null;


            try {

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                int fileLength = urlConnection.getContentLength();


                if (urlConnection.getResponseCode() == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }


            } catch (IOException e) {

                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(final InputStream inputStream) {


            pdfView.fromStream(inputStream)
                    .spacing(5) // in dp
                    .scrollHandle(new DefaultScrollHandle(PdfViewerActivity.this))
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .onPageChange(new OnPageChangeListener() {


                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            pageNumber = page;

                            int pageplusone = page + 1;

                            // android:text="Page no : 3  out of : 27
                            // String counterText = "Page no : "+pageplusone+" out of : "+pageCount;
                            String counterText = pageplusone + "/" + pageCount;
                            pdftextview.setText(counterText);
                            pdftextview.setVisibility(View.VISIBLE);

                        }
                    })
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {

                            Toast.makeText(PdfViewerActivity.this, "No file found", Toast.LENGTH_SHORT).show();

                            onBackPressed();
                        }
                    })

                    .onPageError(new OnPageErrorListener() {
                        @Override
                        public void onPageError(int page, Throwable t) {
                            Toast.makeText(PdfViewerActivity.this, t.getMessage() + "pg", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {

                            progressBar.setVisibility(View.INVISIBLE);

                            getUserRole(MainActivity.userid);

                            //  ivDownloadbtn.setVisibility(View.VISIBLE);


                        }
                    })

                    .load();


        }


    }

    void convertToPdf(String filepath) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CONVERT_TO_PDF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ToPdf res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");

                    if (error.equals("false")) {

                        pdfPathConverted = jsonObject.getString("pdf_path");

                        new RetrivePDFstream().execute(PDF_Url + pdfPathConverted);

                    } else if (error.equals("true")) {

                        Toast.makeText(PdfViewerActivity.this, "Error opening file", Toast.LENGTH_SHORT).show();
                        PdfViewerActivity.super.onBackPressed();

                    } else {

                        Toast.makeText(PdfViewerActivity.this, "Error opening file", Toast.LENGTH_SHORT).show();
                        PdfViewerActivity.super.onBackPressed();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PdfViewerActivity.this, "Error opening file", Toast.LENGTH_SHORT).show();
                PdfViewerActivity.super.onBackPressed();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("docpath", filepath);
                Util.printParams(params, "convert_pdf_param");

                return params;
            }
        };

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);

    }

    void delConvertedFile(String docpath) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CONVERT_TO_PDF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Delete res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equals("false")) {

                        Log.e("pdf_del", msg);

                        PdfViewerActivity.super.onBackPressed();

                    } else if (error.equals("true")) {
                        Log.e("pdf_del", msg);
                        PdfViewerActivity.super.onBackPressed();
                        finish();

                    } else {

                        Log.e("pdf_del", "Pdf deletion failed");
                        PdfViewerActivity.super.onBackPressed();
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("pdf_del", "Pdf deletion failed");
            }


        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("pdf_docpath", docpath);
                Util.printParams(params, "del_pdf_param");

                return params;
            }
        };

        VolleySingelton.getInstance(PdfViewerActivity.this).addToRequestQueue(stringRequest);


    }

    String setRoles() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pdf_download").append(",")
                .append("wf_log").append(",")
                .append("review_log");

        return stringBuilder.toString();
    }


}

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
