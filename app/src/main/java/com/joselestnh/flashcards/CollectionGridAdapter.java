package com.joselestnh.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by Joseles on 03/04/2018.
 */

public class CollectionGridAdapter extends BaseAdapter {

    //estructura separada (?)
    private int images[];
    private String descriptions[];

    private Context context;
    private LayoutInflater inflater;

    public CollectionGridAdapter(Context context, int images[], String descriptions[]){
        this.context = context;
        this.images = images;
        this.descriptions = descriptions;
    }

    @Override
    public int getCount() {
        return descriptions.length;
    }

    @Override
    public Object getItem(int position) {
        return descriptions[position];
    }

    @Override
    public long getItemId(int position) {
        //tiene valor identificativo, al devolver la misma position se rechaza usarlo
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (convertView == null){
            this.inflater = (LayoutInflater) this.context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            gridView = this.inflater.inflate(R.layout.collection_layout,null);
        }

        ImageView image = gridView.findViewById(R.id.collectionImage);
        TextView description = gridView.findViewById(R.id.collectionDescription);

        image.setImageResource(this.images[position]);
        description.setText(this.descriptions[position]);

        return gridView;
    }
}
