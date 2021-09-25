package in.cbslgroup.ezeeoffice.Adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
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


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import in.cbslgroup.ezeeoffice.Model.MultipleSelect;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;


public class MultipleFileChooseAdapter extends RecyclerView.Adapter<MultipleFileChooseAdapter.ViewHolder> {

    List<MultipleSelect> multipleSelectList;
    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;
    Context context;
    ProgressDialog mProgressDialog;

    DownloadManager downloadManager;

    AlertDialog alertDialog;

    public MultipleFileChooseAdapter(List<MultipleSelect> multipleSelectList, Context context) {
        this.multipleSelectList = multipleSelectList;
        this.context = context;
    }

    @NonNull
    @Override
    public MultipleFileChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_file_choose_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleFileChooseAdapter.ViewHolder holder, final int position) {


        holder.tvFilesize.setText(Util.formatFileSize(Long.parseLong(multipleSelectList.get(position).getFilesize())));

        final String filepath = multipleSelectList.get(position).getFilePath();
//        final String uri = multipleSelectList.get(position).getUri();
//
//
//        Log.e("file uri", uri);

        final String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
        final String filetype = filename.substring(filename.lastIndexOf(".") + 1);
        Log.e("filename", filename);


        holder.tvFilename.setText(filename);
        /*String filename = holder.tvFilename.getText().toString().substring(holder.tvFilename.getText().toString().lastIndexOf("/"), holder.tvFilename.getText().toString().length());
        String filetype = filename.substring(filename.lastIndexOf("."), filename.length());*/

        Log.e("filetype", filetype);

        switch (filetype.toLowerCase()) {
            case "pdf":

                holder.ivType.setImageResource(R.drawable.pdf);
                break;
            case "jpg":
                holder.ivType.setImageResource(R.drawable.jpg);
                break;
            case "png":
                holder.ivType.setImageResource(R.drawable.png);
                break;
            case "gif":
                holder.ivType.setImageResource(R.drawable.gif);
                break;
            case "jpeg":
                holder.ivType.setImageResource(R.drawable.jpeg);
                break;

            case "tiff":
                holder.ivType.setImageResource(R.drawable.tiff);
                break;
            case "tif":
                holder.ivType.setImageResource(R.drawable.tif);
                break;

            case "doc":
                holder.ivType.setImageResource(R.drawable.doc);
                break;

            case "txt":
                holder.ivType.setImageResource(R.drawable.unsupported_file);

                break;

            case "docx":
                holder.ivType.setImageResource(R.drawable.docx);
                break;


            case "psd":
                holder.ivType.setImageResource(R.drawable.psd);
                break;

            case "mp4":

                holder.ivType.setImageResource(R.drawable.mp4);
                break;


            case "3gp":
                holder.ivType.setImageResource(R.drawable.threegp);
                break;

            case "mp3":
                holder.ivType.setImageResource(R.drawable.mp3);
                break;

            default:

                holder.ivType.setImageResource(R.drawable.unsupported_file);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return multipleSelectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilename;
        TextView tvFilesize;
        ImageView ivType;

        public ViewHolder(View itemView) {
            super(itemView);


            tvFilename = itemView.findViewById(R.id.tv_multichoose_filename);
            tvFilesize = itemView.findViewById(R.id.tv_multichoose_filesize);
            ivType = itemView.findViewById(R.id.iv_multichoose_filetype);

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
}
