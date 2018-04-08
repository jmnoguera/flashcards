package com.joselestnh.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FlashcardsActivity extends AppCompatActivity {


    private static int RESULT_FLASHCARD = 3;

    GridView gridView;
    List<Flashcard> flashcardList;
    private String collectionName;
//    private int images[];
//    private String wordsA[];
//    private String wordsB[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

          //lo de arriba peta por la actionbar o toolbar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(FlashcardsActivity.this , FlashcardForm.class);
                startActivityForResult(intent, RESULT_FLASHCARD);
            }
        });

        Intent intent = getIntent();
        collectionName = intent.getStringExtra(Flashcard.KEY_FC_COLLECTION);
//        this.images = intent.getIntArrayExtra(Flashcard.KEY_FC_IMAGES);
//        this.wordsA = intent.getStringArrayExtra(Flashcard.KEY_FC_WORDSA);
//        this.wordsB= intent.getStringArrayExtra(Flashcard.KEY_FC_WORDSB);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //buscar en la db
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Flashcard>> result = executorService.submit(new Callable<List<Flashcard>>() {
            @Override
            public List<Flashcard> call() throws Exception {
                flashcardList = MainActivity.getDb().flashcardDao().getAllByCollection(collectionName);
                return flashcardList;
            }
        });
        try {
            flashcardList = result.get();
        } catch (Exception e){
            flashcardList = new ArrayList<>();
        }
        executorService.shutdown();

        gridView = this.findViewById(R.id.flashcardsPool);
        FlashcardsGridAdapter adapter = new FlashcardsGridAdapter(FlashcardsActivity.this, flashcardList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FlashcardsActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  RESULT_FLASHCARD && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();
            String flashcardName = bundle.getString(Flashcard.KEY_FC_NAME);
            int flashcardType = bundle.getInt(Flashcard.KEY_FC_TYPE);
            String flashcardWordA = bundle.getString(Flashcard.KEY_FC_WORDA);
            String flashcardWordB = bundle.getString(Flashcard.KEY_FC_WORDB);
            byte[] flashcardImage = bundle.getByteArray(Flashcard.KEY_FC_IMAGE);

            final Flashcard flashcard= new Flashcard(this.collectionName, flashcardName,
                    flashcardType, flashcardWordA, flashcardWordB, flashcardImage);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getDb().flashcardDao().insertFlashcard(flashcard);
                }
            }).start();

        }
    }
}
