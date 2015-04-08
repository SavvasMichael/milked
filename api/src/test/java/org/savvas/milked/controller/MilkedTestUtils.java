package org.savvas.milked.controller;

import java.util.UUID;

public class MilkedTestUtils {

    public static String randomEmail() {
        return UUID.randomUUID().toString()+"@ymail.com";
    }
}
