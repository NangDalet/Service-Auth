//package com.dt.student.register.service.serviceImpl;
//
//
//import com.dt.student.register.config.AbaConfig;
//import com.dt.student.register.happer.AbaHashUtil;
//import com.dt.student.register.mapper.primary.aba.AbaPayMapper;
//import com.dt.student.register.model.aba.ABA;
//import com.dt.student.register.model.dto.request.aba.ABARequest;
//import com.dt.student.register.service.AbaPayService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//
//public class AbaPayServiceImpl implements AbaPayService {
//
//    private AbaPayMapper abaPayMapper;
//    private AbaConfig abaConfig;
//    private RestTemplate restTemplate = new RestTemplate();
//
//    @Override
//    public Map<String, Object> addTransaction(ABARequest model) throws Exception {
//
//        // 1️⃣ Generate UTC timestamp
//        String reqTime = LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//
//        // 2️⃣ Set default values
//        if (model.getMerchantId() == null) model.setMerchantId(abaConfig.getMerchantId());
//        if (model.getReqTime() == null) model.setReqTime(reqTime);
//        if (model.getTranId() == null || model.getTranId().isEmpty()) model.setTranId(reqTime);
//        if (model.getCallbackUrl() == null)
//            model.setCallbackUrl(Base64.getEncoder().encodeToString(abaConfig.getReturnUrl().getBytes(StandardCharsets.UTF_8)));
//        if (model.getLifetime() == null) model.setLifetime(30L * 24L * 60L); // default 30 days in minutes
//
//        // 3️⃣ Map DTO -> Entity
//        ABA abaEntity = mapRequestToEntity(model);
//
//        // 4️⃣ Generate hash
//        String hash = AbaHashUtil.generateHash(abaEntity, abaConfig.getApiKey());
//        abaEntity.setHash(hash);
//
//        // 5️⃣ Save to DB via MyBatis
//        //abaPayMapper.insertABA(abaEntity);
//
//        // 6️⃣ Build payload for ABA API
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("req_time", abaEntity.getReqTime());
//        payload.put("merchant_id", abaEntity.getMerchantId());
//        payload.put("tran_id", abaEntity.getTranId());
//        payload.put("amount", abaEntity.getAmount().toString());
//        payload.put("payment_option", abaEntity.getPaymentOption());
//        payload.put("first_name", abaEntity.getFirstName());
//        payload.put("last_name", abaEntity.getLastName());
//        payload.put("email", abaEntity.getEmail());
//        payload.put("phone", abaEntity.getPhone());
//        payload.put("currency", abaEntity.getCurrency());
//        payload.put("callback_url", abaEntity.getCallbackUrl());
//        payload.put("return_deeplink", abaEntity.getReturnDeeplink());
//        payload.put("custom_fields", abaEntity.getCustomFields());
//        payload.put("return_params", abaEntity.getReturnParams());
//        payload.put("payout", abaEntity.getPayout());
//        payload.put("lifetime", abaEntity.getLifetime());
//        payload.put("qr_image_template", abaEntity.getQrImageTemplate());
//        payload.put("items", abaEntity.getItems());
//        payload.put("purchase_type", abaEntity.getPurchaseType());
//        payload.put("hash", abaEntity.getHash());
//
//        // 7️⃣ Log payload
//        System.out.println("Sending payload: " + new ObjectMapper().writeValueAsString(payload));
//
//        // 8️⃣ Send request to ABA API
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    abaConfig.getApiUrl(),
//                    HttpMethod.POST,
//                    entity,
//                    Map.class
//            );
//            return response.getBody();
//        } catch (HttpClientErrorException e) {
//            System.err.println("API Error Response: " + e.getResponseBodyAsString());
//            throw new Exception("API Error: " + e.getResponseBodyAsString(), e);
//        }
//    }
//
//    // Helper method: map ABARequest -> ABA entity
//    private ABA mapRequestToEntity(ABARequest request) {
//        ABA aba = new ABA();
//        aba.setTranId(request.getTranId());
//        aba.setMerchantId(request.getMerchantId());
//        aba.setReqTime(request.getReqTime());
//        aba.setAmount(request.getAmount());
//        aba.setCurrency(request.getCurrency());
//        aba.setPaymentOption(request.getPaymentOption());
//        aba.setFirstName(request.getFirstName());
//        aba.setLastName(request.getLastName());
//        aba.setEmail(request.getEmail());
//        aba.setPhone(request.getPhone());
//        aba.setCallbackUrl(request.getCallbackUrl());
//        aba.setReturnDeeplink(request.getReturnDeeplink());
//        aba.setCustomFields(request.getCustomFields());
//        aba.setReturnParams(request.getReturnParams());
//        aba.setPayout(request.getPayout());
//        aba.setLifetime(request.getLifetime());
//        aba.setQrImageTemplate(request.getQrImageTemplate());
//        aba.setItems(request.getItems());
//        aba.setPurchaseType(request.getPurchaseType());
//        aba.setHash(request.getHash());
//        aba.setCreatedAt(LocalDateTime.now());
//        aba.setUpdatedAt(LocalDateTime.now());
//        return aba;
//    }
//
//}
