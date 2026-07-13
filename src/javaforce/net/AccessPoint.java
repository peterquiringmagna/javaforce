package javaforce.net;

/** Access Point.
 *
 * @author pquiring
 */

public class AccessPoint {
  public String dev;
  public String ssid;

  public String encType;
  public String passwd;

  public String toString() {
    return "AccessPoint:" + ssid;
  }
}
