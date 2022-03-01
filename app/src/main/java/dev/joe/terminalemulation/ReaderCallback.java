package dev.joe.terminalemulation;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;

import java.io.IOException;

public class ReaderCallback implements NfcAdapter.ReaderCallback {
    private final MainActivity activity;

    static final byte[] SELECT_PPSE = new byte[] {
            (byte)0x00, (byte)0xA4, (byte)0x04, (byte)0x00, (byte)0x0E, (byte)0x32, (byte)0x50, (byte)0x41, (byte)0x59, (byte)0x2E, (byte)0x53, (byte)0x59, (byte)0x53, (byte)0x2E, (byte)0x44, (byte)0x44, (byte)0x46, (byte)0x30, (byte)0x31, (byte)0x00
    };

    public ReaderCallback(MainActivity activity) {
        this.activity = activity;
    }

    private void queryLoop(IsoDep nfc) {
        while (true) {
            byte[] sending = SELECT_PPSE;

            activity.log("Send: " + Util.bytesToString(sending));

            byte[] got;
            try {
                got = nfc.transceive(sending);
            } catch (IOException e) {
                e.printStackTrace();
                activity.log("Fail on receive");
                return;
            }

            activity.log("Recv: " + Util.bytesToString(got));
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep nfc = IsoDep.get(tag);
        try {
            nfc.connect();
        } catch (IOException e) {
            e.printStackTrace();
            activity.log("Failed to connect to card");
            return;
        }

        activity.log("Card connected");
        nfc.setTimeout(100);

        Thread thread = new Thread(() -> {
            queryLoop(nfc);

            try {
                nfc.close();
            } catch (IOException e) {
                e.printStackTrace();
                activity.log("Failed to disconnect from card");
                return;
            }

            activity.log("Card disconnected");
        });

        thread.start();
    }
}
