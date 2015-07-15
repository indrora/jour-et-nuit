package io.github.indrora.jouretnuit;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import io.github.indrora.jouretnuit.miband.data.BatteryInfo;
import io.github.indrora.jouretnuit.miband.data.StepCount;
import io.github.indrora.jouretnuit.model.StringConstants;
import io.github.indrora.jouretnuit.widget.StepsView;


public class MainActivity extends android.app.Activity {

    private Map<String, BroadcastReceiver> broadcastReceiverMap = new HashMap<>();

    public MainActivity() {
        super();
        broadcastReceiverMap.put(StringConstants.RESPONSE_DEVICE_CONNECTED, onConnectBR);
        broadcastReceiverMap.put(StringConstants.RESPONSE_STEP_COUNT, onStepsBR);
        broadcastReceiverMap.put(StringConstants.RESPONSE_BATTERY_INFO, onBatteryBR);
        broadcastReceiverMap.put(StringConstants.RESPONSE_SYNC_DATA, syncCompleteBR);
    }

    RecyclerView.LayoutManager mLayoutManager;

    TextView stepsView;
    TextView batteryView;
    StepsView mStepsCard;
    ActivityHistoryAdapter activityHistoryAdapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.cardTray);

        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        stepsView = new TextView(this);
        stepsView.setText("...");
        batteryView = new TextView(this);
        batteryView.setText("Wooo!");

        mStepsCard = new StepsView(this);


        activityHistoryAdapter = new ActivityHistoryAdapter(
                new View[]{
                        mStepsCard,
                        stepsView,
                        batteryView},
                null
        );
        rv.setAdapter(activityHistoryAdapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullRefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(StringConstants.REQUEST_FRESH_VALUES);
                i.setClass(MainActivity.this, MiBandService.class);
                startService(i);
            }
        });
    }


    @Override
    protected void onResume() {

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        for (String s : broadcastReceiverMap.keySet()) {
            localBroadcastManager.registerReceiver(broadcastReceiverMap.get(s), new IntentFilter(s));
        }

        if (MiBandService.getContainer() != null) {
            // update the things
            stepsView.setText("Walked " + MiBandService.getContainer().device_steps.getSteps() + " steps");
        }
        super.onResume();
    }

    BroadcastReceiver onStepsBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivty", "onStepsBR: we get device_Steps!");
            int steps = intent.getIntExtra(StepCount.KEY_STEP_COUNT, -1);
            Log.d("MainActivity", "onStepsBR: Got step count "+steps);
            stepsView.setText("Walked " + steps + " device_Steps!");
            stepsView.invalidate();
            activityHistoryAdapter.notifyDataSetChanged();
        }
    };

    BroadcastReceiver onBatteryBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            for (String s : intent.getExtras().keySet()) {
                Log.d("Battery info", "< " + s + " > " + intent.getExtras().get(s));
            }
            batteryView.setText("Battery at " + intent.getIntExtra(BatteryInfo.KEY_CHARGE_LEVEL, -1) + "% ");
            activityHistoryAdapter.notifyDataSetChanged();
        }
    };


    BroadcastReceiver onConnectBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity:onConnect", "WE GET CONNECTION!");
            // We've connected. Get the step count.
            Intent i = new Intent();
            i.setAction(StringConstants.REQUEST_STEP_UPDATES);
            i.setClass(MainActivity.this, MiBandService.class);
            i.putExtra("enable", true);
            MainActivity.this.startService(i);
            i.setAction(StringConstants.REQUEST_FRESH_VALUES);
            i.removeExtra("enable");
            MainActivity.this.startService(i);
        }
    };

    BroadcastReceiver syncCompleteBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onStop() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        // Clean up the broadcast recievers we've used.
        for (BroadcastReceiver br : broadcastReceiverMap.values()) {
            localBroadcastManager.unregisterReceiver(br);
        }

        try {
            MiBandService.getContainer().disconnect();

        }
        catch(Exception e) {}
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
