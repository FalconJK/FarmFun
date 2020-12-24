package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Waileong910910 on 2015/12/9.
 */
public class WebService_MyStore extends Thread {

    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public String arg1, arg2;
    public int arg3;
    Handler handle;
    int type;

    public WebService_MyStore(Handler handle, int type, int arg3) {
        this.handle = handle;
        this.arg3 = arg3;
        this.type = type;
    }


    public WebService_MyStore(Handler handle, int type, String arg1, String arg2) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                insertMyStore(arg1, arg2);
                break;
            case 0x02:
                insert_Service(arg3);
                break;
        }
    }

    public String insertMyStore(String name, String logo)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_MyStore";
        String METHOD_NAME = "Insert_MyStore";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Title", name);
        request.addProperty("Logo", logo);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x01, 0, 0, Integer.parseInt(result.getProperty(0).toString())).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String insert_Service(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Service";
        String METHOD_NAME = "Insert_Service";

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
                handle.obtainMessage(0x02, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }
}
