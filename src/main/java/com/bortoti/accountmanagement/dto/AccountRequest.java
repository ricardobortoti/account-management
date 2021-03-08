package com.bortoti.accountmanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AccountRequest {
    @ApiModelProperty(value = "name", name = "name", dataType = "String", example = "Joao")
    @NotNull
    private String name;
    @Column(unique=true)
    @ApiModelProperty(value = "accountNumber", name = "accountNumber", dataType = "Integer", example = "1")
    @NotNull
    private Integer accountNumber;
    @ApiModelProperty(value = "accountBalance", name = "accountBalance", dataType = "Double", example = "250000.00")
    @NotNull
    private BigDecimal accountBalance;
}
