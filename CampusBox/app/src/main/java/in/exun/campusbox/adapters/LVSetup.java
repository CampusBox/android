package in.exun.campusbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.exun.campusbox.R;
import in.exun.campusbox.helper.AppConstants;

/**
 * Created by ayush on 03/05/17.
 */

public class LVSetup extends ArrayAdapter<String> {

    public LVSetup(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public LVSetup(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_row_add_post, null);
        }

        TextView bleName = (TextView) v.findViewById(R.id.text_name);
        ImageView icon = (ImageView) v.findViewById(R.id.static_icon);
        View line = v.findViewById(R.id.line);

        line.setVisibility(View.GONE);

        String p = getItem(position);

        if (p != null) {

            bleName.setText(p);

            switch (p){
                case AppConstants.ADD_IMAGE:
                    icon.setImageResource(R.drawable.ic_image);
                    break;
                case AppConstants.ADD_SOUND:
                    icon.setImageResource(R.drawable.ic_soundcloud);
                    break;
                case AppConstants.ADD_YOUTUBE:
                    icon.setImageResource(R.drawable.ic_youtube);
                    break;
                case AppConstants.ADD_VIMEO:
                    icon.setImageResource(R.drawable.ic_vimeo);
                    break;
                default:
                    icon.setVisibility(View.GONE);
                    break;
            }
        }

        return v;
    }

}