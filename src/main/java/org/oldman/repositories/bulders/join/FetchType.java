package org.oldman.repositories.bulders.join;

public enum FetchType {
    FETCH("fetch"),
    NONE("");

    private final String fetchType;

    FetchType(String fetchType) {
        this.fetchType = fetchType;
    }

    public String getFetchType() {
        return fetchType.isEmpty() ? fetchType : " " + fetchType;
    }
}
