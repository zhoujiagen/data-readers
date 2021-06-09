package com.spike.data.readers.common.parser.bfs;

import java.util.List;
import java.util.Optional;

public interface BFSModels {

    abstract class Base {
    }

    class Prologue extends Base {
        private Optional<List<Include>> includes;
        private Optional<List<Define>> defines;

        public Optional<List<Include>> getIncludes() {
            return includes;
        }

        public void setIncludes(Optional<List<Include>> includes) {
            this.includes = includes;
        }

        public Optional<List<Define>> getDefines() {
            return defines;
        }

        public void setDefines(Optional<List<Define>> defines) {
            this.defines = defines;
        }
    }

    class Include extends Prologue {
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public String toString() {
            return fileName;
        }
    }

    class Define extends Prologue {
        private String constName;
        private String constValue;

        public String getConstName() {
            return constName;
        }

        public void setConstName(String constName) {
            this.constName = constName;
        }

        public String getConstValue() {
            return constValue;
        }

        public void setConstValue(String constValue) {
            this.constValue = constValue;
        }
    }

    abstract class Specification extends Base {
    }

    class BlockDef extends Specification {
        private String blockName;
        private List<BlockFieldDecl> blockFieldDecls;
        private Optional<BlockTypeSpec> blockTypeSpec;

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public List<BlockFieldDecl> getBlockFieldDecls() {
            return blockFieldDecls;
        }

        public void setBlockFieldDecls(List<BlockFieldDecl> blockFieldDecls) {
            this.blockFieldDecls = blockFieldDecls;
        }

        public Optional<BlockTypeSpec> getBlockTypeSpec() {
            return blockTypeSpec;
        }

        public void setBlockTypeSpec(Optional<BlockTypeSpec> blockTypeSpec) {
            this.blockTypeSpec = blockTypeSpec;
        }
    }

    class BlockDecl extends Specification {
        private String blockName;
        private List<BlockFieldDecl> blockFieldDecls;
        private Optional<Integer> times;

        public String getBlockName() {
            return blockName;
        }

        public void setBlockName(String blockName) {
            this.blockName = blockName;
        }

        public List<BlockFieldDecl> getBlockFieldDecls() {
            return blockFieldDecls;
        }

        public void setBlockFieldDecls(List<BlockFieldDecl> blockFieldDecls) {
            this.blockFieldDecls = blockFieldDecls;
        }

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }


    abstract class BlockFieldDecl extends Base {
    }

    class FieldDecl extends BlockFieldDecl {
        private String fieldName;
        private FieldTypeSpec fieldTypeSpec;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public FieldTypeSpec getFieldTypeSpec() {
            return fieldTypeSpec;
        }

        public void setFieldTypeSpec(FieldTypeSpec fieldTypeSpec) {
            this.fieldTypeSpec = fieldTypeSpec;
        }
    }

    class BlockRef extends BlockFieldDecl {
        private String blockName;
        private Optional<String> aliasName;
        private Optional<BlockRefTypeSpec> blockRefTypeSpec;

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

        public Optional<BlockRefTypeSpec> getBlockRefTypeSpec() {
            return blockRefTypeSpec;
        }

        public void setBlockRefTypeSpec(Optional<BlockRefTypeSpec> blockRefTypeSpec) {
            this.blockRefTypeSpec = blockRefTypeSpec;
        }
    }

    class BlockImplicitDecl extends BlockFieldDecl {
        private Optional<String> blockName;
        private List<BlockFieldDecl> blockFieldDecls;
        private Optional<Integer> times;

        public Optional<String> getBlockName() {
            return blockName;
        }

        public void setBlockName(Optional<String> blockName) {
            this.blockName = blockName;
        }

        public List<BlockFieldDecl> getBlockFieldDecls() {
            return blockFieldDecls;
        }

        public void setBlockFieldDecls(List<BlockFieldDecl> blockFieldDecls) {
            this.blockFieldDecls = blockFieldDecls;
        }

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }

    enum Unit {
        B, b, LB, BB, KB, MB;

        public static Unit of(String text) {
            if (text == null) {
                return null;
            }
            text = text.trim();
            if ("".equals(text)) {
                return null;
            }

            if (b.name().equals(text)) {
                return b;
            } else {
                return valueOf(text.toUpperCase());
            }
        }
    }

    abstract class FieldTypeSpec extends Base {
    }

    class FieldTypeSpecAny extends FieldTypeSpec {
    }

    class FieldTypeSpecFixed extends FieldTypeSpec {
        private int length;
        private Unit unit;
        private Optional<Integer> times;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        public Optional<Integer> getTimes() {
            return times;
        }

        public void setTimes(Optional<Integer> times) {
            this.times = times;
        }
    }


    abstract class BlockTypeSpec extends Base {
    }

    class BlockTypeSpecFixed extends BlockTypeSpec {
        private int length;
        private Unit unit;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }
    }

    class BlockTypeSpecRefed extends BlockTypeSpec {
        private ConstValueRef constValueRef;
        private Unit unit;

        public ConstValueRef getConstValueRef() {
            return constValueRef;
        }

        public void setConstValueRef(ConstValueRef constValueRef) {
            this.constValueRef = constValueRef;
        }

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }
    }

    abstract class BlockRefTypeSpec extends Base {
    }

    class BlockRefTypeSpecAny extends BlockRefTypeSpec {
    }

    class BlockRefTypeSpecFixed extends BlockRefTypeSpec {
        private int times;

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }
    }

    class BlockRefTypeSpecRefed extends BlockRefTypeSpec {
        private FieldValueRef fieldValueRef;

        public FieldValueRef getFieldValueRef() {
            return fieldValueRef;
        }

        public void setFieldValueRef(FieldValueRef fieldValueRef) {
            this.fieldValueRef = fieldValueRef;
        }
    }

    class FieldValueRef extends Base {
        private List<String> valueNames;

        public List<String> getValueNames() {
            return valueNames;
        }

        public void setValueNames(List<String> valueNames) {
            this.valueNames = valueNames;
        }
    }

    class ConstValueRef extends Base {
        private String constValue;

        public String getConstValue() {
            return constValue;
        }

        public void setConstValue(String constValue) {
            this.constValue = constValue;
        }
    }

}
