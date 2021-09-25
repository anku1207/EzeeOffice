package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.MoveStorageActivity;
import in.cbslgroup.ezeeoffice.Activity.Viewer.FileViewActivity;
import in.cbslgroup.ezeeoffice.Interface.CustomItemClickListener;
import in.cbslgroup.ezeeoffice.Model.Foldername;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;



public class FileViewAdapter extends RecyclerView.Adapter<FileViewAdapter.ViewHolder> {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Context context;
    private List<Foldername> folderName;
    private CustomItemClickListener listener;

    private int lastPosition = -1;

    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = true;

    AlertDialog alertDialog;
    DmsActivity dmsActivity ;

    int weight = 3; //number of parts in the recycler view.


    public FileViewAdapter(Context context, List<Foldername> folderName, CustomItemClickListener listener) {

        this.context = context;
        this.folderName = folderName;
        this.listener = listener;
        dmsActivity = (DmsActivity) context;

    }

    @Override
    public FileViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v ;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_icon,null);
       // v.getLayoutParams().height = parent.getHeight() / weight;
        return new ViewHolder(v,dmsActivity);


    }

    @Override
    public void onBindViewHolder(final FileViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        //logic may be not so effiecient
        String regex = "[0-9]+";

        // fName = folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&"));

        if (folderName.get(position).getFoldername().matches(regex)) {

            if(folderName.get(position).getFoldername().equals("0")){

                Log.e("files","no file found");
                holder.lv.setVisibility(View.GONE);
            }
            else{

                holder.checkBoxDmsHierarcy.setVisibility(View.GONE);
                holder.tvFullFoldername.setText(folderName.get(position).getFoldername() + "&&" + DmsActivity.dynamicFileSlid);
                holder.tvFoldername.setText(folderName.get(position).getFoldername() + " files");
                final String fileName = folderName.get(position).getFoldername() + "&&" + DmsActivity.dynamicFileSlid;
                String slidFile = fileName.substring(fileName.indexOf("&&") + 2);
                holder.tvFolderSlid.setText(slidFile);

                // Here you apply the animation when the view is bound
                setAnimation(holder.itemView, position);
                holder.ivFolder.setImageResource(R.mipmap.file_blue);

                holder.lv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String slidFile = fileName.substring(fileName.indexOf("&&") + 2);
                        String fldrname = DmsActivity.foldernameDyanamic;
                        Log.e("Slid in FileViewAdap", slidFile);
                        Intent intent = new Intent(context, FileViewActivity.class);
                        intent.putExtra("slid", slidFile);
                        intent.putExtra("foldername", fldrname);
                        ((Activity) context).startActivityForResult(intent,101);

                        // Toast.makeText(context,"files",Toast.LENGTH_LONG).show();

                    }
                });

                holder.lv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                       // String foldername = folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&"));


                          Log.e("longclick","no long click");




                        return true;
                    }
                });

            }


        } else {

            // Log.e("foldername",folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&")));
            holder.tvFoldername.setText(folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&")));
            //holder.tvFoldername.setText(folderName.get(position).getFoldername());
            // Here you apply the animation when the view is bound
            setAnimation(holder.itemView, position);

            // Changing the folder icon if folder has no child or data

            String checkFolderNull = folderName.get(position).getFoldername().substring(folderName.get(position).getFoldername().lastIndexOf("&&")+2);
            Log.e("checknull",checkFolderNull);

            checkBlank(checkFolderNull,holder);


          //  checkBlank();


          /*  if(checkFolderNull.equalsIgnoreCase("0")){

                holder.ivFolder.setImageResource(R.drawable.ic_folder_open_blue_24dp);

            }
*/
            /////////////////////////////////////////////////

            holder.lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //view.startAnimation(buttonClick);
                    // String foldrName = folderName.get(position).getFoldername().substring(0,folderName.indexOf("&&"));
                    String slid = folderName.get(position).getFoldername().substring(folderName.get(position).getFoldername().indexOf("&&") + 2);
                    String fullFolderName = folderName.get(position).getFoldername();
                    listener.onItemClick(view, position, slid, fullFolderName);


                }
            });

            holder.lv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final String foldername = folderName.get(position).getFoldername().substring(0, folderName.get(position).getFoldername().indexOf("&&"));
                    final String fullfoldername = folderName.get(position).getFoldername();


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.alertdialog_move_copy, null);

                    TextView tvmove= dialogView.findViewById(R.id.alertdialog_tv_move);
                    TextView tvcopy= dialogView.findViewById(R.id.alertdialog_tv_copy);

                    if(DmsActivity.copy_storage.equalsIgnoreCase("1")){

                        tvcopy.setVisibility(View.VISIBLE);
                    }
                    else{

                        tvcopy.setVisibility(View.GONE);

                    }

                    if(DmsActivity.move_storage.equalsIgnoreCase("1")){

                        tvmove.setVisibility(View.VISIBLE);
                    }
                    else{

                        tvmove.setVisibility(View.GONE);

                    }



                    tvmove.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           Intent intent = new Intent(context,MoveStorageActivity.class);
                           intent.putExtra("foldername&&slid",fullfoldername);
                           intent.putExtra("mode","move");

                           context.startActivity(intent);

                           alertDialog.dismiss();


                       }
                   });



                    tvcopy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(context,MoveStorageActivity.class);
                            intent.putExtra("foldername&&slid",fullfoldername);
                            intent.putExtra("mode","copy");
                            context.startActivity(intent);

                            alertDialog.dismiss();

                        }
                    });



                 /*   Button btn_cancel_ok = dialogView.findViewById(R.id.btn_cancel_meta_popup);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });*/

                    dialogBuilder.setView(dialogView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));



                    if(DmsActivity.copy_storage.equalsIgnoreCase("1")||DmsActivity.move_storage.equalsIgnoreCase("1"))

                    {

                        alertDialog.show();

                    }

                    else if(DmsActivity.copy_storage.equalsIgnoreCase("1")&&DmsActivity.move_storage.equalsIgnoreCase("1"))
                    {


                        alertDialog.show();
                    }
                    else

                    {


                        //dont show dialog

                        //alertDialog.show();


                    }

                    return true;
                }
            });

           /* if(!dmsActivity.in_action_mode_dms){

                holder.checkBoxDmsHierarcy.setVisibility(View.GONE);
                // Toast.makeText(context, "in action mode true ", Toast.LENGTH_SHORT).show();

            }

            else{


                holder.checkBoxDmsHierarcy.setVisibility(View.VISIBLE);
                holder.checkBoxDmsHierarcy.setChecked(false);

            }

            holder.checkBoxDmsHierarcy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if(isChecked){

                        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

                    }

                    else{

                        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

                    }

                }


            });*/



        }



    }

    @Override
    public int getItemCount() {

        return folderName.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoldername, tvFolderSlid, tvFullFoldername,tvCheckBlank;
        ImageView ivFolder;
        LinearLayout lv;
        CheckBox checkBoxDmsHierarcy;
        CardView cardView;

        DmsActivity dmsActivity;

        public ViewHolder(View itemView, DmsActivity dmsActivity) {
            super(itemView);

            this.dmsActivity = dmsActivity;

            cardView = itemView.findViewById(R.id.cv_dms_hierarcy_folder_list);

            tvFoldername = itemView.findViewById(R.id.tv_fileview_foldername);
            ivFolder = itemView.findViewById(R.id.imageview_fileview_foldericon);
            lv = itemView.findViewById(R.id.linearlayoutclickedinlist);
            tvFolderSlid = itemView.findViewById(R.id.tv_fileview_folder_slid);
            tvFullFoldername = itemView.findViewById(R.id.tv_fileview_folder_fullfoldername);
            checkBoxDmsHierarcy = itemView.findViewById(R.id.checkbox_dms_hierarcy);
            tvCheckBlank = itemView.findViewById(R.id.tv_fileview_folder_checkblank);


         /*   if(!dmsActivity.in_action_mode){

                checkBoxDmsHierarcy.setVisibility(View.GONE);

                //cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

            }

            else{


            checkBoxDmsHierarcy.setVisibility(View.VISIBLE);
                // holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
               checkBoxDmsHierarcy.setChecked(false);
              // notifyDataSetChanged();
            }
*/

        /*    checkBoxDmsHierarcy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){

                       cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                    }

              else{

                       cardView.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

                    }

                }
            });*/


        }




    }

    @Override
    public int getItemViewType (int position) {
        if (isSwitchView){
            return LIST_ITEM;
        }else{
            return GRID_ITEM;
        }
    }

    public boolean toggleItemViewType () {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    void checkBlank(final String slid, final ViewHolder holder){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.CHECK_BLANK_FOLDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String blank = jsonObject.getString("blank");

                    if(blank.equalsIgnoreCase("0")){

                        holder.ivFolder.setImageResource(R.drawable.ic_folder_open_blue_24dp);

                    }

                    else{


                        holder.ivFolder.setImageResource(R.drawable.ic_folder_light_blue_24dp);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){


            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<>();
                params.put("slid",slid);

                return params;
            }
        };


        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);



    }

    private static void disable(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disable((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

}



