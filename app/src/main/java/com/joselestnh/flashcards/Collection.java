package com.joselestnh.flashcards;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Joseles on 05/04/2018.
 */

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class Collection {

    public static final String KEY_COLLECTION_NAME = "collections.data.name";
    public static final String KEY_COLLECTION_DESCRIPTION = "collections.data.description";
    public static final String KEY_COLLECTION_IMAGE = "collections.data.image";

    @PrimaryKey(autoGenerate = true)
    private int cid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB ,name = "image")
    private byte[] image;

    public Collection(String name, String description, byte[] image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
