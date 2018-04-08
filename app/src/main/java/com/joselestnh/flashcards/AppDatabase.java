package com.joselestnh.flashcards;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Joseles on 05/04/2018.
 */

@Database(entities = {Collection.class, Flashcard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CollectionDao collectionDao();
    public abstract FlashcardDao flashcardDao();
}
