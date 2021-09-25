package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.cbslgroup.ezeeoffice.Interface.AddMoreCashVoucherListener;
import in.cbslgroup.ezeeoffice.Model.CashVocherAddMore;
import in.cbslgroup.ezeeoffice.R;


public class CashVocherAddMoreAdapter extends RecyclerView.Adapter<CashVocherAddMoreAdapter.ViewHolder> {

    List<CashVocherAddMore> cashVocherAddMoreList;
    Context context;
    AddMoreCashVoucherListener listener;

    List<CashVocherAddMore> enteredDataList = new ArrayList<>();


    public CashVocherAddMoreAdapter(List<CashVocherAddMore> cashVocherAddMoreList, Context context, AddMoreCashVoucherListener listener) {
        this.cashVocherAddMoreList = cashVocherAddMoreList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public CashVocherAddMoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_voucher_add_more_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CashVocherAddMoreAdapter.ViewHolder holder, final int position) {



    }

    @Override
    public int getItemCount() {
        return cashVocherAddMoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageButton ibAdd;
        EditText etRupee,etPaisa;



        public ViewHolder(View itemView) {
            super(itemView);

            ibAdd = itemView.findViewById(R.id.ib_intiateworkflow_remove);
            etRupee = itemView.findViewById(R.id.et_intiateworkflow_rupee_dynamic);
            etPaisa = itemView.findViewById(R.id.et_intiateworkflow_paisa_dynamic);
        }
    }


    //get all list data
   public List<CashVocherAddMore> getValues(){

        List<CashVocherAddMore> list = new ArrayList<>();

        for(int i =0 ; i<enteredDataList.size();i++){

            list.add(new CashVocherAddMore(enteredDataList.get(i).getRupee(),enteredDataList.get(i).getPaisa()));

        }

        return list;
    }


}
