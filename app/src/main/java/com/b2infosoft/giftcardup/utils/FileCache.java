package com.b2infosoft.giftcardup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by rajesh on 8/5/2016.
 */
public class FileCache {
    private File cacheDir;

    public FileCache(Context context){

        //Find the dir at SDCARD to save cached images

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
        {
            //if SDCARD is mounted (SDCARD is present on device and mounted)
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),"paathshala");
        }
        else
        {
            // if checking on simulator the create cache dir in your application context
            cacheDir=context.getCacheDir();
        }

        if(!cacheDir.exists()){
            // create cache dir in your application context
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url){
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
    }
    public void setFile(Bitmap bitmap){

    }
    public void clear(){
        // list all files inside cache directory
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        //delete all cache directory files
        for(File f:files)
            f.delete();
    }
    public static String encodeToBase64(Bitmap image) {
        Bitmap bitmap_image = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap_image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
    public static Bitmap getBitmap(String string){
        if(string!=null && string.length()>0) {
            byte[] b = Base64.decode(string, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(b);
            return BitmapFactory.decodeStream(is);
        }
        return null;
    }
}
