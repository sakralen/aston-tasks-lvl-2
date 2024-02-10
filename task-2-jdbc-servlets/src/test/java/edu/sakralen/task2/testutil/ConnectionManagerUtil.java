package edu.sakralen.task2.testutil;

import edu.sakralen.task2.util.PropertiesUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Properties;

public class ConnectionManagerUtil {
    private static final String CONNECTION_MANAGER_CLASS_NAME =
            "edu.sakralen.task2.util.connectionmanager.impl.HikariCpConnectionManager";

    private ConnectionManagerUtil() {
    }

    public static void loadMockedConnectionManagerClass(String jdbcUrl, String username, String password) {
        Properties containerProperties = new Properties();
        containerProperties.put("jdbcUrl", jdbcUrl);
        containerProperties.put("username", username);
        containerProperties.put("password", password);

        try (MockedStatic<PropertiesUtils> propertiesUtilMock = Mockito.mockStatic(PropertiesUtils.class)) {
            propertiesUtilMock.when(PropertiesUtils::getProperties).thenReturn(containerProperties);
            loadConnectionManagerClass();
        }
    }

    private static void loadConnectionManagerClass() {
        try {
            Class.forName(CONNECTION_MANAGER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
