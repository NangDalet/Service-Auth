package com.dt.student.register.service.serviceImpl;

import com.dt.student.register.mapper.primary.dropDown.DropDownMapper;
import com.dt.student.register.model.base.*;
import com.dt.student.register.model.base.filter.Filter;
import com.dt.student.register.model.dto.response.dropDown.BranchDropDownResponse;
import com.dt.student.register.model.dto.response.dropDown.GroupDropDownResponse;
import com.dt.student.register.model.dto.response.dropDown.ProgramDropDownResponse;
import com.dt.student.register.model.dto.response.dropDown.UserDropDownResponse;
import com.dt.student.register.service.DropDownService;
import com.dt.student.register.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;

@Service
public class DropDownServiceImpl implements DropDownService {

    @Autowired
    private DropDownMapper dropDownMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseMessage<BaseResult> getListUser(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {

            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());

            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<UserDropDownResponse> userDropDownResponses = dropDownMapper.getUserDropDown(filter);

            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", userDropDownResponses, pagination, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> getListGroup(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {

            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());

            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<GroupDropDownResponse> groupDropDownResponses = dropDownMapper.getGroupDropDown(filter);

            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", groupDropDownResponses, pagination, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> getListBranch(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {

            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());

            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<BranchDropDownResponse> branchDropDownResponses = dropDownMapper.getBranchDropDown(filter);

            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", branchDropDownResponses, pagination, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> getListProgram(Filter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {

            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());

            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<ProgramDropDownResponse> programDropDownResponses = dropDownMapper.getProgramDropDown(filter);

            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", programDropDownResponses, pagination, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }
}
