package service;

import dao.CurrencyDaoImpl;
import dao.ExchangeRatesDaoImpl;
import dto.request.ExchangeRatesRequestDto;
import dto.response.ExchangeRatesResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRatesEntity;
import exception.DataNotFoundException;
import exception.InternalErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRatesService {

    private final ExchangeRatesDaoImpl exchangeRatesDaoImpl = ExchangeRatesDaoImpl.getInstance();
    private final CurrencyDaoImpl currencyDaoImpl = CurrencyDaoImpl.getInstance();

    public List<ExchangeRatesResponseDto> findAll() {
        List<ExchangeRatesEntity> exchangeRatesEntityList = exchangeRatesDaoImpl.findAll();
        List<ExchangeRatesResponseDto> exchangeRatesResponseDtoList = new ArrayList<>();

        if (exchangeRatesEntityList.isEmpty()) {
            return exchangeRatesResponseDtoList;
        }

        for (ExchangeRatesEntity exchangeRatesEntity : exchangeRatesEntityList) {
            exchangeRatesResponseDtoList.add(convertToDto(exchangeRatesEntity));
        }
        return exchangeRatesResponseDtoList;

    }

    public ExchangeRatesResponseDto findByCodes(String baseCode, String targetCode) {
        Optional<ExchangeRatesEntity> exchangeRatesEntity = exchangeRatesDaoImpl.findByCode(baseCode, targetCode);

        if (exchangeRatesEntity.isEmpty()) {
            throw new DataNotFoundException("Exchange rate for these codes not found");
        }
        return convertToDto(exchangeRatesEntity.get());
    }

    public ExchangeRatesResponseDto create(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRatesEntity exchangeRatesEntity = convertToEntity(exchangeRatesRequestDto);
        Optional<ExchangeRatesEntity> addedExchangeRate = exchangeRatesDaoImpl.create(exchangeRatesEntity);

        if (addedExchangeRate.isEmpty()) {
            throw new InternalErrorException("Internal error");
        }
        return convertToDto(addedExchangeRate.get());
    }

    public ExchangeRatesResponseDto update(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRatesEntity exchangeRatesEntity = convertToEntity(exchangeRatesRequestDto);

        Optional<ExchangeRatesEntity> result = exchangeRatesDaoImpl.update(exchangeRatesEntity);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found for pair "
                    + exchangeRatesRequestDto.getBaseCurrency() +
                    "/" + exchangeRatesRequestDto.getTargetCurrency());
        }
        return convertToDto(result.get());
    }

    private ExchangeRatesResponseDto convertToDto(ExchangeRatesEntity exchangeRatesEntity) {
        return new ExchangeRatesResponseDto(
                exchangeRatesEntity.getId(),
                exchangeRatesEntity.getBaseCurrency(),
                exchangeRatesEntity.getTargetCurrency(),
                exchangeRatesEntity.getRate()
        );
    }

    private ExchangeRatesEntity convertToEntity(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        Optional<CurrencyEntity> baseCurrency = currencyDaoImpl.findByCode(exchangeRatesRequestDto.getBaseCurrency());
        Optional<CurrencyEntity> targetCurrency = currencyDaoImpl.findByCode(exchangeRatesRequestDto.getTargetCurrency());

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Base or target currencies not found");
        }
        return new ExchangeRatesEntity(baseCurrency.get(), targetCurrency.get(), exchangeRatesRequestDto.getRate());
    }
}