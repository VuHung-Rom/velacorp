package com.velacorp.order_management.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CommonException extends RuntimeException {
  private String errorCode;
  private String errorMessage;
  private HttpStatus httpCode;



  public CommonException( Throwable ex, String errorCode, String errorMessage) {
    super(ex);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
  public CommonException( Throwable ex, String errorCode, String errorMessage, HttpStatus httpCode) {
    super(ex);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.httpCode = httpCode;
  }
  public CommonException( String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
  public CommonException( String errorCode, String errorMessage, HttpStatus httpCode) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.httpCode = httpCode;
  }

}
