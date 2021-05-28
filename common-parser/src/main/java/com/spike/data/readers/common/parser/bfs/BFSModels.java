package com.spike.data.readers.common.parser.bfs;

import com.spike.data.readers.common.types.Either;

import java.util.List;

public interface BFSModels {

    abstract class BFSBaseModel {
    }

    class BFSBaseDecl extends BFSBaseModel {
    }

    class BFSFile extends BFSBaseModel {
        private List<String> includes;
        private List<BFSFileFieldSpecification> fieldSpecifications;


        public List<String> getIncludes() {
            return includes;
        }

        public void setIncludes(List<String> includes) {
            this.includes = includes;
        }
    }

    class BFSFileFieldSpecification extends BFSBaseModel {
        private Either<BFSFieldDecl, BFSBlockDecl> either;

        public Either<BFSFieldDecl, BFSBlockDecl> getEither() {
            return either;
        }

        public void setEither(Either<BFSFieldDecl, BFSBlockDecl> either) {
            this.either = either;
        }
    }

    abstract class BFSBlockFieldDecl extends BFSBaseDecl {
    }

    class BFSFieldDecl extends BFSBlockFieldDecl {
        private String fieldName;
        private BFSTypeSpec typeSpec;
        private Integer times;

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

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }

        public byte[] getValues() {
            return values;
        }

        public void setValues(byte[] values) {
            this.values = values;
        }
    }


    class BFSTypeSpec extends BFSBaseModel {
        public enum TypeUnit {
            B, b, LB, BB
        }

        private Integer length;
        private BFSTypeSpec.TypeUnit unit;

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public BFSTypeSpec.TypeUnit getUnit() {
            return unit;
        }

        public void setUnit(BFSTypeSpec.TypeUnit unit) {
            this.unit = unit;
        }
    }

    class BFSBlockDecl extends BFSBaseDecl {
        private String blockName;
        private List<BFSBlockFieldDecl> blockFieldDecls;
        private Integer times;

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

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }
    }


    class BFSBlockDeclRef extends BFSBlockFieldDecl {
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

    class BFSBlockImplicitDecl extends BFSBlockFieldDecl {
        private String blockName; // maybe null
        private BFSBlockFieldDecl blockFieldDecl;
        private Integer times;

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public BFSBlockFieldDecl getBlockFieldDecl() {
            return blockFieldDecl;
        }

        public void setBlockFieldDecl(BFSBlockFieldDecl blockFieldDecl) {
            this.blockFieldDecl = blockFieldDecl;
        }

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }
    }


}
