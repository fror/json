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

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Olivier Grégoire &lt;https://github.com/fror&gt;
 */
public class JsonParserTest {

  @Test
  public void testValues() {
    testParse(new BigInteger("1"), "1");
    testParse(new BigDecimal("1.2"), "1.2");
    testParse("a", "\"a\"");
    testParse(true, "true");
    testParse(false, "false");
    testParse(null, "null");
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
      array.add("a");
      testParse(array, "[\"a\"]");
    }
    {
      array = new JsonArray();
      array.add("a");
      array.add("b");
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
      child.add("a");
      parent.add(child);
      testParse(parent, "[[\"a\"]]");
    }
    {
      parent = new JsonArray();
      child = new JsonArray();
      child.add("a");
      child.add("b");
      parent.add(child);
      testParse(parent, "[[\"a\",\"b\"]]");
    }
    {
      parent = new JsonArray();
      child = new JsonArray();
      child.add("a");
      parent.add(child);
      child = new JsonArray();
      child.add("b");
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
      object.put("a", "b");
      testParse(object, "{\"a\":\"b\"}");
    }
    {
      object = new JsonObject();
      object.put("a", "b");
      object.put("c", "d");
      testParse(object, "{\"a\":\"b\",\"c\":\"d\"}");
    }
  }

  @Test
  public void testObjectInObject() {
    JsonObject parent, child;
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.put("a", child);
      testParse(parent, "{\"a\":{}}");
    }
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.put("a", child);
      child.put("a", "b");
      testParse(parent, "{\"a\":{\"a\":\"b\"}}");
    }
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.put("a", child);
      child.put("a", "b");
      child.put("c", "d");
      testParse(parent, "{\"a\":{\"a\":\"b\",\"c\":\"d\"}}");
    }
    {
      parent = new JsonObject();
      child = new JsonObject();
      parent.put("a", child);
      child = new JsonObject();
      parent.put("b", child);
      testParse(parent, "{\"a\":{},\"b\":{}}");
    }
  }

  @Test
  public void testCompleteObject() throws Exception {
    String json = "{\"a\":{\"b\":[\"c\",1,23,1.23e45],\"d\":false,\"e\":true},\"f\":[1,2,3,null,{\"g\":[{}]},[1]],\"h\":[]}";
    JsonObject o1 = new JsonObject();
    JsonObject o2 = new JsonObject();
    o1.put("a", o2);
    JsonArray a1 = new JsonArray();
    o2.put("b", a1);
    a1.add("c");
    a1.add(BigInteger.ONE);
    a1.add(BigInteger.valueOf(23));
    a1.add(new BigDecimal("1.23e45"));
    o2.put("d", false);
    o2.put("e", true);
    JsonArray a2 = new JsonArray();
    o1.put("f", a2);
    a2.add(BigInteger.valueOf(1));
    a2.add(BigInteger.valueOf(2));
    a2.add(BigInteger.valueOf(3));
    a2.add(null);
    JsonObject o3 = new JsonObject();
    JsonArray a3 = new JsonArray();
    o3.put("g", a3);
    a2.add(o3);
    a3.add(new JsonObject());
    JsonArray a4 = new JsonArray();
    a4.add(BigInteger.ONE);
    a2.add(a4);
    o1.put("h", new JsonArray());
    testParse(o1, json);
  }

  private void testParse(Object expected, String json) {
    try {
      Object result = Json.parse(new StringReader(json));
      assertThat(json, result, equalTo(expected));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (JsonParseException e) {
      fail(e.getMessage() + ", json=\"" + json + "\"");
    }
  }
}
