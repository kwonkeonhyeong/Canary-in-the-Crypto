package cic.infrastructure;

import cic.dto.BinanceAggTradeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class BinanceWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        BinanceAggTradeDto trade = objectMapper.readValue(message.getPayload(), BinanceAggTradeDto.class);
        // 로그 출력: 가격과 거래량을 실시간으로 확인

        log.info("[{}] 가격: {} | 수량: {} | 매도자마켓: {}",
                trade.getSymbol(), trade.getPrice(), trade.getQuantity(), trade.getIsBuyerMaker());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("바이낸스 연결 성공!");
    }
}
