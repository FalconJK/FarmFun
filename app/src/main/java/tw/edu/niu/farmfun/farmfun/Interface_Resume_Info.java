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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2015/10/29.
 */
public class Interface_Resume_Info extends Activity {

    final Context context = this;

    private EditText Date, Introduce, Title;
    private CalendarView Calander;
    private Spinner spn_Work, spn_Farm, spn_Type;
    private Button clickCancel, clickAccept, insertType;
    private ImageButton  clickCalender;

    private int selectFarmID = 0;
    private int selectTypeID = 0;
    private String[] work = {"下市", "接枝", "嫁接", "疏枝", "套裝",  "施肥", "噴灑"};
    private ArrayList<Integer> farmID = null;
    private ArrayList <String> farmName = null;
    private ArrayList<Integer> typeID = null;
    private ArrayList <String> typeName = null;
    private ArrayAdapter<String> workAdapter;
    private ArrayAdapter<String> farmAdapter;
    private ArrayAdapter<String> typeAdapter;
    private int titleID = 0;
    private String strCalender;
    ProgressDialog prog;

    private DatabaseHelper dbhelper = null;
    private int identifyID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_resume);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Resume_Info.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        getMemberID();
        setupComponent();
        setupButtonListener();
        setupSpinnerListener();
        startData();
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

        WebService_Resume wbResume = new WebService_Resume(handle, 0x04, titleID);
        wbResume.start();
    }

    public void setupComponent()
    {
        Date = (EditText)findViewById(R.id.resume_date_edit);
        Title = (EditText)findViewById(R.id.resume_title_edit);
        Introduce = (EditText)findViewById(R.id.resume_introduce_edit);
        spn_Work = (Spinner)findViewById(R.id.resume_title_select);
        spn_Farm = (Spinner)findViewById(R.id.resume_farm_select);
        spn_Type = (Spinner)findViewById(R.id.resume_type_select);
        clickCancel = (Button)findViewById(R.id.resume_cancel_click);
        clickAccept = (Button)findViewById(R.id.resume_accept_click);
        clickCalender = (ImageButton)findViewById(R.id.calender_click);
        insertType = (Button)findViewById(R.id.resume_type_insert_click);

        //Locking
        insertType.setEnabled(false);
        insertType.setVisibility(View.INVISIBLE);
    }

    public void setupButtonListener()
    {
        clickAccept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String date = Date.getText().toString();
                final String title = Title.getText().toString();
                final String intr = Introduce.getText().toString();

                if (date != null && !date.equals("")
                        && title != null && !title.equals("")
                        && intr != null && !intr.equals("")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
                    dialog.setTitle(getString(R.string.message_insert));
                    dialog.setMessage(getString(R.string.message_insert_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i)
                        {
                            prog.show();
                            WebService_Resume wbResume = new WebService_Resume(handle, 0x02, selectTypeID, date, title, intr);
                            wbResume.start();
                        }
                    });

                    dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    });
                    dialog.show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
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

        clickCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                Intent intent = new Intent();
                intent.setClass(Interface_Resume_Info.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Resume_Info.this.finish();
            }
        });

        clickCalender.setOnClickListener(new ImageButton.OnClickListener() {
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
                        Date.setText(strCalender);
                        dialog.dismiss();
                    }
                });

                dialog.show();
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
                            WebService_Crop wbFarm = new WebService_Crop(handle, 0x07, selectFarmID, name.getText().toString());
                            wbFarm.start();

                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    public void setupSpinnerListener()
    {
        workAdapter = new ArrayAdapter<String>(Interface_Resume_Info.this, R.layout.item_spinner, work);
        workAdapter.setDropDownViewResource(R.layout.item_spinner);
        spn_Work.setAdapter(workAdapter);

        spn_Work.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Title.setText(work[position].toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void FarmSpinnerListener()
    {
        farmAdapter = new ArrayAdapter<String>(Interface_Resume_Info.this, R.layout.item_spinner, farmName);
        farmAdapter.setDropDownViewResource(R.layout.item_spinner);
        spn_Farm.setAdapter(farmAdapter);

        spn_Farm .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

    public void TypeSpinnerListener()
    {
        typeAdapter = new ArrayAdapter<String>(Interface_Resume_Info.this, R.layout.item_spinner, typeName);
        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
        spn_Type.setAdapter(typeAdapter);

        spn_Type .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selectTypeID = typeID.get(position);
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
                        dialog.setTitle(getString(R.string.message_no_data));
                        dialog.setMessage(getString(R.string.message_no_data_info2));
                        dialog.setPositiveButton(getString(R.string.click_insert_new_farm), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                onDestroy();
                                Intent intent1 = new Intent();
                                intent1.setClass(Interface_Resume_Info.this, Interface_Farm.class);
                                startActivity(intent1);
                                Interface_Resume_Info.this.finish();
                            }
                        });
                        dialog.show();
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
                        dialog.setTitle(getString(R.string.message_no_data));
                        dialog.setMessage(getString(R.string.message_no_data_info7));
                        dialog.setPositiveButton(getString(R.string.click_send), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x05:
                    String result = null;
                    result = (String)msg.obj;

                    prog.dismiss();
                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_succeed));
                        dialog.setMessage(getString(R.string.message_insert_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Resume_Info.this, Interface_Resume.class);
                                startActivity(intent);
                                Interface_Resume_Info.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
                        dialog.setTitle(getString(R.string.message_update_failed));
                        dialog.setMessage(getString(R.string.message_update_failed_info));
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
                        dialog.setTitle(getString(R.string.message_insert_succeed));
                        dialog.setMessage(getString(R.string.message_insert_succeed_info));
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Resume_Info.this);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

        }
        return false;
    }
}
