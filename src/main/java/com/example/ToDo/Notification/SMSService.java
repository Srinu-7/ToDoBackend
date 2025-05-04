package com.example.ToDo.Notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;


@Service
public class SMSService {
    private static final Dotenv dotenv = Dotenv.load();
    public static final String ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
    public static final String AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
    public static final String FROM_NUMBER = dotenv.get("FROM_NUMBER");

    public SMSService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String to, String messageBody) {
        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(FROM_NUMBER),
                        messageBody)
                .create();
        System.out.println("SMS sent: " + message.getSid());
    }
}
