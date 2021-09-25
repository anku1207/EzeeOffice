package in.cbslgroup.ezeeoffice.Activity.Search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
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
import in.cbslgroup.ezeeoffice.Adapters.SearchListAdapter;
import in.cbslgroup.ezeeoffice.Model.Search;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class QuickSearchActivity extends AppCompatActivity {

    public static String pdfView, imageView, fileDelete, viewMetadata;

    private static final float TOOLBAR_ELEVATION = 14f;
    public static String Roleid;
    public static TextView tvRvNoFileFound;
    ProgressBar progressBar;
    RecyclerView rvSearchList;
    SearchListAdapter searchListAdapter;
    SearchListAdapter filtersearchListAdapter;
    List<Search> searchList = new ArrayList<>();
    LinearLayout llNoFileFound;
    SearchView searchView, searchViewinLayout;
    SessionManager session;
    String session_userid, slid_Session;
    LinearLayout llSearchMain, llstaticbackground;
    String mData = null;
    Toolbar toolbar;
    // Keeps track of the overall vertical offset in the list
    int verticalOffset;
    // Determines the scroll UP/DOWN direction
    boolean scrollingUp;
    String querytext;
    LinearLayout llNoOfFile;
    TextView tvNoofFile;
    ArrayList<String> querylist = new ArrayList<>();
    EditText etSearchFile;

    AlertDialog alertDialog;

    List<Search> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);

        toolbar = findViewById(R.id.toolbar_quicksearch);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        llSearchMain = findViewById(R.id.ll_search_main);
        llstaticbackground = findViewById(R.id.ll_search_static_background);

        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // userid
        session_userid = user.get(SessionManager.KEY_USERID);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetail", MODE_PRIVATE);
        slid_Session = sharedPreferences.getString("userSlid", null);


        progressBar = findViewById(R.id.progressBar_quick_search);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        // rvSearchList = findViewById(R.id.cv_search_quicksearch);
        llNoFileFound = findViewById(R.id.ll_quick_no_file_found);

        rvSearchList = findViewById(R.id.rv_quicksearch);
        rvSearchList.setLayoutManager(new LinearLayoutManager(this));
        // rvSearchList.setHasFixedSize(true);
//        rvSearchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (scrollingUp) {
//                        if (verticalOffset > toolbar.getHeight()) {
//                            toolbarAnimateHide();
//                        } else {
//                            toolbarAnimateShow();
//                        }
//                    } else {
//                        if (toolbar.getTranslationY() < toolbar.getHeight() * -0.6 && verticalOffset > toolbar.getHeight()) {
//                            toolbarAnimateHide();
//                        } else {
//                            toolbarAnimateShow();
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                //verticalOffset += dy;
//                verticalOffset = rvSearchList.computeVerticalScrollOffset();
//                scrollingUp = dy > 0;
//                int toolbarYOffset = (int) (dy - toolbar.getTranslationY());
//                toolbar.animate().cancel();
//                if (scrollingUp) {
//                    if (toolbarYOffset < toolbar.getHeight()) {
//                        if (verticalOffset > toolbar.getHeight()) {
//                            toolbarSetElevation(TOOLBAR_ELEVATION );
//                        }
//                        toolbar.setTranslationY(-toolbarYOffset);
//                    } else {
//                        toolbarSetElevation(0);
//                        toolbar.setTranslationY(-toolbar.getHeight());
//                    }
//                } else {
//                    if (toolbarYOffset < 0) {
//                        if (verticalOffset <= 0) {
//                            toolbarSetElevation(0);
//                        }
//                        toolbar.setTranslationY(0);
//                    } else {
//                        if (verticalOffset > toolbar.getHeight()) {
//
//                            toolbarSetElevation(TOOLBAR_ELEVATION );
//                        }
//                        toolbar.setTranslationY(-toolbarYOffset);
//                    }
//                }
//            }
//
//
//        });


        tvRvNoFileFound = findViewById(R.id.tv_rv_nofilefound);
        llNoOfFile = findViewById(R.id.ll_quick_file_count);
        tvNoofFile = findViewById(R.id.tv_quick_filecount);


        getUserRole(session_userid);


        //searchViewinLayout = findViewById(R.id.searchview_quick_inlayout);
        etSearchFile = findViewById(R.id.et_quick_search_in_list);
        etSearchFile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                //searchViewinLayout.clearFocus();
                String newText = s.toString();

                querytext = newText;

                newText = newText.toLowerCase();
                filteredList = new ArrayList<>();

                for (int i = 0; i < searchList.size(); i++) {

                    final String text = searchList.get(i).getFileName().toLowerCase();
                    if (text.contains(newText)) {

                        filteredList.add(searchList.get(i));
                    }

                    //tvNoofFile.setText(searchListAdapter.getItemCount() +" files found");

                    rvSearchList.setLayoutManager(new LinearLayoutManager(QuickSearchActivity.this));

                    searchListAdapter = new SearchListAdapter(QuickSearchActivity.this, filteredList);
                    searchListAdapter.setOnClickListener(new SearchListAdapter.OnClickListener() {
                        @Override
                        public void onDeleteClick(Search searchobj, String docid, String fullname, String permission, String roleid, String ip, String userid, String filename, String position) {


                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QuickSearchActivity.this);
                            LayoutInflater inflater = (LayoutInflater) QuickSearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);

                            Button btndel = dialogView.findViewById(R.id.btn_delete_popup);
                            btndel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    // listener.onDeleteClick(docid, fullname, "No", roleid, ip, userid, filename, String.valueOf(position));

                                    Log.e("filtered item", String.valueOf(position));
                                    Log.e("recyclelist item", getMainListItemPosition(Integer.parseInt(position)));


                                    //delete doc volley request
                                    deleteDoc(docid, fullname, "No", roleid, ip, userid, filename, getMainListItemPosition(Integer.parseInt(position)));


                                    filteredList.remove(position);
                                    searchListAdapter.notifyDataSetChanged();
                                    searchListAdapter.notifyItemRangeRemoved(Integer.parseInt(position), filteredList.size());

                                    tvNoofFile.setText(searchListAdapter.getItemCount() + " files found");

                                    alertDialog.dismiss();


                                }
                            });

