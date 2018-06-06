package com.edu.fag.app2bim.Util;

public class ByteUtil {
    public static int toInt(byte value) {
        return value & 0xFF;
    }

    public static int byteArrayToInt2(byte[] encodedValue) {
        int value = (encodedValue[1] << (Byte.SIZE * 1));
        value |= (encodedValue[1] & 0xFF) << (Byte.SIZE * 1);
        value |= (encodedValue[0] & 0xFF);
        return value;
    }
    //Converte 4 bytes em inteiro
    public static int byteArrayToInt4(byte[] encodedValue) {
        int value = (encodedValue[3] << (Byte.SIZE * 3));
        value |= (encodedValue[2] & 0xFF) << (Byte.SIZE * 2);
        value |= (encodedValue[1] & 0xFF) << (Byte.SIZE * 1);
        value |= (encodedValue[0] & 0xFF);
        return value;
    }
    //Converte um número em até 4 bytes
    public static byte[] intToByteArray(int value) {
        byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
        encodedValue[3] = (byte) (value >> Byte.SIZE * 3);
        encodedValue[2] = (byte) (value >> Byte.SIZE * 2);
        encodedValue[1] = (byte) (value >> Byte.SIZE);
        encodedValue[0] = (byte) value;
        return encodedValue;
    }
}
