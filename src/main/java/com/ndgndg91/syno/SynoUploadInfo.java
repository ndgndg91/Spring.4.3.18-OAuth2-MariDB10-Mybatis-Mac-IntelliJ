package com.ndgndg91.syno;

import lombok.Getter;

@Getter
public class SynoUploadInfo {
    private String host;
    private String webAPIUploadPost;
    private String webAPIAuthGet;
    private String login;
    private String logout;


    /**
     * upload parameters
     */
    private String apiName;
    private String uploadPath;
    private String apiVersion;
    private String apiMethod;
    private String createParents;

    private SynoUploadInfo(){}
    private SynoUploadInfo(String host, String uploadPath, String webAPIUploadPost, String webAPIAuthGet, String login,
                           String logout, String apiName, String apiVersion, String apiMethod, String createParents){
        this.host = host;
        this.uploadPath = uploadPath;
        this.webAPIUploadPost = webAPIUploadPost;
        this.webAPIAuthGet = webAPIAuthGet;
        this.login = login;
        this.logout = logout;
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        this.apiMethod = apiMethod;
        this.createParents = createParents;
    }
}
