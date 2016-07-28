package com.sensoro.sensordemo;

import android.app.Activity;
import android.content.Context;
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

    private String keyArray[] = {"SN", "RSSI", "硬件版本号", "固件版本号", "电量", "温度", "湿度", "光线", "加速度", "自定义"};
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    public void init() {
        SensoroDevice sensoroDevice = this.getIntent().getParcelableExtra("sensoroDevice");
        mRecyclerView = (RecyclerView)findViewById(R.id.detail_list);
        DeviceInfoAdapter deviceInfoAdapter = new DeviceInfoAdapter(this,sensoroDevice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(deviceInfoAdapter);
        deviceInfoAdapter.setData(sensoroDevice);
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
                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getBatteryLevel()));
                    break;
                case 5:
                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getTemperature()));
                    break;
                case 6:
                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getHumidity()));
                    break;
                case 7:
                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getLight()));
                    break;
                case 8:
                    holder.valueTextView.setText(String.valueOf(sensoroDevice.getAccelerometerCount()));
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