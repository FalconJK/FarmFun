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
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static tw.edu.niu.farmfun.farmfun.DbConstants.IDENTIFY;
import static tw.edu.niu.farmfun.farmfun.DbConstants.MEMBER;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TABLE_NAME;
import static tw.edu.niu.farmfun.farmfun.DbConstants.TITLE;

/**
 * Created by Waileong910910 on 2015/12/8.
 */
public class Interface_Bank extends Activity {

    private EditText bankSigner, bankWhich, bankCode, bankAccount;
    private Button cancelClick, acceptClick;
    private LinearLayout layoutEditor;
    private ListView bankList;

    MyAdapter adapter = null;

    private String Result = null;
    private ArrayList<Integer> Id = null;
    private ArrayList<String> Signer = null;
    private ArrayList<String> Which = null;
    private ArrayList<String> Code = null;
    private ArrayList<String> Account = null;

    private DatabaseHelper dbhelper = null;
    private int identifyID = 0;
    private int titleID = 0;
    private int userID = 0;

    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_bank);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_Bank.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
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
                intent.setClass(Interface_Bank.this, Interface_Bank.class);
                startActivity(intent);
                Interface_Bank.this.finish();
                break;
            case 1:
                intent.setClass(Interface_Bank.this, Interface_Farm.class);
                startActivity(intent);
                Interface_Bank.this.finish();
                break;
            case 2:
                intent.setClass(Interface_Bank.this, Interface_Crop.class);
                startActivity(intent);
                Interface_Bank.this.finish();
                break;
            case 3:
                intent.setClass(Interface_Bank.this, Interface_Resume.class);
                startActivity(intent);
                Interface_Bank.this.finish();
                break;
            case 4:
                intent.setClass(Interface_Bank.this, Interface_Order.class);
                startActivity(intent);
                Interface_Bank.this.finish();
                break;
            case 5:
                if(identifyID==1)
                {
                    intent.setClass(Interface_Bank.this, Interface_Register.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Bank.this.finish();
                }
                else
                {
                    intent.setClass(Interface_Bank.this, Interface_Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("CheckInfo", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Interface_Bank.this.finish();
                }
                break;
            case 7:
                intent.setClass(Interface_Bank.this, Interface_Service.class);
                startActivity(intent);
                Interface_Bank.this.finish();
                break;
            case 8:
                intent.setClass(Interface_Bank.this, Interface_Login.class);
                startActivity(intent);
                Interface_Bank.this.finish();
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
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null)
        {
            boolean checkBank = bundle.getBoolean("CheckBank");
            int giveMeID = bundle.getInt("GiveMeID");
            if(checkBank)
            {
                WebService_Bank wbBank = new WebService_Bank(handle, 0x01, giveMeID);
                wbBank.start();
            }
        }
        else
        {
            WebService_Bank wbBank = new WebService_Bank(handle, 0x01, titleID);
            wbBank.start();
        }
    }

    private void setupComponent()
    {
        layoutEditor = (LinearLayout)findViewById(R.id.bank_layout_view);
        bankSigner = (EditText)findViewById(R.id.bank_signer_edit);
        bankWhich = (EditText)findViewById(R.id.bank_which_edit);
        bankCode = (EditText)findViewById(R.id.bank_code_edit);
        bankAccount = (EditText)findViewById(R.id.bank_account_edit);
        bankList = (ListView)findViewById(R.id.bank_list);
        cancelClick = (Button)findViewById(R.id.bank_cancel_click);
        acceptClick = (Button)findViewById(R.id.bank_insert_click);

        if(identifyID!=1)
        {
            closeOptionsMenu();

            layoutEditor.setEnabled(false);
            acceptClick.setEnabled(false);

            layoutEditor.setVisibility(View.INVISIBLE);
            acceptClick.setVisibility(View.INVISIBLE);
            cancelClick.setText(getString(R.string.click_back));
        }
        else
        {
            cancelClick.setEnabled(false);
            cancelClick.setVisibility(View.INVISIBLE);
        }

        adapter = new MyAdapter(this);
    }

    private void setupButtonListener()
    {
        cancelClick.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onDestroy();

                if(identifyID==1)
                {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Bank.this, Interface_Farm.class);
                    startActivity(intent);
                    Interface_Bank.this.finish();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setClass(Interface_Bank.this, Interface_Order.class);
                    startActivity(intent);
                    Interface_Bank.this.finish();
                }
            }
        });

        acceptClick.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String signer = bankSigner.getText().toString();
                final String which = bankWhich.getText().toString();
                final String code = bankCode.getText().toString();
                final String account = bankAccount.getText().toString();

                if (signer != null && !signer.equals("")
                        && which != null && !which.equals("")
                        && code != null && !code.equals("")
                        && account != null && !account.equals("")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
                    dialog.setTitle(getString(R.string.message_insert));
                    dialog.setMessage(getString(R.string.message_insert_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            prog.show();
                            WebService_Bank wbBank = new WebService_Bank(handle, 0x02, titleID, signer, which, code, account);
                            wbBank.start();
                        }
                    });

                    dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    });
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
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
    }

    private Handler handle = new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x01:
                    Id = null;
                    Id = (ArrayList<Integer>) msg.obj;
                    break;
                case 0x02:
                    Signer = null;
                    Signer = (ArrayList<String>) msg.obj;
                    break;
                case 0x03:
                    Which = null;
                    Which = (ArrayList<String>) msg.obj;
                    break;
                case 0x04:
                    Code = null;
                    Code = (ArrayList<String>) msg.obj;
                    break;
                case 0x05:
                    Account = null;
                    Account = (ArrayList<String>) msg.obj;

                    prog.dismiss();
                    if(Id.get(0)!=0)
                    {
                        bankList.setAdapter(adapter);
                    }
                    else
                    {
                        if(identifyID==1)
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
                            dialog.setTitle(getString(R.string.message_no_data));
                            dialog.setMessage(getString(R.string.message_no_data_info));
                            dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i){}
                            });
                            dialog.show();
                        }
                        else
                        {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
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
                case 0x06:
                    Result = null;
                    Result = (String)msg.obj;
                    if(Result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
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
                    else
                    if(Result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
                        dialog.setTitle(getString(R.string.message_insert_failed));
                        dialog.setMessage(getString(R.string.message_insert_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i) {}
                        });
                        dialog.show();
                    }
                    break;
                case 0x07:
                    Result = null;
                    Result = (String)msg.obj;
                    if(Result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
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
                    else
                    if(Result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
                        dialog.setTitle(getString(R.string.message_delete_failed));
                        dialog.setMessage(getString(R.string.message_delete_failed_info));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i){}
                        });
                        dialog.show();
                    }
                    break;
                case 0xEE:
                    prog.dismiss();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
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

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater myInflater;

        public MyAdapter(Context c) {
            myInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Signer.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return Signer.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            convertView = myInflater.inflate(R.layout.listview_bank, null);

            TextView ssigner = (TextView)convertView.findViewById(R.id.bank_signer_view);
            TextView swhich = (TextView)convertView.findViewById(R.id.bank_which_view);
            TextView saccount = (TextView)convertView.findViewById(R.id.bank_account_view);
            ImageButton sdelete = (ImageButton)convertView.findViewById(R.id.bank_delete_click);

            if(identifyID!=1)
            {
                sdelete.setEnabled(false);
                sdelete.setVisibility(View.INVISIBLE);
            }

            ssigner.setText(getString(R.string.bank_name)+Signer.get(position).toString());
            swhich.setText(getString(R.string.bank_which)+Which.get(position).toString());
            saccount.setText(getString(R.string.bank_code)+Code.get(position).toString() + "\n" + getString(R.string.bank_account)+Account.get(position).toString());

            sdelete.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_Bank.this);
                    dialog.setTitle(getString(R.string.message_delete));
                    dialog.setMessage(getString(R.string.message_delete_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                            prog.show();
                            WebService_Bank wbBank = new WebService_Bank(handle, 0x03, Id.get(position));
                            wbBank.start();
                        }
                    });

                    dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    });
                    dialog.show();
                }
            });

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
