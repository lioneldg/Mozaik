package com.freedroid.mozaik;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private final int[] imageId;
    private final String[] firstName;
    private final String[] lastName;

    public ImageAdapter(Context c, String[] firstName, String[] lastName, int[] imageId) {
        mContext = c;
        this.imageId = imageId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getCount() {
        return imageId.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float a4Width = size.x / 5.0f;
        float a4Height = (a4Width/21f*29.7f);

        if (convertView == null) {
            gridView = new View(mContext);
            assert inflater != null;
            gridView = inflater.inflate(R.layout.cell_layout, null);

            TextView textViewFirstName = gridView.findViewById(R.id.firstName);
            textViewFirstName.setText(firstName[position]);
            float heightFirstName = textViewFirstName.getTextSize();

            TextView textViewLastName = gridView.findViewById(R.id.lastName);
            textViewLastName.setText(lastName[position]);
            float heightLastName = textViewLastName.getTextSize();

            ImageView imageView = gridView.findViewById(R.id.grid_item_image);
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int)a4Width, (int)(a4Height - heightFirstName - heightLastName)));
            imageView.setImageResource(imageId[position]);
        }
        else {
            gridView = convertView;
        }
        return gridView;
    }
}