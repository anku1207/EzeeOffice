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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Player.VideoPlayerActivity;
import in.cbslgroup.ezeeoffice.Activity.SharedFiles.SharedWithMeActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.ImageViewerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.PdfViewerActivity;
import in.cbslgroup.ezeeoffice.Model.SharedWithMe;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;


public class SharedWithMeAdapter extends RecyclerView.Adapter<SharedWithMeAdapter.ViewHolder> {

    List<SharedWithMe> sharedWithMeList;
    Context context;
    AlertDialog alertDialog;
    ProgressDialog mProgressDialog;

    DownloadManager downloadManager;
    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;

    SharedWithMeActivity sharedWithMeActivity;

    public SharedWithMeAdapter(List<SharedWithMe> sharedWithMeAdapterList, Context context) {
        this.sharedWithMeList = sharedWithMeAdapterList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedwithme_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvDocname.setText(sharedWithMeList.get(position).getDocname());
        holder.tvNoOfPages.setText(sharedWithMeList.get(position).getNoOfPages());
        holder.tvStorageName.setText(sharedWithMeList.get(position).getStorageName());
        holder.tvSharedBy.setText(sharedWithMeList.get(position).getSharedBy());
        holder.tvExtension.setText(sharedWithMeList.get(position).getExtension());
        holder.tvdocPath.setText(sharedWithMeList.get(position).getDocPath());
        holder.tvSharedDate.setText(sharedWithMeList.get(position).getSharedDate());

        final String filetype = String.valueOf(holder.tvExtension.getText());
        final String filename = String.valueOf(holder.tvDocname.getText());
        final String filepath = String.valueOf(holder.tvdocPath.getText());
        final String docid = sharedWithMeList.get(position).getDocid();


        switch (filetype.toLowerCase()) {
            case "pdf":

                holder.ivFileType.setImageResource(R.drawable.pdf_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.pdf);

                break;

            case "bmp":

                holder.ivFileType.setImageResource(R.drawable.bmp_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.pdf);

                break;
            case "jpg":
                holder.ivFileType.setImageResource(R.drawable.jpg_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.jpg);

                break;
            case "png":
                holder.ivFileType.setImageResource(R.drawable.png_128px);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "gif":
                holder.ivFileType.setImageResource(R.drawable.gif_128px);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "jpeg":
                holder.ivFileType.setImageResource(R.drawable.jpeg_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.jpeg);

                break;

            case "tiff":
                holder.ivFileType.setImageResource(R.drawable.tiff_128px);
                break;
            case "tif":
                holder.ivFileType.setImageResource(R.drawable.tif_128px);
                break;

            case "doc":
                holder.ivFileType.setImageResource(R.drawable.doc_128px);
                break;

            case "txt":
                holder.ivFileType.setImageResource(R.drawable.unsupported_file_128px);
                break;

            case "docx":
                holder.ivFileType.setImageResource(R.drawable.docx_128px);
                break;


            case "psd":
                holder.ivFileType.setImageResource(R.drawable.psd_128px);
                break;

            case "mp4":

                holder.ivFileType.setImageResource(R.drawable.mp4_128px);
                break;


            case "3gp":
                holder.ivFileType.setImageResource(R.drawable.threegp_128px);
                break;

            case "mp3":
                holder.ivFileType.setImageResource(R.drawable.mp3_128px);
                break;

            default:

                holder.ivFileType.setImageResource(R.drawable.unsupported_file_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.file);
                break;
        }


        holder.cvSharedWithMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filetype.equals("psd")
                        || filetype.equals("pdf")
                        || filetype.equals("doc")
                        || filetype.equals("docx")
                        || filetype.equalsIgnoreCase("tif")
                        || filetype.equalsIgnoreCase("tiff")) {


                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("filename", filename);
                    intent.putExtra("filepath", filepath);
                    intent.putExtra("docid", docid);
                    context.startActivity(intent);


                } else if (filetype.equals("jpg")
                        || filetype.equals("jpeg")
                        || filetype.equals("png")
                        || filetype.equals("bmp")
                        || filetype.equals("gif")) {
                    // holder.ivFileType.setImageResource(R.drawable.ic_round_image_lightblue);
                    Intent intent = new Intent(context, ImageViewerActivity.class);
                    intent.putExtra("date", "-");
                    intent.putExtra("type", filetype);
                    intent.putExtra("name", filename);
                    intent.putExtra("size", "-");
                    intent.putExtra("path", filepath);
                    intent.putExtra("docid", docid);
                    context.startActivity(intent);

                } else if (filetype.equals("mp4") || filetype.equals("3gp")) {
                    //holder.ivFileType.setImageResource(R.drawable.ic_round_pdf);
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("filename", filename);
                    intent.putExtra("filepath", filepath);
                    context.startActivity(intent);

                } else if (filetype.equalsIgnoreCase("mp3")) {

                    showAudioPlayerDialog(context, filepath, filename);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Download File");
                    alertDialog.setIcon(R.drawable.file);
                    alertDialog.setMessage("This is file is not supported instead of this you can download the file so do you want to download" + " " + filename + "." + filetype + "?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    Log.e("doc path list", DOCUMENT_URL + filepath);

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


                                    //new DownloadFile(context).execute(DOCUMENT_URL + filepath);

                                    DownloadFile(DOCUMENT_URL + filepath);


                                }
                            });
                    alertDialog.show();


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


    @Override
    public int getItemCount() {

        return sharedWithMeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNoOfPages, tvDocname, tvdocPath, tvSharedBy, tvSharedDate, tvStorageName, tvExtension;
        CardView cvSharedWithMe;
        ImageView ivFileType;


        public ViewHolder(View itemView) {
            super(itemView);


            tvNoOfPages = itemView.findViewById(R.id.tv_shared_file_with_me_NoOfPages);
            tvSharedBy = itemView.findViewById(R.id.tv_shared_file_with_me_sharedBy);
            tvSharedDate = itemView.findViewById(R.id.tv_shared_file_with_me_sharedDate);
            tvStorageName = itemView.findViewById(R.id.tv_shared_file_with_me_Storagename);
            tvDocname = itemView.findViewById(R.id.tv_shared_with_me_filename);
            tvdocPath = itemView.findViewById(R.id.tv_shared_with_me_filepath);
            tvExtension = itemView.findViewById(R.id.tv_shared_with_me_fileExtension);
            ivFileType = itemView.findViewById(R.id.iv_sharedwithme_filetype);

            cvSharedWithMe = itemView.findViewById(R.id.cv_sharedwithme);


        }
    }


}
