package service;

import dao.CurrencyDaoImpl;
import dao.ExchangeRatesDaoImpl;
import dto.request.ExchangeRatesRequestDto;
import dto.response.ExchangeRatesResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.DataNotFoundException;
import exception.InternalErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRatesService {

    private final ExchangeRatesDaoImpl exchangeRatesDaoImpl;
    private final CurrencyDaoImpl currencyDaoImpl;

    public ExchangeRatesService() {
        this(ExchangeRatesDaoImpl.getInstance(), CurrencyDaoImpl.getInstance());
    }

    public ExchangeRatesService(ExchangeRatesDaoImpl exchangeRatesDaoImpl, CurrencyDaoImpl currencyDaoImpl) {
        this.exchangeRatesDaoImpl = exchangeRatesDaoImpl;
        this.currencyDaoImpl = currencyDaoImpl;
    }

    public List<ExchangeRatesResponseDto> findAll() {
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRatesDaoImpl.findAll();
        List<ExchangeRatesResponseDto> exchangeRatesResponseDtoList = new ArrayList<>();

        if (exchangeRateEntityList.isEmpty()) {
            return exchangeRatesResponseDtoList;
        }

        for (ExchangeRateEntity exchangeRateEntity : exchangeRateEntityList) {
            exchangeRatesResponseDtoList.add(convertToDto(exchangeRateEntity));
        }
        return exchangeRatesResponseDtoList;

    }

    public ExchangeRatesResponseDto findByCodes(String baseCode, String targetCode) {
        Optional<ExchangeRateEntity> exchangeRatesEntity = exchangeRatesDaoImpl.findByCode(baseCode, targetCode);

        if (exchangeRatesEntity.isEmpty()) {
            throw new DataNotFoundException("Exchange rate for these codes not found");
        }
        return convertToDto(exchangeRatesEntity.get());
    }

    public ExchangeRatesResponseDto create(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRateEntity exchangeRateEntity = convertToEntity(exchangeRatesRequestDto);
        Optional<ExchangeRateEntity> addedExchangeRate = exchangeRatesDaoImpl.create(exchangeRateEntity);

        if (addedExchangeRate.isEmpty()) {
            throw new InternalErrorException("Internal error");
        }
        return convertToDto(addedExchangeRate.get());
    }

    public ExchangeRatesResponseDto update(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRateEntity exchangeRateEntity = convertToEntity(exchangeRatesRequestDto);

        Optional<ExchangeRateEntity> result = exchangeRatesDaoImpl.update(exchangeRateEntity);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found for pair "
                    + exchangeRatesRequestDto.getBaseCurrency() +
                    "/" + exchangeRatesRequestDto.getTargetCurrency());
        }
        return convertToDto(result.get());
    }

    private ExchangeRatesResponseDto convertToDto(ExchangeRateEntity exchangeRateEntity) {
        return new ExchangeRatesResponseDto(
                exchangeRateEntity.getId(),
                exchangeRateEntity.getBaseCurrency(),
                exchangeRateEntity.getTargetCurrency(),
                exchangeRateEntity.getRate()
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