package tw.edu.niu.farmfun.farmfun;

import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/10/23.
 */
public class Interface_Farm extends Activity
{
    Context context = this;
    private Spinner spn_Type;
    private TextView datetimeView, cropView;
    private EditText farmName, farmArea, farmQuantity, farmIntroduce;
    private Spinner farmSelect;
    private Button farmUpdate, insertType, deleteType;
    private ImageButton farmInsert, farmDelete;
    private ImageView farmPhoto;

    private int farmID = 0;
    private int typeID = 0;
    private int getSelect = 0;
    private int screenHeight = 0;
    private String result = null;

    private ArrayList <Integer> selectID = null;
    private ArrayList <String> farmData = null;
    private ArrayList <String> selectName = null;
    private ArrayAdapter<String> farmAdapter;

    private boolean boolUpdate = true;
    private boolean boolPrevious = false;
    private boolean boolListData = false;

    private Bitmap myBitmap;
    private byte[] mContent;

    private DatabaseHelper dbhelper = null;
    private int userID = 0;
    private int titleID = 0;
    private int identifyID = 0;

    private ArrayList<Integer> selectTypeID = null;
    private ArrayList <String> selectTypeName = null;
    private ArrayAdapter<String> typeAdapter;
    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_farm);

        prog = ProgressDialog.show(Interface_Farm.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        getMemberID();
        setupComponent();
        setupButtonListener();
        setupImageViewListener();
        startData();
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
                intent.setClass(Interface_Farm.this, Interface_Farm.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            case 2:
                intent.setClass(Interface_Farm.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            case 3:
                intent.setClass(Interface_Farm.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            case 4:
                intent.setClass(Interface_Farm.this, Interface_Order.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            case 5:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Farm.this, Interface_Register.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Farm.this.finish();
                }
                else
                {
                    intent.setClass(Interface_Farm.this, Interface_Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Farm.this.finish();
                }
                break;
            case 6:
                intent.setClass(Interface_Farm.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            case 7:
                intent.setClass(Interface_Farm.this, Interface_Service.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Farm.this, Interface_Login.class);
                startActivity(intent);
                Interface_Farm.this.finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void startData()
    {
        prog.show();
        WebService_Farm wbFarm = new WebService_Farm(handle, 0x01, titleID);
        wbFarm.start();
    }

    public void setupComponent()
    {
        cropView = (TextView)findViewById(R.id.farm_crop_view);
        spn_Type = (Spinner)findViewById(R.id.farm_type_spinner);
        insertType = (Button)findViewById(R.id.farm_type_insert_click);
        deleteType = (Button)findViewById(R.id.farm_type_delete_click);

        farmSelect = (Spinner)findViewById(R.id.farm_select);
        farmInsert = (ImageButton)findViewById(R.id.farm_insert_click);
        farmDelete = (ImageButton)findViewById(R.id.farm_delete_click);
        farmUpdate = (Button)findViewById(R.id.farm_update_click);
        datetimeView = (TextView)findViewById(R.id.farm_datetime_view);
        farmName = (EditText)findViewById(R.id.farm_name_edit);
        farmArea = (EditText)findViewById(R.id.farm_area_edit);
        farmQuantity = (EditText)findViewById(R.id.farm_quantity_edit);
        farmIntroduce = (EditText)findViewById(R.id.farm_introduce_edit);
        farmPhoto = (ImageView)findViewById(R.id.farm_photo);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;

        farmPhoto.getLayoutParams().height = screenHeight/3;

        if(identifyID!=1)
        {
            farmInsert.setEnabled(false);
            farmDelete.setEnabled(false);
            farmUpdate.setEnabled(false);
            insertType.setEnabled(false);
            deleteType.setEnabled(false);

            farmInsert.setVisibility(View.INVISIBLE);
            farmDelete.setVisibility(View.INVISIBLE);
            farmUpdate.setVisibility(View.INVISIBLE);
            insertType.setVisibility(View.INVISIBLE);
            deleteType.setVisibility(View.INVISIBLE);

            farmName.setKeyListener(null);
            farmArea.setKeyListener(null);
            farmQuantity.setKeyListener(null);
            farmIntroduce.setKeyListener(null);
            farmArea.setKeyListener(null);
            farmPhoto.setOnKeyListener(null);
        }
    }

    private void setupButtonListener()
    {
        farmInsert.setOnClickListener(new ImageButton.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                insertFarm();
            }
        });

        farmDelete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(boolListData)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                    dialog.setTitle(getString(R.string.message_delete));
                    dialog.setMessage(getString(R.string.message_delete_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            prog.show();
                            WebService_Farm wbFarm = new WebService_Farm(handle, 0x05, farmID);
                            wbFarm.start();
                        }
                    });
                    dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    });
                    dialog.show();

                }
            }
        });

        farmUpdate.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String area = farmArea.getText().toString();
                final String quantity = farmQuantity.getText().toString();
                final String introduce = farmIntroduce.getText().toString();
                final String photo = imgToString();

                if (area != null && !area.equals("")
                        && quantity != null && !quantity.equals("")
                        && introduce != null && !introduce.equals("")) {
                    if (boolUpdate) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_update));
                        dialog.setMessage(getString(R.string.message_update_info));
                        dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                prog.show();
                                WebService_Farm wbFarm = new WebService_Farm(handle, 0x03, farmID, selectName.get(getSelect).toString(), area, "null", quantity, introduce);
                                wbFarm.start();
                            }
                        });

                        dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    } else {

                        final String name = farmName.getText().toString();
                        if (name != null && !name.equals("")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                            dialog.setTitle(getString(R.string.message_insert));
                            dialog.setMessage(getString(R.string.message_insert_info));
                            dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    prog.show();
                                    WebService_Farm wbFarm = new WebService_Farm(handle, 0x04, titleID, name, area, "null", quantity, introduce, photo);
                                    wbFarm.start();
                                }
                            });

                            dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            });
                            dialog.show();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                            dialog.setTitle(getString(R.string.message_blank));
                            dialog.setMessage(getString(R.string.message_blank_info));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            });
                            dialog.show();
                        }

                    }
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
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

        insertType.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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

        deleteType.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                dialog.setTitle(getString(R.string.message_delete));
                dialog.setMessage(getString(R.string.message_delete_info2) + "\n" + getString(R.string.message_delete_info));
                dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        prog.show();
                        WebService_Crop wbFarm = new WebService_Crop(handle, 0x10, typeID);
                        wbFarm.start();
                    }
                });
                dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {}
                });
                dialog.show();
            }
        });

    }

    private void setupImageViewListener()
    {
        farmPhoto.setOnLongClickListener(new ImageView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!boolUpdate) {
                    selectPhoto();
                }

                return false;
            }
        });
    }

    private void setupSpinnerListener()
    {
        farmAdapter = new ArrayAdapter<String>(Interface_Farm.this, R.layout.item_spinner, selectName);
        farmAdapter.setDropDownViewResource(R.layout.item_spinner);
        farmSelect.setAdapter(farmAdapter);

        farmSelect .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                farmID = selectID.get(position);
                getSelect = position;
                prog.show();
                WebService_Farm wbFarm = new WebService_Farm(handle, 0x02, farmID);
                wbFarm.start();
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
                    selectID = null;
                    selectID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x06:
                    prog.dismiss();
                    selectName = null;
                    selectName = (ArrayList<String>) msg.obj;

                    if(selectID.get(0)!=0)
                    {
                        boolListData = true;
                        setupSpinnerListener();
                    }
                    else
                    {
                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info));
                            dialog.setPositiveButton(getString(R.string.click_send), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    boolListData = false;
                                    insertFarm();
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info6));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                }
                            });
                            dialog.show();
                        }
                    }
                    break;
                case  0x02:
                    farmData = null;
                    farmData = (ArrayList<String>) msg.obj;

                    farmArea.setText(farmData.get(0).toString());
                    farmQuantity.setText(farmData.get(2).toString());
                    farmIntroduce.setText(farmData.get(3).toString());
                    datetimeView.setHint(farmData.get(5).toString());
                    farmPhoto.setImageBitmap(stringToBitmap(farmData.get(4).toString()));

                    WebService_Crop wbFarm = new WebService_Crop(handle, 0x06, farmID);
                    wbFarm.start();
                    break;
                case 0x03:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_update_succeed));
                        dialog.setMessage(getString(R.string.message_update_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                startData();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }

                    break;
                case 0x04:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_update_succeed));
                        dialog.setMessage(getString(R.string.message_update_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                Intent intent = new Intent();
                                intent.setClass(Interface_Farm.this, Interface_Farm.class);
                                startActivity(intent);
                                Interface_Farm.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;

                case 0x05:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_delete_succeeded));
                        dialog.setMessage(getString(R.string.message_delete_succeeded_info));
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_delete_failed));
                        dialog.setMessage(getString(R.string.message_delete_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
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
                        typeAdapter = new ArrayAdapter<String>(Interface_Farm.this, R.layout.item_spinner, selectTypeName);
                        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
                        spn_Type.setAdapter(typeAdapter);

                        spn_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
                            {
                                typeID = selectTypeID.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0){}
                        });
                    }
                    else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_no_data));
                        dialog.setMessage(getString(R.string.message_no_data_info4));
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
                        dialog.setTitle(getString(R.string.message_insert_succeed));
                        dialog.setMessage(getString(R.string.message_insert_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                prog.show();
                                WebService_Crop wbFarm = new WebService_Crop(handle, 0x06, farmID);
                                wbFarm.start();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
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

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Farm.this);
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
        farmPhoto.buildDrawingCache();
        Bitmap bitmap = farmPhoto.getDrawingCache();

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
        AlertDialog dlg = new AlertDialog.Builder(Interface_Farm.this).setTitle("請選擇上傳方式").setItems(items,
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
                farmPhoto.setImageBitmap(myBitmap);
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
                myBitmap.compress(Bitmap.CompressFormat.JPEG , 50, baos);
                mContent=baos.toByteArray();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            farmPhoto.setImageBitmap(myBitmap);
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

        float height = farmPhoto.getHeight();
        float width = farmPhoto.getWidth();

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


    private void insertFarm()
    {
        if (boolPrevious) {

            farmDelete.setEnabled(true);
            farmDelete.setVisibility(View.VISIBLE);

            farmSelect.setVisibility(View.VISIBLE);
            farmName.setVisibility(View.GONE);

            boolUpdate = true;
            boolPrevious = false;

            farmInsert.setImageResource(R.mipmap.icon_add);

            cropView.setText(getString(R.string.farm_crop));

            spn_Type.setEnabled(true);
            insertType.setEnabled(true);
            deleteType.setEnabled(true);
            spn_Type.setVisibility(View.VISIBLE);
            insertType.setVisibility(View.VISIBLE);
            deleteType.setVisibility(View.VISIBLE);

            startData();
        }
        else
        {
            farmDelete.setEnabled(false);
            farmDelete.setVisibility(View.INVISIBLE);

            cropView.setText(getString(R.string.farm_crop2));

            spn_Type.setEnabled(false);
            insertType.setEnabled(false);
            deleteType.setEnabled(false);

            spn_Type.setVisibility(View.INVISIBLE);
            insertType.setVisibility(View.INVISIBLE);
            deleteType.setVisibility(View.INVISIBLE);

            farmSelect.setVisibility(View.GONE);
            farmName.setVisibility(View.VISIBLE);

            boolUpdate = false;
            boolPrevious = true;
            farmInsert.setImageResource(R.mipmap.icon_previous);

            datetimeView.setText("");
            farmName.setText("");
            farmArea.setText("");
            farmQuantity.setText("");
            farmIntroduce.setText("");

            farmName.setHint(getString(R.string.hint_name));
            farmArea.setHint(getString(R.string.hint_area));
            farmQuantity.setHint(getString(R.string.hint_quantity));
            farmIntroduce.setHint(getString(R.string.hint_introduce));

            farmPhoto.setImageDrawable(null);
            farmPhoto.setImageResource(R.mipmap.icon_camera);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

        }
        return false;
    }
}
