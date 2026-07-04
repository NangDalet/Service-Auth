package com.dt.student.register.service.serviceImpl;

import com.dt.student.register.authentication.helper.UserAuthSession;
import com.dt.student.register.mapper.primary.user.ModuleMapper;
import com.dt.student.register.mapper.primary.user.PermissionMapper;
import com.dt.student.register.mapper.primary.user.RoleMapper;
import com.dt.student.register.mapper.primary.user.UserMapper;
import com.dt.student.register.model.base.*;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.dto.request.authentication.changePassword.ChangePasswordRequest;
import com.dt.student.register.model.dto.request.authentication.role.RoleIdList;
import com.dt.student.register.model.dto.request.authentication.user.UserUpdateRequest;
import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.model.dto.response.authentication.user.UserResponse;
import com.dt.student.register.model.users.User;
import com.dt.student.register.model.users.UserRequest;
import com.dt.student.register.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public User getUserAuth() {
        try {
            if (UserAuthSession.getUserAuth() != null) {
                List<User> userList = userMapper.getOneByUsername(UserAuthSession.getUserAuth().getName());
                return userList.get(0);
            } else {
                return new User();
            }
        } catch (Exception error) {
            return null;
        }
    }

    @Override
    public ResponseMessage<UserResponse> me(HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            Long userId = userService.getUserAuth().getId();

            List<UserResponse> userList = userMapper.getOneByUserId(userId);

            if (userList != null && !userList.isEmpty()) {
                userList.getFirst().setModuleTypeList(roleMapper.getModule(userId));
                List<ModuleType> moduleTypeList = userList.getFirst().getModuleTypeList();

                if (moduleTypeList != null && !moduleTypeList.isEmpty()) {
                    for (ModuleType moduleType : moduleTypeList) {
                        Long moduleTypeId = moduleType.getId();
                        moduleType.setModuleList(moduleMapper.getOneByUserId(moduleTypeId, userId));
                    }
                }
            }
            return ResponseMessageUtils.makeSuccessResponse(userList.get(0));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

//    @Override
//    public ResponseMessage<BaseResult> getList(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
//        try {
//            // Check Permission
//            Long userId = userService.getUserAuth().getId();
//            if (permissionMapper.checkPermission(userId, "User (View)") == 0) {
//                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
//            }
//
//            Pagination pagination = new Pagination();
//            pagination.setPage(filter.getPage());
//            pagination.setRowsPerPage(filter.getRowsPerPage());
//            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());
//
//            List<UserResponse> userList = userMapper.getList(filter);
//            if (userList != null && !userList.isEmpty()) {
//                for (UserResponse userResponse : userList) {
//                    userResponse.setUserRoleList(userMapper.getOneUserGroupList(userResponse.getId()));
//                }
//            }
//            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", userList, pagination, true));
//        } catch (Exception error) {
//            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
//        }
//    }

    @Override
    public ResponseMessage<BaseResult> getList(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            // Permission check
            Long userId = userService.getUserAuth().getId();
            if (permissionMapper.checkPermission(userId, "User (View)") == 0) {
                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
            }

            // Pagination
            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());
            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<UserResponse> userList = userMapper.getList(filter);
            if (userList != null && !userList.isEmpty()) {
                for (UserResponse user : userList) {
                    user.setUserRoleList(userMapper.getOneUserGroupList(user.getId()));
                }
            }

            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", userList, pagination, true));
        } catch (Exception e) {
           e.printStackTrace();
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }


    @Override
    public ResponseMessage<BaseResult> getOne(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {

        try {
            // Check Permission
            Long userId = userService.getUserAuth().getId();
            if (permissionMapper.checkPermission(userId, "User (View)") == 0) {
                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
            }

            List<UserResponse> userList = userMapper.getOneByUserId(id);
            if (userList != null && !userList.isEmpty()) {
                for (UserResponse user : userList) {
                    user.setUserRoleList(userMapper.getOneUserGroupList(user.getId()));
                    user.setBranchList(userMapper.getBranch(user.getId()));
                }
            }
            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", userList, true));
        } catch (Exception error) {
            error.printStackTrace();
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> insert(UserRequest userRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            // Check Permission
            Long userId = userService.getUserAuth().getId();
            if (permissionMapper.checkPermission(userId, "User (Add)") == 0) {
                return ResponseMessageUtils.makeResponseByPermission(true, messageService.message("No Permission access.", false));
            }

            if (userMapper.checkDuplicate(userRequest.getUsername()) > 0) {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Duplicate Username", false));
            }

            // ✅ Generate user code
            String lastUserCode = userMapper.getLastUserCode(); // e.g., "UME007"
            String newUserCode = generateNextUserCode(lastUserCode); // e.g., "UME008"

            // Set user data
            User user = new User();
            user.setUserCode(newUserCode); // ✅ Set generated code
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(encoder.encode(userRequest.getPassword()));
            user.setCreatedBy(userId);
            user.setIsActive(1);

            Boolean result = userMapper.insert(user);
            if (result) {
                if (userRequest.getUserGroup() != null && userRequest.getUserGroup().length > 0) {
                    insertSystemRoleUser(userRequest.getUserGroup(), user.getId());
                }

                User currentUser = userService.getUserAuth();
                List<Long> currentUserBranchIds = userMapper.getBranchIdsByUserId(currentUser.getId());
                if (currentUserBranchIds != null && !currentUserBranchIds.isEmpty()) {
                    insertUserBranch(currentUserBranchIds.toArray(new Long[0]), user.getId());
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
    public ResponseMessage<BaseResult> update(UserUpdateRequest updateRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {

        try {

            // upload Signature
            // if (userRequest.getSignature() != null) {
            //     String fileSignature = FileUploadU
            // }
            System.out.println("update updateRequest 00 {} " + updateRequest);
            Boolean result = userMapper.update(updateRequest);
            System.out.println("update user 01 {} " + result);

            if (result) {

                if (updateRequest.getBranchId().length > 0) {
                    insertUserBranch(updateRequest.getBranchId(), updateRequest.getId());
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
    public ResponseMessage<String> delete(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            if (id == 1) {
                return ResponseMessageUtils.makeResponse(true, messageService.message("User Admin Cannot Deleted!", false));
            }
            Boolean result = userMapper.delete(id);
            if (result) {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Success", true));
            } else {
                return ResponseMessageUtils.makeResponse(true, messageService.message("Fail", false));
            }
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<String> changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            List<UserResponse> user = userMapper.getOneByUserId(changePasswordRequest.getUserId());

            if (user == null || user.isEmpty()) {
                return ResponseMessageUtils.makeResponse(false, "User not found.");
            }

            String storedPassword = user.get(0).getPassword();
            String enteredPassword = changePasswordRequest.getCurrentPassword();

            if (storedPassword == null || storedPassword.isEmpty()) {
                // No password set yet: allow to set new password directly
                String encodedNewPassword = encoder.encode(changePasswordRequest.getNewPassword());
                Boolean result = userMapper.updatePassword(changePasswordRequest.getUserId(), encodedNewPassword);
                if (result) {
                    // Assign roles if any
                    if (changePasswordRequest.getRoleList() != null) {
                        insertRoleUser(changePasswordRequest.getRoleList(), changePasswordRequest.getUserId());
                    }
                    return ResponseMessageUtils.makeResponse(true, "Success");
                } else {
                    return ResponseMessageUtils.makeResponse(false, "Failed to set password.");
                }
            }

            // Verify current password
            if (!encoder.matches(enteredPassword, storedPassword)) {
                return ResponseMessageUtils.makeResponse(false, "The password you entered is incorrect.");
            }

            // Update with new password
            String encodedNewPassword = encoder.encode(changePasswordRequest.getNewPassword());
            Boolean result = userMapper.updatePassword(changePasswordRequest.getUserId(), encodedNewPassword);

            if (result) {
                if (changePasswordRequest.getRoleList() != null) {
                    insertRoleUser(changePasswordRequest.getRoleList(), changePasswordRequest.getUserId());
                }
                return ResponseMessageUtils.makeResponse(true, messageService.message("Success", true));
            } else {
                return ResponseMessageUtils.makeResponse(false, messageService.message("Fail", false));
            }
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(false, messageService.message("Error", null, false));
        }
    }

    // function delete UserGroup before update
    private void insertSystemRoleUser(String[] groupId, Long userId) {
        if (groupId.length > 0) {
            userMapper.deleteSystemRoleUser(userId);
            for (String s : groupId) {
                userMapper.insertSystemRoleUser(userId, s.replace("\"", ""));
            }
        }
    }

    public void insertRoleUser(List<RoleIdList> roleIdLists, Long userId) {
        if (roleIdLists != null) {
            roleMapper.deleteRoleUser(userId);
            for (RoleIdList roleIdList : roleIdLists) {
                roleMapper.insertRoleUser(roleIdList.getRoleId(), userId);
            }
        }
    }

    private void insertUserBranch(Long[] branchId, Long userId){

        userMapper.deleteUserBranch(userId);
        for (Long s: branchId) {
            userMapper.insertUserBranch(s, userId);
        }
    }

    private String generateNextUserCode(String lastCode) {
        String prefix = "UME";
        int nextNumber = 1;

        if (lastCode != null && lastCode.startsWith(prefix)) {
            String numberPart = lastCode.substring(prefix.length());
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // fallback to 1
            }
        }

        return String.format("%s%03d", prefix, nextNumber); // UME001, UME002...
    }

}
