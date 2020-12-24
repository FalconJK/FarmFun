package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2015/10/27.
 */
public class Interface_Crop_Info extends Activity {

    private Spinner spn_Unit, spn_Farm, spn_Type;
    private EditText Name, Launch, Package, Price, Unit, Introduce, Store;
    private ImageView Photo;
    private Button Cancel, Accept, insertType;
    private ImageButton imgbCalender;

    private ArrayList<Integer> selectFarmID = null;
    private ArrayList <String> selectFarmName = null;
    private ArrayList<Integer> selectTypeID = null;
    private ArrayList <String> selectTypeName = null;

    private int titleID = 0;
    private int screenHeight = 0;
    private int CropID, store, price, farmID, typeID;

    private String[] unitSelect = {"粒", "包", "箱"};
    private String name, farm, packages, unit, intr, photo, launchdate, type;
    private String result, strCalender;

    private boolean insertBool;

    private DatabaseHelper dbhelper = null;

    private ArrayAdapter<String> unitAdapter;
    private ArrayAdapter<String> farmAdapter;
    private ArrayAdapter<String> typeAdapter;

    private String sphoto = null;
    private Bitmap myBitmap;
    private byte[] mContent;

    private ProgressDialog prog;
    final Context context = this;

    private String resumeTitle = null;
    private String resumeIntrod = null;
    private String resumeDate = null;
    private int resumeTypeID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_crop);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Crop_Info.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        getMemberID();
        setupComponent();
        setupButtonListener();
        setupImageViewListener();
        setupSpinnerListener();
        startData();
        getbundle();
    }

    public void getbundle()
    {
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null)
        {
            insertBool = bundle.getBoolean("boolUpdate");
            if(!insertBool)
            {
                CropID = bundle.getInt("id");
                price = bundle.getInt("price");
                store = bundle.getInt("store");

                name = bundle.getString("name");
                packages = bundle.getString("packages");
                unit = bundle.getString("unit");
                launchdate = bundle.getString("launchdate");
                intr = bundle.getString("intr");
                photo = bundle.getString("photo");

                Name.setText(name);
                Launch.setText(launchdate);
                Package.setText(packages);
                Price.setText(String.valueOf(price));
                Store.setText(String.valueOf(store));
                Unit.setText(String.valueOf(unit));
                Introduce.setText(intr);
                Photo.setImageBitmap(stringToBitmap(photo));
            }
        }
    }

    private void getMemberID()
    {
        dbhelper = new DatabaseHelper(this);

        Cursor cursor = getCursor();
        while(cursor.moveToNext())
        {
            titleID = Integer.parseInt(cursor.getString(2));
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
        WebService_Crop wbCrop = new WebService_Crop(handle, 0x05, titleID);
        wbCrop.start();
    }

    public void setupComponent()
    {
        Name = (EditText)findViewById(R.id.crop_name_edit);
        Launch = (EditText)findViewById(R.id.crop_launchdate_edit);
        Package= (EditText)findViewById(R.id.crop_package_edit);
        Price = (EditText)findViewById(R.id.crop_price_edit);
        Unit = (EditText)findViewById(R.id.crop_unit_edit);
        Store = (EditText)findViewById(R.id.crop_store_edit);
        Introduce = (EditText)findViewById(R.id.crop_introduce_edit);
        Photo = (ImageView)findViewById(R.id.crop_image_upload);
        Cancel = (Button)findViewById(R.id.crop_cancel_click);
        Accept = (Button)findViewById(R.id.crop_accept_click);
        spn_Unit = (Spinner)findViewById(R.id.crop_unit_spinner);
        spn_Farm = (Spinner)findViewById(R.id.crop_farm_spinner);
        spn_Type = (Spinner)findViewById(R.id.crop_type_spinner);
        insertType = (Button)findViewById(R.id.crop_type_insert_click);
        imgbCalender = (ImageButton)findViewById(R.id.crop_calender_click);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        Photo.getLayoutParams().height = screenHeight/3;
        Photo.getLayoutParams().width = screenHeight/4;
        Photo.setImageResource(R.mipmap.icon_camera);

        unitAdapter = new ArrayAdapter<String>(Interface_Crop_Info.this, R.layout.item_spinner, unitSelect);
        unitAdapter.setDropDownViewResource(R.layout.item_spinner);
        spn_Unit.setAdapter(unitAdapter);

        insertBool = true;

        //Locking
        insertType.setEnabled(false);
        insertType.setVisibility(View.INVISIBLE);
    }

    public void setupButtonListener()
    {
        Accept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sname = Name.getText().toString();
                final String spackages = Package.getText().toString();
                final String sunit = Unit.getText().toString();
                final String sintroduce = Introduce.getText().toString();
                final String slaunchdate = Launch.getText().toString();
                final String sprice = Price.getText().toString();
                final String sstore = Store.getText().toString();

                sphoto = null;
                sphoto = imgToString();

                resumeTitle = Name.getText().toString() + "已正式上市";
                resumeDate = Launch.getText().toString();
                resumeTypeID = typeID;
                resumeIntrod = Name.getText().toString() + "終於在今天正式登場囉，如有興趣者可以先至我們的店家了解更多資訊！";

                if(sname!=null && !sname.equals("") &&
                        sunit!=null && !sunit.equals("") &&
                        spackages!=null && !spackages.equals("") &&
                        sprice!=null && !sprice.equals("") &&
                        sstore!=null && !sstore.equals("") &&
                        sintroduce!=null && !sintroduce.equals("")&&
                        slaunchdate!=null && !slaunchdate.equals(""))
                {
                    if (insertBool) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_insert));
                        dialog.setMessage(getString(R.string.message_insert_info));
                        dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                prog.show();
                                WebService_Crop wbCrop = new WebService_Crop(handle, 0x02, farmID, typeID, Integer.parseInt(sprice), Integer.parseInt(sstore), sname, spackages, sunit, sintroduce, slaunchdate, sphoto);
                                wbCrop.start();
                            }
                        });

                        dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_update));
                        dialog.setMessage(getString(R.string.message_update_info));
                        dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                prog.show();
                                WebService_Crop wbCrop = new WebService_Crop(handle, 0x03, CropID, farmID, typeID, Integer.parseInt(sprice), Integer.parseInt(sstore), sname, spackages, sunit, sintroduce, slaunchdate, sphoto);
                                wbCrop.start();
                            }
                        });

                        dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                    dialog.setTitle(getString(R.string.message_blank));
                    dialog.setMessage(getString(R.string.message_blank_info));
                    dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    });
                    dialog.show();
                }
            }
        });

        Cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                Intent intent = new Intent();
                intent.setClass(Interface_Crop_Info.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Crop_Info.this.finish();
            }
        });

        insertType.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_type);
                dialog.setTitle(getString(R.string.message_insert));

                final EditText name = (EditText) dialog.findViewById(R.id.type_name_edit);
                Button cancel = (Button) dialog.findViewById(R.id.type_cancel_click);
                Button accept = (Button) dialog.findViewById(R.id.type_accept_click);

                cancel.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                accept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!name.getText().toString().equals("") && name.getText().toString() != null) {
                            prog.show();
                            WebService_Crop wbFarm = new WebService_Crop(handle, 0x07, farmID, name.getText().toString());
                            wbFarm.start();

                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        imgbCalender.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_calender);
                dialog.setTitle(getString(R.string.click_calender));

                Button cancel = (Button) dialog.findViewById(R.id.cancel_calender_click);
                Button accept = (Button) dialog.findViewById(R.id.accept_calender_click);
                CalendarView view = (CalendarView) dialog.findViewById(R.id.calendarView);
                view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {

                        strCalender = String.valueOf(year).toString() + "/" + String.valueOf(month+1) + "/" + String.valueOf(date);
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
                        Launch.setText(strCalender);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public void setupImageViewListener()
    {
        Photo.setOnLongClickListener(new ImageView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectPhoto();
                return false;
            }
        });
    }

    public void setupSpinnerListener()
    {
        spn_Unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Unit.setText(unitSelect[position].toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private Handler handle = new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_succeed));
                        dialog.setMessage(getString(R.string.message_insert_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                prog = ProgressDialog.show(Interface_Crop_Info.this, getString(R.string.message_resume_loading_info), getString(R.string.message_loading_info));
                                prog.show();

                                WebService_Resume wbResume = new WebService_Resume(handle, 0x09, resumeTypeID,resumeDate, resumeTitle, resumeIntrod);
                                wbResume.start();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }

                    break;
                case 0x02:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_update_succeed));
                        dialog.setMessage(getString(R.string.message_update_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Crop_Info.this, Interface_Crop.class);
                                startActivity(intent);
                                Interface_Crop_Info.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0x13:
                    selectFarmID = null;
                    selectFarmID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x14:
                    selectFarmName = null;
                    selectFarmName = (ArrayList<String>) msg.obj;

                    if(selectFarmID.get(0)!=0)
                    {
                        farmAdapter = new ArrayAdapter<String>(Interface_Crop_Info.this, R.layout.item_spinner, selectFarmName);
                        farmAdapter.setDropDownViewResource(R.layout.item_spinner);
                        spn_Farm.setAdapter(farmAdapter);

                        spn_Farm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                farmID = selectFarmID.get(position);

                                prog.show();
                                WebService_Crop wbCrop = new WebService_Crop(handle, 0x06, farmID);
                                wbCrop.start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_no_data));
                        dialog.setMessage(getString(R.string.message_no_data_info2));
                        dialog.setPositiveButton(getString(R.string.click_insert_new_farm), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Crop_Info.this, Interface_Farm.class);
                                startActivity(intent);
                                Interface_Crop_Info.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x15:
                    selectTypeID = null;
                    selectTypeID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x16:
                    selectTypeName = null;
                    selectTypeName = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(selectTypeID.get(0)!=0)
                    {
                        typeAdapter = new ArrayAdapter<String>(Interface_Crop_Info.this, R.layout.item_spinner, selectTypeName);
                        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
                        spn_Type.setAdapter(typeAdapter);

                        spn_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                typeID = selectTypeID.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_no_data));
                        dialog.setMessage(getString(R.string.message_no_data_info7));
                        dialog.setPositiveButton(getString(R.string.click_send), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_succeed));
                        dialog.setMessage(getString(R.string.message_insert_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                prog.show();
                                WebService_Crop wbCrop = new WebService_Crop(handle, 0x06, farmID);
                                wbCrop.start();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_failed));
                        dialog.setMessage(getString(R.string.message_insert_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0x18:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_succeed));
                        dialog.setMessage(getString(R.string.message_insert_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Crop_Info.this, Interface_Crop.class);
                                startActivity(intent);
                                Interface_Crop_Info.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_failed));
                        dialog.setMessage(getString(R.string.message_insert_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0xEE:
                    prog.dismiss();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop_Info.this);
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

    private String imgToString()
    {
        Photo.buildDrawingCache();
        Bitmap bitmap = Photo.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] appicon = baos.toByteArray();

        String imgStr = null;
        imgStr =  Base64.encodeToString(appicon, Base64.DEFAULT);

        return imgStr;
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

    private void selectPhoto()
    {
        final CharSequence[] items = {"相簿", "照相"};
        AlertDialog dlg = new AlertDialog.Builder(Interface_Crop_Info.this).setTitle("請選擇上傳方式").setItems(items,
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
                Photo.setImageBitmap(myBitmap);
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
            Photo.setImageBitmap(myBitmap);
        }
    }

    private Bitmap Comp(String picturePath)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(picturePath, newOpts);
        newOpts.inJustDecodeBounds = false;

        int orgWidth = newOpts.outWidth;
        int orgHeight = newOpts.outHeight;

        float height = Photo.getHeight();
        float width = Photo.getWidth();

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

        }
        return false;
    }
}
