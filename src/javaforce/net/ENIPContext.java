package javaforce.net;

/** ENIP Context
 *
 * @author pquiring
 */

public class ENIPContext {
  public int session;
  public long context;
  public void increment() {
    context++;
  }
}
