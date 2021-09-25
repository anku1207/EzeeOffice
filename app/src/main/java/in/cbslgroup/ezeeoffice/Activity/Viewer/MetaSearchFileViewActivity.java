package in.cbslgroup.ezeeoffice.Activity.Viewer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.Activity.Dms.CheckInActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.MoveStorageActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.FileViewListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.MetadataFileViewListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.SharedFileStatusAdapter;
import in.cbslgroup.ezeeoffice.Chip.FilterAdapter;
import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.Chip.UsernameCompletionView;
import in.cbslgroup.ezeeoffice.Model.MetaDataViewList;
import in.cbslgroup.ezeeoffice.Model.SharedFileStatus;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class MetaSearchFileViewActivity extends AppCompatActivity implements View.OnLongClickListener {

    public static String doclistArrayMeta;
    public static String movefile, copyfile, checkinCheckout, fileVersion, fileDelete, shareFile, viewMetadata, pdfView, imageView;
    private final Context ACTIVITY = MetaSearchFileViewActivity.this;
    public Boolean in_action_mode = false;
    RecyclerView rvMetaView;
    ProgressBar progressBar;
    String slid, metadata, userid, foldername;
    List<MetaDataViewList> metadatafileViewList = new ArrayList<>();
    LinearLayout ll_nofilefound;
    MetadataFileViewListAdapter metadataFileViewListAdapter;
    Toolbar toolbar;
    //Checkitem list
    ArrayList<String> SelectionList = new ArrayList<>();
    int counter = 0;
    boolean onlongclick = false;
    AlertDialog alertDialog;
    ArrayList<User> usernamelist = new ArrayList<>();
    ArrayList<String> docidList = new ArrayList<>();
    ArrayList<String> useridList = new ArrayList<>();
    ArrayList<String> shareuseridsList = new ArrayList<>();
    ArrayList<SharedFileStatus> sharedFileStatusList = new ArrayList<>();
    SharedFileStatusAdapter sharedFileStatusAdapter;
    RecyclerView rvSharedFileStatus;
    TextView tvNoofItemCount;
    RelativeLayout rlMain;
    Boolean checkAdmin = false;
    ProgressDialog pdMail;
    private UsernameCompletionView autoCompleteTextView;
    private FilterAdapter filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_search_file_view);

        toolbar = findViewById(R.id.toolbar_storage_management_metasearch);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        progressBar = findViewById(R.id.progressBar_storage_metasearchlist);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        pdMail = new ProgressDialog(this);
        ll_nofilefound = findViewById(R.id.metafileview_nofilefound);
        rlMain = findViewById(R.id.rl_metafileview_rv_tvnoofitem);
        slid = getIntent().getStringExtra("slid");
        metadata = getIntent().getStringExtra("metadata");

        Log.e("meta 1 ", metadata);


     /*   slid = getIntent().getStringExtra("slid");
        slidDynamic = slid;*/

        rvMetaView = findViewById(R.id.rv_dms_metaview);

        LinearLayoutManager linearLayoutManagerFileView = new LinearLayoutManager(this);
        rvMetaView.setLayoutManager(linearLayoutManagerFileView);
        rvMetaView.setHasFixedSize(true);

        tvNoofItemCount = findViewById(R.id.metafileview_nooffile);

        getUserRole(MainActivity.userid);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_save_query) {

            showBottomSheetSaveSearchDialog();

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fileview_share) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            if (counter != 0) {

                showMultiShareDialog();

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < FileViewListAdapter.checkedlist.size(); i++) {

                    sb.append(FileViewListAdapter.checkedlist.get(i)).append(",");

                }


                // Toast.makeText(FileViewActivity.this, sb, Toast.LENGTH_LONG).show();

                SelectionList.clear();


            } else {

                Toast.makeText(MetaSearchFileViewActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }


            return true;
        }

        if (id == R.id.action_fileview_delete) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            if (counter != 0) {


                // docidList.clear();

                showMultiDelDialog();

                for (String selection : SelectionList) {

                    Log.e("selection", selection);

                }


                SelectionList.clear();

                //docidList.clear();

            } else {

                Toast.makeText(MetaSearchFileViewActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }


            return true;
        }


        if (id == R.id.action_fileview_move) {

            if (counter != 0) {


                // docidList.clear();

                showMultiMoveDialog();


                SelectionList.clear();

                //docidList.clear();

            } else {

                Toast.makeText(MetaSearchFileViewActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }
            return true;
        }


        if (id == R.id.action_fileview_copy) {

            if (counter != 0) {


                // docidList.clear();

                showMultiCopyDialog();


                SelectionList.clear();

                //docidList.clear();

            } else {

                Toast.makeText(MetaSearchFileViewActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }
            return true;
        }

        if (id == R.id.action_fileview_file_mail) {

            if (counter != 0) {


                showMultiSendMailDialog();

            } else {

                Toast.makeText(MetaSearchFileViewActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    void multiMetaSearch(String metadata, String slid) {

        ll_nofilefound.setVisibility(View.GONE);
        rlMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tvNoofItemCount.setVisibility(View.GONE);
        metadatafileViewList.clear();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_MULTI_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("multimetadata", response);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");

                    Log.e("error", error);

                    if (error.equals("true")) {

                        tvNoofItemCount.setVisibility(View.GONE);
                        tvNoofItemCount.setText("0 files found");
                        progressBar.setVisibility(View.GONE);
                        rlMain.setVisibility(View.GONE);
                        ll_nofilefound.setVisibility(View.VISIBLE);


                    } else if (error.equals("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("metalist");
                        Log.e("metalistsize", String.valueOf(jsonArray.length()));
                        // String error =jsonArray.getJSONObject(0).getString("error");

                        if(jsonArray.length()==0){

                            ll_nofilefound.setVisibility(View.VISIBLE);
                            rlMain.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                        }

                        else{


                            ll_nofilefound.setVisibility(View.GONE);
                            rlMain.setVisibility(View.VISIBLE);

                            tvNoofItemCount.setText(jsonArray.length() + " files found");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String fileName = jsonArray.getJSONObject(i).getString("old_doc_name");
                                String noOfPages = jsonArray.getJSONObject(i).getString("noofpages");
                                String fileSize = jsonArray.getJSONObject(i).getString("doc_size");
                                String filetype = jsonArray.getJSONObject(i).getString("doc_extn");
                                String uploadedBy = jsonArray.getJSONObject(i).getString("uploaded_by");
                                String uploadedDate = jsonArray.getJSONObject(i).getString("dateposted");
                                String fullpath = jsonArray.getJSONObject(i).getString("doc_path");
                                String docid = jsonArray.getJSONObject(i).getString("doc_id");
                                String docname = jsonArray.getJSONObject(i).getString("doc_name");
                                String checkin_out = jsonArray.getJSONObject(i).getString("checkin_status");
                                Log.e("checkin_status", checkin_out);

                                Double fileSizeDouble = Double.parseDouble(fileSize) / 1048576;
                                Log.e("File size ", String.valueOf(fileSizeDouble));
                                String fileSizeInMb = String.valueOf(Math.round(fileSizeDouble * 100D) / 100D);
                                Log.e("File size in mb ", fileSizeInMb);

                                metadatafileViewList.add(new MetaDataViewList(fileName, fileSizeInMb + " " + "mb", noOfPages, uploadedBy, uploadedDate, fullpath, filetype, "null", docid, docname,"1",checkin_out));

                            }



                            Log.e("meta file list size", String.valueOf(metadatafileViewList.size()));


                            metadataFileViewListAdapter = new MetadataFileViewListAdapter(metadatafileViewList, MetaSearchFileViewActivity.this);
                            rvMetaView.setAdapter(metadataFileViewListAdapter);
                            metadataFileViewListAdapter.setOnClickListener(new MetadataFileViewListAdapter.OnClickListener() {
                                @Override
                                public void onCheckInBtnClick(View v, int position, String docid, String docname) {


                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                    TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                    tvHeading.setText("Check In");
                                    tvHeading.setBackgroundColor(getResources().getColor(R.color.green_dark));


                                    TextView tvSubheading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                    tvSubheading.setText("Are you sure you want to check In document " + docname + " ?");

                                    Button btn_cancel = dialogView.findViewById(R.id.btn_no_pDel_popup);
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            alertDialog.dismiss();
                                        }
                                    });

                                    Button btn_ok = dialogView.findViewById(R.id.btn_yes_pDel_popup);
                                    btn_ok.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            checkIn(docid);


                                            alertDialog.dismiss();
                                        }
                                    });

                                    dialogBuilder.setView(dialogView);

                                    alertDialog = dialogBuilder.create();
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    alertDialog.show();

                                }

                                @Override
                                public void onCheckOutBtnClick(View v, int position, String docid, String docname) {

                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                    TextView tvHeading = dialogView.findViewById(R.id.tv_alert_permanent_del_heading);
                                    tvHeading.setText("Check Out");
                                    //tvHeading.setBackgroundColor(Color.GREEN);


                                    TextView tvSubheading = dialogView.findViewById(R.id.tv_recyclebin_perm);
                                    tvSubheading.setText("Are you sure you want to check Out document " + docname + " ?");


                                    Button btn_cancel = dialogView.findViewById(R.id.btn_no_pDel_popup);
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            alertDialog.dismiss();
                                        }
                                    });

                                    Button btn_ok = dialogView.findViewById(R.id.btn_yes_pDel_popup);
                                    btn_ok.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            checkOut(docid);


                                            alertDialog.dismiss();
                                        }
                                    });

                                    dialogBuilder.setView(dialogView);

                                    alertDialog = dialogBuilder.create();
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    alertDialog.show();


                                }
                            });

                            progressBar.setVisibility(View.GONE);
                            tvNoofItemCount.setVisibility(View.VISIBLE);
                            tvNoofItemCount.setText(metadataFileViewListAdapter.getItemCount() + " files found");



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

                Map<String, String> params = new HashMap<String, String>();

                char singleQuote = '"';
                params.put("metadata", metadata);
                params.put("slid", slid);
                params.put("userid", MainActivity.userid);


                JSONObject jsMeta = new JSONObject(params);
                Log.e("js metasearch", jsMeta.toString());

                return params;

            }


        };

        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
