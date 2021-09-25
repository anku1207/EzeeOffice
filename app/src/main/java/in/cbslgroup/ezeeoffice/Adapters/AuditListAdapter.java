package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Interface.OnLoadMoreListener;
import in.cbslgroup.ezeeoffice.Model.AuditTrail;
import in.cbslgroup.ezeeoffice.R;


public class AuditListAdapter extends RecyclerView.Adapter<AuditListAdapter.ViewHolder>{

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Context context;
    private List<AuditTrail> auditTrailList;
    int displaySize;

    public static int lastPosition;

    public  static String sno;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;



    public AuditListAdapter(final Context context, List<AuditTrail> auditTrailList, RecyclerView recyclerView) {
        this.context = context;
        this.auditTrailList = auditTrailList;

    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_list, parent, false);
           return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        lastPosition = position;

        String Sno = auditTrailList.get(position).getSno();
       // Log.e("serial no",Sno.substring(Sno.lastIndexOf(" ")+1));
        sno = Sno.substring(Sno.lastIndexOf(" ")+1);

        /*final Toast toast = Toast.makeText(context, position+"", Toast.LENGTH_SHORT);
        toast.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                toast.cancel();
                handler.removeCallbacks(null);
            }
        },50);
*/


        holder.tvUsername.setText(auditTrailList.get(position).getUsername());
        holder.tvSno.setText(auditTrailList.get(position).getSno());
        holder.tvIp.setText(auditTrailList.get(position).getIp());



        holder.tvActionPerformed.setText(auditTrailList.get(position).getActionPerformed());
        holder.tvRemarks.setText(auditTrailList.get(position).getRemarks());

        holder.tvType.setText(auditTrailList.get(position).getWhichList());


        String listtype = holder.tvType.getText().toString();




        if(listtype.equalsIgnoreCase("user")){

            holder.tvStartDate.setText(auditTrailList.get(position).getActionStartDate());
            holder.tvEndDate.setText(auditTrailList.get(position).getActionEndDate());


        }

        else if (listtype.equalsIgnoreCase("storage")){

            holder.tvStartDate.setText(auditTrailList.get(position).getActionStartDate());
           // holder.tvEndDate.setText(auditTrailList.get(position).getActionEndDate());
            holder.tvStartDateLabel.setText("Action DateTime");
            holder.llEndateLabel.setVisibility(View.GONE);


        }

     /*   Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.top_to_bottom_rv
                        : R.anim.bottom_to_top_rv);
        holder.itemView.startAnimation(animation);
        lastPosition = position;*/
    }



    @Override
    public int getItemCount() {


       // if(displaySize > auditTrailList.size())
           return auditTrailList.size();
          //return setItemCount(10);
       // else
     //  return displaySize;

    }


  /*  public int setItemCount( int count){


        return count;

    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSno ,tvUsername,tvIp,tvActionPerformed,tvStartDate,tvEndDate,tvRemarks,tvType,tvStartDateLabel;

        LinearLayout llEndateLabel;


        public ViewHolder(View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tv_audit_list_listtype);
            tvUsername = itemView.findViewById(R.id.tv_audit_list_username);
            tvActionPerformed = itemView.findViewById(R.id.tv_audit_list_actionperformed);
            tvStartDate = itemView.findViewById(R.id.tv_audit_list_actionstartdate);
            tvEndDate = itemView.findViewById(R.id.tv_audit_list_actionenddate);
            tvIp = itemView.findViewById(R.id.tv_audit_list_sytemip);
            tvRemarks = itemView.findViewById(R.id.tv_audit_list_remarks);
            tvSno = itemView.findViewById(R.id.tv_audit_list_sno);

            //label
            tvStartDateLabel  = itemView.findViewById(R.id.tv_audit_list_actionstartdate_label);
            llEndateLabel = itemView.findViewById(R.id.ll_audit_list_action_end_date);





        }
    }






    public void setDisplayCount(int numberOfEntries) {
        displaySize = numberOfEntries;
        notifyDataSetChanged();

    }



/*    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }*/

}
