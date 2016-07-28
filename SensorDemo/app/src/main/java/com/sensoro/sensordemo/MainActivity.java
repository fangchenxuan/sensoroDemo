package com.sensoro.sensordemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sensoro.sensor.kit.SensoroDevice;
import com.sensoro.sensor.kit.SensoroDeviceListener;
import com.sensoro.sensor.kit.SensoroDeviceManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensoroDeviceManager sensoroDeviceManager;
    private ArrayList<SensoroDevice> deviceArrayList = new ArrayList<>();
    private DeviceListAdapter deviceListAdapter;
    private RecyclerView mRecycleView;
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
        sensoroDeviceManager.setSensoroDeviceListener(new SensoroDeviceListener<SensoroDevice>() {
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
            public void onUpdateDevices(final ArrayList<SensoroDevice> arrayList) {
                Log.d("MainActivity", "===========>onUpdateDevices");
                Log.d("MainActivity", "===========>deviceArrayList.size->" + deviceArrayList.size());
                Log.d("MainActivity", "=============>arrayList.size=>" + arrayList.size());
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

    private void updateDevice(ArrayList<SensoroDevice> list) {

        for (int i = 0; i < list.size(); i++) {
            SensoroDevice new_device = list.get(i);
            SensoroDevice org_device = deviceArrayList.get(i);
        }
    }


    private void initData() {
        mRecycleView = (RecyclerView) findViewById(R.id.main_list);
        deviceListAdapter = new DeviceListAdapter(this, new RecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("sensoroDevice", deviceArrayList.get(position));
                MainActivity.this.startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(deviceListAdapter);
        deviceListAdapter.setData(deviceArrayList);
        leftTitle = (TextView) findViewById(R.id.main_left_title);
        leftTitle.setText(R.string.start);
        leftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftTitle.getText().toString().equals("Start")) {
                    SensoroDeviceManager.getInstance(MainActivity.this).stopService();
                    leftTitle.setText(R.string.start);
                } else {
                    try {
                        SensoroDeviceManager.getInstance(MainActivity.this).startService();
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

    class DeviceListAdapter extends RecyclerView.Adapter<DeviceListItemViewHolder> {

        private Context mContext;
        private ArrayList<SensoroDevice> mList;
        private RecycleViewItemClickListener listener;

        public DeviceListAdapter(Context context, RecycleViewItemClickListener listener) {
            this.mContext = context;
            this.listener = listener;
        }

        public void setData(ArrayList list) {
            this.mList = list;
        }

        @Override
        public DeviceListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_device, null);
            return new DeviceListItemViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(DeviceListItemViewHolder holder, int position) {
            if (mList == null) {
                return;
            }
            SensoroDevice sensoroDevice = mList.get(position);
            if (sensoroDevice.getSerialNumber() == null || sensoroDevice.getSerialNumber().equals("")) {
                holder.snTextView.setText(mList.get(position).getMacAddress());
            } else {
                holder.snTextView.setText(mList.get(position).getSerialNumber());
            }

            holder.rssiTextView.setText(String.valueOf(mList.get(position).getRssi()));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class DeviceListItemViewHolder extends RecyclerView.ViewHolder {

        TextView snTextView;
        TextView rssiTextView;
        RecycleViewItemClickListener itemClickListener;

        DeviceListItemViewHolder(View itemView, RecycleViewItemClickListener listener) {
            super(itemView);
            snTextView = (TextView) itemView.findViewById(R.id.device_sn);
            rssiTextView = (TextView) itemView.findViewById(R.id.device_rssi);
            this.itemClickListener = listener;
            itemView.setOnClickListener(onItemClickListener);
        }

        View.OnClickListener onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        };
    }

    interface RecycleViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
