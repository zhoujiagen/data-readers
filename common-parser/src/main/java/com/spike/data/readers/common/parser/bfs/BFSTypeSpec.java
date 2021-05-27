package com.spike.data.readers.common.parser.bfs;

public class BFSTypeSpec extends BFSBaseModel {
    public enum TypeUnit {
        B, b, LB, BB
    }

    private Integer length;
    private TypeUnit unit;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public TypeUnit getUnit() {
        return unit;
    }

    public void setUnit(TypeUnit unit) {
        this.unit = unit;
    }
}
