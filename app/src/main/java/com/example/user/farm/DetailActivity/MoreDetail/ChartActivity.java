package com.example.user.farm.DetailActivity.MoreDetail;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.user.farm.Funtional.JsonRequest;
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

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {
    private Context context = this;
    private RequestQueue queue;
    private String MachineID;
    private LineChart chart;
    private String[] value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setupComponent();
    }

    private void setupComponent() {
        queue = Volley.newRequestQueue(context);
        MachineID = getIntent().getStringExtra("MachineID");
        chart = (LineChart) findViewById(R.id.chart);
        chart.setOnChartGestureListener(this);
        chart.setOnChartValueSelectedListener(this);
        chart.setDragEnabled(true);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(true);
        chart.getDescription().setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = "http://120.101.8.52/2017farmer/WebService1.asmx/Read_Machine_All_Sensor?MachineID=" + MachineID;
        JsonRequest request = new JsonRequest(url, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                JsonArray sensor = response.get("Sensor").getAsJsonArray();
                if (!TextUtils.equals(sensor.toString(), "[]"))
                    setData(sensor);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void setData(JsonArray sensor) {
        final ArrayList<String> xx = new ArrayList<>();
        ArrayList<Entry> soil = new ArrayList<>();
        ArrayList<Entry> water = new ArrayList<>();
        ArrayList<Entry> tem = new ArrayList<>();
        ArrayList<Entry> hum = new ArrayList<>();


        for (int i = 0; i < sensor.size(); i++) {
            Item item = new Item(sensor.get(sensor.size() - i - 1).getAsJsonObject());
            xx.add(item.getTime());
            soil.add(new Entry(i, item.getSoil()));
            water.add(new Entry(i, item.getWater()));
            tem.add(new Entry(i, item.getTem()));
            hum.add(new Entry(i, item.getHum()));

            Log.d("chart", soil.toString());
            Log.d("chart", water.toString());
        }

        SetXY(xx);

        LineDataSet setsoil = new LineDataSet(soil, "土壤濕度");
        LineDataSet setwater = new LineDataSet(water, "水位");
        LineDataSet setTem = new LineDataSet(tem, "溫度");
        LineDataSet setHum = new LineDataSet(hum, "空氣濕度");

        setsoil.setFillAlpha(110);
        setsoil.setColor(Color.RED);
        setsoil.setValueTextSize(13f);

        setwater.setFillAlpha(110);
        setwater.setColor(Color.BLUE);
        setwater.setValueTextSize(13f);

        setTem.setFillAlpha(110);
        setTem.setColor(Color.GREEN);
        setTem.setValueTextSize(13f);


        setHum.setFillAlpha(110);
        setHum.setColor(Color.CYAN);
        setHum.setValueTextSize(13f);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setsoil);
        dataSets.add(setwater);
        dataSets.add(setTem);
        dataSets.add(setHum);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(60f));
        chart.invalidate();
    }

    private void SetXY(final ArrayList<String> xx) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(9);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xx.get((int) value);
            }
        });
    }


    private class Item {
        Integer soil;
        Integer water;
        String Tem;
        String Hum;
        String time;

        JsonObject data;

        public Integer getSoil() {
            return soil;
        }

        public Integer getWater() {
            return water;
        }

        public Float getTem() {
            return Float.parseFloat(Tem.replace(" C", ""));
        }

        public Float getHum() {
            return Float.parseFloat(Hum.replace(" %", ""));
        }

        public String getTime() {
            return time.substring(10, 15);
        }

        public Item(JsonObject data) {
            this.data = data;
            this.soil = data.get("Seneor_Soil").getAsInt();
            this.water = data.get("Sensor_Water").getAsInt();
            this.Tem = data.get("Sensor_Tem").getAsString();
            this.Hum = data.get("Sensor_Hum").getAsString();
            this.time = data.get("Sensor_DateTime").getAsString();
        }

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
}
