package io.github.indrora.jouretnuit.miband.data;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

/**
 * Created by indrora on 7/12/15.
 */
public abstract class NotifyHandler {
    private BluetoothGatt gatt;
    public NotifyHandler(BluetoothGatt g) {
        gatt = g;
    }
    public void handleChange(BluetoothGattCharacteristic characteristic) {
        Log.d("NotifyHandler", "Called handleChange() for "+characteristic.getUuid().toString());
    }
}
