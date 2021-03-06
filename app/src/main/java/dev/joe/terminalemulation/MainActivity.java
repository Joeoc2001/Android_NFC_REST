package dev.joe.terminalemulation;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EMVRestServer restServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);

        ReaderCallback callback = new ReaderCallback(this);
        adapter.enableReaderMode(this, callback,
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B, null);
        log("Listening for card");

        // Don't go into standby since that disables the reader mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        restServer = new EMVRestServer(callback);
        try {
            restServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            log("Could not start REST server");
            return;
        }
        log("Started REST server");
        log("IP: " + Util.getIPAddress(true) + ":8080");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (restServer != null) {
            restServer.stop();
        }
    }

    public void log(String s) {
        final TextView log = findViewById(R.id.log);

        runOnUiThread(() -> log.setText(String.format("%s\n%s", log.getText(), s)));
    }
}