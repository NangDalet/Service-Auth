//package com.dt.student.register.controller;
//
//import com.dt.student.register.authentication.helper.UserAuthSession;
//import com.dt.student.register.model.base.BaseResult;
//import com.dt.student.register.model.base.ResponseMessage;
//import com.dt.student.register.model.base.ResponseMessageUtils;
//import com.dt.student.register.model.base.filter.Filter;
//import com.dt.student.register.model.dto.request.authentication.changePassword.ChangePasswordRequest;
//import com.dt.student.register.model.dto.request.authentication.user.UserUpdateRequest;
//import com.dt.student.register.model.dto.response.authentication.user.UserResponse;
//import com.dt.student.register.model.users.UserRequest;
//import com.dt.student.register.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.UnknownHostException;
//
//@RestController
//@RequestMapping("/api/user")
//@Tag(name = "User Controller", description = "User Controller")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/list")
//    @Operation(summary = "List user", description = "List user")
//    public ResponseMessage<BaseResult> list(@RequestBody Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        // Check Header Token
//        if (UserAuthSession.getUserAuth() == null) {
//            return ResponseMessageUtils.makeResponse(false, 401, "unauthorized", "No Permission to access");
//        }
//        return userService.getList(filter, httpServletRequest);
//    }
//
//    @PostMapping("/find/{id}")
//    @Operation(summary = "Find user by id", description = "Find user by id.")
//    public ResponseMessage<BaseResult> findById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        // Check Header Token
//        if (UserAuthSession.getUserAuth() == null) {
//            return ResponseMessageUtils.makeResponse(false, 401, "unauthorized", "No Permission to access");
//        }
//        return userService.getOne(id, httpServletRequest);
//    }
//
//    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Add user", description = "Add user.")
//    public ResponseMessage<BaseResult> add(@RequestBody UserRequest userRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return userService.insert(userRequest, httpServletRequest);
//    }
//
//    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Update user", description = "Update user")
//    public ResponseMessage<BaseResult> update(@RequestBody UserUpdateRequest updateRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return userService.update(updateRequest, httpServletRequest);
//    }
//
//    @PostMapping("/delete/{id}")
//    @Operation(summary = "Delete user", description = "Delete user")
//    public ResponseMessage<String> delete(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
//
//        if (UserAuthSession.getUserAuth() == null) {
//            return ResponseMessageUtils.makeResponse(false, 401, "unauthorized", "No Permission to access");
//        }
//        return userService.delete(id, httpServletRequest);
//    }
//
//    @PostMapping("/me")
//    @Operation(summary = "Get current user", description = "Get current user.")
//    public ResponseMessage<UserResponse> me(HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return userService.me(httpServletRequest);
//    }
//
//    @PostMapping("/change-password")
//    @Operation(summary = "Change password", description = "Change password.")
//    public ResponseMessage<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return userService.changePassword(changePasswordRequest, httpServletRequest);
//    }
//}
