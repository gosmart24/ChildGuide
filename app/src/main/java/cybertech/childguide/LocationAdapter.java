package cybertech.childguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 17/08/2017.
 */
public class LocationAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<LocationModel> listStorage;

    public LocationAdapter(Context context, List<LocationModel> Storage) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = Storage;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public LocationModel getItem(int position) {
        return listStorage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        locationHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new locationHolder();
            convertView = layoutInflater.inflate(R.layout.locationxml, parent, false);

            listViewHolder.textInListViewLatitude = (TextView) convertView.findViewById(R.id.tv_locationLatitude);
            listViewHolder.textInListViewLongitude = (TextView) convertView.findViewById(R.id.tv_locationLongitude);
            listViewHolder.textInListViewLocationDate = (TextView) convertView.findViewById(R.id.tv_locationDate);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (locationHolder) convertView.getTag();
        }
        listViewHolder.textInListViewLatitude.setText(listStorage.get(position).getChildLatitude());
        listViewHolder.textInListViewLongitude.setText(listStorage.get(position).getChildLongitude());
        listViewHolder.textInListViewLocationDate.setText(listStorage.get(position).getLocationDate());

        return convertView;
    }

    static class locationHolder {

        TextView textInListViewLatitude;
        TextView textInListViewLongitude;
        TextView textInListViewLocationDate;


    }
}
