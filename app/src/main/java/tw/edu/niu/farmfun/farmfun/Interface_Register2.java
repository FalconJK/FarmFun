package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2016/3/15.
 */
public class Interface_Register2 extends Activity {

    private EditText Account, Password, Name, Year, Month, Date, Phone, Email, Postel, Address;
    private Button Cancel, Send;
    private RadioGroup radiogroupSex;

    private String sex = null;
    private String Result = null;
    private boolean checkInfo;

    private ArrayList<String> userInfo = null;

    private DatabaseHelper dbhelper = null;
    private int userID = 0;
    private int titleID = 0;
    private int identifyID = 0;

    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_register2);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Register2.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        setupComponent();
        setupButtonListener();
        setupRadioButtonListener();
        getBundle();
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

        menu.getItem(0).setEnabled(false);
        menu.getItem(0).setVisible(false);

        if(identifyID!=1)
        {
            menu.getItem(4).setTitle(getString(R.string.login_buy));
            menu.getItem(6).setEnabled(false);
            menu.getItem(6).setVisible(false);
            menu.getItem(7).setEnabled(false);
            menu.getItem(7).setVisible(false);
        }

        if(!checkInfo)
        {
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setEnabled(false);
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setEnabled(false);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setEnabled(false);
            menu.getItem(5).setVisible(false);
            menu.getItem(6).setEnabled(false);
            menu.getItem(6).setVisible(false);
            menu.getItem(7).setEnabled(false);
            menu.getItem(7).setVisible(false);
            menu.getItem(8).setEnabled(false);
            menu.getItem(8).setVisible(false);
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
            case 1:
                intent.setClass(Interface_Register2.this, Interface_Farm.class);
                startActivity(intent);
                Interface_Register2.this.finish();
                break;
            case 2:
                intent.setClass(Interface_Register2.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Register2.this.finish();
                break;
            case 3:
                intent.setClass(Interface_Register2.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Register2.this.finish();
                break;
            case 4:
                intent.setClass(Interface_Register2.this, Interface_Order.class);
                startActivity(intent);
                Interface_Register2.this.finish();
                break;
            case 6:
                intent.setClass(Interface_Register2.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Register2.this.finish();
                break;
            case 7:
                intent.setClass(Interface_Register2.this, Interface_Service.class);
                startActivity(intent);
                Interface_Register2.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Register2.this, Interface_Login.class);
                startActivity(intent);
                Interface_Register2.this.finish();
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


    private void getBundle()
    {
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null)
        {
            checkInfo = bundle.getBoolean("CheckInfo");
            if(checkInfo)
            {
                Cancel.setEnabled(false);
                Cancel.setVisibility(View.INVISIBLE);
                Send.setText(getString(R.string.click_update));

                getMemberID();

                prog.show();
                WebService_Login wbLogin = new WebService_Login(handle, 0x09, userID);
                wbLogin.start();
            }
        }

    }

    private  void noticeDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
        dialog.setTitle(getString(R.string.message_notice));
        dialog.setMessage(getString(R.string.message_notice_info1) + getString(R.string.message_notice_info2) + getString(R.string.message_notice_info3));
        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
            }
        });
        dialog.show();
    }

    private void setupComponent()
    {
        Account = (EditText)findViewById(R.id.register_account_edit2);
        Password = (EditText)findViewById(R.id.register_password_edit2);
        Name = (EditText)findViewById(R.id.register_name_edit2);
        Year = (EditText)findViewById(R.id.register_year_edit2);
        Month = (EditText)findViewById(R.id.register_month_edit2);
        Date = (EditText)findViewById(R.id.register_date_edit2);
        Phone = (EditText)findViewById(R.id.register_phone_edit2);
        Email = (EditText)findViewById(R.id.register_mail_edit2);
        Postel = (EditText)findViewById(R.id.register_postel_edit2);
        Address = (EditText)findViewById(R.id.register_address_edit2);
        radiogroupSex=(RadioGroup)findViewById(R.id.group_sex2);
        Send = (Button)findViewById(R.id.register_send2);
        Cancel = (Button)findViewById(R.id.register_cancel2);
        checkInfo = false;
    }

    public void setupButtonListener()
    {
        Send.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String account = Account.getText().toString();
                final String password = Password.getText().toString();
                final String name = Name.getText().toString();
                final String birth = Year.getText().toString()+"/"+Month.getText().toString()+"/"+Date.getText().toString();
                final String phone = Phone.getText().toString();
                final String mail = Email.getText().toString();
                final String address = Address.getText().toString();

                if (account != null && !account.equals("")
                        && password != null && !password.equals("")
                        && name != null && !name.equals("")
                        && phone != null && !phone.equals("")
                        && mail != null && !mail.equals("")
                        && Postel.getText().toString()!=null && !Postel.getText().toString().equals("")
                        && address != null && !mail.equals(""))
                {
                    if(checkInfo)
                    {
                        AlertDialog.Builder updateDialog = new AlertDialog.Builder(Interface_Register2.this);
                        updateDialog.setTitle(getString(R.string.message_update));
                        updateDialog.setMessage(getString(R.string.message_update_info));
                        updateDialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                prog.show();
                                WebService_Login wbLogin = new WebService_Login(handle, 0x10,
                                        userID,
                                        account,
                                        password,
                                        name,
                                        sex,
                                        birth,
                                        phone,
                                        mail,
                                        Integer.parseInt(Postel.getText().toString()),
                                        address);
                                wbLogin.start();
                            }
                        });
                        updateDialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        updateDialog.show();
                    }
                    else
                    {
                        AlertDialog.Builder InsertDialog = new AlertDialog.Builder(Interface_Register2.this);
                        InsertDialog.setTitle(getString(R.string.message_send_signup));
                        InsertDialog.setMessage(getString(R.string.message_send_signup_info));
                        InsertDialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                prog.show();
                                WebService_Login wbLogin = new WebService_Login(handle, 0x11,
                                        account,
                                        password,
                                        name,
                                        sex,
                                        birth,
                                        phone,
                                        mail,
                                        Integer.parseInt(Postel.getText().toString()),
                                        address);
                                wbLogin.start();
                            }
                        });
                        InsertDialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        InsertDialog.show();
                    }
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
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

        Cancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                onDestroy();
                Intent intent = new Intent();
                intent.setClass(Interface_Register2.this, Interface_Login.class);
                startActivity(intent);
                Interface_Register2.this.finish();
            }
        });
    }

    public void setupRadioButtonListener()
    {
        radiogroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.rmale2:
                        sex = "M";
                        break;
                    case R.id.rfemale2:
                        sex = "F";
                        break;
                    default:
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
                    Result = null;
                    Result = (String) msg.obj;

                    prog.dismiss();

                    if(Result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
                        dialog.setTitle(getString(R.string.message_register_succeed));
                        dialog.setMessage(getString(R.string.message_register_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Register2.this, Interface_Login.class);
                                startActivity(intent);
                                Interface_Register2.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(Result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
                        dialog.setTitle(getString(R.string.message_register_failed));
                        dialog.setMessage(getString(R.string.message_register_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x02:
                    userInfo = null;
                    userInfo = (ArrayList<String>) msg.obj;

                    prog.dismiss();

                    Name.setText(userInfo.get(0));
                    Phone.setText(userInfo.get(3));
                    Email.setText(userInfo.get(4));
                    Postel.setText(userInfo.get(5));
                    Address.setText(userInfo.get(6));

                    if(userInfo.get(1).toString().equals("M"))
                    {
                        radiogroupSex.check(R.id.rmale2);
                    }
                    else if(userInfo.get(1).toString().equals("F"))
                    {
                        radiogroupSex.check(R.id.rfemale2);
                    }

                    String beforeStr = userInfo.get(2).toString();
                    String[] afterStr = beforeStr.split("/");
                    Year.setText(afterStr[0]);
                    Month.setText(afterStr[1]);
                    Date.setText(afterStr[2]);
                    break;
                case 0x03:
                    Result = null;
                    Result = (String) msg.obj;

                    prog.dismiss();

                    if(Result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
                        dialog.setTitle(getString(R.string.message_update_succeed));
                        dialog.setMessage(getString(R.string.message_update_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                onDestroy();
                                Intent intent = new Intent();
                                intent.setClass(Interface_Register2.this, Interface_Login.class);
                                startActivity(intent);
                                Interface_Register2.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(Result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
                        dialog.setTitle(getString(R.string.message_register_failed));
                        dialog.setMessage(getString(R.string.message_register_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0xEE:
                    prog.dismiss();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Register2.this);
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
