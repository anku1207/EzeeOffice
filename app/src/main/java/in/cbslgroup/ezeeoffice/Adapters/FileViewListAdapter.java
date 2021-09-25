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
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.Player.VideoPlayerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.FileViewActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.ImageViewerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.PdfViewerActivity;
import in.cbslgroup.ezeeoffice.Chip.TokenFilterAdapter;
import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.Chip.UsernameCompletionView;
import in.cbslgroup.ezeeoffice.Interface.onFileClick;
import in.cbslgroup.ezeeoffice.Model.FileViewList;
import in.cbslgroup.ezeeoffice.Model.SharedFileStatus;
import in.cbslgroup.ezeeoffice.Model.Version;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class FileViewListAdapter extends RecyclerView.Adapter<FileViewListAdapter.ViewHolder> {


    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;
    static public int checkeditemcounter = 0;
    public static ArrayList<String> docidlist = new ArrayList<>();
    public static ArrayList<String> checkedlist = new ArrayList<>();
    private final SparseBooleanArray checkedarray = new SparseBooleanArray();
    List<FileViewList> fileViewLists;
    Context context;
    onFileClick listener;

    AlertDialog alertDialog, alertDialogVersion;

    FileViewActivity fileViewActivity;
    ArrayList<String> docidList = new ArrayList<>();
    ArrayList<String> shareuseridsList = new ArrayList<>();
    ArrayList<String> useridsList = new ArrayList<>();
    ArrayList<SharedFileStatus> sharedFileStatusList = new ArrayList<>();

    RecyclerView rvSharedFileStatus;
    //version module
    RecyclerView rvVersion;
    VersionAdapter versionAdapter;
    ProgressBar progressBarVersion;
    TextView tvNoVersionFound;


    AppCompatActivity activity;

    ProgressDialog mProgressDialog;
    DownloadManager downloadManager;
    private ArrayList<Version> versionList = new ArrayList<>();
    private ArrayList<User> usernamelist;
    private ArrayList<User> usernamelistwithSlid;
    private TokenFilterAdapter filterAdapter;
    private UsernameCompletionView autoCompleteTextView;
    private ArrayList<String> useridlist = new ArrayList<>();
    private ArrayList<String> shareFilesUseridList = new ArrayList<>();

    int itemPos = 0;


    public FileViewListAdapter(List<FileViewList> fileViewLists, Context context, onFileClick listener) {
        this.fileViewLists = fileViewLists;
        this.context = context;
        this.listener = listener;
        fileViewActivity = (FileViewActivity) context;


    }

    private static void disableOptions(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableOptions((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    private static void enableOptions(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableOptions((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }


    @Override
    public FileViewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dms_fileview_list_2, parent, false);

        return new FileViewListAdapter.ViewHolder(v, fileViewActivity);
    }

/*    public PopupWindow popupDisplay() {

        final PopupWindow popupWindow = new PopupWindow(context);
        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.option_menu_custom, null);
        TextView tv = view.findViewById(R.id.tv_view_file_popup);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath == null || filepath.isEmpty()) {

                    Log.e("file path", "null");
                    Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();
                } else {

                    if (filetype.equals("pdf")) {
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        popupwindow_obj.dismiss();
                        context.startActivity(intent);

                    }

                    if (filetype.equals("jpg") || filetype.equals("jpeg") || filetype.equals("png") || filetype.equals("tiff")) {
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra("date", filedate);
                        intent.putExtra("type", filetype);
                        intent.putExtra("name", filename);
                        intent.putExtra("size", filesize);
                        intent.putExtra("path", filepath);

                        popupwindow_obj.dismiss();
                        context.startActivity(intent);

                    } else {

                        Log.e("File not supported", "true");

                    }
                }


            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setContentView(view);

        return popupWindow;
    }*/

    @Override
    public void onBindViewHolder(final FileViewListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        itemPos = position;

        final FileViewList fileViewList = fileViewLists.get(position);


        holder.tvFileName.setText(fileViewList.getFileName());
        holder.tvFileSize.setText(fileViewList.getFileSize());
        holder.tvUplodedDate.setText(fileViewList.getUploadDate());
        holder.tvUploadedBy.setText(fileViewList.getUploadedBy());
        holder.tvNoOfPages.setText(fileViewList.getNoOfPages());
        holder.tvFilePath.setText(fileViewList.getFilePath());
        holder.tvFileType.setText(fileViewList.getFiletype());
        holder.tvDocid.setText(fileViewList.getDocId());
        holder.tvcheckinstatus.setText(fileViewList.getCheckincheckout());
        holder.tvVersionStatus.setText(fileViewList.getVersionShowHide());
        //holder.tvMetaData.setText(Html.fromHtml(fileViewList.getMetadata()), TextView.BufferType.SPANNABLE);


        String versionstatus = holder.tvVersionStatus.getText().toString();
        String checkinstatus = holder.tvcheckinstatus.getText().toString();
        final String docid = holder.tvDocid.getText().toString();

        Log.e("versionstatus", versionstatus);
        Log.e("checkinstatus", checkinstatus);
        Log.e("metadata", holder.tvMetaData.getText().toString());

        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

//
//                MenuBuilder menuBuilder;
//                menuBuilder = new MenuBuilder(context);
//                MenuInflater inflater = new MenuInflater(context);
//                inflater.inflate(R.menu.menu_fileview, menuBuilder);
//                MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, v);
//                optionsMenu.setForceShowIcon(true);
//
//                // Display the menu
//                optionsMenu.show();

            }
        });

        if (FileViewActivity.checkinCheckout.equalsIgnoreCase("0")) {

            holder.ivCheckin.setVisibility(View.GONE);
            holder.ivCheckOut.setVisibility(View.GONE);


            if (FileViewActivity.pdfView.equalsIgnoreCase("1") || FileViewActivity.imageView.equalsIgnoreCase("1")) {

                holder.ivOpenFile.setVisibility(View.VISIBLE);

            } else {


                holder.ivOpenFile.setVisibility(View.GONE);
            }

            if (FileViewActivity.fileDelete.equalsIgnoreCase("1")) {

                holder.ivDelete.setVisibility(View.VISIBLE);

            } else {


                holder.ivDelete.setVisibility(View.GONE);
            }

            if (FileViewActivity.viewMetadata.equalsIgnoreCase("1")) {

                holder.ivMetadata.setVisibility(View.VISIBLE);

            } else {


                holder.ivMetadata.setVisibility(View.GONE);
            }


            if (FileViewActivity.fileVersion.equalsIgnoreCase("1")) {

                if (versionstatus.equals("1")) {

                    holder.ivVersion.setVisibility(View.VISIBLE);


                } else {


                    holder.ivVersion.setVisibility(View.INVISIBLE);


                }
            }

            if (FileViewActivity.shareFile.equalsIgnoreCase("1")) {


                holder.ivShare.setVisibility(View.VISIBLE);


            } else {


                holder.ivShare.setVisibility(View.GONE);


            }


        } else {


            if (checkinstatus.equals("0")) {


                holder.ivCheckin.setVisibility(View.VISIBLE);
                holder.ivShare.setVisibility(View.GONE);
                holder.ivMetadata.setVisibility(View.GONE);
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivOpenFile.setVisibility(View.GONE);
                holder.ivCheckOut.setVisibility(View.GONE);


            } else {


                holder.ivCheckin.setVisibility(View.GONE);
                holder.ivCheckOut.setVisibility(View.VISIBLE);


                if (FileViewActivity.imageView.equalsIgnoreCase("1") || FileViewActivity.pdfView.equalsIgnoreCase("1")) {

                    holder.ivOpenFile.setVisibility(View.VISIBLE);

                } else {


                    holder.ivOpenFile.setVisibility(View.GONE);
                }

                if (FileViewActivity.fileDelete.equalsIgnoreCase("1")) {

                    holder.ivDelete.setVisibility(View.VISIBLE);

                } else {


                    holder.ivDelete.setVisibility(View.GONE);
                }

                if (FileViewActivity.viewMetadata.equalsIgnoreCase("1")) {

                    holder.ivMetadata.setVisibility(View.VISIBLE);

                } else {


                    holder.ivMetadata.setVisibility(View.GONE);
                }


                if (FileViewActivity.fileVersion.equalsIgnoreCase("1")) {

                    if (versionstatus.equals("1")) {

                        holder.ivVersion.setVisibility(View.VISIBLE);


                    } else {


                        holder.ivVersion.setVisibility(View.INVISIBLE);


                    }
                }

                if (FileViewActivity.shareFile.equalsIgnoreCase("1")) {


                    holder.ivShare.setVisibility(View.VISIBLE);


                } else {


                    holder.ivShare.setVisibility(View.GONE);


                }
/*
                holder.ivShare.setVisibility(View.VISIBLE);
                holder.ivMetadata.setVisibility(View.VISIBLE);
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivOpenFile.setVisibility(View.VISIBLE);
                holder.ivCheckOut.setVisibility(View.VISIBLE);*/


            }


        }


        //holder.cardView.setCardBackgroundColor(fileViewList.isSelected() ? context.getResources().getColor(R.color.grey) : context.getResources().getColor(R.color.transparent));

      /*  //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);*/


        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        // holder.checkBox.setChecked(fileViewList.isSelected());


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fileViewActivity.prepareSelection(v, position, docidlist);
                fileViewList.setSelected(!fileViewList.isSelected());
                holder.cardView.setCardBackgroundColor(fileViewList.isSelected() ? context.getResources().getColor(R.color.grey) : context.getResources().getColor(R.color.transparent));


            }
        });


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    checkedlist.add("" + position);
                    checkeditemcounter++;
                    docidlist.add(holder.tvDocid.getText().toString());
                    checkedarray.append(position, true);


                    //  FileViewActivity.toolbar.setTitle(checkeditemcounter);
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

                } else {

                    docidlist.remove(holder.tvDocid.getText().toString());
                    checkeditemcounter = checkeditemcounter - 1;
                    checkedlist.remove("" + position);
                    checkedarray.delete(position);
                    //FileViewActivity.toolbar.setTitle(checkeditemcounter);
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

                }

            }
        });






          /*  for(int i = 0 ; i<checkedlist.size(); i++){

                String id = checkedlist.get(i);

                Log.e("checkiid",id);

                if(id.equalsIgnoreCase(String.valueOf(i))){

                    Log.e("inloop"+i,"true");

                    if(!holder.checkBox.isChecked()){

                        holder.checkBox.setChecked(true);
                        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

                    }

                }

        }
*/


        if (!FileViewActivity.in_action_mode) {

            holder.checkBox.setVisibility(View.GONE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            //holder.checkBox.setChecked(false);
            enableOptions(holder.llOptions);


        } else {

            holder.checkBox.setVisibility(View.VISIBLE);
            // holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.checkBox.setChecked(false);

            disableOptions(holder.llOptions);


        }


        if (fileViewList.isSelected()) {

            Log.e("checkbox", "true  " + position);
            holder.checkBox.setChecked(true);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));


        } else {
            Log.e("checkbox", "false " + position);
            holder.checkBox.setChecked(false);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));


        }


