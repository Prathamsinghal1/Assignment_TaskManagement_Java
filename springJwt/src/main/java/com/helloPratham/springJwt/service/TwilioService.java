//package com.helloPratham.springJwt.service;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Call;
//import com.twilio.type.PhoneNumber;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.net.URI;
//
//@Service
//public class TwilioService {
//
//    @Value("${twilio.account-sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth-token}")
//    private String authToken;
//
//    @Value("${twilio.phone-number}")
//    private String twilioPhoneNumber;
//
//    @Value("${twilio.given-phone-number}")
//    private String givenPhoneNumber;
//
//    public TwilioService() {
//        // Initialize Twilio with credentials
//        Twilio.init(accountSid, authToken);
//    }
//
//    /**
//     * Makes a call to the specified phone number with a message.
//     *
//     * @param toPhoneNumber The recipient's phone number.
//     * @param message       The message to convey in the call.
//     * @param useGivenNumber Flag to indicate if the call should be made from the given phone number.
//     */
//    public void makeCall(String toPhoneNumber, String message, boolean useGivenNumber) {
//        try {
//            // Choose the from number based on the flag
//            String fromPhoneNumber = useGivenNumber ? givenPhoneNumber : twilioPhoneNumber;
//
//            // Create a TwiML Bin URL for voice message
//            URI twimlUrl = URI.create("http://twimlets.com/message?Message%5B0%5D=" + message);
//
//            Call call = Call.creator(
//                    new PhoneNumber(toPhoneNumber), // To phone number
//                    new PhoneNumber(fromPhoneNumber), // From phone number
//                    twimlUrl // URL for TwiML
//            ).create();
//
//            System.out.println("Call initiated with SID: " + call.getSid());
//        } catch (Exception e) {
//            System.err.println("Error while making a call: " + e.getMessage());
//        }
//    }
//}
