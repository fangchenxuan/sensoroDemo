package com.example.sensordemoforeclipse;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
	private String filter_sn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initSDK();
	}
	
    public boolean containsDevice(SensoroDevice sensoroDevice) {
        boolean isContains = false;
        for (int i = 0 ; i < deviceArrayList.size() ; i ++) {
            SensoroDevice tempDevice = deviceArrayList.get(i);
            if (tempDevice.getSerialNumber().equalsIgnoreCase(sensoroDevice.getSerialNumber())) {
                isContains = true;
            }
        }
        return isContains;
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
		                if (!containsDevice(sensoroDevice)) {
		                    deviceArrayList.add(sensoroDevice);
		                    runOnUiThread(new Runnable() {
		                        @Override
		                        public void run() {
		                            deviceListAdapter.notifyDataSetChanged();
		                        }
		                    });
		                }
					}

					@Override
					public void onGoneDevice(SensoroDevice sensoroDevice) {
		
					}

					@Override
					public void onUpdateDevices(
							final ArrayList<SensoroDevice> arrayList) {
			              for (int i = 0 ; i < arrayList.size() ; i ++) {
			                    SensoroDevice tempDevice = arrayList.get(i);
			                    for (int j = 0 ; j < deviceArrayList.size() ; j ++) {
			                        SensoroDevice sensoroDevice = deviceArrayList.get(j);
			                        if (tempDevice.getSerialNumber().equalsIgnoreCase(sensoroDevice.getSerialNumber())) {
			                            deviceArrayList.set(j, tempDevice);
			                            runOnUiThread(new Runnable() {
			                                @Override
			                                public void run() {
			                                    deviceListAdapter.notifyDataSetChanged();
			                                }
			                            });

			                        }
			                    }
			                }
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

		leftTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			       showDialog();
			}
		});
	}

    public void showDialog() {
        final String sensorArray[] = new String[deviceArrayList.size() + 1];
        sensorArray[0] = "全部";
        for (int i = 0 ; i <deviceArrayList.size() ; i ++) {
            sensorArray[i +1] = deviceArrayList.get(i).getSerialNumber();
        }
        if (sensorArray.length > 0) {
            Dialog alertDialog = new AlertDialog.Builder(this).
                    setTitle("Sensor List").
                    setIcon(R.drawable.ic_launcher)
                    .setItems(sensorArray, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, sensorArray[which], Toast.LENGTH_SHORT).show();
                            if (which == 0) {
                            	filter_sn = null;
                                deviceListAdapter.setData(deviceArrayList);
                                deviceListAdapter.notifyDataSetChanged();
                            } else {
                            	filter_sn = sensorArray[which];
                                ArrayList<SensoroDevice> tempList = new ArrayList<SensoroDevice>();
                                for (int i = 0 ; i < deviceArrayList.size() ; i ++) {
                                    if (filter_sn.equalsIgnoreCase(deviceArrayList.get(i).getSerialNumber())) {
                                        tempList.add(deviceArrayList.get(i));
                                        deviceListAdapter.setData(tempList);
                                        deviceListAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }

                        }
                    }).
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).
                            create();
            alertDialog.show();
        }

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

				holder.dripTextView = (TextView) convertView.findViewById(R.id.device_drip);
				holder.coTextView = (TextView) convertView.findViewById(R.id.device_co);
				holder.co2TextView = (TextView) convertView.findViewById(R.id.device_co2);
				holder.no2TextView = (TextView) convertView.findViewById(R.id.device_no2);
				holder.methaneTextView = (TextView) convertView.findViewById(R.id.device_methane);
				holder.lpgTextView = (TextView) convertView.findViewById(R.id.device_lpg);
				holder.pm1TextView = (TextView) convertView.findViewById(R.id.device_pm1);
				holder.pm25TextView = (TextView) convertView.findViewById(R.id.device_pm25);
				holder.pm10TextView = (TextView) convertView.findViewById(R.id.device_pm10);
				holder.coverTextView = (TextView) convertView.findViewById(R.id.device_cover);
				holder.levelTextView = (TextView) convertView.findViewById(R.id.device_level);
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
            String drip = "" +sensoroDevice.getDrip();
            String co = sensoroDevice.getCo() +"" ;
            String co2 = "" + sensoroDevice.getCo2();
            String no2 =  "" + sensoroDevice.getNo2();
            String methane = "" + sensoroDevice.getMethane();
            String lpg =  "" + sensoroDevice.getLpg();
            String pm1 = "" + sensoroDevice.getPm1();
            String pm25 ="" + sensoroDevice.getPm25();
            String pm10 = "" + sensoroDevice.getPm10();
            String cover =  "" + sensoroDevice.getCoverstatus();
            String level = "" + sensoroDevice.getLevel();
            holder.dripTextView.setText("滴漏:" + drip);
            holder.coTextView.setText("一氧化碳:" +co);
            holder.co2TextView.setText("二氧化碳:" +co2);
            holder.no2TextView.setText("二氧化氮:"+ no2);
            holder.methaneTextView.setText("甲烷:" +methane);
            holder.lpgTextView.setText("液化气:" + lpg);
            holder.pm1TextView.setText("PM1:" + pm1);
            holder.pm25TextView.setText("PM25:" + pm25);
            holder.pm10TextView.setText("PM10:" + pm10);
            holder.coverTextView.setText("井盖:" + cover);
            holder.levelTextView.setText("液位:" + level);
			return convertView;
		}
	}

	// ViewHolder静态类
	static class ViewHolder {
        TextView snTextView;
        TextView rssiTextView;
        TextView dripTextView;
        TextView coTextView;
        TextView co2TextView;
        TextView no2TextView;
        TextView methaneTextView;
        TextView lpgTextView;
        TextView pm1TextView;
        TextView pm25TextView;
        TextView pm10TextView;
        TextView coverTextView;
        TextView levelTextView;
	}
}
