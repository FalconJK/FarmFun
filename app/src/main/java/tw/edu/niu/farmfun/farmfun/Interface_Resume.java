package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2015/10/25.
 */
public class Interface_Resume extends Activity {

    private ImageButton clickNew, clickDelete;
    private ListView resumeList;
    private Spinner select_Farm, select_Type, select_Crop;

    MyAdapter adapter = null;

    private int selectID = 0;
    private int getSelect = 0;
    private int selectFarmID = 0;
    private int selectTypeID = 0;
    private int selectCropID = 0;
    private String result = null;
    private String select_LaunchDate = null;
    private boolean boolListData;

    private ArrayList<Integer> resumeID = null;
    private ArrayList<Integer> farmID = null;
    private ArrayList<Integer> typeID = null;
    private ArrayList<Integer> cropID = null;
    private ArrayList<String> farmName = null;
    private ArrayList<String> typeName = null;
    private ArrayList<String> cropName = null;
    private ArrayList<String> resumeTitle = null;
    private ArrayList<String> resumeDate = null;
    private ArrayList<String> resumeIntroduce = null;
    private ArrayAdapter<String> farmAdapter = null;
    private ArrayAdapter<String> typeAdapter = null;
    private ArrayAdapter<String> cropAdapter = null;

    private DatabaseHelper dbhelper = null;
    private int userID = 0;
    private int titleID = 0;
    private int identifyID = 0;

    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_resume);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Resume.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
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
                intent.setClass(Interface_Resume.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Resume.this.finish();
                break;
            case 1:
                intent.setClass(Interface_Resume.this, Interface_Farm.class);
                startActivity(intent);
                Interface_Resume.this.finish();
                break;
            case 2:
                intent.setClass(Interface_Resume.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Resume.this.finish();
                break;
            case 4:
                intent.setClass(Interface_Resume.this, Interface_Order.class);
                startActivity(intent);
                Interface_Resume.this.finish();
                break;
            case 5:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Resume.this, Interface_Register.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Resume.this.finish();
                }
                else
                {
                    intent.setClass(Interface_Resume.this, Interface_Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Resume.this.finish();
                }
                break;
            case 6:
                intent.setClass(Interface_Resume.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Resume.this.finish();
                break;
            case 7:
                intent.setClass(Interface_Resume.this, Interface_Service.class);
                startActivity(intent);
                Interface_Resume.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Resume.this, Interface_Login.class);
                startActivity(intent);
                Interface_Resume.this.finish();
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
        WebService_Resume wbFarm = new WebService_Resume(handle, 0x04, titleID);
        wbFarm.start();
    }

    private void setupComponent()
    {
        clickNew = (ImageButton)findViewById(R.id.click_new_resume);
        clickDelete = (ImageButton)findViewById(R.id.click_delete_resume);
        resumeList = (ListView)findViewById(R.id.resume_list_view);
        select_Farm = (Spinner)findViewById(R.id.spn_resume_farm);
        select_Type = (Spinner)findViewById(R.id.spn_resume_type);
        select_Crop = (Spinner)findViewById(R.id.spn_resume_crop);
        adapter = new MyAdapter(this);

        boolListData = false;

        if(identifyID!=1)
        {
            clickNew.setEnabled(false);
            clickDelete.setEnabled(false);

            clickNew.setVisibility(View.INVISIBLE);
            clickDelete.setVisibility(View.INVISIBLE);
        }
    }

    private void setupButtonListener()
    {
        clickNew.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                Intent intent = new Intent();
                intent.setClass(Interface_Resume.this, Interface_Resume_Info.class);
                startActivity(intent);
                Interface_Resume.this.finish();
            }
        });

        clickDelete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boolListData) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                    dialog.setTitle(getString(R.string.message_delete));
                    dialog.setMessage(getString(R.string.delete_target) + resumeDate.get(getSelect).toString() + "\n" + getString(R.string.message_delete_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            prog.show();
                            WebService_Resume wbFarm = new WebService_Resume(handle, 0x03, selectID);
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
    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
        {
            getSelect = arg2;
            selectID = resumeID.get(arg2);
        }

    };

    private void FarmSpinnerListener()
    {
        farmAdapter = new ArrayAdapter<String>(Interface_Resume.this, R.layout.item_spinner, farmName);
        farmAdapter.setDropDownViewResource(R.layout.item_spinner);
        select_Farm.setAdapter(farmAdapter);

        select_Farm .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selectFarmID = farmID.get(position);

                if(selectFarmID!=-1)
                {
                    prog.show();
                    WebService_Resume wbResume = new WebService_Resume(handle, 0x05, selectFarmID);
                    wbResume.start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void TypeSpinnerListener()
    {
        typeAdapter = new ArrayAdapter<String>(Interface_Resume.this, R.layout.item_spinner, typeName);
        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
        select_Type.setAdapter(typeAdapter);

        select_Type .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selectTypeID = typeID.get(position);

                if(selectTypeID!=-1)
                {
                    prog.show();
                    WebService_Resume wbResume = new WebService_Resume(handle, 0x06, selectTypeID);
                    wbResume.start();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void CropSpinnerListener()
    {
        cropAdapter = new ArrayAdapter<String>(Interface_Resume.this, R.layout.item_spinner, cropName);
        cropAdapter.setDropDownViewResource(R.layout.item_spinner);
        select_Crop.setAdapter(cropAdapter);

        select_Crop .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selectCropID = cropID.get(position);

                if(selectCropID!=-1)
                {
                    if(selectCropID!=-2)
                    {
                        prog.show();
                        WebService_Resume wbResume = new WebService_Resume(handle, 0x07, selectCropID);
                        wbResume.start();
                    }
                    else
                    {
                        prog.show();
                        WebService_Resume wbResume = new WebService_Resume(handle, 0x08, selectTypeID);
                        wbResume.start();
                    }
                }
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
                    farmID = null;
                    farmID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x02:
                    farmName = null;
                    farmName = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(farmID.get(1)!=0)
                    {
                        FarmSpinnerListener();
                    }
                    else
                    {
                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info2));
                            dialog.setPositiveButton(getString(R.string.click_insert_new_farm), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    boolListData = false;

                                    onDestroy();
                                    Intent intent1 = new Intent();
                                    intent1.setClass(Interface_Resume.this, Interface_Farm.class);
                                    startActivity(intent1);
                                    Interface_Resume.this.finish();
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
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
                case 0x03:
                    typeID = null;
                    typeID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x04:
                    typeName = null;
                    typeName = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(typeID.get(1)!=0)
                    {
                        TypeSpinnerListener();
                    }
                    else
                    {
                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info5));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    boolListData = false;
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
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
                case 0x05:
                    cropID = null;
                    cropID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x06:
                    cropName = null;
                    cropName = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(typeID.get(1)!=0)
                    {
                        CropSpinnerListener();
                    }
                    else
                    {
                        if(identifyID==0)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info3));
                            dialog.setPositiveButton(getString(R.string.click_insert_new_crop), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialoginterface, int i)
                                {
                                    boolListData = false;

                                    onDestroy();
                                    Intent intent = new Intent();
                                    Intent intent1 = new Intent();
                                    intent1.setClass(Interface_Resume.this, Interface_Crop.class);
                                    startActivity(intent1);
                                    Interface_Resume.this.finish();
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
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
                case 0x07:
                    resumeID = null;
                    resumeID = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x08:
                    resumeTitle = null;
                    resumeTitle = (ArrayList<String>) msg.obj;
                    break;
                case 0x09:
                    resumeDate = null;
                    resumeDate = (ArrayList<String>) msg.obj;
                    break;
                case 0x10:
                    resumeIntroduce = null;
                    resumeIntroduce = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(resumeID.get(0)!=0)
                    {
                        boolListData = true;
                        Collections.reverse(resumeID);
                        Collections.reverse(resumeTitle);
                        Collections.reverse(resumeDate);
                        Collections.reverse(resumeIntroduce);

                        resumeList.setAdapter(adapter);
                        resumeList.setOnItemClickListener(listener);
                    }
                    else
                    {
                        resumeList.setAdapter(null);

                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    boolListData = false;
                                }
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
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
                case 0x11:
                    result = null;
                    result = (String) msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                        dialog.setTitle(getString(R.string.message_delete_succeeded));
                        dialog.setMessage(getString(R.string.message_delete_succeeded_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                prog.show();
                                WebService_Resume wbResume = new WebService_Resume(handle, 0x01, selectTypeID, select_LaunchDate);
                                wbResume.start();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
                        dialog.setTitle(getString(R.string.message_delete_failed));
                        dialog.setMessage(getString(R.string.message_delete_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0x12:
                    select_LaunchDate = null;
                    select_LaunchDate = (String) msg.obj;

                    WebService_Resume wbResume = new WebService_Resume(handle, 0x01, selectTypeID, select_LaunchDate);
                    wbResume.start();
                    break;
                case 0xEE:
                    prog.dismiss();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume.this);
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

    private static <E> ArrayList<E> removeDuplicates(ArrayList<E> list2){

        ArrayList<E> usedList = new ArrayList<E>();
        ArrayList<E> newList = new ArrayList<E>();

        for(int i = 0; i < list2.size(); i++){

            E object = list2.get(i);

            if(! usedList.contains(object))
            {
                usedList.add(object);
                newList.add(object);
            }
        }

        return newList;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return resumeID.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return resumeID.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.listview_resume, null);

            TextView sDate = (TextView)convertView.findViewById(R.id.resume_date);
            TextView sIntroduce = (TextView)convertView.findViewById(R.id.resume_introduce);
            TextView sTitle = (TextView)convertView.findViewById(R.id.resume_title);

            sDate.setText(resumeDate.get(position).toString());
            sTitle.setText(resumeTitle.get(position).toString());
            sIntroduce.setText(resumeIntroduce.get(position).toString());

            return convertView;
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
