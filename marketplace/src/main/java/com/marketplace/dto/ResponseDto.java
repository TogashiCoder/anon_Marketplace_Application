package com.marketplace.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "response",
        description = "schema to hold successful response information"
)
@Data
@AllArgsConstructor
public class ResponseDto {
    @Schema(
            description = "Status code in the respnse",example = "200"
    )
    private String statusCode;
    @Schema(
            description = "Status message in the response",example = "Request processed successfully"
    )
    private String statusMessage;
}
