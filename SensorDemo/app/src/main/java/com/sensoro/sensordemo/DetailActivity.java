package com.sensoro.sensordemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sensoro.sensor.kit.SensoroDevice;

import java.util.ArrayList;

/**
 * Created by fangping on 2016/7/14.
 */

public class DetailActivity extends Activity {

    private String keyArray[] = {"SN", "RSSI", "硬件版本号", "固件版本号", "电量", "温度", "湿度", "光线", "加速度", "自定义", "滴漏", "CO", "CO2", "NO2", "甲烷", "液化石油气", "PM1", "PM2.5", "PM10", "井盖状态","液位"};
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    public void init() {
        final SensoroDevice sensoroDevice = this.getIntent().getParcelableExtra("sensoroDevice");
        mRecyclerView = (RecyclerView)findViewById(R.id.detail_list);
        DeviceInfoAdapter deviceInfoAdapter = new DeviceInfoAdapter(this,sensoroDevice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(deviceInfoAdapter);
        deviceInfoAdapter.setData(sensoroDevice);
        final TextView textView = (TextView) findViewById(R.id.detail_right_title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, WriteActivity.class);
                intent.putExtra("sensoroDevice", sensoroDevice);
                startActivity(intent);
            }
        });
    }

    class DeviceInfoAdapter extends RecyclerView.Adapter<DeviceInfoItemViewHolder> {

        private Context mContext;
        private SensoroDevice sensoroDevice;

        public DeviceInfoAdapter(Context context, SensoroDevice sensoroDevice) {
            this.mContext = context;
            this.sensoroDevice = sensoroDevice;
        }

        public void setData(SensoroDevice sensoroDevice) {
            this.sensoroDevice = sensoroDevice;
        }

        @Override
        public DeviceInfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_info, null);
            return new DeviceInfoItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DeviceInfoItemViewHolder holder, int position) {
            if (sensoroDevice == null) {
                return;
            }
            holder.keyTextView.setText(keyArray[position]);
            switch (position) {
                case 0:
                    holder.valueTextView.setText(sensoroDevice.getSerialNumber());
                    break;
                case 1:

                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getRssi()));
                    break;
                case 2:
                    holder.valueTextView.setText(sensoroDevice.getHardwareVersion());
                    break;
                case 3:
                    holder.valueTextView.setText(sensoroDevice.getFirmwareVersion());
                    break;
                case 4:
                    String batteryLevel =  ""+sensoroDevice.getBatteryLevel();
                    holder.valueTextView.setText(batteryLevel);
                    break;
                case 5:
                    String temperature =""+sensoroDevice.getTemperature();
                    holder.valueTextView.setText(temperature);
                    break;
                case 6:
                    String humidity =""+sensoroDevice.getHumidity();
                    holder.valueTextView.setText(humidity);
                    break;
                case 7:
                    String light =""+sensoroDevice.getLight();
                    holder.valueTextView.setText(light);
                    break;
                case 8:
                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getCustomize()));
                    break;
                case 9:

                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getAccelerometerCount()));
                    break;
                case 10:
                    String drip =  ""+sensoroDevice.getDrip();
                    holder.valueTextView.setText(drip);
                    break;
                case 11:
                    String co = ""+sensoroDevice.getCo();
                    holder.valueTextView.setText(co);
                    break;
                case 12:
                    String co2 =  ""+sensoroDevice.getCo2();
                    holder.valueTextView.setText(co2);
                    break;
                case 13:
                    String no2 =  ""+sensoroDevice.getNo2();
                    holder.valueTextView.setText(no2);
                    break;
                case 14:
                    String methane =""+sensoroDevice.getMethane();
                    holder.valueTextView.setText(methane);
                    break;
                case 15:
                    String lpg =  ""+sensoroDevice.getLpg();
                    holder.valueTextView.setText(lpg);
                    break;
                case 16:
                    String pm1 = ""+sensoroDevice.getPm1();
                    holder.valueTextView.setText(pm1);
                    break;
                case 17:
                    String pm25 =  ""+sensoroDevice.getPm25();
                    holder.valueTextView.setText(pm25);
                    break;

                case 18:
                    String pm10 =""+sensoroDevice.getPm10();
                    holder.valueTextView.setText(pm10);
                    break;
                case 19:
                    String coverstatus = ""+sensoroDevice.getCoverstatus();
                    holder.valueTextView.setText(coverstatus);
                    break;
                case 20:
                    String level = ""+sensoroDevice.getLevel();
                    holder.valueTextView.setText(level);
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return keyArray.length;
        }
    }

    class DeviceInfoItemViewHolder extends RecyclerView.ViewHolder {

        TextView keyTextView;
        TextView valueTextView;

        public DeviceInfoItemViewHolder(View itemView) {
            super(itemView);
            keyTextView = (TextView) itemView.findViewById(R.id.device_key);
            valueTextView = (TextView) itemView.findViewById(R.id.device_value);
        }
    }
}