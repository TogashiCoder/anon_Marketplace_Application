package com.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {
    @Schema(
            description = "API path invoked by client"
    )
    private String apiPath;

    @Schema(
            description = "Error code representing the error occurred"
    )
    private HttpStatus httpStatus;

    @Schema(
            description = "Error message representing the error occurred"
    )
    private String errorMessage;

    @Schema(
            description = "Time when the error occurred"
    )
    private LocalDateTime errorTime;

    @Schema(
            description = "Detailed validation errors if any"
    )
    private Map<String, String> validationErrors;
}
