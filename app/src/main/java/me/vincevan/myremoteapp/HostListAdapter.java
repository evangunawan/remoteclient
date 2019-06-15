package me.vincevan.myremoteapp;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.vincevan.myremoteapp.model.SavedHostItem;

public class HostListAdapter extends ArrayAdapter<SavedHostItem> {
    private List<SavedHostItem> itemList;
    private Context context;

    public HostListAdapter(Context context, List<SavedHostItem> objects) {
        super(context,0,objects);
        this.context = context;
        this.itemList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final SavedHostItem item = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_saved_host,parent,false);

        }

        TextView hostName = convertView.findViewById(R.id.txtHostTitle);
        TextView hostAddress = convertView.findViewById(R.id.txtHostIp);

        hostName.setText(item.getHostName());
        hostAddress.setText("IP: " + item.getHostAddress());

        return convertView;

    }
}
