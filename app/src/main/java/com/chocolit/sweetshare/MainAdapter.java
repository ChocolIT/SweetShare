package com.chocolit.sweetshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] categoryName;
    private int[] numberImage;

    public MainAdapter(Context c, String[] categoryName, int[] numberImage){
        context = c;
        this.categoryName = categoryName;
        this.numberImage = numberImage;
    }

    @Override
    public int getCount() {
        return categoryName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.categoryNameText);
        imageView.setBackgroundResource(numberImage[position]);

       textView.setText(categoryName[position]);
        return convertView;
    }
}
