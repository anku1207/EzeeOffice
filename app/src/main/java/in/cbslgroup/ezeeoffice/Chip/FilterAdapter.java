package in.cbslgroup.ezeeoffice.Chip;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import in.cbslgroup.ezeeoffice.R;


public class FilterAdapter extends FilteredArrayAdapter<User> {


    public FilterAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);


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
        ((TextView) convertView.findViewById(R.id.name)).setText(user != null ? user.getUsername() : "");

        assert user != null;
        ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(user.getDrawableId());


        return convertView;
    }


    @Override
    protected boolean keepObject(User obj, String mask) {
        mask = mask.toLowerCase();
        return obj.getUsername().toLowerCase().contains(mask);
    }

}
