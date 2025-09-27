package com.eventbook.paymentservice.service;

import com.eventbook.paymentservice.dto.OrderRequest;
import com.eventbook.paymentservice.dto.PaymentVerificationRequest;
import com.eventbook.paymentservice.model.Payment;
import com.eventbook.paymentservice.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RazorpayClient razorpayClient;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) throws RazorpayException {
        JSONObject orderRequestJson = new JSONObject();
        orderRequestJson.put("amount", orderRequest.getAmount().multiply(new java.math.BigDecimal(100))); // amount in the smallest currency unit
        orderRequestJson.put("currency", orderRequest.getCurrency());
        orderRequestJson.put("receipt", "receipt_order_" + orderRequest.getBookingId());

        Order order = razorpayClient.orders.create(orderRequestJson);

        // Save payment record to our database
        Payment payment = new Payment();
        payment.setBookingId(orderRequest.getBookingId());
        payment.setAmount(orderRequest.getAmount());
        payment.setStatus("CREATED");
        payment.setTransactionId(order.get("id")); // This is the Razorpay Order ID
        payment.setPaymentTime(LocalDateTime.now());
        paymentRepository.save(payment);

        return order;
    }

    @Transactional
    public boolean verifyPayment(PaymentVerificationRequest verificationRequest) {
        try {
            String attributes = verificationRequest.getRazorpayOrderId() + "|" + verificationRequest.getRazorpayPaymentId();
            boolean isValid = Utils.verifyPaymentSignature(attributes, verificationRequest.getRazorpaySignature(), razorpayKeySecret);

            if (isValid) {
                Payment payment = paymentRepository.findByTransactionId(verificationRequest.getRazorpayOrderId())
                        .orElseThrow(() -> new RuntimeException("Payment record not found for orderId: " + verificationRequest.getRazorpayOrderId()));

                payment.setStatus("SUCCESS");
                // In a real app, you would now call the booking service to confirm the booking.
                paymentRepository.save(payment);
                return true;
            }
        } catch (RazorpayException e) {
            // Log error
            return false;
        }
        return false;
    }
}