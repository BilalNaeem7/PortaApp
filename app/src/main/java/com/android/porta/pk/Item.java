package com.android.porta.pk;

/**
 * Created by Bilal on 6/16/2015.
 */
public class Item {
    String name;
    int imgId;

    public Item(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
