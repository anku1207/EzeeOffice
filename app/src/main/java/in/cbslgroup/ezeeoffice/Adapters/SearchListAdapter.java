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
import android.os.PowerManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.Player.VideoPlayerActivity;
import in.cbslgroup.ezeeoffice.Activity.Search.QuickSearchActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.ImageViewerActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.PdfViewerActivity;
import in.cbslgroup.ezeeoffice.Model.Search;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    AlertDialog alertDialog;
    String Roleid;


    OnClickListener onClickListener;
    String metaData = "";
    TextView textViewMeta;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Context context;
    private List<Search> searchList;
    DownloadManager downloadManager;

    PowerManager.WakeLock wakeLock;

    ProgressDialog mProgressDialog;
    private static final String DOCUMENT_URL = ApiUrl.BASE_URL;
    // String filetype,path,filename,size,date;


    Search searchObj;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public SearchListAdapter(Context context, List<Search> searchList) {
        this.context = context;
        this.searchList = searchList;

    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        searchObj = searchList.get(position);

        holder.tvFilename.setText(searchList.get(position).getFileName());
        holder.tvFileSize.setText(searchList.get(position).getFileSize());
        holder.tvNoOfPages.setText(searchList.get(position).getNoOfPages());
        holder.tvStorageName.setText(searchList.get(position).getStorageName());
        holder.tvMetadata.setText(searchList.get(position).getMetadata());
        // holder.tvMetadata.setText(searchList.get(position).getMetadata());
        holder.tvPath.setText(searchList.get(position).getPath());
        holder.tvExtension.setText(searchList.get(position).getExtension());
        holder.tvdate.setText(searchList.get(position).getDate());
        holder.tvdocid.setText(searchList.get(position).getDocid());

        final String path = searchList.get(position).getPath();
        final String filetype = searchList.get(position).getExtension();
        Log.e("extension", filetype + " " + position);
        final String filename = searchList.get(position).getFileName();
        final String size = searchList.get(position).getFileSize();
        final String date = searchList.get(position).getDate();
        final String meta = holder.tvMetadata.getText().toString();
        final String docid = holder.tvdocid.getText().toString();
        final String filepath = searchList.get(position).getPath();


        if (QuickSearchActivity.pdfView.equalsIgnoreCase("1") || QuickSearchActivity.imageView.equalsIgnoreCase("1")) {

            holder.ivOpenFile.setVisibility(View.VISIBLE);

        } else {


            holder.ivOpenFile.setVisibility(View.GONE);
        }
        if (QuickSearchActivity.viewMetadata.equalsIgnoreCase("1")) {
            holder.ivMetadata.setVisibility(View.VISIBLE);

        } else {

            holder.ivMetadata.setVisibility(View.GONE);

        }

        if (QuickSearchActivity.fileDelete.equalsIgnoreCase("1")) {

            holder.ivDelete.setVisibility(View.VISIBLE);
        } else {

            holder.ivDelete.setVisibility(View.GONE);

        }


        switch (filetype.toLowerCase()) {
            case "pdf":

                holder.ivFiletype.setImageResource(R.drawable.pdf_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;
            case "jpg":
                holder.ivFiletype.setImageResource(R.drawable.jpg_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;
            case "png":
                holder.ivFiletype.setImageResource(R.drawable.png_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);

                break;
            case "gif":
                holder.ivFiletype.setImageResource(R.drawable.gif_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);

                break;
            case "jpeg":
                holder.ivFiletype.setImageResource(R.drawable.jpeg_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);

                break;

            case "tiff":
                holder.ivFiletype.setImageResource(R.drawable.tiff_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;
            case "tif":
                holder.ivFiletype.setImageResource(R.drawable.tif_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "doc":
                holder.ivFiletype.setImageResource(R.drawable.doc_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "txt":
                holder.ivFiletype.setImageResource(R.drawable.unsupported_file_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "docx":
                holder.ivFiletype.setImageResource(R.drawable.docx_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;


            case "psd":
                holder.ivFiletype.setImageResource(R.drawable.psd_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;

            case "bmp":
                holder.ivFiletype.setImageResource(R.drawable.bmp_128px);
                holder.ivOpenFile.setImageResource(R.drawable.file_view_48px);
                break;


            case "mp4":

                holder.ivFiletype.setImageResource(R.drawable.mp4_128px);
                holder.ivOpenFile.setImageResource(R.drawable.ic_play_circle_filled_blue_24dp);
                break;


            case "3gp":
                holder.ivFiletype.setImageResource(R.drawable.threegp_128px);
                holder.ivOpenFile.setImageResource(R.drawable.ic_play_circle_filled_blue_24dp);
                break;

            case "mp3":
                holder.ivFiletype.setImageResource(R.drawable.mp3_128px);
                holder.ivOpenFile.setImageResource(R.drawable.ic_play_circle_filled_blue_24dp);
                break;

            default:

                holder.ivFiletype.setImageResource(R.drawable.unsupported_file_128px);
                holder.ivOpenFile.setImageResource(R.drawable.unsupported_file_48px);
                break;
        }


//        switch (filetype) {
//            case "pdf":
//                holder.ivFiletype.setImageResource(R.drawable.pdf);
//                break;
//            case "jpg":
//                holder.ivFiletype.setImageResource(R.drawable.jpg);
//                break;
//            case "png":
//                holder.ivFiletype.setImageResource(R.drawable.png);
//                break;
//            case "jpeg":
//                holder.ivFiletype.setImageResource(R.drawable.jpeg);
//                break;
//            case "tiff":
//                holder.ivFiletype.setImageResource(R.drawable.tiff_png);
//                break;
//            case "mp4":
//                holder.ivFiletype.setImageResource(R.drawable.mp4);
//                break;
//            default:
//                holder.ivFiletype.setImageResource(R.drawable.file);
//                break;
//        }


        holder.ivFiletype.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("filepath", path);
                    intent.putExtra("docid", docid);
                    context.startActivity(intent);

                } else if (filetype.equals("jpg")
                        || filetype.equals("jpeg")
                        || filetype.equals("png")
                        || filetype.equals("bmp")
                        || filetype.equals("gif")) {
                    Intent intent = new Intent(context, ImageViewerActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("type", filetype);
                    intent.putExtra("name", filename);
                    intent.putExtra("size", size);
                    intent.putExtra("path", path);
                    intent.putExtra("docid", docid);
                    context.startActivity(intent);

                } else if (filetype.equals("mp4") || filetype.equals("3gp")) {
                    //holder.ivFiletype.setImageResource(R.drawable.ic_round_pdf);
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("filename", filename);
                    intent.putExtra("filepath", filepath);
                    context.startActivity(intent);

                } else if (filetype.equalsIgnoreCase("mp3")) {

                    showAudioPlayerDialog(context, filepath, filename);

                } else {
                   // Toast.makeText(context, "File not supported", Toast.LENGTH_LONG).show();

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


                                    Log.e("doc path list", DOCUMENT_URL + path);

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


                                    DownloadFile(DOCUMENT_URL + path);


                                }
                            });
                    alertDialog.show();


                    //Log.e("File not supported","true");

                }
            }


        });

        holder.ivOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                } else if (filetype.equals("jpg")
                        || filetype.equals("jpeg")
                        || filetype.equals("png")
                        || filetype.equals("bmp")
                        || filetype.equals("gif")) {
                    // holder.ivFiletypeIcon.setImageResource(R.drawable.ic_round_image_lightblue);
                    Intent intent = new Intent(context, ImageViewerActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("type", filetype);
                    intent.putExtra("name", filename);
                    intent.putExtra("size", size);
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


        });

        holder.ivMetadata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Position  :  " + position, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.show_meta_dialog, null);

                ProgressBar pb = dialogView.findViewById(R.id.pg_dms_file_metadata_popup);
                pb.setVisibility(View.VISIBLE);

                textViewMeta = dialogView.findViewById(R.id.tv_dms_file_metadata_popup);

                Log.e("metadata in getspecific", holder.tvMetadata.getText().toString());
                Log.e("docid in getspecific", holder.tvdocid.getText().toString());

                String docname = holder.tvMetadata.getText().toString();
                String docid = holder.tvdocid.getText().toString();

                getMetdataSpecific(docid, docname, pb);


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
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String fullname = MainActivity.username;
                String roleid = QuickSearchActivity.Roleid;
                String ip = MainActivity.ip;
                String userid = MainActivity.userid;
                String filename = String.valueOf(holder.tvFilename.getText());

                onClickListener.onDeleteClick(searchObj,docid, fullname, "No", roleid, ip, userid, filename, String.valueOf(position));



            }
        });



      /*  holder.ivActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setAnimation(buttonClick);
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


                        if (path == null || path.isEmpty()) {

                            Log.e("file path", "null");
                            Toast.makeText(context, "File path is null", Toast.LENGTH_LONG).show();

                        }

                        else{

                            if(filetype.equals("pdf")){
                                Intent intent = new Intent(context, PdfViewerActivity.class);
                                intent.putExtra("filename",filename);
                                intent.putExtra("filepath", path);

                                popupWindow.dismiss();
                                context.startActivity(intent);

                            }

                            else if(filetype.equals("jpg")||filetype.equals("jpeg")||filetype.equals("png")){
                                Intent intent = new Intent(context, ImageViewerActivity.class);
                                intent.putExtra("date",date);
                                intent.putExtra("type", filetype);
                                intent.putExtra("name",filename);
                                intent.putExtra("size",size);
                                intent.putExtra("path",path);

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
                popupWindow.showAsDropDown(v,-40, 18);



            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

//    private void deleteDoc(final String docid, final String fullname, final String permission, final String roleid, final String ip, final String userid, final String filename, final String position) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DELETE_DOC, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.e("response deldoc", response);
//
//                try {
//
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    String message = jsonObject.getString("message");
//                    String error = jsonObject.getString("error");
//
//                    if (error.equals("true")) {
//
//                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//
//
//                    } else if (error.equals("false")) {
//
//                        searchList.remove(Integer.parseInt(position));
//                        notifyItemRemoved(Integer.parseInt(position));
//
//
//                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//
//                        if (getItemCount() == 0) {
//                            QuickSearchActivity.tvRvNoFileFound.setVisibility(View.VISIBLE);
//
//
//                        } else {
//
//                            QuickSearchActivity.tvRvNoFileFound.setVisibility(View.GONE);
//
//                        }
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("userid", userid);
//                params.put("docid", docid);
//                params.put("fullname", fullname);
//                params.put("ip", ip);
//                params.put("permission", permission);
//                params.put("roleid", roleid);
//                params.put("filename", filename);
//
//
//                return params;
//            }
//        };
//
//        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);
//
//
//    }

    public void getMetdataSpecific(final String docid, final String docname, ProgressBar progressBar) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_MULTI_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("metadataspecific", response);
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {

                        textViewMeta.setVisibility(View.VISIBLE);
                        textViewMeta.setText("No metadata found");
                        progressBar.setVisibility(View.GONE);


                    } else {

                        String metadata;
                        StringBuilder sb = new StringBuilder();
                        Log.e("array length", String.valueOf(jsonArray.length()));
                        //clearing the stringbuilder string which is dynamically building
                        sb.setLength(0);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("i", String.valueOf(i));

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.e("jsonobject", jsonObject.toString());


////                            if(jsonObject.has("null")|| jsonObject instanceof JSONObject){
////
////                                Log.e("jsonobject","is_null");
////                            }
//
//                            else{
//
//
//
//
//                                //  String uploadedBy = jsonArray.getJSONObject(i).getString("uploaded_by");
//
//                            }


                            metadata = jsonObject.toString();//{"AadharNumber":"1003"}
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

                        Log.e("metadata", sb.toString());

                        metaData = sb.toString();
                        textViewMeta.setText(Html.fromHtml(metaData), TextView.BufferType.SPANNABLE);
                        progressBar.setVisibility(View.GONE);

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

                params.put("docname", docname);
                params.put("docid", docid);
                return params;
            }
        };


        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }


    public interface OnClickListener {

        void onDeleteClick(Search searchobj,String docid, String fullname, String permission, String roleid, String ip, String userid, String filename, String position);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFilename, tvFileSize, tvNoOfPages, tvMetadata, tvStorageName, tvPath, tvExtension, tvdate, tvdocid;

        ImageView ivActions, ivFiletype, ivOpenFile, ivDelete, ivMetadata;
        //CardView cvList;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFilename = itemView.findViewById(R.id.tv_search_list_filename);
            tvFileSize = itemView.findViewById(R.id.tv_search_list_filesize);
            tvNoOfPages = itemView.findViewById(R.id.tv_search_list_NoOfPages);
            tvMetadata = itemView.findViewById(R.id.tv_search_list_metadata);
            tvStorageName = itemView.findViewById(R.id.tv_search_list_storagename);
            tvPath = itemView.findViewById(R.id.tv_search_list_path);
            tvExtension = itemView.findViewById(R.id.tv_search_list_extension);
            tvdate = itemView.findViewById(R.id.tv_search_list_date);
            tvdocid = itemView.findViewById(R.id.tv_search_list_docid);

            //  ivActions= itemView.findViewById(R.id.iv_search_list_actions);

            ivFiletype = itemView.findViewById(R.id.iv_search_list_filetype);
            ivOpenFile = itemView.findViewById(R.id.iv_search_list_openfile);
            ivDelete = itemView.findViewById(R.id.iv_search_list_delete);
            ivMetadata = itemView.findViewById(R.id.iv_search_list_show_metadata);


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

       /* AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
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
*/

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
//                File myDirectory = new File(Environment.getExternalStorageDirectory(), "EzeeOffice Downloads");
//
//                if(!myDirectory.exists()) {
//                    myDirectory.mkdirs();
//
//                    output = new FileOutputStream("/sdcard/EzeeOffice Downloads/"+filename+"."+filetype);
//                }
//                else{
//                    output = new FileOutputStream("/sdcard/EzeeOffice Downloads/"+filename+"."+filetype);
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

