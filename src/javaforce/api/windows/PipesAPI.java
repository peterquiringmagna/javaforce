package javaforce.api.windows;

import javaforce.ffm.*;

/** Windows Pipes native API
 *
 * @author pquiring
 */

public interface PipesAPI {
  public static PipesAPI getInstance() {
    return PipesFFM.getInstance();
  }

  public long pipeCreate(String name, boolean first);
  public void pipeClose(long ctx);
  public int pipeRead(long ctx, byte[] buf, int offset, int length);
  public int pipeWrite(String name, byte[] buf, int offset, int length);
}
