package com.dt.student.register.happer;

import org.springframework.stereotype.Service;

@Service
public class Helper {
    public String getModuleCode(String prefix, int srId, String column, String table, String conditions) {
        // Logic to generate module code based on the passed parameters
        return prefix + String.format("%05d", srId); // Example logic
    }
}
