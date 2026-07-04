package com.dt.student.register.model;


import com.dt.student.register.model.base.BaseModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction extends BaseModel {

    private Long id;                 // Primary key
    private String tranId;        // Linked to StudentDetails / invoice
    private String invoiceNo;        // Linked to StudentDetails / invoice
    private Long studentId;          // Student reference
    private BigDecimal total;        // Total amount of invoice
    private BigDecimal deposit;      // Amount already paid
    private BigDecimal balanceDue;   // Remaining balance
    private String status;           // e.g., PENDING, PAID, PARTIAL
    private LocalDateTime postingDate; // When transaction was created
    private String remark;           // Optional note (e.g., "Pending payment")

}
