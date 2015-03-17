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

import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Olivier Grégoire &lt;https://github.com/fror&gt;
 */
public class CompleteTest {

  @Test
  public void testResources() throws IOException {
    testResource("dummy1.json");
    testResource("dummy2.json");
  }

  private void testResource(String resourceName) throws IOException {
    try (InputStream in = CompleteTest.class.getResourceAsStream(resourceName)) {
      Reader reader = new InputStreamReader(in, UTF_8);
      Json.parse(reader, JsonCallback.noopCallback());
    }
  }
}
