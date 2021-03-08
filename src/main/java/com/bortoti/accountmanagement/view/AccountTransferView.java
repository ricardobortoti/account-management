package com.bortoti.accountmanagement.view;

import com.bortoti.accountmanagement.domain.AccountTransferStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountTransferView {
    UUID getId();
    String getFromAccount();
    String getToAccount();
    BigDecimal getAmount();
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreatedAt();
    AccountTransferStatusEnum getStatus();
}
