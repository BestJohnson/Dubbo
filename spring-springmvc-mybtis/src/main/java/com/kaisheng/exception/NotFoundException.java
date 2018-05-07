package com.kaisheng.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "你要找的东西不存在")
public class NotFoundException extends RuntimeException {

}
