package service;

import dao.CurrencyDaoImpl;
import dto.request.CurrencyRequestDto;
import dto.response.CurrencyResponseDto;
import entity.CurrencyEntity;
import exception.DataNotFoundException;
import exception.InternalErrorException;
import util.ValidationUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private final CurrencyDaoImpl currencyDaoImpl;

    public CurrencyService() {
        this(CurrencyDaoImpl.getInstance());
    }

    public CurrencyService(CurrencyDaoImpl currencyDaoImpl) {
        this.currencyDaoImpl = currencyDaoImpl;
    }

    public CurrencyResponseDto create(CurrencyRequestDto currencyRequestDto) {
        CurrencyEntity entity = convertToEntity(currencyRequestDto);
        Optional<CurrencyEntity> addedEntity = currencyDaoImpl.create(entity);

        if (addedEntity.isEmpty()) {
            throw new InternalErrorException("Failed to create currency");
        }
        return convertToDto(addedEntity.get());
    }

    public CurrencyResponseDto findByCode(String code) {
        Optional<CurrencyEntity> currencyEntity = currencyDaoImpl.findByCode(code);

        if (currencyEntity.isEmpty()) {
            throw new DataNotFoundException("Currency does not found:" + code);
        }
        return convertToDto(currencyEntity.get());
    }

    public List<CurrencyResponseDto> findAll() {
        List<CurrencyResponseDto> responseDtoList = new ArrayList<>();

        for (CurrencyEntity currencyEntity : currencyDaoImpl.findAll()) {
            responseDtoList.add(convertToDto(currencyEntity));
        }
        return responseDtoList;
    }

    private CurrencyEntity convertToEntity(CurrencyRequestDto currencyRequestDto) {
        ValidationUtil.validateInput(currencyRequestDto.getCode());
        ValidationUtil.validateInput(currencyRequestDto.getSign());
        ValidationUtil.validateInput(currencyRequestDto.getFullName());

        return new CurrencyEntity(currencyRequestDto.getCode(), currencyRequestDto.getFullName(), currencyRequestDto.getSign());

    }

    private CurrencyResponseDto convertToDto(CurrencyEntity entity) {
        return new CurrencyResponseDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getSign()
        );
    }
}
