package com.bortoti.accountmanagement.view;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface AccountTransferView {
    String getFromAccount();
    String getToAccount();
    Double getAmount();
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreatedAt();
}
