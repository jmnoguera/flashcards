package com.joselestnh.flashcards;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;

public class FlashcardForm extends AppCompatActivity {


    private final static int RESULT_LOAD_IMAGE = 1;
    private final static int GALLERY_PERMISSION = 10;

    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_form);

//        findViewById(R.id.flashcardImageForm).setVisibility(View.GONE);
//        findViewById(R.id.flashcardWordAForm).setVisibility(View.GONE);
//        findViewById(R.id.flashcardWordBForm).setVisibility(View.GONE);

        ImageButton imageButton = findViewById(R.id.flashcardImageForm);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        Spinner spinner = findViewById(R.id.flashcardSpinnerForm);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.flashcardType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case Flashcard.TRANSLATE:
                        findViewById(R.id.flashcardImageForm).setVisibility(View.GONE);
                        findViewById(R.id.flashcardWordAForm).setVisibility(View.VISIBLE);
                        findViewById(R.id.flashcardWordBForm).setVisibility(View.VISIBLE);
//                        findViewById(R.id.flashcardFormLayout).invalidate();
                        break;

                    case Flashcard.RELATE:
                        findViewById(R.id.flashcardImageForm).setVisibility(View.VISIBLE);
                        findViewById(R.id.flashcardWordAForm).setVisibility(View.VISIBLE);
                        findViewById(R.id.flashcardWordBForm).setVisibility(View.GONE);
//                        findViewById(R.id.flashcardFormLayout).invalidate();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = findViewById(R.id.flashcardAddButtonForm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();


                //cambiar que devuelve
                String flashcardName = ((EditText)findViewById(R.id.flashcardNameForm)).getText().toString();
                int flashcardType = ((Spinner)findViewById(R.id.flashcardSpinnerForm)).getSelectedItemPosition();
                //int flashcardType = ((EditText)findViewById(R.id.collectionDescriptionForm)).getText().toString();
                byte[] collectionImage = new byte[0];
                String flashcardWordA = ((EditText)findViewById(R.id.flashcardWordAForm)).getText().toString();
                String flashcardWordB = "";
                switch (flashcardType){
                    case Flashcard.TRANSLATE:
                        flashcardWordB = ((EditText)findViewById(R.id.flashcardWordBForm)).getText().toString();
                        break;
                    case Flashcard.RELATE:
                        Drawable drawable = ((ImageButton)findViewById(R.id.flashcardImageForm)).getDrawable();
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                        //resize
                        bitmap = BitmapUtilities.fitBitmap(bitmap, CollectionGridAdapter.CELL_WIDTH,
                                CollectionGridAdapter.CELL_HEIGHT, true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        collectionImage = stream.toByteArray();
                        break;
                }


                Bundle bundle = new Bundle();
                bundle.putString(Flashcard.KEY_FC_NAME,flashcardName);
                bundle.putInt(Flashcard.KEY_FC_TYPE,flashcardType);
                bundle.putString(Flashcard.KEY_FC_WORDA,flashcardWordA);
                bundle.putString(Flashcard.KEY_FC_WORDB,flashcardWordB);
                bundle.putByteArray(Flashcard.KEY_FC_IMAGE,collectionImage);
                intent.putExtras(bundle);

                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_PERMISSION);
            }else{
                ImageButton imageButton = findViewById(R.id.flashcardImageForm);
                imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case GALLERY_PERMISSION:
                ImageButton imageButton = findViewById(R.id.flashcardImageForm);
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }else{
                    imageButton.setImageResource(R.drawable.placeholder);
                }
                break;
        }

    }
}
