package com.example.cy601.stepcounterdemo.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.cy601.stepcounterdemo.R;
import com.example.cy601.stepcounterdemo.Tool.BindService;
import com.example.cy601.stepcounterdemo.Tool.UpdateUiCallBack;
import com.example.cy601.stepcounterdemo.View.CircleProgressView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;


public class MainActivity extends AppCompatActivity {
    private CircleProgressView circle_progress;
    private TextView tv_progress;
    ColumnChartView columnChartView = null;

    private ColumnChartData data;
    private int[] stepData = new int[7];
    static int targerStep = 0;


    private BindService bindService;
    private TextView textView;
    private boolean isBind;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tv_progress.setText(msg.arg1 + "");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, BindService.class);
        isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);


       // init();
       //generateDefaultData();
    }





    private void init() {
        circle_progress = (CircleProgressView) findViewById(R.id.circle_progress);
        tv_progress = findViewById(R.id.tv_progress);
        columnChartView = (ColumnChartView) findViewById(R.id.combine_chart2);

        for (int i = 0; i < 7; i++) {
            stepData[0] = 2377;
            stepData[1] = 8340;
            stepData[2] = 22301;
            stepData[3] = 7834;
            stepData[4] = 8084;
            stepData[5] = 32437;
            stepData[6] = 12321;

        }
        circle_progress.startAnimProgress(80, 1000);

        //监听进度体进度
        circle_progress.setOnAnimProgressListener(new CircleProgressView.OnAnimProgressListener() {
            @Override
            public void valueUpdate(int progress) {
                //tv_progress.setText(String.valueOf(progress));
            }
        });

    }


    private void generateDefaultData() {


        int numSubcolumns = 1;
        int numColumns = 7;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
//                SubcolumnValue subcolumnValue=new SubcolumnValue();
//                subcolumnValue.setColor(ChartUtils.pickColor());
//                subcolumnValue.setTarget((float) Math.random() * 500f + 5);
//                subcolumnValue.setValue(stepData[i]);
//                values.add(subcolumnValue);
                values.add(new SubcolumnValue((float) Math.random() * 50000f,
                        ChartUtils.pickColor()).setTarget(stepData[i]));
            }

            Column column = new Column(values);
            column.setHasLabels(true);   //标签
            //column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        //   底部标题
        List<String> title = new ArrayList<>();
        //颜色值
        List<Integer> color = new ArrayList<>();
        //X、Y轴值list
        List<AxisValue> axisXValues = new ArrayList<>();
        List<SubcolumnValue> mPointValues = new ArrayList<>();
        //所有的柱子
        //   List<Column> columns = new ArrayList<>();
        //单个柱子
        // Column column = new Column();

        title.add("Sun");
        title.add("Mon");
        title.add("Tue");
        title.add("Wed");
        title.add("Thu");
        title.add("Fri");
        title.add("Sat");
        //对每个集合的柱子进行遍历
        for (int i = 0; i < title.size(); i++) {
            //设置X轴的柱子所对应的属性名称(底部文字)
            axisXValues.add(new AxisValue(i).setLabel(title.get(i)));

        }
        //底部
        Axis axisBottom = new Axis(axisXValues);
        //是否显示X轴的网格线
        axisBottom.setHasLines(false);
        //分割线颜色
        axisBottom.setLineColor(Color.parseColor("#ff0000"));
        //字体颜色
        axisBottom.setTextColor(Color.parseColor("#666666"));
        //字体大小
        axisBottom.setTextSize(10);
        //底部文字
        //axisBottom.setName("底部标题");
        //每个柱子的便签是否倾斜着显示
        //axisBottom.setHasTiltedLabels(true);
        //距离各标签之间的距离,包括离Y轴间距 (0-32之间)
        //   axisBottom.setMaxLabelChars(10);
        //设置是否自动生成轴对象,自动适应表格的范围(设置之后底部标题变成0-5)
        //axisBottom.setAutoGenerated(true);
        axisBottom.setHasSeparationLine(true);
        //设置x轴在底部显示
        data = new ColumnChartData(columns);
        Axis axisLeft = new Axis();
        // data.setAxisYLeft(axisLeft);
        data.setAxisXBottom(axisBottom);


        //   ColumnChartData data;

        columnChartView.setColumnChartData(data);
        columnChartView.startDataAnimation(1200);
    }


    //和绷定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    //当前接收到stepCount数据，就是最新的步数
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = stepCount;
                    handler.sendMessage(message);
                    Log.i("MainActivity—updateUi", "当前步数" + stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void onDestroy() {  //app被关闭之前，service先解除绑定
        super.onDestroy();
        if (isBind) {
            this.unbindService(serviceConnection);
        }


    }
}
