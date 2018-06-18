package cybertech.childguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 */
public class KeyLoggerAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<KeyLoggerModel> listStorage;

    public KeyLoggerAdapter(Context context, List<KeyLoggerModel> Storage) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = Storage;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public KeyLoggerModel getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.keyloggerxml, parent, false);

            listViewHolder.keyLoggerIcon = (ImageView) convertView.findViewById(R.id.keyloggerIcon);
            listViewHolder.keyLoggerEventTV = (TextView) convertView.findViewById(R.id.keyLoggerEvent);
            listViewHolder.keyLoggerDateTV = (TextView) convertView.findViewById(R.id.keyLoggerDate);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (locationHolder) convertView.getTag();
        }

        ColorGenerator generator = ColorGenerator.DEFAULT;
        int mycolor = generator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();
        TextDrawable drawable = builder.build(listStorage.get(position).getEventType().substring(0, 1).toUpperCase(), mycolor);


        listViewHolder.keyLoggerEventTV.setText(listStorage.get(position).getEventType());
        listViewHolder.keyLoggerDateTV.setText(listStorage.get(position).getEventDate());
        listViewHolder.keyLoggerIcon.setImageDrawable(drawable);
        /*if (listStorage.get(position).getEventType().equals("Search"))
            listViewHolder.keyLoggerIcon.setImageResource(R.mipmap.search);
        if (listStorage.get(position).getEventType().equals("Text"))
            listViewHolder.keyLoggerIcon.setImageResource(R.mipmap.text);
*/
        return convertView;
    }

    static class locationHolder {

        TextView keyLoggerEventTV;
        TextView keyLoggerDateTV;
        ImageView keyLoggerIcon;
    }
}
