package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/10/29.
 */
public class WebService_Resume extends Thread {

    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public int arg1, arg2, arg3, arg4, arg5;
    public String arg6, arg7, arg8, arg9, arg10;
    Handler handle;
    int type;

    public WebService_Resume(Handler handle, int type, int arg1) {
        this.handle = handle;
        this.arg1 = arg1;
        this.type = type;
    }

    public WebService_Resume(Handler handle, int type, int arg1, String arg6) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg6 = arg6;
        this.type = type;
    }

    public WebService_Resume(Handler handle, int type, int arg1, String arg6, String arg7, String arg8) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.type = type;
    }

    public WebService_Resume(Handler handle, int type, int arg1, int arg2, String arg6, String arg7, String arg8) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                getResumeID(arg1,arg6);
                getResumeName(arg1, arg6);
                getResumeDate(arg1,arg6);
                getResumeIntroduce(arg1,arg6);
                break;

            case 0x02:
                insertResume(arg1, arg6, arg7, arg8);
                break;

            case 0x03:
                deleteResume(arg1);
                break;

            case 0x04:
                getFarmID(arg1);
                getFarmName(arg1);
                break;

            case 0x05:
                getTypeID(arg1);
                getTypeName(arg1);
                break;

            case 0x06:
                getCropID(arg1);
                getCropName(arg1);
                break;

            case 0x07:
                getLaunchDate(arg1);
                break;

            case 0x08:
                getResumeID2(arg1);
                getResumeName2(arg1);
                getResumeDate2(arg1);
                getResumeIntroduce2(arg1);
                break;

            case 0x09:
                insertResume2(arg1, arg6, arg7, arg8);
                break;
        }
    }

    public String getFarmID(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_FarmID";
        String METHOD_NAME = "Read_FarmID";

        ArrayList<Integer> id = new ArrayList<Integer>();
        id.add(-1);

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
        name.add("請選擇農場");

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
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getTypeID(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_TypeID";
        String METHOD_NAME = "Read_TypeID";

        ArrayList<Integer> id = new ArrayList<Integer>();
        id.add(-1);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("FarmID", farmID);
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

            handle.obtainMessage(0x03, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getTypeName(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_TypeName";
        String METHOD_NAME = "Read_TypeName";

        ArrayList<String> name = new ArrayList<String>();
        name.add("請選擇農作物");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("FarmID", farmID);
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

            handle.obtainMessage(0x04, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getCropID(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropIDByType";
        String METHOD_NAME = "Read_CropIDByType";

        ArrayList<Integer> id = new ArrayList<Integer>();
        id.add(-1);
        id.add(-2);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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

            handle.obtainMessage(0x05, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getCropName(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropNameByType";
        String METHOD_NAME = "Read_CropNameByType";

        ArrayList<String> name = new ArrayList<String>();
        name.add("請選擇商品");
        name.add("全部");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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

    public String getLaunchDate(int cropID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_LaunchDate";
        String METHOD_NAME = "Read_LaunchDate";

        ArrayList<String> launch = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("CropID", cropID);
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
                launch.add(str);
            }
            handle.obtainMessage(0x12, 0, 0, launch.get(0).toString()).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeID(int typeID, String launch)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeID";
        String METHOD_NAME = "Read_ResumeID";

        ArrayList<Integer> id = new ArrayList<Integer>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
        request.addProperty("LaunchDate", launch);
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
            handle.obtainMessage(0x07, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeName(int typeID, String launch)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeName";
        String METHOD_NAME = "Read_ResumeName";

        ArrayList<String> name = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
        request.addProperty("LaunchDate", launch);
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

            handle.obtainMessage(0x08, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeDate(int typeID, String launch)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeDate";
        String METHOD_NAME = "Read_ResumeDate";

        ArrayList<String> date = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
        request.addProperty("LaunchDate", launch);
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
                date.add(str);
            }

            handle.obtainMessage(0x09, 0, 0, date).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeIntroduce(int typeID, String launch)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeIntroduce";
        String METHOD_NAME = "Read_ResumeIntroduce";

        ArrayList<String> intr = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
        request.addProperty("LaunchDate", launch);
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
                intr.add(str);
            }

            handle.obtainMessage(0x10, 0, 0, intr).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String insertResume(int typeID, String date, String title, String intr)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Resume";
        String METHOD_NAME = "Insert_Resume";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
        request.addProperty("Date", date);
        request.addProperty("Name", title);
        request.addProperty("Introduce", intr);

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
        return  null;
    }

    public String insertResume2(int typeID, String date, String title, String intr)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Resume";
        String METHOD_NAME = "Insert_Resume";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
        request.addProperty("Date", date);
        request.addProperty("Name", title);
        request.addProperty("Introduce", intr);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x18, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String deleteResume(int resumeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Delete_Resume";
        String METHOD_NAME = "Delete_Resume";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("resumeID", resumeID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x11, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String getResumeID2(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeID2";
        String METHOD_NAME = "Read_ResumeID2";

        ArrayList<Integer> id = new ArrayList<Integer>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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
            handle.obtainMessage(0x07, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeName2(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeName2";
        String METHOD_NAME = "Read_ResumeName2";

        ArrayList<String> name = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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

            handle.obtainMessage(0x08, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeDate2(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeDate2";
        String METHOD_NAME = "Read_ResumeDate2";

        ArrayList<String> date = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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
                date.add(str);
            }

            handle.obtainMessage(0x09, 0, 0, date).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }
        return null;
    }

    public String getResumeIntroduce2(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_ResumeIntroduce2";
        String METHOD_NAME = "Read_ResumeIntroduce2";

        ArrayList<String> intr = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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
                intr.add(str);
            }

            handle.obtainMessage(0x10, 0, 0, intr).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }
}
