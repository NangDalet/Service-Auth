package com.dt.student.register.service.serviceImpl;

import com.dt.student.register.mapper.primary.user.ModuleMapper;
import com.dt.student.register.mapper.primary.user.ModuleTypeMapper;
import com.dt.student.register.model.base.*;
import com.dt.student.register.model.base.filter.ModuleTypeFilter;
import com.dt.student.register.model.dto.response.authentication.module.ModuleType;
import com.dt.student.register.service.ModuleTypeService;
import com.dt.student.register.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;

@Service
public class ModuleTypeServiceImpl implements ModuleTypeService {

    @Autowired
    private ModuleTypeMapper moduleTypeMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private MessageService messageService;


    @Override
    public ResponseMessage<BaseResult> getList(ModuleTypeFilter filter, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {

            Pagination pagination = new Pagination();
            pagination.setPage(filter.getPage());
            pagination.setRowsPerPage(filter.getRowsPerPage());
            pagination.setTotal(moduleTypeMapper.countList(filter));

            filter.setPage((filter.getPage() - 1) * filter.getRowsPerPage());

            List<ModuleType> moduleTypeList = moduleTypeMapper.getList(filter);
            if (moduleTypeList != null) {
                for (ModuleType moduleType : moduleTypeList) {
                    Long id = moduleType.getId();
                    if (filter.getRoleId() == null || filter.getRoleId() == 0) {
                        moduleType.setModuleList(moduleMapper.getOne(id));
                    } else {
                        moduleType.setModuleList(moduleMapper.getOneByRoleId(id, filter.getRoleId()));
                    }
                }
            }
            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", moduleTypeList, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }

    @Override
    public ResponseMessage<BaseResult> getOne(Long id, HttpServletRequest httpServletRequest) throws UnknownHostException {
        try {
            List<ModuleType> moduleTypes = moduleTypeMapper.getOne(id);
            if (moduleTypes != null) {
                for (ModuleType moduleType : moduleTypes) {
                    moduleType.setModuleList(moduleMapper.getOne(id));
                }
            }
            return ResponseMessageUtils.makeResponse(true, messageService.message("Success", moduleTypes, true));
        } catch (Exception error) {
            return ResponseMessageUtils.makeResponse(true, messageService.message("Error", null, false));
        }
    }
}
