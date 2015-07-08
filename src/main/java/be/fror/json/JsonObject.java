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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author Olivier Grégoire
 */
public final class JsonObject extends JsonElement {

  private final Map<String, JsonElement> delegate = new LinkedHashMap<>();
  private final Map<String, JsonElement> unmodifiableView = Collections.unmodifiableMap(delegate);

  JsonObject() {
    super(Type.OBJECT);
  }

  public int size() {
    return delegate.size();
  }

  public Optional<JsonElement> get(String property) {
    return Optional.ofNullable(delegate.get(property));
  }

  public boolean has(String property) {
    return delegate.containsKey(property);
  }

  void add(String property, JsonElement value) {
    if (property == null || value == null) {
      throw new NullPointerException();
    }
    delegate.put(property, value);
  }

  public Set<Map.Entry<String, JsonElement>> entries() {
    return unmodifiableView.entrySet();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JsonObject) {
      JsonObject other = (JsonObject) obj;
      return Objects.equals(delegate, other.delegate);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(delegate);
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

}
