package com.bortoti.accountmanagement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class AccountTransferRequest {
    @ApiModelProperty(value = "toAccount", name = "toAccount", dataType = "Integer", example = "2")
    @NotNull(message = "Destiny Account cannot be Null")
    private Integer toAccount;
    @ApiModelProperty(value = "amount", name = "amount", dataType = "Double", example = "500.00")
    @NotNull(message = "Amount cannot be Null")
    @Positive(message = "Amount Must Be Positive")
    private BigDecimal amount;
}
