package cic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Binance Aggregated Trade 데이터 매핑
 *
 * Payload 예시:
 * {
 *   "e": "aggTrade",  // Event type
 *   "E": 1672515782,  // Event time (eventTime)
 *   "s": "BTCUSDT",   // Symbol (symbol)
 *   "a": 12345,       // Aggregate trade ID
 *   "p": "0.001",     // Price (price)
 *   "q": "100",       // Quantity (quantity)
 *   "f": 100,         // First trade ID
 *   "l": 105,         // Last trade ID
 *   "T": 1672515781,  // Trade time
 *   "m": true,        // Is the buyer the market maker? (isBuyerMaker)
 *   "M": true         // Ignore
 * }
 */
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceAggTradeDto {
    @JsonProperty("s")
    private String symbol;

    @JsonProperty("a")
    private String aggregateTradeId;

    @JsonProperty("p")
    private String price;

    @JsonProperty("q")
    private String quantity;

    @JsonProperty("f")
    private String firstTradeId;

    @JsonProperty("l")
    private String lastTradeId;

    @JsonProperty("E")
    private Long eventTime;

    @JsonProperty("m")
    private Boolean isBuyerMaker;
}
