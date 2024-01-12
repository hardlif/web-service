package org.pan.exception.handlers;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.pan.exception.enums.PrivateHttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = FeignException.class)
    @ResponseBody
    public ResponseEntity<ApiError> exceptionHandler(FeignException e){
        if (e.status()== PrivateHttpStatus.USERNAME_PASSWORD_WRONG.getStatus()){
            return buildResponseEntity(ApiError.error(PrivateHttpStatus.USERNAME_PASSWORD_WRONG.getStatus()
                    ,PrivateHttpStatus.USERNAME_PASSWORD_WRONG.getMessage()));
        }
        if (e.status()== HttpStatus.UNAUTHORIZED.value()){
            return buildResponseEntity(ApiError.error(HttpStatus.UNAUTHORIZED.value()
                    ,HttpStatus.UNAUTHORIZED.getReasonPhrase()));
        }
        return buildResponseEntity(ApiError.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"远程接口调用异常，请联系管理员"));
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleException(Throwable e) {
        // 打印堆栈信息
        log.error(e.getMessage());
        return buildResponseEntity(ApiError.error(e.getMessage()));
    }

    /**
     * 统一返回
     */
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }
}
