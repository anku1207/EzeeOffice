package in.cbslgroup.ezeeoffice.Chip;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import in.cbslgroup.ezeeoffice.R;

public abstract class TokenFilterAdapter extends ArrayAdapter<User> {

    private static final Object NO_DATA_FOUND = "No Data Found" ;
    private Filter filter;
    private List<User> originalObjects;
    private ArrayList<User> sourceObjects;


    public TokenFilterAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull User[] objects) {
        this(context, resource, textViewResourceId, new ArrayList<User>(Arrays.asList(objects)));
    }

    public TokenFilterAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        this(context, resource, 0, objects);
    }

    public TokenFilterAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<User> objects) {
        super(context, resource, textViewResourceId, new ArrayList<User>(objects));
        this.originalObjects = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_user, parent, false);

        }


        User user = getItem(position);
        assert convertView != null;


        TextView textView = convertView.findViewById(R.id.name);

//        if(user.getUsername() == "No Data Found"){
//
//          textView.setEnabled(false);
//          textView.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View view) {
//
//                  Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
//
//              }
//          });
//
//        }


        textView.setText(user != null ? user.getUsername() : "");

        assert user != null;
        ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(user.getDrawableId());


        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        ((TokenFilterAdapter.AppFilter)getFilter()).setSourceObjects(this.originalObjects);
        super.notifyDataSetChanged();
    }


    @Override
    public void notifyDataSetInvalidated(){
        ((TokenFilterAdapter.AppFilter)getFilter()).setSourceObjects(this.originalObjects);
        super.notifyDataSetInvalidated();
    }

    @Override
    public boolean isEnabled(int position) {


        Log.e("Enabled", String.valueOf(TokenFilterAdapter.this.getItem(position).getId()!= Long.valueOf(007)));
        Log.e("Enabled Pos", String.valueOf(position));
        Log.e("Enabled Username",  String.valueOf((TokenFilterAdapter.this.getItem(position).getId())));
        return (TokenFilterAdapter.this.getItem(position).getId()!= Long.valueOf(007));

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

//    @Override
//    public int getCount() {
//        return sourceObjects.size();
//    }


    //    @Override
//    public boolean areAllItemsEnabled(){
//        return true;
//    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new TokenFilterAdapter.AppFilter(originalObjects);
        return filter;
    }

    abstract protected boolean keepObject(User obj, String mask);

    private class AppFilter extends Filter {


       // private ArrayList<User> sourceObjects;

        public AppFilter(List<User> objects) {

            setSourceObjects(objects);
        }

        public void setSourceObjects(List<User> objects) {
            synchronized (this) {
                sourceObjects = new ArrayList<User>(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            FilterResults result = new FilterResults();
           // notifyDataSetChanged();
            if (chars != null && chars.length() > 0) {
                String mask = chars.toString();
                ArrayList<User> keptObjects = new ArrayList<User>();

                for (User object :sourceObjects) {

                    if (keepObject(object, mask))
                        keptObjects.add(object);
                }
                result.count = keptObjects.size();
                result.values = keptObjects;
            } else {
                // add all objects
                result.values = originalObjects;
                result.count = originalObjects.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results.count > 0) {

                TokenFilterAdapter.this.addAll((Collection) results.values);
                notifyDataSetChanged();

//                List<User> filtered = (ArrayList<User>) results.values;
//                TokenFilterAdapter.this.notifyDataSetChanged();
//                TokenFilterAdapter.this.clear();
//                if (filtered != null) {
//
//                    for (int i = 0; i < filtered.size(); i++)
//                        TokenFilterAdapter.this.add(filtered.get(i));
//                }
//
//                TokenFilterAdapter.this.notifyDataSetInvalidated();

            } else {


                //notifyDataSetInvalidated();

               // sourceObjects.clear();
                User user = new User();
                user.setUsername("No Data Found");
                user.setId(007L);
                user.setEnabled(false);
                TokenFilterAdapter.this.add(user);
                notifyDataSetChanged();
            }


        }
    }




}
