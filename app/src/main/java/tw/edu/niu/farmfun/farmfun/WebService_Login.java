package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/10/26.
 */
public class WebService_Login extends Thread
{

    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public String account;
    public String password;

    public String arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg9, arg10, arg11, arg12, arg13;
    public int arg8, arg14;

    Handler handle;
    int type;

    public WebService_Login(Handler handle, int type) {
        this.handle = handle;
        this.type = type;
    }

    public WebService_Login(Handler handle, int type, int arg8) {
        this.handle = handle;
        this.arg8 = arg8;
        this.type = type;
    }

    public WebService_Login(Handler handle, int type, String data1, String data2) {
        this.handle = handle;
        this.account = data1;
        this.password = data2;
        this.type = type;
    }

    public WebService_Login(Handler handle, int type, int arg14, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, int arg8, String arg9) {
        this.handle = handle;
        this.arg14 = arg14;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.arg9 = arg9;
        this.type = type;
    }

    public WebService_Login(Handler handle, int type, int arg14, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, int arg8, String arg9, String arg10, String arg11, String arg12, String arg13) {
        this.handle = handle;
        this.arg14 = arg14;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.arg9 = arg9;
        this.arg10 = arg10;
        this.arg11 = arg11;
        this.arg12 = arg12;
        this.arg13 = arg13;
        this.type = type;
    }

