package dev.joe.terminalemulation;

import ru.skornei.restserver.annotations.Produces;
import ru.skornei.restserver.annotations.RestController;
import ru.skornei.restserver.annotations.RestServer;
import ru.skornei.restserver.annotations.methods.GET;
import ru.skornei.restserver.server.BaseRestServer;
import ru.skornei.restserver.server.dictionary.ContentType;
import ru.skornei.restserver.server.protocol.ResponseInfo;

@RestServer(
        port = EMVRestServer.PORT,
        controllers = {
            EMVRestServer.PingController.class
        }
)
public class EMVRestServer extends BaseRestServer {
    public static final int PORT = 8080;

    @RestController("/ping")
    public static class PingController {
        @GET
        @Produces(ContentType.TEXT_PLAIN)
        public void ping(ResponseInfo response) {
            response.setBody("Pong!".getBytes());
        }
    }
}
