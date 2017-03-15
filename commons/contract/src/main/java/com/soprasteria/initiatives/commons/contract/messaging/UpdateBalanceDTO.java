package com.soprasteria.initiatives.commons.contract.messaging;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * Bean containing infos to update account balance
 *
 * @author jntakpe
 */
public class UpdateBalanceDTO {

    private String accNumber;

    private BigDecimal amount;

    public UpdateBalanceDTO() {
    }

    public UpdateBalanceDTO(String accNumber, BigDecimal amount) {
        this.accNumber = accNumber;
        this.amount = amount;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public UpdateBalanceDTO setAccNumber(String accNumber) {
        this.accNumber = accNumber;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UpdateBalanceDTO setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accNumber", accNumber)
                .append("amount", amount)
                .toString();
    }
}
