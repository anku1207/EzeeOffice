package in.cbslgroup.ezeeoffice.Activity.Dms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.AssignMetadataSelectAdapter;
import in.cbslgroup.ezeeoffice.Chip.TokenFilterAdapter;
import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.Chip.UsernameCompletionView;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class AssignMetadataActivity extends AppCompatActivity {


    UsernameCompletionView metaDataCompletionView;
    List<User> metadatalist = new ArrayList<>();

    List<String> metaUpdateList = new ArrayList<>();

    TokenFilterAdapter filterAdapter;
    TextView tvfoldername;

    LinearLayout llroot, llbuttons;
    ProgressBar progressBar;
    Toolbar toolbar;
    Button btnCancel, btnSave;
    Handler h;

    List<User> assignedMetaList = new ArrayList<>();//right
    List<User> selectMetaList = new ArrayList<>();//left

    AssignMetadataSelectAdapter assignSelectMetadataSelectAdapter, assignAssignedMetadataSelectAdapter;
    RecyclerView rvSelectMeta;
    RecyclerView rvAssignedMeta;


    // ChipGroup mChipGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_metadata);

        toolbar = findViewById(R.id.toolbar_Assignmetaadata);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        Intent intent = getIntent();
        String slid = intent.getStringExtra("fSlid");

        Log.e("fslid", slid);


        llroot = findViewById(R.id.ll_assignmetadata_root);
        llbuttons = findViewById(R.id.ll_assign_metadata_buttons);
        progressBar = findViewById(R.id.progressBar_assignmetadata);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        //mChipGroup = findViewById(R.id.assign_meta_chip_group);

        rvSelectMeta = findViewById(R.id.rv_select_meta);
        rvAssignedMeta = findViewById(R.id.rv_meta_assigned);

        rvSelectMeta.setLayoutManager(new LinearLayoutManager(this));
        rvAssignedMeta.setLayoutManager(new LinearLayoutManager(this));


        btnCancel = findViewById(R.id.btn_assignmetadata_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });

        btnSave = findViewById(R.id.btn_assignmetadata_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                List<User> tokens = metaDataCompletionView.getObjects();
//
//                //for removing unwanted text in the autocomplete text
//                metaDataCompletionView.deleteText();
//                // clearUnwantedTextInTokenAutoComplete();
//
//
//                //metaUpdateList.clear();
//                for (int i = 0; i < tokens.size(); i++) {
//                    String slid = tokens.get(i).getSlid();
//                    Log.e("metatokens", slid);
//                    metaUpdateList.add(slid);
//                }

                JSONArray jsonArray = new JSONArray();

                if (assignAssignedMetadataSelectAdapter.getMetaIdList() != null) {

                    metaUpdateList = assignAssignedMetadataSelectAdapter.getMetaIdList();


                    for (int i = 0; i < metaUpdateList.size(); i++) {

                        jsonArray.put(Integer.parseInt(metaUpdateList.get(i)));

                    }
                    Log.e("jsonarraymeta", jsonArray.toString());
                    Log.e("fjjfj", jsonArray.toString() + " " + MainActivity.userid + " " + MainActivity.username + " " + MainActivity.ip + " " + DmsActivity.dynamicFileSlid);

                }


                updateMetaData(jsonArray.toString(), MainActivity.userid, MainActivity.username, MainActivity.ip, DmsActivity.dynamicFileSlid);


            }
        });


        tvfoldername = findViewById(R.id.tv_assignmetadata_foldername);
        toolbar.setSubtitle(DmsActivity.foldernameDyanamic + ": Assign Meta-Data Fields to ");
        // tvfoldername.setText(DmsActivity.foldernameDyanamic);



        getMetaData(DmsActivity.dynamicFileSlid);


    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

    }

    void getMetaData(final String slid) {

        progressBar.setVisibility(View.VISIBLE);
        llroot.setVisibility(View.GONE);
        llbuttons.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ASSIGN_METADATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getAllMetaData", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("true")) {
                        progressBar.setVisibility(View.GONE);
                        llroot.setVisibility(View.VISIBLE);
                        llbuttons.setVisibility(View.VISIBLE);

                    } else if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArrayAssignedMeta = jsonObject.getJSONArray("assigned_metadata");
                        JSONArray jsonArraySelectedMeta = jsonObject.getJSONArray("select_metadata");


                        for (int i = 0; i < jsonArrayAssignedMeta.length(); i++) {


                            String metaname = jsonArrayAssignedMeta.getJSONObject(i).getString("field_name");
                            String metaid = jsonArrayAssignedMeta.getJSONObject(i).getString("id");
                            assignedMetaList.add(new User(metaname, metaid, R.drawable.ic_round_metadata_folder));


                        }


                        for (int j = 0; j < jsonArraySelectedMeta.length(); j++) {

                            String metaname = jsonArraySelectedMeta.getJSONObject(j).getString("field_name");
                            String metaid = jsonArraySelectedMeta.getJSONObject(j).getString("id");
                            selectMetaList.add(new User(metaname, metaid, R.drawable.ic_round_metadata_folder));


                        }


                        progressBar.setVisibility(View.GONE);
                        llroot.setVisibility(View.VISIBLE);
                        llbuttons.setVisibility(View.VISIBLE);

                    } else {

                        progressBar.setVisibility(View.GONE);
                        llroot.setVisibility(View.VISIBLE);
                        llbuttons.setVisibility(View.VISIBLE);

                    }


                    assignSelectMetadataSelectAdapter = new AssignMetadataSelectAdapter(selectMetaList, AssignMetadataActivity.this, new AssignMetadataSelectAdapter.OnItemClickListener() {
                        @Override
                        public void onButtonClick(int pos, String metaname, User obj) {


                            selectMetaList.remove(obj);
                            assignedMetaList.add(obj);

                            assignSelectMetadataSelectAdapter.notifyDataSetChanged();
                            assignAssignedMetadataSelectAdapter.notifyDataSetChanged();


                        }
                    });


                    assignAssignedMetadataSelectAdapter = new AssignMetadataSelectAdapter(assignedMetaList, AssignMetadataActivity.this, new AssignMetadataSelectAdapter.OnItemClickListener() {
                        @Override
                        public void onButtonClick(int pos, String metaname, User obj) {

                            // assignAssignedMetadataSelectAdapter.notifyItemRemoved(pos);
                            assignedMetaList.remove(obj);
                            //assignAllMetadataSelectAdapter.notifyItemInserted(pos);
                            selectMetaList.add(obj);

                            assignSelectMetadataSelectAdapter.notifyDataSetChanged();
                            assignAssignedMetadataSelectAdapter.notifyDataSetChanged();
                        }
                    });


                    //assignAllMetadataSelectAdapter.notifyDataSetChanged();


                    rvSelectMeta.setAdapter(assignSelectMetadataSelectAdapter);
                    rvAssignedMeta.setAdapter(assignAssignedMetadataSelectAdapter);


                    Log.e("folderslid", DmsActivity.dynamicFileSlid);


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
                params.put("assign_select_slid", slid);


                return params;
            }
        };


        VolleySingelton.getInstance(AssignMetadataActivity.this).addToRequestQueue(stringRequest);

    }

    void updateMetaData(final String metadatalist, final String userid, final String username, final String ip, final String folderslid) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating MetaData");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.ASSIGN_METADATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("metaupdate", response);

                // {"error":"false","message":"Metadata Update Succesfully"}

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(AssignMetadataActivity.this, message, Toast.LENGTH_SHORT).show();
                        h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //onBackPressed();

                                Intent intent = getIntent();
                                intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                                setResult(RESULT_OK, intent);

                                h.removeCallbacks(null);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                finish();

                            }
                        }, 1000);

                    } else if (error.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(AssignMetadataActivity.this, "Metadata Updated Sucessfully !!", Toast.LENGTH_SHORT).show();
                        h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                onBackPressed();
                                h.removeCallbacks(null);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            }
                        }, 1000);


                    } else {


                        progressDialog.dismiss();
                        Toast.makeText(AssignMetadataActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //updateMetaData(metadatalist,userid,username,ip,folderslid);

                progressDialog.dismiss();
                Toast.makeText(AssignMetadataActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("assignMetaList", metadatalist);
                params.put("assignUserid", userid);
                params.put("assignUsername", username);
                params.put("assignIp", ip);
                params.put("folderId", folderslid);


                return params;
            }
        };

        VolleySingelton.getInstance(AssignMetadataActivity.this).addToRequestQueue(stringRequest);

    }


}


