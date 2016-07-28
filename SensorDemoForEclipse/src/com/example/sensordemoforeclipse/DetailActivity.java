package com.example.sensordemoforeclipse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sensordemoforeclipse.MainActivity.ViewHolder;
import com.sensoro.sensor.kit.SensoroDevice;

import java.util.ArrayList;

/**
 * Created by fangping on 2016/7/14.
 */

public class DetailActivity extends Activity {

    private String keyArray[] = {"SN", "RSSI", "硬件版本号", "固件版本号", "电量", "温度", "湿度", "光线", "加速度", "自定义"};
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
          init();
    }

    public void init() {
        SensoroDevice sensoroDevice = this.getIntent().getParcelableExtra("sensoroDevice");
        mListView = (ListView)findViewById(R.id.detail_list);
        DeviceInfoAdapter deviceInfoAdapter = new DeviceInfoAdapter(this,sensoroDevice);
        mListView.setAdapter(deviceInfoAdapter);
        deviceInfoAdapter.setData(sensoroDevice);
    }

    class DeviceInfoAdapter extends BaseAdapter{

        private Context mContext;
        private SensoroDevice sensoroDevice;
        private LayoutInflater mInflater;
        public DeviceInfoAdapter(Context context, SensoroDevice sensoroDevice) {
            this.mContext = context;
            this.sensoroDevice = sensoroDevice;
            this.mInflater = LayoutInflater.from(context);
        }

        public void setData(SensoroDevice sensoroDevice) {
            this.sensoroDevice = sensoroDevice;
        }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return keyArray.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return keyArray[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			DeviceInfoItemViewHolder holder = null;
			if (convertView == null) {
				holder = new DeviceInfoItemViewHolder();
				convertView = mInflater.inflate(R.layout.item_info, null);
				holder.keyTextView = (TextView)convertView.findViewById(R.id.device_key);
				holder.valueTextView = (TextView) convertView.findViewById(R.id.device_value);
				convertView.setTag(holder);
			}else {
				holder = (DeviceInfoItemViewHolder) convertView.getTag();
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
			return convertView;
		}

         
    }

    static class DeviceInfoItemViewHolder {

        TextView keyTextView;
        TextView valueTextView;

    }
}