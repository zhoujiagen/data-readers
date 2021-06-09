grammar BFSParser;
import BFSLexer;


root: prologue* specification+;

// 序言
prologue:
    INCLUDE fileName=FILE_NAME                                       #include
    | DEFINE constName=NAME '=' constValue=DIGITS                    #define
    ;

// 规格描述
specification:
    blockDef
    | blockDecl;

// 块定义
blockDef:
    TYPE_DEF BLOCK blockName=NAME '{' blockFieldDecl+ '}' (TYPE_DECL blockTypeSpec)?;

// 块声明
blockDecl:
    BLOCK blockName=NAME '{' blockFieldDecl+ '}' times=DIGITS?;

// 块中字段声明
blockFieldDecl:
    fieldDecl
    | blockRef
    | blockImplicitDecl
    ;

// 字段声明
fieldDecl:
    fieldName=NAME TYPE_DECL fieldTypeSpec;

// 块引用
blockRef:
    BLOCK blockName=NAME aliasName=NAME? (TYPE_DECL blockRefTypeSpec)?;

// 块隐式申明
blockImplicitDecl:
    BLOCK blockName=NAME? '{' blockFieldDecl+ '}' times=DIGITS?;

// 字段的类型: ..., 2B, 2B 2
fieldTypeSpec:
    ELLIPSIS                                    #fieldTypeSpecAny
    | length=DIGITS unit=UNIT times=DIGITS?     #fieldTypeSpecFixed
    ;

// 块的类型: 16KB, ${VAR}KB
blockTypeSpec:
    length=DIGITS unit=UNIT                     #blockTypeSpecFixed
    | constValueRef unit=UNIT                   #blockTypeSpecRefed
    ;

// 块引用的类型: ..., 2, $[field]
blockRefTypeSpec:
    ELLIPSIS                                    #blockRefTypeSpecAny
    | times=DIGITS                              #blockRefTypeSpecFixed
    | fieldValueRef                             #blockRefTypeSpecRefed
    ;

// 字段值引用: $[field]
fieldValueRef: '$[' valueNames+=NAME (DOT valueNames+=NAME)* ']';
// 常量值引用: ${VAR}
constValueRef: '${' constValue=NAME '}';


