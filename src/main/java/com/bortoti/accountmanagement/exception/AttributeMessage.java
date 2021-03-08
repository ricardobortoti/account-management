package com.bortoti.accountmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeMessage implements Serializable {
  private static final long serialVersionUID = 1L;

  private String attribute;
  private String message;

}