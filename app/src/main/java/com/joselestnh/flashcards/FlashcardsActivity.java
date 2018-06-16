package com.joselestnh.flashcards;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
    FlashcardsGridAdapter adapter;
    List<Flashcard> flashcardList;
    private String collectionName;
    private List<Integer> selectedFlashcards = new ArrayList<>();
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
        reloadFlashcards();

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

        gridView = this.findViewById(R.id.flashcardsPool);
        adapter = new FlashcardsGridAdapter(FlashcardsActivity.this, flashcardList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(FlashcardsActivity.this, "Funciona", Toast.LENGTH_SHORT).show();
            }
        });

        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (selectedFlashcards.contains(position)){
                    selectedFlashcards.remove(((Integer)position));
                    View view = gridView.getChildAt(position);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }else{
                    selectedFlashcards.add(position);
                    View view = gridView.getChildAt(position);
                    view.setBackgroundColor(Color.LTGRAY);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_flashcards, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.flashcard_delete:
                        List<Flashcard> flashcardsToDelete = new ArrayList<>();
                        for(Integer flashcardIndex : selectedFlashcards){
                            flashcardsToDelete.add(flashcardList.get(flashcardIndex));
                        }
                        final Flashcard[] flashcardsToDeleteArray = flashcardsToDelete.toArray(new Flashcard[0]);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.getDb().flashcardDao().deleteFlashcards(flashcardsToDeleteArray);
                            }
                        }).start();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectedFlashcards.clear();
                int size = gridView.getChildCount();
                for (int i=0;i<size;i++){
                    gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                reloadFlashcards();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadFlashcards();

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

            final Flashcard flashcard = new Flashcard(this.collectionName, flashcardName,
                    flashcardType, flashcardWordA, flashcardWordB, flashcardImage);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.getDb().flashcardDao().insertFlashcard(flashcard);
                }
            }).start();
            reloadFlashcards();

        }
    }

    private void reloadFlashcards(){
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
            if (adapter != null){
                adapter.updateData(flashcardList);
            }
        } catch (Exception e){
            flashcardList = new ArrayList<>();
        }
        executorService.shutdown();
    }
}
