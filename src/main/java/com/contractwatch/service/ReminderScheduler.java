package com.contractwatch.service;

import com.contractwatch.entity.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderScheduler
{
    private static final Logger log =
            LoggerFactory.getLogger(
                    ReminderScheduler.class
            );

    private final ContractService service;
    private final ObjectProvider<JavaMailSender>
            mailSenderProvider;
    private final Environment environment;

    public ReminderScheduler(
            ContractService service,
            ObjectProvider<JavaMailSender> mailSenderProvider,
            Environment environment)
    {
        this.service = service;
        this.mailSenderProvider = mailSenderProvider;
        this.environment = environment;
    }

    // Runs daily at 9:00 AM using the server's configured timezone.
    @Scheduled(cron = "0 0 9 * * *")
    public void sendRenewalReminders()
    {
        service.refreshRenewalFlags();

        JavaMailSender sender =
                mailSenderProvider.getIfAvailable();

        List<Contract> expiring =
                service.renewalReviewContracts();

        String smtpHost =
                environment.getProperty(
                        "spring.mail.host",
                        ""
                );

        if (sender == null || smtpHost.isBlank())
        {
            if (!expiring.isEmpty())
            {
                log.info(
                        "{} active contract(s) are in their renewal notice window. " +
                        "Email is not configured; reminders are logged only.",
                        expiring.size()
                );
            }

            return;
        }

        for (Contract contract : expiring)
        {
            String email =
                    contract.getContactEmail();

            if (email == null || email.isBlank())
            {
                email = contract.getVendor()
                        .getContactEmail();
            }

            if (email == null || email.isBlank())
            {
                log.info(
                        "Reminder skipped for contract id {} " +
                        "because contact email is blank",
                        contract.getId()
                );

                continue;
            }

            try
            {
                SimpleMailMessage message =
                        new SimpleMailMessage();

                message.setTo(email);

                message.setSubject(
                        "Contract renewal reminder: " +
                        contract.getVendor().getName()
                );

                message.setText(
                        "Reminder: the contract for " +
                        contract.getServiceName() +
                        " with " +
                        contract.getVendor().getName() +
                        " has an end date of " +
                        contract.getEndDate() +
                        ". The renewal notice period is " +
                        contract.getRenewalNoticePeriodDays() +
                        " day(s). Please review the contract."
                );

                sender.send(message);

                log.info(
                        "Sent renewal reminder for contract id {}",
                        contract.getId()
                );
            }
            catch (Exception ex)
            {
                log.error(
                        "Unable to send reminder for contract id {}: {}",
                        contract.getId(),
                        ex.getMessage()
                );
            }
        }
    }
}
