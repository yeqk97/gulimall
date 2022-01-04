package com.yeqk97.gulimall.thirdparty;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Test
    void contextLoads() throws IOException {
//        String configurationFilePath = "~/.oci/config";
//        String profile = "DEFAULT";
//        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();
//
//        final AuthenticationDetailsProvider provider =
//                new ConfigFileAuthenticationDetailsProvider(configFile);
//
//        ObjectStorage client = new ObjectStorageClient(provider);
//        client.setRegion(Region.EU_FRANKFURT_1);
//
//
//        GetNamespaceResponse ns =
//                client.getNamespace(GetNamespaceRequest.builder().build());
//        System.out.println(ns.getValue());


        ZonedDateTime t = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));

        System.out.println(t.toString());

        LocalDateTime loca = t.toLocalDateTime();
        System.out.println(loca.toString());

        ZonedDateTime parisTime =
                t.withZoneSameLocal(ZoneId.of("Europe/Paris"));
        System.out.println("parisTIme: " + parisTime);


        ZonedDateTime parisTime2 =
                t.withZoneSameInstant(ZoneId.of("Europe/Paris"));
        System.out.println("same instant parisTIme: " + parisTime2);
    }

}
