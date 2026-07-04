package com.dt.student.register.service.serviceImpl;

import com.dt.student.register.happer.FileUploadUtils;
import com.dt.student.register.model.base.BaseResult;
import com.dt.student.register.model.base.MessageService;
import com.dt.student.register.model.base.ResponseMessage;
import com.dt.student.register.model.base.ResponseMessageUtils;
import com.dt.student.register.service.FileUploadService;
import com.dt.student.register.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.UnknownHostException;
import java.util.logging.Logger;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger LOGGER = Logger.getLogger(FileUploadServiceImpl.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    Environment environment;

    @Autowired
    private MessageService messageService;

    @Override
    public ResponseMessage<BaseResult> insert(MultipartFile file, HttpServletRequest httpServletRequest) throws UnknownHostException {
        String filePhoto = "";

        try {
            // Validate user permissions
            Long userId = userService.getUserAuth().getId();

            // Check if file is valid
            if (file == null || file.isEmpty()) {
                LOGGER.warning("No file provided or file is empty.");
                return ResponseMessageUtils.makeResponse(false, messageService.message("No file provided or file is empty.", false));
            }

            // Upload the file
            filePhoto = FileUploadUtils.saveFileUploaded(file);
            if (filePhoto.isEmpty()) {
                LOGGER.warning("File upload failed: Unable to save the file.");
                return ResponseMessageUtils.makeResponse(false, messageService.message("File upload failed.", false));
            }

            LOGGER.info("File successfully uploaded to path: " + filePhoto);


            // Return success response
            return ResponseMessageUtils.makeResponse(true, messageService.message(filePhoto, true));

        } catch (Exception error) {
            // Return error response
            return ResponseMessageUtils.makeResponse(false, messageService.message("Error during file upload: " + error.getMessage(), false));
        }
    }
}
