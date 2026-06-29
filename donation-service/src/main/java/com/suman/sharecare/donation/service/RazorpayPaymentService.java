package com.suman.sharecare.donation.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.suman.sharecare.donation.config.RazorpayConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RazorpayPaymentService {
    private final RazorpayClient razorpayClient;
    private final RazorpayConfig razorpayConfig;

    public Order createOrder(BigDecimal amount, String receipt) throws RazorpayException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
        jsonObject.put("currency", "INR");
        jsonObject.put("receipt", receipt);
        return razorpayClient.orders.create(jsonObject);
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String paymentSignature) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", paymentSignature);

        return Utils.verifyPaymentSignature(options, razorpayConfig.getKeySecret());
    }
}
