package com.example.lucky7postservice.utils.openfeign;

import com.example.lucky7postservice.utils.config.BaseException;
import com.example.lucky7postservice.utils.config.BaseResponseStatus;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 403) {
            return new BaseException(BaseResponseStatus.INVALID_USER_FEIGN);
        }
        return new Exception(response.reason());
    }
}
