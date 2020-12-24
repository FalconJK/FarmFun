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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2016/3/29.
 */
public class Interface_Service  extends Activity
{
    private CheckBox service1Check, service2Check;
    private EditText service1Edit, service2Edit;
    private Button cancelClick, updateClick;

    private int costService1 = 0;
    private int costService2 = 0;
    private ArrayList<Integer> serviceResult = null;

    private DatabaseHelper dbhelper = null;
    private int identifyID = 0;
    private int titleID = 0;
    private int userID = 0;

    private ProgressDialog prog;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_service);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Service.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
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
                intent.setClass(Interface_Service.this, Interface_Service.class);
                startActivity(intent);
                Interface_Service.this.finish();
                break;
            case 1:
                intent.setClass(Interface_Service.this, Interface_Farm.class);
                startActivity(intent);
                Interface_Service.this.finish();
                break;
            case 2:
                intent.setClass(Interface_Service.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Service.this.finish();
                break;
            case 3:
                intent.setClass(Interface_Service.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Service.this.finish();
                break;
            case 4:
                intent.setClass(Interface_Service.this, Interface_Order.class);
                startActivity(intent);
                Interface_Service.this.finish();
                break;
            case 5:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Service.this, Interface_Register.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Service.this.finish();
                }
                else
                {
                    intent.setClass(Interface_Service.this, Interface_Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Service.this.finish();
                }
                break;
            case 6:
                intent.setClass(Interface_Service.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Service.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Service.this, Interface_Login.class);
                startActivity(intent);
                Interface_Service.this.finish();
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
        WebService_Service wbService = new WebService_Service(handle, 0x02, titleID);
        wbService.start();
    }

    private void setupComponent()
    {
        service1Check = (CheckBox)findViewById(R.id.service_one_check);
        service2Check = (CheckBox)findViewById(R.id.service_two_check);
        service1Edit = (EditText)findViewById(R.id.service_one_edit);
        service2Edit = (EditText)findViewById(R.id.service_two_edit);
        updateClick = (Button)findViewById(R.id.service_update_click);

        service1Check.setOnCheckedChangeListener(chklistener);
        service2Check.setOnCheckedChangeListener(chklistener);
    }

    private CheckBox.OnCheckedChangeListener chklistener = new CheckBox.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if(service1Check.isChecked()==true)
            {
                service1Check.setText(getString(R.string.service_one));
            }
            else
            {
                service1Check.setText("");
                service1Check.setHint(getString(R.string.service_one));
            }

            if(service2Check.isChecked()==true)
            {
                service2Check.setText(getString(R.string.service_two));
            }
            else
            {
                service2Check.setText("");
                service2Check.setHint(getString(R.string.service_two));
            }
        }
    };

    private void setupButtonListener()
    {
        updateClick.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(service1Check.isChecked()==true)
                {
                    if(!service1Edit.getText().toString().equals(""))
                    {
                        costService1 = Integer.parseInt(service1Edit.getText().toString());
                    }
                    else
                    {
                        costService1 = 0;
                    }
                }
                else
                {
                    costService1 = 0;
                }

                if(service2Check.isChecked()==true)
                {
                    if(!service2Edit.getText().toString().equals(""))
                    {
                        costService2 = Integer.parseInt(service2Edit.getText().toString());
                    }
                    else
                    {
                        costService2 = 0;
                    }
                }
                else
                {
                    costService2 = 0;
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Service.this);
                dialog.setTitle(getString(R.string.message_update));
                dialog.setMessage(getString(R.string.message_update_info));
                dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        prog.show();
                        WebService_Service wbService = new WebService_Service(handle, 0x03, titleID, costService1, costService2);
                        wbService.start();
                    }
                });
                dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                });
                dialog.show();
            }
        });
    }

    private Handler handle = new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    serviceResult = null;
                    serviceResult = (ArrayList<Integer>) msg.obj;

                    prog.dismiss();

                    if(serviceResult.get(0)!=0)
                    {
                        service1Check.setChecked(true);
                        service1Edit.setText(Integer.toString(serviceResult.get(0)));
                    }
                    else
                    {
                        service1Check.setChecked(false);
                        service1Check.setText("");
                        service1Check.setHint(getString(R.string.service_one));
                    }

                    if(serviceResult.get(1)!=0)
                    {
                        service2Check.setChecked(true);
                        service2Edit.setText(Integer.toString(serviceResult.get(1)));
                    }
                    else
                    {
                        service2Check.setChecked(false);
                        service2Check.setText("");
                        service2Check.setHint(getString(R.string.service_two));
                    }
                    break;
                case 0x02:
                    String result = null;
                    result = (String) msg.obj;

                    prog.dismiss();

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Service.this);
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Service.this);
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

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Service.this);
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
