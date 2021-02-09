package com.savytskyy.contactservices.config;

import com.savytskyy.contactservices.annotations.SystemProp;
import lombok.Data;

@Data
public class AppConfiguration {

    @SystemProp("app.service.workmode")
    private String workMode;

    @SystemProp("api.base-uri")
    private String baseURI;

    @SystemProp("file.path")
    private String filePath;
}
