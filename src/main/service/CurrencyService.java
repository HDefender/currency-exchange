package main.service;

import main.dao.CurrencyDao;
import main.dto.request.CurrencyRequestDto;
import main.dto.response.CurrencyResponseDto;
import main.entity.CurrencyEntity;
import main.exception.DataNotFoundException;
import main.exception.IncorrectInputException;
import main.exception.InternalErrorException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public CurrencyResponseDto post(CurrencyRequestDto currencyRequestDto){
//        if (!currencyDao.findByCode(currencyRequestDto.getCode()).isEmpty()){
//            throw new AlreadyExistException("Currency already exist");
//        }

        CurrencyEntity entity = convertToEntity(currencyRequestDto);
        Optional<CurrencyEntity> addedEntity = currencyDao.create(entity);

        if(addedEntity.isPresent()){
           return new CurrencyResponseDto(entity.getId(),
                    entity.getCode(), entity.getFullName(), entity.getSign());
        } else {
            throw new InternalErrorException("Internal error");
        }
    }

    public CurrencyResponseDto findByCode(String code){
        Optional<CurrencyEntity> currencyEntity = currencyDao.findByCode(code);

        if(currencyEntity.isPresent()){
            return new CurrencyResponseDto(currencyEntity.get().getId(),
                    currencyEntity.get().getCode(),
                    currencyEntity.get().getFullName(),
                    currencyEntity.get().getSign());
        } else {
            throw new DataNotFoundException("Value does not found");
        }
    }

    public List<CurrencyResponseDto> findAll(){
        List<CurrencyResponseDto> responseDtoList = new ArrayList<>();
        List<CurrencyEntity> currencyEntities = currencyDao.findAll();

        if(currencyEntities.isEmpty()){
            throw new DataNotFoundException("Currencies don't found");
        }
        for (CurrencyEntity currencyEntity : currencyEntities) {
            responseDtoList.add(
                    new CurrencyResponseDto(currencyEntity.getId(),
                    currencyEntity.getCode(),
                    currencyEntity.getFullName(),
                    currencyEntity.getSign()));
        }
        return responseDtoList;
    }

    public CurrencyEntity convertToEntity(CurrencyRequestDto currencyRequestDto){
        if (currencyRequestDto == null ||
                currencyRequestDto.getCode().isEmpty() ||
                currencyRequestDto.getSign().isEmpty() ||
            currencyRequestDto.getFullName().isEmpty()){
            throw new IncorrectInputException("Incorrect input");
        } else{
            return new CurrencyEntity(currencyRequestDto.getCode(), currencyRequestDto.getFullName(), currencyRequestDto.getSign());
        }
    }

}
