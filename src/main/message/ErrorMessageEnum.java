package main.message;

public enum ErrorMessageEnum {
    SUCCESS("Успешно"),
    DATABASE_NOT_AVAILABLE("База данных недоступна"),
    NECESSARY_FIELD_NOT_FOUND("Отсутствует нужное поле формы"),

    CURRENCY_NOT_FOUND("Валюта не найдена"),
    CURRENCY_CODE_ERROR("Код валюты отсутствует в адресе"),
    CURRENCY_ALREADY_EXISTS("Валюта с таким кодом уже существует"),

    EXCHANGE_PAIR_NOT_FOUND("Обменный курс для пары не найден"),
    EXCHANGE_PAIR_ADDRESS_ERROR("Коды валют пары отсутствуют в адресе"),
    EXCHANGE_PAIR_ALREADY_EXISTS("Валютная пара с таким кодом уже существует"),
    EXCHANGE_PAIR_ONE_OR_BOTH_NOT_EXISTS("Одна (или обе) валюта из валютной пары не существует в БД"),
    EXCHANGE_PAIR_NOT_EXIST("Валютная пара отсутствует в базе данных ");

    private final String message;
    ErrorMessageEnum(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }








}
