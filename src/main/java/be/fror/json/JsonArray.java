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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Olivier Grégoire
 */
public final class JsonArray extends JsonElement implements Iterable<JsonElement> {

  private final List<JsonElement> delegate = new ArrayList<>();
  private final List<JsonElement> unmodifiableView = Collections.unmodifiableList(delegate);

  JsonArray() {
    super(Type.ARRAY);
  }

  public int size() {
    return delegate.size();
  }

  public Optional<JsonElement> get(int index) {
    if (0 <= index && index < delegate.size()) {
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
  void accept(JsonVisitor visitor) {
    visitor.entering(this);
    this.forEach(e -> e.accept(visitor));
    visitor.leaving(this);
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
    return Objects.hash(delegate);
  }

  @Override
  void toJsonString(Appendable appendable) throws IOException {
    appendable.append("[");
    Iterator<JsonElement> it = iterator();
    if (it.hasNext()) {
      it.next().toJsonString(appendable);
      while (it.hasNext()) {
        appendable.append(",");
        it.next().toJsonString(appendable);
      }
    }
    appendable.append("]");
  }

}
