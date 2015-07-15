package io.github.indrora.jouretnuit.model;

import java.util.Calendar;

import io.github.indrora.jouretnuit.miband.data.UserInfo;

/**
 * Created by indrora on 7/5/15.
 */
public class ActivityPeriod {
    public Calendar start;
    // we'll divide this by 60 later.
    public int minutes;
    public int steps;
    // in meters
    public int distance;

    public ActivityPeriod() {
        start = Calendar.getInstance();
        minutes = 0;
        steps = 0;
        distance = 0;
    }

    public float caloriesBurned(UserInfo user) {
        // CB = [0.0215 x KPH^3 - 0.1765 x KPH^2 + 0.8710 x KPH + 1.4577] x WKG x T
        float km = (float)distance/1000f;
        float hr = (float)minutes/60f;
        float kmh = km/hr;
        return (
                0.0215f * (float)Math.pow(kmh, 3)
                - 0.7165f * (float)Math.pow(kmh, 2)
                + 1.4577f
                ) * (float)(user.mass) * hr;
    }


}
