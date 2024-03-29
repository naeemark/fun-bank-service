package com.naeemark.fbs.models.responses;

import com.naeemark.fbs.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Naeem <naeemark@gmail.com>.
 * <p>
 * Created on: 2022-12-11
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountResponse {

    private int id;
    private int balance;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
    }
}
