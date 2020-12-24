package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/10/25.
 */
public class WebService_Farm extends Thread
{
    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public int arg1;
    public String arg2, arg3, arg4, arg5, arg6, arg7;
    Handler handle;
    int type;

    public WebService_Farm(Handler handle, int type, int arg1) {
        this.handle = handle;
        this.arg1 = arg1;
        this.type = type;
    }

    public WebService_Farm(Handler handle, int type, int arg1, String arg2, String arg3, String arg4, String arg5, String arg6) {
            this.handle = handle;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
            this.arg4 = arg4;
            this.arg5 = arg5;
            this.arg6 = arg6;
            this.type = type;
    }

    public WebService_Farm(Handler handle, int type, int arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                getFarmID(arg1);
                getFarmName(arg1);
                break;
            case 0x02:
                getFarmData(arg1);
                break;
            case 0x03:
                updateFarm(arg1, arg2, arg3, arg4, arg5, arg6);
                break;
            case 0x04:
                insertFarm(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
                break;
            case 0x05:
                deleteFarm(arg1);
                break;
        }
    }

    public String getFarmID(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_FarmID";
        String METHOD_NAME = "Read_FarmID";

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
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getFarmName(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_FarmName2";
        String METHOD_NAME = "Read_FarmName2";

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

            handle.obtainMessage(0x06, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getFarmData(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_Farm";
        String METHOD_NAME = "Read_Farm";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("farmID", farmID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            ArrayList<String> farmData = new ArrayList<String>();
            int count=((SoapObject)result.getProperty(0)).getPropertyCount();
            for(int i = 0; i < count; i++)
            {
                String str=((SoapObject)result.getProperty(0)).getProperty(i).toString();
                farmData.add(str);
            }

            handle.obtainMessage(0x02, 0, 0, farmData).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return null;
    }

    public String updateFarm(int farmID, String name, String area, String crop, String quantity, String introduce)
    {
        String SOAP_ACTION = "http://tempuri.org/Updata_Farm";
        String METHOD_NAME = "Updata_Farm";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("farmID", farmID);
        request.addProperty("Name", name);
        request.addProperty("Area", area);
        request.addProperty("Crop", String.valueOf(crop));
        request.addProperty("Quantity", quantity);
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

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String insertFarm(int titleID, String name, String area, String crop, String quantity, String introduce, String photo)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Farm";
        String METHOD_NAME = "Insert_Farm";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TitleID", titleID);
        request.addProperty("Name", name);
        request.addProperty("Area", area);
        request.addProperty("Crop", crop);
        request.addProperty("Quantity", quantity);
        request.addProperty("Introduce", introduce);
        request.addProperty("Photo", photo);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x04, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String deleteFarm(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Delete_Farm";
        String METHOD_NAME = "Delete_Farm";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("farmID", farmID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x05, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return null;
    }
}
