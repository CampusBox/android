package in.exun.campusbox.model;

import android.graphics.Bitmap;

/**
 * Created by ayush on 03/05/17.
 */

public class CreativityItems {

    private String data;
    private int type;
    private Bitmap bitmap;

    public CreativityItems(int type,String data,Bitmap bitmap){
        this.data = data;
        this.type = type;
        this.bitmap = bitmap;
    }

    public CreativityItems(int type,String data,String bitmap){
        this.data = data;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setData(String link) {
        data = link;
    }

    public void setBitmap(Bitmap mBitmap) {
        bitmap = mBitmap;
    }
}
