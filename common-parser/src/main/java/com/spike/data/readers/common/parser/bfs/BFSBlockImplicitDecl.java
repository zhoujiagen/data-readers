package com.spike.data.readers.common.parser.bfs;

public class BFSBlockImplicitDecl extends BFSBaseDecl {
    private String blockName; // maybe null

    // choose one
    private BFSFieldDecl fieldDecl;
    private BFSBlockDecl blockDecl;

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public BFSFieldDecl getFieldDecl() {
        return fieldDecl;
    }

    public void setFieldDecl(BFSFieldDecl fieldDecl) {
        this.fieldDecl = fieldDecl;
    }

    public BFSBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(BFSBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }
}
