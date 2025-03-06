package com.pro.common.modules.service.dependencies.modelauth.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    // Access token for authorization
    private String accessToken;  // eg: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJndWFyZCI6ImFwaSIsImlzcyI6ImltLndlYiIsImV4cCI6MTczOTk1MDU3NSwiaWF0IjoxNzM5ODY0MTc1LCJqdGkiOiIyMDU0In0.F-D6Rbt3z7GgbuMncSj0_GtApnrcKqQayYo5tYvv_iw"

    // Time in seconds for which the access token is valid
    private Integer expiresIn;  // eg: 86400 (24 hours)

    // Type of the token (usually "Bearer")
    private String type;  // eg: "Bearer"
}
