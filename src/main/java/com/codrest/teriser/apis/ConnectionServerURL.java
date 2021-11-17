/*
 * Author : Hyeokwoo Kwon
 * Filename : ConnectionServerURL.java
 * Desc :
 */

package com.codrest.teriser.apis;

public class ConnectionServerURL {

    private static String PREFIX = "http://";
    private static String POSTFIX = ".cs.teriser.codrest.com";

    public static String of(String csName, String projectName) {
        return PREFIX + csName + POSTFIX + "/" + projectName;
    }

    public static String of(String csName, String projectName, String methodName) {
        return PREFIX + csName + POSTFIX + "/" + projectName + "/" + methodName;
    }
}
