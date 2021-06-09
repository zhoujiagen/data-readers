package com.spike.data.readers.common.parser.bfs;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;
import com.spike.data.readers.common.DataFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public final class BFSDataReader {
    private static final Logger LOG = LoggerFactory.getLogger(BFSDataReader.class);

    private final BFSParser parser = new BFSParser();

    private BFSIRs.BFSFileUnit bfsFile;

    public BFSDataReader(Path bfsFilePath) throws IOException {
        Preconditions.checkArgument(bfsFilePath != null, "bfsFilePath cannot be null");
        this.bfsFile = parser.parse(bfsFilePath);
    }

    public <OUT> void read(DataFileReader<OUT> reader) throws IOException {
        Optional<List<BFSModels.Include>> includes = bfsFile.getIncludes();
        if (LOG.isDebugEnabled()) {
            if (includes.isPresent()) {
                LOG.debug("{} include: {}", bfsFile.getFileName(), Joiner.on(",").join(includes.get()));
            }
        }

        List<BFSModels.Specification> fieldSpecifications = bfsFile.getSpecifications();
        for (BFSModels.Specification specification : fieldSpecifications) {
            if (specification instanceof BFSModels.BlockDecl) {
                BFSModels.BlockDecl blockDecl = (BFSModels.BlockDecl) specification;
                this.read(reader, blockDecl, 0);
            } else if (specification instanceof BFSModels.BlockDef) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("nothing do to when read BlockDef");
                }
            } else {
                throw new RuntimeException("Invalid specification: "
                        + specification.getClass().getName());
            }
        }
    }

    private <OUT> void read(
            DataFileReader<OUT> reader,
            BFSModels.FieldDecl fieldDecl,
            int level) throws IOException {
        final String fieldName = fieldDecl.getFieldName();
        final String pad = Strings.repeat("\t", level);
        System.err.print(pad + fieldName + ": ");

        byte[] data = new byte[0];

        final BFSModels.FieldTypeSpec fieldTypeSpec = fieldDecl.getFieldTypeSpec();
        if (fieldTypeSpec instanceof BFSModels.FieldTypeSpecAny) {
            LOG.warn("currently not support read FieldTypeSpecAny");
        } else if (fieldTypeSpec instanceof BFSModels.FieldTypeSpecFixed) {
            BFSModels.FieldTypeSpecFixed fieldTypeSpecFixed =
                    (BFSModels.FieldTypeSpecFixed) fieldTypeSpec;
            final BFSModels.Unit typeUnit = fieldTypeSpecFixed.getUnit();
            final Optional<Integer> times = fieldTypeSpecFixed.getTimes();

            if (BFSModels.Unit.b.equals(typeUnit)) {
                LOG.warn("currently not support read bit");
                return;
            } else {
                if (!times.isPresent()) {
                    data = reader.readByte(fieldTypeSpecFixed.getLength());
                } else if (times.get() > 0) {
                    for (int i = 0; i < times.get(); i++) {
                        data = Bytes.concat(data, reader.readByte(fieldTypeSpecFixed.getLength()));
                    }
                }
            }
        } else {
            throw new RuntimeException("Invalid FieldTypeSpec: "
                    + fieldTypeSpec.getClass().getName());
        }

        System.err.println(reader.hexDumpString(data));
    }

    private <OUT> void read(
            DataFileReader<OUT> reader,
            BFSModels.BlockDecl blockDecl,
            int level) throws IOException {
        final String blockName = blockDecl.getBlockName();
        final String pad = Strings.repeat("\t", level);
        System.err.print(pad + blockName + ": {" + System.lineSeparator());
        final int nextLevel = level + 1;

        final List<BFSModels.BlockFieldDecl> blockFieldDecls =
                blockDecl.getBlockFieldDecls();
        final Optional<Integer> times = blockDecl.getTimes();
        if (!times.isPresent()) {
            this.read(reader, blockFieldDecls, nextLevel);
        } else if (times.get() > 0) {
            for (int i = 0; i < times.get(); i++) {
                this.read(reader, blockFieldDecls, nextLevel);
            }
        }
        System.err.println(pad + "}");
    }

    private <OUT> void read(
            DataFileReader<OUT> reader,
            List<BFSModels.BlockFieldDecl> blockFieldDecls,
            int level) throws IOException {
        if (blockFieldDecls == null || blockFieldDecls.size() == 0) {
            return;
        }

        final int nextLevel = level + 1;
        for (BFSModels.BlockFieldDecl blockFieldDecl : blockFieldDecls) {
            if (blockFieldDecl instanceof BFSModels.FieldDecl) {
                this.read(reader, (BFSModels.FieldDecl) blockFieldDecl, nextLevel);
            } else if (blockFieldDecl instanceof BFSModels.BlockRef) {
                LOG.warn("currently not support read BFSBlockDeclRef");
//                BFSModels.BlockRef blockRef = (BFSModels.BlockRef) blockFieldDecl;
//                Optional<BFSModels.Specification> specification =
//                        BFSIRs.PARSE_CONTEXT.findSpecification(blockRef.getBlockName());
//                if (!specification.isPresent()) {
//                    throw new RuntimeException("Specification/BlockRef not found: {}" + blockRef.getBlockName());
//                } else {
//
//                }
            } else if (blockFieldDecl instanceof BFSModels.BlockImplicitDecl) {
                LOG.warn("currently not support read BFSBlockImplicitDecl");
            } else {
                throw new RuntimeException("read " + blockFieldDecl.getClass());
            }
        }
    }

}
