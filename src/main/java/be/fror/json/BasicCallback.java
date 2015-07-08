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

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Olivier Grégoire
 */
class BasicCallback implements JsonCallback {

  private final Deque<Object> values = new LinkedList<>();

  @Override
  public void arrayStart() {
    offerContainer(new JsonArray());
  }

  @Override
  public void objectStart() {
    offerContainer(new JsonObject());
  }

  private void offerContainer(JsonElement container) {
    if (!values.isEmpty()) {
      addToParentContainer(container);
    }
    values.offerFirst(container);
  }

  @Override
  public void value(JsonElement value) {
    if (values.isEmpty()) {
      values.offerFirst(value);
    } else {
      addToParentContainer(value);
    }
  }

  private void addToParentContainer(JsonElement child) {
    Object previous = values.peekFirst();
    if (previous instanceof String) {
      String key = (String) values.pollFirst(); // consume it
      ((JsonObject) values.peekFirst()).add(key, child);
    } else if (previous instanceof JsonArray) {
      ((JsonArray) previous).add(child);
    }
  }

  @Override
  public void arrayEnd() {
    poll();
  }

  @Override
  public void objectEnd() {
    poll();
  }

  private void poll() {
    if (values.size() > 1) {
      values.pollFirst();
    }
  }

  @Override
  public void key(String key) {
    values.offerFirst(key);
  }

  public JsonElement getResult() {
    return (JsonElement)values.pollFirst();
  }

}
