package be.fror.json;

%%
%class JsonLexer
%line
%column
%char
%function nextToken
%type JsonToken
%yylexthrow JsonParseException
%eofval{
  return new JsonToken(JsonToken.Type.END_OF_STREAM, "", yyline, yychar, yychar);
%eofval}
%{
   private final StringBuilder string = new StringBuilder();
   int getCharOffset() { return yychar; }
   int getLineNumber() { return yyline; }
   int getColumn() { return yycolumn; }
   private JsonParseException newUnexpectedCharException() { return new JsonParseException(JsonParseException.Type.UNEXPECTED_CHAR, yytext(), yyline, yycolumn); }
   private JsonToken createToken(JsonToken.Type type) { return new JsonToken(type, yytext(), yyline, yychar, yychar + yylength()); }
%}
%state STRING
WHITE_SPACE_CHAR = [\n\r\ \t\b\012]
NUMBER_TEXT      = -?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?
ANY_CHAR         = .
%%
<YYINITIAL> {
  ","                { return createToken(JsonToken.Type.COMMA);        }
  ":"                { return createToken(JsonToken.Type.COLON);        }
  "["                { return createToken(JsonToken.Type.START_ARRAY);  }
  "]"                { return createToken(JsonToken.Type.END_ARRAY);    }
  "{"                { return createToken(JsonToken.Type.START_OBJECT); }
  "}"                { return createToken(JsonToken.Type.END_OBJECT);   }
  "true"             { return createToken(JsonToken.Type.TRUE);         }
  "false"            { return createToken(JsonToken.Type.FALSE);        }
  "null"             { return createToken(JsonToken.Type.NULL);         }
  \"                 { string.setLength(0); yybegin(STRING);            }
  {NUMBER_TEXT}      { return createToken(JsonToken.Type.NUMBER);       } 
  {WHITE_SPACE_CHAR} {                                                  }
  {ANY_CHAR}         { throw newUnexpectedCharException();              }
}

<STRING> {
  \"                 { yybegin(YYINITIAL); return (new JsonToken(JsonToken.Type.STRING, string.toString(), yyline, yychar, yychar+string.length()));}
  [^\n\r\"\\]+       { string.append(yytext()); }
  \\\"               { string.append('\"');     }
  \\\\               { string.append('\\');     }
  \\\/               { string.append('/');      }
  \\b                { string.append('\b');     }
  \\f                { string.append('\f');     }
  \\n                { string.append('\n');     }
  \\r                { string.append('\r');     }
  \\t                { string.append('\t');     }
  \\u[0-9A-Fa-f]{4}  { string.append(Character.toChars(Integer.parseInt(yytext().substring(2),16))); }
}
