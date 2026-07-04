package com.dt.student.register.model.base.filter;

import lombok.Data;

@Data
public class UserLogFilter extends Filter{

    private String dateFrom;
    private String dateTo;
    private Long userNameId;
}
