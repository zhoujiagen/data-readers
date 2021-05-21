
lexer grammar TestBinaryFileSpecificationLexer;

SPACE: [ \t\r\n]+    -> channel(HIDDEN);
COMMENT_INPUT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: (('-- ' | '#' | '//') ~[\r\n]* ('\r'? '\n' | EOF)
                | '--' ('\r'? '\n' | EOF)) -> channel(HIDDEN);




// 文件后缀
FILE_SUFFIX: '.bff';
FILE_NAME: REGUALR_NAME_SYMBOL+ FILE_SUFFIX;

// 关键字
INCLUDE: '-INCLUDE';
BLOCK: 'BLOCK';

// 符号
CLAUSE_END: ';' -> channel(HIDDEN);
TYPE_DECL: ':';

// 长度单位
UNIT: 'B' // byte
    | 'b' // bit
    | [lL]'B' // byte, little endian
    | [bB]'B' // byte, big endian
    ;

// 名称
NAME: [a-zA-Z][0-9a-zA-Z_\-]+[0-9a-zA-Z];

INT: DIGIT+;
fragment DIGIT: [0-9];
fragment REGUALR_NAME_SYMBOL: [0-9a-zA-Z_\-];