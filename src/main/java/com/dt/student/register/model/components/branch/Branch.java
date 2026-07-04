package com.dt.student.register.model.components.branch;

import lombok.Data;

import java.io.Serializable;

@Data
public class Branch implements Serializable {

    private Long id;
    private Long companyId;
    private Long currencyCenterId;
    private Long branchTypeId;
    private String name;
    private String nameOther;
    private String telephone;
    private String emailAddress;
    private String faxNumber;
    private String longitude;
    private String lat;
    private String address;
    private String addressOther;
    private String workStart;
    private String workEnd;
    private Long createdBy;
    private String created;
    private Long modifiedBy;
    private String modified;
}