grammar TestBinaryFileSpecificationParser;
import TestBinaryFileSpecificationLexer;

root: prologue? fieldSpecifications;

prologue: include+;

include: INCLUDE fileName=FILE_NAME;

fieldSpecifications: (fieldDecl | blockDecl)+;

fieldDecl:
    FIELD fieldName=NAME TYPE_DECL typeSpec times=INT?;

typeSpec:
    length=INT unit=UNIT;

blockDecl:
    BLOCK blockName=NAME  '{' (fieldDecl | blockDeclRef | blockImplicitDecl)+ '}';

blockDeclRef:
    BLOCK blockName=NAME times=INT?;

blockImplicitDecl:
    BLOCK blockName=NAME? '{' fieldDecl | blockDecl '}';
