package com.example.user.farm.Producer.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.user.farm.Funtional.QRCode;
import com.example.user.farm.R;
import com.example.user.farm.SharePreference.AccountData;
import com.google.gson.JsonObject;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by user on 2018/3/16.
 */

public class AddEmployee extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private ImageView qrcode;
    private Context context;
    private SegmentedGroup segmentedGroup;
    private String StoreID;
    private JsonObject jsonObject;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_add, container, false);
        context = getActivity();
        qrcode = (ImageView) view.findViewById(R.id.qrcodeimg);

        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented);
        segmentedGroup.setOnCheckedChangeListener(this);

        StoreID = AccountData.getbelong_StoreID(context);

        jsonObject = new JsonObject();
        jsonObject.addProperty("storeID", StoreID);

        Glide.with(context).load(R.drawable.farmfun_logo).into(qrcode);

        return view;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.level0:
                jsonObject.remove("level");
                jsonObject.addProperty("level", "0");
                break;

            case R.id.level1:
                jsonObject.remove("level");
                jsonObject.addProperty("level", "1");
                break;

        }
        qrcode.setImageBitmap(QRCode.createQRCodeWithLogo5(jsonObject.toString(), 500,
                drawableToBitmap(getResources().getDrawable(R.mipmap.ic_launcher))));
    }
}
