package com.dt.student.register.model.base.filter;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class FilterBase {

    private Integer page;

    private Integer rowsPerPage;


    private String orderBy;

    private String searchText;

}
