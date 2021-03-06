package com.gabriellorandi.paymentprocessing.transaction.domain;

import com.gabriellorandi.paymentprocessing.account.domain.Account;
import com.gabriellorandi.paymentprocessing.account.repository.AccountRepository;
import com.gabriellorandi.paymentprocessing.operationtype.domain.OperationType;
import com.gabriellorandi.paymentprocessing.operationtype.service.OperationTypeCacheService;
import com.gabriellorandi.paymentprocessing.transaction.application.dto.CreateTransactionRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", referencedColumnName = "id")
    private OperationType operationType;

    @Column(name = "amount")
    private BigDecimal amount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "event_date")
    private ZonedDateTime eventDate;

    public static Transaction from(CreateTransactionRequest createTransactionRequest, AccountRepository accountRepository,
                                   OperationTypeCacheService operationTypeCacheService) {
        return new Transaction(
                null,
                accountRepository.findById(createTransactionRequest.getAccountId()).orElse(null),
                operationTypeCacheService.findById(createTransactionRequest.getOperationTypeId()),
                createTransactionRequest.getAmount(),
                ZonedDateTime.now()
        );
    }
}
