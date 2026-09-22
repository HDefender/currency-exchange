package service;

import dao.CurrencyDao;
import dao.ExchangeRatesDao;
import dto.request.ExchangeRateRequestDto;
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

    private final ExchangeRatesDao exchangeRatesDao;
    private final CurrencyDao currencyDao;

    public ExchangeRateService(ExchangeRatesDao exchangeRatesDao, CurrencyDao currencyDao) {
        this.exchangeRatesDao = exchangeRatesDao;
        this.currencyDao = currencyDao;
    }

    public List<ExchangeRateResponseDto> findAll() {
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRatesDao.findAll();
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
        Optional<ExchangeRateEntity> exchangeRatesEntity = exchangeRatesDao.findByCode(baseCode, targetCode);

        if (exchangeRatesEntity.isEmpty()) {
            throw new DataNotFoundException("Exchange rate for these codes not found");
        }
        return convertToDto(exchangeRatesEntity.get());
    }

    public ExchangeRateResponseDto create(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRateEntity exchangeRateEntity = convertToEntity(exchangeRateRequestDto);
        Optional<ExchangeRateEntity> addedExchangeRate = exchangeRatesDao.create(exchangeRateEntity);

        if (addedExchangeRate.isEmpty()) {
            throw new InternalErrorException("Internal error");
        }
        return convertToDto(addedExchangeRate.get());
    }

    public ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRateEntity exchangeRateEntity = convertToEntity(exchangeRateRequestDto);

        Optional<ExchangeRateEntity> result = exchangeRatesDao.update(exchangeRateEntity);
        if (result.isEmpty()) {
            throw new DataNotFoundException("Exchange rate not found for pair "
                    + exchangeRateRequestDto.baseCurrency() +
                    "/" + exchangeRateRequestDto.targetCurrency());
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

    private ExchangeRateEntity convertToEntity(ExchangeRateRequestDto exchangeRateRequestDto) {
        Optional<CurrencyEntity> baseCurrency = currencyDao.findByCode(exchangeRateRequestDto.baseCurrency());
        Optional<CurrencyEntity> targetCurrency = currencyDao.findByCode(exchangeRateRequestDto.targetCurrency());

        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            throw new DataNotFoundException("Base or target currencies not found");
        }
        return new ExchangeRateEntity(baseCurrency.get(), targetCurrency.get(), exchangeRateRequestDto.rate());
    }
}