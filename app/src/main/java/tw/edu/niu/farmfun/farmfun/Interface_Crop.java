package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2015/10/24.
 */
public class Interface_Crop extends Activity {

    final Context context = this;

    private Button insertClick, modifyClick, deleteClick;
    private ListView cropList;
    private Spinner spn_Farm;

    MyAdapter adapter = null;
    private ArrayAdapter<String> farmAdapter;

    private int selectID = 0;
    private int screenHeight = 0;
    private int totalQuantity = 0;
    private int totalPrice = 0;
    private String result = null;
    private boolean boolListData;

    private ArrayList<Integer> id = null;
    private ArrayList<Integer> price = null;
    private ArrayList<Integer> quantity = null;
    private ArrayList<String> name = null;
    private ArrayList<String> packages = null;
    private ArrayList<String> unit = null;
    private ArrayList<String> intr = null;
    private ArrayList<String> launchdate = null;
    private ArrayList<String> photo = null;
    private ArrayList<String> datetime = null;

    private ArrayList<Integer> farmID = null;
    private ArrayList<String> farmName = null;

    private DatabaseHelper dbhelper = null;
    private int userID = 0;
    private int titleID = 0;
    private int identifyID = 0;

    private ArrayList<Integer> serviceResult = null;
    private int servicePrice = 0;
    private String serviceCode = null;
    private String serviceSelect = null;
    private String serviceAddress = null;

    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_crop);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Crop.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        getMemberID();
        setupComponent();
        setupButtonListener();
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
                intent.setClass(Interface_Crop.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Crop.this.finish();
                break;
            case 1:
                intent.setClass(Interface_Crop.this, Interface_Farm.class);
                startActivity(intent);
                Interface_Crop.this.finish();
                break;
            case 3:
                intent.setClass(Interface_Crop.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Crop.this.finish();
                break;
            case 4:
                intent.setClass(Interface_Crop.this, Interface_Order.class);
                startActivity(intent);
                Interface_Crop.this.finish();
                break;
            case 5:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Crop.this, Interface_Register.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Crop.this.finish();
                }
                else
                {
                    intent.setClass(Interface_Crop.this, Interface_Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Crop.this.finish();
                }
                break;
            case 6:
                intent.setClass(Interface_Crop.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Crop.this.finish();
                break;
            case 7:
                intent.setClass(Interface_Crop.this, Interface_Service.class);
                startActivity(intent);
                Interface_Crop.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Crop.this, Interface_Login.class);
                startActivity(intent);
                Interface_Crop.this.finish();
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
        WebService_Crop wbCrop = new WebService_Crop(handle, 0x05, titleID);
        wbCrop.start();
    }

    private void setupComponent()
    {
        insertClick = (Button)findViewById(R.id.crop_insert_click);
        modifyClick = (Button)findViewById(R.id.crop_modify_click);
        deleteClick = (Button)findViewById(R.id.crop_delete_click);
        spn_Farm = (Spinner)findViewById(R.id.crop_farm_spn);

        cropList = (ListView)findViewById(R.id.crop_list_view);
        adapter = new MyAdapter(this);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;

        boolListData = false;

        if(identifyID!=1)
        {
            insertClick.setText(R.string.crop_buy_title);
            modifyClick.setText(R.string.crop_search_resume);

            deleteClick.setEnabled(false);
            deleteClick.setVisibility(View.INVISIBLE);
        }
    }

    private void setupButtonListener()
    {
        insertClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (identifyID == 1) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Crop.this, Interface_Crop_Info.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("boolUpdate", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Crop.this.finish();
                } else {
                    if (boolListData) {
                        if (quantity.get(selectID) != 0) {
                            prog.show();
                            WebService_Service wbService = new WebService_Service(handle, 0x04, titleID);
                            wbService.start();
                        } else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                            dialog.setTitle(getString(R.string.crop_outstore));
                            dialog.setMessage(getString(R.string.crop_outstore_info));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            });
                            dialog.show();
                        }
                    }
                }
            }
        });

        modifyClick.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(boolListData)
                {
                    if(identifyID==1)
                    {
                        int sid = id.get(selectID);
                        int sprice = price.get(selectID);
                        int squantity = quantity.get(selectID);

                        String sname = name.get(selectID).toString();
                        String spackages = packages.get(selectID).toString();
                        String sunit = unit.get(selectID).toString();

                        String slaunchdate = launchdate.get(selectID).toString();
                        String sintr = intr.get(selectID).toString();
                        String sphoto = photo.get(selectID).toString();

                        Intent intent = new Intent();
                        intent.setClass(Interface_Crop.this, Interface_Crop_Info.class);
                        Bundle bundle = new Bundle();

                        bundle.putBoolean("boolUpdate", false);

                        bundle.putInt("id", sid);
                        bundle.putInt("price", sprice);
                        bundle.putInt("store", squantity);

                        bundle.putString("name", sname);
                        bundle.putString("packages", spackages);
                        bundle.putString("unit", sunit);
                        bundle.putString("launchdate", slaunchdate);
                        bundle.putString("intr", sintr);
                        bundle.putString("photo", sphoto);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        Interface_Crop.this.finish();
                    }
                    else
                    {
                        Toast.makeText(Interface_Crop.this, "抱歉，功能暫時停用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        deleteClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(boolListData)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                    dialog.setTitle(getString(R.string.message_delete));
                    dialog.setMessage(getString(R.string.delete_target)+name.get(selectID).toString() + "\n" + getString(R.string.message_delete_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            prog.show();
                            WebService_Crop wbCrop = new WebService_Crop(handle, 0x04, id.get(selectID));
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
        });
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
                    name = null;
                    name = (ArrayList<String>) msg.obj;
                    break;
                case 0x03:
                    unit = null;
                    unit = (ArrayList<String>) msg.obj;
                    break;
                case 0x04:
                    packages = null;
                    packages = (ArrayList<String>) msg.obj;
                    break;
                case 0x05:
                    price = null;
                    price = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x06:
                    quantity = null;
                    quantity = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x07:
                    intr = null;
                    intr = (ArrayList<String>) msg.obj;
                    break;
                case 0x08:
                    datetime = null;
                    datetime = (ArrayList<String>) msg.obj;
                    break;
                case 0x10:
                    launchdate = null;
                    launchdate = (ArrayList<String>) msg.obj;
                    break;
                case 0x11:
                    photo = null;
                    photo = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(id.get(0)!=0)
                    {
                        cropList.setAdapter(adapter);
                        cropList.setOnItemClickListener(listener);
                        boolListData = true;
                    }
                    else
                    {
                        if(identifyID == 1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    boolListData = false;
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
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
                case 0x12:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                        dialog.setTitle(getString(R.string.message_delete_succeeded));
                        dialog.setMessage(getString(R.string.message_delete_succeeded_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i){
                                startData();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                        dialog.setTitle(getString(R.string.message_delete_failed));
                        dialog.setMessage(getString(R.string.message_delete_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i){}
                        });
                        dialog.show();
                    }
                    break;
                case 0x13:
                    farmID = null;
                    farmID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x14:
                    farmName = null;
                    farmName = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(farmID.get(0)!=0)
                    {
                        farmAdapter = new ArrayAdapter<String>(Interface_Crop.this, R.layout.item_spinner, farmName);
                        farmAdapter.setDropDownViewResource(R.layout.item_spinner);
                        spn_Farm.setAdapter(farmAdapter);

                        spn_Farm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
                            {
                                prog.show();
                                WebService_Crop wbCrop = new WebService_Crop(handle, 0x01, farmID.get(position));
                                wbCrop.start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });
                    }
                    else
                    {
                        if(identifyID == 1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info2));
                            dialog.setPositiveButton(getString(R.string.click_insert_new_farm), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    Intent intent = new Intent();
                                    intent.setClass(Interface_Crop.this, Interface_Farm.class);
                                    startActivity(intent);
                                    Interface_Crop.this.finish();
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
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
                case 0x16:
                    result = null;
                    result = (String) msg.obj;

                    if(result.equals("Succeed"))
                    {
                        WebService_Crop wbCrop = new WebService_Crop(handle, 0x09, titleID, userID, id.get(selectID), totalQuantity, totalPrice, "W", serviceAddress);
                        wbCrop.start();
                    }
                    else if(result.equals("Failed"))
                    {
                        prog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                        dialog.setTitle(getString(R.string.message_buy_failed));
                        dialog.setMessage(getString(R.string.message_buy_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                        dialog.setTitle(getString(R.string.message_buy_succeed));
                        dialog.setMessage(getString(R.string.message_buy_succeed_info));
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
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
                    serviceResult = null;
                    serviceResult = (ArrayList<Integer>) msg.obj;
                    prog.dismiss();
                    OrderDialog();
                    break;
                case 0xEE:
                    prog.dismiss();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
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

    private void OrderDialog()
    {
        final Dialog orderDialog = new Dialog(context);
        orderDialog.setContentView(R.layout.dialog_order);
        orderDialog.setTitle(getString(R.string.crop_buy_title));

        final RadioGroup radiogroupService = (RadioGroup)orderDialog.findViewById(R.id.customer_service_group);
        final RadioButton r0 = (RadioButton)orderDialog.findViewById(R.id.customer_service_myself);
        final RadioButton r1 = (RadioButton)orderDialog.findViewById(R.id.customer_service_one);
        final RadioButton r2 = (RadioButton)orderDialog.findViewById(R.id.customer_service_two);
        final TextView tv_name = (TextView)orderDialog.findViewById(R.id.customer_buy_name);
        final EditText tv_quantity = (EditText)orderDialog.findViewById(R.id.customer_buy_quantity);
        final EditText address = (EditText)orderDialog.findViewById(R.id.customer_service_address);
        Button cancel = (Button) orderDialog.findViewById(R.id.customer_buy_cancel);
        Button accept = (Button) orderDialog.findViewById(R.id.customer_buy_accept);
        Button increase = (Button) orderDialog.findViewById(R.id.customer_buy_increase);
        Button decrease = (Button) orderDialog.findViewById(R.id.customer_buy_decrease);
        tv_name.setText(name.get(selectID).toString());
        tv_quantity.setText("1");

        radiogroupService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                serviceCode = null;
                switch (checkedId) {
                    case R.id.customer_service_myself:
                        serviceCode = "S0 ";
                        serviceSelect = r0.getText().toString();
                        servicePrice = 0;
                        address.setEnabled(false);
                        address.setBackgroundColor(Color.parseColor("#dddddd"));
                        break;
                    case R.id.customer_service_one:
                        serviceCode = "S1 ";
                        serviceSelect = r1.getText().toString();
                        servicePrice = serviceResult.get(0);
                        address.setEnabled(true);
                        address.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.customer_service_two:
                        serviceCode = "S2 ";
                        serviceSelect = r2.getText().toString();
                        servicePrice = serviceResult.get(1);
                        address.setEnabled(true);
                        address.setBackgroundColor(Color.parseColor("#ffffff"));
                    default:
                }
            }
        });

        r0.setChecked(true);
        if(serviceResult.get(0)!=0)
        {
            String str = r1.getText().toString();
            r1.setText(str + " " + Integer.toString(serviceResult.get(0)) + getString(R.string.taiwan_dollar));
        }
        else
        {
            r1.setEnabled(false);
        }

        if(serviceResult.get(1)!=0)
        {
            String str = r2.getText().toString();
            r2.setText(str + " " + Integer.toString(serviceResult.get(1)) + getString(R.string.taiwan_dollar));
        }
        else
        {
            r2.setEnabled(false);
        }

        increase.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(tv_quantity.getText().toString()) + 1;

                if (n > quantity.get(selectID)) {
                    n = quantity.get(selectID);
                }
                tv_quantity.setText(Integer.toString(n));
            }
        });

        decrease.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(tv_quantity.getText().toString()) - 1;

                if (n < 0) {
                    n = 0;
                }
                tv_quantity.setText(Integer.toString(n));
            }
        });

        accept.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Integer.parseInt(tv_quantity.getText().toString()) != 0 && Integer.parseInt(tv_quantity.getText().toString()) < quantity.get(selectID))
                {
                    final int getOrderQuatity = Integer.parseInt(tv_quantity.getText().toString());
                    final int getCalculate = quantity.get(selectID) - getOrderQuatity;
                    final int getTotalPrice = (price.get(selectID) * getOrderQuatity) + servicePrice;

                    AlertDialog.Builder agreementDialog = new AlertDialog.Builder(Interface_Crop.this);
                    agreementDialog.setTitle(getString(R.string.crop_buy_agreement));
                    agreementDialog.setMessage(getString(R.string.crop_buy_name) + tv_name.getText().toString() + "\n" +
                            getString(R.string.crop_buy_quantity) + tv_quantity.getText().toString() + unit.get(selectID) + "\n" +
                            getString(R.string.crop_totalprice) + Integer.toString(getTotalPrice) + "NT" + "\n" +
                            getString(R.string.crop_service) + serviceSelect.toString() + "\n" +
                            getString(R.string.crop_service_address) + address.getText().toString() + "\n" +
                            getString(R.string.crop_buy_question));

                    agreementDialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            totalQuantity = getOrderQuatity;
                            totalPrice = getTotalPrice;
                            serviceAddress = serviceCode.toString() + address.getText().toString();

                            prog.show();
                            WebService_Crop wbCrop = new WebService_Crop(handle, 0x08, id.get(selectID), getCalculate);
                            wbCrop.start();

                            orderDialog.dismiss();
                        }
                    });

                    agreementDialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            orderDialog.dismiss();
                        }
                    });
                    agreementDialog.show();
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Crop.this);
                    dialog.setTitle(getString(R.string.crop_buy_error));
                    dialog.setMessage(getString(R.string.crop_buy_error_info));
                    dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialoginterface, int i) {}
                    });
                    dialog.show();
                }
            }
        });

        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog.dismiss();
            }
        });
        orderDialog.show();
    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
        {
            selectID = 0;
            selectID = arg2;
        }

    };


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

        }
        return false;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.listview_crop, null);

            TextView sname = (TextView)convertView.findViewById(R.id.crop_name_view);
            TextView sdatetime = (TextView)convertView.findViewById(R.id.crop_datetime_view);
            TextView squantity = (TextView)convertView.findViewById(R.id.crop_quantity_view);
            TextView sprice = (TextView)convertView.findViewById(R.id.crop_unitprice_view);
            TextView slaunchdate  = (TextView)convertView.findViewById(R.id.crop_launchdate_view);
            TextView sintroduce = (TextView)convertView.findViewById(R.id.crop_introduce_view);
            ImageView sphoto = (ImageView)convertView.findViewById(R.id.crop_photo);

            sphoto.getLayoutParams().height = screenHeight/4;
            sphoto.getLayoutParams().width = screenHeight/4;

            sname.setText(name.get(position).toString());
            squantity.setText(getString(R.string.crop_store) + (String.valueOf(quantity.get(position))) + " " + unit.get(position).toString());
            sprice.setText(packages.get(position).toString() + " " + String.valueOf(price.get(position)) + "NT");

            slaunchdate.setText(getString(R.string.crop_launchdate)+launchdate.get(position).toString());
            sintroduce.setText(intr.get(position).toString());
            sdatetime.setHint(datetime.get(position).toString());
            sphoto.setImageBitmap(stringToBitmap(photo.get(position).toString()));

            return convertView;
        }
    }
}
