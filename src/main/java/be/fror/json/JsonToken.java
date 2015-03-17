/*
 * Copyright 2015 Olivier Grégoire <fror@users.noreply.github.com>.
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

/**
 * @author Olivier Grégoire &lt;https://github.com/fror&gt;
 */
class JsonToken {

  enum Type {

    START_OBJECT,
    END_OBJECT,
    START_ARRAY,
    END_ARRAY,
    COLON,
    COMMA,
    STRING,
    NUMBER,
    TRUE,
    FALSE,
    NULL,
    END_OF_STREAM
  }

  final Type type;
  final String text;
  final int line;
  final int charBegin;
  final int charEnd;

  JsonToken(Type type, String text, int line, int charBegin, int charEnd) {
    this.type = type;
    this.text = text;
    this.line = line;
    this.charBegin = charBegin;
    this.charEnd = charEnd;
  }

  @Override
  public String toString() {
    return String.format("Token{%s, %s}", this.type, this.text);
  }
}
