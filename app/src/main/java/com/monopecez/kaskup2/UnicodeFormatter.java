package com.monopecez.kaskup2;

/* loaded from: classes2.dex */
public class UnicodeFormatter {
    public static String byteToHex(byte b) {
        char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] array = {hexDigit[(b >> 4) & 15], hexDigit[b & 15]};
        return new String(array);
    }

    public static String charToHex(char c) {
        byte hi = (byte) (c >>> '\b');
        byte lo = (byte) (c & 255);
        return byteToHex(hi) + byteToHex(lo);
    }
}