//btn_close_delete_popup //btn_delete_popup
                            Button btn_cancel_ok = dialogView.findViewById(R.id.btn_close_delete_popup);
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


                    // deleteDoc(docid, fullname, "No", roleid, ip, userid, filename, String.valueOf(position));


                    if (searchListAdapter.getItemCount() == 0) {

                        tvRvNoFileFound.setVisibility(View.VISIBLE);
                        rvSearchList.setAdapter(searchListAdapter);
                        searchListAdapter.notifyDataSetChanged();

                    } else {

                        tvRvNoFileFound.setVisibility(View.GONE);
                        rvSearchList.setAdapter(searchListAdapter);
                        searchListAdapter.notifyDataSetChanged();


                    }

                    if (searchListAdapter.getItemCount() == 0) {

                        tvNoofFile.setText("0 file found");

                    } else {

                        tvNoofFile.setText(searchListAdapter.getItemCount() + " files found");

                    }


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

     /*   searchViewinLayout.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                //searchViewinLayout.clearFocus();
                querytext = newText;

                newText = newText.toLowerCase();
                final List<Search> filteredList = new ArrayList<>();

                for (int i = 0; i < searchList.size(); i++) {

                    final String text = searchList.get(i).getFileName().toLowerCase();
                    if (text.contains(newText)) {

                        filteredList.add(searchList.get(i));
                    }

                    //tvNoofFile.setText(searchListAdapter.getItemCount() +" files found");

                    rvSearchList.setLayoutManager(new LinearLayoutManager(QuickSearchActivity.this));

                    searchListAdapter = new SearchListAdapter(QuickSearchActivity.this, filteredList, new SearchListAdapter.onDeleteClick() {
                        @Override
                        public void onDeleteClick(String docid, String fullname, String permission, String roleid, String ip, String userid, String filename, String position) {

                            deleteDoc(docid, fullname, "No", roleid, ip, userid, filename, String.valueOf(position));


                        }
                    });

                    if (filteredList.size() == 0) {

                        tvRvNoFileFound.setVisibility(View.VISIBLE);
                        rvSearchList.setAdapter(searchListAdapter);
                        searchListAdapter.notifyDataSetChanged();

                    } else {

                        tvRvNoFileFound.setVisibility(View.GONE);
                        rvSearchList.setAdapter(searchListAdapter);
                        searchListAdapter.notifyDataSetChanged();


                    }

                    if (searchListAdapter.getItemCount() == 0) {

                        tvNoofFile.setText("0 file found");

                    } else {

                        tvNoofFile.setText(searchListAdapter.getItemCount() + " files found");

                    }


                }

                return false;
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quick_search_settings, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_quick_search);
        searchView = (SearchView) MenuItemCompat.getActionView(myActionMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                //UserFeedback.show( "SearchOnQueryTextSubmit: " + query);

                //searchView.setQuery(query,false);

                // searchView.clearFocus();

                //querylist.add(query);


                Search(query, slid_Session);

                if (!searchView.isIconified()) {

                    searchView.setIconified(false);

                }


                //myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }


        });

        // toolbar.setSubtitle("Searched Text : "+ querytext);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_quick_search) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void Search(final String text, final String slid) {


        llSearchMain.setVisibility(View.GONE);
        llstaticbackground.setVisibility(View.GONE);
        llNoOfFile.setVisibility(View.GONE);
        llNoFileFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        searchList.clear();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.QUICK_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("quick search", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("array quick", String.valueOf(jsonArray.length()));

                    // tvNoofFile.setText(jsonArray.length() + " files found");

                    if (jsonArray.length() == 0 || TextUtils.isEmpty(response)) {


                        llSearchMain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llNoFileFound.setVisibility(View.VISIBLE);
                    } else {

                        //  String metadata;
                        // StringBuilder sb = new StringBuilder();

                        llNoFileFound.setVisibility(View.GONE);

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //JSONArray jsonArray1 = jsonObject.getJSONArray("metadata");
                            // String meta =jsonArray1.get(0).toString();

                            //clearing the stringbuilder string which is dynamically building
                            //sb.setLength(0);

                           /* for (int j = 0; j < jsonArray1.length(); j++) {


                                metadata = jsonArray1.get(j).toString();//{"AadharNumber":"TOOLBAR_ELEVATION 03"}
                                String a = metadata.substring(metadata.indexOf("{") + 1, metadata.indexOf("}"));//"AadharNumber":"TOOLBAR_ELEVATION 03"
                                String b = a.substring(0, a.indexOf(":"));//"AadharNumber"
                                String label = b.substring(1, b.length() - 1);//label = AadharNumber
                                String c = a.substring(a.indexOf(":") + 1, a.length());//"1003"

                                if (c.equals("null")) {

                                    c = " ";
                                    sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");
                                } else {

                                    c = c.substring(1, c.length() - 1);
                                    sb.append("<b>").append(label).append("</b>").append(" : ").append(c).append("<br>");

                                }

                                // Log.e("metadata",c);

                                // Log.e("metadata", "Field : " + label+ "||" + "Meta : " + c);

                                //metadata = "<b>" + label + "</b>" + " : " + mData;//label+" : "+meta;

                                // Log.e("metadata", "Field" + label + " " + "Meta" + metadata);
                                // String c = a.substring(a.indexOf(":"),b.length());
                                // Log.e("value",label);


                            }
*/
                            // String meta = sb.toString();
                            // Log.e("metadata",meta);
                            String docid = jsonObject.getString("doc_id");
                            String docname = jsonObject.getString("doc_name");
                            String extension = jsonObject.getString("doc_extn");
                            String path = jsonObject.getString("doc_path");
                            String filename = jsonObject.getString("old_doc_name");
                            String size = jsonObject.getString("doc_size");
                            String pagecount = jsonObject.getString("noofpages");
                            String storagename = jsonObject.getString("storagename");

                            //Log.e("docid", docid);
                            // Log.e("docname", docname);
                            // String date = jsonObject.getString("dateposted");

                            // Log.e("doc path", path + "    " + extension);

                            String intsize = String.valueOf(Integer.parseInt(size) / 1024);
                            searchList.add(new Search(filename, intsize + " KB", pagecount, storagename, docname, path, extension, "-", docid));

                        }

                        searchListAdapter = new SearchListAdapter(QuickSearchActivity.this, searchList);
                        rvSearchList.setAdapter(searchListAdapter);
                        searchListAdapter.setOnClickListener(new SearchListAdapter.OnClickListener() {
                            @Override
                            public void onDeleteClick(Search sobj, String docid, String fullname, String permission, String roleid, String ip, String userid, String filename, String position) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QuickSearchActivity.this);
                                LayoutInflater inflater = (LayoutInflater) QuickSearchActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_delete, null);

                                Button btndel = dialogView.findViewById(R.id.btn_delete_popup);
                                btndel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        // listener.onDeleteClick(docid, fullname, "No", roleid, ip, userid, filename, String.valueOf(position));

                                        //delete doc volley request
                                        deleteDoc(docid, fullname, "No", roleid, ip, userid, filename, String.valueOf(position));


                                        alertDialog.dismiss();


                                    }
                                });

