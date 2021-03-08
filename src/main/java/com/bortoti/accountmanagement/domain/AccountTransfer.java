package com.bortoti.accountmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.math.BigDecimal;
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
    private UUID id;
    private Integer fromAccount;
    private Integer toAccount;
    private BigDecimal amount;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createdAt", name = "createdAt", hidden = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "status", name = "Transfer Status", example = "SUCCESS")
    private AccountTransferStatusEnum status;
}
