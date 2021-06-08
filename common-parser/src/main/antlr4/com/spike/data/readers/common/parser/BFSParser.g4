grammar BFSParser;
import BFSLexer;

root: prologue? fieldSpecification+;

prologue: include+;

include: INCLUDE fileName=FILE_NAME CLAUSE_END;

fieldSpecification:
    fieldDecl
    | blockDef
    | blockDecl;

fieldDecl:
    fieldName=NAME TYPE_DECL typeSpec times=DIGITS?;

typeSpec:
    length=DIGITS unit=UNIT;

blockDef:
    TYPE_DEF BLOCK blockName=NAME '{' blockFieldDecl+ '}';

blockDecl:
    BLOCK blockName=NAME  '{' blockFieldDecl+ '}' times=DIGITS?;

blockFieldDecl:
    fieldDecl
    | blockDeclRef
    | blockImplicitDecl
    ;

blockDeclRef:
    BLOCK blockName=NAME aliasName=NAME? times=DIGITS?;

blockImplicitDecl:
    BLOCK blockName=NAME? '{' blockFieldDecl+ '}' times=DIGITS?;
