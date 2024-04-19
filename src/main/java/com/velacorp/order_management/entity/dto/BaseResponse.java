package com.velacorp.order_management.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResponse {
  String responseCode;
  String message;
  Object data;

}