//        if(requestCode==007)
//        {
//           String meta = data.getStringExtra("metadata");
//           multiMetaSearch(meta,slid);
//        }
    }

    public void prepareSelection(View v, int position, ArrayList<String> docidlist) {


        docidList = docidlist;


        if (((CheckBox) v).isChecked()) {

            SelectionList.add("" + position);

            counter++;
            updateCounter(counter);

        } else {

            SelectionList.remove("" + position);
            counter--;
            updateCounter(counter);
        }


    }


    // Updating the counter number of the selected item
    // How many items are selected in the multislect mode
    public void updateCounter(int counter) {

        if (counter == 0) {

            toolbar.setTitle("0 item selected");


        } else {

            toolbar.setTitle(counter + " item selected");
        }

    }

    public void groupMemberList(final String userid) {

        usernamelist.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response membrs flview", response);

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
                            usernamelist.add(new User(username, names, R.drawable.ic_user));


                            // usernamelistwithSlid.add(new User(username,names,R.drawable.ic_user));
                            //  usernamelist.add(new User(username,names,R.drawable.ic_user));

                            // String userid  = names.substring(names.indexOf("&&"),names.length());


                            // spinnermemberlist.add(username);

                        }

                    }


                    //Initializing and attaching adapter for AutocompleteTextView
                    filterAdapter = new FilterAdapter(MetaSearchFileViewActivity.this, R.layout.item_user, usernamelist);

                    autoCompleteTextView.setAdapter(filterAdapter);

                    autoCompleteTextView.allowDuplicates(false);
                    autoCompleteTextView.setShowAlways(true);

                    autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            autoCompleteTextView.showDropDown();

                        }
                    });

                    autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            autoCompleteTextView.showDropDown();
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {


                            autoCompleteTextView.showDropDown();

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                            autoCompleteTextView.showDropDown();
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


        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);


    }

    void showMultiShareDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);
        LayoutInflater inflater = MetaSearchFileViewActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_share_files, null);

        autoCompleteTextView = dialogView.findViewById(R.id.autocomplete_textview);
        //Set the action to be taken when a Token is clicked
        autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);

        userid = DmsActivity.userid;
        groupMemberList(userid);

        Button btn_share = dialogView.findViewById(R.id.btn_share_popup);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONObject jsonObject = null;
                List<User> tokens = autoCompleteTextView.getObjects();
                StringBuilder content = new StringBuilder();
                for (int i = 0; i < tokens.size(); i++) {
                    String slid = tokens.get(i).getSlid();
                    content.append(slid.substring(slid.indexOf("&&") + 2)).append(" ");
                    shareuseridsList.add(slid.substring(slid.indexOf("&&") + 2));
                }
                //shareduserids = 1

                userid = MainActivity.userid;
                useridList.add(userid);

                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArray.put(docidList.get(i));

                }

                String jsonarraystringDocids = jsonArray.toString();

                Log.e("doclist json array", jsonarraystringDocids);

                for (int i = 0; i < shareuseridsList.size(); i++) {

                    jsonArray1.put(shareuseridsList.get(i));

                }

                String jsonarraystringUserids = jsonArray1.toString();

                Log.e("useridlist json array", jsonarraystringUserids);


                String username = MainActivity.username;


                if (shareuseridsList.size() != 0) {
                    ShareMultipeFiles(userid, jsonarraystringUserids, jsonarraystringDocids, MainActivity.ip, username);
                } else {

                    Toast.makeText(MetaSearchFileViewActivity.this, "Select atleast one user for sharing files", Toast.LENGTH_SHORT).show();

                }

                //   Toast.makeText(FileViewActivity.this,sb,Toast.LENGTH_LONG).show();


                //posting request


            }
        });


        Button btn_close = dialogView.findViewById(R.id.btn_close_share_popup);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (in_action_mode) {

                    counter = 0;
                    toolbar.getMenu().clear();
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    toolbar.setTitle("Storage Management");
                    toolbar.setSubtitle(foldername);
                    in_action_mode = false;
                    onlongclick = false;
                    metadataFileViewListAdapter.notifyDataSetChanged();

                    //code here for close dialog button
                    alertDialog.dismiss();

                }

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }

    void showMultiDelDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);


        TextView tvMessage = dialogView.findViewById(R.id.tv_alert_delete_message);
        tvMessage.setText("Are you sure you want to delete " + counter + " files ?");

        Button delPer = dialogView.findViewById(R.id.btn_per_delete_popup);

        Button btn_cancel = dialogView.findViewById(R.id.btn_close_delete_popup);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });


        if (checkAdmin) {

            delPer.setVisibility(View.VISIBLE);

        } else {

            delPer.setVisibility(View.GONE);

        }

        delPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("delMultidocids", jsonArrayDocids.toString());
                Log.e("delMultidocids", DmsActivity.dynamicFileSlid);


                multiDel(userid, MainActivity.username, MainActivity.ip, jsonArrayDocids.toString(), "Yes", DmsActivity.dynamicFileSlid);

                // docidList.clear();

                normalMode();

                metadataFileViewListAdapter.clearCheckedList();


                alertDialog.dismiss();


            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_delete_popup);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String fullname = String.valueOf(holder.tvUploadedBy.getText());
                String roleid = MainActivity.roleId;
                String ip = MainActivity.ip;
                String userid = MainActivity.userid;
                String username = MainActivity.username;

                //String filename = String.valueOf(holder.tvFileName.getText());
                // String docid = String.valueOf(holder.tvDocid.getText());

                //delete doc volley request
                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("delMultidocids", jsonArrayDocids.toString());
                Log.e("delMultidocids", DmsActivity.dynamicFileSlid);


                multiDel(userid, username, ip, jsonArrayDocids.toString(), "No", DmsActivity.dynamicFileSlid);

                // docidList.clear();

                normalMode();

                metadataFileViewListAdapter.clearCheckedList();


                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    void showMultiMoveDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);


        TextView tvHeading = dialogView.findViewById(R.id.tv_alert_restore_heading);
        tvHeading.setText("Move Files");

        TextView tvMessage = dialogView.findViewById(R.id.tv_recyclebin_restore_message);
        tvMessage.setText("Are you sure you want to move " + counter + " files ?");

        Button btn_cancel = dialogView.findViewById(R.id.btn_no_restore_popup);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();


            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_yes_restore_popup);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String fullname = String.valueOf(holder.tvUploadedBy.getText());
                String roleid = DmsActivity.roleid;
                String ip = MainActivity.ip;
                String userid = DmsActivity.userid;
                String username = MainActivity.username;

                //String filename = String.valueOf(holder.tvFileName.getText());
                // String docid = String.valueOf(holder.tvDocid.getText());

                //delete doc volley request
                //  docidList.clear();

                // fileViewListAdapter.clearCheckedList();

                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("moveMultidocids", jsonArrayDocids.toString());
                // Log.e("delMultidocids",DmsActivity.dynamicFileSlid);

                doclistArrayMeta = jsonArrayDocids.toString();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
                String slid_Session = sharedPreferences.getString("userSlid", null);

                //multiMove(username,userid,ip,slid_Session,);

                Intent intent = new Intent(MetaSearchFileViewActivity.this, MoveStorageActivity.class);
                intent.putExtra("mode", "movefiles");
                intent.putExtra("foldername&&slid", DmsActivity.foldernameDyanamic + "&&" + DmsActivity.dynamicFileSlid);
                intent.putExtra("viewer", "meta");
                //intent.putExtra("filecount",counter);

                startActivity(intent);

                normalMode();

                metadataFileViewListAdapter.clearCheckedList();

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }

    void showMultiCopyDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);


        TextView tvHeading = dialogView.findViewById(R.id.tv_alert_restore_heading);
        tvHeading.setText("Copy Files");

        TextView tvMessage = dialogView.findViewById(R.id.tv_recyclebin_restore_message);
        tvMessage.setText("Are you sure you want to copy " + counter + " files ?");

        Button btn_cancel = dialogView.findViewById(R.id.btn_no_restore_popup);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();


            }
        });


        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_yes_restore_popup);
        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuilder sb = new StringBuilder();

                for (String ids : docidList) {

                    sb.append(ids).append(",");
                    Log.e("ids", ids);

                }

                JSONArray jsonArrayDocids = new JSONArray();


                for (int i = 0; i < docidList.size(); i++) {

                    jsonArrayDocids.put(Integer.parseInt(docidList.get(i)));

                }

                Log.e("copyMultidocids", jsonArrayDocids.toString());
                // Log.e("delMultidocids",DmsActivity.dynamicFileSlid);

                doclistArrayMeta = jsonArrayDocids.toString();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
                String slid_Session = sharedPreferences.getString("userSlid", null);

                //multiMove(username,userid,ip,slid_Session,);

                Intent intent = new Intent(MetaSearchFileViewActivity.this, MoveStorageActivity.class);
                intent.putExtra("mode", "copyfiles");
                intent.putExtra("foldername&&slid", DmsActivity.foldernameDyanamic + "&&" + DmsActivity.dynamicFileSlid);
                intent.putExtra("viewer", "meta");
                //intent.putExtra("filecount",counter);

                startActivity(intent);

                normalMode();

                metadataFileViewListAdapter.clearCheckedList();

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


    }

    void normalMode() {

        if (in_action_mode) {

            counter = 0;
            toolbar.getMenu().clear();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle("Storage Management");
            toolbar.setSubtitle(foldername);
            in_action_mode = false;
            onlongclick = false;
            metadataFileViewListAdapter.notifyDataSetChanged();

            //code here for close dialog button
            alertDialog.dismiss();

        }


    }

    void ShareMultipeFiles(final String userid, final String userids, final String docids, final String ip, final String username) {


        sharedFileStatusList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("reponse sharefiles", response);

                try {
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


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaSearchFileViewActivity.this);

                    LayoutInflater inflater = MetaSearchFileViewActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_shared_multiple_files_status, null);

                    rvSharedFileStatus = dialogView.findViewById(R.id.rv_shared_file_status);
                    LinearLayoutManager linearLayoutManagerSharedFileStatus = new LinearLayoutManager(MetaSearchFileViewActivity.this);
                    rvSharedFileStatus.setLayoutManager(linearLayoutManagerSharedFileStatus);
                    rvSharedFileStatus.setHasFixedSize(true);
                    rvSharedFileStatus.setItemViewCacheSize(sharedFileStatusList.size());

                    SharedFileStatusAdapter sharedFileStatusAdapter = new SharedFileStatusAdapter(sharedFileStatusList, MetaSearchFileViewActivity.this);
                    rvSharedFileStatus.setAdapter(sharedFileStatusAdapter);


                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_no_sharedstatus_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });

                    dialogBuilder.setView(dialogView);

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();

                /* String error= jsonArray.getJSONArray(0).getJSONObject(0).getString("error");
                 String message = jsonArray.getJSONArray(0).getJSONObject(0).getString("message");
*/
                   /* switch (error) {
                        case "null":

                            Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                            break;
                        case "true":

                            Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                            break;
                        case "false":

                            Toast.makeText(FileViewActivity.this, message, Toast.LENGTH_LONG).show();

                            break;
                    }*/


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


        FileViewListAdapter.docidlist.clear();
        docidList.clear();
        shareuseridsList.clear();

        //switching to normal mode from multiselct mode
        normalMode();


        alertDialog.dismiss();

        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);


    }

    @Override
    public void onBackPressed() {


        if (in_action_mode) {

            counter = 0;
            toolbar.getMenu().clear();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle("Storage Management");
            toolbar.setSubtitle(foldername);
            in_action_mode = false;
            onlongclick = false;
            metadataFileViewListAdapter.notifyDataSetChanged();


        } else {

            super.onBackPressed();
            //this.finish();
           /* Intent intent = new Intent(MetaSearchFileViewActivity.this,DmsActivity.class);
            startActivity(intent);*/
            finish();
            // finishAffinity();


        }

    }

    void multiDel(final String userid, final String username, final String ip, final String delMultiDocids, final String delPermission, final String slid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("muldel", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MetaSearchFileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //docidList.clear();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(MetaSearchFileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        normalMode();
                        //docidList.clear();

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
                params.put("delIp", ip);
                params.put("delUsername", username);
                params.put("delMultiple", delPermission);
                params.put("delSlid", slid);
                params.put("delUserid", userid);
                params.put("delFileDocids", delMultiDocids);


                return params;
            }
        };


        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);


    }

    @Override
    public boolean onLongClick(View v) {


        if (!onlongclick) {


            toolbar.setBackgroundColor(getResources().getColor(R.color.black_blur));
            toolbar.setTitle("0 item selected");
            toolbar.inflateMenu(R.menu.fileview_multiselect_menu);

            if (movefile.equalsIgnoreCase("1")) {

                toolbar.getMenu().findItem(R.id.action_fileview_move).setVisible(true);

            } else {

                toolbar.getMenu().findItem(R.id.action_fileview_move).setVisible(false);

            }


            if (copyfile.equalsIgnoreCase("1")) {

                toolbar.getMenu().findItem(R.id.action_fileview_copy).setVisible(true);
            } else {
                toolbar.getMenu().findItem(R.id.action_fileview_copy).setVisible(false);

            }

            if (shareFile.equalsIgnoreCase("1")) {

                toolbar.getMenu().findItem(R.id.action_fileview_share).setVisible(true);
            } else {
                toolbar.getMenu().findItem(R.id.action_fileview_share).setVisible(false);

            }

            if (fileDelete.equalsIgnoreCase("1")) {

                toolbar.getMenu().findItem(R.id.action_fileview_delete).setVisible(true);
            } else {
                toolbar.getMenu().findItem(R.id.action_fileview_delete).setVisible(false);

            }


            toolbar.setSubtitle("");
            in_action_mode = true;
            metadataFileViewListAdapter.notifyDataSetChanged();
            onlongclick = true;

        } else {

            Log.e("onclick", "false");

        }

        return true;


    }

    void getUserRole(final String userid) {

        progressBar.setVisibility(View.VISIBLE);
        rlMain.setVisibility(View.GONE);
        ll_nofilefound.setVisibility(View.GONE);

        foldername = getIntent().getStringExtra("foldername");
        toolbar.setSubtitle(foldername);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String move_file = jsonObject.getString("move_file");
                    String copy_file = jsonObject.getString("copy_file");
                    String share_file = jsonObject.getString("share_file");//both places


                    String checkin_checkout = jsonObject.getString("checkin_checkout");
                    String file_delete = jsonObject.getString("file_delete");
                    String file_version = jsonObject.getString("file_version");
                    String view_metadata = jsonObject.getString("view_metadata");
                    // String file_view = jsonObject.getString("file_view");

                    String pdf_file = jsonObject.getString("pdf_file");
                    String image_file = jsonObject.getString("image_file");

                    if (pdf_file.equalsIgnoreCase("1")) {

                        pdfView = "1";
                        Log.e("ok", "ok");

                    } else {

                        pdfView = "0";
                        Log.e("ok", "ok 0");

                    }


                    if (image_file.equalsIgnoreCase("1")) {

                        imageView = "1";
                        Log.e("ok", "ok");

                    } else {

                        imageView = "0";
                        Log.e("ok", "ok 0");

                    }


                    if (share_file.equalsIgnoreCase("1")) {

                        shareFile = "1";

                    } else {

                        shareFile = "0";

                    }

                    if (view_metadata.equalsIgnoreCase("1")) {

                        viewMetadata = "1";

                    } else {

                        viewMetadata = "0";

                    }


                    if (file_version.equalsIgnoreCase("1")) {

                        fileVersion = "1";

                    } else {

                        fileVersion = "0";

                    }

                    if (file_delete.equalsIgnoreCase("1")) {

                        fileDelete = "1";

                    } else {

                        fileDelete = "0";

                    }

                    if (checkin_checkout.equalsIgnoreCase("1")) {

                        checkinCheckout = "1";

                    } else {

                        checkinCheckout = "0";

                    }
                    if (move_file.equalsIgnoreCase("1")) {


                        movefile = "1";
                    } else {


                        movefile = "0";
                    }
                    if (copy_file.equalsIgnoreCase("1")) {

                        copyfile = "1";

                    } else {

                        copyfile = "0";
                    }


                    multiMetaSearch(metadata, slid);
                    // multiMetaSearch("{\"multiMetaSearch\":[{\"metadata\":\"File_Number\",\"cond\":\"Equal\",\"searchText\":\"1003\"}]}", slid);
                    Log.e("meta in user", metadata);


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

        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);


    }

    void meta(String metadata) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_MULTI_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response meta", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("metadata", metadata);
                params.put("slid", "113");
                params.put("userid", "1");


                JSONObject jsMeta = new JSONObject(params);
                Log.e("js metasearch", jsMeta.toString());

                return params;

            }

        };


        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);

    }

    void sendDocOnMail(String mailto, String mailbody, String subject, String mailFile, String userid, String slid) {

        pdMail.setTitle("Sending Document on mail");
        pdMail.setMessage("please wait...");
        pdMail.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_FILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("send doc on mail", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(MetaSearchFileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        normalMode();
                        alertDialog.dismiss();
                        pdMail.dismiss();

                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MetaSearchFileViewActivity.this, msg, Toast.LENGTH_SHORT).show();
                        pdMail.dismiss();

                    } else {

                        Toast.makeText(MetaSearchFileViewActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        pdMail.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pdMail.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mailto", mailto);
                params.put("subject", subject);
                params.put("mailbody", mailbody);
                params.put("mailFile", mailFile);
                params.put("userid", userid);
                params.put("slid", slid);
                params.put("projectName", "EzeeOffice");
                params.put("ip", MainActivity.ip);

                Util.printParams(params, "send on mail");

                return params;

            }
        };

        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);

    }

    void showMultiSendMailDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ACTIVITY);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_share_files_mail, null);

        TextInputEditText tieSubject = dialogView.findViewById(R.id.ad_tie_share_files_mail_enter_subject);
        TextInputEditText tieEmail = dialogView.findViewById(R.id.ad_tie_share_files_mail_enter_email_id);
        EditText etMessage = dialogView.findViewById(R.id.ad_et_share_files_mail_enter_msg);

        Button btnClose = dialogView.findViewById(R.id.ad_btn_share_files_mail_close);
        Button btnSend = dialogView.findViewById(R.id.ad_btn_share_files_mail_send);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String subj = tieSubject.getText().toString();
                String mailid = tieEmail.getText().toString();
                String msg = etMessage.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Pattern pattern = Pattern.compile(emailPattern);


                if (mailid.length() == 0 || TextUtils.isEmpty(mailid)) {

                    Toast.makeText(ACTIVITY, "Enter email id", Toast.LENGTH_SHORT).show();
                } /*else if (!pattern.matcher(mailid).matches()) {

                    Toast.makeText(FileViewActivity.this, "Enter valid email id", Toast.LENGTH_SHORT).show();

                }*/ else if (subj.length() == 0 || TextUtils.isEmpty(subj)) {

                    Toast.makeText(ACTIVITY, "Enter subject", Toast.LENGTH_SHORT).show();

                } else if (msg.length() == 0 || TextUtils.isEmpty(msg)) {

                    Toast.makeText(ACTIVITY, "Enter Message", Toast.LENGTH_SHORT).show();

                } else {


                    String list = Arrays.toString(docidList.toArray());
                    String docids = list.substring(list.indexOf("[") + 1, list.lastIndexOf("]"));
                    sendDocOnMail(mailid, msg, subj, docids, MainActivity.userid, DmsActivity.dynamicFileSlid);

                }


            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_metafileview, menu);
        return true;
    }

    public void showBottomSheetSaveSearchDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ACTIVITY);
        LayoutInflater inflater = (LayoutInflater) ACTIVITY.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.bottomsheetdialog_save_search, null);
        EditText et = dialogview.findViewById(R.id.et_bottomsheet_metafileview_enter_query);
        Button btnYes = dialogview.findViewById(R.id.btn_bottomsheet_metafileview_yes);
        Button btnNo = dialogview.findViewById(R.id.btn_bottomsheet_metafileview_no);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtQuery = et.getText().toString();

                if (txtQuery.length() == 0) {

                    et.setError("Enter query");

                } else {

                    et.setError(null);

                    try {
                        JSONObject jsonObject = new JSONObject(metadata);
                        JSONArray jsonArray = jsonObject.getJSONArray("multiMetaSearch");

                        StringBuilder url = new StringBuilder();
                        StringBuilder md = new StringBuilder();
                        StringBuilder cd = new StringBuilder();
                        StringBuilder txt = new StringBuilder();

                        url.append("metasearch?id=" + slid);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String metadata = jsonArray.getJSONObject(i).getString("metadata");
                            String cond = jsonArray.getJSONObject(i).getString("cond");
                            String searchtxt = jsonArray.getJSONObject(i).getString("searchText");

                            url.append("&metadata%5B%5D=" + metadata + "&cond%5B%5D=" + cond + "&searchText%5B%5D=" + searchtxt);
                            md.append(metadata + ",");
                            cd.append(cond + ",");
                            txt.append(searchtxt + ",");


                        }


                        /* String urlEncoded = URLEncoder.encode(url.toString(), "utf-8");*/
                        //.substring(1, url.toString().length());
                        String dynaURL = url.toString().replace(" ", "+");
                        String metaList = md.toString();
                        if (metaList.endsWith(",")) {
                            metaList = metaList.substring(0, metaList.length() - 1);
                        }

                        Log.e("metalist", metaList);

                        String condList = cd.toString();
                        if (condList.endsWith(",")) {
                            condList = condList.substring(0, condList.length() - 1);
                        }

                        Log.e("condList", condList);

                        String txtList = txt.toString();
                        if (txtList.endsWith(",")) {
                            txtList = txtList.substring(0, txtList.length() - 1);
                        }

                        Log.e("txtList", txtList);
                        Log.e("Url made", dynaURL);


                        saveSearch(dynaURL, metaList, condList, txtList, et.getText().toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        dialogBuilder.setView(dialogview);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }


    void saveSearch(String url, String metadata, String condition, String searchtext, String queryname) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_MULTI_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("save_search_res",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(ACTIVITY, msg, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(ACTIVITY, msg, Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(ACTIVITY, "server error", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(ACTIVITY, "server error", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("metadata_ss", metadata);
                params.put("url_ss", url);
                params.put("cond_ss", condition);
                params.put("txt_ss", searchtext);
                params.put("queryText_ss", queryname);
                params.put("userid_ss",MainActivity.userid);
                params.put("slid_ss",slid);

                Util.printParams(params,"save_search_res_params");

                return params;
            }
        };

        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);

    }

    String setRoles(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("move_file").append(",")
                .append("copy_file").append(",")
                .append("share_file").append(",")
                .append("checkin_checkout").append(",")
                .append("file_delete").append(",")
                .append("file_version").append(",")
                .append("view_metadata").append(",")
                .append("pdf_file").append(",")
                .append("image_file");

         return stringBuilder.toString();
    }

    private void checkOut(final String docid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECKIN_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // [{"message":"Check Out successful","error":"false"}]

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String message = jsonArray.getJSONObject(0).getString("message");
                    String error = jsonArray.getJSONObject(0).getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        /*    getFileListData(slidDynamic, "1");
                         */


                        getUserRole(MainActivity.userid);


                        Toast.makeText(MetaSearchFileViewActivity.this, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MetaSearchFileViewActivity.this, message, Toast.LENGTH_SHORT).show();

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
                params.put("checkout", docid);
                return params;
            }
        };

        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);

    }

    void checkIn(final String docid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECKIN_CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response checkin", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    String error = jsonArray.getJSONObject(0).getString("error");
                    String msg = jsonArray.getJSONObject(0).getString("message");

                    if (error.equalsIgnoreCase("false")) {


                        Intent intent = new Intent(MetaSearchFileViewActivity.this, CheckInActivity.class);
                        intent.putExtra("docid", docid);
                        intent.putExtra("slid", slid);
                        intent.putExtra("fname", foldername);
                        intent.putExtra("mode", "metadatasearchActivity");
                        startActivityForResult(intent,007);

                        multiMetaSearch(metadata,slid);


                        // checkIn(docid);

                    } else if (error.equalsIgnoreCase("true")) {


                        Toast.makeText(MetaSearchFileViewActivity.this, msg, Toast.LENGTH_SHORT).show();


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
                params.put("checkin", docid);


                return params;
            }
        };


        VolleySingelton.getInstance(MetaSearchFileViewActivity.this).addToRequestQueue(stringRequest);


    }


}
