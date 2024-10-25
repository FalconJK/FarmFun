package com.example.user.farm.Funtional;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by iirlabpro_2 on 2018/3/21.
 */

public class Imageitem implements Comparable<Imageitem>{

    private String bitmap;
    private String pos;
    private int num;

    public Imageitem(String bitmap,String pos,int num) {
        this.bitmap = bitmap;
        this.pos=pos;
        this.num=num;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int compareTo(@NonNull Imageitem o) {
        int i=this.num-o.getNum();
        return i;
    }
}
