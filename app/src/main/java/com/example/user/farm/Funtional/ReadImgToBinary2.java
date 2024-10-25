package com.example.user.farm.Funtional;

/**
 * Created by iirlabpro_2 on 2018/3/8.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.user.farm.Consumer.Actitvity.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadImgToBinary2 {


    public static String img2LargeString(String imgPath, Bitmap bitmap) {
        if (imgPath !=null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if(bitmap == null){
            //bitmap not found!!
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static String img2SmallString(String imgPath, Bitmap bitmap) {
        if (imgPath !=null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if(bitmap == null){
            //bitmap not found!!
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }

    /**
     *
     * @param base64Data
     * @param imgName
     * @param imgFormat 圖片格式
     */
    public static void base64ToBitmap(String base64Data, String imgName, String imgFormat) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        File myCaptureFile = new File("/sdcard/", imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 5, fos);
        if (isTu) {
            // fos.notifyAll();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Bitmap stringToBitmap(String imgStr)
    {
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(imgStr.replace(" ","+"), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        }
        catch (Exception e)
        {

        }
        return bitmap;
    }
    VolleyMethod volleyMethod;
    public static byte[] stringTobyte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
