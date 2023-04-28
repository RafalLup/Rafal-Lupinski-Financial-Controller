package com.example.final_project.api.responses;




public record ApiErrorResponse(
        Integer code,
        String reason,
        String message
) {
}
