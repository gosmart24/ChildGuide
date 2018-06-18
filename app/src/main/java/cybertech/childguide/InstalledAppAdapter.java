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
public class InstalledAppAdapter extends BaseAdapter {

    ImageView imageView;
    private LayoutInflater layoutInflater;
    private List<AppList> listStorage;

    public InstalledAppAdapter(Context context, List<AppList> customizedListView) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public AppList getItem(int position) {
        return listStorage.get(position);
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
            convertView = layoutInflater.inflate(R.layout.installedappsxml, parent, false);

            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.tv_appNameXML);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.icon_XML);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        ColorGenerator generator = ColorGenerator.DEFAULT;
        int mycolor = generator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();
        TextDrawable drawable = builder.build(listStorage.get(position).getName().substring(0, 1).toUpperCase(), mycolor);

        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageDrawable(drawable);
        //listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
        //new DownloadImageTask(listViewHolder.imageInListView).execute(listStorage.get(position).getImag());

        return convertView;
    }

    static class ViewHolder {

        TextView textInListView;
        ImageView imageInListView;
    }

}
