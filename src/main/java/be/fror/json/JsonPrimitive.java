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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Olivier Grégoire
 */
public final class JsonPrimitive extends JsonElement {

  private static final JsonPrimitive TRUE = new JsonPrimitive(true);
  private static final JsonPrimitive FALSE = new JsonPrimitive(false);

  public static JsonPrimitive wrap(boolean value) {
    return value ? TRUE : FALSE;
  }

  public static JsonPrimitive wrap(String value) {
    if (value == null) {
      throw new NullPointerException();
    }
    return new JsonPrimitive(value);
  }

  public static JsonPrimitive wrap(long value) {
    return new JsonPrimitive(BigDecimal.valueOf(value));
  }

  public static JsonPrimitive wrap(BigInteger value) {
    if (value == null) {
      throw new NullPointerException();
    }
    return new JsonPrimitive(new BigDecimal(value));
  }

  public static JsonPrimitive wrap(BigDecimal value) {
    if (value == null) {
      throw new NullPointerException();
    }
    return new JsonPrimitive(value);
  }

  private final Object value;

  private JsonPrimitive(Object value) {
    super(Type.PRIMITIVE);
    this.value = value;
  }

  public boolean isBoolean() {
    return value instanceof Boolean;
  }

  public boolean getAsBoolean() {
    checkType(isBoolean());
    return (Boolean) value;
  }

  public boolean isNumber() {
    return value instanceof Number;
  }

  public Number getAsNumber() {
    checkType(isNumber());
    return (Number) value;
  }

  public boolean isString() {
    return value instanceof String;
  }

  public String getAsString() {
    checkType(isString());
    return (String) value;
  }

  private void checkType(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  public double getAsDouble() {
    return getAsNumber().doubleValue();
  }

  public float getAsFloat() {
    return getAsNumber().floatValue();
  }

  public long getAsLong() {
    return getAsNumber().longValue();
  }

  public int getAsInt() {
    return getAsNumber().intValue();
  }

  public BigDecimal getAsBigDecimal() {
    return (BigDecimal) value;
  }

  public BigInteger getAsBigInteger() {
    return ((BigDecimal) value).toBigInteger();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JsonPrimitive) {
      JsonPrimitive other = (JsonPrimitive) obj;
      return Objects.equals(value, other.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

}
