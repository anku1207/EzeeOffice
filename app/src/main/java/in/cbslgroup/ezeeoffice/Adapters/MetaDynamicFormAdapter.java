package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.R;

public class MetaDynamicFormAdapter extends RecyclerView.Adapter<MetaDynamicFormAdapter.ViewHolder> {


    Context context;
    List<String> spinnerConditionlist;
    List<String> spinnerMetadatalist;
    List<String> formPos;
    OnButtonClickListener onButtonClickListener;

    ViewHolder myViewHolder;
    int itemPos;




    public MetaDynamicFormAdapter(Context context, List<String> spinnerConditionlist, List<String> spinnerMetadatalist, List<String> formPos) {
        this.context = context;
        this.spinnerConditionlist = spinnerConditionlist;
        this.spinnerMetadatalist = spinnerMetadatalist;
        this.formPos = formPos;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {

        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.dms_nav_left_meta_form, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


         myViewHolder = holder;
         itemPos = position;

        ArrayAdapter<String> metaAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerMetadatalist) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }

        };




        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerConditionlist) {
            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }

        };

        metaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_metadata);
        holder.spMetadata.setAdapter(metaAdapter);
        holder.spMetadata.setSelection(metaAdapter.getCount());

        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spCondition.setAdapter(conditionAdapter);
        holder.spCondition.setSelection(conditionAdapter.getCount());


        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* formPos.remove(position);
                notifyItemRemoved(position);*/
               /* textMap.remove(position);
                metadataMap.remove(position);
                conditionMap.remove(position);*/

                onButtonClickListener.onRemoveButtonClick(position);

            }
        });





    }

    @Override
    public int getItemCount() {

        return formPos.size();

    }

    public interface OnButtonClickListener {

        void onRemoveButtonClick(int pos);
       // void onSaveButtonClick(ViewHolder viewHolder,int pos);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Spinner spCondition;
        Spinner spMetadata;
        TextView tvText;
        ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            spMetadata = itemView.findViewById(R.id.dyanamic_spinner_condition_nav_left_form_with_cancel);
            spCondition = itemView.findViewById(R.id.dyanamic_spinner_metadata_nav_left_form_with_cancel);
            tvText = itemView.findViewById(R.id.et_dynamic_text_search_nav_left_form_with_cancel);
            ivIcon = itemView.findViewById(R.id.iv_nav_left_del_btn);

        }
    }

    public void getEmptyFields(){



            if (myViewHolder.tvText.getText().toString().length() == 0) {

                Toast.makeText(context, "Please fill text"+itemPos, Toast.LENGTH_SHORT).show();

            }

            if (myViewHolder.spMetadata.getSelectedItem().toString().equalsIgnoreCase("Select metadata")) {
                Toast.makeText(context, "Please select Metadata"+itemPos, Toast.LENGTH_SHORT).show();
                ((TextView)myViewHolder.spMetadata.getSelectedView())
                        .setError("Select metadata"
                        );
            }

            if (myViewHolder.spCondition.getSelectedItem().toString().equalsIgnoreCase("Select condition")) {

                Toast.makeText(context, "Please select Condition"+itemPos, Toast.LENGTH_SHORT).show();
                ((TextView)myViewHolder.spCondition.getSelectedView())
                        .setError("Select condition"
                        );
            }

    }



}
