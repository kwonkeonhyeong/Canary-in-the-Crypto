package cic.analyze;

import cic.dto.BinanceAggTradeDto;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisService {

    private final Deque<MinuteTrade> window = new ConcurrentLinkedDeque<>();

    private double currentVolumeSum = 0.0;
    private static final long WINDOW_SIZE_MS = 60_000;

    public void processTrade(BinanceAggTradeDto trade) {
        long now = System.currentTimeMillis();
        double price = Double.parseDouble(trade.getPrice());
        double quantity = Double.parseDouble(trade.getQuantity());

        window.addLast(new MinuteTrade(now, price, quantity));
        currentVolumeSum += quantity;

        while (!window.isEmpty() && window.peekFirst().timestamp() < now - WINDOW_SIZE_MS) {
            MinuteTrade removed = window.pollFirst();
            currentVolumeSum -= removed.quantity();
        }

        calculateMetrics();
    }

    private void calculateMetrics() {
        if (window.isEmpty()) return;

        double startPrice = window.peekFirst().price();
        double endPrice = window.peekLast().price();
        double changeRate = ((endPrice - startPrice) / startPrice) * 100;

        log.info("📊 [1분 통계] 거래량 합계: {} | 변동률: {}%",
                String.format("%.4f", currentVolumeSum),
                String.format("%.2f", changeRate));
    }
}
