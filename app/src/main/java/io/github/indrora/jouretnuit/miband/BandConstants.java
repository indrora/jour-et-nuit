package io.github.indrora.jouretnuit.miband;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by indrora on 7/6/15.
 */
public class BandConstants {

    public static final int ALIAS_LEN = 10;

    public static final int USER_GENDER_FEMALE = 0;
    public static final int USER_GENDER_MALE = 1;

    /**
     * Things we can be notified about.
     */
    public final class NOTIFY {
        public static final int NORMAL = 0;
        public static final int DEVICE_MALFUNCTION = 255;
        public static final int FITNESS_GOAL_ACHIEVED = 7;
        public static final int PAIR_CANCEL = 239;
        public static final int SET_LATENCY_SUCCESS = 8;
        public static final int UNKNOWN = -1;

        /**
         * Authentication (??)
         */
        public final class AUTHENTICATION {
            public static final int FAILED = 6;
            public static final int SUCCESS = 5;
        }

        /**
         * Connection parameter updates
         */
        public final class CONN_PARAM_UPDATE {
            public static final int FAILED = 3;
            public static final int SUCCESS = 4;
        }

        /**
         * Firmware (update?)
         */
        public final class FIRMWARE {
            public static final int FAILED = 1;
            public static final int SUCCESS = 2;
        }

        /**
         * FW check (??)
         */
        public final class FW_CHECK {
            public static final int FAILED = 11;
            public static final int SUCCESS = 12;
        }

        /**
         * Reset authentication
         */
        public final class RESET_AUTHENTICATION {
            public static final int FAILED = 9;
            public static final int SUCCESS = 10;
        }

        /**
         * Motor status?
         */
        public final class STATUS {
            public static final int MOTOR_ALARM = 17;
            public static final int MOTOR_AUTH = 19;
            public static final int MOTOR_AUTH_SUCCESS = 21;
            public static final int MOTOR_CALL = 14;
            public static final int MOTOR_DISCONNECT = 15;
            public static final int MOTOR_GOAL = 18;
            public static final int MOTOR_NOTIFY = 13;
            public static final int MOTOR_SHUTDOWN = 20;
            public static final int MOTOR_SMART_ALARM = 16;
            public static final int MOTOR_TEST = 22;
        }
    }

    public static final UUID UUID_DESCRIPTOR_CHARACTERISTIC_USER_CONFIGURATION = Helper.UUID16("2901");
    public static final UUID UUID_DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION = Helper.UUID16("2902");


    public static final UUID UUID_CHARACTERISTIC_ACTIVITY_DATA = Helper.UUID16("FF07");
    public static final UUID UUID_CHARACTERISTIC_BATTERY = Helper.UUID16("FF0C");
    public static final UUID UUID_CHARACTERISTIC_CONTROL_POINT = Helper.UUID16("FF05");
    public static final UUID UUID_CHARACTERISTIC_DATE_TIME = Helper.UUID16("FF0A");
    public static final UUID UUID_CHARACTERISTIC_DEVICE_INFO = Helper.UUID16("FF01");
    public static final UUID UUID_CHARACTERISTIC_DEVICE_NAME = Helper.UUID16("FF02");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_DATA = Helper.UUID16("FF08");
    public static final UUID UUID_CHARACTERISTIC_LE_PARAMS = Helper.UUID16("FF09");
    public static final UUID UUID_CHARACTERISTIC_NOTIFICATION = Helper.UUID16("FF03");
    public static final UUID UUID_CHARACTERISTIC_REALTIME_STEPS = Helper.UUID16("FF06");
    public static final UUID UUID_CHARACTERISTIC_SENSOR_DATA = Helper.UUID16("FF0E");
    public static final UUID UUID_CHARACTERISTIC_STATISTICS = Helper.UUID16("FF0B");
    public static final UUID UUID_CHARACTERISTIC_TEST = Helper.UUID16("FF0D");
    public static final UUID UUID_CHARACTERISTIC_USER_INFO = Helper.UUID16("FF04");
    public static final UUID UUID_SERVICE_MILI_SERVICE = Helper.UUID16("FEE0");



    public static String getHelp(UUID which) {
        if(which.equals(UUID_CHARACTERISTIC_ACTIVITY_DATA )) return "known: activity data";
        if(which.equals(UUID_CHARACTERISTIC_BATTERY)) return "known: battery";
        if(which.equals(UUID_CHARACTERISTIC_CONTROL_POINT )) return "known: control point";
        if(which.equals(UUID_CHARACTERISTIC_DATE_TIME)) return "known: date_time";
        if(which.equals(UUID_CHARACTERISTIC_DEVICE_INFO)) return "known: device_info";
        if(which.equals(UUID_CHARACTERISTIC_DEVICE_NAME)) return "known: device_name";
        if(which.equals(UUID_CHARACTERISTIC_FIRMWARE_DATA)) return "known: firmware_data";
        if(which.equals(UUID_CHARACTERISTIC_LE_PARAMS )) return "known: le_params";
        if(which.equals(UUID_CHARACTERISTIC_NOTIFICATION)) return "known: notification";
        if(which.equals(UUID_CHARACTERISTIC_REALTIME_STEPS)) return "known: realtime_steps";
        if(which.equals(UUID_CHARACTERISTIC_SENSOR_DATA)) return "known: sensor_data";
        if(which.equals(UUID_CHARACTERISTIC_STATISTICS )) return "known: statistics";
        if(which.equals(UUID_CHARACTERISTIC_TEST)) return "known: test";
        if(which.equals(UUID_CHARACTERISTIC_USER_INFO)) return "known: user_info";
        if(which.equals(UUID_SERVICE_MILI_SERVICE)) return "known: Service MILI_SERVICE";
        return "unkown: "+which.toString();
    }

}
