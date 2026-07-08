package javaforce.linux;

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
    long display = api.wl_display_create();
    if (debug) JFLog.log("display=" + display);
    long event_loop = api.wl_event_loop_create();
    if (debug) JFLog.log("event_loop=" + event_loop);
    long session = api.wlr_session_create(display);
    if (debug) JFLog.log("session=" + session);
    long backend = api.wlr_backend_autocreate(event_loop, session);
    if (debug) JFLog.log("backend=" + backend);
    api.wlr_backend_start(backend);
    long renderer = api.wlr_renderer_autocreate(backend);
    if (debug) JFLog.log("renderer=" + renderer);
    long compositor = api.wlr_compositor_create(display, renderer);
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
