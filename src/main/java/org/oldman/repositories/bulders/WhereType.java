package org.oldman.repositories.bulders;

public enum WhereType {
    WHERE("where"),
    AND("and"),
    OR("or");

    private final String whereType;

    WhereType(String whereType) {
        this.whereType = whereType;
    }

    public String getWhereType() {
        return " " + whereType;
    }
}
