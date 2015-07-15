package io.github.indrora.jouretnuit;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import io.github.indrora.jouretnuit.model.StringConstants;

/**
 * Created by indrora on 7/7/15.
 */
public class JENApp extends Application {

    private static JENApp _instance;

    private static MiBandDevice _miBand = MiBandService.getContainer();

    public static void makeBroadcast(Intent i) {
        Log.d("JENApp", "Sending broadcast for "+i.getAction());
        if(_instance != null) {
            LocalBroadcastManager.getInstance(_instance).sendBroadcast(i);
        }
        else {
            Log.d("JENApp", "Can't send intent: JENApp hasn't been done yet.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("JENApp", "Startup -> onCreate");
        _instance = this;

/*        Intent i = new Intent();
        i.setAction(StringConstants.REQUEST_DEVICE_CONNECT);
        i.setClass(this, MiBandService.class);
        i.putExtra("address", "88:0F:10:86:EF:F7");
        startService(i);
*/
        Intent i = new Intent();
        i.setClass(this, SearchBandActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    public void onTerminate() {

        Intent i = new Intent(StringConstants.REQUEST_DEVICE_DISCONNECT);
        startService(i);

        super.onTerminate();
    }
}
