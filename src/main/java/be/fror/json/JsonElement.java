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

/**
 * @author Olivier Grégoire
 */
public abstract class JsonElement {

  enum Type {

    OBJECT,
    ARRAY,
    PRIMITIVE,
    NULL;
  }

  private final Type type;

  JsonElement(Type type) {
    this.type = type;
  }

  public boolean isJsonObject() {
    return isType(Type.OBJECT);
  }

  public boolean isJsonArray() {
    return isType(Type.ARRAY);
  }

  public boolean isJsonPrimitive() {
    return isType(Type.PRIMITIVE);
  }

  public boolean isJsonNull() {
    return isType(Type.NULL);
  }

  public JsonObject asJsonObject() {
    checkType(Type.OBJECT);
    return (JsonObject) this;
  }

  public JsonArray asJsonArray() {
    checkType(Type.ARRAY);
    return (JsonArray) this;
  }

  public JsonPrimitive asJsonPrimitive() {
    checkType(Type.PRIMITIVE);
    return (JsonPrimitive) this;
  }

  public JsonNull asJsonNull() {
    checkType(Type.NULL);
    return (JsonNull) this;
  }

  private boolean isType(Type type) {
    return this.type == type;
  }

  private void checkType(Type type) {
    if (!isType(type)) {
      throw new IllegalStateException();
    }
  }

  abstract void accept(JsonVisitor visitor);

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    try {
      toJsonString(sb);
    } catch (IOException ignore) {
    }
    return sb.toString();
  }

  abstract void toJsonString(Appendable appendable) throws IOException;
}
