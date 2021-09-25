package in.cbslgroup.ezeeoffice.Activity.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import in.cbslgroup.ezeeoffice.Activity.Viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeeoffice.Adapters.MetaDynamicFormAdapter;
import in.cbslgroup.ezeeoffice.Adapters.SpinnerDropDownAdapter;
import in.cbslgroup.ezeeoffice.Interface.OnDropDownListClickListener;
import in.cbslgroup.ezeeoffice.Model.SpinnerDropDown;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class MetaDataSearchActivity extends AppCompatActivity {

    TextView tvRequiredFiledLabel, tvStoragenamelabel, tvSelectmetadataLabel, tvSelectCondition, tvEditTextLabel;

    Spinner spMetaData, spCondition;

    EditText etEnterText;

    List<String> spinnerConditionlist = new ArrayList<>();
    List<String> spinnerMetadatalist = new ArrayList<>();


    SessionManager session;

    String session_userid, slid_Session;

    List<SpinnerDropDown> storageList = new ArrayList<>();

    RecyclerView recyclerView, rvForm;
    AlertDialog alertDialog;
    TextView tvStoragenameSelected, tvSlid;
    ImageView ivArrowDownStoragename;
    Button btnsearch;
    public static String dynamicSlid;

    ArrayList<String> metalist = new ArrayList<>();
    ArrayList<String> conditionlist = new ArrayList<>();
    ArrayList<String> formList = new ArrayList<>();
    ArrayList<String> textlist = new ArrayList<>();

    ArrayAdapter<String> metaAdapter;

    ProgressBar progressBar;

    TextView tvSelectMetadataHint;

    LinearLayout llSelectStorage;
    ImageView ivAddForm;

    int counterAddForm = 1;

    MetaDynamicFormAdapter metaDynamicFormAdapter;

    HashMap<String, String> storageMap = new HashMap<>();
    JSONArray jsonArray;
    int count = 1;

    private RecyclerViewReadyCallback recyclerViewReadyCallback;

    //counting the lines of the given string
    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_data_search);


        Toolbar toolbar = findViewById(R.id.toolbar_metadata_search);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // userid
        session_userid = user.get(SessionManager.KEY_USERID);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
        slid_Session = sharedPreferences.getString("userSlid", null);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        tvRequiredFiledLabel = findViewById(R.id.tv_metadatasearch_requiredfields);
        tvStoragenamelabel = findViewById(R.id.tv_metadatasearch_storagename);
        tvSelectCondition = findViewById(R.id.tv_meta_search_selectcondition);
        tvSelectmetadataLabel = findViewById(R.id.tv_meta_search_selectmetadata);
        tvEditTextLabel = findViewById(R.id.tv_meta_search_enterText);
        //spStorageName = findViewById(R.id.spinner_metadatasearch_storagename);
        spCondition = findViewById(R.id.spinner_metadatasearch_select_condition);
        spMetaData = findViewById(R.id.spinner_metadatasearch_select_metadata);

        // Select metadata components
        etEnterText = findViewById(R.id.et_metadatasearch_enter_text);
        progressBar = findViewById(R.id.progressbar_metasearch_selectMetadata);
        tvSelectMetadataHint = findViewById(R.id.tv_meta_search_selectmetadata_hint);
        llSelectStorage = findViewById(R.id.ll_metasearch_select_storage);
        ivAddForm = findViewById(R.id.iv_metasearch_add_form);
        tvStoragenameSelected = findViewById(R.id.tv_spinner_dropdown_setStoragename);
        ivArrowDownStoragename = findViewById(R.id.iv_spinner_dropdown_setStoragename_arrow);
        btnsearch = findViewById(R.id.btn_metadatasearch);
        tvSlid = findViewById(R.id.tv_spinner_dropdown_setSlid);


        rvForm = findViewById(R.id.rv_metadatasearch_form);
        // rvForm.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // linearLayoutManager.setAutoMeasureEnabled(true);
        rvForm.setLayoutManager(linearLayoutManager);
        metaDynamicFormAdapter = new MetaDynamicFormAdapter(MetaDataSearchActivity.this, spinnerConditionlist, spinnerMetadatalist, formList);
        rvForm.setAdapter(metaDynamicFormAdapter);


        //rvForm.setNestedScrollingEnabled(false);
        //ViewCompat.setNestedScrollingEnabled(rvForm, false);


        ivAddForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String storageSpItemselected = tvStoragenameSelected.getText().toString();
                Log.e("storage_selected", storageSpItemselected);
                Log.e("counterAddform", String.valueOf(counterAddForm));
                Log.e("sp metalist size", String.valueOf(spinnerMetadatalist.size() - 1));

                if (tvStoragenameSelected.getText().toString().trim().equals("") || tvStoragenameSelected.getText().toString().isEmpty() || tvStoragenameSelected.getText().toString().equalsIgnoreCase("Select Storage")) {

                    Toast.makeText(MetaDataSearchActivity.this, "Please Select Storage", Toast.LENGTH_SHORT).show();

                } else if (spinnerMetadatalist.size() - 1 == counterAddForm) {

                    Toast.makeText(MetaDataSearchActivity.this, "No more metadata found", Toast.LENGTH_SHORT).show();

                } else {

                    formList.add(String.valueOf(counterAddForm));
                    metaDynamicFormAdapter.notifyItemInserted(counterAddForm);
                    counterAddForm++;
                    Log.e("add form", String.valueOf(counterAddForm));


                }


            }


        });

        metaDynamicFormAdapter.setOnButtonClickListener(new MetaDynamicFormAdapter.OnButtonClickListener() {
            @Override
            public void onRemoveButtonClick(int pos) {

                counterAddForm--;
                formList.remove(pos);
                metaDynamicFormAdapter.notifyItemRemoved(pos);

            }
        });


        llSelectStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaDataSearchActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_spinner_dropdown, null);

                recyclerView = dialogView.findViewById(R.id.rv_spinner_dropdown);
                recyclerView.setLayoutManager(new LinearLayoutManager(MetaDataSearchActivity.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(storageList.size());

                recyclerView.setAdapter(new SpinnerDropDownAdapter(storageList, MetaDataSearchActivity.this, new OnDropDownListClickListener() {
                    @Override
                    public void onClickItem(View v, int position, String storagename, String slid) {


                        tvStoragenameSelected.setText(storagename);

                        getMetadata(slid, "null");
                        dynamicSlid = slid;
                        alertDialog.dismiss();


                    }
                }));


                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();


            }
        });

        etEnterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etEnterText.setCursorVisible(true);


            }
        });

        tvStoragenameSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MetaDataSearchActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_spinner_dropdown, null);

                recyclerView = dialogView.findViewById(R.id.rv_spinner_dropdown);
                recyclerView.setLayoutManager(new LinearLayoutManager(MetaDataSearchActivity.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(storageList.size());

                recyclerView.setAdapter(new SpinnerDropDownAdapter(storageList, MetaDataSearchActivity.this, new OnDropDownListClickListener() {
                    @Override
                    public void onClickItem(View v, int position, String storagename, String slid) {


                        tvStoragenameSelected.setText(storagename);

                        getMetadata(slid, "null");

                        dynamicSlid = slid;

                        alertDialog.dismiss();


                    }
                }));


                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();


            }
        });


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metalist.clear();
                conditionlist.clear();
                textlist.clear();




              /*  HashMap<Integer, String> textMap = metaDynamicFormAdapter.getTextMap();

                for (int i = 0; i < textMap.size(); i++) {

                    String text = textMap.get(i);
                    Log.e("text", text);
                }*/


                if (tvStoragenameSelected.getText().toString().trim().equals("") || tvStoragenameSelected.getText().toString().isEmpty() || tvStoragenameSelected.getText().toString().equalsIgnoreCase("Select Storage")) {

                    Toast.makeText(MetaDataSearchActivity.this, "Please Select Storage", Toast.LENGTH_SHORT).show();
                } else if (spMetaData.getSelectedItem() == null) {

                    Toast.makeText(MetaDataSearchActivity.this, "Please Select Metadata", Toast.LENGTH_SHORT).show();

                } else if (spMetaData.getSelectedItem().toString().equalsIgnoreCase("Select Metadata")) {

                    Toast.makeText(MetaDataSearchActivity.this, "Please Select Metadata", Toast.LENGTH_SHORT).show();
                } else if (spCondition.getSelectedItem() == null) {
                    Toast.makeText(MetaDataSearchActivity.this, "Please Select Condition", Toast.LENGTH_SHORT).show();

                } else if (spCondition.getSelectedItem().toString().equalsIgnoreCase("Select Condition")) {

                    Toast.makeText(MetaDataSearchActivity.this, "Please Select Condition", Toast.LENGTH_SHORT).show();

                } else if (etEnterText.getText().toString().equalsIgnoreCase("") || etEnterText.getText().toString().isEmpty()) {

                    Toast.makeText(MetaDataSearchActivity.this, "Enter text for search", Toast.LENGTH_SHORT).show();
                } else if (checkFormValidation().equalsIgnoreCase("no")) {

                    Log.e("formvalue", "missing");

                } else {


                    metalist.add(spMetaData.getSelectedItem().toString());
                    conditionlist.add(spCondition.getSelectedItem().toString());
                    textlist.add(etEnterText.getText().toString());

                    for (int j = 0; j < metaDynamicFormAdapter.getItemCount(); j++) {

                        Log.e("loop val", String.valueOf(j));

                        View itemView = rvForm.getLayoutManager().findViewByPosition(j);
                        TextView textView = itemView.findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);
                        Spinner spCondition = itemView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);
                        Spinner spMetadata = itemView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);

                        String text = textView.getText().toString();
                        String spCondItem = spCondition.getSelectedItem().toString();
                        String spMetaItem = spMetadata.getSelectedItem().toString();


                        metalist.add(spMetaItem);
                        conditionlist.add(spCondItem);
                        textlist.add(text);


                    }


                    // Toast.makeText(MetaDataSearchActivity.this, "Ok", Toast.LENGTH_SHORT).show();

                    try {


                        JSONObject jsonObject = makeJsonObject(metalist, conditionlist, textlist, metalist.size());
                        Log.e("JsonObject: ", jsonObject.toString());

                        Intent intent = new Intent(MetaDataSearchActivity.this, MetaSearchFileViewActivity.class);

                        Intent in = getIntent();
                        if (in.getStringExtra("queryname") != null) {

                            String slid = tvSlid.getText().toString();
                            intent.putExtra("slid", slid);

                        } else {
                            intent.putExtra("slid", dynamicSlid);

                        }


                        intent.putExtra("metadata", String.valueOf(jsonObject));
                        intent.putExtra("foldername", tvStoragenameSelected.getText().toString());
                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        // spinnerMetadatalist.add("Select Metadata");

        spinnerConditionlist.add("Equal");
        spinnerConditionlist.add("Contains");
        spinnerConditionlist.add("Like");
        spinnerConditionlist.add("Not Like");
        spinnerConditionlist.add("Select Condition");


        String requiredText = "REQUIRED FIELDS ARE MARKED WITH A (<font color=\"red\">*</font>) ";
        tvRequiredFiledLabel.setText(Html.fromHtml(requiredText), TextView.BufferType.SPANNABLE);

        String storageText = "Storage Name (<font color =\"red\">*</font>)";
        tvStoragenamelabel.setText(Html.fromHtml(storageText), TextView.BufferType.SPANNABLE);

        String metatext = "Select Metadata (<font color =\"red\">*</font>)";
        tvSelectmetadataLabel.setText(Html.fromHtml(metatext), TextView.BufferType.SPANNABLE);

        String conditiontext = "Select Condition (<font color =\"red\">*</font>)";
        tvSelectCondition.setText(Html.fromHtml(conditiontext), TextView.BufferType.SPANNABLE);

        String enterText = "Enter Text (<font color =\"red\">*</font>)";
        tvEditTextLabel.setText(Html.fromHtml(enterText), TextView.BufferType.SPANNABLE);

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }


        };
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCondition.setAdapter(conditionAdapter);
        spCondition.setSelection(conditionAdapter.getCount());


        metaAdapter = new ArrayAdapter<String>(
                MetaDataSearchActivity.this, android.R.layout.simple_spinner_item, spinnerMetadatalist) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }

        };

        getList(session_userid);


    }

    private void getList(final String userid) {

        storageList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.STORAGE_LIST_ALLOTED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("storagelist response", response);
                Log.e("Count lines :", String.valueOf(countLines(response)));

                for (int i = 0; i < countLines(response); i++) {

                    String n = response.split("\n")[i];
                    String storagename = n.substring(0, n.lastIndexOf("&&"));
                    String slid = n.substring(n.indexOf("&&") + 2);

                    Log.e("storagename: ", storagename);
                    Log.e("slid ", slid);


                    storageList.add(new SpinnerDropDown(storagename, slid));
                    storageMap.put(slid, storagename);

                    //System.out.println(n);
                }


                Intent intent = getIntent();
                if (intent.getStringExtra("queryname") != null) {

                    String queryname = intent.getStringExtra("queryname");
                    getFreqQueryData("getFreqQuery", queryname);


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameter = new HashMap<String, String>();

                parameter.put("userid", userid);


                return parameter;
            }
        };

        VolleySingelton.getInstance(MetaDataSearchActivity.this).addToRequestQueue(stringRequest);
        //VolleySingelton.getInstance(MetaDataSearchActivity.this).getRequestQueue().getCache().remove(ApiUrl.STORAGE_URL);

    }

    //getting the list of metadata in dropdown of each indiviual folder dynamically
    private void getMetadata(final String slid, String responseFreq) {

        tvSelectMetadataHint.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        spMetaData.setVisibility(View.GONE);
        spinnerMetadatalist.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_METADATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("metadata", response);

                try {
                    JSONArray jsonArr = new JSONArray(response);

                    if (jsonArr.length() == 0) {

                        tvSelectMetadataHint.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        spMetaData.setVisibility(View.GONE);


                        spinnerMetadatalist.clear();
                        metaAdapter.notifyDataSetChanged();

                        etEnterText.setText("");
                        Toast.makeText(MetaDataSearchActivity.this, "No Metadata Found", Toast.LENGTH_SHORT).show();


                    } else {

                        spinnerMetadatalist.clear();
                        metaAdapter.notifyDataSetChanged();

                        for (int i = 0; i < jsonArr.length(); i++) {

                            String metadata = jsonArr.getString(i);
                            spinnerMetadatalist.add(metadata);


                        }

                        spinnerMetadatalist.add("FileName");
                        spinnerMetadatalist.add("No Of Pages");
                        spinnerMetadatalist.add("Select Metadata");
                        metaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_metadata);
                        spMetaData.setAdapter(metaAdapter);
                        spMetaData.setSelection(metaAdapter.getCount());

                        tvSelectMetadataHint.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        spMetaData.setVisibility(View.VISIBLE);


                    }

                    if (!response.equalsIgnoreCase("null"))

                    {

                        JSONObject jsonObject = new JSONObject(responseFreq);

                        String error = jsonObject.getString("error");

                        if (error.equalsIgnoreCase("false")) {

                            jsonArray = jsonObject.getJSONArray("freqQueryList");

                            String queryTextStatic = jsonArray.getJSONObject(0).getString("query");
                            String queryConditionStatic = jsonArray.getJSONObject(0).getString("condition");
                            String queryMetadataStatic = jsonArray.getJSONObject(0).getString("metadata");

                            int conditionSelectedItemPos = getConditionListItemPos(queryConditionStatic);
                            int metaSelectedItemPos = getMetadataListItemPos(queryMetadataStatic);


                            ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(
                                    MetaDataSearchActivity.this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
                                @Override
                                public int getCount() {
                                    // don't display last item. It is used as hint.
                                    int count = super.getCount();
                                    return count > 0 ? count - 1 : count;
                                }


                            };
                            conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spCondition.setAdapter(conditionAdapter);
                            spCondition.setSelection(conditionAdapter.getCount());


                            metaAdapter = new ArrayAdapter<String>(
                                    MetaDataSearchActivity.this, android.R.layout.simple_spinner_item, spinnerMetadatalist) {
                                @Override
                                public int getCount() {
                                    // don't display last item. It is used as hint.
                                    int count = super.getCount();
                                    return count > 0 ? count - 1 : count;
                                }

                            };

                            spMetaData.setAdapter(metaAdapter);
                            spMetaData.setSelection(metaAdapter.getCount());


                            //((TextView) spCondition.getSelectedView()).setError("error");
                            // ((TextView) spMetaData.getSelectedView()).setText(queryMetadataStatic);


                            spMetaData.setSelection(metaSelectedItemPos);
                            spCondition.setSelection(conditionSelectedItemPos);

                            etEnterText.setText(queryTextStatic);

                            for (int j = 1; j < jsonArray.length(); j++) {

                                formList.add(String.valueOf(counterAddForm));
                                metaDynamicFormAdapter.notifyItemInserted(counterAddForm);
                                counterAddForm++;

                            }

                            rvForm.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    if (recyclerViewReadyCallback != null) {
                                        recyclerViewReadyCallback.onLayoutReady();
                                    }
                                    rvForm.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });

                            recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
                                @Override
                                public void onLayoutReady() {


                                    //Toast.makeText(MetaDataSearchActivity.this, "ok", Toast.LENGTH_SHORT).show();


                                    for (int i = 1; i < jsonArray.length(); i++) {

                                        try {

                                            Log.e("yes", "working");


                                            String queryText = jsonArray.getJSONObject(i).getString("query");
                                            String queryCondition = jsonArray.getJSONObject(i).getString("condition");
                                            String queryMetadata = jsonArray.getJSONObject(i).getString("metadata");

                                            int conditionSelectedItemPos = getConditionListItemPos(queryCondition);
                                            int metaSelectedItemPos = getMetadataListItemPos(queryMetadata);

                                            Log.e("queryText", queryText);
                                            Log.e("queryCondition", queryCondition + "pos : " + conditionSelectedItemPos);
                                            Log.e("queryMetadata", queryMetadata + "pos : " + metaSelectedItemPos);

                                            Log.e("childcount", String.valueOf(rvForm.getLayoutManager().getChildCount()));

                                            View itemView = rvForm.getLayoutManager().findViewByPosition(i - 1);
                                            EditText editText = itemView.findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);
                                            Spinner spCondition = itemView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);
                                            Spinner spMetadata = itemView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);


                                            ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(
                                                    MetaDataSearchActivity.this, android.R.layout.simple_spinner_item, spinnerConditionlist) {
                                                @Override
                                                public int getCount() {
                                                    // don't display last item. It is used as hint.
                                                    int count = super.getCount();
                                                    return count > 0 ? count - 1 : count;
                                                }

                                            };
                                            conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spCondition.setAdapter(conditionAdapter);
                                            spCondition.setSelection(conditionAdapter.getCount());


                                            ArrayAdapter<String> metaAdapter = new ArrayAdapter<String>(
                                                    MetaDataSearchActivity.this, android.R.layout.simple_spinner_item, spinnerMetadatalist) {
                                                @Override
                                                public int getCount() {
                                                    // don't display last item. It is used as hint.
                                                    int count = super.getCount();
                                                    return count > 0 ? count - 1 : count;
                                                }

                                            };

                                            metaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_metadata);
                                            spMetadata.setAdapter(metaAdapter);
                                            spMetadata.setSelection(metaAdapter.getCount());


                                            spCondition.setSelection(conditionSelectedItemPos, false);
                                            spMetadata.setSelection(metaSelectedItemPos, false);


                                            // ((TextView) spCondition.getSelectedView()).setText(queryCondition);
                                            //((TextView) spMetadata.getSelectedView()).setText(queryMetadata);
                                            editText.setText(queryText);
                                       /* View itemView = rvForm.findViewHolderForAdapterPosition(adapterPosition);


                                        TextView textView = itemView.findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);
                                        Spinner spCondition = itemView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
                                        Spinner spMetadata = itemView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);

                                        ((TextView) spCondition.getSelectedView()).setText(queryCondition);
                                        ((TextView) spMetadata.getSelectedView()).setText(queryMetadata);
                                        textView.setText(queryText);



                                       /* new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {




                                            }
                                        }, 500);
*/


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("error", "yes");
                                        }


                           /* String text = textView.getText().toString();
                            String spCondItem = spCondition.getSelectedItem().toString();
                            String spMetaItem = spMetadata.getSelectedItem().toString();


                            metalist.add(spMetaItem);
                            conditionlist.add(spCondItem);
                            textlist.add(text);
*/


                                    }


                                }
                            };


                        } else if (error.equalsIgnoreCase("true")) {


                        } else {

                            Toast.makeText(MetaDataSearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // getMetadata(slid,"null");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("slid", slid);


                return params;
            }
        };

        VolleySingelton.getInstance(MetaDataSearchActivity.this).addToRequestQueue(stringRequest);
    }

    private JSONObject makeJsonObject(ArrayList<String> metaData, ArrayList<String> condition, ArrayList<String> text,
                                      int formcount)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();

        for (int i = 0; i < formcount; i++) {
            obj = new JSONObject();
            try {
                obj.put("metadata", metaData.get(i));
                obj.put("cond", condition.get(i));
                obj.put("searchText", text.get(i));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("multiMetaSearch", jsonArray1);
        return finalobject;
    }

    String checkFormValidation() {

        Log.e("adapter getitemcount", String.valueOf(metaDynamicFormAdapter.getItemCount()));


        if (metaDynamicFormAdapter.getItemCount() > 0) {

            for (int j = 0; j < metaDynamicFormAdapter.getItemCount(); j++) {

                Log.e("loop val", String.valueOf(j));

                View itemView = rvForm.getLayoutManager().findViewByPosition(j);
                TextView textView = itemView.findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);
                Spinner spCondition = itemView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
                Spinner spMetadata = itemView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);

                if (textView.length() == 0) {

                    textView.setError("Please enter text");
                    Toast.makeText(MetaDataSearchActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();

                    return "no";

                }
                if (spCondition.getSelectedItem().toString().equalsIgnoreCase("Select condition")) {

                    ((TextView) spCondition.getSelectedView()).setError("Please select condition");
                    Toast.makeText(MetaDataSearchActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();

                    return "no";

                }
                if (spMetadata.getSelectedItem().toString().equalsIgnoreCase("Select metadata")) {

                    ((TextView) spMetadata.getSelectedView()).setError("Please select metadata");
                    Toast.makeText(MetaDataSearchActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();

                    return "no";

                }

            }

        } else {

            return "yes";

        }

        return "yes";


    }

    void getFreqQueryData(String action, String quername) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.FREQ_QUERY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getFreqQueryData", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String slid = jsonObject.getString("slid");
                    String storagename = getStorageName(slid);

                    if (storagename.length() != 0 && storagename !=null) {

                        tvStoragenameSelected.setText(storagename);
                        tvSlid.setText(slid);
                        getMetadata(slid, response);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MetaDataSearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("action", action);
                params.put("queryname", quername);
                return params;
            }
        };


        VolleySingelton.getInstance(MetaDataSearchActivity.this).addToRequestQueue(stringRequest);

    }

    //getStorage from slid
    String getStorageName(String slid) {

        String name = storageMap.get(slid);
        return name;

    }

    int getConditionListItemPos(String condition) {

        Log.e("condition_get", condition);
        int pos = 0;

        if (spinnerConditionlist.contains(condition)) {

            pos = spinnerConditionlist.indexOf(condition);
            Log.e("condition_get", "ok" + pos);


        }

        return pos;
    }

    //get conditionlist item pos

    int getMetadataListItemPos(String metadata) {

        int pos = 0;

        if (spinnerMetadatalist.contains(metadata)) {

            pos = spinnerMetadatalist.indexOf(metadata);

        }

        return pos;

    }

    public interface RecyclerViewReadyCallback {
        void onLayoutReady();
    }


}
