package io.github.indrora.jouretnuit.miband.data;

import android.content.Intent;
import android.util.Log;

import io.github.indrora.jouretnuit.JENApp;
import io.github.indrora.jouretnuit.miband.Helper;
import io.github.indrora.jouretnuit.model.StringConstants;

/**
 * Created by indrora on 7/16/15.
 */
public class StepCount implements IBinaryData, ICharacteristicBound {
    private int steps = 0;

    public static final String KEY_STEP_COUNT = "steps";

    public int getSteps() { return steps; }

    @Override
    public byte[] toBytes() {
        return null;
    }

    @Override
    public boolean fromBytes(byte[] in) {
        if(in.length == 4) {
            steps = (in[0] & 0xFF) +
                    ((in[1] & 0xFF) << 8 )+
                    ((in[2] & 0xFF) << 16 )+
                    ((in[3] & 0xFF) << 24 );
            Log.d("StepCount", "converted step count: "+steps);
            return true;
        }

        return false;
    }

    @Override
    public void characteristicChanged(byte[] newValue) {
        if(!this.fromBytes(newValue)) {
            Log.e("StepCount", "Failed: step count is wrong!");
        }
        Intent intent = new Intent(StringConstants.RESPONSE_STEP_COUNT);
        intent.putExtra(KEY_STEP_COUNT, this.steps);
        JENApp.makeBroadcast(intent);
    }
}
