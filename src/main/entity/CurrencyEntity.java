package main.entity;

public class CurrencyEntity {
    private int id;
    private String code;
    private String fullName;
    private String Sign;

    public CurrencyEntity(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        Sign = sign;
    }

    public CurrencyEntity(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        Sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    @Override
    public String toString() {
        return "CurrencyEntity{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", fullName='" + fullName + '\'' +
               ", Sign='" + Sign + '\'' +
               '}';
    }
}
