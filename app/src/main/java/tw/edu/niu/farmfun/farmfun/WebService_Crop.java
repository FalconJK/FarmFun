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
public class WebService_Crop extends Thread
{
    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    public int arg1, arg2, arg3, arg4, arg5;
    public String arg6, arg7, arg8, arg9, arg10, arg11;
    Handler handle;
    int type;

    public WebService_Crop(Handler handle, int type, int arg1) {
        this.handle = handle;
        this.arg1 = arg1;
        this.type = type;
    }

    public WebService_Crop(Handler handle, int type, int arg1, int arg2) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.type = type;
    }

    public WebService_Crop(Handler handle, int type, int arg1, String arg6) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg6 = arg6;
        this.type = type;
    }

    public WebService_Crop(Handler handle, int type, int arg1, int arg2, int arg3, int arg4, int arg5, String arg6, String arg7) {
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

    public WebService_Crop(Handler handle, int type, int arg1, int arg2 , int arg3, int arg4, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.arg9 = arg9;
        this.arg10 = arg10;
        this.arg11 = arg11;
        this.type = type;
    }

    public WebService_Crop(Handler handle, int type, int arg1, int arg2 , int arg3, int arg4, int arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11) {
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
        this.type = type;
    }
    public void run()
    {
        switch (type)
        {
            case 0x01:
                getCropID(arg1);
                getCropName(arg1);
                getCropUnit(arg1);
                getCropPackage(arg1);
                getCropPrice(arg1);
                getCropQuantity(arg1);
                getCropIntroduce(arg1);
                getCropDateTime(arg1);
                getCropLaunchDate(arg1);
                getCropPhoto(arg1);
                break;

            case 0x02:
                insertCrop(arg1,arg2,arg3,arg4,arg6,arg7,arg8,arg9,arg10,arg11);
                break;

            case 0x03:
                updateCrop(arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9,arg10,arg11);
                break;

            case 0x04:
                deleteCrop(arg1);
                break;

            case 0x05:
                getFarmID(arg1);
                getFarmName(arg1);
                break;

            case 0x06:
                getTypeID(arg1);
                getTypeName(arg1);
                break;

            case 0x07:
                insertType(arg1, arg6);
                break;

            case 0x08:
                update_CropQuantity(arg1,arg2);
                break;

            case 0x09:
                insertCustomerOrder(arg1,arg2,arg3,arg4,arg5,arg6,arg7);
                break;

            case 0x10:
                delete_Type(arg1);
                break;
        }
    }

    public String update_CropQuantity(int cropID, int quantity)
    {
        String SOAP_ACTION = "http://tempuri.org/Calculate_Cost";
        String METHOD_NAME = "Calculate_Cost";

        ArrayList<String> datetime = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("CropID", cropID);
        request.addProperty("Quantity", quantity);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x16, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String insertCustomerOrder(int titleID, int customerID, int cropID, int many, int price, String states, String address)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Order";
        String METHOD_NAME = "Insert_Order";

        ArrayList<String> datetime = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("TitleID", titleID);
        request.addProperty("CustomerID", customerID);
        request.addProperty("CropID", cropID);
        request.addProperty("Many", many);
        request.addProperty("Price", price);
        request.addProperty("Status", states);
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
                handle.obtainMessage(0x17, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String getCropID(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropID";
        String METHOD_NAME = "Read_CropID";

        ArrayList<Integer> id = new ArrayList<Integer>();

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

            handle.obtainMessage(0x01, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropName(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropName";
        String METHOD_NAME = "Read_CropName";

        ArrayList<String> name = new ArrayList<String>();

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

            handle.obtainMessage(0x02, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropUnit(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropUnit";
        String METHOD_NAME = "Read_CropUnit";

        ArrayList<String> unit = new ArrayList<String>();

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
                unit.add(str);
            }

            handle.obtainMessage(0x03, 0, 0, unit).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropPackage(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropPackages";
        String METHOD_NAME = "Read_CropPackages";

        ArrayList<String> packages = new ArrayList<String>();

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
                packages.add(str);
            }

            handle.obtainMessage(0x04, 0, 0, packages).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropPrice(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropPrice";
        String METHOD_NAME = "Read_CropPrice";

        ArrayList<Integer> price = new ArrayList<Integer>();

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
                price.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x05, 0, 0, price).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropQuantity(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropQuantity";
        String METHOD_NAME = "Read_CropQuantity";

        ArrayList<Integer> quantity = new ArrayList<Integer>();

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
                quantity.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x06, 0, 0, quantity).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropIntroduce(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropIntroduce";
        String METHOD_NAME = "Read_CropIntroduce";

        ArrayList<String> intr = new ArrayList<String>();

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
                intr.add(str);
            }

            handle.obtainMessage(0x07, 0, 0, intr).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropDateTime(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropDateTime";
        String METHOD_NAME = "Read_CropDateTime";

        ArrayList<String> datetime = new ArrayList<String>();

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
                datetime.add(str);
            }

            handle.obtainMessage(0x08, 0, 0, datetime).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCropLaunchDate(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropLaunchDate";
        String METHOD_NAME = "Read_CropLaunchDate";

        ArrayList<String> launch = new ArrayList<String>();

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
                launch.add(str);
            }
            handle.obtainMessage(0x10, 0, 0, launch).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String getCropPhoto(int farmID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_CropPhoto";
        String METHOD_NAME = "Read_CropPhoto";

        ArrayList<String> photo = new ArrayList<String>();

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
                photo.add(str);
            }

            handle.obtainMessage(0x11, 0, 0, photo).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String insertCrop(int farmID, int typeID, int price, int store, String name, String packages, String  unit, String introduce, String launchdate, String photo)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Crop";
        String METHOD_NAME = "Insert_Crop";

        ArrayList<String> datetime = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("FarmID", farmID);
        request.addProperty("TypeID", typeID);
        request.addProperty("Price", price);
        request.addProperty("Quantity", store);

        request.addProperty("Name", name);
        request.addProperty("Package", packages);
        request.addProperty("Unit", unit);
        request.addProperty("Introduce", introduce);
        request.addProperty("LaunchDate", launchdate);
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
                handle.obtainMessage(0x01, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String updateCrop(int cropID, int farmID, int typeID, int price, int store, String name, String packages, String  unit, String introduce, String launchdate, String photo)
    {
        String SOAP_ACTION = "http://tempuri.org/Updata_Crop";
        String METHOD_NAME = "Updata_Crop";

        ArrayList<String> datetime = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("CropID", cropID);
        request.addProperty("FarmID", farmID);
        request.addProperty("TypeID", typeID);

        request.addProperty("Price", price);
        request.addProperty("Quantity", store);

        request.addProperty("Name", name);
        request.addProperty("Package", packages);
        request.addProperty("Unit", unit);
        request.addProperty("Introduce", introduce);
        request.addProperty("LaunchDate", launchdate);
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
                handle.obtainMessage(0x02, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String deleteCrop(int cropID)
    {
        String SOAP_ACTION = "http://tempuri.org/Delete_Crop";
        String METHOD_NAME = "Delete_Crop";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("CropID", cropID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x12, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
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

            handle.obtainMessage(0x13, 0, 0, id).sendToTarget();

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

            handle.obtainMessage(0x14, 0, 0, name).sendToTarget();

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

            handle.obtainMessage(0x15, 0, 0, id).sendToTarget();

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

            handle.obtainMessage(0x16, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String insertType(int farmID, String name)
    {
        String SOAP_ACTION = "http://tempuri.org/Insert_Type";
        String METHOD_NAME = "Insert_Type";

        ArrayList<String> datetime = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("FarmID", farmID);
        request.addProperty("Name", name);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x17, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String delete_Type(int typeID)
    {
        String SOAP_ACTION = "http://tempuri.org/Delete_Type";
        String METHOD_NAME = "Delete_Type";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("TypeID", typeID);
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
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

}