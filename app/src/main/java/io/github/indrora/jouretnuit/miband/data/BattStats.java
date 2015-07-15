package io.github.indrora.jouretnuit.miband.data;

import io.github.indrora.jouretnuit.miband.Helper;

/**
 * Created by indrora on 7/10/15.
 */
public class BattStats implements IBinaryData {

    int timeWake = 0;
    int timeLight = 0;
    int timeConnect = 0;
    int timeVibrate = 0;
    int timeOther = 0;

    @Override
    public byte[] toBytes() {
        return null;
    }

    @Override
    public boolean fromBytes(byte[] in) {
        if(in == null) return false;
        if(in.length != 20) { return false; }

        this.timeWake = Helper.intFromBytes(in, 0);
        this.timeVibrate = Helper.intFromBytes(in, 4);
        this.timeLight = Helper.intFromBytes(in, 8);
        this.timeConnect = Helper.intFromBytes(in, 12);
        this.timeOther = Helper.intFromBytes(in, 16);

        return true;

    }
}
