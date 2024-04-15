package com.example.lucky7postservice.utils.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "data"})
// BaseResponse
public class BaseResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "응답 코드", example = "GLB-ERR-007")
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "응답 메세지", example = "요청에 성공하였습니다.")
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // Request success
    public BaseResponse(T data) {
        this.data = data;
    }

    // Request Fail
    public BaseResponse(BaseResponseStatus status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }

    public BaseResponse(BaseResponseStatus invalidParameters, String errorMessage) {
        this.code = invalidParameters.getCode();
        this.message = errorMessage;
    }
}

