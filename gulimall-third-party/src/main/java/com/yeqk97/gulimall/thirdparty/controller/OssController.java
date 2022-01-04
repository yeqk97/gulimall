package com.yeqk97.gulimall.thirdparty.controller;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;
import com.yeqk97.common.utils.R;
import com.yeqk97.gulimall.thirdparty.config.OciProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OssController {

    private OciProperties ociProperties;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String NAMESPACE_URL_PREFIX = "/n/";

    public OssController(OciProperties ociProperties) {
        this.ociProperties = ociProperties;
    }

    @RequestMapping("/oss/policy")
    public R policy() throws IOException {
        final ConfigFileReader.ConfigFile configFile =
                ConfigFileReader.parse(ociProperties.getConfigLocation(),
                        ociProperties.getConfigProfile());
        final ConfigFileAuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(ociProperties.getRegion());

        CreatePreauthenticatedRequestDetails
                details =
                CreatePreauthenticatedRequestDetails.builder()
                        .accessType(
                                CreatePreauthenticatedRequestDetails.AccessType.AnyObjectReadWrite)
                        .name(
                                LocalDateTime.now().toString())
                        .timeExpires(Date.from(Instant.now().plusSeconds(300)))
                        .build();

        CreatePreauthenticatedRequestRequest request =
                CreatePreauthenticatedRequestRequest.builder()
                        .namespaceName(ociProperties.getNamespace())
                        .bucketName(ociProperties.getBucketName())
                        .createPreauthenticatedRequestDetails(details)
                        .build();

        PreauthenticatedRequest
                response = client.createPreauthenticatedRequest(request)
                .getPreauthenticatedRequest();

        String host = String.format(
                "https://objectstorage.%s.oraclecloud.com",
                ociProperties.getRegion());

        Map<String, String> responseMap = new LinkedHashMap<>();
        responseMap.put("id", response.getId());
        responseMap.put("host", host);
        responseMap.put("preAuthPath", response.getAccessUri()
                .substring(0, response.getAccessUri().indexOf(NAMESPACE_URL_PREFIX)));
        responseMap.put("dir", response.getAccessUri()
                .substring(response.getAccessUri().indexOf(NAMESPACE_URL_PREFIX))+ ZonedDateTime.now(
                ZoneId.of("UTC")).format(formatter) + "/");
        responseMap.put("expire",
                String.valueOf(response.getTimeExpires().getTime()));

        return R.ok().put("data", responseMap);
    }

}
