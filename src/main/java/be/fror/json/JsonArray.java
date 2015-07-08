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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Olivier Grégoire
 */
public class JsonArray extends JsonElement implements Iterable<JsonElement> {

  private final List<JsonElement> delegate = new ArrayList<>();
  private final List<JsonElement> unmodifiableView = Collections.unmodifiableList(delegate);

  public JsonArray() {
    super(Type.ARRAY);
  }

  public Optional<JsonElement> get(int index) {
    if (index < delegate.size()) {
      return Optional.of(delegate.get(index));
    }
    return Optional.empty();
  }

  void add(JsonElement element) {
    delegate.add(element);
  }

  @Override
  public Iterator<JsonElement> iterator() {
    return unmodifiableView.iterator();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JsonArray) {
      JsonArray other = (JsonArray) obj;
      return Objects.equals(delegate, other.delegate);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
  }
}
