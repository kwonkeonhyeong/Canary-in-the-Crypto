package cic.service;

import cic.infrastructure.BinanceWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinanceSocketService {
    private final BinanceWebSocketHandler handler;
    private WebSocketConnectionManager webSocketConnectionManager;
    private static final String AGG_TRADE_URL = "wss://stream.binance.com:9443/ws/btcusdt@aggTrade";

    public void startConnection() {
        if (webSocketConnectionManager != null && webSocketConnectionManager.isRunning()) {
            webSocketConnectionManager.stop();
        }

        webSocketConnectionManager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                handler,
                AGG_TRADE_URL
        );
        webSocketConnectionManager.setAutoStartup(true);
        webSocketConnectionManager.start();
        log.info("🚀 바이낸스 웹소켓 연결 시작");
    }

    // 23시간 50분마다(85,800,000ms) 실행하여 연결 갱신
    @Scheduled(fixedRate = 85_800_000)
    public void refreshConnection() {
        log.info("🔄 24시간 주기 연결 갱신을 시작합니다.");
        startConnection();
    }
}
