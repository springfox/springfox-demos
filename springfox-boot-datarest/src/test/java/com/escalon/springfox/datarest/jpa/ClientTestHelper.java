package com.escalon.springfox.datarest.jpa;

import org.junit.jupiter.api.TestInfo;

/**
 * @author amutsch
 * @since 12/18/2018
 */
public class ClientTestHelper {

    public static String getDynamicDataFromTestInfo(TestInfo testInfo) {
        return testInfo.getDisplayName().length() > 50 ? testInfo.getDisplayName().substring(0, 50) :
            testInfo.getDisplayName();
    }

    public static Client createValidClient(TestInfo testInfo) {
        String dynamicData = getDynamicDataFromTestInfo(testInfo);
        Client client = new Client();
        client.setClientName(dynamicData);
        client.setActive(true);
        client.setCreatedBy(dynamicData);
        client.setUpdatedBy(dynamicData);
        return client;
    }
}
