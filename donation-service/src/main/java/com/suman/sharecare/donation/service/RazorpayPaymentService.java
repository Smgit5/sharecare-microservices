package com.suman.sharecare.donation.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.suman.sharecare.donation.config.RazorpayConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentService {
    private final RazorpayClient razorpayClient;

    public Order createOrder(BigDecimal amount, String receipt) throws RazorpayException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
        jsonObject.put("currency", "INR");
        jsonObject.put("receipt", receipt);
        return razorpayClient.orders.create(jsonObject);
    }
}
