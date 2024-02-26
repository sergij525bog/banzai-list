package org.oldman.repositories.bulders.join;

public interface JoinData {
    String buildJoinData();

    interface JoinDataBuilder {
        JoinData build();
    }
}
