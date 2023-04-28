package com.example.final_project.api.mapper;

import com.example.final_project.api.responses.ApiErrorResponse;
import com.example.final_project.domain.error.ApplicationProcessError;

public class ErrorMapper {

    public static ApiErrorResponse mapError(final ApplicationProcessError error) {
        return new ApiErrorResponse(
                error.getCode(),
                error.getReason(),
                error.getMessage());
    }

}
