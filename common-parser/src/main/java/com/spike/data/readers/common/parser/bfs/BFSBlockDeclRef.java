package com.spike.data.readers.common.parser.bfs;

public class BFSBlockDeclRef extends BFSBaseDecl {
    private String blockName;
    private Integer times;

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
