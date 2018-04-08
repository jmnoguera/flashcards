package com.joselestnh.flashcards;

import android.app.Activity;
import android.graphics.Bitmap;

public class BitmapUtilities {

    //reduce the size used by the image
    public static Bitmap fitBitmap(Bitmap src, int cellWidth, int cellHeight, boolean filter){
        float proportion;

        if(src.getWidth() > src.getHeight()){
            proportion = (float) cellWidth  / src.getWidth();
        }else{
            proportion = (float) cellHeight / src.getHeight();
        }

        return Bitmap.createScaledBitmap(src, (int)(src.getWidth()*proportion),
                (int)(src.getHeight()*proportion),filter);
    }

}
