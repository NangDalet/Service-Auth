package com.dt.student.register.model.dto.response.branch;

import com.dt.student.register.model.base.BaseModel;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.users.UserList;
import lombok.Data;

import java.util.List;

@Data
public class BranchResponse extends BaseModel {

    private Long id;
    private Long companyId;
    private String baseCurrencyId;
    private String baseCurrencyName;
    private String baseCurrencySymbol;
    private String branchName;
    private String branchNameKhmer;
    private String telephone;
    private String type;
    private String faxNumber;
    private String emailAddress;
    private String workStart;
    private String workEnd;
    private String longitude;
    private String latitude;
    private String address;
    private String addressKhmer;
    private List<UserList> userList;

}
