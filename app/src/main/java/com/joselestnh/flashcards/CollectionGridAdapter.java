package com.joselestnh.flashcards;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joseles on 03/04/2018.
 */

public class CollectionGridAdapter extends BaseAdapter {

    public final static int CELL_WIDTH = 180;
    public final static int CELL_HEIGHT = 180;

    //estructura separada (?)
    private List<Collection> collectionList;

    private Context context;
    private LayoutInflater inflater;

    public CollectionGridAdapter(Context context, List<Collection> collection){
        this.context = context;
        this.collectionList = collection;
    }

    @Override
    public int getCount() {
        return collectionList.size();
    }

    @Override
    public Object getItem(int position) {
        return collectionList.get(position);
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
        TextView name = gridView.findViewById(R.id.collectionName);

        byte[] imageBytes = this.collectionList.get(position).getImage();
        image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length));
        name.setText(this.collectionList.get(position).getName());

        return gridView;
    }
}
