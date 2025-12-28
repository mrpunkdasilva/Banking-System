package org.jala.university.application.service;

/**
 * Service for sending emails.
 */
public interface EmailService {

    /**
     * Sends an email.
     * @param to the recipient's email address
     * @param subject the email subject
     * @param body the email body
     * @return true if the email was sent successfully, false otherwise
     */
    boolean sendEmail(String to, String subject, String body);
}