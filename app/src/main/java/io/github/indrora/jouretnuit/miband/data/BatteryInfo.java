package io.github.indrora.jouretnuit.miband.data;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.GregorianCalendar;

import io.github.indrora.jouretnuit.JENApp;
import io.github.indrora.jouretnuit.miband.Helper;
import io.github.indrora.jouretnuit.model.StringConstants;

/**
 * Created by indrora on 7/9/15.
 */
public class BatteryInfo implements IBinaryData, ICharacteristicBound {

    public static final String KEY_CHARGE_LEVEL = "chargeLevel";
    public static final String KEY_CHARGE_COUNT = "chargeCount";
    public static final String KEY_LAST_CHARGED = "lastCharged";
    public static final String KEY_CHARGE_STATUS = "chargeStatus";

    public static final int STATUS_CHARGED = 0;
    public static final int STATUS_CHARGING = 1;
    public static final int SOMETHING_ELSE = 2;

    public GregorianCalendar lastCharged;
    public int chargeLevel;
    public int chargeCount;
    public int status;


    @Override
    public byte[] toBytes() {
        // since we have no data to produce (it's a one-way operation)
        // we'll return null.
        return null;
    }

    @Override
    public boolean fromBytes(byte[] in) {
        // If we have no data, we have no data
        if (in == null) {
            Log.e("BatteryInfo", "Input was null!");
            return false;
        }
        // if we have not-10 bytes, we don't have the right packet.
        if (in.length != 10) {
            Log.e("BatteryInfo", "Was expecting 10 bytes, got "+in.length);
            return false;
        }
        // Charge level (0-100)
        this.chargeLevel = in[0];

        this.lastCharged = Helper.calendarFromBytes(in, 1);
        this.chargeCount = Helper.int16FromBytes(in, 7);
        this.status = in[9];
        return true;

    }

    @Override
    public void characteristicChanged(byte[] newValue) {
        if (this.fromBytes(newValue)) {
            Intent i = new Intent(StringConstants.RESPONSE_BATTERY_INFO);
            i.putExtra(KEY_CHARGE_LEVEL, this.chargeLevel);
            i.putExtra(KEY_CHARGE_STATUS, this.status);
            i.putExtra(KEY_CHARGE_COUNT, this.chargeCount);
            i.putExtra(KEY_LAST_CHARGED, this.lastCharged);
            JENApp.makeBroadcast(i);
        }
        else {
            Log.e("BatteryInfo", "WTF? Couldn't get the right new value");
        }
    }
}
