package dto;

public class ExceptionDto extends BaseDto {
    private String message;

    public ExceptionDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
