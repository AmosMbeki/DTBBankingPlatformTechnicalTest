package com.amos.nyagar.account_service.utils.testutils;

import com.amos.nyagar.account_service.entity.Account;
import com.amos.nyagar.account_service.entity.DTO.AccountDTO;

import java.util.UUID;

public class AccountTestBuilder {

    public static Account buildAccount() {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID().toString());
        account.setIban("GB29NWBK60161331926819");
        account.setBicSwift("NWBKGB2L");
        account.setCustomerId("cust-12345");
        return account;
    }

    public static AccountDTO buildAccountDTO() {
        Account account = buildAccount();
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setIban(account.getIban());
        dto.setBicSwift(account.getBicSwift());
        dto.setCustomerId(account.getCustomerId());
        return dto;
    }
}