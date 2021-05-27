grammar BFSParser;
import BFSLexer;

root: prologue? fieldSpecifications;

prologue: include+;

include: INCLUDE fileName=FILE_NAME;

fieldSpecifications: (fieldDecl | blockDecl)+;

fieldDecl:
    fieldName=NAME TYPE_DECL typeSpec times=DIGITS?;

typeSpec:
    length=DIGITS unit=UNIT
    | numericDataType
    | dataAndTimeDataType
    | stringDataType
    | spatialDataType
    | jsonDataType
    | nullDataType
    ;

numericDataType:
    BIT (LP m=DIGITS RP)?                                                                           # bit
    | TINYINT (LP m=DIGITS RP)? UNSIGNED? ZEROFILL?                                                 # tinyint
    | (BOOL | BOOLEAN)                                                                              # bool
    | SMALLINT (LP m=DIGITS RP)? UNSIGNED? ZEROFILL?                                                # smallint
    | MEDIUMINT (LP m=DIGITS RP)? UNSIGNED? ZEROFILL?                                               # mediumint
    | INT (LP m=DIGITS RP)? UNSIGNED? ZEROFILL?                                                     # aint
    | INTEGER (LP m=DIGITS RP)? UNSIGNED? ZEROFILL?                                                 # integer
    | BIGINT (LP m=DIGITS RP)? UNSIGNED? ZEROFILL?                                                  # bigint
    | (DECIMAL|DEC|NUMERIC|FIXED) (LP m=DIGITS (COMMA d=DIGITS)?  RP)? UNSIGNED? ZEROFILL?          # decimal
    | FLOAT LP p=DIGITS RP UNSIGNED? ZEROFILL?                                                      # floatPrecision
    | FLOAT (LP m=DIGITS (COMMA d=DIGITS)?  RP)? UNSIGNED? ZEROFILL?                                # afloat
    | DOUBLE PRECISION (LP m=DIGITS COMMA d=DIGITS RP)? UNSIGNED? ZEROFILL?                         # doublePrecision
    | DOUBLE (LP m=DIGITS COMMA d=DIGITS RP)? UNSIGNED? ZEROFILL?                                   # adouble
    | REAL (LP m=DIGITS COMMA d=DIGITS RP)? UNSIGNED? ZEROFILL?                                     # real
    ;

dataAndTimeDataType:
    DATE                                # date
    | DATETIME (LP fsp=DIGITS RP)?       # datetime
    | TIMESTAMP (LP fsp=DIGITS RP)?      # timestamp
    | TIME (LP fsp=DIGITS RP)?           # time
    | YEAR '(4)'?                       # year
    ;

stringDataType:
    NATIONAL? CHAR (LP m=DIGITS RP)? (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?            # achar
    | NATIONAL? VARCHAR LP m=DIGITS RP (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?          # varchar
    | BINARY (LP m=DIGITS RP)?                                                                                  # binary
    | VARBINARY LP m=DIGITS RP                                                                                  # varbinary
    | TINYBLOB                                                                                                  # tinyblob
    | TINYTEXT (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?                                  # tinytext
    | BLOB (LP m=DIGITS RP)?                                                                                    # blob
    | TEXT (LP m=DIGITS RP)? (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?                    # text
    | MEDIUMBLOB                                                                                                # mediumblob
    | MEDIUMTEXT (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?                                # mediumtext
    | LONGBLOB                                                                                                  # longblob
    | LONGTEXT (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?                                  # longtext
    | ENUM LP SQ values+=NAME SQ (COMMA SQ values+=NAME)* SQ RP (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?   # aenum
    | SET LP SQ values+=NAME SQ (COMMA SQ values+=NAME)* SQ RP (CHARACTER_SET charsetName=NAME)? (COLLATE collationName=NAME)?    # set
    ;

spatialDataType:
    GEOMETRY                    # geometry
    | POINT                     # point
    | LINESTRING                # linestring
    | POLYGON                   # polygon
    | MULTIPOINT                # multipoint
    | MULTILINESTRING           # multilinestring
    | MULTIPOLYGON              # multipolygon
    | GEOMETRYCOLLECTION        # geometrycollection
    ;

jsonDataType: JSON;

nullDataType: NULL;

blockDecl:
    BLOCK blockName=NAME  '{' (fieldDecl | blockDeclRef | blockImplicitDecl)+ '}';

blockDeclRef:
    BLOCK blockName=NAME times=DIGITS?;

blockImplicitDecl:
    BLOCK blockName=NAME? '{' fieldDecl | blockDecl '}';
