package com.example.user.farm.BlueTooth;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.farm.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import me.aflak.bluetooth.Bluetooth;

public class DataActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback, OnChartGestureListener, OnChartValueSelectedListener {
    private String name;
    private Bluetooth b;
    private EditText message;
    private Button send;
    private TextView text;
    private String data = "";
    private ProgressBar progressBar;
    private LineChart chart;
    private View view;

    private boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setupComponent();
        setupLinstener();
    }

    private void setupComponent() {
        text = (TextView) findViewById(R.id.text);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        chart = (LineChart) findViewById(R.id.chart2);


        chart.setOnChartGestureListener(this);
        chart.setOnChartValueSelectedListener(this);
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(true);
        chart.getDescription().setEnabled(false);


        text.setMovementMethod(new ScrollingMovementMethod());
        send.setEnabled(false);

        b = new Bluetooth(this);
        b.enableBluetooth();
        b.setCommunicationCallback(this);

        int pos = getIntent().getExtras().getInt("pos");

        name = b.getPairedDevices().get(pos).getName();
        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered = true;
    }

    private void setupLinstener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                message.setText("");
                b.send(msg);
                Display("You: " + msg);
            }
        });

    }


    private void setData(JsonArray sensor) {
        final ArrayList<String> xx = new ArrayList<>();
        ArrayList<Entry> tem = new ArrayList<>();
        ArrayList<Entry> hum = new ArrayList<>();
        Item item;


        for (int i = 0; i < sensor.size(); i++) {
            item = new Item(sensor.get(i).getAsJsonObject());
            xx.add(item.getTime());
            tem.add(new Entry(i, item.getTem()));
            hum.add(new Entry(i, item.getHum()));

        }

        SetXY(xx);

        LineDataSet settem = new LineDataSet(tem, "溫度");
        LineDataSet sethum = new LineDataSet(hum, "濕度");


        settem.setFillAlpha(110);
        settem.setColor(Color.RED);
        settem.setValueTextSize(13f);

        sethum.setFillAlpha(110);
        sethum.setColor(Color.BLUE);
        sethum.setValueTextSize(13f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(settem);
        dataSets.add(sethum);

        LineData data = new LineData(dataSets);

        chart.setData(data);
        chart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(60f));
        progressBar.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);
        chart.invalidate();
    }

    private void SetXY(final ArrayList<String> xx) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(xx.size() > 9 ? 9 : xx.size());
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xx.get((int) value);
            }
        });
    }

    //region
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //endregion
    private class Item {
        Float tem;
        Float hum;
        Float lat;
        Float lng;
        String date;
        String time;
        JsonObject data;

        public Float getTem() {
            return tem;
        }

        public Float getHum() {
            return hum;
        }

        public Float getLat() {
            return lat;
        }

        public Float getLng() {
            return lng;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public Item(JsonObject data) {
            this.data = data;
            this.tem = data.get("Tem:").getAsFloat();
            this.hum = data.get("Hum:").getAsFloat();
            this.lat = data.get("lat").getAsFloat();
            this.lng = data.get("lng").getAsFloat();
            this.date = data.get("date").getAsString();
            this.time = data.get("time").getAsString();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registered) {
            unregisterReceiver(mReceiver);
            registered = false;
        }
    }

    public void Display(final String s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                text.append(s + "\n");
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Display("Disconnected!");
        Display("Connecting again...");
        b.connectToDevice(device);
    }

    @Override
    public void onMessage(final String message) {
        data += message;
//        Display(message);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message.equals("]")) {
                    try {
//                        Toast.makeText(DataActivity.this, data, Toast.LENGTH_SHORT).show();
                        JsonArray array = new JsonParser().parse(data).getAsJsonArray();
                        setData(array);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
    

    @Override
    public void onError(String message) {
        Display("Error: " + message);
    }

    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
//        Display("Error: " + message);
//        Display("Trying again in 3 sec.");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        Display("Connected to " + device.getName());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                send.setEnabled(true);
            }
        });
    }

    //region
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(DataActivity.this, SelectActivity.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if (registered) {
                            unregisterReceiver(mReceiver);
                            registered = false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if (registered) {
                            unregisterReceiver(mReceiver);
                            registered = false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };
    //endregion

}
