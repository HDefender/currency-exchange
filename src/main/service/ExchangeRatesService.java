package main.service;

import main.dao.CurrencyDao;
import main.dao.ExchangeRatesDao;
import main.dto.request.ExchangeRatesRequestDto;
import main.dto.response.ExchangeRatesResponseDto;
import main.entity.CurrencyEntity;
import main.entity.ExchangeRatesEntity;
import main.exception.DataNotFoundException;
import main.exception.InternalErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRatesService {

    private final ExchangeRatesDao exchangeRatesDao = ExchangeRatesDao.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public List<ExchangeRatesResponseDto> findAll() {
        List <ExchangeRatesEntity> exchangeRatesEntityList = exchangeRatesDao.findAll();
        List <ExchangeRatesResponseDto> exchangeRatesResponseDtoList = new ArrayList<>();

        if (exchangeRatesEntityList.isEmpty()) {
            throw new DataNotFoundException("Exchange rates don't found");
        } else {
            for (ExchangeRatesEntity exchangeRatesEntity : exchangeRatesEntityList) {
                exchangeRatesResponseDtoList.add(convertToDto(exchangeRatesEntity));
            }
            return exchangeRatesResponseDtoList;
        }
    }

    public ExchangeRatesResponseDto findByCodes(String firstCode, String secondCode) {
        Optional<ExchangeRatesEntity> exchangeRatesEntity = exchangeRatesDao.findByCode(firstCode, secondCode);
        if(exchangeRatesEntity.isPresent()) {
            ExchangeRatesResponseDto exchangeRatesResponseDto = convertToDto(exchangeRatesEntity.get());
            return exchangeRatesResponseDto;
        } else {
            throw new DataNotFoundException("Exchange rate for these codes don't found");
        }
    }

    public ExchangeRatesResponseDto post(ExchangeRatesRequestDto exchangeRatesRequestDto){

        ExchangeRatesEntity exchangeRatesEntity = checkDto(exchangeRatesRequestDto);
        Optional<ExchangeRatesEntity> addedExchangeRate = exchangeRatesDao.create(exchangeRatesEntity);

        if(addedExchangeRate.isPresent()) {
            return convertToDto(addedExchangeRate.get());
        } else {
            throw new InternalErrorException("Internal error");
        }
    }

    public ExchangeRatesResponseDto update(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        ExchangeRatesEntity exchangeRatesEntity = checkDto(exchangeRatesRequestDto);

        if(findByCodes(exchangeRatesRequestDto.getBaseCurrency(),
                exchangeRatesRequestDto.getTargetCurrency()) != null){
            exchangeRatesDao.update(exchangeRatesEntity);
        } else {
            throw new DataNotFoundException("Exchange rate for these codes don't found");
        }
        return findByCodes(exchangeRatesRequestDto.getBaseCurrency(),
                exchangeRatesRequestDto.getTargetCurrency());
    }

    private ExchangeRatesResponseDto convertToDto (ExchangeRatesEntity exchangeRatesEntity) {
        return new ExchangeRatesResponseDto(
                exchangeRatesEntity.getId(),
                exchangeRatesEntity.getBaseCurrency(),
                exchangeRatesEntity.getTargetCurrency(),
                exchangeRatesEntity.getRate()
        );
    }

    private ExchangeRatesEntity checkDto (ExchangeRatesRequestDto exchangeRatesRequestDto) {
        Optional<CurrencyEntity> baseCurrency = currencyDao.findByCode(exchangeRatesRequestDto.getBaseCurrency());
        Optional<CurrencyEntity> targetCurrency = currencyDao.findByCode(exchangeRatesRequestDto.getTargetCurrency());

        if(baseCurrency.isEmpty() || targetCurrency.isEmpty()){
            throw new DataNotFoundException("Base or target currencies not found");
        }
        return new ExchangeRatesEntity(baseCurrency.get(),targetCurrency.get(),exchangeRatesRequestDto.getRate());

    }

}
