package com.rakuten.fullstackrecruitmenttest.utils;

import java.util.Arrays;

public enum Designation {
    DEVELOPER("Developer"),
    SENIOR_DEVELOPER("Senior Developer"),
    MANAGER("Manager"),
    TEAM_LEAD("Team Lead"),
    VP("VP"),
    CEO("CEO");

    private String name;

    private Designation(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static boolean exist(String name) {
        return Arrays.stream(Designation.values())
                .filter(designation -> designation.name.equals(name))
                .count() > 0;
    }

    public static Designation get(String name) {
        return Arrays.stream(Designation.values())
                .filter(designation -> designation.name.equals(name))
                .findFirst()
                .get();
    }
}
