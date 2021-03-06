package com.bortoti.accountmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransfer {
    @Id
    private UUID id;
    private Integer from;
    private Integer to;
    private Double amount;
    private LocalDateTime date;
    private Boolean success;
}
