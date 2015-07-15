package io.github.indrora.jouretnuit.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import io.github.indrora.jouretnuit.MiBandDevice;
import io.github.indrora.jouretnuit.MiBandService;
import io.github.indrora.jouretnuit.R;
import io.github.indrora.jouretnuit.model.StringConstants;


/**
 * Created by indrora on 7/14/15.
 */
public class StepsView extends LinearLayout {

    private void inflate(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_steps, this, true);
    }

    View rootView;
    public StepsView(Context context) {
        super(context);
        inflate(context);
    }

    public StepsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    TextView stepsTV;
    TextView batteryTV;
    TextView detailsTV;
    ProgressWheel spinny;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        stepsTV = (TextView)this.findViewById(R.id.stepsTV);
        detailsTV = (TextView)this.findViewById(R.id.detailsTV);
        batteryTV = (TextView)this.findViewById(R.id.batteryTV);
        spinny = (ProgressWheel)this.findViewById(R.id.stepsProgress);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StringConstants.RESPONSE_STEP_COUNT);
        intentFilter.addAction(StringConstants.RESPONSE_BATTERY_INFO);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        localBroadcastManager.registerReceiver(valuesBR, intentFilter);

        updateValues();

    }

    BroadcastReceiver valuesBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("StepsView", "Got intent...");
            updateValues();
        }
    };

    private void updateValues() {
        MiBandDevice device = MiBandService.getContainer();
        if(device != null) {
            int steps =  device.device_steps.getSteps();
            stepsTV.setText(String.format("%d steps",steps));
            int meters = (int)(0.7f*steps);
            detailsTV.setText("dist: " + meters);
            spinny.setProgress(steps / 5000f);

            Date d = device.device_batteryinfo.lastCharged.getTime();

            DateFormat df = DateFormat.getDateInstance();
            batteryTV.setText(String.format("%d%% - %s", device.device_batteryinfo.chargeLevel,df.format(d)) );
        }
        else {
            Log.d("StepsView", "WTF?");
            stepsTV.setText("???");
        }
        this.invalidate();
    }
}
