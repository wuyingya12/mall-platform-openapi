package com.car.mall.openapi.exceptions;

import com.car.mall.openapi.base.ResponseEntity;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理.
 * @author dongliu
 */
@ControllerAdvice
public class GlobalException {
    private static Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity processIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.error(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity processNullPointerException(NullPointerException e){
        return ResponseEntity.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = InnerException.class)
    public ResponseEntity processInnerException(InnerException e){
        logger.error(e.getMessageData(),e.getException());
        return ResponseEntity.error(e.getMessageData());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity processException(Exception e){
        logger.error("程序发生未知异常,Content:", e );
        return ResponseEntity.error("程序发生未知异常");
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public Object unauth()  {
        return ResponseEntity.unauth("无操作权限");
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public Object auth()  {
        return ResponseEntity.sessionOver();
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity processException(MissingServletRequestParameterException e){
        logger.error("参数确实异常"+e.getMessage());
        return ResponseEntity.error("请求参数缺失，请检查");
    }
}
