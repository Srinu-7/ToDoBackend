package com.example.ToDo.Notification;

import com.example.ToDo.Model.Task;
import com.example.ToDo.Model.User;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    private final EmailService emailService;
    private final SMSService smsService;

    public NotificationService(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public void sendEmailNotification(Task task, User user, boolean isUpdated) {
        if (user.getEmail() == null) return;
        String action = isUpdated ? "updated" : "created"; // set the action based on the isUpdated parameter

        // Create the styled HTML message
        String message = String.format(
                "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                        "<div style='border: 1px solid #ccc; border-radius: 5px; padding: 20px; width: 400px; margin: auto; background-color: #ffffff; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
                        "<h2 style='color: #4CAF50;'>Task %s</h2>" +
                        "<p style='font-size: 16px;'><strong>Task:</strong> %s</p>" +
                        "<p style='font-size: 16px;'><strong>Operation:</strong> %s</p>" +
                        "<p style='font-size: 16px;'><strong>Urgency:</strong> %s</p>" +
                        "<p style='font-size: 16px;'><strong>Due Date:</strong> %s</p>" +
                        "<hr style='border: 1px solid #4CAF50;'>" +
                        "<p style='font-size: 14px; color: #777;'>Thank you for using our service!</p>" +
                        "</div>" +
                        "</body></html>",
                action, task.getTask(), task.getOperation(), task.getUrgency(), task.getDateTime()
        );

        String subject = "Task " + action.toUpperCase();
        emailService.sendNotification(user.getEmail(), subject, message);
    }

    public void sendSMSNotification(Task task, User user, boolean isUpdated) {
        if (user.getPhoneNumber() == null) return;
        String action = isUpdated ? "updated" : "created";

        // Create a plain text message instead of HTML
        String message = String.format(
                "Task %s\n" +
                        "Task: %s\n" +
                        "Operation: %s\n" +
                        "Urgency: %s\n" +
                        "Due Date: %s\n" +
                        "Thank you for using our service!",
                action, task.getTask(), task.getOperation(), task.getUrgency(), task.getDateTime()
        );

        try {
            smsService.sendSms("+91"+user.getPhoneNumber(), message);
        } catch (Exception e) {
            // Log the error or handle it appropriately
            log.error("Failed to send SMS to {}: {}", user.getPhoneNumber(), e.getMessage(), e);
        }
    }
}
