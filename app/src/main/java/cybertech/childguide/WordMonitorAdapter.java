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
 * Created by CyberTech on 6/4/2013.
 */
public class WordMonitorAdapter extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private List<wordMonitorModel> listStorage;

    public WordMonitorAdapter(Context context, List<wordMonitorModel> Storage) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = Storage;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public wordMonitorModel getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.monitorwordsxml, parent, false);

            listViewHolder.wordUsedTV = (TextView) convertView.findViewById(R.id.tv_wordUsed);
            listViewHolder.wordUsedInTV = (TextView) convertView.findViewById(R.id.tv_wordUsedIn);
            listViewHolder.wordUsedDateTV = (TextView) convertView.findViewById(R.id.tvwordUsedDate);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (locationHolder) convertView.getTag();
        }
        listViewHolder.wordUsedTV.setText(listStorage.get(position).getWordUsed());
        listViewHolder.wordUsedInTV.setText(listStorage.get(position).getWordUsedIn());
        listViewHolder.wordUsedDateTV.setText(listStorage.get(position).getWordUsedDate());

        return convertView;
    }

    static class locationHolder {

        TextView wordUsedTV;
        TextView wordUsedInTV;
        TextView wordUsedDateTV;


    }
}
