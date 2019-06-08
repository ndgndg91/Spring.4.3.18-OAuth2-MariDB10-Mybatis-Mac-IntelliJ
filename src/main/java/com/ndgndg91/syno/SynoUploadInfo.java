package com.ndgndg91.syno;

import lombok.Getter;

@Getter
public class SynoUploadInfo {
    private String host;
    private String uploadPath;
    private String webAPI;

    /**
     * parameters
     */
    private String apiName;
    private String apiVersion;
    private String apiMethod;
    private String createParents;

    private SynoUploadInfo(){}
    private SynoUploadInfo(String host, String uploadPath, String webAPI,
                           String apiName, String apiVersion, String apiMethod, String createParents){
        this.host = host;
        this.uploadPath = uploadPath;
        this.webAPI = webAPI;
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        this.apiMethod = apiMethod;
        this.createParents = createParents;
    }
}
