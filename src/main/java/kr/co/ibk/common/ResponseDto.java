package kr.co.ibk.common;

import kr.co.ibk.domain.enums.ResultCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseDto<T> {
    private final String code;
    private final String message;
    private final T data;
    private final Object errorList;
    private boolean hasNextPage;

    public ResponseDto(ResultCodeType resultCodeType, String message, T data) {
        this(resultCodeType, message, data, null);
    }

    public ResponseDto(ResultCodeType resultCodeType, String message, T data, Object errorList) {
        this.code = resultCodeType.code();
        this.message = message;
        this.data = data;
        this.errorList = errorList;
    }
}
