package in.cbslgroup.ezeeoffice.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.cbslgroup.ezeeoffice.Model.CheckinFields;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.CustomDateTimePicker;

public class CheckinFieldAdapter extends RecyclerView.Adapter<CheckinFieldAdapter.ViewHolder> {


    List<CheckinFields> checkinFieldsList;
    Context context;
    CustomDateTimePicker dateTimePicker;

    public CheckinFieldAdapter(List<CheckinFields> checkinFieldsList, Context context) {
        this.checkinFieldsList = checkinFieldsList;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckinFieldAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upload_describes_dynamic_tv_et, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckinFieldAdapter.ViewHolder viewHolder, int i) {

        CheckinFields item = checkinFieldsList.get(i);
        String length = item.getSize();
        String datatype = item.getDatatype();
        String mandatory = item.getMandatorystatus();
        String value = item.getValue();
        String metaname = item.getFieldname();


        // final EditText viewHolder.etDescribe =  viewHolder.etDescribe;
        // final TextView tv_star =  viewHolder.tvStar;

        viewHolder.tvDataType.setText(datatype);
        viewHolder.tvMetaname.setText(metaname);

        if (item.getMandatorystatus().equalsIgnoreCase("yes")) {

            viewHolder.tvStar.setVisibility(View.VISIBLE);
        } else {

            viewHolder.tvStar.setVisibility(View.INVISIBLE);
        }

        if (datatype.equals("varchar")) {

            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_TEXT);
            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // viewHolder.etDescribe.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});

        } else if (datatype.equals("bit")) {


            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_NUMBER);
            viewHolder.etDescribe.setHint("Data should " + length + " only");

            // viewHolder.etDescribe.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});

        } else if (datatype.equals("float")) {


            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // viewHolder.etDescribe.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
        } else if (datatype.equals("Int")) {


            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // viewHolder.etDescribe.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
        } else if (datatype.equals("BigInt")) {


            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "digits.");
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }

        } else if (datatype.equals("TEXT")) {

            viewHolder.etDescribe.setHint("");

        } else if (datatype.equals("char")) {


            //ivDatePicker.setVisibility(View.GONE);
            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
            // viewHolder.etDescribe.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
            //  viewHolder.etDescribe.setInputType(InputType.);
            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }


        } else if (datatype.equals("datetime")) {


            viewHolder.etDescribe.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
            viewHolder.etDescribe.setHint("yyyy-mm-dd");
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_DATETIME);
            // viewHolder.etDescribe.setKeyListener(null);
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_DATETIME);
            viewHolder.etDescribe.setFocusable(false);
            viewHolder.etDescribe.setFocusableInTouchMode(false);
            viewHolder.etDescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {


                    dateTimePicker = new CustomDateTimePicker((AppCompatActivity) context, new CustomDateTimePicker.ICustomDateTimeListener() {
                        @Override
                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {

                            viewHolder.etDescribe.setText("");

                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));

                            String Month = "";

                            if (monthNumber + 1 <= 9) {

                                Month = "0" + (monthNumber + 1);

                            } else {

                                Month = String.valueOf(monthNumber + 1);
                            }


                            String secounds = String.valueOf(sec);
                            String minutes = String.valueOf(min);

                            if (min < 10) {
                                minutes = "0" + minutes;

                            }

                            if (sec < 10) {

                                secounds = "0" + secounds;
                            }


                            viewHolder.etDescribe.setText(year
                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                    + " " + hour12 + ":" + minutes + ":" + secounds
                            );


                        }

                        @Override
                        public void onCancel() {

                            dateTimePicker.dismissDialog();

                        }
                    });

                    dateTimePicker.set24HourFormat(true);
                    dateTimePicker.setDate(Calendar.getInstance());
                    dateTimePicker.showDialog();


                }
            });


        } else if (datatype.equals("DATETIME")) {

            viewHolder.etDescribe.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_date_range_black_24dp, 0);
            viewHolder.etDescribe.setHint("yyyy-mm-dd");
            // viewHolder.etDescribe.setKeyListener(null);
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_DATETIME);
            viewHolder.etDescribe.setFocusable(false);
            viewHolder.etDescribe.setFocusableInTouchMode(false);
            // viewHolder.etDescribe.setKeyListener(null);
            viewHolder.etDescribe.setInputType(InputType.TYPE_CLASS_DATETIME);
            viewHolder.etDescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dateTimePicker = new CustomDateTimePicker((AppCompatActivity) context, new CustomDateTimePicker.ICustomDateTimeListener() {
                        @Override
                        public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {

                            viewHolder.etDescribe.setText("");

                            Log.e("date_picker", "year" + year + "\n" + "month" + (monthNumber + 1) + "\n" + "day" + calendarSelected.get(Calendar.DAY_OF_MONTH));

                            String Month = "";

                            if (monthNumber + 1 <= 9) {

                                Month = "0" + (monthNumber + 1);

                            } else {

                                Month = String.valueOf(monthNumber + 1);
                            }

                            String secounds = String.valueOf(sec);
                            String minutes = String.valueOf(min);

                            if (min < 10) {
                                minutes = "0" + minutes;

                            }

                            if (sec < 10) {

                                secounds = "0" + secounds;
                            }

                            viewHolder.etDescribe.setText(year
                                    + "-" + Month + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                    + " " + hour12 + ":" + minutes + ":" + secounds
                            );


                        }

                        @Override
                        public void onCancel() {

                            dateTimePicker.dismissDialog();

                        }
                    });

                    dateTimePicker.set24HourFormat(true);
                    dateTimePicker.setDate(Calendar.getInstance());
                    dateTimePicker.showDialog();


                }
            });

        } else {

            //ivDatePicker.setVisibility(View.GONE);
            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");
            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }
            // viewHolder.etDescribe.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Integer.parseInt(length))});
        }


        if (mandatory.equals("Yes")) {


            viewHolder.tvStar.setVisibility(View.VISIBLE);


        } else {
            viewHolder.tvStar.setVisibility(View.GONE);

        }

        //viewHolder.etDescribe = rowView.findViewById(R.id.et_upload_describe);
        if (value.equalsIgnoreCase("null")) {

            viewHolder.etDescribe.setHint("Data length shouldn't Exceed from" + " " + length + " " + "characters.");

            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }


        } else {


            viewHolder.etDescribe.setText(value);

            if (length.equals("")) {


            } else {

                viewHolder.etDescribe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(length))});

            }


        }


    }

    @Override
    public int getItemCount() {
        return checkinFieldsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText etDescribe;
        TextView tvStar;
        TextView tvMetaname;
        TextView tvDataType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            etDescribe = itemView.findViewById(R.id.et_upload_describe);
            tvStar = itemView.findViewById(R.id.tv_upload_describes_mandatory);
            tvMetaname = itemView.findViewById(R.id.tv_upload_describe_metaname);
            tvDataType = itemView.findViewById(R.id.tv_upload_describe_datatype);


        }
    }
}

