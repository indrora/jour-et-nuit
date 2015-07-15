package io.github.indrora.jouretnuit;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import io.github.indrora.jouretnuit.model.StringConstants;

public class MiBandService extends IntentService {

    public MiBandService() {
        super("MiBandService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("MiBandService", "handling intent" + intent.getAction());

        if (intent == null) {
            return;
        }

        if (mDevice == null) {
            mDevice = new MiBandDevice(getApplicationContext());
        }

        if (intent.getAction().equals(StringConstants.REQUEST_DEVICE_CONNECT)) {
            // handle the request to connect.
            String address = intent.getStringExtra("address");
            if (address == null || address.isEmpty()) {
                return; // Don't even service it.
            }
            mDevice.connect(address);
        }else if (intent.getAction().equals(StringConstants.REQUEST_DEVICE_DISCONNECT)) {
            mDevice.disconnect();

        } else if (intent.getAction().equals(StringConstants.REQUEST_STEP_COUNT)) {

            mDevice.mBluetoothGatt.readCharacteristic(mDevice.mStepsCharacteristic);

        } else if (intent.getAction().equals(StringConstants.REQUEST_BATTERY_INFO)) {

            mDevice.mBluetoothGatt.readCharacteristic(mDevice.mBatteryCharacteristic);

        } else if (intent.getAction().equals(StringConstants.REQUEST_STEP_UPDATES)) {
            Log.v("MiBandService", "Writing to the device...");
            boolean enable = intent.getBooleanExtra("enable", false);
            mDevice.setStepUpdates(enable);
        } else if (intent.getAction().equals(StringConstants.REQUEST_FRESH_VALUES)) {
            mDevice.updateValues();

        }
    }

    private static MiBandDevice mDevice;

    public static MiBandDevice getContainer() {
        return mDevice;
    }
}
