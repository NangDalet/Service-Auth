package com.dt.student.register.authentication.util;


import com.dt.student.register.mapper.primary.auth.AuthMapper;
import com.dt.student.register.model.base.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthValidation {

    private final AuthMapper authMapper;
    private final PasswordUtil passwordUtil;
    private final EmailServiceUtil emailServiceUtil;
    private final MessageService messageService;

    @Autowired
    public AuthValidation(AuthMapper authMapper, PasswordUtil passwordUtil,
                          EmailServiceUtil emailServiceUtil, MessageService messageService
    ) {
        this.authMapper = authMapper;
        this.passwordUtil = passwordUtil;
        this.emailServiceUtil = emailServiceUtil;
        this.messageService = messageService;
    }



//    public String ensureUniqueUsername(String username) {
//        int suffix = 1;
//        String newUsername = username;
//        while (authMapper.findUserDetails(newUsername) != null) {
//            newUsername = username + suffix;
//            suffix++;
//        }
//        return newUsername;
//    }
//
//    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
//        CookieUtils.getCookie(request, "accessToken").ifPresent(cookie -> CookieUtils.deleteCookie(request, response, "accessToken"));
//        CookieUtils.getCookie(request, "refreshToken").ifPresent(cookie -> CookieUtils.deleteCookie(request, response, "refreshToken"));
//    }


}
