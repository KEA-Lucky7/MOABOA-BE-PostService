package com.example.lucky7postservice.utils.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * Error code
 */
@Getter
public enum BaseResponseStatus {
    /** 기본 Response **/
    BAD_REQUEST("GLB-ERR-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    WRONG_STATUS_CODE("GLB-ERR-004", HttpStatus.NOT_FOUND, "존재하지 않은 상태코드입니다."),
    INVALID_PARAMETERS("GLB-ERR-005", HttpStatus.BAD_REQUEST, "입력값이 잘못되었습니다.");

    // BaseResponseStatus Mapping
    private final String code;
    private final HttpStatus status;
    private final String message;

    BaseResponseStatus(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public static BaseResponseStatus getBaseStatusByCode(String code) throws BaseException {
        for (BaseResponseStatus baseStatus: BaseResponseStatus.values()) {
            if (Objects.equals(baseStatus.code, code)) {
                return baseStatus;
            }
        }
        throw new BaseException(BaseResponseStatus.WRONG_STATUS_CODE);
    }
}
