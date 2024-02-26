package org.oldman.repositories.bulders;

import org.oldman.repositories.bulders.pojo.FieldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class SelectClauseBuilder implements QueryPartBuilder {
    private String selectString;
    
    public void select(String tableAlias) {
        this.selectString = tableAlias;
    }


    @Override
    public String buildQueryPart() {
        if (selectString == null || selectString.isBlank()) {
            throw new IllegalStateException("There is no data for select");
        }
        return "select " + selectString;
    }
}
