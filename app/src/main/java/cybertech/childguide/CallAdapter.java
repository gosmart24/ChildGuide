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
 * Created by CyberTech on 9/23/2017.
 */
public class CallAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<CallModel> callstorage;

    public CallAdapter(Context context, List<CallModel> customizedListView) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        callstorage = customizedListView;
    }

    @Override
    public int getCount() {
        return callstorage.size();
    }

    @Override
    public CallModel getItem(int position) {
        return callstorage.get(position);
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
            convertView = layoutInflater.inflate(R.layout.callrowxml, parent, false);

            listViewHolder.callerNametv = (TextView) convertView.findViewById(R.id.callerName);
            listViewHolder.callerNotv = (TextView) convertView.findViewById(R.id.callerNo);
            listViewHolder.callerDate = (TextView) convertView.findViewById(R.id.callDate);
            listViewHolder.callIconIMG = (ImageView) convertView.findViewById(R.id.callIcon);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        ColorGenerator generator = ColorGenerator.DEFAULT;
        int mycolor = generator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();
        TextDrawable drawables = builder.build(callstorage.get(position).getCallName().substring(0, 1).toUpperCase(), mycolor);

        listViewHolder.callerNametv.setText(callstorage.get(position).getCallName());
        listViewHolder.callerNotv.setText(callstorage.get(position).getCallNo());
        listViewHolder.callerDate.setText(callstorage.get(position).getCallDate());
        switch (callstorage.get(position).getCallType()) {
            case "Outgoing":
                if (Integer.parseInt(callstorage.get(position).getCallDuration()) <= 0) {
                    // setting image to D.
                    TextDrawable drawable = builder.build("D".toUpperCase(), mycolor);
                    listViewHolder.callIconIMG.setImageDrawable(drawable);
                } else {
                    // setting image to O.
                    TextDrawable drawable = builder.build("O".toUpperCase(), mycolor);
                    listViewHolder.callIconIMG.setImageDrawable(drawable);
                }
                break;
            case "Received":
                // setting image to R.
                TextDrawable drawable = builder.build("R".toUpperCase(), mycolor);
                listViewHolder.callIconIMG.setImageDrawable(drawable);
                break;
            case "Missed":
                // setting image to M.
                TextDrawable drawablem = builder.build("M".toUpperCase(), mycolor);
                listViewHolder.callIconIMG.setImageDrawable(drawablem);
                break;
        }
        return convertView;
    }

    static class ViewHolder {

        TextView callerDate;
        TextView callerNametv;
        TextView callerNotv;
        ImageView callIconIMG;
    }

}
