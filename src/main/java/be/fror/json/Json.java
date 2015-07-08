/*
 * Copyright 2015 Olivier Grégoire
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.fror.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Olivier Grégoire
 */
public final class Json {

  private Json() {
  }

  private static enum State {

    INITIAL,
    OBJECT_START,
    OBJECT_EXPECT_COLON,
    OBJECT_EXPECT_VALUE,
    OBJECT_AFTER_VALUE,
    OBJECT_EXPECT_STRING,
    ARRAY_START,
    ARRAY_EXPECT_VALUE,
    ARRAY_AFTER_VALUE,
    FINISHED,
    ERROR
  }

  public static void visit(JsonElement element, JsonVisitor visitor) {
    if (element == null || visitor == null) {
      throw new NullPointerException();
    }
    element.accept(visitor);
  }

  public static void write(JsonElement element, Writer writer) throws IOException {
    if (element == null || writer == null) {
      throw new NullPointerException();
    }
    element.toJsonString(writer);
  }

  public static JsonElement parse(Reader reader) throws IOException {
    BasicCallback callback = new BasicCallback();
    parse(reader, callback);
    return callback.getResult();
  }

  public static void parse(Reader reader, JsonCallback callback)
      throws IOException {
    if (reader == null || callback == null) {
      throw new NullPointerException();
    }
    JsonLexer lexer = new JsonLexer(reader);
    State state = State.INITIAL;
    Deque<State> states = new LinkedList<>();
    do {
      JsonToken token = lexer.nextToken();
      switch (state) {
        case INITIAL: {
          switch (token.type) {
            case START_OBJECT: {
              state = State.OBJECT_START;
              states.offerFirst(State.FINISHED);
              callback.objectStart();
              break;
            }
            case START_ARRAY: {
              state = State.ARRAY_START;
              states.offerFirst(State.FINISHED);
              callback.arrayStart();
              break;
            }
            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL: {
              state = State.FINISHED;
              callback.value(valueOf(token));
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case INITIAL

        case OBJECT_START: {
          switch (token.type) {
            case STRING: {
              callback.key(token.text);
              state = State.OBJECT_EXPECT_COLON;
              break;
            }
            case END_OBJECT: {
              state = states.pollFirst();
              callback.objectEnd();
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case OBJECT_START

        case OBJECT_EXPECT_COLON: {
          switch (token.type) {
            case COLON: {
              state = State.OBJECT_EXPECT_VALUE;
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case OBJECT_EXPECT_COLON

        case OBJECT_EXPECT_VALUE: {
          switch (token.type) {
            case START_OBJECT: {
              state = State.OBJECT_START;
              states.offerFirst(State.OBJECT_AFTER_VALUE);
              callback.objectStart();
              break;
            }
            case START_ARRAY: {
              state = State.ARRAY_START;
              states.offerFirst(State.OBJECT_AFTER_VALUE);
              callback.arrayStart();
              break;
            }
            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL: {
              callback.value(valueOf(token));
              state = State.OBJECT_AFTER_VALUE;
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case OBJECT_EXPECT_VALUE

        case OBJECT_AFTER_VALUE: {
          switch (token.type) {
            case COMMA: {
              state = State.OBJECT_EXPECT_STRING;
              break;
            }
            case END_OBJECT: {
              state = states.pollFirst();
              callback.objectEnd();
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case OBJECT_AFTER_VALUE

        case OBJECT_EXPECT_STRING: {
          switch (token.type) {
            case STRING: {
              callback.key(token.text);
              state = State.OBJECT_EXPECT_COLON;
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case OBJECT_EXPECT_STRING

        case ARRAY_START: {
          switch (token.type) {
            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL: {
              callback.value(valueOf(token));
              state = State.ARRAY_AFTER_VALUE;
              break;
            }
            case END_ARRAY: {
              state = states.pollFirst();
              callback.arrayEnd();
              break;
            }
            case START_ARRAY: {
              state = State.ARRAY_START;
              states.offerFirst(State.ARRAY_AFTER_VALUE);
              callback.arrayStart();
              break;
            }
            case START_OBJECT: {
              state = State.OBJECT_START;
              states.offerFirst(State.ARRAY_AFTER_VALUE);
              callback.objectStart();
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case ARRAY_START

        case ARRAY_AFTER_VALUE: {
          switch (token.type) {
            case COMMA: {
              state = State.ARRAY_EXPECT_VALUE;
              break;
            }
            case END_ARRAY: {
              state = states.pollFirst();
              callback.arrayEnd();
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case ARRAY_AFTER_VALUE

        case ARRAY_EXPECT_VALUE: {
          switch (token.type) {
            case STRING:
            case NUMBER:
            case TRUE:
            case FALSE:
            case NULL: {
              callback.value(valueOf(token));
              state = State.ARRAY_AFTER_VALUE;
              break;
            }
            case START_ARRAY: {
              state = State.ARRAY_START;
              states.offerFirst(State.ARRAY_AFTER_VALUE);
              callback.arrayStart();
              break;
            }
            case START_OBJECT: {
              state = State.OBJECT_START;
              states.offerFirst(State.ARRAY_AFTER_VALUE);
              callback.objectStart();
              break;
            }
            default: {
              state = State.ERROR;
              break;
            }
          }
          break;
        } // case ARRAY_EXPECT_VALUE

        case FINISHED: {
          return;
        } // case FINISHED
      }
      if (state == State.ERROR
          || token.type == JsonToken.Type.END_OF_STREAM) {
        throw new JsonParseException(
            JsonParseException.Type.UNEXPECTED_TOKEN,
            token.toString(),
            lexer.getLineNumber(),
            lexer.getColumn());
      }
    } while (true);
  }

  private static JsonElement valueOf(JsonToken token) {
    switch (token.type) {
      case STRING:
        return JsonPrimitive.wrap(token.text);
      case NUMBER:
        return JsonPrimitive.wrap(new BigDecimal(token.text));
      case TRUE:
        return JsonPrimitive.wrap(true);
      case FALSE:
        return JsonPrimitive.wrap(false);
      case NULL:
        return JsonNull.instance();
      default:
        throw new InternalError("Unexpected token to extract value from");
    }
  }

}
