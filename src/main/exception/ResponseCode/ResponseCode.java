package main.exception.ResponseCode;

public enum ResponseCode {

    SUCCESS(200),
    SUCCESS_CREATED(201),

    BAD_REQUEST(400),
    NOT_FOUND(404),
    ALREADY_EXISTS(409),

    INTERNAL_ERROR(500);

    private final int httpStatus;

    ResponseCode(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() { return httpStatus;
    }
}
