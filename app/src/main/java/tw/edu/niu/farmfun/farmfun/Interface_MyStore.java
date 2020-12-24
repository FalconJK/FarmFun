package tw.edu.niu.farmfun.farmfun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/12/9.
 */
public class Interface_MyStore extends Activity {

    private EditText nameEdit;
    private ImageView logoView;
    private Button cancelClick, acceptClick;

    private int titleID = 0;
    private int screenHeight = 0;
    private Bitmap myBitmap;
    private byte[] mContent;
    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interface_mystore);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5C755E")));

        prog = ProgressDialog.show(Interface_MyStore.this, getString(R.string.message_loading), getString(R.string.message_loading_info));
        prog.dismiss();

        noticeDialog();
        setupComponent();
        setupButtonListener();
        setupImageViewListener();
    }

    private  void noticeDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
        dialog.setTitle(getString(R.string.message_notice));
        dialog.setMessage(getString(R.string.message_notice_info4));
        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
            }
        });
        dialog.show();
    }

    public void setupComponent()
    {
        nameEdit = (EditText)findViewById(R.id.mystore_name_edit);
        logoView = (ImageView)findViewById(R.id.mystore_logo_view);
        cancelClick = (Button)findViewById(R.id.mystore_cancel_click);
        acceptClick = (Button)findViewById(R.id.mystore_accept_click);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        logoView.getLayoutParams().height = screenHeight/3;
        logoView.getLayoutParams().width = screenHeight/4;
        logoView.setImageResource(R.mipmap.icon_camera);
    }

    public void setupImageViewListener()
    {
        logoView.setOnLongClickListener(new ImageView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectPhoto();
                return false;
            }
        });
    }

    public void setupButtonListener()
    {
        cancelClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Interface_MyStore.this, Interface_Login.class);
                startActivity(intent);
                Interface_MyStore.this.finish();
            }
        });

        acceptClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEdit.getText().toString();
                final String logo = imgToString();

                if (name != null && !name.equals("")
                        && logo != null && !logo.equals("")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
                    dialog.setTitle(getString(R.string.message_insert));
                    dialog.setMessage(getString(R.string.message_insert_info));
                    dialog.setPositiveButton(getString(R.string.click_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            prog.show();
                            WebService_MyStore wbStore = new WebService_MyStore(handle, 0x01, name, logo);
                            wbStore.start();
                        }
                    });

                    dialog.setNegativeButton(getString(R.string.click_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    });
                    dialog.show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
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
                    titleID = 0;
                    titleID = (Integer)msg.obj;

                    if(titleID!=0)
                    {
                        prog.show();
                        WebService_MyStore wbStore = new WebService_MyStore(handle, 0x02, titleID);
                        wbStore.start();
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
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
                    String result = null;
                    result = (String)msg.obj;

                    if(result.equals("Succeed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
                        dialog.setTitle(getString(R.string.message_register_succeed));
                        dialog.setMessage(getString(R.string.message_register_succeed_info2) + " " + Integer.toString(titleID) + "\n" + getString(R.string.message_register_succeed_info3));
                        dialog.setPositiveButton(getString(R.string.alertdialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                Intent intent = new Intent();
                                intent.setClass(Interface_MyStore.this, Interface_Login.class);
                                startActivity(intent);
                                Interface_MyStore.this.finish();
                            }
                        });
                        dialog.show();
                    }
                    else if(result.equals("Failed"))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
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

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Interface_MyStore.this);
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
        logoView.buildDrawingCache();
        Bitmap bitmap = logoView.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] appicon = baos.toByteArray();

        String imgStr = null;
        imgStr =  Base64.encodeToString(appicon, Base64.DEFAULT);

        return imgStr;
    }

    private void selectPhoto()
    {
        final CharSequence[] items = {"相簿", "照相"};
        AlertDialog dlg = new AlertDialog.Builder(Interface_MyStore.this).setTitle("請選擇上傳方式").setItems(items,
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
                logoView.setImageBitmap(myBitmap);
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
            logoView.setImageBitmap(myBitmap);
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

        float height = logoView.getHeight();
        float width = logoView.getWidth();

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
