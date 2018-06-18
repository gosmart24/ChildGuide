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

import java.util.ArrayList;

/**
 * Created by CyberTech on 6/4/2013.
 */
public class DefaultchildAdapter extends BaseAdapter {
    ArrayList<String> storage;
    ArrayList<String> childList;
    private LayoutInflater layoutInflater;

    public DefaultchildAdapter(Context context, ArrayList<String> storage, ArrayList<String> ChildIDstorage) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.storage = storage;
        this.childList = ChildIDstorage;
    }

    @Override
    public int getCount() {
        return storage.size();
    }

    @Override
    public String getItem(int position) {
        return storage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.choseidrow, parent, false);

            listViewHolder.tvName = (TextView) convertView.findViewById(R.id.dName);
            listViewHolder.tvChildID = (TextView) convertView.findViewById(R.id.dchildID);
            listViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.dIcon);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        ColorGenerator generator = ColorGenerator.DEFAULT;
        int mycolor = generator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();
        TextDrawable drawables = builder.build(storage.get(position).substring(0, 1).toUpperCase(), mycolor);

        listViewHolder.tvName.setText(storage.get(position).toUpperCase());
        listViewHolder.tvChildID.setText(childList.get(position));
        listViewHolder.ivIcon.setImageDrawable(drawables);

        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvChildID;
        ImageView ivIcon;
    }
}
