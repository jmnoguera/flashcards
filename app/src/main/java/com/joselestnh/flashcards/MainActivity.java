package com.joselestnh.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    int images[] = {R.drawable.phaceholder};
    String descriptions[] = {"Placeholder"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cargar de donde sea los datos de las colecciones

        gridView = this.findViewById(R.id.collectionsPool);
        CollectionGridAdapter adapter = new CollectionGridAdapter(MainActivity.this, images, descriptions);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Llega", Toast.LENGTH_SHORT).show();

                openFlashcardsPool(position);
            }
        });

        //por defeto en el layout con +
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        //cambiar por recuperar datos de la db
        intent.putExtra(FlashcardsActivity.KEY_FC_IMAGES,this.images);
        intent.putExtra(FlashcardsActivity.KEY_FC_WORDSA,this.descriptions);
        intent.putExtra(FlashcardsActivity.KEY_FC_WORDSB,this.descriptions);
        //putextra o bundle

        startActivity(intent);

    }

}
