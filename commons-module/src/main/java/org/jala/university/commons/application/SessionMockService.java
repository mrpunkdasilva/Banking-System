package org.jala.university.commons.application;

import org.jala.university.commons.application.service.SessionService;
import org.jala.university.commons.domain.Role;

public final class SessionMockService implements SessionService {

    /*
    * This is the default account number for the current session
    * */
    private static final String STR_DEFAULT_ACCOUNT_NUMBER = "123456";
    /*
    * This is the default role for the current session
    * */
    private static final Role DEFAUTL_ROLE = Role.USER;
    private static SessionService instance;

    public static class InstanceHolder {
        private static final SessionService INSTANCE = new SessionMockService();
    }

    public static SessionService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private final String accountId;
    private final Role role;

    private SessionMockService() {
        this.accountId = STR_DEFAULT_ACCOUNT_NUMBER;
        this.role = DEFAUTL_ROLE;
    }

    @Override
    public String getAccountNumber() {
        return this.accountId;
    }

    @Override
    public Role getRole() {
        return this.role;
        }

}
