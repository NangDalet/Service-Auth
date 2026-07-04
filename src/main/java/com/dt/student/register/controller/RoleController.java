//package com.dt.student.register.controller;
//
//import com.dt.student.register.authentication.helper.UserAuthSession;
//import com.dt.student.register.model.base.BaseResult;
//import com.dt.student.register.model.base.ResponseMessage;
//import com.dt.student.register.model.base.ResponseMessageUtils;
//import com.dt.student.register.model.base.filter.Filter;
//import com.dt.student.register.model.dto.request.authentication.role.RoleDataUpdate;
//import com.dt.student.register.model.dto.response.authentication.role.RoleData;
//import com.dt.student.register.service.RoleService;
//import io.micrometer.core.annotation.Timed;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.Authorization;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.UnknownHostException;
//@RestController
//@RequestMapping("/api/role")
//@Tag(name = "Role Controller", description = "Role Controller")
//@Timed
//public class RoleController {
//
//    @Autowired
//    private RoleService roleService;
//
//    @PostMapping("/list")
//    @Operation(summary = "List role by filter", description = "List role by filter.")
//    public ResponseMessage<BaseResult> list(@RequestBody Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return roleService.getList(filter, httpServletRequest);
//    }
//
//    @PostMapping("/find/{id}")
//    @Operation(summary = "Find role by id", description = "Find role by id.")
//    public ResponseMessage<BaseResult> findById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return roleService.getOne(id, httpServletRequest);
//    }
//
//    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Add role", description = "Add role.")
//    public ResponseMessage<BaseResult> add(@RequestBody RoleData roleData, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return roleService.insert(roleData, bindingResult, httpServletRequest);
//    }
//
//    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Update role by id", description = "Update role by id.")
//    public ResponseMessage<BaseResult> update(@RequestBody RoleDataUpdate roleDataUpdate, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return roleService.update(roleDataUpdate, bindingResult, httpServletRequest);
//    }
//
//    @PostMapping("/menu")
//    @ApiOperation(value = "list menu", notes = "list menu", authorizations = {@Authorization(value = "Bearer")})
//    public ResponseMessage<BaseResult> menu(HttpServletRequest httpServletRequest) throws UnknownHostException {
//        return roleService.menu(httpServletRequest);
//    }
//
//    @PostMapping("/delete/{id}")
//    @ApiOperation(value = "Delete role by id", notes = "Delete role", authorizations = {@Authorization(value = "Bearer")})
//    public ResponseMessage<BaseResult> delete(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        return roleService.delete(id, httpServletRequest);
//    }
//}
