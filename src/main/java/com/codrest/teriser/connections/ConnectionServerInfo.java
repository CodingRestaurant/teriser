/*
 * Author : Hyeokwoo Kwon
 * Filename : ConnectionService.java
 * Desc :
 */

package com.codrest.teriser.connections;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionServerInfo {

    public static final ConnectionServerInfo instance = new ConnectionServerInfo();

    private final Object connectionNumberLock = new Object();
    private final Map<String, String> tokenNameMap;
    private final Map<String, Long> tokenConnectionNumberMap = new ConcurrentHashMap<>();

    {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        try {
            List<String> lines = Files.readAllLines(Path.of("cs"), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] values = line.split(",");
                builder.put(values[0], values[1]); // token, name
                tokenConnectionNumberMap.put(values[0], 0L);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tokenNameMap = builder.build();
    }

    public void increaseConnection(String serverToken) {
        synchronized (connectionNumberLock) {
            tokenConnectionNumberMap.put(serverToken, tokenConnectionNumberMap.get(serverToken) + 1);
        }
    }

    public void decreaseConnection(String serverToken) {
        synchronized (connectionNumberLock) {
            tokenConnectionNumberMap.put(serverToken, tokenConnectionNumberMap.get(serverToken) - 1);
        }
    }

    @Nonnull
    public String optimalConnectionServerName() {
        return tokenNameMap.get(Collections.min(tokenConnectionNumberMap.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    @Nullable
    public String getServerNameByToken(String serverToken) {
        return tokenNameMap.get(serverToken);
    }
}
