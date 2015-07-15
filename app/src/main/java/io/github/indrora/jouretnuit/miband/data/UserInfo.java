package io.github.indrora.jouretnuit.miband.data;

import java.util.Arrays;

import io.github.indrora.jouretnuit.miband.Helper;

/**
 * Created by indrora on 7/9/15.
 */
public class UserInfo implements IBinaryData {

    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 0;

    public int age;
    public int height;
    public int mass;
    public int __type__;
    public int uid;
    public int gender;
    public byte[] alias;

    public String getStringAlias() {
        return String.valueOf(alias);
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public boolean fromBytes(byte[] in) {
        if(in == null) return false;
        if(in.length != 20) return false;
        uid = Helper.intFromBytes(in, 0);
        gender = in[4];
        age = in[5];
        height = in[6];
        mass = in[7];
        __type__ = in[8];
        alias = Arrays.copyOfRange(in, 9, 19);
        return true;
    }
}
