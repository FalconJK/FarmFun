package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2015/11/30.
 */
public class Interface_Order extends Activity
{
    private ListView orderList;

    MyAdapter adapter = null;

    private String result = null;
    private String receipt = null;
    private int getSelect = 0;

    private ArrayList<Integer> id = null;
    private ArrayList<Integer> cid = null;
    private ArrayList<Integer> many = null;
    private ArrayList<Integer> price = null;
    private ArrayList<String> name = null;
    private ArrayList<String> crop = null;
    private ArrayList<String> status = null;
    private ArrayList<String> datetime = null;
    private ArrayList<String> customInfo = null;

    private DatabaseHelper dbhelper = null;
    private int userID = 0;
    private int titleID = 0;
    private int identifyID = 0;

    private ArrayList<Integer> marketID = null;
    private ArrayAdapter<String> marketAdapter;
    private ArrayList <String> marketName = null;
    int selectMarketID = 0;

    private ArrayList <String> serviceResult = null;
    private String serviceChoose = null;
    private String serviceAddress = null;

    private int selectOrderID = 0;
    private Bitmap myBitmap;
    private byte[] mContent;

    private ProgressDialog prog;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_order);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Order.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        getMemberID();
        setupComponent();
        startData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, getString(R.string.click_refresh));
        menu.add(0, 1, 1, getString(R.string.login_farm));
        menu.add(0, 2, 2, getString(R.string.login_crop));
        menu.add(0, 3, 3, getString(R.string.login_resume));
        menu.add(0, 4, 4, getString(R.string.login_store));
        menu.add(0, 5, 5, getString(R.string.login_userinfo));
        menu.add(0, 6, 6, getString(R.string.login_bank));
        menu.add(0, 7, 7, getString(R.string.login_service));
        menu.add(0, 8, 8, getString(R.string.logout_enter));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(identifyID!=1)
        {
            menu.getItem(4).setTitle(getString(R.string.login_buy));
            menu.getItem(6).setEnabled(false);
            menu.getItem(6).setVisible(false);
            menu.getItem(7).setEnabled(false);
            menu.getItem(7).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onDestroy();
        Intent intent = new Intent();
        switch(item.getItemId())
        {
            case 0:
                intent.setClass(Interface_Order.this, Interface_Order.class);
                startActivity(intent);
                Interface_Order.this.finish();
                break;
            case 1:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Order.this, Interface_Farm.class);
                    startActivity(intent);
                    Interface_Order.this.finish();
                }
                else
                {
                    if(titleID==0)
                    {
                        selectMarketID = 1;
                        prog.show();
                        WebService_Login wbOrder = new WebService_Login(handle, 0x08);
                        wbOrder.start();
                    }
                    else
                    {
                        intent.setClass(Interface_Order.this, Interface_Farm.class);
                        startActivity(intent);
                        Interface_Order.this.finish();
                    }
                }
                break;
            case 2:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Order.this, Interface_Crop.class);
                    startActivity(intent);
                    Interface_Order.this.finish();
                }
                else
                {
                    if(titleID==0)
                    {
                        selectMarketID = 2;
                        prog.show();
                        WebService_Login wbOrder = new WebService_Login(handle, 0x08);
                        wbOrder.start();
                    }
                    else
                    {
                        intent.setClass(Interface_Order.this, Interface_Crop.class);
                        startActivity(intent);
                        Interface_Order.this.finish();
                    }
                }
                break;
            case 3:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Order.this, Interface_Resume.class);
                    startActivity(intent);
                    Interface_Order.this.finish();
                }
                else
                {
                    if(titleID==0)
                    {
                        selectMarketID = 3;
                        prog.show();
                        WebService_Login wbOrder = new WebService_Login(handle, 0x08);
                        wbOrder.start();
                    }
                    else
                    {
                        intent.setClass(Interface_Order.this, Interface_Resume.class);
                        startActivity(intent);
                        Interface_Order.this.finish();
                    }
                }
                break;
            case 5:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Order.this, Interface_Register.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Order.this.finish();
                }
                else
                {
                    intent.setClass(Interface_Order.this, Interface_Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Order.this.finish();
                }
                break;
            case 6:
                intent.setClass(Interface_Order.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Order.this.finish();
                break;
            case 7:
                intent.setClass(Interface_Order.this, Interface_Service.class);
                startActivity(intent);
                Interface_Order.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Order.this, Interface_Login.class);
                startActivity(intent);
                Interface_Order.this.finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void getMemberID()
    {
        dbhelper = new DatabaseHelper(this);

        Cursor cursor = getCursor();
        while(cursor.moveToNext())
        {
            userID = Integer.parseInt(cursor.getString(1));
            titleID = Integer.parseInt(cursor.getString(2));
            identifyID = Integer.parseInt(cursor.getString(3));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }

    @SuppressWarnings("deprecation")
    private Cursor getCursor(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] columns = {_ID, MEMBER, TITLE, IDENTIFY};

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        startManagingCursor(cursor);

        return cursor;
    }

    private void startData()
    {
        prog.show();
        if(identifyID==1)
        {
            WebService_Order wbOrder = new WebService_Order(handle, 0x01, titleID);
            wbOrder.start();
        }
        else
        {
            WebService_Order wbOrder = new WebService_Order(handle, 0x06, userID);
            wbOrder.start();
        }
    }

    private void setupComponent()
    {
        orderList = (ListView)findViewById(R.id.order_list_view);
        adapter = new MyAdapter(this);
    }

    private Handler handle = new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    id = null;
                    id = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x02:
                    cid = null;
                    cid = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x03:
                    name = null;
                    name = (ArrayList<String>) msg.obj;
                    break;
                case 0x04:
                    crop = null;
                    crop = (ArrayList<String>) msg.obj;
                    break;
                case 0x05:
                    many = null;
                    many = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x06:
                    price = null;
                    price = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x07:
                    status = null;
                    status = (ArrayList<String>) msg.obj;
                    break;
                case 0x08:
                    datetime = null;
                    datetime = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(id.get(0)!=0)
                    {
                        Collections.reverse(id);
                        Collections.reverse(cid);
                        Collections.reverse(name);
                        Collections.reverse(crop);
                        Collections.reverse(many);
                        Collections.reverse(price);
                        Collections.reverse(status);
                        Collections.reverse(datetime);

                        orderList.setAdapter(adapter);
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_order));
                        dialog.setMessage(getString(R.string.message_no_order_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                            }
                        });
                        dialog.show();
                    }

                    break;
                case 0x09:
                    customInfo = null;
                    customInfo = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    final Dialog cusDialog = new Dialog(context);
                    cusDialog.setContentView(R.layout.dialog_customer_info);
                    cusDialog.setTitle(getString(R.string.customer_Info));

                    TextView name = (TextView) cusDialog.findViewById(R.id.customer_name_view);
                    TextView sex = (TextView) cusDialog.findViewById(R.id.customer_sex_view);
                    TextView birth = (TextView) cusDialog.findViewById(R.id.customer_birth_view);
                    TextView phone = (TextView) cusDialog.findViewById(R.id.customer_phone_view);
                    TextView mail = (TextView) cusDialog.findViewById(R.id.customer_mail_view);
                    TextView address = (TextView) cusDialog.findViewById(R.id.customer_address_view);

                    name.setText(customInfo.get(0).toString());
                    sex.setText(customInfo.get(1).toString());
                    birth.setText(customInfo.get(2).toString());
                    phone.setText(customInfo.get(3).toString());
                    mail.setText(customInfo.get(4).toString());
                    address.setText(customInfo.get(5).toString()+customInfo.get(6).toString());

                    cusDialog.show();

                    break;
                case 0x10:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_update_succeed));
                        dialog.setMessage(getString(R.string.message_update_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                startData();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0x11:
                    receipt = null;
                    receipt =(String) msg.obj;
                    prog.dismiss();

                    if(!receipt.equals("No Data"))
                    {
                        dialogReceipt();
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_order));
                        dialog.setMessage(getString(R.string.message_no_order_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x12:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_update_succeed));
                        dialog.setMessage(getString(R.string.message_update_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                startData();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x13:
                    marketID = null;
                    marketID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x14:
                    prog.dismiss();
                    marketName = null;
                    marketName = (ArrayList<String>) msg.obj;

                    if(marketID.get(0)!=0)
                    {
                        selectMarket(selectMarketID);
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_no_data));
                        dialog.setMessage(getString(R.string.message_no_data_info6));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Order.this, Interface_Login.class);
                                startActivity(intent);
                                Interface_Order.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x15:
                    serviceResult = null;
                    serviceResult = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(serviceResult.get(0).substring(0,2).toString().equals("S0"))
                    {
                        serviceChoose = "自取";
                    }
                    else
                    if(serviceResult.get(0).substring(0,2).toString().equals("S1"))
                    {
                        serviceChoose = "宅配/快遞";
                    }
                    else
                    if(serviceResult.get(0).substring(0,2).toString().equals("S2"))
                    {
                        serviceChoose = "7-11取貨";
                    }
                    serviceAddress = serviceResult.get(0).substring(3).toString();
                    serviceDialog();
                    break;
                case 0x16:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        prog.show();
                        WebService_Order wbOrder = new WebService_Order(handle, 0x08, id.get(getSelect), "Y");
                        wbOrder.start();
                    }
                    else if(result.equals("Failed")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_upload_Image_failed));
                        dialog.setMessage(getString(R.string.message_upload_Image_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x17:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_message_upload_Image_succeed));
                        dialog.setMessage(getString(R.string.message_message_upload_Image_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                startData();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0xEE:
                    prog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                    dialog.setTitle(getString(R.string.message_error));
                    dialog.setMessage(getString(R.string.message_error_info));
                    dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialoginterface, int i) {}
                    });
                    dialog.show();
                    break;
                case 0xFF:
                    break;

            }
        }
    };

    private void dialogReceipt()
    {
        final Dialog recDialog = new Dialog(context);
        recDialog.setContentView(R.layout.dialog_bill);
        recDialog.setTitle(getString(R.string.message_upload_Image));

        Button cancel = (Button)recDialog.findViewById(R.id.order_receipt_cancel);
        Button accept = (Button)recDialog.findViewById(R.id.order_receipt_accept);
        final ImageView view = (ImageView)recDialog.findViewById(R.id.order_receipt_img);
        view.setImageBitmap(myBitmap);

        if(identifyID==1)
        {
            accept.setEnabled(false);
            cancel.setEnabled(false);
            accept.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);

            view.setImageBitmap(stringToBitmap(receipt));
        }
        else
        {
            view.setImageBitmap(myBitmap);
        }

        accept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String sphoto = BitMapToString(myBitmap);

                AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                dialog.setTitle(getString(R.string.message_upload_Image));
                dialog.setMessage(getString(R.string.message_upload_Image_info));

                dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        prog.show();
                        WebService_Order wbOrder = new WebService_Order(handle, 0x07, id.get(getSelect), sphoto);
                        wbOrder.start();

                        recDialog.dismiss();
                    }
                });

                dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        recDialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                recDialog.dismiss();
            }
        });
        recDialog.show();
    }

    private void selectPhoto()
    {
        final CharSequence[] items = {"相簿", "照相"};
        AlertDialog dlg = new AlertDialog.Builder(Interface_Order.this).setTitle("請上傳收據").setItems(items,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(getImageByCamera, 1);
                        } else {
                            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                            getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            getImage.setType("image/jpeg");
                            startActivityForResult(getImage, 0);
                        }

                    }
                }).create();
        dlg.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        ContentResolver contentResolver  =getContentResolver();
        if(requestCode==0)
        {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                myBitmap = Comp(picturePath);
                myBitmap = compressImage(myBitmap);
                dialogReceipt();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }else if(requestCode==1)
        {
            try {
                Bundle extras = data.getExtras();
                myBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG , 100, baos);
                mContent=baos.toByteArray();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            dialogReceipt();
        }
    }

    private Bitmap Comp(String picturePath)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(picturePath, newOpts);
        newOpts.inJustDecodeBounds = false;

        int orgWidth = newOpts.outWidth;
        int orgHeight = newOpts.outHeight;

        float height = screenHeight;
        float width = screenWidth;

        int simpleSize = 1;
        if (orgWidth > orgHeight && orgWidth > width)
        {
            simpleSize = (int) (newOpts.outWidth / width);
        }
        else if (orgWidth < orgHeight && orgHeight > height)
        {
            simpleSize = (int) (newOpts.outHeight / height);
        }
        if (simpleSize <= 0)
            simpleSize = 1;

        newOpts.inSampleSize = simpleSize;
        bitmap = BitmapFactory.decodeFile(picturePath, newOpts);
        bitmap = compressImage(bitmap);
        return compressImage(bitmap);
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1024>100)
        {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    private String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private Bitmap stringToBitmap(String imgStr)
    {
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(imgStr, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        }
        catch (Exception e)
        {

        }
        return bitmap;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return name.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.listview_order, null);

            TextView sname = (TextView)convertView.findViewById(R.id.order_name_view);
            TextView scrop = (TextView)convertView.findViewById(R.id.order_crop_view);
            TextView smany = (TextView)convertView.findViewById(R.id.order_many_view);
            TextView sprice = (TextView)convertView.findViewById(R.id.order_price_view);
            TextView sdatetime = (TextView)convertView.findViewById(R.id.order_date_view);
            TextView sstatus = (TextView)convertView.findViewById(R.id.order_status_view);
            ImageButton info = (ImageButton)convertView.findViewById(R.id.order_info_click);
            ImageButton bill = (ImageButton)convertView.findViewById(R.id.order_bill_click);
            ImageButton accept = (ImageButton)convertView.findViewById(R.id.order_accept_click);
            ImageButton service = (ImageButton)convertView.findViewById(R.id.order_service_click);

            sname.setText(name.get(position).toString());
            scrop.setText(crop.get(position).toString());
            scrop.setTextSize(24);

            smany.setText(getString(R.string.order_many) + Integer.toString(many.get(position)));
            sprice.setText(getString(R.string.crop_price) + Integer.toString(price.get(position)) + getString(R.string.taiwan_dollar));
            sdatetime.setHint(datetime.get(position).toString());

            if(identifyID!=1)
            {
                info.setImageResource(R.mipmap.icon_bank);
                bill.setEnabled(false);
                bill.setVisibility(View.INVISIBLE);
            }

            String str_Status = null;
            if(status.get(position).toString().equals("W"))
            {
                str_Status = getString(R.string.status_waiting);
                sstatus.setTextColor(Color.parseColor("#FF0000"));
            }
            else
            if(status.get(position).toString().equals("X"))
            {
                str_Status = getString(R.string.status_paying);
                sstatus.setTextColor(Color.parseColor("#000000"));
            }
            else
            if(status.get(position).toString().equals("Y"))
            {
                str_Status = getString(R.string.status_paid);
                sstatus.setTextColor(Color.parseColor("#FF0000"));
            }
            else
            if(status.get(position).toString().equals("Z"))
            {
                str_Status = getString(R.string.status_finished);
                sstatus.setTextColor(Color.parseColor("#000000"));
            }
            sstatus.setText(str_Status);

            service.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prog.show();
                    WebService_Service wbService = new WebService_Service(handle, 0x05, id.get(position));
                    wbService.start();
                }
            });

            info.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(identifyID==1)
                    {
                        prog.show();
                        WebService_Order wbOrder = new WebService_Order(handle, 0x02, cid.get(position));
                        wbOrder.start();
                    }
                    else
                    {
                        onDestroy();
                        Intent intent = new Intent();
                        intent.setClass(Interface_Order.this, Interface_Bank.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("CheckBank", true);
                        bundle.putInt("GiveMeID", cid.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Interface_Order.this.finish();
                    }
                }
            });

            bill.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(identifyID==1)
                    {
                        if(status.get(position).toString().equals("Y"))
                        {
                            prog.show();
                            WebService_Order wbOrder = new WebService_Order(handle, 0x04, id.get(position));
                            wbOrder.start();
                        }
                    }
                }
            });

            accept.setOnClickListener(new ImageButton.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(status.get(position).toString().equals("W"))
                    {
                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                            dialog.setTitle(getString(R.string.message_order_question1));
                            dialog.setMessage(getString(R.string.message_order_question1_info));
                            dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {

                                    prog.show();
                                    WebService_Order wbOrder = new WebService_Order(handle, 0x03, id.get(position), "X");
                                    wbOrder.start();
                                }
                            });
                            dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {}
                            });
                            dialog.show();
                        }
                    }
                    else if(status.get(position).toString().equals("Y"))
                    {
                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                            dialog.setTitle(getString(R.string.message_order_question2));
                            dialog.setMessage(getString(R.string.message_order_question2_info));
                            dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    prog.show();
                                    WebService_Order wbOrder = new WebService_Order(handle, 0x03, id.get(position), "Z");
                                    wbOrder.start();
                                }
                            });
                            dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {}
                            });
                            dialog.show();
                        }
                    }
                    else if(status.get(position).toString().equals("X"))
                    {
                        if(identifyID!=1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Order.this);
                            dialog.setTitle(getString(R.string.message_order_question3));
                            dialog.setMessage(getString(R.string.message_order_question3_info));
                            dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    getSelect = position;
                                    selectPhoto();
                                }
                            });
                            dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {}
                            });
                            dialog.show();
                        }
                    }
                }
            });

            return convertView;
        }
    }

    private void serviceDialog()
    {
        final Dialog dialogService = new Dialog(context);
        dialogService.setContentView(R.layout.dialog_service);
        dialogService.setTitle(getString(R.string.login_service));

        TextView choose = (TextView)dialogService.findViewById(R.id.service_choose);
        TextView address = (TextView)dialogService.findViewById(R.id.service_address);

        choose.setText(serviceChoose.toString());
        address.setText(serviceAddress.toString());

        dialogService.show();
    }

    private void selectMarket(final int selectID)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_market);
        dialog.setTitle(getString(R.string.select_market));

        Button cancel = (Button) dialog.findViewById(R.id.market_cancel_click);
        Button accept = (Button) dialog.findViewById(R.id.market_accept_click);
        Spinner selection = (Spinner)dialog.findViewById(R.id.spn_market);

        marketAdapter = new ArrayAdapter<String>(Interface_Order.this, R.layout.item_spinner, marketName);
        marketAdapter.setDropDownViewResource(R.layout.item_spinner);
        selection.setAdapter(marketAdapter);

        selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                titleID = marketID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        accept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                del();
                add();

                if (selectID == 1) {
                    IntentFarm();
                }

                if (selectID == 2) {
                    IntentCrop();
                }

                if (selectID == 3) {
                    IntentResume();
                }
            }
        });
        dialog.show();
    }

    private void IntentFarm()
    {
        onDestroy();
        Intent intent = new Intent();
        intent.setClass(Interface_Order.this, Interface_Farm.class);
        startActivity(intent);
        Interface_Order.this.finish();
    }

    private void IntentCrop()
    {
        onDestroy();
        Intent intent = new Intent();
        intent.setClass(Interface_Order.this, Interface_Crop.class);
        startActivity(intent);
        Interface_Order.this.finish();
    }

    private void IntentResume()
    {
        onDestroy();
        Intent intent = new Intent();
        intent.setClass(Interface_Order.this, Interface_Resume.class);
        startActivity(intent);
        Interface_Order.this.finish();
    }

    private void add()
    {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEMBER, Integer.toString(userID));
        values.put(TITLE, titleID);
        values.put(IDENTIFY, Integer.toString(identifyID));
        db.insert(TABLE_NAME, null, values);
    }

    private void del()
    {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

        }
        return false;
    }
}
