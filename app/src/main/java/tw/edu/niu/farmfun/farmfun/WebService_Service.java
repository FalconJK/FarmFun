package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2016/3/29.
 */
public class WebService_Service extends Thread
{

    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public int arg1, arg2, arg3, arg4, arg5;
    public String arg6, arg7, arg8, arg9, arg10;
    Handler handle;
    int type;

    public WebService_Service(Handler handle, int type, int arg1) {
        this.handle = handle;
        this.arg1 = arg1;;
        this.type = type;
    }

    public WebService_Service(Handler handle, int type, int arg1, int arg2, int arg3) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                insert_Service(arg1);
                break;
            case 0x02:
                Read_Service(arg1);
                break;
            case 0x03:
                Update_Service(arg1,arg2,arg3);
                break;
            case 0x04:
                Read_Service_Customer(arg1);
                break;
            case 0x05:
                Read_Service_info(arg1);
                break;
        }
    }

    public String Read_Service(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_Service";
        String METHOD_NAME = "Read_Service";

        ArrayList<Integer> service = new ArrayList<Integer>();

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
                service.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x01, 0, 0, service).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String Read_Service_Customer(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_Service";
        String METHOD_NAME = "Read_Service";

        ArrayList<Integer> service = new ArrayList<Integer>();

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
                service.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x18, 0, 0, service).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String Read_Service_info(int orderID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CustomerService";
        String METHOD_NAME = "Read_CustomerService";

        ArrayList<String> service = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("OrderID", orderID);
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
                service.add(str);
            }

            handle.obtainMessage(0x15, 0, 0, service).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String insert_Service(int titleID)
    {
        return null;
    }

    public String Update_Service(int titleID, int service1, int service2)
    {
        String SOAP_ACTION = "http://tempuri.org/Update_Service";
        String METHOD_NAME = "Update_Service";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
        request.addProperty("Service1", service1);
        request.addProperty("Service2", service2);
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
            handle.obtainMessage(0xEE);
        }
        return  null;
    }
}
