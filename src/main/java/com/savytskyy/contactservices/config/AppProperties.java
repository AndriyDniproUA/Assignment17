package com.savytskyy.contactservices.config;

import com.savytskyy.contactservices.annotations.SystemProp;
import lombok.Data;

@Data
public class AppProperties {
    @SystemProp("contactservices.profile")
    private String profile;

}
