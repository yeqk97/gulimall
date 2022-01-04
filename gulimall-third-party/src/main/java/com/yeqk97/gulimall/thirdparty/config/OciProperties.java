package com.yeqk97.gulimall.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oci")
@Data
@RefreshScope
public class OciProperties {
    private String configLocation;
    private String configProfile;
    private String region;
    private String namespace;
    private String bucketName;
}
