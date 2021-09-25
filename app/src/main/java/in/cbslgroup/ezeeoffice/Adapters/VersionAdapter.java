package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


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
import com.google.android.exoplayer2.util.Util;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Viewer.FileViewActivity;
import in.cbslgroup.ezeeoffice.Interface.onFileClick;
import in.cbslgroup.ezeeoffice.Model.Version;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;


public class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.ViewHolder> {

    List<Version> versionList;
    Context context;
    onFileClick listener;

    ProgressDialog mProgressDialog;
    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;

    DownloadManager downloadManager;
    AlertDialog alertDialog;
    OnButtonClickListener onButtonClickListener;

    public VersionAdapter(List<Version> versionList, Context context) {
        this.versionList = versionList;
        this.context = context;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }


    interface OnButtonClickListener {

        void onFileViewButtonClick(String path, String filetype, String filename, String docid, String docpath, int pos);

        void onDeleteVersionButtonClick(String docid, int pos);

        void onMetaViewButtonClick(String docname, String docid);

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewfiletype) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.versionlist_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        if (position == versionList.size() - 1) {

            holder.decorator.setVisibility(View.GONE);
        }

        holder.tvVersion.setText(versionList.get(position).getDocVersionName());
        holder.tvDocname.setText(versionList.get(position).getDocName());
        holder.tvDocid.setText(versionList.get(position).getDocId());
        holder.tvpath.setText(versionList.get(position).getDocpath());


        if (FileViewActivity.delversion.equalsIgnoreCase("1")) {

            holder.ivDelVer.setVisibility(View.VISIBLE);

        } else {

            holder.ivDelVer.setVisibility(View.GONE);
        }

        if (FileViewActivity.pdfView.equalsIgnoreCase("1")) {

            holder.ivView.setVisibility(View.VISIBLE);

        } else {

            holder.ivView.setVisibility(View.GONE);
        }

        if (FileViewActivity.viewMetadata.equalsIgnoreCase("1")) {
            holder.ivMetaView.setVisibility(View.VISIBLE);

        } else {

            holder.ivMetaView.setVisibility(View.GONE);
        }


        holder.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //listener.onDelVersionBtnClick(v,position,holder.tvDocid.getText().toString());
                String path = holder.tvpath.getText().toString();
                String filetype = path.substring(path.lastIndexOf(".") + 1);
                String filename = holder.tvDocname.getText().toString();
                String docid = versionList.get(position).getDocId();
                String docpath = versionList.get(position).getDocpath();

                onButtonClickListener.onFileViewButtonClick(path, filetype, filename, docid, docpath, position);


            }
        });

        holder.ivDelVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String docid = versionList.get(position).getDocId();
                onButtonClickListener.onDeleteVersionButtonClick(docid, position);


            }
        });


        holder.ivMetaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String slid = versionList.get(position).getDocSlid();
                String docid = versionList.get(position).getDocId();

                onButtonClickListener.onMetaViewButtonClick(slid, docid);

            }
        });


    }

    @Override
    public int getItemCount() {
        return versionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvVersion, tvDocname, tvDocid, tvpath;
        ImageView ivView, ivDelVer, ivMetaView;
        View decorator;

        public ViewHolder(View itemView) {
            super(itemView);

            tvVersion = itemView.findViewById(R.id.tv_version);
            tvDocid = itemView.findViewById(R.id.tv_version_docid);
            tvDocname = itemView.findViewById(R.id.tv_version_docname);
            tvpath = itemView.findViewById(R.id.tv_version_docpath);
            ivView = itemView.findViewById(R.id.iv_version_view);
            ivDelVer = itemView.findViewById(R.id.iv_version_del);
            ivMetaView = itemView.findViewById(R.id.iv_version_view_meta);
            decorator = itemView.findViewById(R.id.view_version_decorator);

        }
    }


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
//            String filefiletype = filename.substring(filename.lastIndexOf("."),filename.length());
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
//                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/"+filename+"."+filefiletype);
//                }
//                else{
//                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/"+filename+"."+filefiletype);
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
}