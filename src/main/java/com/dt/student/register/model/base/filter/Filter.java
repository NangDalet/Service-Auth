package com.dt.student.register.model.base.filter;

import lombok.Data;

@Data
public class Filter extends FilterBase {

    private String orderBy;

    private String searchText;

}
