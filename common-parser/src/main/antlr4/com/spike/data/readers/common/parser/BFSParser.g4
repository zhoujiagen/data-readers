grammar BFSParser;
import BFSLexer;


root: prologue? fieldSpecification+;

prologue: include+ define?;

include: INCLUDE fileName=FILE_NAME;

define: DEFINE constName=NAME '=' length=DIGITS unit=UNIT?;

fieldSpecification:
    fieldDecl
    | blockDef
    | blockDecl;

fieldDecl:
    fieldName=NAME TYPE_DECL typeSpec times=DIGITS?;

typeSpec:
    | ELLIPSIS
    | length=DIGITS unit=UNIT
    ;

blockDef:
    TYPE_DEF BLOCK blockName=NAME '{' blockFieldDecl+ '}' (TYPE_DECL valueRef)?;

blockDecl:
    BLOCK blockName=NAME '{' blockFieldDecl+ '}' times=DIGITS?;

blockFieldDecl:
    fieldDecl
    | blockDeclRef
    | blockImplicitDecl
    ;

blockDeclRef:
    BLOCK blockName=NAME aliasName=NAME? (valueRef? | times=DIGITS?);

valueRef:
    '$[' NAME (DOT NAME)* ']'
    | '${' constValue=NAME '}'
    ;

blockImplicitDecl:
    BLOCK blockName=NAME? '{' blockFieldDecl+ '}' times=DIGITS?;