    public WebService_Login(Handler handle, int type, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, int arg8, String arg9, String arg10, String arg11, String arg12, String arg13, int arg14) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.arg9 = arg9;
        this.arg10 = arg10;
        this.arg11 = arg11;
        this.arg12 = arg12;
        this.arg13 = arg13;
        this.arg14 = arg14;
        this.type = type;
    }

    public WebService_Login(Handler handle, int type, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, int arg8, String arg9) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.arg9 = arg9;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                login(account, password);
                break;
            case 0x02:
                register(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
                break;
            case 0x03:
                getUserInfo(arg8);
                break;
            case 0x04:
                getTitleID(arg8);
                break;
            case 0x05:
                update_user(arg14, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
                break;
            case 0x06:
                getTitleLogo(arg8);
                break;

            case 0x07:
                login2(account, password);
                break;
            case 0x08:
                getAllMarketID();
                getAllMarketName();
                break;
            case 0x09:
                getCustomInfo(arg8);
                break;
            case 0x10:
                update_customer(arg14, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
                break;
            case 0x11:
                register2(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
                break;
        }
    }

    public String login(String account, String password)
    {
        String SOAP_ACTION = "http://tempuri.org/Account_Login";
        String METHOD_NAME = "Account_Login";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Account", account);
        request.addProperty("Password", password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
               handle.obtainMessage(0x01, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String login2(String account, String password)
    {
        String SOAP_ACTION = "http://tempuri.org/Customer_Login";
        String METHOD_NAME = "Customer_Login";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Account", account);
        request.addProperty("Password", password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x01, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getAllMarketID()
    {
        String SOAP_ACTION = "http://tempuri.org/AllMarketID";
        String METHOD_NAME = "AllMarketID";

        ArrayList<Integer> id = new ArrayList<Integer>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;

            int count=((SoapObject)result.getProperty(0)).getPropertyCount();
            for(int i = 0; i < count; i++)
            {
                String str=((SoapObject)result.getProperty(0)).getProperty(i).toString();
                id.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x13, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }
    public String getAllMarketName()
    {
        String SOAP_ACTION = "http://tempuri.org/AllMarketName";
        String METHOD_NAME = "AllMarketName";

        ArrayList<String> name = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;

            int count=((SoapObject)result.getProperty(0)).getPropertyCount();
            for(int i = 0; i < count; i++)
            {
                String str=((SoapObject)result.getProperty(0)).getProperty(i).toString();
                name.add(str);
            }

            handle.obtainMessage(0x14, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String register(String account, String password, String name, String sex, String birth, String phone, String mail, int postel, String address, String store, String workstart, String workend, String introduce, int code)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_User";
        String METHOD_NAME = "Insert_User";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Account", account);
        request.addProperty("Password", password);
        request.addProperty("Name", name);
        request.addProperty("Sex", sex);
        request.addProperty("Birth", birth);
        request.addProperty("Phone", phone);
        request.addProperty("Email", mail);
        request.addProperty("Post", postel);
        request.addProperty("Address", address);
        request.addProperty("StoreName", store);
        request.addProperty("Rank", 0);
        request.addProperty("WorkTimeStart", workstart);
        request.addProperty("WorkTimeEnd", workend);
        request.addProperty("Introduce", introduce);
        request.addProperty("TitleID", code);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x01, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String register2(String account, String password, String name, String sex, String birth, String phone, String mail, int postel, String address)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Customer";
        String METHOD_NAME = "Insert_Customer";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("Account", account);
        request.addProperty("Password", password);
        request.addProperty("Name", name);
        request.addProperty("Sex", sex);
        request.addProperty("Birth", birth);
        request.addProperty("Phone", phone);
        request.addProperty("Mail", mail);
        request.addProperty("Post", postel);
        request.addProperty("Address", address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x01, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String update_user(int memberID, String account, String password, String name, String sex, String birth, String phone, String mail, int postel, String address, String store, String workstart, String workend, String introduce)
    {
        String SOAP_ACTION = "http://tempuri.org/Update_User";
        String METHOD_NAME = "Update_User";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Id", memberID);
        request.addProperty("Account", account);
        request.addProperty("Password", password);
        request.addProperty("Name", name);
        request.addProperty("Sex", sex);
        request.addProperty("Birth", birth);
        request.addProperty("Phone", phone);
        request.addProperty("Email", mail);
        request.addProperty("Post", postel);
        request.addProperty("Address", address);
        request.addProperty("StoreName", store);
        request.addProperty("Rank", 0);
        request.addProperty("WorkTimeStart", workstart);
        request.addProperty("WorkTimeEnd", workend);
        request.addProperty("Introduce", introduce);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x03, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String update_customer(int memberID, String account, String password, String name, String sex, String birth, String phone, String mail, int postel, String address)
    {
        String SOAP_ACTION = "http://tempuri.org/Update_Customer";
        String METHOD_NAME = "Update_Customer";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Id", memberID);
        request.addProperty("Account", account);
        request.addProperty("Password", password);
        request.addProperty("Name", name);
        request.addProperty("Sex", sex);
        request.addProperty("Birth", birth);
        request.addProperty("Phone", phone);
        request.addProperty("Email", mail);
        request.addProperty("Post", postel);
        request.addProperty("Address", address);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x03, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String getUserInfo(int memberID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_UserInfo";
        String METHOD_NAME = "Read_UserInfo";

        ArrayList<String> info = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("MemberID", memberID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;

            int count=((SoapObject)result.getProperty(0)).getPropertyCount();
            for(int i = 0; i < count; i++)
            {
                String str=((SoapObject)result.getProperty(0)).getProperty(i).toString();
                info.add(str);
            }

            handle.obtainMessage(0x02, 0, 0, info).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomInfo(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CustomerInfo";
        String METHOD_NAME = "Read_CustomerInfo";

        ArrayList<String> info = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("CustomerID", customerID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;

            int count=((SoapObject)result.getProperty(0)).getPropertyCount();
            for(int i = 0; i < count; i++)
            {
                String str=((SoapObject)result.getProperty(0)).getProperty(i).toString();
                info.add(str);
            }

            handle.obtainMessage(0x02, 0, 0, info).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getTitleID(int memberID)
    {
        String SOAP_ACTION = "http://tempuri.org/Get_TitleID";
        String METHOD_NAME = "Get_TitleID";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("MemberID", memberID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x02, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getTitleLogo(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_StoreLogo";
        String METHOD_NAME = "Read_StoreLogo";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x03, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }
}
