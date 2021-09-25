package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Search.MetaDataSearchActivity;
import in.cbslgroup.ezeeoffice.R;

public class FreqQueryAdapter extends RecyclerView.Adapter<FreqQueryAdapter.ViewHolder> {

    Context context;
    List<String> list;


    public FreqQueryAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.freq_query_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String freq = list.get(position);
        holder.tvQuery.setText(freq);

        holder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,MetaDataSearchActivity.class);
                intent.putExtra("queryname",holder.tvQuery.getText());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuery;
        LinearLayout llmain;

        public ViewHolder(View itemView) {
            super(itemView);

            tvQuery = itemView.findViewById(R.id.tv_freq_query_query);
            llmain = itemView.findViewById(R.id.ll_freq_query_query);

        }


    }
}
