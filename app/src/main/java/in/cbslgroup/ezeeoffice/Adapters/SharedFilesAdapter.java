package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;





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

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.Player.VideoPlayerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.ImageViewerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.PdfViewerActivity;
import in.cbslgroup.ezeeoffice.Interface.SharedFilesListListener;
import in.cbslgroup.ezeeoffice.Model.SharedFiles;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;


public class SharedFilesAdapter extends RecyclerView.Adapter<SharedFilesAdapter.ViewHolder> {

    List<SharedFiles> sharedFilesList;
    Context context;
    int Position;
    AlertDialog alertDialog;
    String filetype;
    SharedFilesListListener listener;

    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;
    ProgressDialog mProgressDialog;
    DownloadManager downloadManager;


    public SharedFilesAdapter(List<SharedFiles> sharedFilesList, Context context, SharedFilesListListener listener) {
        this.sharedFilesList = sharedFilesList;
        this.context = context;
        this.listener = listener;
    }



    @Override
    public SharedFilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_files_list, parent, false);
         return new ViewHolder(v);



    }



    @Override
    public void onBindViewHolder(@NonNull final SharedFilesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.tvfilename.setText(sharedFilesList.get(position).getFilename());
        holder.tvnoOfPages.setText(sharedFilesList.get(position).getNoOfPages());
        holder.tvshareDate.setText(sharedFilesList.get(position).getSharedDate());
        holder.tvstorageName.setText(sharedFilesList.get(position).getStorageName());
        holder.tvsharedTo.setText(sharedFilesList.get(position).getSharedTo());
        // holder.tvMetadata.setText(searchList.get(position).getMetadata());
        holder.tvPath.setText(sharedFilesList.get(position).getFilepath());
        holder.tvDocid.setText(sharedFilesList.get(position).getdocid());
        holder.tvExtension.setText(sharedFilesList.get(position).getFileExtension());
        holder.tvtoUserid.setText(sharedFilesList.get(position).getTouserid());


        final String filetype = String.valueOf(holder.tvExtension.getText());
        final String filename = sharedFilesList.get(position).getFilename();
        final String filepath = String.valueOf(holder.tvPath.getText());
        final String filedate = " ";
        final String filesize =" ";
        final String docid = sharedFilesList.get(position).getdocid();

        holder.ivFiletypeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (filepath == null || filepath.isEmpty()) {

                    Log.e("file path", "null");
                    Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                } else {

                    if (  filetype.equals("psd")
                            || filetype.equals("pdf")
                            || filetype.equals("doc")
                            || filetype.equals("docx")
                            || filetype.equalsIgnoreCase("tif")
                            || filetype.equalsIgnoreCase("tiff")) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
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
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", filedate);
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", filesize);
                        intent.putExtra("path", filepath);
                        intent.putExtra("docid", docid);
                        context.startActivity(intent);

                    } else if (filetype.equals("mp4") || filetype.equals("3gp")) {
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        context.startActivity(intent);

                    } else if (filetype.equalsIgnoreCase("mp3")) {

                        showAudioPlayerDialog(context, filepath, filename);

                    } else {
                        // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_file_light_blue);
                        //Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

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


                        //Log.e("File not supported","true");

                    }



            }



            }
        });


        switch (filetype.toLowerCase()) {
            case "pdf":

                holder.ivFiletypeIcon.setImageResource(R.drawable.pdf_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.pdf);

                break;
            case "jpg":
                holder.ivFiletypeIcon.setImageResource(R.drawable.jpg_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.jpg);

                break;
            case "png":
                holder.ivFiletypeIcon.setImageResource(R.drawable.png_128px);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "gif":
                holder.ivFiletypeIcon.setImageResource(R.drawable.gif_128px);
                //holder.ivOpenFile.setImageResource(R.drawable.png);

                break;
            case "jpeg":
                holder.ivFiletypeIcon.setImageResource(R.drawable.jpeg_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.jpeg);
                break;

            case "tiff":
                holder.ivFiletypeIcon.setImageResource(R.drawable.tiff_128px);
                break;
            case "tif":
                holder.ivFiletypeIcon.setImageResource(R.drawable.tif_128px);
                break;

            case "doc":
                holder.ivFiletypeIcon.setImageResource(R.drawable.doc_128px);
                break;

            case "txt":
                holder.ivFiletypeIcon.setImageResource(R.drawable.unsupported_file_128px);
                break;

            case "bmp":
                holder.ivFiletypeIcon.setImageResource(R.drawable.bmp_128px);
                break;

            case "docx":
                holder.ivFiletypeIcon.setImageResource(R.drawable.docx_128px);
                break;


            case "psd":
                holder.ivFiletypeIcon.setImageResource(R.drawable.psd_128px);
                break;

            case "mp4":

                holder.ivFiletypeIcon.setImageResource(R.drawable.mp4_128px);

                break;


            case "3gp":
                holder.ivFiletypeIcon.setImageResource(R.drawable.threegp_128px);

                break;

            case "mp3":
                holder.ivFiletypeIcon.setImageResource(R.drawable.mp3_128px);
                break;

            default:

                holder.ivFiletypeIcon.setImageResource(R.drawable.unsupported_file_128px);
                // holder.ivOpenFile.setImageResource(R.drawable.file);
                break;
        }





        holder.btnUndoShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String undouserid = MainActivity.userid;
                String undousername = MainActivity.username;
                String undoIp = MainActivity.ip;
                String undodocid = holder.tvDocid.getText().toString();
                String undoTouserid = MainActivity.userid;
                String toids = holder.tvtoUserid.getText().toString();

                listener.onUndoButtonClick(v,undodocid,position,toids);





            }
        });


    }

    @Override
    public int getItemCount() {
        return sharedFilesList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvToDocid,tvfilename, tvnoOfPages, tvstorageName, tvshareDate, tvsharedTo, tvPath, tvExtension, tvDocid, tvtoUserid;
        ImageView ivFiletypeIcon;
        Button btnUndoShare;

        public ViewHolder(View itemView) {
            super(itemView);

            tvfilename = itemView.findViewById(R.id.tv_shared_file_list_filename);
            tvnoOfPages = itemView.findViewById(R.id.tv_shared_files_list_NoOfPages);
            tvExtension = itemView.findViewById(R.id.tv_shared_file_list_fileExtension);
            tvsharedTo = itemView.findViewById(R.id.tv_shared_files_list_sharedto);
            tvshareDate = itemView.findViewById(R.id.tv_shared_file_list_sharedDate);
            tvDocid = itemView.findViewById(R.id.tv_shared_file_list_docid);
            tvtoUserid = itemView.findViewById(R.id.tv_shared_file_list_touserid);

            ivFiletypeIcon = itemView.findViewById(R.id.iv_sharedfiles_filetype);
            tvstorageName = itemView.findViewById(R.id.tv_shared_files_list_Storagename);
            tvPath = itemView.findViewById(R.id.tv_shared_file_list_filepath);
            btnUndoShare = itemView.findViewById(R.id.btn_shared_files_list_undoshare);

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

   /* void UndoSharedFiles(final String userid, final String username, final String ip, final String docid, final int position) {

        SharedFilesActivity.llNofilefound.setVisibility(View.GONE);
        SharedFilesActivity.rvSharedFiles.setVisibility(View.GONE);
        SharedFilesActivity.progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("undo", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0 || response.equals("") || response.isEmpty()) {

                        SharedFilesActivity.llNofilefound.setVisibility(View.VISIBLE);


                    }


                    else {

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        Log.e("error and message", error + " " + message);

                        switch (error) {
                            case "null":

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                break;
                            case "true":

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                break;

                            case "false":

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                sharedFilesList.remove(position);
                                notifyDataSetChanged();



                                break;
                        }

                    }



                    SharedFilesActivity.llNofilefound.setVisibility(View.GONE);
                    SharedFilesActivity.rvSharedFiles.setVisibility(View.VISIBLE);
                    SharedFilesActivity.progressBar.setVisibility(View.GONE);


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
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("UndoUserid", userid);
                parameters.put("UndoIp", ip);
                parameters.put("UndoUsername", username);
                parameters.put("UndoDocid", docid);

                return parameters;

            }


        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }*/





}
