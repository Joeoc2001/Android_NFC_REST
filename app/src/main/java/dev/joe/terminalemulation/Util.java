package dev.joe.terminalemulation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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

    public static byte[] bytesFromString(String s) {
        int len = s.length();
        if (len % 2 == 1) {
            return bytesFromString("0" + s);
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) (
                    (Character.digit(s.charAt(i + 0), 16) << 4) +
                            (Character.digit(s.charAt(i + 1), 16) << 0)
            );
        }
        return data;
    }

    // Taken from https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }
}