/*

        if(fileViewList.getIsChecked().equals("1"))
        {
            holder.checkBox.setChecked(true);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

        }
        else {

            holder.checkBox.setChecked(false);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
*/


        final String filepath = fileViewLists.get(position).getFilePath();
        final String filename = fileViewLists.get(position).getFileName();
        final String filetype = String.valueOf(holder.tvFileType.getText());
        Log.e("filetype", filetype);
        final String filesize = fileViewLists.get(position).getFileSize();
        final String filedate = fileViewLists.get(position).getUploadDate();
        final String meta = String.valueOf(holder.tvMetaData.getText());


        //holder.ivFiletypeIcon.setImageResource();


        switch (filetype.toLowerCase()) {
            case "pdf":

                holder.ivFiletypeIcon.setImageResource(R.drawable.pdf_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;
            case "jpg":
                holder.ivFiletypeIcon.setImageResource(R.drawable.jpg_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;
            case "png":
                holder.ivFiletypeIcon.setImageResource(R.drawable.png_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);

                break;
            case "gif":
                holder.ivFiletypeIcon.setImageResource(R.drawable.gif_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);

                break;
            case "jpeg":
                holder.ivFiletypeIcon.setImageResource(R.drawable.jpeg_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);

                break;

            case "tiff":
                holder.ivFiletypeIcon.setImageResource(R.drawable.tiff_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;
            case "tif":
                holder.ivFiletypeIcon.setImageResource(R.drawable.tif_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "doc":
                holder.ivFiletypeIcon.setImageResource(R.drawable.doc_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "txt":
                holder.ivFiletypeIcon.setImageResource(R.drawable.unsupported_file_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "docx":
                holder.ivFiletypeIcon.setImageResource(R.drawable.docx_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "bmp":
                holder.ivFiletypeIcon.setImageResource(R.drawable.bmp_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;


            case "psd":
                holder.ivFiletypeIcon.setImageResource(R.drawable.psd_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "mp4":

                holder.ivFiletypeIcon.setImageResource(R.drawable.mp4_128px);
                holder.ivOpenFile.setImageResource(R.drawable.ic_play_circle_filled_blue_24dp);
                break;


            case "3gp":
                holder.ivFiletypeIcon.setImageResource(R.drawable.threegp_128px);
                holder.ivOpenFile.setImageResource(R.drawable.ic_play_circle_filled_blue_24dp);
                break;

            case "mp3":
                holder.ivFiletypeIcon.setImageResource(R.drawable.mp3_128px);
                holder.ivOpenFile.setImageResource(R.drawable.ic_play_circle_filled_blue_24dp);
                break;



            default:

                holder.ivFiletypeIcon.setImageResource(R.drawable.unsupported_file_128px);
                holder.ivOpenFile.setImageResource(R.drawable.unsupported_file_48px);
                break;
        }


        holder.ivOpenFile.setOnClickListener(new View.OnClickListener() {
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
                        //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("filename", filename);
                        intent.putExtra("filepath", filepath);
                        intent.putExtra("docid", docid);
                        context.startActivity(intent);

                    } else if (filetype.equalsIgnoreCase("jpg")
                            || filetype.equalsIgnoreCase("jpeg")
                            || filetype.equalsIgnoreCase("png")
                            || filetype.equalsIgnoreCase("bmp")
                            || filetype.equalsIgnoreCase("gif")) {
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

        holder.ivMetadata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.show_meta_dialog, null);


                TextView textView = dialogView.findViewById(R.id.tv_dms_file_metadata_popup);
                ProgressBar pg = dialogView.findViewById(R.id.pg_dms_file_metadata_popup);


                getMetadata(fileViewList.getDocName(), fileViewList.getDocId(), textView, pg);


          /*      if (meta.isEmpty() || meta.equals("")) {
                    textView.setText("No Metadata Found");
                } else {

                    textView.setText(meta);
                }*/


                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_cancel_meta_popup);
                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

            }


        });

        holder.ivCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCheckInBtnClick(v, position, docid, filename);


            }
        });

        holder.ivCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCheckOutBtnClick(v, position, docid, filename);


            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener.onDeleteBtnClick(v, position, String.valueOf(holder.tvDocid.getText()), String.valueOf(holder.tvUploadedBy.getText()), String.valueOf(holder.tvFileName.getText()));
/*
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);

                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_delete_popup);
                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fullname = String.valueOf(holder.tvUploadedBy.getText());
                        String roleid = DmsActivity.roleid;
                        String ip = DmsActivity.ip;
                        String userid = DmsActivity.userid;
                        String filename = String.valueOf(holder.tvFileName.getText());
                        String docid = String.valueOf(holder.tvDocid.getText());

                        //delete doc volley request

                        deleteDoc(docid, fullname, "No", roleid, ip, userid, filename,position);

                        alertDialog.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();*/
            }
        });

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_share_files, null, false);



             /*   Spinner spinner = dialogView.findViewById(R.id.spinner_share_dialog);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context,android.R.layout.simple_spinner_dropdown_item, usernames);
                spinner.setAdapter(adapter);*/
                //                autoCompleteTextView.allowCollapse(true);
