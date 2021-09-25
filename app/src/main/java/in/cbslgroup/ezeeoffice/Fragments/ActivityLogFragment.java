package in.cbslgroup.ezeeoffice.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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

import in.cbslgroup.ezeeoffice.Adapters.LogAdapter;
import in.cbslgroup.ezeeoffice.Model.Logs;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLogFragment extends Fragment {

    RecyclerView rv;
    List<Logs> logsList = new ArrayList<>();
    LogAdapter logAdapter;
    ProgressBar pbmain;
    TextView tvNoLogFound;

    public ActivityLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity_log, container, false);

        Bundle bundle=getArguments();
        String docid =bundle.getString("docid");

        rv = v.findViewById(R.id.rv_activity_log);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        pbmain = v.findViewById(R.id.pb_activity_log);
        tvNoLogFound = v.findViewById(R.id.tv_activity_log_no_log_found);


        getActivityLog(docid,"getAllActivityLogs");


        return v;
    }


    void getActivityLog(String docid, String action) {

        pbmain.setVisibility(View.VISIBLE);
        tvNoLogFound.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.VIEWER_LOGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Log.e("actvitity_log",response);

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if(error.equalsIgnoreCase("false"))
                    {

                        Log.e("ok","ok1");

                        JSONArray jsonArray = jsonObject.getJSONArray("log_list");

                        for(int i = 0 ; i<jsonArray.length();i++)
                        {

                           String action =  jsonArray.getJSONObject(i).getString("action");
                           String actionby =  jsonArray.getJSONObject(i).getString("action_by");
                           String actionTime =  jsonArray.getJSONObject(i).getString("action_time");

                           Logs logs = new Logs();
                           logs.setAction(action);
                           logs.setActionBy(actionby);
                           logs.setActionTime(actionTime);

                           logsList.add(logs);

                        }

                        Log.e("ok","ok2");

                        Log.e("array_size", String.valueOf(jsonArray.length()));
                        Log.e("list_log_size", String.valueOf(logsList.size()));

                        logAdapter = new LogAdapter(logsList,getActivity());
                        rv.setAdapter(logAdapter);
                        logAdapter.notifyDataSetChanged();

                        logAdapter.getItemCount();

                        Log.e("ok","ok"+ logAdapter.getItemCount());

                        pbmain.setVisibility(View.GONE);
                        tvNoLogFound.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);


                    }

                    else if(error.equalsIgnoreCase("true")){

                        pbmain.setVisibility(View.GONE);
                        tvNoLogFound.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);

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
                params.put("action", action);
                params.put("docid", docid);
                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }



}
