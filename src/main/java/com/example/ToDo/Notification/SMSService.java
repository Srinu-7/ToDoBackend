package com.example.ToDo.Notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;


@Service
public class SMSService {
    public static final String ACCOUNT_SID = "ACbba46f0f09342858cf081791cb1f35e6"; // Replace with your Account SID
    public static final String AUTH_TOKEN = "30f0ac1589fbdcce4001dd2c1355e111"; // Replace with your Auth Token
    public static final String FROM_NUMBER = "+16203170716"; // Replace with your Twilio number

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
