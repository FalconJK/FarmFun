package tw.edu.niu.farmfun.farmfun;

import android.os.Handler;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by Waileong910910 on 2015/11/30.
 */
public class WebService_Order extends Thread
{
    private String NAMESPACE = "http://tempuri.org/" ;
    private String URL = "http://120.101.8.4/sugarweb/WebService1.asmx";

    int arg1, arg2;
    String arg3;
    Handler handle;
    int type;

    public WebService_Order(Handler handle, int type) {
        this.handle = handle;
        this.type = type;
    }

    public WebService_Order(Handler handle, int type, int arg1) {
        this.handle = handle;
        this.arg1 = arg1;
        this.type = type;
    }

    public WebService_Order(Handler handle, int type, int arg1, String arg3) {
        this.handle = handle;
        this.arg1 = arg1;
        this.arg3 = arg3;
        this.type = type;
    }

    public void run()
    {
        switch (type)
        {
            case 0x01:
                getOrderID(arg1);
                getCustomID(arg1);
                getCustomName(arg1);
                getCustomCrop(arg1);
                getCustomMany(arg1);
                getCustomPrice(arg1);
                getCustomStatus(arg1);
                getCustomDatetime(arg1);
                break;
            case 0x02:
                getCustomInfo(arg1);
                break;
            case 0x03:
                sendOrder(arg1, arg3);
                break;
            case 0x04:
                getReceipt(arg1);
                break;
            case 0x05:
                break;
            case 0x06:
                getOrderID2(arg1);
                getCustomID2(arg1);
                getCustomName2(arg1);
                getCustomCrop2(arg1);
                getCustomMany2(arg1);
                getCustomPrice2(arg1);
                getCustomStatus2(arg1);
                getCustomDatetime2(arg1);
                break;
            case 0x07:
                uploadReceipt(arg1,arg3);
                break;
            case 0x08:
                sendOrder2(arg1, arg3);
                break;
        }
    }

    public String uploadReceipt(int orderID, String photo)
    {
        String SOAP_ACTION = "http://tempuri.org/Upload_Receipt";
        String METHOD_NAME = "Upload_Receipt";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("OrderID", orderID);
        request.addProperty("Receipt", photo);
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

        } catch (Exception e) {
            e.printStackTrace();
            handle.obtainMessage(0xEE);
        }
        return  null;
    }

    public String getOrderID(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OrderID";
        String METHOD_NAME = "Read_OrderID";

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

    public String getCustomID(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OrderCustomerID";
        String METHOD_NAME = "Read_OrderCustomerID";

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

            handle.obtainMessage(0x02, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomName(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OderCustomer";
        String METHOD_NAME = "Read_OderCustomer";

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

            handle.obtainMessage(0x03, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomCrop(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OderCrop";
        String METHOD_NAME = "Read_OderCrop";

        ArrayList<String> crop = new ArrayList<String>();

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
                crop.add(str);
            }

            handle.obtainMessage(0x04, 0, 0, crop).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomMany(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OrderMany";
        String METHOD_NAME = "Read_OrderMany";

        ArrayList<Integer> many = new ArrayList<Integer>();

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
                many.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x05, 0, 0, many).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomPrice(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OrderPrice";
        String METHOD_NAME = "Read_OrderPrice";

        ArrayList<Integer> price = new ArrayList<Integer>();

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
                price.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x06, 0, 0, price).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomStatus(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OrderStatus";
        String METHOD_NAME = "Read_OrderStatus";

        ArrayList<String> status = new ArrayList<String>();

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
                status.add(str);
            }

            handle.obtainMessage(0x07, 0, 0, status).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomDatetime(int titleID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OrderDateTime";
        String METHOD_NAME = "Read_OrderDateTime";

        ArrayList<String> date = new ArrayList<String>();

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
                date.add(str);
            }

            handle.obtainMessage(0x08, 0, 0, date).sendToTarget();

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

            handle.obtainMessage(0x09, 0, 0, info).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String sendOrder(int orderID, String status)
    {
        String SOAP_ACTION = "http://tempuri.org/Updata_OrderStatus";
        String METHOD_NAME = "Updata_OrderStatus";

        ArrayList<String> info = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("OrderID", orderID);
        request.addProperty("StatusInput", status);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            if (result!= null && !result.getProperty(0).toString().equals(""))
            {
                handle.obtainMessage(0x10, 0, 0, result.getProperty(0).toString()).sendToTarget();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            handle.obtainMessage(0xEE).sendToTarget();
        }

        return null;
    }

    public String sendOrder2(int orderID, String status)
    {
        String SOAP_ACTION = "http://tempuri.org/Updata_OrderStatus";
        String METHOD_NAME = "Updata_OrderStatus";

        ArrayList<String> info = new ArrayList<String>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("OrderID", orderID);
        request.addProperty("StatusInput", status);
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

    public String getReceipt(int orderID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read_OderReceipt";
        String METHOD_NAME = "Read_OderReceipt";

        String receipt = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("OrderID", orderID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            receipt=((SoapObject)result.getProperty(0)).getProperty(0).toString();
            handle.obtainMessage(0x11, 0, 0, receipt).sendToTarget();
            Log.i("result", receipt );

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getOrderID2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OrderID";
        String METHOD_NAME = "Read2_OrderID";

        ArrayList<Integer> id = new ArrayList<Integer>();

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
                id.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x01, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomID2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_TitleID";
        String METHOD_NAME = "Read2_TitleID";

        ArrayList<Integer> id = new ArrayList<Integer>();

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
                id.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x02, 0, 0, id).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomName2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OderTitleName";
        String METHOD_NAME = "Read2_OderTitleName";

        ArrayList<String> name = new ArrayList<String>();

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
                name.add(str);
            }

            handle.obtainMessage(0x03, 0, 0, name).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomCrop2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OderCrop";
        String METHOD_NAME = "Read2_OderCrop";

        ArrayList<String> crop = new ArrayList<String>();

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
                crop.add(str);
            }

            handle.obtainMessage(0x04, 0, 0, crop).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomMany2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OrderMany";
        String METHOD_NAME = "Read2_OrderMany";

        ArrayList<Integer> many = new ArrayList<Integer>();

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
                many.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x05, 0, 0, many).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomPrice2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OrderPrice";
        String METHOD_NAME = "Read2_OrderPrice";

        ArrayList<Integer> price = new ArrayList<Integer>();

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
                price.add(Integer.parseInt(str));
            }

            handle.obtainMessage(0x06, 0, 0, price).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomStatus2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OrderStatus";
        String METHOD_NAME = "Read2_OrderStatus";

        ArrayList<String> status = new ArrayList<String>();

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
                status.add(str);
            }

            handle.obtainMessage(0x07, 0, 0, status).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getCustomDatetime2(int customerID)
    {
        String SOAP_ACTION = "http://tempuri.org/Read2_OrderDateTime";
        String METHOD_NAME = "Read2_OrderDateTime";

        ArrayList<String> date = new ArrayList<String>();

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
                date.add(str);
            }

            handle.obtainMessage(0x08, 0, 0, date).sendToTarget();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
