package in.cbslgroup.ezeeoffice.Activity.Dms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import in.cbslgroup.ezeeoffice.Activity.Viewer.FileViewActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.MetaSearchFileViewActivity;
import in.cbslgroup.ezeeoffice.Adapters.FileViewHorizontalAdapter;
import in.cbslgroup.ezeeoffice.Adapters.MoveStorageListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.StorageAllotedAdapter;
import in.cbslgroup.ezeeoffice.Interface.CustomItemClickListener;
import in.cbslgroup.ezeeoffice.Interface.MoveStorageListListener;
import in.cbslgroup.ezeeoffice.Model.Foldername;
import in.cbslgroup.ezeeoffice.Model.MoveStorage;
import in.cbslgroup.ezeeoffice.Model.StorageAlloted;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class MoveStorageActivity extends AppCompatActivity {


    JSONArray jsonArray;

    RecyclerView rvMoveStorage, rvHori;
    List<MoveStorage> moveStorageList = new ArrayList<>();
    MoveStorageListAdapter moveStorageListAdapter;

    String previousslid;

    ArrayList<Foldername> horilist = new ArrayList<>();

    String slidStr;
    TextView tvpreviousSlid, tvpreviousFname;
    FileViewHorizontalAdapter fileViewHorizontalAdapter;
    String storagename;
    LinearLayout llnodirectoryfound;
    ProgressBar progressBar;

    String rootMoveFoldername, rootMoveFolderSlid;

    Toolbar toolbar;

    String mode, docids, viewer;

    TextView tvStorageAllotedPopup, tvFoldername;
    List<StorageAlloted> storageAllotedList = new ArrayList<>();
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_storage);

        toolbar = findViewById(R.id.toolbar_storage_management_movestorage);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        docids = intent.getStringExtra("docids");
        viewer = intent.getStringExtra("viewer");

        //filecount = intent.getStringExtra("filecount");

        String foldernameNslid = intent.getStringExtra("foldername&&slid");
        Log.e("foldernameprevious", foldernameNslid);
