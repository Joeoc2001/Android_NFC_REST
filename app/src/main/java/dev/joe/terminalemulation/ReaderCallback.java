package dev.joe.terminalemulation;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;

import java.io.IOException;

public class ReaderCallback implements NfcAdapter.ReaderCallback {
    private final MainActivity activity;
    private IsoDep nfc = null;

    public ReaderCallback(MainActivity activity) {
        this.activity = activity;
    }

    public byte[] transceive(byte[] toSend) throws IOException {
        if (nfc == null || !nfc.isConnected()) {
            close();
            throw new IOException("No NFC card connected");
        }

        activity.log("Send: " + Util.bytesToString(toSend));

        byte[] got;
        try {
            got = nfc.transceive(toSend);
        } catch (IOException e) {
            e.printStackTrace();
            activity.log("Fail on receive");
            throw e;
        }

        activity.log("Recv: " + Util.bytesToString(got));

        return got;
    }

    public void close() {
        if (nfc != null && nfc.isConnected()) {
            try {
                nfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        nfc = null;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        nfc = IsoDep.get(tag);
        try {
            nfc.connect();
        } catch (IOException e) {
            e.printStackTrace();
            activity.log("Failed to connect to card");
            nfc = null;
            return;
        }

        activity.log("Card connected");
        nfc.setTimeout(100);
    }
}
