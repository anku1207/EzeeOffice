package in.cbslgroup.ezeeoffice.Adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;




import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


import java.io.File;
import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Player.VideoPlayerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.ImageViewerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.PdfViewerActivity;
import in.cbslgroup.ezeeoffice.Model.DocumentListWorkflow;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;

public class WorkflowDocListAdapter extends RecyclerView.Adapter<WorkflowDocListAdapter.ViewHolder> {

    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;

    DownloadManager downloadManager;

    List<DocumentListWorkflow> documentListWorkflow;
    Context context;
    PowerManager.WakeLock wakeLock;

    ProgressDialog mProgressDialog;
    AlertDialog alertDialog;

    public WorkflowDocListAdapter(List<DocumentListWorkflow> documentListWorkflow, Context context) {
        this.documentListWorkflow = documentListWorkflow;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workflow_document_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DocumentListWorkflow documentList = documentListWorkflow.get(position);

        holder.tvFilename.setText(documentList.getDocname());
        holder.tvExtension.setText(documentList.getExtension());
        holder.tvPath.setText(documentList.getDocpath());
        holder.tvDocid.setText(documentList.getDocid());

        final String filetype = holder.tvExtension.getText().toString().trim().toLowerCase();
        final String filename = holder.tvFilename.getText().toString().trim();
        final String filepath = holder.tvPath.getText().toString().trim();
        final  String docid = documentList.getDocid();


        switch (filetype.toLowerCase()) {


            case "apk":
                holder.ivFiletype.setImageResource(R.drawable.ic_android_green_24dp);
                break;


            case "bmp":
                holder.ivFiletype.setImageResource(R.drawable.bmp_48px);
                break;

            case "pdf":

                holder.ivFiletype.setImageResource(R.drawable.pdf_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.pdf);

                break;
            case "jpg":
                holder.ivFiletype.setImageResource(R.drawable.jpg_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.jpg);

                break;
            case "png":
                holder.ivFiletype.setImageResource(R.drawable.png_128px);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "gif":
                holder.ivFiletype.setImageResource(R.drawable.gif_128px);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "jpeg":
                holder.ivFiletype.setImageResource(R.drawable.jpeg_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.jpeg);

                break;

            case "tiff":
                holder.ivFiletype.setImageResource(R.drawable.tiff_128px);
                break;
            case "tif":
                holder.ivFiletype.setImageResource(R.drawable.tif_128px);
                break;

            case "doc":
                holder.ivFiletype.setImageResource(R.drawable.doc_128px);
                break;

            case "txt":
                holder.ivFiletype.setImageResource(R.drawable.unsupported_file_128px);
                break;

            case "docx":
                holder.ivFiletype.setImageResource(R.drawable.docx_128px);
                break;


            case "psd":
                holder.ivFiletype.setImageResource(R.drawable.psd_128px);
                break;

            case "mp4":

                holder.ivFiletype.setImageResource(R.drawable.mp4_128px);

                break;


            case "3gp":
                holder.ivFiletype.setImageResource(R.drawable.threegp_128px);

                break;

            case "mp3":
                holder.ivFiletype.setImageResource(R.drawable.mp3_128px);

                break;

            default:

                holder.ivFiletype.setImageResource(R.drawable.unsupported_file_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.file);
                break;


        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filepath == null || filepath.isEmpty()) {

                    Log.e("file path", "null");
                    Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                } else {

                    if ( filetype.equals("psd")
                            || filetype.equals("pdf")
                            || filetype.equals("doc")
                            || filetype.equals("docx")
                            || filetype.equalsIgnoreCase("tif")
                            || filetype.equalsIgnoreCase("tiff")) {
                        //holder.ivFiletype.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        intent.putExtra("docid", docid);
                        Log.e("doc path list pdf",filepath);
                        context.startActivity(intent);

                    } else if (filetype.equals("jpg")
                            || filetype.equals("jpeg")
                            || filetype.equals("png")
                            || filetype.equals("bmp")
                            || filetype.equals("gif")) {
                        // holder.ivFiletype.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", "-");
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", "-");
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", docid);

                        Log.e("doc path list pic",filepath);


                        context.startActivity(intent);

                    }else if (filetype.equals("mp4") || filetype.equals("3gp")) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        context.startActivity(intent);

                    } else if (filetype.equalsIgnoreCase("mp3")) {

                        showAudioPlayerDialog(context, filepath, filename);

                    } else {
                        // holder.ivFiletype.setImageResource(R.drawable.ic_round_file_light_blue);
                        //Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Download File");
                        alertDialog.setIcon(R.drawable.file);
                        alertDialog.setMessage("This is file is not supported instead of this you can download the file so do you want to download"+" "+filename+"."+filetype+"?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        Log.e("doc path list",DOCUMENT_URL+filepath);

                                        mProgressDialog = new ProgressDialog(context);
                                        mProgressDialog.setMessage("File is downloading");
                                        mProgressDialog.setIndeterminate(true);
                                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        mProgressDialog.setCancelable(false);


                                      /*  Intent intent = new Intent(context, DownloadService.class);
                                        intent.putExtra("url", DOCUMENT_URL+filepath);
                                        intent.putExtra("filename",filename);
                                        intent.putExtra("type",filetype);
                                        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                        context.startService(intent);*/


                                        DownloadFile(DOCUMENT_URL+filepath);
                                        //new DownloadFile(context).execute(DOCUMENT_URL+filepath);




                                    }
                                });
                        alertDialog.show();



