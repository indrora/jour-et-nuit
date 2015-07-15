package io.github.indrora.jouretnuit.miband.data;

/**
 * Created by indrora on 7/9/15.
 */
public interface IBinaryData {
    byte[] toBytes();
    boolean fromBytes(byte[] in);
}
