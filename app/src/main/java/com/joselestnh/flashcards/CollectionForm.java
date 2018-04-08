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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

public class CollectionForm extends AppCompatActivity {

    private final static int RESULT_LOAD_IMAGE = 1;
    private final static int GALLERY_PERMISSION = 10;

    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_form);

        ImageButton imageButton = findViewById(R.id.collectionImageForm);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });


        Button button = findViewById(R.id.collectionAddButtonForm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                String collectionName = ((EditText)findViewById(R.id.collectionNameForm)).getText().toString();
                String collectionDescription = ((EditText)findViewById(R.id.collectionDescriptionForm)).getText().toString();
                Drawable drawable = ((ImageButton)findViewById(R.id.collectionImageForm)).getDrawable();
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                //resize
                bitmap = BitmapUtilities.fitBitmap(bitmap, CollectionGridAdapter.CELL_WIDTH,
                        CollectionGridAdapter.CELL_HEIGHT, true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] collectionImage = stream.toByteArray();
//                Collection collection = new Collection(collectionName, collectionDescription, image);

                Bundle bundle = new Bundle();
                bundle.putString(Collection.KEY_COLLECTION_NAME,collectionName);
                bundle.putString(Collection.KEY_COLLECTION_DESCRIPTION,collectionDescription);
                bundle.putByteArray(Collection.KEY_COLLECTION_IMAGE,collectionImage);
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
                ImageButton imageButton = findViewById(R.id.collectionImageForm);
                imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
//            ImageButton imageButton = findViewById(R.id.collectionImageForm);
//            imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case GALLERY_PERMISSION:
                ImageButton imageButton = findViewById(R.id.collectionImageForm);
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }else{
                    imageButton.setImageResource(R.drawable.placeholder);
                }
                break;
        }

    }
}
