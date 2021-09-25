package in.cbslgroup.ezeeoffice.Activity.SharedFiles;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.SharedWithMeAdapter;
import in.cbslgroup.ezeeoffice.Model.SharedWithMe;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.ConnectivityReceiver;
import in.cbslgroup.ezeeoffice.Utils.Initializer;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class SharedWithMeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    RecyclerView rvSharedWithMe;
    SharedWithMeAdapter sharedWithMeAdapter;
    List<SharedWithMe> sharedWithMeList = new ArrayList<>();
    ProgressBar progressBar;
    LinearLayout llnofilefound,llnointernetfound;
    String session_userid;
    Toolbar toolbar;
    TextView tvResultsFound;

    /////////////////////////////////////
    String docname,docsize, docpath,pagecount,docExtn,storagename,fileSizeInMb;
    Double fileSizeDouble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_with_me);

        toolbar = findViewById(R.id.toolbar_Shared_with_me_activity);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        llnofilefound = findViewById(R.id.ll_sharedwithmeactvity_nofilefound);
        llnointernetfound = findViewById(R.id.ll_sharedwithmeactivity_nointernetfound);

        rvSharedWithMe = findViewById(R.id.rv_shared_with_me);
        rvSharedWithMe.setHasFixedSize(true);
        rvSharedWithMe.setItemViewCacheSize(sharedWithMeList.size());
        rvSharedWithMe.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBarSharedwithme);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        tvResultsFound = findViewById(R.id.tv_shared_with_me_resultsfound);

        session_userid = MainActivity.userid;

        Log.e("session userid shared",session_userid );


        getShareWithMeList(session_userid);

       /* Collections.sort(myList, new Comparator<MyObject>() {
            public int compare(MyObject o1, MyObject o2) {
                if (o1.getDateTime() == null || o2.getDateTime() == null)
                    return 0;
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });*/


    }


    void getShareWithMeList(final String userid) {

        rvSharedWithMe.setVisibility(View.GONE);
        llnofilefound.setVisibility(View.GONE);
        tvResultsFound.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("sharedwithme", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);



                    if(jsonArray.length()==0){

                        tvResultsFound.setText("0 results found");
                        tvResultsFound.setVisibility(View.GONE);
                        rvSharedWithMe.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llnofilefound.setVisibility(View.VISIBLE);


                    }

                    else{


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String error = jsonArray.getJSONObject(i).getString("error");
                            Log.e("error", error);
                            if (error.equals("false")) {

                                docname = jsonArray.getJSONObject(i).getString("docname");
                                // String doctype = jsonArray.getJSONObject(0).getString("doctype");
                                docsize = jsonArray.getJSONObject(i).getString("docsize");
                                docpath = jsonArray.getJSONObject(i).getString("docpath");
                                pagecount = jsonArray.getJSONObject(i).getString("pagecount");
                                String fromUsername = jsonArray.getJSONObject(i).getString("fromUsername");
                                String shareddate = jsonArray.getJSONObject(i).getString("shareddate");
                                String docid = jsonArray.getJSONObject(i).getString("docid");
                                docExtn = jsonArray.getJSONObject(i).getString("docExtn");

                                if(docname.equalsIgnoreCase("null")){

                                    docname = "null";

                                }

                                if(docsize.equalsIgnoreCase("null")){

                                    docsize = "null";

                                }

                                else{

                                    fileSizeDouble = Double.parseDouble(docsize) / 1048576;
                                    Log.e("File size ", String.valueOf(fileSizeDouble));

                                    fileSizeInMb = String.valueOf(Math.round(fileSizeDouble * 100D) / 100D);
                                    Log.e("File size in mb ", fileSizeInMb);



                                }

                                if(docpath.equalsIgnoreCase("null")){

                                    docpath = "null";

                                }
                                if(pagecount.equalsIgnoreCase("null")){

                                    pagecount = "null";

                                }
                                if(docExtn.equalsIgnoreCase("null")){

                                    docExtn = "null";

                                }


                                Log.e("docpath", docpath);

                                storagename = jsonArray.getJSONObject(i).getString("storagename");

                                if(storagename.equalsIgnoreCase("null")){

                                    storagename ="null";

                                }

                                Log.e("storage",storagename);


                                sharedWithMeList.add(new SharedWithMe(docname, fileSizeInMb + "mb", pagecount, fromUsername, shareddate, storagename, docpath, docExtn,docid));

                            }


                        }

                        sharedWithMeAdapter = new SharedWithMeAdapter(sharedWithMeList, SharedWithMeActivity.this);
                        rvSharedWithMe.setAdapter(sharedWithMeAdapter);

                        tvResultsFound.setText(jsonArray.length()+" results found");
                        tvResultsFound.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);
                        rvSharedWithMe.setVisibility(View.VISIBLE);
                        llnofilefound.setVisibility(View.GONE);

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
                params.put("SWMuserid", userid);


                return params;
            }
        };


        VolleySingelton.getInstance(SharedWithMeActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showNoInternetLayout(isConnected);

    }

    void showNoInternetLayout(Boolean isconnected){


        if(!isconnected){

//         llnointernetfound.setVisibility(View.VISIBLE);
//          rvSharedWithMe.setVisibility(View.GONE);
//          progressBar.setVisibility(View.GONE);
//          llnofilefound.setVisibility(View.GONE);


        }

        else{

//            llnointernetfound.setVisibility(View.GONE);
//            rvSharedWithMe.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//            llnofilefound.setVisibility(View.GONE);


        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        // register connection status listener
        Initializer.getInstance().setConnectivityListener(this);
    }
}
