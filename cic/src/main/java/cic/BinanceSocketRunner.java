package cic;

import cic.infrastructure.BinanceWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Component
@RequiredArgsConstructor
public class BinanceSocketRunner implements CommandLineRunner {

    private final BinanceWebSocketHandler handler;

    @Override
    public void run(String... args) throws Exception {
        String url = "wss://stream.binance.com:9443/ws/btcusdt@aggTrade";

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                handler,
                url
        );
        manager.setAutoStartup(true);
        manager.start();
    }
}
