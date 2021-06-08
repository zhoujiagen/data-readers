package com.spike.data.readers.common.parser.bfs;

import java.util.List;
import java.util.Optional;

public interface BFSModels {

    abstract class BFSBaseModel {
    }

    class BFSInclude extends BFSBaseModel {
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    class BFSBaseDecl extends BFSBaseModel {
    }

    class BFSFile extends BFSBaseModel {
        private String fileName;
        private Optional<List<BFSInclude>> includes;
        private List<BFSFileFieldSpecification> fieldSpecifications;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Optional<List<BFSInclude>> getIncludes() {
            return includes;
        }

        public void setIncludes(Optional<List<BFSInclude>> includes) {
            this.includes = includes;
        }

        public List<BFSFileFieldSpecification> getFieldSpecifications() {
            return fieldSpecifications;
        }

        public void setFieldSpecifications(List<BFSFileFieldSpecification> fieldSpecifications) {
            this.fieldSpecifications = fieldSpecifications;
        }
    }

    class BFSFileFieldSpecification extends BFSBaseModel {
        private Optional<BFSFieldDecl> fieldDecl = Optional.empty();
        private Optional<BFSBlockDef> blockDef = Optional.empty();
        private Optional<BFSBlockDecl> blockDecl = Optional.empty();

        public Optional<BFSFieldDecl> getFieldDecl() {
            return fieldDecl;
        }

        public void setFieldDecl(Optional<BFSFieldDecl> fieldDecl) {
            this.fieldDecl = fieldDecl;
        }

        public Optional<BFSBlockDef> getBlockDef() {
            return blockDef;
        }

        public void setBlockDef(Optional<BFSBlockDef> blockDef) {
            this.blockDef = blockDef;
        }

        public Optional<BFSBlockDecl> getBlockDecl() {
            return blockDecl;
        }

        public void setBlockDecl(Optional<BFSBlockDecl> blockDecl) {
            this.blockDecl = blockDecl;
        }
    }

    abstract class BFSBlockFieldDecl extends BFSBaseDecl {
    }

    class BFSFieldDecl extends BFSBlockFieldDecl {
        private String fieldName;
        private BFSTypeSpec typeSpec;
        private Optional<Integer> times;

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

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }

    enum BFSTypeUnit {
        B, b, LB, BB;
    }

    class BFSTypeSpec extends BFSBaseModel {
        private int length;
        private BFSTypeUnit unit;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public BFSTypeUnit getUnit() {
            return unit;
        }

        public void setUnit(BFSTypeUnit unit) {
            this.unit = unit;
        }
    }

    class BFSBlockDef extends BFSBaseDecl {
        private String blockName;
        private List<BFSBlockFieldDecl> blockFieldDecls;

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public List<BFSBlockFieldDecl> getBlockFieldDecls() {
            return blockFieldDecls;
        }

        public void setBlockFieldDecls(List<BFSBlockFieldDecl> blockFieldDecls) {
            this.blockFieldDecls = blockFieldDecls;
        }
    }

    class BFSBlockDecl extends BFSBaseDecl {
        private String blockName;
        private List<BFSBlockFieldDecl> blockFieldDecls;
        private Optional<Integer> times;

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public List<BFSBlockFieldDecl> getBlockFieldDecls() {
            return blockFieldDecls;
        }

        public void setBlockFieldDecls(List<BFSBlockFieldDecl> blockFieldDecls) {
            this.blockFieldDecls = blockFieldDecls;
        }

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }


    class BFSBlockDeclRef extends BFSBlockFieldDecl {
        private String blockName;
        private Optional<String> aliasName;
        private Optional<Integer> times;

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public Optional<String> getAliasName() {
            return aliasName;
        }

        public void setAliasName(Optional<String> aliasName) {
            this.aliasName = aliasName;
        }

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }

    class BFSBlockImplicitDecl extends BFSBlockFieldDecl {
        private Optional<String> blockName; // maybe null
        private List<BFSBlockFieldDecl> blockFieldDecls;
        private Optional<Integer> times;

        public Optional<String> getBlockName() {
            return blockName;
        }

        public void setBlockName(Optional<String> blockName) {
            this.blockName = blockName;
        }

        public List<BFSBlockFieldDecl> getBlockFieldDecls() {
            return blockFieldDecls;
        }

        public void setBlockFieldDecls(List<BFSBlockFieldDecl> blockFieldDecls) {
            this.blockFieldDecls = blockFieldDecls;
        }

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }


}
