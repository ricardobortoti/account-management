package com.bortoti.accountmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AccountTransfer {
    @Id
    @JsonIgnore
    private UUID id;
    @ApiModelProperty(value = "fromAccount", name = "fromAccount", dataType = "Integer", example = "1")
    private Integer fromAccount;
    @ApiModelProperty(value = "toAccount", name = "toAccount", dataType = "Integer", example = "2")
    private Integer toAccount;
    @ApiModelProperty(value = "amount", name = "amount", dataType = "Double", example = "500.00")
    private Double amount;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createdAt", name = "createdAt", hidden = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "status", name = "Transfer Status", example = "SUCCESS")
    private AccountTransferStatusEnum status;
}
