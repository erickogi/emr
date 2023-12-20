package com.kogi.emr.services;

import com.kogi.emr.models.EmailDetails;

public interface SMSService {

    /**
     * This method is used to send an email.
     * @param details
     */
    String sendSMSMessage (EmailDetails details);

}
