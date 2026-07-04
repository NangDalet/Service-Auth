package com.dt.student.register.model.log;

import com.dt.student.register.model.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityLog extends BaseModel {

    private Long id;

    private String module;

    private Long moduleId;

    private String act;

    private String description;

    private String bug;

    private String browser;

    private String operatingSystem;

    private String ip;

    private Long duration;

    private String hostName;

    private Long line;

    private String endpoint;

}