                        //Log.e("File not supported","true");

                    }

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return documentListWorkflow.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPath, tvExtension, tvFilename, tvDocid;
        CardView cardView;
        ImageView ivFiletype;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cv_bottom_sheet_workflow_doclist_item);
            tvFilename = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_filename);
            tvExtension = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_fileextension);
            tvPath = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_filepath);
            ivFiletype = itemView.findViewById(R.id.iv_bottom_sheet_workflow_doc_filetype);
            tvDocid = itemView.findViewById(R.id.tv_bottom_sheet_workflow_doc_docid);


        }
    }


    void showAudioPlayerDialog(Context context, String filepath, String songname) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog_audio_player, null);
        dialogBuilder.setView(dialogView);

        //player
        SimpleExoPlayerView simpleExoPlayerView = dialogView.findViewById(R.id.iv_audio_player_dialog_audio_view);
        ImageView btnClose = dialogView.findViewById(R.id.iv_audio_player_dialog_close);
        TextView tvName = dialogView.findViewById(R.id.tv_audio_player_dialog_name);
        tvName.setText(songname);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DataSource.Factory dateSourceFactory = new DefaultDataSourceFactory(context, com.google.android.exoplayer2.util.Util.getUserAgent(context, context.getPackageName()));
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(ApiUrl.BASE_URL + "/" + filepath), dateSourceFactory, extractorsFactory, new Handler(), Throwable::printStackTrace);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(trackSelectionFactory));
        player.prepare(mediaSource);
        //Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setControllerHideOnTouch(false);
        simpleExoPlayerView.setControllerShowTimeoutMs(0);
        player.setPlayWhenReady(true);
        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                player.stop();

            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }



/*    public static class DownloadService extends IntentService {
        public static final int UPDATE_PROGRESS = 8344;


        public DownloadService(){
            super("DownloadService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String urlToDownload = intent.getStringExtra("url");
            String filename = intent.getStringExtra("filename");
            String type = intent.getStringExtra("type");
            ResultReceiver receiver = intent.getParcelableExtra("receiver");
            try {
                OutputStream output;

                URL url = new URL(urlToDownload);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(connection.getInputStream());

                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeFile Downloads");

                if(!myDirectory.exists()) {
                    myDirectory.mkdirs();

                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+filename+"."+type);
                }
                else{
                    output = new FileOutputStream("/sdcard/EzeeFile Downloads/"+filename+"."+type);
                }


                *//*OutputStream output = new FileOutputStream("/sdcard/BarcodeScanner-debug.apk");*//*

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    Bundle resultData = new Bundle();
                    resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                    receiver.send(UPDATE_PROGRESS, resultData);
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bundle resultData = new Bundle();
            resultData.putInt("progress" ,100);
            receiver.send(UPDATE_PROGRESS, resultData);
        }
    }

    private  class DownloadReceiver extends ResultReceiver{

        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == PdfViewerActivity.DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");


                mNotifyManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context);

                mBuilder.setContentTitle("File Download")
                        .setContentText("Download in progress")
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setProgress(100,progress,false)
                        .setAutoCancel(true)

                ;
                mNotifyManager.notify(0, mBuilder.build());

                PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        getClass().getName());
                wakeLock.acquire();

                //mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    Log.e("wakelock","working 1");
                    wakeLock.release();

                    Toast.makeText(context, "File Download Successfully", Toast.LENGTH_SHORT).show();
                    mNotifyManager.cancel(0);
            *//*        mBuilder.setContentTitle("File Download Successfully")
                            .setContentText("")
                            .setColor(getResources().getColor(R.color.colorPrimary))
                            .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                            //.setProgress(0,0,false)
                    ;
                    mNotifyManager.notify(0, mBuilder.build());*//*

                    Log.e("wakelock","working 2");

                    //mProgressDialog.dismiss();


                }
            }
        }
    }*/

//    //Download file from server
//    public class DownloadFile extends AsyncTask<String, Integer, String> {
//
//        private Context context;
//        private PowerManager.WakeLock mWakeLock;
//
//        NotificationManager mNotifyManager;
//        NotificationCompat.Builder mBuilder;
//
//        public DownloadFile(Context context) {
//            this.context = context;
//        }
//        @Override
//        protected String doInBackground(String... sUrl) {
//            InputStream input = null;
//            OutputStream output = null;
//            HttpURLConnection connection = null;
//
//
//            String filename = sUrl[0].substring(sUrl[0].lastIndexOf("/"),sUrl[0].length());
//            String filetype = filename.substring(filename.lastIndexOf("."),filename.length());
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
//                if(!myDirectory.exists()) {
//                    myDirectory.mkdirs();
//
//                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/"+filename+"."+filetype);
//                }
//                else{
//                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/"+filename+"."+filetype);
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
//                     ;
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
//                Toast.makeText(context,"Download error", Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(context,"File downloaded Succesfully", Toast.LENGTH_SHORT).show();
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


    //Downloading file
    public void DownloadFile(String url) {

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
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


}
