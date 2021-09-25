package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Interface.OnDropDownListClickListener;
import in.cbslgroup.ezeeoffice.Model.SpinnerDropDown;
import in.cbslgroup.ezeeoffice.R;


public class SpinnerDropDownAdapter extends RecyclerView.Adapter<SpinnerDropDownAdapter.ViewHolder> {

    List<SpinnerDropDown> spinnerDropDownList;
    Context context;

    OnDropDownListClickListener listener;

    public SpinnerDropDownAdapter(List<SpinnerDropDown> spinnerDropDownList, Context context, OnDropDownListClickListener listener) {
        this.spinnerDropDownList = spinnerDropDownList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpinnerDropDownAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_textview_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpinnerDropDownAdapter.ViewHolder holder, final int position) {


        holder.tvStoragename.setText(spinnerDropDownList.get(position).getStoragename());
        holder.tvSlid.setText(spinnerDropDownList.get(position).getSlid());

        if(position == spinnerDropDownList.size()-1)
        {
            holder.decorator.setVisibility(View.GONE);

        }

        else{

            holder.decorator.setVisibility(View.VISIBLE);

        }





        holder.llroot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickItem(v, position, holder.tvStoragename.getText().toString(), holder.tvSlid.getText().toString());

            }
        });

    }

    @Override
    public int getItemCount() {
        return spinnerDropDownList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llroot;
        TextView tvStoragename, tvSlid;
        View decorator;

        public ViewHolder(View itemView) {
            super(itemView);

            llroot = itemView.findViewById(R.id.ll_spinner_textview_layout);


            tvStoragename = itemView.findViewById(R.id.tv_spinner_dropdown_storagename);
            tvSlid = itemView.findViewById(R.id.tv_spinner_dropdown_slid);
            decorator = itemView.findViewById(R.id.view_spinner_dropdown_line);

        }
    }
}
