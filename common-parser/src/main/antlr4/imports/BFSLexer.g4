
lexer grammar BFSLexer;

SPACE: [ \t\r\n]+    -> channel(HIDDEN);
COMMENT_INPUT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: (('-- ' | '#' | '//') ~[\r\n]* ('\r'? '\n' | EOF)
                | '--' ('\r'? '\n' | EOF)) -> channel(HIDDEN);




// 文件后缀
FILE_SUFFIX: '.BFS';
FILE_NAME: REGUALR_NAME_SYMBOL+ FILE_SUFFIX;

// 关键字
INCLUDE: '-INCLUDE';
DEFINE: 'DEFINE';
BLOCK: 'BLOCK';
TYPE_DEF: 'TYPEDEF';

// 类型: MYSQL
// numeric data types
BIT: 'BIT';
TINYINT: 'TINYINT';
BOOL: 'BOOL';
BOOLEAN: 'BOOLEAN';
SMALLINT: 'SMALLINT';
MEDIUMINT: 'MEDIUMINT';
INT: 'INT';
INTEGER: 'INTEGER';
BIGINT: 'BIGINT';
DECIMAL: 'DECIMAL';
DEC: 'DEC';
NUMERIC: 'NUMERIC';
FIXED: 'FIXED';
FLOAT: 'FLOAT';
DOUBLE: 'DOUBLE';
REAL: 'REAL';
// data and time data types
DATE: 'DATE';
DATETIME: 'DATETIME';
TIMESTAMP: 'TIMESTAMP';
TIME: 'TIME';
YEAR: 'YEAR';
// string data types
CHAR: 'CHAR';
VARCHAR: 'VARCHAR';
BINARY: 'BINARY';
VARBINARY: 'VARBINARY';
TINYBLOB: 'TINYBLOB';
TINYTEXT: 'TINYTEXT';
BLOB: 'BLOB';
TEXT: 'TEXT';
MEDIUMBLOB: 'MEDIUMBLOB';
MEDIUMTEXT: 'MEDIUMTEXT';
LONGBLOB: 'LONGBLOB';
LONGTEXT: 'LONGTEXT';
ENUM: 'ENUM';
SET: 'SET';
// spatial data types
GEOMETRY: 'GEOMETRY';
POINT: 'POINT';
LINESTRING: 'LINESTRING';
POLYGON: 'POLYGON';
MULTIPOINT: 'MULTIPOINT';
MULTILINESTRING: 'MULTILINESTRING';
MULTIPOLYGON: 'MULTIPOLYGON';
GEOMETRYCOLLECTION: 'GEOMETRYCOLLECTION';
// JSON
JSON: 'JSON';
// null
NULL: 'NULL';

// 类型描述符
UNSIGNED: 'UNSIGNED';
ZEROFILL: 'ZEROFILL';
PRECISION: 'PRECISION';
NATIONAL: 'NATIONAL';
CHARACTER_SET: 'CHARACTER SET';
COLLATE: 'COLLATE';

// 符号
CLAUSE_END: ';' -> channel(HIDDEN);
TYPE_DECL: ':';

// 长度单位
UNIT: 'B' // byte
    | 'b' // bit
    | [lL]'B' // byte, little endian
    | [bB]'B' // byte, big endian
    | [kK]'B'
    | [mM]'B'
    ;

// 名称
NAME: [a-zA-Z][0-9a-zA-Z_\-]*;

LP: '(';    // parenthesis
RP: ')';
LB: '[';    // bracket
RB: ']';
SQ: '\'';   // single quotes
DQ: '"';    // double quotes
COMMA: ',';
ELLIPSIS: '...';
DOT: '.';

DIGITS: DIGIT+;
fragment DIGIT: [0-9];
fragment REGUALR_NAME_SYMBOL: [0-9a-zA-Z_\-];
