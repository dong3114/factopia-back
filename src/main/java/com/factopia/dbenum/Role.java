package com.factopia.dbenum;

public enum Role {
    MEMBER(1, "ROLE_MEMBER"),
    COMPANY_MANAGER(2, "ROLE_COMPANY_MANAGER"),
    COMPANY_OWNER(3, "ROLE_COMPANY_OWNER"),
    ADMIN(4, "ROLE_ADMIN");

    private final int level;
    private final String roleName;

    Role(int level, String roleName) {
        this.level = level;
        this.roleName = roleName;
    }

    public int getLevel() {
        return level;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role fromLevel(int level) {
        for (Role role : values()) {
            if (role.getLevel() == level) {
                return role;
            }
        }
        throw new IllegalArgumentException("권한레벨: " + level);
    }
}

