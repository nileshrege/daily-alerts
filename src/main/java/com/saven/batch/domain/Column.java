package com.saven.batch.domain;

import java.io.Serializable;

/**
 * Created by nrege on 1/18/2017.
 */
public class Column<T> implements Serializable {

    Class clazz;

    String name;

    T val;

    public String getName(){
        return name;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }

    public Column(String name){
        this.name = name;
    }

    public Column(){}

    public Column(String name, T val){
        this.name = name;
        this.val = val;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
