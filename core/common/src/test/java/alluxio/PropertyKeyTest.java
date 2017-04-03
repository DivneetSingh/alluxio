/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests enum type {@link PropertyKey}.
 */
public final class PropertyKeyTest {

  /**
   * Tests parsing string to PropertyKey by {@link PropertyKey#fromString}.
   */
  @Test
  public void fromString() throws Exception {
    Assert.assertEquals(PropertyKey.VERSION,
        PropertyKey.fromString(PropertyKey.VERSION.toString()));
  }

  @Test
  public void equalsTest() throws Exception {
    Assert.assertEquals(new PropertyKey("foo"), new PropertyKey("foo"));
    Assert.assertNotEquals(new PropertyKey("foo"), new PropertyKey("bar"));
  }
}
