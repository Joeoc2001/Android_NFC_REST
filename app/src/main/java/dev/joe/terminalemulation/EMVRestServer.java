package dev.joe.terminalemulation;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class EMVRestServer extends NanoHTTPD {
    private final ReaderCallback callback;

    static final byte[] SELECT_PPSE = new byte[] {
            (byte)0x00, (byte)0xA4, (byte)0x04, (byte)0x00, (byte)0x0E, (byte)0x32, (byte)0x50, (byte)0x41, (byte)0x59, (byte)0x2E, (byte)0x53, (byte)0x59, (byte)0x53, (byte)0x2E, (byte)0x44, (byte)0x44, (byte)0x46, (byte)0x30, (byte)0x31, (byte)0x00
    };

    public EMVRestServer(ReaderCallback callback) {
        super(8080);

        this.callback = callback;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String answer;
        try {
            byte[] resp = callback.transceive(SELECT_PPSE);
            answer = Util.bytesToString(resp);
        } catch (IOException e) {
            e.printStackTrace();
            answer = e.toString();
        }

        return newFixedLengthResponse(answer);
    }
}