package com.joselestnh.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Joseles on 03/04/2018.
 */

public class FlashcardsGridAdapter extends BaseAdapter {

    //clase que defina las propiedades (?)
    private int backgrounds[];
    private String wordsA[];
    private String wordsB[];
    private Context context;
    private LayoutInflater inflater;

    public FlashcardsGridAdapter(Context context, int backgrounds[],
                                 String wordsA[], String wordsB[]){
        this.context = context;
        this.backgrounds = backgrounds;
        this.wordsA = wordsA;
        this.wordsB = wordsB;
    }

    @Override
    public int getCount() {
        return wordsA.length;
    }

    @Override
    public Object getItem(int position) {
        return wordsA[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (convertView == null){
            this.inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.flashcards_layout, null);
        }

        ImageView background = gridView.findViewById(R.id.flashcardBackground);
        TextView wordA = gridView.findViewById(R.id.flashcardWord);

        background.setImageResource(this.backgrounds[position]);
        wordA.setText(this.wordsA[position]);

        return gridView;
    }
}