//        Log.e("docids",docids);
        // Log.e("filecount",filecount);


        rootMoveFoldername = foldernameNslid.substring(0, foldernameNslid.indexOf("&&"));
        rootMoveFolderSlid = foldernameNslid.substring(foldernameNslid.indexOf("&&") + 2);
        Log.e("rootmovename", rootMoveFoldername);

        rvMoveStorage = findViewById(R.id.rv_move_storage);
        rvMoveStorage.setLayoutManager(new LinearLayoutManager(this));
        rvMoveStorage.setItemViewCacheSize(moveStorageList.size());
        rvMoveStorage.setHasFixedSize(true);

        rvHori = findViewById(R.id.rv_move_storage_hori);
        rvHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHori.setItemViewCacheSize(moveStorageList.size());
        rvHori.setHasFixedSize(true);

        tvpreviousFname = findViewById(R.id.tv_move_storage_fname_previous);
        tvpreviousSlid = findViewById(R.id.tv_move_storage_slid_previous);
        tvFoldername = findViewById(R.id.tv_move_storage_foldername);

        tvStorageAllotedPopup = findViewById(R.id.move_copy_storage_alloted_popup);

        llnodirectoryfound = findViewById(R.id.ll_movestorage_nofilefound);
        progressBar = findViewById(R.id.progressBar_move_storage);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        getSlidId(MainActivity.userid);


        tvStorageAllotedPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                horilist.clear();
                showSelectStoragePopup(storageAllotedList);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.move_storage_menu, menu);

        if (mode.equals("copy")) {

            toolbar.setTitle("Copy Storage");
            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(false);
        } else if (mode.equals("move")) {

            toolbar.setTitle("Move Storage");

            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(false);

        } else if (mode.equals("movefiles")) {

            toolbar.setTitle("Move Storage Files");


            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(true);


            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(false);


        } else if (mode.equals("copyfiles")) {

            toolbar.setTitle("Copy Storage Files");


            toolbar.getMenu().findItem(R.id.action_move_storage_copyfiles).setVisible(true);

            toolbar.getMenu().findItem(R.id.action_move_storage_movefiles).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_copyhere).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_move_storage_movehere).setVisible(false);


        }


        return true;
    }

    @Override
    public void onBackPressed() {


        // getFoldername(slidlist.get(slidlist.size()-1));
        //slidlist.remove(slidlist.size()-1);


        super.onBackPressed();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_move_storage_copyhere) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);


            AlertDialog alertDialog = new AlertDialog.Builder(MoveStorageActivity.this).create();
            alertDialog.setTitle("Copy Storage");
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage("Are you sure you want to copy " + rootMoveFoldername + " " + "to" + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Copy Here",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

                            copyStorage(dSlid, rootMoveFolderSlid, rootMoveFoldername, MainActivity.ip, MainActivity.username, MainActivity.userid);

                            dialog.dismiss();
                        }
                    });

            alertDialog.show();

            return true;
        }
        if (id == R.id.action_move_storage_movehere) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);


            AlertDialog alertDialog = new AlertDialog.Builder(MoveStorageActivity.this).create();
            alertDialog.setTitle("Move Storage");
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage("Are you sure you want to move " + rootMoveFoldername + " " + "to" + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Move Here",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

                            moveStorage(rootMoveFolderSlid, dSlid, MainActivity.ip, MainActivity.username, MainActivity.userid);

                            dialog.dismiss();
                        }
                    });

            alertDialog.show();

            return true;
        }


        if (id == R.id.action_move_storage_movefiles) {


            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

            AlertDialog alertDialog = new AlertDialog.Builder(MoveStorageActivity.this).create();
            alertDialog.setTitle("Move Storage Files");
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage("Are you sure you want to move files from " + rootMoveFoldername + " " + "to" + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Move Files Here",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

                            String doclistArray = FileViewActivity.doclistArray;

                            //multiMove("Ankit Roy ","54","100.89.117.173","329","329","899","[19824]");

                            if (viewer.equalsIgnoreCase("file")) {

                                multiMove(MainActivity.username, MainActivity.userid, MainActivity.ip, DmsActivity.slid_Session, rootMoveFolderSlid, dSlid, FileViewActivity.doclistArray);

                            } else if (viewer.equalsIgnoreCase("meta")) {
                                multiMove(MainActivity.username, MainActivity.userid, MainActivity.ip, DmsActivity.slid_Session, rootMoveFolderSlid, dSlid, MetaSearchFileViewActivity.doclistArrayMeta);
                            }


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();


            return true;
        }


        if (id == R.id.action_move_storage_copyfiles) {

            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

            AlertDialog alertDialog = new AlertDialog.Builder(MoveStorageActivity.this).create();
            alertDialog.setTitle("Copy Storage Files");
            alertDialog.setIcon(R.drawable.ic_round_folder);
            alertDialog.setMessage("Are you sure you want to copy files from " + rootMoveFoldername + " " + "to" + " " + dFname + " ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Copy Files Here",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                            String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                            final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

                            String doclistArray = FileViewActivity.doclistArray;

                            //multiMove("Ankit Roy ","54","100.89.117.173","329","329","899","[19824]");

                            if (viewer.equalsIgnoreCase("file")) {

                                multiCopy(MainActivity.username, MainActivity.userid, MainActivity.ip, DmsActivity.slid_Session, rootMoveFolderSlid, dSlid, FileViewActivity.doclistArray);
                            } else if (viewer.equalsIgnoreCase("meta")) {

                                multiCopy(MainActivity.username, MainActivity.userid, MainActivity.ip, DmsActivity.slid_Session, rootMoveFolderSlid, dSlid, MetaSearchFileViewActivity.doclistArrayMeta);

                            }


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();


            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    void getFoldername(final String slid) {

        //spinnerlist.clear();

        moveStorageList.clear();
        //horilist.subList(1,horilist.size()-1).clear();
        //horilist.clear();


        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMoveStorage.setVisibility(View.GONE);


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("getfolname", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String str = jsonObject.getString("storagename");

                    storagename = str.substring(0, str.indexOf("&&"));
                    slidStr = str.substring(str.indexOf("&&") + 2);


                    //329

                    JSONArray jsonArray = jsonObject.getJSONArray("foldername");

                    if (jsonArray.length() == 0) {

                        llnodirectoryfound.setVisibility(View.VISIBLE);
                        rvMoveStorage.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);


                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2);

                            moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));

                            // spinnerlist.add(fname);

                        }

                        String id = storagename + "&&" + DmsActivity.slid_Session;
                        Foldername foldername1 = new Foldername();
                        foldername1.setFoldername(id);
                        horilist.add(foldername1);

                        fileViewHorizontalAdapter = new FileViewHorizontalAdapter(horilist, MoveStorageActivity.this, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String slid, String fullFolderName) {


                                if (slid == DmsActivity.slid_Session) {

                                    horilist.clear();
                                    String id = storagename + "&&" + DmsActivity.slid_Session;
                                    Foldername foldername1 = new Foldername();
                                    foldername1.setFoldername(id);
                                    horilist.add(foldername1);


                                    fileViewHorizontalAdapter.notifyDataSetChanged();


                                } else {

                                    horilist.subList(position + 1, horilist.size()).clear();
                                    fileViewHorizontalAdapter.notifyDataSetChanged();

                                    getChildFoldername(slid);

                                }


                            }
                        });

                        rvHori.setAdapter(fileViewHorizontalAdapter);


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, MoveStorageActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {


                                // tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);

                                fileViewHorizontalAdapter.notifyDataSetChanged();

                           /* tvpreviousFname.setText(foldername);
                            tvpreviousSlid.setText(slid);*/


                                getChildFoldername(slid);


                            }
                        });
                        rvMoveStorage.setAdapter(moveStorageListAdapter);
                        moveStorageListAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        rvMoveStorage.setVisibility(View.VISIBLE);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


              /*  spinnerlist.add("Select Storage");
                spinnerStaticAdapter.notifyDataSetChanged();

                spinnerStaticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerStorageLevel.setAdapter(spinnerStaticAdapter);
                spinnerStorageLevel.setSelected(false);

                spinnerStorageLevel.setSelection(0,true);


                final AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selected = parent.getItemAtPosition(position).toString();
                        String slid = selected.substring(selected.indexOf("&&")+2,selected.length());

                        Log.e("slid",slid);

                        if(slid.matches("[0-9]+")){


                            llInflation.removeViews(3,myLayouts.size());
                            myLayouts.subList(0,myLayouts.size()).clear();

                            Toast.makeText(MoveStorageActivity.this, slid, Toast.LENGTH_SHORT).show();

                            getChildFoldername(slid);
                            //addSpinner();

                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };

               // spinnerStorageLevel.setSelection(0,true);


                spinnerStorageLevel.setSelection(spinnerStaticAdapter.getCount());
                spinnerStorageLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        spinnerStorageLevel.setOnItemSelectedListener(listener);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {



                    }
                });
*/

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("slid", slid);


                return params;
            }
        };


        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void getChildFoldername(final String slid) {

        // spinnerchildlist.clear();

        progressBar.setVisibility(View.VISIBLE);
        llnodirectoryfound.setVisibility(View.GONE);
        rvMoveStorage.setVisibility(View.GONE);


        moveStorageList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("getchildfoldernames", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("foldername");


                    if (jsonArray.length() == 0) {

                        progressBar.setVisibility(View.GONE);
                        rvMoveStorage.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.VISIBLE);


                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fname = jsonArray.getJSONObject(i).getString("foldername");

                            String foldername = fname.substring(0, fname.indexOf("&&"));
                            String slid = fname.substring(fname.indexOf("&&") + 2);

                            moveStorageList.add(new MoveStorage(foldername, slid, R.drawable.ic_no_of_folders));


                         /*   arrayListchild.add(fname);


                            mySpinnerChildlist.add(arrayListchild);
*/
                        }


                        moveStorageListAdapter = new MoveStorageListAdapter(moveStorageList, MoveStorageActivity.this, new MoveStorageListListener() {
                            @Override
                            public void onCardviewClick(View v, int position, String slid, String foldername) {

                                //tvFoldername.setText(foldername);

                                String id = foldername + "&&" + slid;
                                Foldername foldername1 = new Foldername();
                                foldername1.setFoldername(id);
                                horilist.add(foldername1);
                                fileViewHorizontalAdapter.notifyDataSetChanged();

                                getChildFoldername(slid);
                            }
                        });

                        rvMoveStorage.setAdapter(moveStorageListAdapter);


                        moveStorageListAdapter.notifyDataSetChanged();


                        progressBar.setVisibility(View.GONE);
                        llnodirectoryfound.setVisibility(View.GONE);
                        rvMoveStorage.setVisibility(View.VISIBLE);


                    }




                    /*  addSpinner();*/

                    /*    mySpinnerChildlistCounter++;
                     */


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
                params.put("slid", slid);


                return params;
            }
        };

        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }

    //TODO
    //have to mak move storage api and function

    void moveStorage(final String rootMoveFolderSlid, final String MoveFolderDestinationSlid, final String ip, final String username, final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_COPY_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("movestorage", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {

                        Toast.makeText(MoveStorageActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else if (error.equals("false")) {

                        Toast.makeText(MoveStorageActivity.this, message, Toast.LENGTH_SHORT).show();
                       /*Intent intent = new Intent(MoveStorageActivity.this,DmsActivity.class);
                       startActivity(intent);
                       overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);*/
                        String destinationfoldername = horilist.get(horilist.size() - 1).getFoldername();
                        String dFname = destinationfoldername.substring(0, destinationfoldername.indexOf("&&"));
                        final String dSlid = destinationfoldername.substring(destinationfoldername.indexOf("&&") + 2);

                        onBackPressed();

                        //getChildFoldername(dSlid);


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
                params.put("rootFolderSlid", rootMoveFolderSlid);
                params.put("destinationFolderSlid", MoveFolderDestinationSlid);
                params.put("moveIp", ip);
                params.put("moveUserId", userid);
                params.put("moveUsername", username);


                Util.printParams(params, "moveparams");


                return params;
            }
        };

        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void copyStorage(final String rootMoveFolderSlid, final String CopyFolderDestinationSlid, final String CopyFolderDestinationFoldername, final String ip, final String username, final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MOVE_COPY_STORAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("copyStorage", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {

                        Toast.makeText(MoveStorageActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else if (error.equals("false")) {

                        Toast.makeText(MoveStorageActivity.this, message, Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MoveStorageActivity.this, DmsActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                          onBackPressed();

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
                params.put("copyrootFolderSlid", rootMoveFolderSlid);
                params.put("copydestinationFolderSlid", CopyFolderDestinationSlid);
                params.put("copyIp", ip);
                params.put("copyUserId", userid);
                params.put("copyUsername", username);
                params.put("copydestinationFoldername", CopyFolderDestinationFoldername);


                return params;
            }
        };

        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void multiMove(final String username, final String userid, final String ip, final String movetoParentid, final String moveFromSlid, final String movetoSlid, final String docids) {


        // Log.e("multimoveValues",username+" "+userid+" "+ip+" "+movetoParentid+" "+moveFromSlid+" "+movetoSlid+" "+docids);

        Log.e("-", "------ move multiple -----");
        Log.e("username", username);
        Log.e("userid", userid);
        Log.e("ip", ip);
        Log.e("movetoParentid", movetoParentid);
        Log.e("moveFromSlid", moveFromSlid);
        Log.e("movetoSlid", movetoSlid);
        Log.e("docids", docids);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("multimove", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MoveStorageActivity.this, msg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(MoveStorageActivity.this, msg, Toast.LENGTH_SHORT).show();
                        onBackPressed();


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
                params.put("mulMove_ip", ip);
                params.put("mulMove_username", username);
                params.put("mulMove_userid", userid);
                params.put("mulMove_sl_id_move_multi", moveFromSlid);
                params.put("mulMove_lastMoveId", movetoSlid);
                params.put("mulMove_moveToParentId", movetoParentid);
                params.put("mulMove_doc_id_smove_multi", docids);//crct


                Util.printParams(params, "multimoveparams");

                return params;
            }


        };


        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void multiCopy(final String username, final String userid, final String ip, final String copytoParentid, final String copyFromSlid, final String copytoSlid, final String docids) {

        Log.e("-", "------copy multiple-----");
        Log.e("username", username);
        Log.e("userid", userid);
        Log.e("ip", ip);
        Log.e("copytoParentid", copytoParentid);
        Log.e("copyFromSlid", copyFromSlid);
        Log.e("copytoSlid", copytoSlid);
        Log.e("docids", docids);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.MULTI_MOVE_COPY_DEL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("multicopy", response);


                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(MoveStorageActivity.this, msg, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(MoveStorageActivity.this, msg, Toast.LENGTH_SHORT).show();
                        onBackPressed();

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
                params.put("mulCopy_ip", ip);
                params.put("mulCopy_username", username);
                params.put("mulCopy_fromSlid", copyFromSlid);
                params.put("mulCopy_toSlid", copytoSlid);
                params.put("mulCopy_userid", userid);
                params.put("mulCopy_copyToParentId", copytoParentid);
                params.put("mulCopy_doc_ids", docids);

                Util.printParams(params, "multicopyparams");


                return params;
            }


        };


        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }

    void showSelectStoragePopup(List<StorageAlloted> list) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MoveStorageActivity.this);
        LayoutInflater inflater = (LayoutInflater) MoveStorageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_select_root_storage_popup, null);

        RecyclerView rvMain = dialogView.findViewById(R.id.rv_mv_cpy_select_storage);
        rvMain.setLayoutManager(new LinearLayoutManager(MoveStorageActivity.this));
        rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        StorageAllotedAdapter storageAllotedAdapter = new StorageAllotedAdapter(list, MoveStorageActivity.this);
        rvMain.setAdapter(storageAllotedAdapter);

        storageAllotedAdapter.setOnClickListener(new StorageAllotedAdapter.OnClickListener() {
            @Override
            public void onStorageClicked(String slid,String storagename) {

                //toolbar.setSubtitle(rootMoveFoldername);
                tvStorageAllotedPopup.setText(storagename);
                previousslid = slid;
                alertDialog.dismiss();

                tvStorageAllotedPopup.setVisibility(View.VISIBLE);

                horilist.clear();
                getFoldername(slid);

            }
        });

        Button btnClose = dialogView.findViewById(R.id.btn_cpy_mv_cancel_popup);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                onBackPressed();

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    public void getSlidId(String userid) {

        tvStorageAllotedPopup.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.DASHBOARD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dashboard response", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jobj = jsonObject.getJSONObject("dashboard_data");

                    JSONArray jsonArray = jobj.getJSONArray("storagealloted");

                    if (jsonArray.length() == 0) {

                        tvStorageAllotedPopup.setVisibility(View.GONE);

                        onBackPressed();
                        Toast.makeText(MoveStorageActivity.this, "No Storage Alloted", Toast.LENGTH_SHORT).show();

                    }
                    else if(jsonArray.length()>1) {



                        for (int i = 0; i < jsonArray.length(); i++) {

                            String storagename = jsonArray.getJSONObject(i).getString("storage_name");
                            String storageid = jsonArray.getJSONObject(i).getString("storage_id");

                            storageAllotedList.add(new StorageAlloted(storagename, storageid));

                        }

                        showSelectStoragePopup(storageAllotedList);

                    }

                    else{

                        //toolbar.setSubtitle(rootMoveFoldername);
                        tvStorageAllotedPopup.setText(rootMoveFoldername);
                        previousslid = MainActivity.slid_session;

                        getFoldername(MainActivity.slid_session);


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);

                // getSlidId(userid);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parameters = new HashMap<String, String>();
                Log.e("userid before response", userid);
                parameters.put("userid", userid);
                parameters.put("action", "getDashBoardData");


                return parameters;
            }
        };


        VolleySingelton.getInstance(MoveStorageActivity.this).addToRequestQueue(stringRequest);


    }


}
