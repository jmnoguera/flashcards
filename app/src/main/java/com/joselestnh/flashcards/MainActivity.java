package com.joselestnh.flashcards;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {

    private static int RESULT_COLLECTION = 2;

    GridView gridView;
    CollectionGridAdapter adapter;
    private List<Collection> collectionList;
    private static AppDatabase db;
    private List<Integer> selectedCollections = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,
                "FlashCards-DB").build();

        reloadCollections();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this , CollectionForm.class);
                startActivityForResult(intent, RESULT_COLLECTION);
            }
        });

        gridView = this.findViewById(R.id.collectionsPool);
        adapter = new CollectionGridAdapter(MainActivity.this, collectionList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openFlashcardsPool(position);
            }
        });
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (selectedCollections.contains(position)){
                    selectedCollections.remove((Integer) position);
                    View view = gridView.getChildAt(position);
                    view.setBackgroundColor(Color.TRANSPARENT);

                }else{
                    selectedCollections.add(position);
                    View view = gridView.getChildAt(position);
                    view.setBackgroundColor(Color.LTGRAY);
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_collections, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.collection_delete:
                        List<Collection> collectionsToDelete = new ArrayList<>();
                        for(Integer collectionIndex : selectedCollections){
                            collectionsToDelete.add(collectionList.get(collectionIndex));
                            //peta al borrar algo
                            //puede que cambien los valores mientras se borran :S
                        }
                        final Collection[] collectionsToDeleteArray = collectionsToDelete.toArray(new Collection[0]);
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                db.collectionDao().deleteCollections(collectionsToDeleteArray);
                            }
                        }).start();
                        reloadCollections();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectedCollections.clear();
                int size = gridView.getChildCount();
                for (int i=0;i<size;i++){
                    gridView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                reloadCollections();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadCollections();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  RESULT_COLLECTION && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();
            String collectionName = bundle.getString(Collection.KEY_COLLECTION_NAME);
            String collectionDescription = bundle.getString(Collection.KEY_COLLECTION_DESCRIPTION);
            byte[] collectionImage = bundle.getByteArray(Collection.KEY_COLLECTION_IMAGE);

            final Collection collection = new Collection(collectionName,collectionDescription,collectionImage);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.collectionDao().insertCollection(collection);
                }
            }).start();
            reloadCollections();

        }
    }

    private void reloadCollections(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Collection>> result = executorService.submit(new Callable<List<Collection>>() {
            @Override
            public List<Collection> call() throws Exception {
                collectionList = db.collectionDao().getAll();
                return collectionList;
            }
        });
        try {
            collectionList = result.get();
            if(adapter != null){
                adapter.updateData(collectionList);
            }

        } catch (Exception e){
            collectionList = new ArrayList<>();
        }
        executorService.shutdown();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //revisar argumentos que necesite: position,...
    public void openFlashcardsPool(int position){
        Intent intent = new Intent(this, FlashcardsActivity.class);
        //cambiar por recuperar datos de la db, pasar nombre de la coleccion
        intent.putExtra(Flashcard.KEY_FC_COLLECTION,collectionList.get(position).getName());
//        intent.putExtra(Flashcard.KEY_FC_IMAGES,this.collectionList.);
//        intent.putExtra(Flashcard.KEY_FC_WORDSA,this.descriptions);
//        intent.putExtra(Flashcard.KEY_FC_WORDSB,this.descriptions);
        //putextra o bundle
        startActivity(intent);

    }


    public static AppDatabase getDb() {
        return db;
    }

}
