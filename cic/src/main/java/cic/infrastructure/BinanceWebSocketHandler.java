package cic.infrastructure;

import cic.analyze.AnalysisService;
import cic.dto.BinanceAggTradeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class BinanceWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AnalysisService analysisService;

    private Long lastLastId = null;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        BinanceAggTradeDto trade = objectMapper.readValue(message.getPayload(), BinanceAggTradeDto.class);
        // 로그 출력: 가격과 거래량을 실시간으로 확인

        long currentFirstId = Long.parseLong(trade.getFirstTradeId()); // JSON의 "f"
        long currentLastId = Long.parseLong(trade.getLastTradeId());   // JSON의 "l"

        // 누락 검증 로직
        if (lastLastId != null) {
            if (currentFirstId != lastLastId + 1) {
                long missingCount = currentFirstId - (lastLastId + 1);
                // 로그를 WARN 레벨로 찍어 터미널에서 눈에 띄게 합니다.
                log.warn("⚠️ [데이터 누락] {}건 유실! (예상 시작 ID: {}, 실제 시작 ID: {})",
                        missingCount, lastLastId + 1, currentFirstId);
            }
        }

        // 상태 업데이트: 다음 비교를 위해 현재의 마지막 ID를 저장
        lastLastId = currentLastId;

        /*log.info("[{}] 가격: {} | 수량: {} | 매도자마켓: {} | AggregateId: {} | FirstTradeID: {} | LastTradeID: {}",
                trade.getSymbol(),
                trade.getPrice(),
                trade.getQuantity(),
                trade.getIsBuyerMaker(),
                trade.getAggregateTradeId(),
                trade.getFirstTradeId(),
                trade.getLastTradeId()
        );*/

        analysisService.processTrade(trade);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.warn("❌ 바이낸스 연결 종료: {}. 재연결을 시도합니다.", status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("바이낸스 연결 성공!");
    }
}
