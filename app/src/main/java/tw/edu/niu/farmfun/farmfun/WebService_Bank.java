package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/12/8.
 */
public class WebService_Bank extends Thread {

    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public int arg1;
    public String arg2, arg3, arg4, arg5;
    Handler handle;
    int type;

    public WebService_Bank(Handler handle, int type) {
        this.handle = handle;
        this.type = type;
    }

    public WebService_Bank(Handler handle, int type, int arg1) {
        this.handle = handle;
        this.arg1 = arg1;
        this.type = type;
    }

    public WebService_Bank(Handler handle, int type, int arg1, String arg2,  String arg3,  String arg4,  String arg5) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                getBankID(arg1);
                getBankSigner(arg1);
                getBankWhich(arg1);
                getBankCode(arg1);
                getBankAccount(arg1);
                break;
            case 0x02:
                insertBank(arg1, arg2, arg3, arg4, arg5);
                break;
            case 0x03:
                deleteBank(arg1);
                break;
        }
    }

    public String getBankID(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_BankID";
        String METHOD_NAME = "Read_BankID";

        ArrayList<Integer> id = new ArrayList<Integer>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
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

            handle.obtainMessage(0x01, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getBankSigner(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_BankName";
        String METHOD_NAME = "Read_BankName";

        ArrayList<String> name = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
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

            handle.obtainMessage(0x02, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getBankWhich(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_BankWhich";
        String METHOD_NAME = "Read_BankWhich";

        ArrayList<String> which = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
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
                which.add(str);
            }

            handle.obtainMessage(0x03, 0, 0, which).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getBankCode(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_BankCode";
        String METHOD_NAME = "Read_BankCode";

        ArrayList<String> code = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
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
                code.add(str);
            }

            handle.obtainMessage(0x04, 0, 0, code).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getBankAccount(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_BankAccount";
        String METHOD_NAME = "Read_BankAccount";

        ArrayList<String> account = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
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
                account.add(str);
            }

            handle.obtainMessage(0x05, 0, 0, account).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String insertBank(int titleID, String signer, String which, String code, String account)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Bank";
        String METHOD_NAME = "Insert_Bank";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
        request.addProperty("Name", signer);
        request.addProperty("Which", which);
        request.addProperty("Code", code);
        request.addProperty("Account", account);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x06, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String deleteBank(int bankID)
    {
        String SOAP_ACTION = "http://tempuri.org/Delete_Bank";
        String METHOD_NAME = "Delete_Bank";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("BankID", bankID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x07, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }
}