//                autoCompleteTextView.clearListSelection();

                autoCompleteTextView = dialogView.findViewById(R.id.autocomplete_textview);
                autoCompleteTextView.setThreshold(1);

                //Getting the whole list of group member which will be shown in share file dialog list

                String userid = DmsActivity.userid;
                groupMemberList(userid);


                //Set the action to be taken when a Token is clicked
                autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
                usernamelistwithSlid = new ArrayList<>();
                usernamelist = new ArrayList<>();


                Button btn_share = dialogView.findViewById(R.id.btn_share_popup);
                btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        docidlist.clear();
                        useridlist.clear();
                        shareFilesUseridList.clear();
                        // autoCompleteTextView.clearComposingText();

                        List<User> tokens = autoCompleteTextView.getObjects();
                        StringBuilder content = new StringBuilder();
                        for (int i = 0; i < tokens.size(); i++) {
                            String slid = tokens.get(i).getSlid();
                            content.append(slid.substring(slid.indexOf("&&") + 2)).append(" ");
                            shareFilesUseridList.add(slid.substring(slid.indexOf("&&") + 2));
                        }

                        String docid = String.valueOf(holder.tvDocid.getText());
                        String userid = DmsActivity.userid;

                        docidlist.add(docid);
                        useridlist.add(userid);

                        JSONArray jsonArray = new JSONArray();
                        JSONArray jsonArray1 = new JSONArray();

                        useridsList.add(userid);
                        docidList.add(docid);


                        String ip = MainActivity.ip;
                        String username = MainActivity.username;

                        for (int i = 0; i < shareFilesUseridList.size(); i++) {

                            jsonArray1.put(shareFilesUseridList.get(i));

                        }

                        String jsonarraystringUserids = jsonArray1.toString();

                        for (int i = 0; i < docidList.size(); i++) {

                            jsonArray.put(docidList.get(i));

                        }

                        String jsonarraystringDocids = jsonArray.toString();

                        Log.e("jsonarraystringDocids", jsonarraystringDocids);
                        Log.e("jsonarraystringuserids", jsonarraystringUserids);


                        if (shareFilesUseridList.size() != 0) {

                            autoCompleteTextView.deleteText();

                            //sharing single file
                            ShareSingleFiles(userid, jsonarraystringUserids, jsonarraystringDocids, ip, username);


                            //Toast.makeText(context, String.format("docid : "+docid+"\n"+"userid : "+userid+"\n"+"Userid : %s", content.toString())+"\n", Toast.LENGTH_LONG).show();

                            // Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
                            // Log.e("shareJson",jsonObject.toString());

                            alertDialog.cancel();


                        } else {

                            autoCompleteTextView.deleteText();
                            Toast.makeText(context, "Please select atleast one user to share file", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                Button btn_close = dialogView.findViewById(R.id.btn_close_share_popup);
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //code here for close dialog button

                        alertDialog.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                //alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                alertDialog.show();
            }
        });

        holder.ivVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_version, null);

                progressBarVersion = dialogView.findViewById(R.id.progressbar_version);
                tvNoVersionFound = dialogView.findViewById(R.id.tv_no_version_found);
                progressBarVersion.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progressbar_rotate));
                rvVersion = dialogView.findViewById(R.id.rv_dms_file_view_version_list);
                rvVersion.setLayoutManager(new LinearLayoutManager(context));
                // rvVersion.setHasFixedSize(true);
                //rvVersion.setItemViewCacheSize(fileViewLists.size());


                Log.e("userid: ", MainActivity.userid);
                Log.e("slid", DmsActivity.dynamicFileSlid);
                Log.e("docid", holder.tvDocid.getText().toString());


                versionList(MainActivity.userid, holder.tvDocid.getText().toString(), DmsActivity.dynamicFileSlid);


                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_close_version_popup);
                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialogVersion.dismiss();

                    }
                });

                dialogBuilder.setView(dialogView);

                alertDialogVersion = dialogBuilder.create();
                alertDialogVersion.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialogVersion.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                alertDialogVersion.show();


            }
        });



