package com.saven.batch.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by nrege on 1/17/2017.
 */
public class Row implements Serializable{

    int index;

    String identity;

    List<Column> columns;

    public String getIdentity() {
        return identity;
    }

    public String getIdentityValue() {
        return getColumn(identity).get().getVal().toString();
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public List<Column> getColumns() {
        if (columns == null) columns = new ArrayList<>();
        return columns;
    }

    public void add(Column column){
        this.getColumns().add(column);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Optional<Column> getColumn(String name) {
        return getColumns().stream().filter(c -> c.getName().equals(name)).findFirst();
    }
}
