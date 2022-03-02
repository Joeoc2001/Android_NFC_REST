package dev.joe.terminalemulation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class EMVRestServer extends NanoHTTPD {
    private final ReaderCallback callback;

    public EMVRestServer(ReaderCallback callback) {
        super(8080);

        this.callback = callback;
    }

    private <K, T> T getOrDefault(Map<K, T> map, K key, T def) {
        T val = map.containsKey(key) ? map.get(key) : def;
        val = val == null ? def : val;

        return val;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getMethod() != Method.GET) {
            return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, MIME_PLAINTEXT, "");
        }

        Map<String, List<String>> params = session.getParameters();
        // Only take the first of each
        Map<String, String> args = new HashMap<>();
        for (Map.Entry<String, List<String>> v : params.entrySet()) {
            args.put(v.getKey(), v.getValue().get(0));
        }

        switch (session.getUri()) {
            case "/transceive":
                return transceive(args);
            case "/is_connected":
                return isConnected(args);
            default:
                return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, session.getUri());
        }
    }

    private Response isConnected(Map<String, String> args) {
        String answer = callback.isConnected() ? "True" : "False";

        return newFixedLengthResponse(answer);
    }

    private Response transceive(Map<String, String> args) {
        String cmd = getOrDefault(args, "cmd", "");
        byte[] command = Util.bytesFromString(cmd);

        byte[] resp;
        try {
            resp = callback.transceive(command);
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, e.getMessage());
        }

        String answer = Util.bytesToString(resp);
        return newFixedLengthResponse(answer);
    }
}