/*        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //popupwindow_obj = popupDisplay();
                final PopupWindow popupWindow = new PopupWindow(context);
                // inflate your layout or dynamically add view
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                View view = inflater.inflate(R.layout.option_menu_custom, null);
                TextView tv = view.findViewById(R.id.tv_view_file_popup);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filepath == null || filepath.isEmpty()) {

                            Log.e("file path", "null");
                            Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                        }

                        else{

                            if(filetype.equals("pdf")){
                                Intent intent = new Intent(context, PdfViewerActivity.class);
                                intent.putExtra("filename",filename);
                                intent.putExtra("filepath", filepath);

                                popupWindow.dismiss();
                                context.startActivity(intent);

                            }

                            else if(filetype.equals("jpg")||filetype.equals("jpeg")||filetype.equals("png")){
                                Intent intent = new Intent(context, ImageViewerActivity.class);
                                intent.putExtra("date",filedate);
                                intent.putExtra("type", filetype);
                                intent.putExtra("name",filename);
                                intent.putExtra("size",filesize);
                                intent.putExtra("path",filepath);

                                popupWindow.dismiss();
                                context.startActivity(intent);

                            }

                            else
                            {
                                Toast.makeText(context,"File not supported",Toast.LENGTH_LONG).show();

                                //Log.e("File not supported","true");

                            }
                        }


                    }
                });

                popupWindow.setFocusable(true);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(null);
                popupWindow.setContentView(view);
                popupWindow.showAsDropDown(v);


            }
        });*/

    }

   /* private void versionPresent(final String userid, final String docid, final String slid, final ViewHolder holder) {

        holder.progressBarVersion.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("onVersionResponse: ", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                   if(jsonArray.length()==0){

                       holder.ivVersion.setVisibility(View.GONE);
                       holder.progressBarVersion.setVisibility(View.GONE);

                   }
                   else{


                       holder.ivVersion.setVisibility(View.VISIBLE);
                       holder.progressBarVersion.setVisibility(View.GONE);
                   }






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

                Map<String, String> params = new HashMap<>();


                params.put("userid", userid);
                params.put("docid", docid);
                params.put("slid", slid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }*/

    @Override
    public int getItemCount() {

        return fileViewLists.size();
    }

    private void versionList(final String userid, final String docid, final String slid) {

        versionList.clear();
        progressBarVersion.setVisibility(View.VISIBLE);
        rvVersion.setVisibility(View.GONE);
        tvNoVersionFound.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("onVersionResponse: ", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() != 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {


                            String versionname = jsonArray.getJSONObject(i).getString("docVersion");
                            String docpath = jsonArray.getJSONObject(i).getString("docpath");
                            String docname = jsonArray.getJSONObject(i).getString("docname");
                            String docid = jsonArray.getJSONObject(i).getString("docid");
                            String fileSlid = jsonArray.getJSONObject(i).getString("file_slid");

                            versionList.add(new Version(docname, docid, docpath, versionname, fileSlid));


                        }

                        progressBarVersion.setVisibility(View.GONE);
                        rvVersion.setVisibility(View.VISIBLE);
                        tvNoVersionFound.setVisibility(View.GONE);


                        versionAdapter = new VersionAdapter(versionList, context);
                        versionAdapter.setOnButtonClickListener(new VersionAdapter.OnButtonClickListener() {
                            @Override
                            public void onFileViewButtonClick(String path, String filetype, String filename, String docid, String docpath, int
                                    pos) {


                                Log.e("filetype", filetype);

                                if (filetype.equals("psd")
                                        || filetype.equals("pdf")
                                        || filetype.equals("doc")
                                        || filetype.equals("docx")
                                        || filetype.equalsIgnoreCase("tif")
                                        || filetype.equalsIgnoreCase("tiff")) {


                                    Intent intent = new Intent(context, PdfViewerActivity.class);
                                    intent.putExtra("filename", filename);
                                    intent.putExtra("filepath", path);
                                    intent.putExtra("docid", docid);

                                    context.startActivity(intent);


                                } else if ((filetype.equals("jpg")
                                        || filetype.equals("jpeg")
                                        || filetype.equals("png")
                                        || filetype.equals("bmp")
                                        || filetype.equals("gif"))) {


                                    Intent intent = new Intent(context, ImageViewerActivity.class);
                                    intent.putExtra("date", "");
                                    intent.putExtra("type", filetype);
                                    intent.putExtra("name", filename);
                                    intent.putExtra("size", "");
                                    intent.putExtra("path", docpath);
                                    intent.putExtra("docid", docid);

                                    context.startActivity(intent);

                                } else if (filetype.equals("mp4") || filetype.equals("3gp")) {
                                    //holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_pdf);
                                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                                    intent.putExtra("filename", filename);
                                    intent.putExtra("filepath", docpath);
                                    context.startActivity(intent);

                                } else if (filetype.equalsIgnoreCase("mp3")) {

                                    showAudioPlayerDialog(context, docpath, filename);

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


                                                    Log.e("doc path list", DOCUMENT_URL + versionList.get(pos).getDocpath());

                                                    mProgressDialog = new ProgressDialog(context);
                                                    mProgressDialog.setMessage("File is downloading");
                                                    mProgressDialog.setIndeterminate(true);
                                                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                    mProgressDialog.setCancelable(false);


                                      /*  Intent intent = new Intent(context, DownloadService.class);
                                        intent.putExtra("url", DOCUMENT_URL+filepath);
                                        intent.putExtra("filename",filename);
                                        intent.putExtra("filetype",filefiletype);
                                        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                        context.startService(intent);*/


                                                    DownloadFile(DOCUMENT_URL + versionList.get(pos).getDocpath());


                                                }
                                            });
                                    alertDialog.show();

                                }


                            }

                            @Override
                            public void onDeleteVersionButtonClick(String docid, int pos) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                tvHeading.setText("Delete Version");
                                //tvHeading.setBackgroundColor(getResources().getColor(R.color.green_dark));


                                TextView tvSubheading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                tvSubheading.setText("Are you sure you want to del the version of this document ?");

                                Button btn_cancel = dialogView.findViewById(R.id.btn_no_pDel_popup);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        alertDialog.dismiss();
                                    }
                                });

                                Button btn_ok = dialogView.findViewById(R.id.btn_yes_pDel_popup);
                                btn_ok.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        // checkIn(docid);
                                        delVersion(docid, pos);

                                        alertDialog.dismiss();
                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                alertDialog.show();


                            }

                            @Override
                            public void onMetaViewButtonClick(String docname, String docid) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.show_meta_dialog, null);


                                TextView textView = dialogView.findViewById(R.id.tv_dms_file_metadata_popup);
                                ProgressBar pg = dialogView.findViewById(R.id.pg_dms_file_metadata_popup);


                                getMetadata(docname, docid, textView, pg);


                                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_cancel_meta_popup);
                                btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();

                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                alertDialog.show();


                            }
                        });

                        rvVersion.setAdapter(versionAdapter);

                    } else {


                        progressBarVersion.setVisibility(View.GONE);
                        rvVersion.setVisibility(View.GONE);
                        tvNoVersionFound.setVisibility(View.VISIBLE);


                    }


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

                Map<String, String> params = new HashMap<>();


                params.put("userid", userid);
                params.put("docid", docid);
                params.put("slid", slid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }


    private void deleteDoc(final String docid, final String fullname, final String permission, final String roleid, final String ip, final String userid, final String filename, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DELETE_DOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response deldoc", response);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equals("true")) {

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        String slid = FileViewActivity.slidDynamic;


                    } else if (error.equals("false")) {


                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        fileViewLists.remove(position);
                        notifyDataSetChanged();

                    }


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
                params.put("docid", docid);
                params.put("fullname", fullname);
                params.put("ip", ip);
                params.put("permission", permission);
                params.put("roleid", roleid);
                params.put("filename", filename);


                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        vg.setEnabled(enable);
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            child.setClickable(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }

    public void groupMemberList(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response in getmembrs", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {


                        String names = jsonArray.getString(i);

                        String namesOnly = names.substring(0, names.indexOf("&&"));

                        if (namesOnly.isEmpty() || namesOnly.equals("")) {

                            Log.e("names", "null");


                        } else {

                            // spinnermemberfullwithidList.add(names);

                            // usernamelist.add(new User(names,R.drawable.ic_user));


                            String username = names.substring(0, names.indexOf("&&"));

                            usernamelistwithSlid.add(new User(username, names, R.drawable.ic_user));
                            usernamelist.add(new User(username, names, R.drawable.ic_user));

                            // String userid  = names.substring(names.indexOf("&&"),names.length());


                            // spinnermemberlist.add(username);

                        }

                    }


                    //Initializing and attaching adapter for AutocompleteTextView
                    filterAdapter = new TokenFilterAdapter(context, R.layout.item_user, usernamelist) {
                        @Override
                        protected boolean keepObject(User obj, String mask) {


                            //CustomSimpleSearchAdapter.this.adapter.data.get(i).toString().toLowerCase().indexOf(charSequence.toString().toLowerCase()) == 0
                            mask = mask.toLowerCase();
                            return obj.getUsername().toLowerCase().contains(mask);

                            //return  obj.getUsername().toLowerCase().indexOf(mask.toLowerCase()) == 0;


                        }
                    };

                    autoCompleteTextView.setAdapter(filterAdapter);
                    autoCompleteTextView.allowDuplicates(false);
                    autoCompleteTextView.setShowAlways(true);
                    autoCompleteTextView.showDropDown();
                    autoCompleteTextView.deleteText();
                    autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            autoCompleteTextView.showDropDown();
                            autoCompleteTextView.deleteText();


                        }
                    });


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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userid", userid);


                return parameters;
            }


        };


        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private JSONObject makeJsonObjectSingleShare(ArrayList<String> docid, ArrayList<String> userid, ArrayList<String> shareuserids,
                                                 int formcount)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();

        for (int i = 0; i < formcount; i++) {
            obj = new JSONObject();
            try {
                obj.put("docid", docid.get(0));
                obj.put("userid", userid.get(0));
                obj.put("shareUserIds", shareuserids.get(i));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("SingleShare", jsonArray1);
        return finalobject;
    }


    void ShareSingleFiles(final String userid, final String userids, final String docids, final String ip, final String username) {

        shareuseridsList.clear();
        docidList.clear();

        sharedFileStatusList.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("reponse singlefiles", response);

                try {
                  /*  JSONArray jsonArray = new JSONArray(response);
                    String error = jsonArray.getJSONObject(0).getString("error");
                    String message = jsonArray.getJSONObject(0).getString("message");
                    Log.e("error",error);

                    switch (error) {
                        case "null":

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            break;
                        case "true":

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            break;
                        case "false":

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            break;
                    }*/


                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        String toUsername = jsonObject.getString("toUsername");
                        String docname = jsonObject.getString("docname");

                        if (error.equals("null") || error.equals("true")) {

                            String status = docname + " " + "to" + " " + toUsername + " " + message;
                            sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_close_black_24dp));
                        } else if (error.equals("false")) {

                            String status = docname + " " + "to" + " " + toUsername + " " + message;
                            sharedFileStatusList.add(new SharedFileStatus(status, R.drawable.ic_check_green_24dp));

                        }


                    }


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);

                    rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                    LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(context);
                    rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);
                    rvSharedFileStatus.setHasFixedSize(true);
                    rvSharedFileStatus.setItemViewCacheSize(sharedFileStatusList.size());

                    SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, context);
                    rvSharedFileStatus.setAdapter(sharedFileStatusAdapter);

                    Log.e("rv height", String.valueOf(rvSharedFileStatus.getHeight()));
                    Log.e("rv getmeasuredheight", String.valueOf(rvSharedFileStatus.getMeasuredHeight()));

                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_no_sharedstatus_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });

                    dialogBuilder.setView(dialogView);

                  /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(alertDialog.getWindow().getAttributes());
                    int dialogWidth = lp.width;
                    int dialogHeight = lp.height;

