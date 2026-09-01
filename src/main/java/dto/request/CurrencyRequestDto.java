package dto.request;

import dto.BaseDto;

public class CurrencyRequestDto extends BaseDto {
    private final String code;
    private final String name;
    private final String sign;

    public CurrencyRequestDto(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return name;
    }

    public String getSign() {
        return sign;
    }
}
