package io.github.indrora.jouretnuit.model;

/**
 * Created by indrora on 7/7/15.
 */
public class StringConstants {

    // INTENT_xxx is sent from the service
    // BLE_xxx is sent TO the service.

    public static final String RESPONSE_DEVICE_CONNECTED = "respond:ble:deviceConnected";
    public static final String RESPONSE_STEP_COUNT = "respond:ble:updateSteps";
    public static final String RESPONSE_BATTERY_INFO = "respond:ble:device_batteryinfo";
    public static final String RESPONSE_SYNC_DATA = "respond:ble:getData";

    public static final String REQUEST_DEVICE_CONNECT = "ble:connect";
    public static final String REQUEST_DEVICE_DISCONNECT = "ble:disconnect";

    public static final String REQUEST_DEVICE_PAIR = "ble:pairDevice";
    public static final String REQUEST_STEP_UPDATES = "ble:registerSteps";

    public static final String REQUEST_STEP_COUNT = "ble:getSteps";
    public static final String REQUEST_DEVICE_NAME = "ble:getDeviceName";
    public static final String REQUEST_BATTERY_INFO = "ble:getBattery";
    public static final String REQUEST_BATTERY_USAGE = "ble:getUsage";

    public static final String BLE_SET_USER_INFO = "ble:setUserInfo";
    public static final String BLE_SET_LED_COLOR = "ble:setLedColor";
    public static final String BLE_SET_GOAL = "ble:setGoal";

    public static final String BLE_QUERY_BATTERY = "ble:queryBattery";
    public static final String BLE_QUERY_DEVICE_INFO = "ble:queryDevicInfo";

    public static final String BLE_GET_ACTIVITY_DATA = "ble:getActivityData";

    public static final String REQUEST_FRESH_VALUES = "ble:getFresh";
}
