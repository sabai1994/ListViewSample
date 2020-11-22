package com.example.listviewsample;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.listviewsample.Model.LanguageInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

public class LanguageAdapter extends BaseAdapter {
    ArrayList<LanguageInfo> infoArrayList;
    Context context;
    public LanguageAdapter(Context context, ArrayList<LanguageInfo> infos){
            this.context = context;
            this.infoArrayList = infos;
    }
    @Override
    public int getCount() {
        return infoArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return infoArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item,null);
        TextView txtLangName = v.findViewById(R.id.langName);
        TextView txtLandDate = v.findViewById(R.id.langDate);
        TextView txtLangDesc= v.findViewById(R.id.langDesc);
        txtLangName.setText(infoArrayList.get(i).getName());
        Date releaseDate = infoArrayList.get(i).getReleaseDate();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        txtLandDate.setText(df.format(releaseDate).toString());
        txtLangDesc.setText(infoArrayList.get(i).getDescription());
        txtLandDate.setTextColor(Color.RED);
        return v;
    }
}
