package com.bortoti.accountmanagement.repository;

import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.view.AccountTransferView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, UUID> {
    List<AccountTransferView> findByFromAccountOrToAccountOrderByCreatedAtDesc(Integer fromAccount, Integer toAccount);
}
