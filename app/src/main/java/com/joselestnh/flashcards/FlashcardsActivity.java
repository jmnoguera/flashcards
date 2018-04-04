package com.joselestnh.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class FlashcardsActivity extends AppCompatActivity {

    public static final String KEY_FC_IMAGES = "flashcards.fcdata.images";
    public static final String KEY_FC_WORDSA = "flashcards.fcdata.wordsa";
    public static final String KEY_FC_WORDSB = "flashcards.fcdata.wordsb";

    GridView gridView;
    private int images[];
    private String wordsA[];
    private String wordsB[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
          //lo de arriba peta por la actionbar o toolbar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        this.images = intent.getIntArrayExtra(FlashcardsActivity.KEY_FC_IMAGES);
        this.wordsA = intent.getStringArrayExtra(FlashcardsActivity.KEY_FC_WORDSA);
        this.wordsB= intent.getStringArrayExtra(FlashcardsActivity.KEY_FC_WORDSB);

        gridView = this.findViewById(R.id.flashcardsPool);
        FlashcardsGridAdapter adapter = new FlashcardsGridAdapter(FlashcardsActivity.this, this.images, this.wordsA, this.wordsB);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FlashcardsActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