//btn_close_delete_popup //btn_delete_popup
                                Button btn_cancel_ok = dialogView.findViewById(R.id.btn_close_delete_popup);
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

                        tvNoofFile.setText(jsonArray.length() + " files found");
                        llNoFileFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llSearchMain.setVisibility(View.VISIBLE);
                        llNoOfFile.setVisibility(View.VISIBLE);


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
                params.put("text", text);
                params.put("slid", slid);
                params.put("userid", MainActivity.userid);


                Util.printParams(params, "quicksearchparam");
                return params;

            }


        };

        VolleySingelton.getInstance(QuickSearchActivity.this).addToRequestQueue(stringRequest);


    }

    void getUserRole(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {


                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    // String userrole = jsonObject.getString("user_role");
                    String roleid = jsonObject.getString("role_id");
                    String file_delete = jsonObject.getString("file_delete");
                    String view_metadata = jsonObject.getString("view_metadata");
                    //String file_view = jsonObject.getString("file_view");

                    String pdf_file = jsonObject.getString("pdf_file");
                    String image_file = jsonObject.getString("image_file");


                    if (file_delete.equalsIgnoreCase("1")) {

                        fileDelete = "1";

                    } else {

                        fileDelete = "0";
                    }

                    if (view_metadata.equalsIgnoreCase("1")) {

                        viewMetadata = "1";

                    } else {

                        viewMetadata = "0";
                    }

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


                    Roleid = roleid;

                    Log.e("roleid", roleid);


                    Intent intent = getIntent();
                    String text = intent.getStringExtra("text");

                    if (text == null || text.equals("") || text.isEmpty()) {


                        Log.e("No intent data", "null");

                        showNoFileFound();

                    } else {

                        Search(text, slid_Session);
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
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");


                return params;
            }
        };

        VolleySingelton.getInstance(QuickSearchActivity.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void onBackPressed() {

        // Log.e("onBackPressed: ",searchViewinLayout.getQueryHint().toString());

        Intent intent = new Intent(QuickSearchActivity.this, MainActivity.class);
        startActivity(intent);


    }


    private void deleteDoc(final String docid, final String fullname, final String permission, final String roleid, final String ip, final String userid, final String filename, final String position) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DELETE_DOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response deldoc", response);

                try {


                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if (error.equals("true")) {

                        Toast.makeText(QuickSearchActivity.this, message, Toast.LENGTH_LONG).show();


                    } else if (error.equals("false")) {


                        if (searchListAdapter.getItemCount() == 0) {

                            tvRvNoFileFound.setVisibility(View.VISIBLE);

                        } else {

                            searchList.remove(Integer.parseInt(position));
                            searchListAdapter.notifyDataSetChanged();
                            tvRvNoFileFound.setVisibility(View.GONE);

                            if (searchListAdapter.getItemCount() == 0) {

                                getUserRole(userid);

                            }


                        }

                        Toast.makeText(QuickSearchActivity.this, message, Toast.LENGTH_LONG).show();


                        tvNoofFile.setText(searchListAdapter.getItemCount() + " files found");

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

        VolleySingelton.getInstance(QuickSearchActivity.this).addToRequestQueue(stringRequest);


    }

    String setRoles() {


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("role_id").append(",")
                .append("file_delete").append(",")
                .append("view_metadata").append(",")
                .append("pdf_file").append(",")
                .append("image_file");

        return stringBuilder.toString();

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        // setElevation() only works on Lollipop
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(elevation);
        }
    }

    private void toolbarAnimateShow() {
        toolbar.animate()

                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        getSupportActionBar().show();
                    }
                });
    }

    private void toolbarAnimateHide() {
        toolbar.animate()

                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getSupportActionBar().hide();
                    }
                });
    }


    void showNoFileFound() {

        llSearchMain.setVisibility(View.GONE);
        llstaticbackground.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        llNoOfFile.setVisibility(View.GONE);
        llNoFileFound.setVisibility(View.VISIBLE);

    }


    String getMainListItemPosition(int pos) {

        String filDocid = filteredList.get(pos).getDocid();


        for (int i = 0; i < searchList.size(); i++) {

            if (filDocid.equalsIgnoreCase(searchList.get(i).getDocid())) {


                return String.valueOf(i);

            }

        }

        return null;

    }


}
