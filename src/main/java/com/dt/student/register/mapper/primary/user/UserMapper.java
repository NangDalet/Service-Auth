package com.dt.student.register.mapper.primary.user;

import com.dt.student.register.model.dto.request.authentication.user.UserUpdateRequest;
import com.dt.student.register.model.dto.response.authentication.user.UserResponse;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.users.User;

import com.dt.student.register.model.users.UserBranch;
import com.dt.student.register.model.users.UserGroupList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    List<UserResponse> getList(Filter filter);

    Boolean insert(User user);

    Boolean update(UserUpdateRequest userRequest);

    List<User> getOneByUsername(String username);

    List<UserResponse> getOneByUserId(Long userId);

    Boolean editProfile(User user);

    Long checkDuplicate(String username);

    List<UserGroupList> getOneUserGroupList(Long id);

    List<UserBranch> getBranch(Long id);

    User findUserById(Long id);

    Boolean delete(Long id);

    Boolean insertUserBranch(Long branchId, Long userId);

    Boolean deleteUserBranch(Long userId);

    Boolean updatePassword(Long id, String password);


    Boolean deleteSystemRoleUser(@Param("userId") Long userId);

    Boolean insertSystemRoleUser(@Param("userId") Long userId, @Param("groupId") String groupId);

    List<Long> getBranchIdsByUserId(@Param("userId") Long userId);

    String getLastUserCode(); // Get latest userCode like "UME009"



}
