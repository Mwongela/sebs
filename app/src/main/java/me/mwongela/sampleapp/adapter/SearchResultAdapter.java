package me.mwongela.sampleapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.mwongela.sampleapp.R;
import me.mwongela.sampleapp.model.Offence;

public class SearchResultAdapter extends BaseAdapter {

    public ArrayList<Offence> data;
    private Context context;
    private LayoutInflater inflater;

    public SearchResultAdapter(Context context, ArrayList<Offence> data) {

        this.context = context;
        this.data = data;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Offence offence = data.get(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_search_result, parent, false);
        }

        TextView tvRegNo = (TextView) convertView.findViewById(R.id.vehicleRegNo);
        tvRegNo.setText(offence.getRegNo());

        TextView tvIdNo = (TextView) convertView.findViewById(R.id.idNo);
        tvIdNo.setText(offence.getIdNumber());

        TextView tvStatus = (TextView) convertView.findViewById(R.id.status);
        tvStatus.setText("Status: " + offence.getStatus());

        if(offence.getStatus().equals("PAID")) {
            tvStatus.setTextColor(Color.GREEN);
        } else {
            tvStatus.setTextColor(Color.RED);
        }

        return convertView;
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Offence> offences) {
        data.addAll(offences);
        notifyDataSetChanged();
    }
}