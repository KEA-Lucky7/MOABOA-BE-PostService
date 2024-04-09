package com.example.lucky7postservice.utils.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseResponseStatus> BaseException(BaseException e) {
        BaseResponseStatus baseResponse = null;
        try {
            baseResponse = BaseResponseStatus.getBaseStatusByCode(e.getStatus().getCode());
        } catch (Exception error) {
            log.error(error.getMessage());
        } finally {
            log.error(e.getMessage());
        }
        return new BaseResponse<>(baseResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<String> BaseException(MethodArgumentNotValidException e) {
        // 에러 메시지 추출
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(errorMessage);

        // INVALID_PARAMETERS
        return new BaseResponse<>(BaseResponseStatus.INVALID_PARAMETERS, errorMessage);
    }

}
