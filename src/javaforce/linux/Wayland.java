package javaforce.linux;

import javaforce.api.linux.*;

/** Wayland Compositor.
 *
 * Based on wlroots.
 *
 * @author pquiring
 */

public class Wayland {
  public void start() {
    WaylandAPI api = WaylandAPI.getInstance();
    long display = api.wl_display_create();
    long event_loop = api.wl_create_loop_create();
    long session = api.wlr_session_create(display);
    long backend = api.wlr_backend_autocreate(event_loop, session);
    api.wlr_backend_start(backend);
    long renderer = api.wlr_renderer_autocreate(backend);
    long compositor = api.wlr_compositor_create(display, renderer);
    long xwayland = api.wlr_xwayland_create(display, compositor, true);
    String socket = api.wl_display_add_socket_auto(display);
    LinuxAPI.getInstance().setEnv("WAYLAND_DISPLAY", socket);
    api.wl_display_run(display);
    api.wlr_xwayland_destroy(xwayland);
    api.wlr_backend_destroy(backend);
    api.wl_display_destroy(display);
  }
  public void stop() {
    //TODO
  }
}
