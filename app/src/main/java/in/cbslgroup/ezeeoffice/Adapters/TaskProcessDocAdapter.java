package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Player.VideoPlayerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.ImageViewerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.PdfViewerActivity;
import in.cbslgroup.ezeeoffice.Model.TaskProcessDocs;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;


public class TaskProcessDocAdapter extends RecyclerView.Adapter<TaskProcessDocAdapter.ViewHolder> {

    List<TaskProcessDocs> taskProcessDocsList;
    Context context;

    AlertDialog alertDialog;


    public TaskProcessDocAdapter(List<TaskProcessDocs> taskProcessDocsList, Context context) {
        this.taskProcessDocsList = taskProcessDocsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_process_doc_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.docname.setText(taskProcessDocsList.get(position).getDocname());
        holder.docPath.setText(taskProcessDocsList.get(position).getDocPath());
        holder.doctype.setText(taskProcessDocsList.get(position).getExtension());

        final String filetype = holder.doctype.getText().toString();
        final String filepath = taskProcessDocsList.get(position).getDocPath();
        final String filename = holder.docname.getText().toString();
        final String docid = taskProcessDocsList.get(position).getDocid();

        switch (filetype.toLowerCase()) {

            case "apk":

                holder.ivDocType.setImageResource(R.drawable.ic_android_green_24dp);
                break;


            case "pdf":

                holder.ivDocType.setImageResource(R.drawable.pdf);
                // holder.ivOpenFile.setImageResource(R.drawable.pdf);
                break;
            case "jpg":
                holder.ivDocType.setImageResource(R.drawable.jpg);
                // holder.ivOpenFile.setImageResource(R.drawable.jpg);

                break;
            case "png":
                holder.ivDocType.setImageResource(R.drawable.png);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "gif":
                holder.ivDocType.setImageResource(R.drawable.gif);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "jpeg":
                holder.ivDocType.setImageResource(R.drawable.jpeg);
                // holder.ivOpenFile.setImageResource(R.drawable.jpeg);

                break;

            case "tiff":
                holder.ivDocType.setImageResource(R.drawable.tiff);
                break;
            case "tif":
                holder.ivDocType.setImageResource(R.drawable.tif);
                break;

            case "doc":
                holder.ivDocType.setImageResource(R.drawable.doc);
                break;

            case "txt":
                holder.ivDocType.setImageResource(R.drawable.unsupported_file);
                break;

            case "docx":
                holder.ivDocType.setImageResource(R.drawable.docx);
                break;


            case "psd":
                holder.ivDocType.setImageResource(R.drawable.psd);
                break;

            case "mp4":

                holder.ivDocType.setImageResource(R.drawable.mp4);

                break;


            case "3gp":
                holder.ivDocType.setImageResource(R.drawable.threegp);

                break;

            case "mp3":
                holder.ivDocType.setImageResource(R.drawable.mp3);

                break;

            default:

                holder.ivDocType.setImageResource(R.drawable.unsupported_file);
                // holder.ivOpenFile.setImageResource(R.drawable.file);
                break;


        }


        holder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filepath == null || filepath.isEmpty()) {

                    Log.e("file path", "null");
                    Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                } else {

                    if (filetype.equals("psd")
                            || filetype.equals("pdf")
                            || filetype.equals("doc")
                            || filetype.equals("docx")
                            || filetype.equalsIgnoreCase("tif")
                            || filetype.equalsIgnoreCase("tiff")) {
                        //holder.ivDocType.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        intent.putExtra("docid", docid);


                        context.startActivity(intent);

                    } else if (filetype.equals("jpg")
                            || filetype.equals("jpeg")
                            || filetype.equals("png") ||
                            filetype.equals("gif")) {
                        // holder.ivDocType.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", "-");
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", "-");
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", docid);
                        context.startActivity(intent);

                    }
                    else if (filetype.equals("mp4") || filetype.equals("3gp")) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        context.startActivity(intent);

                    } else if (filetype.equalsIgnoreCase("mp3")) {

                        showAudioPlayerDialog(context, filepath, filename);

                    } else {
                        // holder.ivDocType.setImageResource(R.drawable.ic_round_file_light_blue);
                        Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();
                        //Log.e("File not supported","true");

                    }

                }


            }
        });


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

    @Override
    public int getItemCount() {
        return taskProcessDocsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView docname;
        ImageView ivDocType;
        TextView docPath;
        TextView doctype;
        CardView cardView;
        LinearLayout llmain;


        public ViewHolder(View itemView) {
            super(itemView);

            docname = itemView.findViewById(R.id.tv_task_process_doc_name);
            docname.setPaintFlags(docname.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            doctype = itemView.findViewById(R.id.tv_task_process_doc_type);
            docPath = itemView.findViewById(R.id.tv_task_process_doc_path);
            ivDocType = itemView.findViewById(R.id.iv_task_process_doc_type);
            cardView = itemView.findViewById(R.id.cv_task_in_process_item);
            llmain = itemView.findViewById(R.id.ll_task_in_process_item);

        }
    }
}
