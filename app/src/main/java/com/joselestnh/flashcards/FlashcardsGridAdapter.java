package com.joselestnh.flashcards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Joseles on 03/04/2018.
 */

public class FlashcardsGridAdapter extends BaseAdapter {

    //clase que defina las propiedades (?)
    private List<Flashcard> flashcardList;
//    private int backgrounds[];
//    private String wordsA[];
//    private String wordsB[];
    private Context context;
    private LayoutInflater inflater;

    public FlashcardsGridAdapter(Context context, List<Flashcard> flashcardList){
        this.context = context;
//        this.backgrounds = backgrounds;
//        this.wordsA = wordsA;
//        this.wordsB = wordsB;
        this.flashcardList = flashcardList;
    }

    @Override
    public int getCount() {
        return flashcardList.size();
    }

    @Override
    public Object getItem(int position) {
        return flashcardList.get(position);
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


        TextView name = gridView.findViewById(R.id.flashcardName);
        name.setText(this.flashcardList.get(position).getName());
        ImageView background = gridView.findViewById(R.id.flashcardImage);
        TextView wordA = gridView.findViewById(R.id.flashcardWord);

        int flashcardType = flashcardList.get(position).getType();
        switch(flashcardType){
            case Flashcard.TRANSLATE:
                background.setBackgroundResource(R.color.veryLightGray);
                wordA.setText(this.flashcardList.get(position).getWordA());
                wordA.setVisibility(View.VISIBLE);
                break;

            case Flashcard.RELATE:
                byte[] imageBytes = this.flashcardList.get(position).getImage();
                background.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length));
                wordA.setVisibility(View.GONE);
                break;

        }

        //comprobar background plano y si se usa wordA o wordB
        return gridView;
    }

    public void updateData(List<Flashcard> flashcardList){
        this.flashcardList = flashcardList;
        notifyDataSetChanged();
    }
}
