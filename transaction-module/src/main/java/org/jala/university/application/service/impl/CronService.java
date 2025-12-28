package org.jala.university.application.service.impl;

import org.jala.university.application.service.TransactionsService;

import java.time.LocalDate;
import java.util.TimerTask;

public class CronService extends TimerTask {
    private final TransactionsService transactionsService;

    public CronService() {
        this.transactionsService = new TransactionsServiceImpl();
    }

    @Override
    public void run() {
        LocalDate today = LocalDate.now();
        this.transactionsService.findAll().stream()
                .filter(transaction -> transaction.getTransactionSchedule().isEqual(today))
                .forEach(transactionsService::save);


    }
}