*/

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    /*if(dialogHeight>400){

                        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,400);

                    }
                    else{

                        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }*/
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    alertDialog.show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Userid", userid);
                parameters.put("userids", userids);
                parameters.put("docids", docids);
                parameters.put("ip", ip);
                parameters.put("username", username);

                return parameters;
            }

        };


        alertDialog.dismiss();

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }


    public void clearCheckedList() {

        checkedlist.clear();
        docidList.clear();
        docidlist.clear();


    }

    public void checkboxUnselectAll() {

        for (FileViewList list :
                fileViewLists
        ) {

            list.setSelected(false);
        }


    }

    void getMetadata(String docname, String docid, TextView tvMeta, ProgressBar pg) {

        pg.setVisibility(View.VISIBLE);
        tvMeta.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String metadata;
                StringBuilder sb = new StringBuilder();
                String meta = null;

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {

                        meta = "No Metadata Found";
                        pg.setVisibility(View.GONE);
                        tvMeta.setText(Html.fromHtml(meta), TextView.BufferType.SPANNABLE);
                        tvMeta.setVisibility(View.VISIBLE);


                    } else {

                        //clearing the stringbuilder string which is dynamically building
                        sb.setLength(0);


                        for (int j = 0; j < jsonArray.length(); j++) {

                            metadata = jsonArray.get(j).toString();//{"AadharNumber":"1003"}
                            String a = metadata.substring(metadata.indexOf("{") + 1, metadata.indexOf("}"));//"AadharNumber":"1003"
                            String b = a.substring(0, a.indexOf(":"));//"AadharNumber"
                            String label = b.substring(1, b.length() - 1);//label = AadharNumber
                            String c = a.substring(a.indexOf(":") + 1);//"1003"

                            if (c.equals("null")) {

                                c = " ";
                                sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");
                            } else {

                                c = c.substring(1, c.length() - 1);
                                sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");

                            }

                        }

                        meta = sb.toString();


                        Log.e("meta in fileview", meta);


                        pg.setVisibility(View.GONE);
                        tvMeta.setText(Html.fromHtml(meta), TextView.BufferType.SPANNABLE);
                        tvMeta.setVisibility(View.VISIBLE);


                    }


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
                Map<String, String> params = new HashMap<>();
                params.put("docname", docname);
                params.put("docid", docid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.checkBox.setChecked(false);
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvcheckinstatus, tvVersionStatus, tvFileName, tvFileSize, tvNoOfPages, tvUploadedBy, tvUplodedDate, tvFilePath, tvFileType, tvMetaData, tvDocid;
        //  ImageView ivAction;

        ImageView ivMetadata, ivOpenFile, ivDelete, ivFiletypeIcon, ivShare, ivVersion, ivCheckin, ivCheckOut, ivMore;

        CardView cardView;
        CheckBox checkBox;

        FileViewActivity fileViewActivity;

        LinearLayout llOptions;
        ProgressBar progressBarVersion;

        public ViewHolder(View itemView, final FileViewActivity fileViewActivity) {
            super(itemView);


            //this.setIsRecyclable(false);
            this.fileViewActivity = fileViewActivity;


            tvDocid = itemView.findViewById(R.id.tv_dms_list_docid);
            tvFileName = itemView.findViewById(R.id.tv_dms_list_filename);
            tvFileSize = itemView.findViewById(R.id.tv_dms_list_filesize);
            tvNoOfPages = itemView.findViewById(R.id.tv_dms_list_NoOfPages);
            tvUploadedBy = itemView.findViewById(R.id.tv_dms_list_uploadedby);
            tvUplodedDate = itemView.findViewById(R.id.tv_dms_list_uploadedDate);
            tvFilePath = itemView.findViewById(R.id.tv_dms_list_filepath);
            //  ivAction = itemView.findViewById(R.id.iv_dms_list_actions);
            tvFileType = itemView.findViewById(R.id.tv_dms_list_filetype);
            tvMetaData = itemView.findViewById(R.id.tv_dms_list_metadata);
            tvVersionStatus = itemView.findViewById(R.id.tv_dms_list_versionstatus);
            tvcheckinstatus = itemView.findViewById(R.id.tv_dms_list_checkinstatus);

            ivMetadata = itemView.findViewById(R.id.iv_show_metadata);
            ivOpenFile = itemView.findViewById(R.id.iv_open_file);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivFiletypeIcon = itemView.findViewById(R.id.iv_filetype);
            ivVersion = itemView.findViewById(R.id.iv_version);
            ivCheckin = itemView.findViewById(R.id.iv_checkin);
            ivCheckOut = itemView.findViewById(R.id.iv_checkout);
            ivShare = itemView.findViewById(R.id.iv_share);
            ivMore = itemView.findViewById(R.id.three_dots);

            cardView = itemView.findViewById(R.id.cv_search_list);
            checkBox = itemView.findViewById(R.id.checkbox_dms_list);

            cardView.setOnLongClickListener(fileViewActivity);

            progressBarVersion = itemView.findViewById(R.id.progressbar_version);

            //

            llOptions = itemView.findViewById(R.id.ll_dms_fileview_options);


        }
    }


    //Downloading file
    private void DownloadFile(String url) {

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

    private void showAudioPlayerDialog(Context context, String filepath, String songname) {

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
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }


    private void delVersion(final String docid, final int pos) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DEL_DOC_VERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("onResponse: ", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        versionList.remove(pos);
                        versionAdapter.notifyItemRemoved(pos);
                        //notifyItemChanged(pos);

                        if (versionList.size() == 0) {


                            progressBarVersion.setVisibility(View.GONE);
                            rvVersion.setVisibility(View.GONE);
                            tvNoVersionFound.setVisibility(View.VISIBLE);

                        }


                    }

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

                Map<String, String> params = new HashMap<>();
                params.put("docid", docid);
                params.put("ip", MainActivity.ip);
                params.put("username", MainActivity.username);
                params.put("userid", MainActivity.userid);

                Util.printParams(params, "version_del_params");
                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    /*//Download file from server
    public class DownloadFile extends AsyncTask<String, Integer, String> {

        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadFile(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;


            String filename = sUrl[0].substring(sUrl[0].lastIndexOf("/"), sUrl[0].length());
            String filetype = filename.substring(filename.lastIndexOf("."), filename.length());
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeOffice Downloads");

                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();

                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/" + filename + "." + filetype);
                } else {
                    output = new FileOutputStream("/sdcard/EzeeProcess Downloads/" + filename + "." + filetype);
                }


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

           *//* mNotifyManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setContentTitle("File Download")
                    .setContentText("Download in progress")
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_file_download_white_24dp)*//*
            ;


            //Toast.makeText(context, "Downloading the file... The download progress is on notification bar.", Toast.LENGTH_LONG).show();

            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);

           *//* mBuilder.setProgress(100, progress[0], false);
            // Displays the progress bar on notification
            mNotifyManager.notify(0, mBuilder.build());*//*

        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded Succesfully", Toast.LENGTH_SHORT).show();
           *//* mBuilder.setContentTitle("File Download Succesfully")
                     .setContentText("")
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.drawable.ic_file_download_white_24dp);

             mNotifyManager.notify(0, mBuilder.build());*//*


        }
    }*/

}
