package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

public class Interface_Login extends Activity
{
    final Context context = this;

    private EditText accountEdit, passwordEdit;
    private Button loginClick, leaveClick, farmClick, cropClick, resumeClick, storeCLick;
    private CheckBox rememberMe;
    private CheckBox seller, customer;
    private ImageView logoView;

    private String memberID;
    private String titleID;
    private String titleLogo;
    private boolean boolLogin;
    private ProgressDialog prog;

    private DatabaseHelper dbhelper = null;

    private String userNameValue,passwordValue;
    private SharedPreferences sp;

    private boolean identifyLogin;
    private int identifyID;

    private ArrayAdapter<String> marketAdapter;
    private ArrayList<Integer> marketID = null;
    private ArrayList <String> marketName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

        dbhelper = new DatabaseHelper(this);
        del();

        setupComponent();
        setCheckBoxListener();
        setupButtonListener();

        boolButtonFunction(false);
        setupRememberMe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, getString(R.string.actionbar_mystoresignup));
        menu.add(0, 1, 1, getString(R.string.actionbar_membersignup));
        menu.add(0, 2, 2, getString(R.string.actionbar_membersignup2));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case 0:
                Intent intent0 = new Intent();
                intent0.setClass(Interface_Login.this, Interface_MyStore.class);
                startActivity(intent0);
                Interface_Login.this.finish();
                break;
            case 1:
                Intent intent1 = new Intent();
                intent1.setClass(Interface_Login.this, Interface_Register.class);
                startActivity(intent1);
                Interface_Login.this.finish();
                break;
            case 2:
                Intent intent2 = new Intent();
                intent2.setClass(Interface_Login.this, Interface_Register2.class);
                startActivity(intent2);
                Interface_Login.this.finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupComponent()
    {
        accountEdit = (EditText)findViewById(R.id.login_account_edit);
        passwordEdit = (EditText)findViewById(R.id.login_password_edit);
        rememberMe = (CheckBox)findViewById(R.id.login_remember_check);
        loginClick  = (Button)findViewById(R.id.login_enter_click);
        leaveClick  = (Button)findViewById(R.id.login_leave_click);
        farmClick = (Button)findViewById(R.id.login_farm_click);
        cropClick = (Button)findViewById(R.id.login_crop_click);
        resumeClick = (Button)findViewById(R.id.login_resume_click);
        storeCLick = (Button)findViewById(R.id.login_store_click);
        logoView = (ImageView)findViewById(R.id.login_photo);

        seller = (CheckBox)findViewById(R.id.login_seller);
        customer = (CheckBox)findViewById(R.id.login_customer);
        seller.setChecked(true);

        logoView.setImageResource(R.mipmap.farmfun_logo);

        identifyLogin = true;
        identifyID = 1;
    }

    private void setupRememberMe()
    {
        if(sp.getBoolean("ISCHECK", false))
        {
            rememberMe.setChecked(true);
            accountEdit.setText(sp.getString("USER_NAME", ""));
            passwordEdit.setText(sp.getString("PASSWORD", ""));
        }
    }

    public void setCheckBoxListener()
    {
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rememberMe.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }

            }
        });

        seller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (seller.isChecked()) {
                    customer.setChecked(false);
                    identifyLogin = true;
                    identifyID = 1;

                    storeCLick.setText(getString(R.string.login_store));
                }
            }
        });

        customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (customer.isChecked()) {
                    seller.setChecked(false);
                    identifyLogin = false;
                    identifyID = 0;

                    storeCLick.setText(getString(R.string.login_buy));
                }
            }
        });
    }

    public void setupButtonListener()
    {
        loginClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (boolLogin) {
                    String account = accountEdit.getText().toString();
                    String password = passwordEdit.getText().toString();

                    if (account != null && !account.equals("") && password != null && !password.equals("")) {
                        prog = ProgressDialog.show(Interface_Login.this, getString(R.string.message_loading), getString(R.string.message_loading_info));

                        if (identifyLogin) {
                            WebService_Login wbLogin = new WebService_Login(handle, 0x01, account, password);
                            wbLogin.start();
                        } else {
                            WebService_Login wbLogin = new WebService_Login(handle, 0x07, account, password);
                            wbLogin.start();
                        }

                        if (rememberMe.isChecked()) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", account);
                            editor.putString("PASSWORD", password);
                            editor.commit();
                        }

                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
                        dialog.setTitle(getString(R.string.message_login_blank));
                        dialog.setMessage(getString(R.string.message_login_blank_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        });
                        dialog.show();
                    }
                } else {
                    memberID = null;
                    passwordEdit.setText("");
                    boolButtonFunction(false);
                    logoView.setImageDrawable(null);
                    logoView.setImageResource(R.mipmap.farmfun_logo);
                }
            }
        });

        farmClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identifyLogin) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Farm.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                } else {
                    selectMarket(1);
                }
            }
        });

        cropClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identifyLogin) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Crop.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                } else {
                    selectMarket(2);
                }
            }
        });

        resumeClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identifyLogin) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Resume.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                } else {
                    selectMarket(3);
                }
            }
        });

        storeCLick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!identifyLogin)
                {
                    titleID = "0";
                    add();
                }
                Intent intent = new Intent();
                intent.setClass(Interface_Login.this, Interface_Order.class);
                startActivity(intent);
                Interface_Login.this.finish();
            }
        });

        leaveClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Interface_Login.this.finish();
            }
        });

    }

    Handler handle = new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    memberID = null;
                    memberID = (String)msg.obj;

                    if(!memberID.equals("No Date"))
                    {
                        if(identifyLogin)
                        {
                            WebService_Login wbLogin = new WebService_Login(handle, 0x04, Integer.parseInt(memberID));
                            wbLogin.start();
                        }else{
                            WebService_Login wbLogin = new WebService_Login(handle, 0x08);
                            wbLogin.start();
                        }
                    }
                    else
                    {
                        prog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
                        dialog.setTitle(getString(R.string.message_no_account));
                        dialog.setMessage(getString(R.string.message_no_account_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                passwordEdit.setText("");
                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x02:
                    titleID = null;
                    titleID = (String)msg.obj;

                    if(!titleID.equals("No Date"))
                    {
                        WebService_Login wbLogin = new WebService_Login(handle, 0x06, Integer.parseInt(titleID));
                        wbLogin.start();
                    }
                    else
                    {
                        prog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
                        dialog.setTitle(getString(R.string.message_account_error));
                        dialog.setMessage(getString(R.string.message_account_error_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

                            }
                        });
                        dialog.show();
                    }
                    break;
                case 0x03:
                    titleLogo = null;
                    titleLogo = (String)msg.obj;

                    prog.dismiss();
                    if(!titleLogo.equals("No Data"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
                        dialog.setTitle(getString(R.string.message_login_succeed));
                        dialog.setMessage(getString(R.string.message_login_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
                                imm.hideSoftInputFromWindow(Interface_Login.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                                add();
                                boolButtonFunction(true);

                                logoView.setImageBitmap(stringToBitmap(titleLogo));
                                UpdateUrlCount();
                            }
                        });
                        dialog.show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
                        dialog.setTitle(getString(R.string.message_account_error));
                        dialog.setMessage(getString(R.string.message_account_error_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {

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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
                        dialog.setTitle(getString(R.string.message_login_succeed));
                        dialog.setMessage(getString(R.string.message_login_succeed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
                                imm.hideSoftInputFromWindow(Interface_Login.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                                boolButtonFunction(true);
                                UpdateUrlCount();
                            }
                        });
                        dialog.show();
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
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
                    break;
                case 0xEE:
                    prog.dismiss();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Login.this);
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

    public void selectMarket(final int selectID)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_market);
        dialog.setTitle(getString(R.string.select_market));

        Button cancel = (Button) dialog.findViewById(R.id.market_cancel_click);
        Button accept = (Button) dialog.findViewById(R.id.market_accept_click);
        Spinner selection = (Spinner)dialog.findViewById(R.id.spn_market);

        marketAdapter = new ArrayAdapter<String>(Interface_Login.this, R.layout.item_spinner, marketName);
        marketAdapter.setDropDownViewResource(R.layout.item_spinner);
        selection.setAdapter(marketAdapter);

        selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                titleID = Integer.toString(marketID.get(position));
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
                add();
                if (selectID == 1) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Farm.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                }

                if (selectID == 2) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Crop.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                }

                if (selectID == 3) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Resume.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                }

                if (selectID == 4) {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Login.this, Interface_Order.class);
                    startActivity(intent);
                    Interface_Login.this.finish();
                }
            }
        });


        dialog.show();
    }

    public void boolButtonFunction(boolean locker)
    {
        if(locker)
        {
            farmClick.setEnabled(true);
            cropClick.setEnabled(true);
            resumeClick.setEnabled(true);
            storeCLick.setEnabled(true);

            farmClick.setBackgroundResource(R.drawable.buttonshape);
            cropClick.setBackgroundResource(R.drawable.buttonshape);
            resumeClick.setBackgroundResource(R.drawable.buttonshape);
            storeCLick.setBackgroundResource(R.drawable.buttonshape);
            loginClick.setBackgroundResource(R.drawable.buttonshapered);
            loginClick.setText(getString(R.string.logout_enter));

            boolLogin = false;
        }
        else
        {
            farmClick.setEnabled(false);
            cropClick.setEnabled(false);
            resumeClick.setEnabled(false);
            storeCLick.setEnabled(false);

            farmClick.setBackgroundResource(R.drawable.buttonshapered);
            cropClick.setBackgroundResource(R.drawable.buttonshapered);
            resumeClick.setBackgroundResource(R.drawable.buttonshapered);
            storeCLick.setBackgroundResource(R.drawable.buttonshapered);
            loginClick.setBackgroundResource(R.drawable.buttonshape);
            loginClick.setText(getString(R.string.login_enter));

            boolLogin = true;
        }
    }

    public void add()
    {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEMBER, memberID.toString());
        values.put(TITLE, titleID);
        values.put(IDENTIFY, Integer.toString(identifyID));
        db.insert(TABLE_NAME, null, values);
    }

    private void del()
    {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {

        }
        return false;
    }

    private void UpdateUrlCount()
    {
        String url = "http://140.115.197.16/?school=niu&app=farmfun";
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try{
            HttpResponse response = client.execute(request);
            Log.i("aaa","aaaaaaaaaaaa");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            client.getConnectionManager().shutdown();
        }
    }
}