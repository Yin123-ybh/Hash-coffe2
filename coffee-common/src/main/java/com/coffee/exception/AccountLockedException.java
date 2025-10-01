package com.coffee.exception;

public class AccountLockedException extends BaseException {
    public AccountLockedException() {
    }

    public AccountLockedException(String msg) {
        super(msg);
    }
}
