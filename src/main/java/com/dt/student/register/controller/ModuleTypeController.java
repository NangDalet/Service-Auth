//package com.dt.student.register.controller;
//
//import com.dt.student.register.authentication.helper.UserAuthSession;
//import com.dt.student.register.model.base.BaseResult;
//import com.dt.student.register.model.base.ResponseMessage;
//import com.dt.student.register.model.base.ResponseMessageUtils;
//import com.dt.student.register.model.base.filter.ModuleTypeFilter;
//import com.dt.student.register.service.ModuleTypeService;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.Authorization;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.UnknownHostException;
//
//@RestController
//@RequestMapping("/api/module")
//@Tag(name = "Module Controller", description = "Module Controller")
//public class ModuleTypeController {
//
//    @Autowired
//    private ModuleTypeService moduleTypeService;
//
//    @PostMapping("/list")
//    @ApiOperation(value = "List module type by filter", notes = "List module type by filter", authorizations = {@Authorization(value = "Bearer")})
//    public ResponseMessage<BaseResult> list(@RequestBody ModuleTypeFilter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        return moduleTypeService.getList(filter, httpServletRequest);
//    }
//
//    @PostMapping("/find/{id}")
//    @Operation(summary = "Find module type by id", description = "Find module type by id.")
//    public ResponseMessage<BaseResult> findById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        if (!UserAuthSession.isAuthenticated()) {
//            return ResponseMessageUtils.makeResponse(false, 401, "Unauthorized", "You must be logged in");
//        }
//        return moduleTypeService.getOne(id, httpServletRequest);
//    }
//
//}
