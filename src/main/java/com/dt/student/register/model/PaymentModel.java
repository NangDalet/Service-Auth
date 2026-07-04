package com.dt.student.register.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)

@Data
public class PaymentModel {

    private String reqTime;         // req_time
    private String merchantId;      // merchant_id
    private String tranId;          // tran_id
    private Double amount;          // amount
    private String currency;        // currency
    private String paymentOption;   // payment_option
    private String returnUrl;       // return_url / callback URL
}
