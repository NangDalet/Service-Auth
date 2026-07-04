package com.dt.student.register.happer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUploadUtils {

    private static final String ROOT;
    private static final String PROJECT_NAME;
    private static final String FOLDER_UPLOAD;

    private static final Logger LOGGER = Logger.getLogger(FileUploadUtils.class.getName());

    static {
        ROOT = System.getProperty("catalina.base", System.getProperty("java.io.tmpdir"));
        PROJECT_NAME = "logs/DTWDAPi";
        FOLDER_UPLOAD = "upload";
    }

    private FileUploadUtils() {
        // Prevent instantiation
    }

    /**
     * Save an uploaded file to the specified directory.
     *
     * @param fileUploaded MultipartFile object representing the file.
     * @return The URL of the uploaded file or an empty string in case of failure.
     */
    public static String saveFileUploaded(MultipartFile fileUploaded) {
        if (fileUploaded == null || fileUploaded.isEmpty()) {
            LOGGER.warning("Invalid file: File is null or empty.");
            return "";
        }

        try {
            byte[] bytes = IOUtils.toByteArray(fileUploaded.getInputStream());

            File path = getUploadDirectory();
            if (!validateUploadDirectory(path)) {
                return "";
            }

            String originalFilename = fileUploaded.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                LOGGER.warning("File upload failed: Original filename is missing.");
                return "";
            }

            String extension = FilenameUtils.getExtension(originalFilename);
            if (!validateFileExtension(extension)) {
                LOGGER.warning("File upload failed: Invalid file extension.");
                return "";
            }

            String uniqueFilename = generateUniqueFilename(extension);
            File file = new File(path, uniqueFilename);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }

            LOGGER.info("File uploaded successfully to path: " + file.getAbsolutePath());
            return makeFileUploadedUrl(uniqueFilename);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File upload failed due to IOException: " + e.getMessage(), e);
            return "";
        }
    }

    /**
     * Generate a unique filename using UUID.
     *
     * @param extension File extension.
     * @return A unique filename with the provided extension.
     */
    private static String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString() + FilenameUtils.EXTENSION_SEPARATOR + extension;
    }

    /**
     * Validate the file extension.
     *
     * @param extension File extension.
     * @return True if the extension is valid, false otherwise.
     */
    private static boolean validateFileExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }

        // Allow only common image file extensions
        String[] allowedExtensions = {
                "jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg"
        };

        for (String allowed : allowedExtensions) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Create the URL for the uploaded file.
     *
     * @param filename Name of the uploaded file.
     * @return The URL of the file.
     */
    public static String makeFileUploadedUrl(String filename) {
        if (filename == null || filename.isEmpty()) {
            LOGGER.warning("Cannot create file URL: Filename is null or empty.");
            return "";
        }
        return "/" + FOLDER_UPLOAD + "/" + filename;
    }

    /**
     * Delete a file from the directory.
     *
     * @param fileName Name of the file to delete.
     */
    public static void deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.warning("Invalid file deletion request: Filename is null or empty.");
            return;
        }

        File file = new File(ROOT + File.separator + PROJECT_NAME + File.separator + fileName);
        if (file.exists()) {
            if (file.delete()) {
                LOGGER.info("File deleted successfully: " + fileName);
            } else {
                LOGGER.warning("Failed to delete file: " + fileName);
            }
        } else {
            LOGGER.warning("File does not exist for deletion: " + fileName);
        }
    }

    /**
     * Save a Base64-encoded file to the specified directory.
     *
     * @param base64 Base64 string representation of the file.
     * @return The URL of the uploaded file or an empty string in case of failure.
     */
    public static String saveFileBase64(String base64) {
        if (base64 == null || base64.isEmpty()) {
            LOGGER.warning("Invalid Base64 input: Input is null or empty.");
            return "";
        }

        try {
            String[] parts = base64.split(",");
            if (parts.length < 2) {
                LOGGER.warning("Invalid Base64 input: Missing data after the comma.");
                return "";
            }

            byte[] bytes = Base64.getDecoder().decode(parts[1]);
            File path = getUploadDirectory();
            if (!validateUploadDirectory(path)) {
                return "";
            }

            String uniqueFilename = generateUniqueFilename("png");
            File file = new File(path, uniqueFilename);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }

            LOGGER.info("Base64 file uploaded successfully to path: " + file.getAbsolutePath());
            return makeFileUploadedUrl(uniqueFilename);

        } catch (IllegalArgumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Base64 file upload failed: " + e.getMessage(), e);
            return "";
        }
    }

    /**
     * Get the upload directory.
     *
     * @return The File object representing the upload directory.
     */
    private static File getUploadDirectory() {
        return new File(ROOT + File.separator + PROJECT_NAME + File.separator + FOLDER_UPLOAD);
    }

    /**
     * Validate the upload directory.
     *
     * @param path The File object representing the directory.
     * @return True if the directory is valid, false otherwise.
     */
    private static boolean validateUploadDirectory(File path) {
        if (!path.exists() && !path.mkdirs()) {
            LOGGER.warning("Failed to create upload directory: " + path.getAbsolutePath());
            return false;
        }
        return true;
    }

    public static class ModuleCodeHelper {

        // Method to generate the module code
        public String getModuleCode(String dnCode, Long deliveryId, String columnName, String tableName, String condition) {
            // Implement the logic to generate the module code
            // For now, we'll return a mock code; you can customize this as needed.
            return dnCode + "-" + deliveryId;
        }
    }

    public class DateHelper {

        private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        public static String convertDate(String dateStr) {
            if (dateStr == null || dateStr.isEmpty() || "0000-00-00".equals(dateStr)) {
                return "0000-00-00"; // Maintain consistency with the PHP logic
            }
            try {
                LocalDate date = LocalDate.parse(dateStr, INPUT_FORMAT);
                return date.format(OUTPUT_FORMAT); // Converts from "yyyy-MM-dd" to "dd/MM/yyyy"
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + dateStr);
            }
        }
    }

    public class NumberHelper {

        public static double formatQuantity(double quantity) {
            // Define a DecimalFormat with 6 decimal places
            DecimalFormat df = new DecimalFormat("#.######", new DecimalFormatSymbols(Locale.US));

            // Format and parse back to ensure it's correctly formatted as a number
            return Double.parseDouble(df.format(quantity));
        }
    }

}
