package com.spike.data.readers.common.parser;

import com.google.common.collect.Maps;
import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TestBFSReader {
    static final Map<Path, String> errorMsgCollector = Maps.newHashMap();

    static ANTLRErrorStrategy errorStrategy = new DefaultErrorStrategy() {
    };

    static class MyErrorListener extends BaseErrorListener {
        private Path path;

        public MyErrorListener(Path path) {
            this.path = path;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                int charPositionInLine, String msg, RecognitionException e) {
            errorMsgCollector.put(path, msg);
        }
    }

    ;

    public static void main(String[] args) throws IOException {
        System.out.println(Paths.get(".").toFile().getAbsolutePath());
        Path path = Paths.get("mysql/src/main/bfs/frmFile.bfs");
        CharStream rawCS = CharStreams.fromPath(path);
        CaseChangingCharStream cs = new CaseChangingCharStream(rawCS, true);
        BFSParserLexer lexer = new BFSParserLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BFSParserParser parser = new BFSParserParser(tokens);

        parser.setErrorHandler(errorStrategy);
        parser.addErrorListener(new MyErrorListener(path));

//        ParseTree tree = parser.root();
//        System.out.println(tree.toStringTree(parser));

        BFSParserParser.RootContext rootContext = parser.root();
        BFSReader visitor = new BFSReader();
        visitor.visitRoot(rootContext);
    }
}
