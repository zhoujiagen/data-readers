package com.spike.data.readers.common.parser.bfs;

import java.util.List;

public class BFSFile extends BFSBaseModel {
    private List<String> includes;
    private List<BFSBaseDecl> fieldSpecifications;

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<BFSBaseDecl> getFieldSpecifications() {
        return fieldSpecifications;
    }

    public void setFieldSpecifications(List<BFSBaseDecl> fieldSpecifications) {
        this.fieldSpecifications = fieldSpecifications;
    }
}
