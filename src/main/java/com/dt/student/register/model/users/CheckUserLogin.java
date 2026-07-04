package com.dt.student.register.model.users;

import lombok.Data;

@Data
public class CheckUserLogin {

    private Long id;

    private String hostName;

    private String hostAddress;

    private String verifyChaptchar;

    private Long numberOfLogin;
}
