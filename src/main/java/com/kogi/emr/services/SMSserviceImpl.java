package com.kogi.emr.services;


import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;
import com.kogi.emr.models.EmailDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SMSserviceImpl implements SMSService {



    public String sendSMSMessage(EmailDetails details){

        String username = "erickogi";
        String apiKey = "6ba98aaac1fd35d540adb83320bcbad85d9440171af94abb43f1cf795abd90e8";
        AfricasTalking.initialize(username, apiKey);

        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

        try {
            List<Recipient> response = sms.send(details.getMsgBody(), details.getRecipient(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "Email sent successfully";

    }
}
