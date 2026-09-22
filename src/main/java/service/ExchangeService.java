package service;

import dao.ExchangeRatesDao;
import dto.response.CurrencyResponseDto;
import dto.response.ExchangeResponseDto;
import dto.request.ExchangeRequestDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.ExchangeNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


public class ExchangeService {
    private final ExchangeRatesDao exchangeRatesDao;

    private static final String CROSS_CURRENCY = "USD";
    private static final int RATE_SCALE = 6;
    private static final int AMOUNT_SCALE = 2;

    public ExchangeService(ExchangeRatesDao exchangeRatesDao) {
        this.exchangeRatesDao = exchangeRatesDao;
    }

    public ExchangeResponseDto convert(ExchangeRequestDto request) {

        return resolveDirect(request)
                .orElseGet(() -> resolveReverse(request)
                        .orElseGet(() -> resolveCross(request)
                                .orElseThrow(() -> new ExchangeNotFoundException("Cannot convert from "+request.baseCurrency()
                                        + " to " +request.targetCurrency()+" via USD"))));
    }

    private Optional<ExchangeResponseDto> resolveDirect(ExchangeRequestDto request) {
        return exchangeRatesDao
                .findByCode(request.baseCurrency(), request.targetCurrency())
                .map(exchangeRateEntity ->
                        buildDirect(exchangeRateEntity, request.amount()));
    }

    private Optional<ExchangeResponseDto> resolveReverse(ExchangeRequestDto request) {
        return exchangeRatesDao.findByCode(request.targetCurrency(),
                request.baseCurrency()).map(exchangeRateEntity ->
                buildReverse(exchangeRateEntity, request.amount()));
    }

    private Optional<ExchangeResponseDto> resolveCross(ExchangeRequestDto request){

        return exchangeRatesDao
                .findByCode(CROSS_CURRENCY, request.baseCurrency())
                .flatMap(baseRate -> exchangeRatesDao
                        .findByCode(CROSS_CURRENCY, request.targetCurrency())
                        .map(targetRate -> buildCrossResponse(request, baseRate, targetRate)));

    }

    private ExchangeResponseDto buildDirect(ExchangeRateEntity exchangeRateEntity, BigDecimal amount) {
        return new ExchangeResponseDto(
                convertToCurrencyDto(exchangeRateEntity.getBaseCurrency()),
                convertToCurrencyDto(exchangeRateEntity.getTargetCurrency()),
                exchangeRateEntity.getRate(),
                amount,
                amount.multiply(exchangeRateEntity.getRate()).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        );
    }

    private ExchangeResponseDto buildReverse(ExchangeRateEntity exchangeRateEntity, BigDecimal amount) {

        BigDecimal inverseRate = BigDecimal.ONE.divide(exchangeRateEntity.getRate(), RATE_SCALE, RoundingMode.HALF_UP);
        return new ExchangeResponseDto(
                convertToCurrencyDto(exchangeRateEntity.getTargetCurrency()),
                convertToCurrencyDto(exchangeRateEntity.getBaseCurrency()),
                inverseRate,
                amount,
                inverseRate.multiply(amount).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        );
    }

    private ExchangeResponseDto buildCrossResponse(ExchangeRequestDto request,
                                                    ExchangeRateEntity USDBaseResponse,
                                                    ExchangeRateEntity USDTargetResponse) {
        BigDecimal rate = USDTargetResponse.getRate().divide(USDBaseResponse.getRate(),RATE_SCALE, RoundingMode.HALF_UP);
        return new ExchangeResponseDto(
                convertToCurrencyDto(USDBaseResponse.getTargetCurrency()),
                convertToCurrencyDto(USDTargetResponse.getTargetCurrency()),
                rate,
                request.amount(),
                rate.multiply(request.amount()).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        );
    }

    private CurrencyResponseDto convertToCurrencyDto(CurrencyEntity currencyEntity) {
        return new CurrencyResponseDto(
                currencyEntity.getId(),
                currencyEntity.getCode(),
                currencyEntity.getName(),
                currencyEntity.getSign()
        );
    }
}