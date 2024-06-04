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
    SUCCESS("GLB-SUC-000", HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    BAD_REQUEST("GLB-ERR-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    WRONG_STATUS_CODE("GLB-ERR-004", HttpStatus.NOT_FOUND, "존재하지 않은 상태코드입니다."),
    INVALID_PARAMETERS("GLB-ERR-005", HttpStatus.BAD_REQUEST, "입력값이 잘못되었습니다."),
    UNAUTHORIZED_CLIENT("AUT-ERR-002", HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다"),
    EXPIRED_ACCESS_TOKEN("AUT-ERR-003", HttpStatus.UNAUTHORIZED, "만료된 액세스 토큰입니다"),
    EXPIRED_REFRESH_TOKEN("AUT-ERR-004", HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다"),
    EMPTY_ACCESS_TOKEN("AUT-ERR-005", HttpStatus.BAD_REQUEST, "액세스 토큰이 비어있습니다"),
    BAD_ACCESS_TOKEN("AUT-ERR-006", HttpStatus.BAD_REQUEST, "액세스 토큰이 유효하지 않습니다"),

    /** 유저 관련 Response **/
    INVALID_USER("GLB-ERR-006", HttpStatus.UNAUTHORIZED, "존재하지 않는 유저입니다."),
    INVALID_BLOG("GLB-ERR-007", HttpStatus.UNAUTHORIZED, "존재하지 않는 블로그입니다."),
    INVALID_BLOG_USER("GLB-ERR-012", HttpStatus.UNAUTHORIZED, "블로그 주인이 존재하지 않습니다."),

    /** 글 관련 Response **/
    INVALID_POST("GLB-ERR-008", HttpStatus.NOT_FOUND, "존재하지 않는 글입니다."),
    INVALID_POST_LIKE("GLB-ERR-009", HttpStatus.NOT_FOUND, "존재하지 않는 글 좋아요입니다."),
    INVALID_DATE("GLB-ERR-012", HttpStatus.BAD_REQUEST, "특정 날짜는 오늘보다 이전이어야 합니다."),

    /** 댓글 관련 Response **/
    INVALID_COMMENT("GLB-ERR-010", HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    INVALID_REPLY("GLB-ERR-011", HttpStatus.NOT_FOUND, "존재하지 않는 답글입니다.");


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
