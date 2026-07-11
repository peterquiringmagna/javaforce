package javaforce;

import java.util.*;

/** JFDictionary.
 *
 * Internally a HashMap is used to store key/value pairs.
 *
 * @author pquiring
 */

public class JFDictionary<K,V> {
  public HashMap<K,V> map = new HashMap<>();

  public Class<K> key_type;
  public Class<V> value_type;

  public JFDictionary(Class<K> key_type, Class<V> value_type) {
    this.key_type = key_type;
    this.value_type = value_type;
  }
}
