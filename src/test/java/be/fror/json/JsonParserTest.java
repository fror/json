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
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Olivier Grégoire
 */
public class JsonParserTest {

  @Test
  public void testValues() {
    testParse(JsonPrimitive.wrap(1), "1");
    testParse(JsonPrimitive.wrap(new BigDecimal("1.2")), "1.2");
    testParse(JsonPrimitive.wrap("a"), "\"a\"");
    testParse(JsonPrimitive.wrap(true), "true");
    testParse(JsonPrimitive.wrap(false), "false");
    testParse(JsonNull.instance(), "null");
  }

  @Test
  public void testSingleLevelArray() {
    JsonArray array;
    {
      array = new JsonArray();
      testParse(array, "[]");
    }
    {
      array = new JsonArray();
      array.add(JsonPrimitive.wrap("a"));
      testParse(array, "[\"a\"]");
    }
    {
      array = new JsonArray();
      array.add(JsonPrimitive.wrap("a"));
      array.add(JsonPrimitive.wrap("b"));
      testParse(array, "[\"a\",\"b\"]");
    }
  }

  @Test
  public void testArrayInArray() {
    JsonArray parent, child;
    {
      parent = new JsonArray();
      child = new JsonArray();
      parent.add(child);
      testParse(parent, "[[]]");
    }
    {
      parent = new JsonArray();
      child = new JsonArray();
      child.add(JsonPrimitive.wrap("a"));
      parent.add(child);
      testParse(parent, "[[\"a\"]]");
    }
    {
      parent = new JsonArray();
      child = new JsonArray();
      child.add(JsonPrimitive.wrap("a"));
      child.add(JsonPrimitive.wrap("b"));
      parent.add(child);
      testParse(parent, "[[\"a\",\"b\"]]");
    }
    {
      parent = new JsonArray();
      child = new JsonArray();
      child.add(JsonPrimitive.wrap("a"));
      parent.add(child);
      child = new JsonArray();
      child.add(JsonPrimitive.wrap("b"));
      parent.add(child);
      testParse(parent, "[[\"a\"],[\"b\"]]");
    }
  }

  @Test
  public void testSingleLevelObject() {
    JsonObject object;
    {
      object = new JsonObject();
      testParse(object, "{}");
    }
    {
      object = new JsonObject();
      object.add("a", JsonPrimitive.wrap("b"));
      testParse(object, "{\"a\":\"b\"}");
    }
    {
      object = new JsonObject();
      object.add("a", JsonPrimitive.wrap("b"));
      object.add("c", JsonPrimitive.wrap("d"));
      testParse(object, "{\"a\":\"b\",\"c\":\"d\"}");
    }
  }

  @Test
  public void testObjectInObject() {
    JsonObject parent, child;
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.add("a", child);
      testParse(parent, "{\"a\":{}}");
    }
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.add("a", child);
      child.add("a", JsonPrimitive.wrap("b"));
      testParse(parent, "{\"a\":{\"a\":\"b\"}}");
    }
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.add("a", child);
      child.add("a", JsonPrimitive.wrap("b"));
      child.add("c", JsonPrimitive.wrap("d"));
      testParse(parent, "{\"a\":{\"a\":\"b\",\"c\":\"d\"}}");
    }
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.add("a", child);
      child = new JsonObject();
      parent.add("b", child);
      testParse(parent, "{\"a\":{},\"b\":{}}");
    }
  }

  @Test
  public void testCompleteObject() throws Exception {
    String json = "{\"a\":{\"b\":[\"c\",1,23,1.23e45],\"d\":false,\"e\":true},\"f\":[1,2,3,null,{\"g\":[{}]},[1]],\"h\":[]}";
    JsonObject o1 = new JsonObject();
    JsonObject o2 = new JsonObject();
    o1.add("a", o2);
    JsonArray a1 = new JsonArray();
    o2.add("b", a1);
    a1.add(JsonPrimitive.wrap("c"));
    a1.add(JsonPrimitive.wrap(BigInteger.ONE));
    a1.add(JsonPrimitive.wrap(BigInteger.valueOf(23)));
    a1.add(JsonPrimitive.wrap(new BigDecimal("1.23e45")));
    o2.add("d", JsonPrimitive.wrap(false));
    o2.add("e", JsonPrimitive.wrap(true));
    JsonArray a2 = new JsonArray();
    o1.add("f", a2);
    a2.add(JsonPrimitive.wrap(BigInteger.valueOf(1)));
    a2.add(JsonPrimitive.wrap(BigInteger.valueOf(2)));
    a2.add(JsonPrimitive.wrap(BigInteger.valueOf(3)));
    a2.add(null);
    JsonObject o3 = new JsonObject();
    JsonArray a3 = new JsonArray();
    o3.add("g", a3);
    a2.add(o3);
    a3.add(new JsonObject());
    JsonArray a4 = new JsonArray();
    a4.add(JsonPrimitive.wrap(BigInteger.ONE));
    a2.add(a4);
    o1.add("h", new JsonArray());
    testParse(o1, json);
  }

  private void testParse(JsonElement expected, String json) {
    try {
      JsonElement result = Json.parse(new StringReader(json));
      assertThat(json, result, equalTo(expected));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (JsonParseException e) {
      fail(e.getMessage() + ", json=\"" + json + "\"");
    }
  }
}
