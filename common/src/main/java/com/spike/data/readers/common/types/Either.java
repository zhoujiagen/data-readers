package com.spike.data.readers.common.types;

import com.google.common.base.Preconditions;

public final class Either<L, R> {
    private L l;
    private R r;

    private Either(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public static <L, R> Either<L, R> of(L l, R r) {
        Preconditions.checkArgument(
                (l == null && r != null) || (l != null && r == null),
                "invalid Either argument");
        return new Either<>(l, r);
    }

    public boolean isLeft() {
        return l != null;
    }

    public L getLeft() {
        return l;
    }

    public boolean isRight() {
        return r != null;
    }

    public R getRight() {
        return r;
    }
}
