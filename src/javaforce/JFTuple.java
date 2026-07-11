package javaforce;

/** Key/Value Tuple
 *
 * @author pquiring
 */

public class JFTuple<K,V> {
  public K key;
  public V value;

  public Class<K> key_type;
  public Class<V> value_type;

  public JFTuple(Class<K> key_type, Class<V> value_type) {
    this.key_type = key_type;
    this.value_type = value_type;
  }
}
