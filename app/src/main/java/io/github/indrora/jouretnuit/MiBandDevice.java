package io.github.indrora.jouretnuit;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.indrora.jouretnuit.miband.BandConstants;
import io.github.indrora.jouretnuit.miband.Helper;
import io.github.indrora.jouretnuit.miband.data.BatteryInfo;
import io.github.indrora.jouretnuit.miband.data.ICharacteristicBound;
import io.github.indrora.jouretnuit.miband.data.StepCount;
import io.github.indrora.jouretnuit.model.StringConstants;

/**
 * Created by indrora on 7/15/15.
 */
public class MiBandDevice {


    public BatteryInfo device_batteryinfo = new BatteryInfo();
    public StepCount device_steps = new StepCount();

    private Map<UUID, ICharacteristicBound> boundCharacteristics = new HashMap<UUID, ICharacteristicBound>();

    BluetoothManager mBtManager;
    BluetoothGatt mBluetoothGatt;
    BluetoothDevice mBluetoothDevice;
    BluetoothGattService mBandGattService;

    BluetoothGattCharacteristic mControlPointCharacteristic;
    BluetoothGattCharacteristic mStepsCharacteristic;
    BluetoothGattCharacteristic mBatteryCharacteristic;
    BluetoothGattCharacteristic mSensorsCharacteristic;

    private String btAddress;
    private boolean isConnected;


    Context appContext;

    public MiBandDevice(Context appContext) {
        this.appContext = appContext;

        boundCharacteristics.put(BandConstants.UUID_CHARACTERISTIC_REALTIME_STEPS, device_steps);
        boundCharacteristics.put(BandConstants.UUID_CHARACTERISTIC_BATTERY, device_batteryinfo);
    }


