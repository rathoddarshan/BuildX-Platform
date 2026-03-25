package com.codingShuttle.projects.buildX.platform.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Value("${stripe.secret}")
    private String stripeSecreteKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeSecreteKey;
    }
}
