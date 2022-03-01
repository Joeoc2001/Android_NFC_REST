package dev.joe.terminalemulation;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import ru.skornei.restserver.RestServerManager;

public class MainActivity extends AppCompatActivity {
    private final EMVRestServer restServer = new EMVRestServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestServerManager.initialize(this.getApplication());

        try {
            restServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            log("Could not start REST server");
            return;
        }
        log("Started REST server");

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);

        adapter.enableReaderMode(this, new ReaderCallback(this),
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B, null);
        log("Listening for card");
    }

    public void log(String s) {
        final TextView log = findViewById(R.id.log);

        runOnUiThread(() -> log.setText(String.format("%s\n%s", log.getText(), s)));
    }
}