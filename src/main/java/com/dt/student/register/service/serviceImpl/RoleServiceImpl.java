package com.dt.student.register.service.serviceImpl;

import com.dt.student.register.mapper.primary.user.ModuleMapper;
import com.dt.student.register.mapper.primary.user.ModuleTypeMapper;
import com.dt.student.register.mapper.primary.user.PermissionMapper;
import com.dt.student.register.mapper.primary.user.RoleMapper;
import com.dt.student.register.model.base.*;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.base.filter.ModuleTypeFilter;
import com.dt.student.register.model.dto.request.authentication.role.RoleDataUpdate;
import com.dt.student.register.model.dto.response.authentication.module.ModuleList;
import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.model.dto.response.authentication.role.RoleData;
import com.dt.student.register.model.dto.response.authentication.role.RoleResponse;
import com.dt.student.register.model.role.Role;
import com.dt.student.register.model.users.UserList;
import com.dt.student.register.service.RoleService;
import com.dt.student.register.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private ModuleTypeMapper moduleTypeMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;
    @Override
    public ResponseMessage<BaseResult> getList(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {

        Long userId = userService.getUserAuth().getId();

        try {
             //Check Permission
          if (permissionMapper.checkPermission(userId, "System Role (View)") == 0) {
            return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
          }
            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());
            pagination.setTotal(roleMapper.countList(filter));
            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<RoleResponse> roleList = roleMapper.getList(filter);

            if(!roleList.isEmpty()){
                for (RoleResponse role : roleList) {
                    Long id = role.getId();
                    role.setUserList(roleMapper.getUser(id));

                    ModuleTypeFilter moduleTypeFilter = new ModuleTypeFilter();
                    moduleTypeFilter.setPage(filter.getPage());
                    moduleTypeFilter.setRowsPerPage(filter.getRowsPerPage());
                    List<ModuleType> moduleTypeList = moduleTypeMapper.getList(moduleTypeFilter);

                    if (moduleTypeList != null) {
                        for (ModuleType moduleType : moduleTypeList) {
                            Long moduleTypeId = moduleType.getId();
                            moduleType.setModuleList(moduleMapper.getOneByRoleId(moduleTypeId, id));
                        }
                    }
                    role.setModuleTypeList(moduleTypeList);
                }
            }
            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", roleList, pagination, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> getOne(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {

        try {
            // Check Permission
            Long userId = userService.getUserAuth().getId();
      if (permissionMapper.checkPermission(userId, "System Role (View)") == 0) {
        return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
      }

            List<RoleResponse> roles = roleMapper.getOne(id);
            if(!roles.isEmpty()){
                Long roleId = roles.get(0).getId();
                roles.get(0).setUserList(roleMapper.getUser(roleId));

                ModuleTypeFilter moduleTypeFilter = new ModuleTypeFilter();
                moduleTypeFilter.setPage(0);
                moduleTypeFilter.setRowsPerPage(10000);
                List<ModuleType> moduleTypeList = moduleTypeMapper.getList(moduleTypeFilter);
                if(moduleTypeList != null){
                    for (ModuleType moduleType : moduleTypeList) {
                        Long moduleTypeId = moduleType.getId();
                        moduleType.setModuleList(moduleMapper.getOneByRoleId(moduleTypeId, roleId));
                    }
                }
                roles.get(0).setModuleTypeList(moduleTypeList);
            }
            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", roles, true));
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> menu(HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            Long userId = userService.getUserAuth().getId();
            List<ModuleType> roleList = roleMapper.getModule(userId);

            if(roleList != null && roleList.size() > 0){
                for (ModuleType moduleType : roleList) {
                    Long moduleTypeId = moduleType.getId();
                    moduleType.setModuleList(moduleMapper.getOneByUserId(moduleTypeId, userId));
                }
            }

            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", roleList,true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> insert(RoleData roleData, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            // Check Permission
            Long userId = userService.getUserAuth().getId();
             if (permissionMapper.checkPermission(userId, "System Role (Add)") == 0) {
               return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
             }

            // Check Validation
            if (bindingResult.hasErrors()) {
                return ResponseMessageUtils.makeResponse(true, bindingResult);
            }

            // Check Required
            if(roleData.getName().isEmpty()){
                return ResponseMessageUtils.makeResponse(true, messageService.message("Required", false));
            }

            // Check Duplicate
            if(roleMapper.checkDuplicate(roleData.getName(), null) > 0){
                return ResponseMessageUtils.makeResponse(true, messageService.message("Duplicate name", false));
            }

            // Check Data
            Role role = new Role();
            role.setName(roleData.getName());
            role.setCreatedBy(userId);
            role.setIsActive(1);

            Boolean result = roleMapper.insert(role);
            if (result) {
                //Insert User and Module
                if(roleData.getUserList() != null){
                    insertRoleUser(roleData.getUserList(), role.getId());
                }
                if(roleData.getModuleList() != null){
                    insertRolePermission(roleData.getModuleList(), role.getId());
                }
                return ResponseMessageUtils.makeResponse(true, messageService.message("Success", true));
            } else {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Fail", false));
            }
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> update(RoleDataUpdate roleDataUpdate, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            // Check Permission
            Long userId = userService.getUserAuth().getId();
            if (permissionMapper.checkPermission(userId, "System Role (Edit)") == 0) {
                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
            }

            // Check Validation
            if (bindingResult.hasErrors()) {
                return ResponseMessageUtils.makeResponse(true, bindingResult);
            }

            // Check Required field
            if (roleDataUpdate.getId() == null || roleDataUpdate.getId() == 0 || roleDataUpdate.getName().isEmpty() || roleDataUpdate.getName() == null) {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Required", false));
            }

            // Check Duplicate
            if(roleMapper.checkDuplicate(roleDataUpdate.getName(), roleDataUpdate.getId()) > 0){
                return ResponseMessageUtils.makeResponse(true, messageService.message("Duplicate", false));
            }
            // Check Data
            Role role = new Role();
            role.setId(roleDataUpdate.getId());
            role.setName(roleDataUpdate.getName());
            role.setIsActive(1);
            role.setModifiedBy(userService.getUserAuth().getId());

            Boolean result = roleMapper.update(role);
            if (result) {
                //Insert User and Module
                insertRoleUser(roleDataUpdate.getUserList(), role.getId());
                insertRolePermission(roleDataUpdate.getModuleList(), role.getId());

                return ResponseMessageUtils.makeResponse(true, messageService.message("Success", true));
            } else {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Fail", false));
            }
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> delete(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            // Check Permission
            Long userId = userService.getUserAuth().getId();
            if (permissionMapper.checkPermission(userId, "System Role (Delete)") == 0) {
                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
            }

            if (id == 1) {
                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("You can't delete Super Admin role.", false));
            }

            Boolean result = roleMapper.delete(id);
            if (result) {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Success", true));
            } else {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Fail", false));
            }
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    private void insertRoleUser(List<UserList> userLists, Long roleId){
        if(userLists != null){
            roleMapper.deleteRoleUser(roleId);
            for (UserList userList : userLists) {
                roleMapper.insertRoleUser(roleId, userList.getUserId());
            }
        }
    }

    private void insertRolePermission(List<ModuleList> moduleLists, Long roleId){
        if(moduleLists != null){
            permissionMapper.delete(roleId);
            for (ModuleList moduleList : moduleLists) {
                permissionMapper.insert(roleId, moduleList.getModuleId());
            }
        }
    }
}
