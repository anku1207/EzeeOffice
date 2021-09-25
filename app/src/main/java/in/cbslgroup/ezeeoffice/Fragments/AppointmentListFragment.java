package in.cbslgroup.ezeeoffice.Fragments;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Adapters.AppointmentlistAdapter;
import in.cbslgroup.ezeeoffice.Model.AppointmentList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentListFragment extends Fragment {

    List<AppointmentList> appointmentList = new ArrayList<>();
    private String dateOfApt = "";
    RecyclerView rvApt;
    ProgressBar pb;
    LinearLayout llNoAptFound;

    public AppointmentListFragment() {
        // Required empty public constructor
    }

    public static AppointmentListFragment createInstance(String date)
    {
        AppointmentListFragment fragment = new AppointmentListFragment();
        fragment.dateOfApt = date;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        rvApt = v.findViewById(R.id.rv_tabbed_appointmentlist);
        rvApt.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvApt.hasFixedSize();

        pb = v.findViewById(R.id.pb_tabbed_appointmentlist);
        llNoAptFound = v.findViewById(R.id.ll_notodofound_tabbed_appointmentlist);


        getSpecificDayApt(dateOfApt,"getSpecificDateApt");



        return v;
    }


    void getSpecificDayApt(String date, String action) {

        rvApt.setVisibility(View.GONE);
        llNoAptFound.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject item = jsonArray.getJSONObject(i);

                            Log.e("item todo", item.toString());

                            AppointmentList apt = new AppointmentList();
                            apt.setAppTime(jsonArray.getJSONObject(i).getString("app_time"));//
                            apt.setTitle(jsonArray.getJSONObject(i).getString("title"));//
                            apt.setAppDate(jsonArray.getJSONObject(i).getString("app_date"));//
                            apt.setId(jsonArray.getJSONObject(i).getString("id"));//
                            appointmentList.add(apt);
                        }

                        rvApt.setAdapter(new AppointmentlistAdapter(appointmentList, getActivity()));

                        llNoAptFound.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        rvApt.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {

                        llNoAptFound.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        rvApt.setVisibility(View.GONE);

                    }

                } catch (Exception e) {


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                HashMap<String, String> params = new HashMap<>();
                params.put("action", action);
                params.put("date", date);
                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

}
