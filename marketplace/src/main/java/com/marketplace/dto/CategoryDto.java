package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Long parentCategoryId;

    private List<Long> subCategoryIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer level;

//    @NotNull(message = "Active status is required")
//    private Boolean isActive;

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private Date createdAt;
//
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private Date updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> productIds;
}
