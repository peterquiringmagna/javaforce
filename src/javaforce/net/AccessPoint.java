package javaforce.net;

import java.util.*;

import javaforce.*;

/** Access Point.
 *
 * @author pquiring
 */

public class AccessPoint {
  public String dev;
  public String ssid;

  public String encType;
  public String passwd;

  public boolean active;

  private static AccessPoint fromDictionaryOne(JFDictionary<String, String> dict) {
    AccessPoint ap = new AccessPoint();
    ap.dev = (String)dict.map.get("dev");
    ap.ssid = (String)dict.map.get("ssid");
    ap.encType = (String)dict.map.get("encType");
    ap.active = ((String)dict.map.get("active")).equals("true");
    return ap;
  }

  public static AccessPoint[] fromDictionary(JFDictionary<String, JFVariant<JFDictionary<String, String>>> dict) {
    String[] keys = dict.map.keySet().toArray(new String[0]);
    ArrayList<AccessPoint> aps = new ArrayList<>();
    for(String key : keys) {
      JFVariant<JFDictionary<String, String>> v = dict.map.get(key);
      JFDictionary<String, String> apdict = (JFDictionary<String, String>)v.value;
      aps.add(fromDictionaryOne(apdict));
    }
    return aps.toArray(new AccessPoint[0]);
  }

  public static JFDictionary toDictionary(AccessPoint[] aps) {
    JFDictionary<String, JFVariant> dict = new JFDictionary<>(String.class, JFVariant.class);
    for(AccessPoint ap : aps) {
      JFVariant<JFDictionary> v = new JFVariant<>(ap.toDictionary());
      dict.map.put(ap.getKey(), v);
    }
    return dict;
  }

  public String getKey() {
    if (dev != null) {
      return dev + ":" + ssid;
    }
    return ssid;
  }

  public JFDictionary toDictionary() {
    JFDictionary<String, String> dict = new JFDictionary<>(String.class, String.class);
    dict.map.put("dev", dev);
    dict.map.put("ssid", ssid);
    dict.map.put("encType", encType);
    dict.map.put("passwd", passwd);
    dict.map.put("active", active ? "true" : "false");
    return dict;
  }

  public String toString() {
    return "AccessPoint:" + ssid;
  }
}
