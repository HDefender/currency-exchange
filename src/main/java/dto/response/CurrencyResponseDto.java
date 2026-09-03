package dto.response;

import dto.BaseDto;

public class CurrencyResponseDto extends BaseDto {
    private int id;
    private String code;
    private String name;
    private String sign;

    public CurrencyResponseDto(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

}
