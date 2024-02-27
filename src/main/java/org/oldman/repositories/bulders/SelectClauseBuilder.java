package org.oldman.repositories.bulders;

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