    BluetoothGattCallback btGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d("$GattCallback", "onConnectionStateChange -> " + newState);
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                isConnected = true;
                gatt.discoverServices();
            } else {
                isConnected = false;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("$GattCallback", "onServicesDiscovered -> " + status);
            super.onServicesDiscovered(gatt, status);
            mBandGattService = gatt.getService(BandConstants.UUID_SERVICE_MILI_SERVICE);

            mControlPointCharacteristic = mBandGattService.getCharacteristic(BandConstants.UUID_CHARACTERISTIC_CONTROL_POINT);

            mStepsCharacteristic = mBandGattService.getCharacteristic(BandConstants.UUID_CHARACTERISTIC_REALTIME_STEPS);
            mBatteryCharacteristic = mBandGattService.getCharacteristic(BandConstants.UUID_CHARACTERISTIC_BATTERY);
            mSensorsCharacteristic = mBandGattService.getCharacteristic(BandConstants.UUID_CHARACTERISTIC_SENSOR_DATA);

            updateValues();

            // set up the neccesary parts for real time updates
            setCharacteristicNotification(mStepsCharacteristic, true);
            setCharacteristicNotification(mSensorsCharacteristic, true);
            Log.d("$GattCallback", "onServicesDiscovered -> we've got walk sign.");

            Intent i = new Intent();
            i.setAction(StringConstants.RESPONSE_DEVICE_CONNECTED);
            JENApp.makeBroadcast(i);

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            synchronized (characteristic) {

                Log.d("$GattCallback", "onCharacteristicRead -> " + BandConstants.getHelp(characteristic.getUuid()) + " -> " + status);

                byte[] b = characteristic.getValue();
                Log.d("$GattCallback", "New value: [ " + Helper.byteArrayToHex(b) + "]");

                UUID tUuid = characteristic.getUuid();
                if (boundCharacteristics.containsKey(tUuid)) {
                    boundCharacteristics.get(tUuid).characteristicChanged(b);
                }

                characteristic.notify();
            }

            super.onCharacteristicRead(gatt, characteristic, status);

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d("$GattCallback", "Wrote to {" + characteristic.getUuid() + "} with status " + status);
            synchronized (characteristic) {
                characteristic.notify();
            }
            //super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d("$GattCallback", "Characteristic {" + characteristic.getUuid() + "} changed!");
            byte[] b = characteristic.getValue();
            Log.d("$GattCallback", "new value: " + Helper.byteArrayToHex(b));


            if (boundCharacteristics.containsKey(characteristic.getUuid())) {
                boundCharacteristics.get(characteristic.getUuid()).characteristicChanged(b);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d("$GattCallback", "Wrote descriptor: " + descriptor.getUuid() + " new value = " + Helper.byteArrayToHex(descriptor.getValue()));
            //super.onDescriptorWrite(gatt, descriptor, status);
            synchronized (descriptor) {
                descriptor.notify();
            }
        }
    };


    public boolean getConnected() {
        return isConnected && mBluetoothGatt != null;
    }

    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
        BluetoothGattDescriptor btGattDescriptor = characteristic.getDescriptor(BandConstants.UUID_DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION);

        if (!enable) {
            btGattDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        } else {
            if (0 != (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE))
                btGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            else if (0 != (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY))
                btGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        }
        boolean result = mBluetoothGatt.writeDescriptor(btGattDescriptor);
        try {
            btGattDescriptor.wait();
        } finally {
            return result;
        }
    }

    public void connect(String address) {
        btAddress = address;
        Log.d("MiBandService", "Connecting to device " + btAddress);
        mBtManager = (BluetoothManager) appContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothDevice = mBtManager.getAdapter().getRemoteDevice(btAddress);
        mBluetoothGatt = mBluetoothDevice.connectGatt(appContext, true, btGattCallback);
        if( mBluetoothGatt.connect() ) {
            Log.d("MiBandDevice", "Connected mBluetoothGatt!");
        }
        else {
            Log.e("MiBandDevice", "Unable to connect to mBluetoothGatt!!");
        }
    }


    public void updateValues() {

        Thread t = new Thread() {
            @Override
            public void run() {
                if(mBandGattService == null) { return; }
                synchronized (mStepsCharacteristic) {
                    mBluetoothGatt.readCharacteristic(mStepsCharacteristic);
                    try {
                        Log.d("MiBandDevice", "wait on: steps characteristic");
                        mStepsCharacteristic.wait();
                    } catch (Exception ee) {
                        Log.e("MiBandDevice", "Failed in waiting!", ee);
                    }
                }
                synchronized (mBatteryCharacteristic) {

                    mBluetoothGatt.readCharacteristic(mBatteryCharacteristic);
                    try {
                        Log.d("MiBandDevice", "Wait on: battery characteristic");
                        mBatteryCharacteristic.wait();
                    } catch (Exception ee) {
                        Log.e("MiBandDevice", "Failed in waiting!", ee);
                    }
                }


                Log.d("MiBandDevice", "Finished updating values");
                Intent i = new Intent(StringConstants.RESPONSE_SYNC_DATA);
                JENApp.makeBroadcast(i);

            }
        };

        t.start();



    }

    public void disconnect() {
        // disconnect BTLE.
        if(mStepsCharacteristic != null)
            setCharacteristicNotification(mStepsCharacteristic, false);
        if(mSensorsCharacteristic != null)
            setCharacteristicNotification(mSensorsCharacteristic, false);

        setStepUpdates(false);

        isConnected = false;
        mBluetoothGatt.close();

    }


    public boolean setStepUpdates(boolean enable) {
        Log.d("MiBandDevice", "Setting step updates to " + (enable ? "enabled" : "disabled"));
        mControlPointCharacteristic = mBandGattService.getCharacteristic(BandConstants.UUID_CHARACTERISTIC_CONTROL_POINT);
        mControlPointCharacteristic.setValue(new byte[]{18, (byte) (enable ? 1 : 0)});
        if (mBluetoothGatt.writeCharacteristic(mControlPointCharacteristic)) {
            Log.d("MiBandDevice", "We wrote the value!");
            try {
                mControlPointCharacteristic.wait(2000);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        Log.e("MiBandDevice", "Couldn't write value!");
        return false;
    }
}
