package service;

import dao.CurrencyDaoImpl;
import dao.ExchangeRatesDaoImpl;
import dto.request.ExchangeRatesRequestDto;
import dto.response.CurrencyResponseDto;
import dto.response.ExchangeRateResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.DataNotFoundException;
import exception.InternalErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRateService {

    private final ExchangeRatesDaoImpl exchangeRatesDaoImpl;
    private final CurrencyDaoImpl currencyDaoImpl;

    public ExchangeRateService() {
        this(ExchangeRatesDaoImpl.getInstance(), CurrencyDaoImpl.getInstance());
    }

    public ExchangeRateService(ExchangeRatesDaoImpl exchangeRatesDaoImpl, CurrencyDaoImpl currencyDaoImpl) {
        this.exchangeRatesDaoImpl = exchangeRatesDaoImpl;
        this.currencyDaoImpl = currencyDaoImpl;
    }

    public List<ExchangeRateResponseDto> findAll() {
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRatesDaoImpl.findAll();
        List<ExchangeRateResponseDto> exchangeRateResponseDtoList = new ArrayList<>();

        if (exchangeRateEntityList.isEmpty()) {
            return exchangeRateResponseDtoList;
        }

        for (ExchangeRateEntity exchangeRateEntity : exchangeRateEntityList) {
            exchangeRateResponseDtoList.add(convertToDto(exchangeRateEntity));
        }
        return exchangeRateResponseDtoList;

    }

    public ExchangeRateResponseDto findByCodes(String baseCode, String targetCode) {
        Optional<ExchangeRateEntity> exchangeRatesEntity = exchangeRatesDaoImpl.findByCode(baseCode, targetCode);

        if (exchangeRatesEntity.isEmpty()) {
            throw new DataNotFoundException("Exchange rate for these codes not found");
        }
        return convertToDto(exchangeRatesEntity.get());
    }

    public ExchangeRateResponseDto create(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRateEntity exchangeRateEntity = convertToEntity(exchangeRatesRequestDto);
        Optional<ExchangeRateEntity> addedExchangeRate = exchangeRatesDaoImpl.create(exchangeRateEntity);

        if (addedExchangeRate.isEmpty()) {
            throw new InternalErrorException("Internal error");
        }
        return convertToDto(addedExchangeRate.get());
    }

    public ExchangeRateResponseDto update(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRateEntity exchangeRateEntity = convertToEntity(exchangeRatesRequestDto);

        Optional<ExchangeRateEntity> result = exchangeRatesDaoImpl.update(exchangeRateEntity);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found for pair "
                    + exchangeRatesRequestDto.getBaseCurrency() +
                    "/" + exchangeRatesRequestDto.getTargetCurrency());
        }
        return convertToDto(result.get());
    }

    private ExchangeRateResponseDto convertToDto(ExchangeRateEntity exchangeRateEntity) {
        return new ExchangeRateResponseDto(
                exchangeRateEntity.getId(),
                convertToDto(exchangeRateEntity.getBaseCurrency()),
                convertToDto(exchangeRateEntity.getTargetCurrency()),
                exchangeRateEntity.getRate()
        );
    }

    private CurrencyResponseDto convertToDto(CurrencyEntity currencyEntity) {
        return new CurrencyResponseDto(
                currencyEntity.getId(),
                currencyEntity.getCode(),
                currencyEntity.getName(),
                currencyEntity.getSign()
        );
    }

    private ExchangeRateEntity convertToEntity(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        Optional<CurrencyEntity> baseCurrency = currencyDaoImpl.findByCode(exchangeRatesRequestDto.getBaseCurrency());
        Optional<CurrencyEntity> targetCurrency = currencyDaoImpl.findByCode(exchangeRatesRequestDto.getTargetCurrency());

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Base or target currencies not found");
        }
        return new ExchangeRateEntity(baseCurrency.get(), targetCurrency.get(), exchangeRatesRequestDto.getRate());
    }
}