package org.jala.university.infrastructure.mock;

import org.jala.university.application.service.EmailService;

import java.util.logging.Logger;

/**
 * Mock implementation of the EmailService for development and testing.
 */
public class MockEmailServiceImpl implements EmailService {

    private static final Logger LOGGER = Logger.getLogger(MockEmailServiceImpl.class.getName());

    @Override
    public boolean sendEmail(String to, String subject, String body) {
        LOGGER.info("=== MOCK EMAIL ===");
        LOGGER.info("To: " + to);
        LOGGER.info("Subject: " + subject);
        LOGGER.info("Body: " + body);
        LOGGER.info("=================");
        return true;
    }
}
