package com.spike.data.readers.common.parser.bfs;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spike.data.readers.common.parser.BFSParserBaseVisitor;
import com.spike.data.readers.common.parser.BFSParserLexer;
import com.spike.data.readers.common.parser.BFSParserParser;
import com.spike.data.readers.common.parser.CaseChangingCharStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

class BFSParser extends BFSParserBaseVisitor<BFSModels.Base> {
    private static final Logger LOG = LoggerFactory.getLogger(BFSParser.class);
    private static final Gson gson = new GsonBuilder()
//            .serializeNulls()
//            .setPrettyPrinting()
            .create();

    BFSIRs.BFSFileUnit parse(Path path) throws IOException {
        BFSIRs.BFSFileUnit result = this.parse(path, null);

        if (LOG.isDebugEnabled()) {
            LOG.debug("CONTEXT: {}{}", System.lineSeparator(), gson.toJson(BFSIRs.PARSE_CONTEXT));
        }

        return result;
    }

    private BFSIRs.BFSFileUnit parse(Path bfsFilePath, String trace) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Parse {} [{}]", bfsFilePath.toString(), Strings.nullToEmpty(trace));
        }
        if (trace == null || trace.trim().equals("")) {
            trace = bfsFilePath.getFileName().toString();
        }

        CharStream rawCS = CharStreams.fromPath(bfsFilePath);
        CaseChangingCharStream cs = new CaseChangingCharStream(rawCS, true);
        BFSParserLexer lexer = new BFSParserLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BFSParserParser parser = new BFSParserParser(tokens);

        // FIXME(zhoujiagen) remove ambiguity
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
//        parser.removeErrorListeners();
        parser.addErrorListener(new DiagnosticErrorListener());
        parser.setErrorHandler(new DefaultErrorStrategy());

        BFSParserParser.RootContext rootContext = parser.root();
        BFSIRs.BFSFileUnit result = this.visitRoot(rootContext);
        result.setFileName(bfsFilePath.getFileName().toString());

        // parse included
        if (result.getIncludes().isPresent()) {
            for (BFSModels.Include includeFile : result.getIncludes().get()) {
                String nextTrace = trace + " => " + includeFile.getFileName();
                this.parse(bfsFilePath.getParent().resolve(includeFile.getFileName()), nextTrace);
            }
        }
        BFSIRs.PARSE_CONTEXT.registerBFSFileUnit(bfsFilePath, result);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parse {} [{}] Result: {}{}",
                    bfsFilePath.toString(),
                    Strings.nullToEmpty(trace),
                    System.lineSeparator(),
                    gson.toJson(result));
        }

        return result;
    }


    @Override
    public BFSIRs.BFSFileUnit visitRoot(BFSParserParser.RootContext ctx) {
        BFSIRs.BFSFileUnit result = new BFSIRs.BFSFileUnit();

        if (ctx.prologue() != null) {
            List<BFSModels.Include> includes = Lists.newArrayList();
            List<BFSModels.Define> defines = Lists.newArrayList();
            List<BFSParserParser.PrologueContext> prologueContexts = ctx.prologue();
            for (BFSParserParser.PrologueContext prologueContext : prologueContexts) {
                if (prologueContext instanceof BFSParserParser.IncludeContext) {
                    includes.add(this.visitInclude((BFSParserParser.IncludeContext) prologueContext));
                } else if (prologueContext instanceof BFSParserParser.DefineContext) {
                    defines.add(this.visitDefine((BFSParserParser.DefineContext) prologueContext));
                } else {
                    throw new RuntimeException(ctx.toStringTree());
                }
            }

            if (includes.isEmpty()) {
                result.setIncludes(Optional.empty());
            } else {
                result.setIncludes(Optional.of(includes));
            }
            if (defines.isEmpty()) {
                result.setDefines(Optional.empty());
            } else {
                result.setDefines(Optional.of(defines));
            }
        } else {
            result.setIncludes(Optional.empty());
            result.setDefines(Optional.empty());
        }


        List<BFSModels.Specification> specifications = Lists.newArrayList();
        List<BFSParserParser.SpecificationContext> specificationContexts =
                ctx.specification();
        for (BFSParserParser.SpecificationContext specificationContext :
                specificationContexts) {
            specifications.add(this.visitSpecification(specificationContext));
        }
        result.setSpecifications(specifications);

        return result;
    }


    @Override
    public BFSModels.Include visitInclude(BFSParserParser.IncludeContext ctx) {
        BFSModels.Include result = new BFSModels.Include();
        result.setFileName(ctx.fileName.getText());
        return result;
    }


    @Override
    public BFSModels.Define visitDefine(BFSParserParser.DefineContext ctx) {
        BFSModels.Define result = new BFSModels.Define();
        result.setConstName(ctx.constName.getText());
        result.setConstValue(ctx.constValue.getText());
        return result;
    }


    @Override
    public BFSModels.Specification visitSpecification(BFSParserParser.SpecificationContext ctx) {
        BFSModels.Specification result = null;
        if (ctx.blockDef() != null) {
            result = this.visitBlockDef(ctx.blockDef());
        } else if (ctx.blockDecl() != null) {
            result = this.visitBlockDecl(ctx.blockDecl());
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }

        BFSIRs.PARSE_CONTEXT.registerSpecification(result);
        return result;
    }


    @Override
    public BFSModels.BlockDef visitBlockDef(BFSParserParser.BlockDefContext ctx) {
        BFSModels.BlockDef result = new BFSModels.BlockDef();
        result.setBlockName(ctx.blockName.getText());

        List<BFSModels.BlockFieldDecl> blockFieldDecls = Lists.newArrayList();
        List<BFSParserParser.BlockFieldDeclContext> blockFieldDeclCtxs =
                ctx.blockFieldDecl();
        for (BFSParserParser.BlockFieldDeclContext childCtx : blockFieldDeclCtxs) {
            blockFieldDecls.add(this.visitBlockFieldDecl(childCtx));
        }
        result.setBlockFieldDecls(blockFieldDecls);

        if (ctx.blockTypeSpec() != null) {
            result.setBlockTypeSpec(Optional.of(this.visitBlockTypeSpec(ctx.blockTypeSpec())));
        } else {
            result.setBlockTypeSpec(Optional.empty());
        }

        // FIXME(zhoujiagen): handle store block definition
//        final Map<String, BFSIRs.BFSBlockDefinition> BLOCK_DEF_MAP = Maps.newHashMap();
//        BLOCK_DEF_MAP.put(result.getBlockName(), result);
        return result;
    }


    @Override
    public BFSModels.BlockDecl visitBlockDecl(BFSParserParser.BlockDeclContext ctx) {
        BFSModels.BlockDecl result = new BFSModels.BlockDecl();
        result.setBlockName(ctx.blockName.getText());

        List<BFSModels.BlockFieldDecl> blockFieldDecls = Lists.newArrayList();
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


    @Override
    public BFSModels.BlockFieldDecl visitBlockFieldDecl(BFSParserParser.BlockFieldDeclContext ctx) {
        if (ctx.fieldDecl() != null) {
            return this.visitFieldDecl(ctx.fieldDecl());
        } else if (ctx.blockRef() != null) {
            return this.visitBlockRef(ctx.blockRef());
        } else if (ctx.blockImplicitDecl() != null) {
            return this.visitBlockImplicitDecl(ctx.blockImplicitDecl());
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }
    }

    @Override
    public BFSModels.FieldDecl visitFieldDecl(BFSParserParser.FieldDeclContext ctx) {
        BFSModels.FieldDecl result = new BFSModels.FieldDecl();
        result.setFieldName(ctx.fieldName.getText());
        result.setFieldTypeSpec(this.visitFieldTypeSpec(ctx.fieldTypeSpec()));
        return result;
    }

    @Override
    public BFSModels.BlockRef visitBlockRef(BFSParserParser.BlockRefContext ctx) {
        BFSModels.BlockRef result = new BFSModels.BlockRef();
        result.setBlockName(ctx.blockName.getText());

        if (ctx.aliasName != null) {
            result.setAliasName(Optional.of(ctx.aliasName.getText()));
        } else {
            result.setAliasName(Optional.empty());
        }

        if (ctx.blockRefTypeSpec() != null) {
            result.setBlockRefTypeSpec(Optional.of(this.visitBlockRefTypeSpec(ctx.blockRefTypeSpec())));
        } else {
            result.setBlockRefTypeSpec(Optional.empty());
        }

        return result;
    }


    @Override
    public BFSModels.BlockImplicitDecl visitBlockImplicitDecl(BFSParserParser.BlockImplicitDeclContext ctx) {
        BFSModels.BlockImplicitDecl result = new BFSModels.BlockImplicitDecl();
        if (ctx.blockName != null) {
            result.setBlockName(Optional.of(ctx.blockName.getText()));
        } else {
            result.setBlockName(Optional.empty());
        }

        List<BFSModels.BlockFieldDecl> blockFieldDecls = Lists.newArrayList();
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

    private BFSModels.FieldTypeSpec visitFieldTypeSpec(BFSParserParser.FieldTypeSpecContext ctx) {

        if (ctx instanceof BFSParserParser.FieldTypeSpecAnyContext) {
            return this.visitFieldTypeSpecAny((BFSParserParser.FieldTypeSpecAnyContext) ctx);
        } else if (ctx instanceof BFSParserParser.FieldTypeSpecFixedContext) {
            return this.visitFieldTypeSpecFixed((BFSParserParser.FieldTypeSpecFixedContext) ctx);
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }
    }

    @Override
    public BFSModels.FieldTypeSpecAny visitFieldTypeSpecAny(BFSParserParser.FieldTypeSpecAnyContext ctx) {
        return new BFSModels.FieldTypeSpecAny();
    }

    @Override
    public BFSModels.FieldTypeSpecFixed visitFieldTypeSpecFixed(BFSParserParser.FieldTypeSpecFixedContext ctx) {
        BFSModels.FieldTypeSpecFixed result = new BFSModels.FieldTypeSpecFixed();
        result.setLength(Integer.parseInt(ctx.length.getText()));
        result.setUnit(BFSModels.Unit.of(ctx.unit.getText()));
        if (ctx.times != null) {
            result.setTimes(Optional.of(Integer.parseInt(ctx.times.getText())));
        } else {
            result.setTimes(Optional.empty());
        }
        return result;
    }

    private BFSModels.BlockTypeSpec visitBlockTypeSpec(BFSParserParser.BlockTypeSpecContext ctx) {
        if (ctx instanceof BFSParserParser.BlockTypeSpecFixedContext) {
            return this.visitBlockTypeSpecFixed((BFSParserParser.BlockTypeSpecFixedContext) ctx);
        } else if (ctx instanceof BFSParserParser.BlockTypeSpecRefedContext) {
            return this.visitBlockTypeSpecRefed((BFSParserParser.BlockTypeSpecRefedContext) ctx);
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }
    }

    @Override
    public BFSModels.BlockTypeSpecFixed visitBlockTypeSpecFixed(BFSParserParser.BlockTypeSpecFixedContext ctx) {
        BFSModels.BlockTypeSpecFixed result = new BFSModels.BlockTypeSpecFixed();
        result.setLength(Integer.parseInt(ctx.length.getText()));
        result.setUnit(BFSModels.Unit.of(ctx.unit.getText()));
        return result;
    }


    @Override
    public BFSModels.BlockTypeSpecRefed visitBlockTypeSpecRefed(BFSParserParser.BlockTypeSpecRefedContext ctx) {
        BFSModels.BlockTypeSpecRefed result = new BFSModels.BlockTypeSpecRefed();
        result.setConstValueRef(this.visitConstValueRef(ctx.constValueRef()));
        result.setUnit(BFSModels.Unit.of(ctx.unit.getText()));
        return result;
    }

    private BFSModels.BlockRefTypeSpec visitBlockRefTypeSpec(BFSParserParser.BlockRefTypeSpecContext ctx) {
        if (ctx instanceof BFSParserParser.BlockRefTypeSpecAnyContext) {
            return this.visitBlockRefTypeSpecAny((BFSParserParser.BlockRefTypeSpecAnyContext) ctx);
        } else if (ctx instanceof BFSParserParser.BlockRefTypeSpecFixedContext) {
            return this.visitBlockRefTypeSpecFixed((BFSParserParser.BlockRefTypeSpecFixedContext) ctx);
        } else if (ctx instanceof BFSParserParser.BlockRefTypeSpecRefedContext) {
            return this.visitBlockRefTypeSpecRefed((BFSParserParser.BlockRefTypeSpecRefedContext) ctx);
        } else {
            throw new RuntimeException(ctx.toStringTree());
        }
    }


    @Override
    public BFSModels.BlockRefTypeSpecAny visitBlockRefTypeSpecAny(BFSParserParser.BlockRefTypeSpecAnyContext ctx) {
        return new BFSModels.BlockRefTypeSpecAny();
    }


    @Override
    public BFSModels.BlockRefTypeSpecFixed visitBlockRefTypeSpecFixed(BFSParserParser.BlockRefTypeSpecFixedContext ctx) {
        BFSModels.BlockRefTypeSpecFixed result = new BFSModels.BlockRefTypeSpecFixed();
        result.setTimes(Integer.parseInt(ctx.times.getText()));
        return result;
    }


    @Override
    public BFSModels.BlockRefTypeSpecRefed visitBlockRefTypeSpecRefed(BFSParserParser.BlockRefTypeSpecRefedContext ctx) {
        BFSModels.BlockRefTypeSpecRefed result = new BFSModels.BlockRefTypeSpecRefed();
        result.setFieldValueRef(this.visitFieldValueRef(ctx.fieldValueRef()));
        return result;
    }

    @Override
    public BFSModels.FieldValueRef visitFieldValueRef(BFSParserParser.FieldValueRefContext ctx) {
        BFSModels.FieldValueRef result = new BFSModels.FieldValueRef();
        List<String> valueNames = Lists.newArrayList();
        for (Token token : ctx.valueNames) {
            valueNames.add(token.getText());
        }
        result.setValueNames(valueNames);
        return result;
    }

    @Override
    public BFSModels.ConstValueRef visitConstValueRef(BFSParserParser.ConstValueRefContext ctx) {
        BFSModels.ConstValueRef result = new BFSModels.ConstValueRef();
        result.setConstValue(ctx.constValue.getText());
        return result;
    }
}
