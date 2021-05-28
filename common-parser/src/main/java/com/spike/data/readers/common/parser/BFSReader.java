package com.spike.data.readers.common.parser;

import com.google.common.collect.Lists;
import com.spike.data.readers.common.parser.bfs.BFSModels;
import com.spike.data.readers.common.types.Either;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BFSReader extends BFSParserBaseVisitor<BFSModels.BFSBaseModel> {
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
            log.warn("currently not support include: " + ctx.prologue().toStringTree());
        }

        List<BFSModels.BFSFileFieldSpecification> specifications = Lists.newArrayList();
        List<BFSParserParser.FieldSpecificationContext> fieldSpecificationContexts = ctx.fieldSpecification();
        for (BFSParserParser.FieldSpecificationContext fieldSpecificationContext : fieldSpecificationContexts) {
            specifications.add(this.visitFieldSpecification(fieldSpecificationContext));
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
    public BFSModels.BFSBaseModel visitInclude(BFSParserParser.IncludeContext ctx) {
        return visitChildren(ctx);
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
        BFSParserParser.FieldDeclContext fieldDeclContext = ctx.fieldDecl();
        BFSParserParser.BlockDeclContext blockDeclContext = ctx.blockDecl();
        if (fieldDeclContext != null) {
            result.setEither(Either.of(this.visitFieldDecl(fieldDeclContext), null));
        } else if (blockDeclContext != null) {
            result.setEither(Either.of(null, this.visitBlockDecl(blockDeclContext)));
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
            result.setTimes(Integer.valueOf(ctx.times.getText()));
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
//        return visitChildren(ctx);
        // FIXME
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
            result.setTimes(Integer.valueOf(ctx.times.getText()));
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
        // FIXME
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
        // FIXME
        return result;
    }

}
