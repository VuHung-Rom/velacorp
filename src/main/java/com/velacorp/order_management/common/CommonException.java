package com.velacorp.order_management.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException {
  private String errorCode;
  private String errorMessage;

  public CommonException( Throwable ex, String errorCode, String errorMessage) {
    super(ex);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
  public CommonException( String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

}
