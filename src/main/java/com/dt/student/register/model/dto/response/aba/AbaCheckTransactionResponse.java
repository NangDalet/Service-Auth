package com.dt.student.register.model.dto.response.aba;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AbaCheckTransactionResponse {

    @JsonProperty("status")
    private Status status;

    @JsonProperty("qr_string")
    private String qrString;

    @JsonProperty("abapay_deeplink")
    private String abapayDeeplink;

    @JsonProperty("checkout_qr_url")
    private String checkoutQrUrl;

    @Data
    public static class Status {
        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;

        @JsonProperty("tran_id")
        private String tranId;
    }
}
