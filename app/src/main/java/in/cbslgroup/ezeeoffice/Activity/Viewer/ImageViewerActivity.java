package in.cbslgroup.ezeeoffice.Activity.Viewer;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class ImageViewerActivity extends AppCompatActivity {

    // ImageView ivImageViewer;
    private static final String IMAGE_URL = ApiUrl.BASE_URL;
    String filepath, filename, fileType, fileSize, fileDate,docid;

    TextView tvName, tvSize, tvDate;

    LinearLayout llPanel, llNoFileFound;
    ProgressBar progressBar;

    ProgressDialog mProgressDialog;

    Toolbar toolbar;

    Glide glide;

    DownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);


        toolbar = findViewById(R.id.toolbar_image_viewer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });


        // ivImageViewer = findViewById(R.id.iv_image_viewer);
        PhotoView photoView = findViewById(R.id.iv_image_viewer);

        Intent intent = getIntent();
        // filepath = intent.getStringExtra("filepath");
        //filepath="images/1501762341_sample-pan-card-front.jpg";
        filename = intent.getStringExtra("name");
        filepath = intent.getStringExtra("path");
        fileType = intent.getStringExtra("type");
        fileDate = intent.getStringExtra("date");
        fileSize = intent.getStringExtra("size");
        docid = intent.getStringExtra("docid");

        Log.e("filepath image ", filepath);

        tvName = findViewById(R.id.tv_imageviewer_name);
        tvDate = findViewById(R.id.tv_imageviewer_date);
        tvSize = findViewById(R.id.tv_imageviewer_size);

        tvName.setText(filename);
        tvSize.setText(fileSize);
        tvDate.setText(fileDate);

        progressBar = findViewById(R.id.progressBar_Glide);
        llPanel = findViewById(R.id.ll_image_viewer_panel);
        llNoFileFound = findViewById(R.id.ll_image_viewer_no_file_found);

        progressBar.setVisibility(View.VISIBLE);
        //progressBar.setLayoutParams(new LinearLayout.LayoutParams());
        if (fileType.equals("gif")) {

            Glide.with(ImageViewerActivity.this)
                    .asGif()
                    .load(IMAGE_URL + filepath)
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions() .crossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            llPanel.setVisibility(View.GONE);
                            llNoFileFound.setVisibility(View.VISIBLE);
                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
                            }
                            Log.e("Glide exception ", String.valueOf(e));
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            llNoFileFound.setVisibility(View.GONE);
                            llPanel.setVisibility(View.VISIBLE);
                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
                            }
                            return false;
                        }
                    })
                    .into(photoView);


        }
        else if (fileType.equalsIgnoreCase("bmp"))

        {

            Glide.with(ImageViewerActivity.this)
                    .asBitmap()
                    .load(IMAGE_URL + filepath)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            llPanel.setVisibility(View.GONE);
                            llNoFileFound.setVisibility(View.VISIBLE);
                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
                            }
                            Log.e("Glide exception ", String.valueOf(e));
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            llNoFileFound.setVisibility(View.GONE);
                            llPanel.setVisibility(View.VISIBLE);
                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
                            }
                            return false;
                        }
                    })
                   .into(photoView)
            ;






        }



        else {


            Glide.with(ImageViewerActivity.this)
                    .load(IMAGE_URL + filepath)
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions() .crossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            llPanel.setVisibility(View.GONE);
                            llNoFileFound.setVisibility(View.VISIBLE);
                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(false);
                            }
                            Log.e("Glide exception ", String.valueOf(e));
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            llNoFileFound.setVisibility(View.GONE);
                            llPanel.setVisibility(View.VISIBLE);
                            if(toolbar.getMenu().findItem(R.id.action_image_viewer_download)!=null){
                                toolbar.getMenu().findItem(R.id.action_image_viewer_download).setVisible(true);
                            }
                            return false;
                        }
                    })
                    .into(photoView);
        }

       //creating log for image view
       createViewFileLog(MainActivity.userid,docid,MainActivity.ip, DmsActivity.dynamicFileSlid,filename);

    }

    void createViewFileLog(String userid,String docid ,String ip,String slid,String filename){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("log_viewed",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<>();
                params.put("userid_log",userid);
                params.put("docid_log",docid);
                params.put("ip_log",ip);
                params.put("slid_log",slid);
                params.put("filename_log",filename);

                Util.printParams(params,"log_params");

                return params;
            }
        };

        VolleySingelton.getInstance(ImageViewerActivity.this).addToRequestQueue(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.imageviewer_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_image_viewer_download) {

            mProgressDialog = new ProgressDialog(ImageViewerActivity.this);
            mProgressDialog.setMessage("File is downloading");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);


            DownloadFile(IMAGE_URL + filepath);


            BroadcastReceiver onComplete=new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    // Do Something
                    mProgressDialog.dismiss();

                }
            };

            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));



            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    //Downloading file
    public void DownloadFile(String url) {


        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
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


//    //Download file from server
//    public class DownloadFile extends AsyncTask<String, Integer, String> {
//
//        NotificationManager mNotifyManager;
//        NotificationCompat.Builder mBuilder;
//        private Context context;
//        private PowerManager.WakeLock mWakeLock;
//
//        public DownloadFile(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(String... sUrl) {
//            InputStream input = null;
//            OutputStream output = null;
//            HttpURLConnection connection = null;
//
//
//            String filename = sUrl[0].substring(sUrl[0].lastIndexOf("/"), sUrl[0].length());
//            String filetype = filename.substring(filename.lastIndexOf("."), filename.length());
//            try {
//                URL url = new URL(sUrl[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                // expect HTTP 200 OK, so we don't mistakenly save error report
//                // instead of the file
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return "Server returned HTTP " + connection.getResponseCode()
//                            + " " + connection.getResponseMessage();
//                }
//
//                // this will be useful to display download percentage
//                // might be -1: server did not report the length
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                input = connection.getInputStream();
//
//                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeProcess Downloads");
//
//                if (!myDirectory.exists()) {
//                    myDirectory.mkdirs();
//
//                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/" + filename + "." + filetype);
//                } else {
//                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/" + filename + "." + filetype);
//                }
//
//
//                byte data[] = new byte[4096];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    // allow canceling with back button
//                    if (isCancelled()) {
//                        input.close();
//                        return null;
//                    }
//                    total += count;
//                    // publishing the progress....
//                    if (fileLength > 0) // only if total length is known
//                        publishProgress((int) (total * 100 / fileLength));
//                    output.write(data, 0, count);
//                }
//            } catch (Exception e) {
//                return e.toString();
//            } finally {
//                try {
//                    if (output != null)
//                        output.close();
//                    if (input != null)
//                        input.close();
//                } catch (IOException ignored) {
//                }
//
//                if (connection != null)
//                    connection.disconnect();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // take CPU lock to prevent CPU from going off if the user
//            // presses the power button during download
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                    getClass().getName());
//            mWakeLock.acquire();
//
//           /* mNotifyManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mBuilder = new NotificationCompat.Builder(context);
//
//            mBuilder.setContentTitle("File Download")
//                    .setContentText("Download in progress")
//                    .setColor(context.getResources().getColor(R.color.colorPrimary))
//                    .setSmallIcon(R.drawable.ic_file_download_white_24dp)*/
//            ;
//
//
//            //Toast.makeText(context, "Downloading the file... The download progress is on notification bar.", Toast.LENGTH_LONG).show();
//
//            mProgressDialog.show();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            // if we get here, length is known, now set indeterminate to false
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(progress[0]);
//
//           /* mBuilder.setProgress(100, progress[0], false);
//            // Displays the progress bar on notification
//            mNotifyManager.notify(0, mBuilder.build());*/
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            mWakeLock.release();
//            mProgressDialog.dismiss();
//            if (result != null)
//                Toast.makeText(context, "Download error", Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(context, "File downloaded Succesfully", Toast.LENGTH_SHORT).show();
//           /* mBuilder.setContentTitle("File Download Succesfully")
//                     .setContentText("")
//                    .setColor(context.getResources().getColor(R.color.colorPrimary))
//                    .setSmallIcon(R.drawable.ic_file_download_white_24dp);
//
//             mNotifyManager.notify(0, mBuilder.build());*/
//
//
//        }
//    }

}
