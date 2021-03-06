package com.bortoti.accountmanagement.repository;

import com.bortoti.accountmanagement.domain.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, UUID> {
    List<AccountTransfer> findByFromAccountOrderByCreatedAtDesc(Integer fromAccount);
}
