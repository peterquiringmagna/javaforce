package javaforce;

/** JFVariant.
 *
 * A DBus object.
 *
 * Variants can hold any type of DBus object.
 *
 * @author pquiring
 */

public class JFVariant<T> {
  public T value;

  public JFVariant(T value) {
    this.value = value;
  }
}
