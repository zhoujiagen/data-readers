package com.spike.data.readers.common.parser.bfs;

import com.spike.data.readers.common.types.Either;

import java.util.List;
import java.util.Optional;

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

        public List<BFSFileFieldSpecification> getFieldSpecifications() {
            return fieldSpecifications;
        }

        public void setFieldSpecifications(List<BFSFileFieldSpecification> fieldSpecifications) {
            this.fieldSpecifications = fieldSpecifications;
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
        private Optional<String> blockName; // maybe null
        private List<BFSBlockFieldDecl> blockFieldDecls;
        private Integer times;

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

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }
    }


}
