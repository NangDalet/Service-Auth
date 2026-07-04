package com.dt.student.register.service;

import com.dt.student.register.model.dto.response.aba.AbaCheckTransactionResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AbaCallbackService {

    private static final Logger logger = LoggerFactory.getLogger(AbaCallbackService.class);
    private static final ZoneId CAMBODIA_TZ = ZoneId.of("Asia/Phnom_Penh");

    private final RestTemplate restTemplate = new RestTemplate();

    private final String apiUrl = "https://checkout.payway.com.kh/api/payment-gateway/v1/check-transaction-2";

    public ResponseEntity<Map<String, Object>> processCallback(
            String stationId,
            String connectorId,
            String idTag,
            double amount,
            String tranId,
            String screenId
    ) {
        try {
            if (stationId == null || connectorId == null || idTag == null || tranId == null || screenId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Missing required parameter"));
            }

            String merchantId = "EASYLAUNDRY";
            String publicKey = "9338e835-20fa-4bcb-b098-e45af44a3d4e";

            String reqTime = ZonedDateTime.now(CAMBODIA_TZ).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String hash = generateHmacSha512(reqTime + merchantId + tranId, publicKey);
            logger.info("Generated hash for ABA check: {}", hash);

            Map<String, Object> payload = new HashMap<>();
            payload.put("req_time", reqTime);
            payload.put("merchant_id", merchantId);
            payload.put("tran_id", tranId);
            payload.put("hash", hash);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<AbaCheckTransactionResponse> response = restTemplate
                    .postForEntity(apiUrl, request, AbaCheckTransactionResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                logger.error("ABA API call failed: {}", response.getStatusCode());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "ABA API call failed"));
            }

            AbaCheckTransactionResponse respBody = response.getBody();

            if ("00".equals(respBody.getStatus().getCode())) {
                logger.info("Payment approved for tranId {}", tranId);

                return ResponseEntity.ok(Map.of(
                        "status", "Transaction approved",
                        "tran_id", tranId,
                        "qr_string", respBody.getQrString(),
                        "abapay_deeplink", respBody.getAbapayDeeplink(),
                        "checkout_qr_url", respBody.getCheckoutQrUrl()
                ));
            } else {
                logger.warn("Payment not approved for tranId {}, code {}", tranId, respBody.getStatus().getCode());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Payment not approved", "code", respBody.getStatus().getCode()));
            }

        } catch (Exception e) {
            logger.error("Error processing ABA callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String generateHmacSha512(String data, String key) throws Exception {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        byte[] hashBytes = sha512Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(hashBytes);
    }
}