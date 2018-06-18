package cybertech.childguide;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 9/23/2017.
 */

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

public class BrowserAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<BrowserModel> storage;

    public BrowserAdapter(Context context, List<BrowserModel> storage) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.storage = storage;
    }

    @Override
    public int getCount() {
        return storage.size();
    }

    @Override
    public BrowserModel getItem(int position) {
        return storage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mViewHolder holder;
        if (convertView == null) {
            holder = new mViewHolder();
            convertView = layoutInflater.inflate(R.layout.browserrowxml, parent, false);

            holder.icon = (ImageView) convertView.findViewById(R.id.searchImageView);
            holder.searchWtv = (TextView) convertView.findViewById(R.id.searchWordsView);
            holder.searchURltv = (TextView) convertView.findViewById(R.id.searchUrlView);
            holder.searchDate = (TextView) convertView.findViewById(R.id.searchDateView);
            convertView.setTag(holder);
        } else {
            holder = (mViewHolder) convertView.getTag();
        }
        ColorGenerator generator = ColorGenerator.DEFAULT;
        int mycolor = generator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();
        TextDrawable drawable = builder.build(storage.get(position).getSearchWord().substring(0, 1).toUpperCase(), mycolor);

        //holder.icon.setImageBitmap(storage.get(position).getSearchIcon());
        holder.searchWtv.setText(storage.get(position).getSearchWord());
        holder.searchURltv.setText(storage.get(position).getSearchSite());
        holder.searchDate.setText(storage.get(position).getSearchDate());
        holder.icon.setImageDrawable(drawable);
        //new DownloadImageTask(holder.icon).execute(storage.get(position).getSearchIconURL());
        return convertView;
    }

    class mViewHolder {
        ImageView icon;
        TextView searchWtv;
        TextView searchURltv;
        TextView searchDate;

    }

}
