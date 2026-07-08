package javaforce.linux;

import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;

import javaforce.*;
import javaforce.api.linux.*;

/** Wayland Compositor.
 *
 * Based on wlroots.
 *
 * @author pquiring
 */

public class Wayland {
  private static int LOG_DISPLAY = 1;
  private static boolean debug = true;
  public void start() {
    WaylandAPI api = WaylandAPI.getInstance();
    Arena arena = Arena.ofAuto();
    long display = api.wl_display_create();
    if (debug) JFLog.log("display=" + display);
    long event_loop = api.wl_event_loop_create();
    if (debug) JFLog.log("event_loop=" + event_loop);
    long session = 0;
    MemorySegment session_ptr = arena.allocateFrom(JAVA_LONG, session);
    long backend = api.wlr_backend_autocreate(display, session_ptr.address());
    if (debug) JFLog.log("backend=" + backend);
    if (session_ptr.address() != 0) {
      session = session_ptr.get(JAVA_LONG, 0);
      if (debug) JFLog.log("session=" + session);
    }
    api.wlr_backend_start(backend);
    long renderer = api.wlr_renderer_autocreate(backend);
    if (debug) JFLog.log("renderer=" + renderer);
    long compositor = api.wlr_compositor_create(display, 6, renderer);
    if (debug) JFLog.log("compositor=" + compositor);
    long xwayland = api.wlr_xwayland_create(display, compositor, true);
    if (debug) JFLog.log("xwayland=" + xwayland);
    String socket = api.wl_display_add_socket_auto(display);
    if (debug) JFLog.log("WAYLAND_SOCKET=" + socket);
    LinuxAPI.getInstance().setEnv("WAYLAND_DISPLAY", socket);
    if (debug) JFLog.log("wl_display_run");
    api.wl_display_run(display);
    if (debug) JFLog.log("wayland shutdown...");
    api.wlr_xwayland_destroy(xwayland);
    api.wlr_backend_destroy(backend);
    api.wl_display_destroy(display);
  }
  public void stop() {
    //TODO
  }
}
