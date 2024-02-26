package org.oldman.repositories.bulders.join.enums;

public enum JoinType {
    JOIN("join"),
    LEFT_JOIN("left join"),
    RIGHT_JOIN("right join");

    private final String joinType;

    JoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getJoinType() {
        return " " + joinType;
    }
}
