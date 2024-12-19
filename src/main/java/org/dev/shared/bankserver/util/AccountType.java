package org.dev.shared.bankserver.util;

public enum AccountType {
    SAVINGS(1),
    FIXED(2),
    CHECKING(3),
    MINOR(4);

    private final int accountType;

    private AccountType(int accountType) {
        this.accountType = accountType;
    }

    public AccountType getAccountType(int accountType) {
        return switch (accountType) {
            case 1 -> AccountType.SAVINGS;
            case 2 -> AccountType.FIXED;
            case 3 -> AccountType.CHECKING;
            case 4 -> AccountType.MINOR;
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        };
    }
}
