package com.dt.student.register.mapper.primary.dropDown;

import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.dto.response.dropDown.BranchDropDownResponse;
import com.dt.student.register.model.dto.response.dropDown.GroupDropDownResponse;
import com.dt.student.register.model.dto.response.dropDown.ProgramDropDownResponse;
import com.dt.student.register.model.dto.response.dropDown.UserDropDownResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DropDownMapper {

    List<UserDropDownResponse> getUserDropDown(Filter filter);

    List<GroupDropDownResponse> getGroupDropDown(Filter filter);

    List<BranchDropDownResponse> getBranchDropDown(Filter filter);

    List<ProgramDropDownResponse> getProgramDropDown(Filter filter);

}
