package com.spike.data.readers.common.parser.bfs;

public class BFSFieldDecl extends BFSBaseDecl {
    private String fieldName;
    private BFSTypeSpec typeSpec;
    private int times;

    // contain values
    private byte[] values;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public BFSTypeSpec getTypeSpec() {
        return typeSpec;
    }

    public void setTypeSpec(BFSTypeSpec typeSpec) {
        this.typeSpec = typeSpec;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }
}
