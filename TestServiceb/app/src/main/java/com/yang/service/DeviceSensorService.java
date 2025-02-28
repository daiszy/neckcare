package com.yang.service;

/**
 * 加速度传感,陀螺仪，压力传感器，数据收集
 *
 */

import java.text.SimpleDateFormat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class DeviceSensorService extends Service {
    private static final String TAG = "DeviceSensorService";
    Sensor sensorAcc;
    float sensorValue;
    SensorManager sm;
    WakeLock m_wklk;

    @Override
    public void onCreate() {
        super.onCreate();
            if (sm == null) {
                sm = (SensorManager) getApplicationContext().getSystemService(
                        Context.SENSOR_SERVICE);
        }
        Sensor sensorAcc = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        /*
         * 最常用的一个方法 注册事件
         * 参数1 ：SensorEventListener监听器
         * 参数2 ：Sensor 一个服务可能有多个Sensor实现，此处调用getDefaultSensor获取默认的Sensor
         * 参数3 ：模式 可选数据变化的刷新频率，采样率
         * SENSOR_DELAY_FASTEST,100次左右
         * SENSOR_DELAY_GAME,50次左右
         * SENSOR_DELAY_UI,20次左右
         * SENSOR_DELAY_NORMAL,5次左右
         */
        sm.registerListener(mySensorListener, sensorAcc,
                SensorManager.SENSOR_DELAY_NORMAL); //以普通采样率注册监听器

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, DeviceSensorService.class.getName());
        m_wklk.acquire();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "zhangjieqiong onStartCommand");
        return Service.START_STICKY;
    }

    public void onDestroy() {
        if (sm != null) {
            sm.unregisterListener(mySensorListener);
            mySensorListener = null;
        }
        if (m_wklk != null) {
            m_wklk.release();
            m_wklk = null;
        }
    };

    /*
     * SensorEventListener 接口的实现，需要实现两个方法
     * 方法1 onSensorChanged 当数据变化的时候被触发调用
     * 方法2 onAccuracyChanged 当获得数据的精度发生变化的时候被调用，比如突然无法获得数据时
     */
    private SensorEventListener mySensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (this) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS ");
                String date = sDateFormat.format(new java.util.Date());
                        float X_lateral = sensorEvent.values[0];
                        sensorValue=X_lateral;
                        float Y_longitudinal = sensorEvent.values[1];
                        float Z_vertical = sensorEvent.values[2];
                        Log.i("sensor", "TYPE_ORIENTATION:"+ date + ", "
                                + X_lateral + "," + Y_longitudinal + "," + Z_vertical
                                + ";");

                }

            }


        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i("sensor", "onAccuracyChanged-----sensor"+ sensor + ",acc:" + accuracy);

        }
    };

}