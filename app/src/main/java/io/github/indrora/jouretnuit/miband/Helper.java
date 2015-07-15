package io.github.indrora.jouretnuit.miband;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by indrora on 7/7/15.
 */
public class Helper {
    public static UUID UUID16(final String s) {
        return UUID.fromString(String.format("0000%4s-0000-1000-8000-00805f9b34fb", s));
    }

    public static int int8FromBytes(byte[] data, int offset) {
        return data[offset] & 0xFF;
    }
    public static int int16FromBytes(byte[] data, int offset) {
        return data[offset] & 0xFF | ( data[offset+1] & 0xFF) << 8;
    }
    public static byte[] int8ToBytes(byte in) {
        return new byte[] { in };
    }
    public static byte[] int16ToBytes(short in) {
        return new byte[] { (byte)(in & 0xFF), (byte)( (in & 0xFF00) >> 8) };
    }

    public static int intFromBytes(byte[] buffer, int offset) {
        int tmp = 0;
        for(int bOffset = 0; bOffset < 4; bOffset++) {
            tmp |= (buffer[offset+bOffset] << bOffset*8);
        }
        return tmp;
    }

    public static byte[] appendArrays(byte[] ... arrays ) {
        byte[] retArr = new byte[0];
        int pos = 0;
        for(byte[] arr : arrays) {
            pos = retArr.length;
            retArr = Arrays.copyOf(retArr, retArr.length + arr.length);
            for(int idx = 0; idx < retArr.length; idx++) {
                retArr[pos+idx] = arr[idx];
            }
        }
        return retArr;
    }

    public static byte[] calendarToBytes(GregorianCalendar cal) {
        return new byte[] {
                (byte)( cal.get(GregorianCalendar.YEAR) - 2000),
                (byte)cal.get(GregorianCalendar.MONTH),
                (byte)cal.get(GregorianCalendar.DAY_OF_MONTH),
                (byte)cal.get(GregorianCalendar.HOUR_OF_DAY),
                (byte)cal.get(GregorianCalendar.MINUTE),
                (byte)cal.get(GregorianCalendar.SECOND)
        };
    }
    public static GregorianCalendar calendarFromBytes(byte[] buffer, int offset) {
        if(offset+5 > buffer.length) return null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(buffer[offset]+2000, buffer[offset+1], buffer[offset+2], buffer[offset+3], buffer[offset+4], buffer[offset+5]);
        return cal;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }
}
