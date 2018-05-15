package com.joselestnh.flashcards;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Joseles on 05/04/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Collection.class,
                                    parentColumns = "name",
                                    childColumns = "collection",
                                    onDelete = CASCADE))
public class Flashcard {

//    public static final String KEY_FC_IMAGES = "flashcards.data.images";
//    public static final String KEY_FC_WORDSA = "flashcards.data.wordsa";
//    public static final String KEY_FC_WORDSB = "flashcards.data.wordsb";
    public static final String KEY_FC_COLLECTION = "flashcards.data.collection";
    public static final String KEY_FC_NAME = "flashcard.data.name";
    public static final String KEY_FC_TYPE = "flashcard.data.type";
    public static final String KEY_FC_WORDA = "flashcard.data.word_a";
    public static final String KEY_FC_WORDB = "flashcard.data.word_b";
    public static final String KEY_FC_IMAGE = "flashcard.data.image";


    public static final int TRANSLATE = 0;
    public static final int RELATE = 1;


    @PrimaryKey(autoGenerate = true)
    private int fid;

    @ColumnInfo(name = "collection")
    private String collection;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    private int type;

    //can be used in translate flashcards
    @ColumnInfo(name = "wordA")
    private String wordA;

    //used as the solution of the flashcard
    @ColumnInfo(name = "wordB")
    private String wordB;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "image")
    private byte[] image;

    @ColumnInfo(name = "done")
    private int done;

    public Flashcard(String collection, String name, int type, String wordA, String wordB, byte[] image) {
        this.collection = collection;
        this.name = name;
        this.type = type;
        this.wordA = wordA;
        this.wordB = wordB;
        this.image = image;
        this.done = 0;
    }

    @Ignore
    public Flashcard(String collection, String name, int type, String wordA, String wordB, byte[] image, int done) {
        this.collection = collection;
        this.name = name;
        this.type = type;
        this.wordA = wordA;
        this.wordB = wordB;
        this.image = image;
        this.done = done;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWordA() {
        return wordA;
    }

    public void setWordA(String wordA) {
        this.wordA = wordA;
    }

    public String getWordB() {
        return wordB;
    }

    public void setWordB(String wordB) {
        this.wordB = wordB;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }
}
