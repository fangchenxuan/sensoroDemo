#Android Sensor SDK 

##Step By Step

###1.新建工程
Android Studio配置

将sensoro-sensor-kit.jar包放入道libs文件夹下,然后在当前工程下的build.gradle文件配置项中的dependencies新增内容,，如下compile files('libs/sensoro-sensor-kit.jar')

Eclipse配置

将sensoro-sensor-kit.jar包放入道libs文件夹下,右击工程propeties,选择Java build Path，在Library选项中添加sensoro-sensor-kit依赖关系

###2.Android Manifest文件说明
在permission节点下新增以下权限和功能，以下权限和功能是必选项 

	<uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
添加service,如下

    <service android:name="com.sensoro.sensor.kit.SensoroDeviceService"></service>
    <service android:name="com.sensoro.sensor.kit.IntentProcessorService"></service>
###3.代码调用示例说明

1.sdk 核心功能调用示例

       SensoroDeviceManager sensoroDeviceManager = SensoroDeviceManager.getInstance(this);
        try {
            sensoroDeviceManager.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sensoroDeviceManager.setSensoroDeviceListener(new SensoroDeviceListener<SensoroDevice>() {
            @Override
            public void onNewDevice(SensoroDevice sensoroDevice) {//当发现新设备的时候该函数会被回调

            }

            @Override
            public void onGoneDevice(SensoroDevice sensoroDevice) {//设备消失的情况下，该函数被回调

            }

            @Override
            public void onUpdateDevices(final ArrayList<SensoroDevice> arrayList) {//定期回调数据更新函数
            }

        });

说明：SensoroDeviceManager 是传感器设备管理类，负责处理发现设备和设备消失以及设备信息更新功能，该类是单例类，可通过getInstance方法获得该对象
SensoroDeviceListener 用于回调通知发现设备，设备消息，和设备更新


###6. 传感器设备对象说明
说明：SensoroDevice 为传感器设备对象，以下为对象属性

     serialNumber ---String; // SN
     macAddress---String; // MAC
     hardwareVersion----String;//硬件版本号
     firmwareVersion-----String;//固件版本号
     batteryLevel-----int;// 剩余电量
     temperature---float;// 温度
     light----float; // 光线照度
     humidity---int;//湿度
     accelerometerCount---int; // 加速度计数器
     rssi---int;
     customize----byte[];//自定义数据
		



## 例子代码

[Github 代码](https://github.com/fangchenxuan/sensoroDemo)

包括 eclipse 和Android studio 两个版本。

##修订历史
日期 | 版本 | 修订人 | 内容
---|---|---|---
2016-07-27|1.0|Will | 初始内容





















