package com.spike.data.readers.common.parser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.spike.data.readers.common.DataFileReader;
import com.spike.data.readers.common.parser.bfs.BFSIRs;
import com.spike.data.readers.common.parser.bfs.BFSModels;
import org.antlr.v4.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BFSReader extends BFSParserBaseVisitor<BFSModels.BFSBaseModel> {
    private static final Logger LOG = LoggerFactory.getLogger(BFSReader.class);

    private Path path;
    private BFSModels.BFSFile bfsFile;

    public BFSReader(Path path) throws IOException {
        Preconditions.checkArgument(path != null, "path cannot be null");
        this.path = path;
        this.bfsFile = parse();
    }

    //---------------------------------------------------------------------------
    // Read
    //---------------------------------------------------------------------------

    public <OUT> void read(DataFileReader<OUT> reader) throws IOException {
        Optional<List<BFSModels.BFSInclude>> includes = bfsFile.getIncludes();
        if (LOG.isDebugEnabled()) {
            if (includes.isPresent()) {
                LOG.debug("Include: {}", Joiner.on(",").join(includes.get()));
            }
        }

        List<BFSModels.BFSFileFieldSpecification> fieldSpecifications =
                bfsFile.getFieldSpecifications();
        for (BFSModels.BFSFileFieldSpecification fieldSpecification :
                fieldSpecifications) {
            if (fieldSpecification.getFieldDecl().isPresent()) {
                BFSModels.BFSFieldDecl fieldDecl = fieldSpecification.getFieldDecl().get();
                this.read(reader, fieldDecl, 0);
            } else if (fieldSpecification.getBlockDef().isPresent()) {
                BFSModels.BFSBlockDef blockDef = fieldSpecification.getBlockDef().get();
                // do nothing
            } else if (fieldSpecification.getBlockDecl().isPresent()) {
                BFSModels.BFSBlockDecl blockDecl = fieldSpecification.getBlockDecl().get();
                this.read(reader, blockDecl, 0);
            }
        }
    }

    private <OUT> void read(
            DataFileReader<OUT> reader,
            BFSModels.BFSFieldDecl fieldDecl,
            int level) throws IOException {
        final String fieldName = fieldDecl.getFieldName();
        final String pad = Strings.repeat("\t", level);
        System.err.print(pad + fieldName + ": ");

        byte[] data = new byte[0];

        final BFSModels.BFSTypeSpec typeSpec = fieldDecl.getTypeSpec();
        final BFSModels.BFSTypeUnit typeUnit = typeSpec.getUnit();
        final Optional<Integer> times = fieldDecl.getTimes();

        if (BFSModels.BFSTypeUnit.b.equals(typeUnit)) {
            LOG.warn("currently not support read bit");
            return;
        } else {
            if (!times.isPresent()) {
                data = reader.readByte(typeSpec.getLength());
            } else if (times.get() > 0) {
                for (int i = 0; i < times.get(); i++) {
                    data = Bytes.concat(data, reader.readByte(typeSpec.getLength()));
                }
            }
        }

        System.err.println(reader.hexDumpString(data));
    }

    private <OUT> void read(
            DataFileReader<OUT> reader,
            BFSModels.BFSBlockDecl blockDecl,
            int level) throws IOException {
        final String blockName = blockDecl.getBlockName();
        final String pad = Strings.repeat("\t", level);
        System.err.print(pad + blockName + ": {" + System.lineSeparator());
        final int nextLevel = level + 1;

        final List<BFSModels.BFSBlockFieldDecl> blockFieldDecls =
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
            List<BFSModels.BFSBlockFieldDecl> blockFieldDecls,
            int level) throws IOException {
        if (blockFieldDecls == null || blockFieldDecls.size() == 0) {
            return;
        }

        final int nextLevel = level + 1;
        for (BFSModels.BFSBlockFieldDecl blockFieldDecl : blockFieldDecls) {
            if (blockFieldDecl instanceof BFSModels.BFSFieldDecl) {
                this.read(reader, (BFSModels.BFSFieldDecl) blockFieldDecl, nextLevel);
            } else if (blockFieldDecl instanceof BFSModels.BFSBlockDeclRef) {
                LOG.warn("currently not support read BFSBlockDeclRef");
            } else if (blockFieldDecl instanceof BFSModels.BFSBlockImplicitDecl) {
                LOG.warn("currently not support read BFSBlockImplicitDecl");
            } else {
                throw new RuntimeException("read " + blockFieldDecl.getClass());
            }
        }
    }


    //---------------------------------------------------------------------------
    // Parse
    //---------------------------------------------------------------------------

    static class ParseContext {
        Map<String, BFSIRs.BFSFileUnit> bfsFileUnitMap = Maps.newHashMap();

        public void registerBFSFile(BFSIRs.BFSFileUnit bfsFileUnit) {
            final String fileName = bfsFileUnit.getFileName();
            if (bfsFileUnitMap.containsKey(fileName)) {
                LOG.warn("File Unit already exist: {}", fileName);
            }
            bfsFileUnitMap.put(fileName, bfsFileUnit);
        }
    }

    static final ParseContext PARSE_CONTEXT = new ParseContext();

    BFSModels.BFSFile parse() throws IOException {
        this.bfsFile = this.parse(path);

        BFSIRs.BFSFileUnit bfsFileUnit = new BFSIRs.BFSFileUnit();
        bfsFileUnit.setFileName(this.bfsFile.getFileName());
        bfsFileUnit.setBfsFile(this.bfsFile);

        if (bfsFile.getIncludes().isPresent()) {
            List<BFSIRs.BFSFileUnit> includeBFSFileUnits = Lists.newArrayList();
            for (BFSModels.BFSInclude includeFile : bfsFile.getIncludes().get()) {
                BFSModels.BFSFile includeBFSFile =
                        this.parse(path.getParent().resolve(includeFile.getFileName()));
                BFSIRs.BFSFileUnit includeBFSFileUnit = new BFSIRs.BFSFileUnit();
                includeBFSFileUnit.setFileName(includeBFSFile.getFileName());
                includeBFSFileUnit.setBfsFile(includeBFSFile);
                PARSE_CONTEXT.registerBFSFile(includeBFSFileUnit);
                includeBFSFileUnits.add(includeBFSFileUnit);
            }
            bfsFileUnit.setIncludes(Optional.of(includeBFSFileUnits));
        } else {
            bfsFileUnit.setIncludes(Optional.empty());
        }

        PARSE_CONTEXT.registerBFSFile(bfsFileUnit);

        return this.bfsFile;
    }

    BFSModels.BFSFile parse(Path bfsFilePath) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parse {}", bfsFilePath.toString());
        }

        CharStream rawCS = CharStreams.fromPath(bfsFilePath);
        CaseChangingCharStream cs = new CaseChangingCharStream(rawCS, true);
        BFSParserLexer lexer = new BFSParserLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BFSParserParser parser = new BFSParserParser(tokens);

        parser.setErrorHandler(new DefaultErrorStrategy());
        parser.addErrorListener(new DiagnosticErrorListener());

        BFSParserParser.RootContext rootContext = parser.root();
        BFSModels.BFSFile result = this.visitRoot(rootContext);

        result.setFileName(bfsFilePath.getFileName().toString());
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSFile visitRoot(BFSParserParser.RootContext ctx) {
        BFSModels.BFSFile result = new BFSModels.BFSFile();

        if (ctx.prologue() != null) {
            List<BFSModels.BFSInclude> includes = Lists.newArrayList();
            List<BFSParserParser.IncludeContext> includeContexts = ctx.prologue().include();
            for (BFSParserParser.IncludeContext includeContext : includeContexts) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("include: {}", includeContext.fileName.getText());
                }
                includes.add(this.visitInclude(includeContext));
            }
            result.setIncludes(Optional.of(includes));
        } else {
            result.setIncludes(Optional.empty());
        }

        List<BFSModels.BFSFileFieldSpecification> specifications = Lists.newArrayList();
        List<BFSParserParser.FieldSpecificationContext> fieldSpecificationContexts =
                ctx.fieldSpecification();
        for (BFSParserParser.FieldSpecificationContext fieldSpecificationContext :
                fieldSpecificationContexts) {
            specifications.add(this.visitFieldSpecification(fieldSpecificationContext));
        }
        result.setFieldSpecifications(specifications);

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSBaseModel visitPrologue(BFSParserParser.PrologueContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSInclude visitInclude(BFSParserParser.IncludeContext ctx) {
        BFSModels.BFSInclude result = new BFSModels.BFSInclude();
        result.setFileName(ctx.fileName.getText());
        return result;
    }


    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSFileFieldSpecification visitFieldSpecification(BFSParserParser.FieldSpecificationContext ctx) {
        BFSModels.BFSFileFieldSpecification result = new BFSModels.BFSFileFieldSpecification();
        final BFSParserParser.FieldDeclContext fieldDeclContext = ctx.fieldDecl();
        final BFSParserParser.BlockDefContext blockDefContext = ctx.blockDef();
        final BFSParserParser.BlockDeclContext blockDeclContext = ctx.blockDecl();

        if (fieldDeclContext != null) {
            result.setFieldDecl(Optional.of(this.visitFieldDecl(fieldDeclContext)));
        } else if (blockDefContext != null) {
            result.setBlockDef(Optional.of(this.visitBlockDef(blockDefContext)));
        } else if (blockDeclContext != null) {
            result.setBlockDecl(Optional.of(this.visitBlockDecl(blockDeclContext)));
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }
        return result;
    }


    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSFieldDecl visitFieldDecl(BFSParserParser.FieldDeclContext ctx) {
        BFSModels.BFSFieldDecl result = new BFSModels.BFSFieldDecl();
        result.setFieldName(ctx.fieldName.getText());
        result.setTypeSpec(this.visitTypeSpec(ctx.typeSpec()));
        if (ctx.times != null) {
            result.setTimes(Optional.of(Integer.valueOf(ctx.times.getText())));
        } else {
            result.setTimes(Optional.empty());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSTypeSpec visitTypeSpec(BFSParserParser.TypeSpecContext ctx) {
        BFSModels.BFSTypeSpec result = new BFSModels.BFSTypeSpec();
        result.setLength(Integer.valueOf(ctx.length.getText()));
        result.setUnit(BFSModels.BFSTypeUnit.valueOf(ctx.unit.getText()));
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSBlockDef visitBlockDef(BFSParserParser.BlockDefContext ctx) {
        BFSModels.BFSBlockDef result = new BFSModels.BFSBlockDef();
        result.setBlockName(ctx.blockName.getText());

        List<BFSModels.BFSBlockFieldDecl> blockFieldDecls = Lists.newArrayList();
        List<BFSParserParser.BlockFieldDeclContext> blockFieldDeclCtxs =
                ctx.blockFieldDecl();
        for (BFSParserParser.BlockFieldDeclContext childCtx : blockFieldDeclCtxs) {
            blockFieldDecls.add(this.visitBlockFieldDecl(childCtx));
        }
        result.setBlockFieldDecls(blockFieldDecls);

        // FIXME(zhoujiagen): handle store block definition
//        final Map<String, BFSIRs.BFSBlockDefinition> BLOCK_DEF_MAP = Maps.newHashMap();
//        BLOCK_DEF_MAP.put(result.getBlockName(), result);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSBlockDecl visitBlockDecl(BFSParserParser.BlockDeclContext ctx) {
        BFSModels.BFSBlockDecl result = new BFSModels.BFSBlockDecl();
        result.setBlockName(ctx.blockName.getText());

        List<BFSModels.BFSBlockFieldDecl> blockFieldDecls = Lists.newArrayList();
        List<BFSParserParser.BlockFieldDeclContext> blockFieldDeclCtxs =
                ctx.blockFieldDecl();
        for (BFSParserParser.BlockFieldDeclContext childCtx : blockFieldDeclCtxs) {
            blockFieldDecls.add(this.visitBlockFieldDecl(childCtx));
        }
        result.setBlockFieldDecls(blockFieldDecls);

        if (ctx.times != null) {
            result.setTimes(Optional.of(Integer.valueOf(ctx.times.getText())));
        } else {
            result.setTimes(Optional.empty());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSBlockFieldDecl visitBlockFieldDecl(BFSParserParser.BlockFieldDeclContext ctx) {
        if (ctx.fieldDecl() != null) {
            return this.visitFieldDecl(ctx.fieldDecl());
        } else if (ctx.blockDeclRef() != null) {
            return this.visitBlockDeclRef(ctx.blockDeclRef());
        } else if (ctx.blockImplicitDecl() != null) {
            return this.visitBlockImplicitDecl(ctx.blockImplicitDecl());
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }
    }


    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSBlockDeclRef visitBlockDeclRef(BFSParserParser.BlockDeclRefContext ctx) {
        BFSModels.BFSBlockDeclRef result = new BFSModels.BFSBlockDeclRef();
        result.setBlockName(ctx.blockName.getText());

        if (ctx.aliasName != null) {
            result.setAliasName(Optional.of(ctx.aliasName.getText()));
        } else {
            result.setAliasName(Optional.empty());
        }

        if (ctx.times != null) {
            result.setTimes(Optional.of(Integer.valueOf(ctx.times.getText())));
        } else {
            result.setTimes(Optional.empty());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public BFSModels.BFSBlockImplicitDecl visitBlockImplicitDecl(BFSParserParser.BlockImplicitDeclContext ctx) {
        BFSModels.BFSBlockImplicitDecl result = new BFSModels.BFSBlockImplicitDecl();
        if (ctx.blockName != null) {
            result.setBlockName(Optional.of(ctx.blockName.getText()));
        } else {
            result.setBlockName(Optional.empty());
        }

        List<BFSModels.BFSBlockFieldDecl> blockFieldDecls = Lists.newArrayList();
        List<BFSParserParser.BlockFieldDeclContext> blockFieldDeclCtxs =
                ctx.blockFieldDecl();
        for (BFSParserParser.BlockFieldDeclContext childCtx : blockFieldDeclCtxs) {
            blockFieldDecls.add(this.visitBlockFieldDecl(childCtx));
        }
        result.setBlockFieldDecls(blockFieldDecls);

        if (ctx.times != null) {
            result.setTimes(Optional.of(Integer.valueOf(ctx.times.getText())));
        } else {
            result.setTimes(Optional.empty());
        }
        return result;
    }

}
