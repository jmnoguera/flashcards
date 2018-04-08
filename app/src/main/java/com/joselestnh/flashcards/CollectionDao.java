package com.joselestnh.flashcards;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Joseles on 05/04/2018.
 */

@Dao
public interface CollectionDao {

    @Query("SELECT * FROM Collection")
    List<Collection> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCollection(Collection... collections);

    @Delete
    public void delete(Collection collection);
}
