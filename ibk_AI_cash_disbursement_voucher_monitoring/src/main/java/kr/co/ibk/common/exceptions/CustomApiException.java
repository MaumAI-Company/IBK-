package kr.co.ibk.common.exceptions;

public class CustomApiException extends RuntimeException {

    public CustomApiException(String message) {
        super(message);
    }
}
