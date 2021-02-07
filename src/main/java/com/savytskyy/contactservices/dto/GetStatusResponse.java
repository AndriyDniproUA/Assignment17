package com.savytskyy.contactservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetStatusResponse {
    private String status;
    private String error;


}
