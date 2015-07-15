package io.github.indrora.jouretnuit;


import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class SearchBandActivity extends Activity {

    BluetoothManager btManager;
    BluetoothDevice bDevice;

    private static final int STATE_SEARCH_DEVICE = 0;
    private static final int STATE_CONFIRM_DEVICE = 1;
    private static final int STATE_CONFIGURE_DEVICE = 2;

    private void setState(int state) {

    }

    View mSearchView;
    View mConfirmView;
    View mConfigView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_band);

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.flipper);
/*        mSearchView = getLayoutInflater().inflate(R.layout.search_scan, null);

        ImageView miliImage = (ImageView)mSearchView.findViewById(R.id.mili_image);
        Animation fadeAnim = new AlphaAnimation(0.5f, 1.0f);
        fadeAnim.setDuration(750);
        fadeAnim.setRepeatMode(Animation.REVERSE);
        fadeAnim.setRepeatCount(Animation.INFINITE);
        miliImage.startAnimation(fadeAnim);

        vf.addView(mSearchView, STATE_SEARCH_DEVICE);
*/

        mConfigView = getLayoutInflater().inflate(R.layout.search_profile, null);

        vf.addView(mConfigView);

        // get the bluetooth manager
        btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        btManager.getAdapter().startLeScan(scanCallback);
    }

    BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(device.getAddress().startsWith("88:0F:10")) {
                // It's the device we want.
                Log.d("SearchBandActivity", "Found Mi band @ " + device.getAddress() + " with rssi " + rssi);
                bDevice = device;
                // stop the scan.
                Toast.makeText(SearchBandActivity.this, "Found Mi Band: "+device.getAddress(), Toast.LENGTH_SHORT);


            }
        }
    };

}
