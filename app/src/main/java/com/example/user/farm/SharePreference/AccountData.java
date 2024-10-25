package com.example.user.farm.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by example on 16/9/2.
 */

public class AccountData {

    private static final String SHARED_PREF_AccountData = "account";
    private static Context context;

    public AccountData(Context context) {
        this.context = context;
    }

    public static void setCustomer(String email, String password, String customerName, String sex,
                                    String birth, String phone, String post, String address, String seller) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("customerName", customerName);
        editor.putBoolean("sex", sex.equals("True") ? true : false);
        editor.putString("birth", birth);
        editor.putString("phone", phone);
        editor.putString("post", post);
        editor.putString("address", address);
        editor.putBoolean("seller", seller.equals("True") ? true : false);
        editor.commit();
    }

    public static void setSeller(String sellerID, String level, String belong_StoreID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sellerID", sellerID);
        editor.putString("level", level);
        editor.putString("belong_StoreID", belong_StoreID);
        editor.commit();
    }

    public static void setID(String ID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID", ID);
        editor.commit();
    }
    public void trueseller() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("seller", true);
        editor.commit();
    }



    public static String getemail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }
    public static String getID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("ID", null);
    }

    public static String getpassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", null);
    }

    public static String getcustomerName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("customerName", null);
    }

    public static String getsexText(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("sex", true)?"男性":"女性";
    }
    public static boolean getsex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("sex", true);
    }

    public static String getbirth(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        String birth=sharedPreferences.getString("birth", null);

        try {
            String year=birth.substring(0,4);
            int mouth=Integer.parseInt(birth.substring(4,6));
            int day=Integer.parseInt(birth.substring(6,8));

            return String.format("%s年%d月%d日",year,mouth,day);
        }catch (Exception e){
            return birth;
        }

    }

    public static String getphone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", null);
    }

    public static String getpost(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("post", null);
    }

    public static String getaddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("address", null);
    }

    public static boolean isseller(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("seller", false);
    }

    public static String getsellerID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("sellerID", null);
    }

    public static String getlevel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("level", null);
    }
    public static boolean is_highest_level(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        String level=sharedPreferences.getString("level", null);
        return level.equals("2");
    }
    public static boolean is_high_level(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        String level=sharedPreferences.getString("level", null);
        return level.equals("2")||level.equals("1");
    }

    public static int highest_level(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("level", null).equals("2")?0x00000000:0x00000008;
    }


    public static String getbelong_StoreID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_AccountData, Context.MODE_PRIVATE);
        return sharedPreferences.getString("belong_StoreID", null);
    }

}
