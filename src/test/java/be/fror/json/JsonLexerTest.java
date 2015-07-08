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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Olivier Grégoire
 */
public class JsonLexerTest {

  @Test
  public void testNextToken() throws IOException {
    JsonLexer lexer = new JsonLexer(new StringReader("{}"));
    while (lexer.nextToken().type != JsonToken.Type.END_OF_STREAM) {
    }
  }

  @Test
  public void testException() throws IOException {
    testForException("{}ab", "a", 0, 2);
  }

  private void testForException(String malformedJson, String expectedBadCharacter, int expectedLine, int expectedColumn) throws IOException {
    try {
      JsonLexer lexer = new JsonLexer(new StringReader(malformedJson));
      while (lexer.nextToken().type != JsonToken.Type.END_OF_STREAM) {
      }
      fail();
    } catch (JsonParseException e) {
      assertThat(e.getType(), equalTo(JsonParseException.Type.UNEXPECTED_CHAR));
      assertThat(e.getText(), equalTo(expectedBadCharacter));
      assertThat(e.getLineNumber(), equalTo(expectedLine));
      assertThat(e.getColumn(), equalTo(expectedColumn));
    }
  }
}
