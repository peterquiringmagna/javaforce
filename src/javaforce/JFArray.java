package javaforce;

import java.util.*;
import java.lang.reflect.*;

/** JF Array generic.
 *
 * @author pquiring
 */

public class JFArray<T> {
  protected T[] buf;
  protected int count;
  protected Class type;

  public static int initSize = 16;

  /** Construct new JFArray.
   * @param type = T.class
   */
  @SuppressWarnings("unchecked")
  public JFArray(Class<T> type) {
    count = 0;
    this.type = type;
    buf = (T[])Array.newInstance(type, initSize);
  }

  public int size() {
    return count;
  }

  public void clear() {
    count = 0;
  }

  private void grow(int minsize) {
    int current = buf.length;
    if (minsize <= current) return;
    while (minsize > current) {
      current <<= 1;
    }
    buf = Arrays.copyOf(buf, current);
  }

  public void append(T s) {
    int newcount = count + 1;
    grow(newcount);
    buf[count] = s;
    count = newcount;
  }

  public void append(T[] s) {
    int newcount = count + s.length;
    grow(newcount);
    System.arraycopy(s, 0, buf, count, s.length);
    count = newcount;
  }

  public void set(T[] s, int pos) {
    int newcount = pos + s.length;
    grow(newcount);
    System.arraycopy(s, 0, buf, pos, s.length);
  }

  public T[] toArray() {
    return Arrays.copyOf(buf, count);
  }

  public T[] toArray(int pos, int length) {
    return Arrays.copyOfRange(buf, pos, pos + length);
  }

  //returns the backing buffer (size may be larger than expected)
  public T[] getBuffer() {
    return buf;
  }
}
