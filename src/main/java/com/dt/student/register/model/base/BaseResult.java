package com.dt.student.register.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseResult {

    private Boolean status;

    private Integer msgCode;

    private String message;

    private Pagination pagination;

    private List data;

    private Long id;

}