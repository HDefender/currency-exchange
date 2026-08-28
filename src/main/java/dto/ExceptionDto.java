package dto;

public class ExceptionDto extends BaseDto {
    private final String message;

    public ExceptionDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
