package me.vincevan.myremoteapp;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class HostListAdapter extends BaseAdapter {

    private TextView txtTitle;
    private TextView txtSubtitle;
    private Context mContext;
    private ArrayList mData;

    public HostListAdapter(Context context, Map<String, String> map) {
        this.mContext = context;
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            v = layoutInflater.inflate(R.layout.list_saved_host,null);

            txtTitle = v.findViewById(R.id.txtHostTitle);
            txtSubtitle = v.findViewById(R.id.txtHostIp);
        }

        Map.Entry<String,String> p = getItem(position);
        if (p!=null){
            txtTitle.setText(p.getKey());
            txtSubtitle.setText("IP: " + p.getValue());
        }

        return v;
    }
}
