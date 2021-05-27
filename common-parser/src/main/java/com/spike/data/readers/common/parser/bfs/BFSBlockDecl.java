package com.spike.data.readers.common.parser.bfs;

import java.util.List;

public class BFSBlockDecl extends BFSBaseDecl {
    private String blockName;
    private List<BFSBaseDecl> decls;

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public List<BFSBaseDecl> getDecls() {
        return decls;
    }

    public void setDecls(List<BFSBaseDecl> decls) {
        this.decls = decls;
    }
}
