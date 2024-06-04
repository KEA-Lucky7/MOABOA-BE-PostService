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
        int status = response.status();
        if(status == 403) {
            return new BaseException(BaseResponseStatus.UNAUTHORIZED_CLIENT);
        } else if(status == 401) {
            return new BaseException(BaseResponseStatus.UNAUTHORIZED_CLIENT);
        } else if(status == 400) {
            return new BaseException(BaseResponseStatus.BAD_ACCESS_TOKEN);
        }

        return new Exception(response.reason());
    }
}
