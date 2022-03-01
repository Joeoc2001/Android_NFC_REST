package dev.joe.terminalemulation;

public class Util {
    public static String byteToString(byte b) {
        int lower = b & 0xF;
        int upper = b >> 4;
        char[] lookup = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return String.valueOf(lookup[(upper + 16) % 16]) + lookup[(lower + 16) % 16];
    }

    public static String bytesToString(byte[] bs) {
        StringBuilder builder = new StringBuilder();

        for (byte b : bs) {
            builder.append(Util.byteToString(b));
        }

        return builder.toString();
    }
}
