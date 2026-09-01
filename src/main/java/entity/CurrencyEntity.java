package entity;

public class CurrencyEntity {
    private int id;
    private String code;
    private final String name;
    private final String sign;

    public CurrencyEntity(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public CurrencyEntity(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
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

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "CurrencyEntity{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", Sign='" + sign + '\'' +
               '}';
    }
}
