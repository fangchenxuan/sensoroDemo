package com.example.sensordemoforeclipse;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sensoro.sensor.kit.SensoroDevice;
import com.sensoro.sensor.kit.SensoroDeviceListener;
import com.sensoro.sensor.kit.SensoroDeviceManager;

public class MainActivity extends Activity {

	private SensoroDeviceManager sensoroDeviceManager;
	private ArrayList<SensoroDevice> deviceArrayList = new ArrayList<SensoroDevice>();
	private DeviceListAdapter deviceListAdapter;
	private ListView mListView;
	private MyHandler myHandler = new MyHandler();
	private TextView leftTitle;
	private TextView rightTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initSDK();
	}
	private void initSDK() {
		sensoroDeviceManager = SensoroDeviceManager.getInstance(this);
		try {
			sensoroDeviceManager.startService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sensoroDeviceManager
				.setSensoroDeviceListener(new SensoroDeviceListener<SensoroDevice>() {
					@Override
					public void onNewDevice(SensoroDevice sensoroDevice) {
						deviceArrayList.add(sensoroDevice);
						myHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								deviceListAdapter.notifyDataSetChanged();
							}
						}, 100);
					}

					@Override
					public void onGoneDevice(SensoroDevice sensoroDevice) {
						deviceArrayList.remove(sensoroDevice);
						myHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								deviceListAdapter.notifyDataSetChanged();
							}
						}, 100);
					}

					@Override
					public void onUpdateDevices(
							final ArrayList<SensoroDevice> arrayList) {
						myHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								deviceArrayList.clear();
								deviceArrayList.addAll(arrayList);
								deviceListAdapter.notifyDataSetChanged();
							}
						}, 100);
					}

				});
	}


	private void initData() {
		deviceListAdapter = new DeviceListAdapter(this);
        mListView = (ListView)findViewById(R.id.main_list);
		mListView.setAdapter(deviceListAdapter);
		deviceListAdapter.setData(deviceArrayList);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("sensoroDevice", deviceArrayList.get(arg2));
                MainActivity.this.startActivity(intent);
			}
			
		});
		leftTitle = (TextView) findViewById(R.id.main_left_title);
		leftTitle.setText(R.string.start);
		leftTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (leftTitle.getText().toString().equals("Start")) {
					SensoroDeviceManager.getInstance(MainActivity.this)
							.stopService();
					leftTitle.setText(R.string.start);
				} else {
					try {
						SensoroDeviceManager.getInstance(MainActivity.this)
								.startService();
						leftTitle.setText(R.string.stop);
					} catch (Exception e) {
						e.printStackTrace();
						leftTitle.setText(R.string.start);
					}
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		sensoroDeviceManager.stopService();
	}

	static class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	}

	class DeviceListAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<SensoroDevice> mList = new ArrayList<SensoroDevice>();
		private LayoutInflater mInflater;

		public DeviceListAdapter(Context context) {
			this.mContext = context;
			mInflater = LayoutInflater.from(context);
		}

		public void setData(ArrayList<SensoroDevice> data) {
			mList = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_device, null);
				holder.snTextView = (TextView) convertView.findViewById(R.id.device_sn);
				holder.rssiTextView = (TextView) convertView.findViewById(R.id.device_rssi);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder.snTextView == null || holder.rssiTextView == null) {
				return null;
			}
            SensoroDevice sensoroDevice = mList.get(position);
            if (sensoroDevice.getSerialNumber() == null || sensoroDevice.getSerialNumber().equals("")) {
                holder.snTextView.setText(mList.get(position).getMacAddress());
            } else {
                holder.snTextView.setText(mList.get(position).getSerialNumber());
            }

            holder.rssiTextView.setText(String.valueOf(mList.get(position).getRssi()));
			return convertView;
		}
	}

	// ViewHolderæ≤Ã¨¿‡
	static class ViewHolder {
        TextView snTextView;
        TextView rssiTextView;
	}
}
