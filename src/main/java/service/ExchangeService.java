package service;

import dao.ExchangeRatesDaoImpl;
import dto.response.ExchangeResponseDto;
import dto.request.ExchangeRequestDto;
import entity.ExchangeRatesEntity;
import exception.ExchangeNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


public class ExchangeService {
    private final ExchangeRatesDaoImpl exchangeRatesDaoImpl = ExchangeRatesDaoImpl.getInstance();
    private static final String CROSS_CURRENCY = "USD";
    private static final int RATE_SCALE = 6;
    private static final int AMOUNT_SCALE = 2;

    public ExchangeResponseDto convert(ExchangeRequestDto request) {

        return resolveDirect(request)
                .orElseGet(() -> resolveReverse(request)
                        .orElseGet(() -> resolveCross(request)
                                .orElseThrow(() -> new ExchangeNotFoundException("Cannot convert from "+request.getBaseCurrency()
                                        + " to " +request.getTargetCurrency()+" via USD"))));
    }

    private Optional<ExchangeResponseDto> resolveDirect(ExchangeRequestDto request) {
        return exchangeRatesDaoImpl
                .findByCode(request.getBaseCurrency(), request.getTargetCurrency())
                .map(exchangeRatesEntity ->
                        buildDirect(exchangeRatesEntity, request.getAmount()));
    }

    private Optional<ExchangeResponseDto> resolveReverse(ExchangeRequestDto request) {
        return exchangeRatesDaoImpl.findByCode(request.getTargetCurrency(),
                request.getBaseCurrency()).map(exchangeRatesEntity ->
                buildReverse(exchangeRatesEntity, request.getAmount()));
    }

    private Optional<ExchangeResponseDto> resolveCross(ExchangeRequestDto request){

        return exchangeRatesDaoImpl
                .findByCode(CROSS_CURRENCY, request.getBaseCurrency())
                .flatMap(baseRate -> exchangeRatesDaoImpl
                        .findByCode(CROSS_CURRENCY, request.getTargetCurrency())
                        .map(targetRate -> buildCrossResponse(request, baseRate, targetRate)));

    }

    private ExchangeResponseDto buildDirect(ExchangeRatesEntity exchangeRatesEntity, BigDecimal amount) {
        return new ExchangeResponseDto(
                exchangeRatesEntity.getBaseCurrency(),
                exchangeRatesEntity.getTargetCurrency(),
                exchangeRatesEntity.getRate(),
                amount,
                amount.multiply(exchangeRatesEntity.getRate()).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        );
    }

    private ExchangeResponseDto buildReverse(ExchangeRatesEntity exchangeRatesEntity, BigDecimal amount) {

        BigDecimal inverseRate = BigDecimal.ONE.divide(exchangeRatesEntity.getRate(), RATE_SCALE, RoundingMode.HALF_UP);
        return new ExchangeResponseDto(
                exchangeRatesEntity.getTargetCurrency(),
                exchangeRatesEntity.getBaseCurrency(),
                inverseRate,
                amount,
                inverseRate.multiply(amount).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        );
    }

    private ExchangeResponseDto buildCrossResponse(ExchangeRequestDto request,
                                                    ExchangeRatesEntity USDBaseResponse,
                                                    ExchangeRatesEntity USDTargetResponse) {
        BigDecimal rate = USDTargetResponse.getRate().divide(USDBaseResponse.getRate(),RATE_SCALE, RoundingMode.HALF_UP);
        return new ExchangeResponseDto(
                USDBaseResponse.getTargetCurrency(),
                USDTargetResponse.getTargetCurrency(),
                rate,
                request.getAmount(),
                rate.multiply(request.getAmount()).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        );
    }
}