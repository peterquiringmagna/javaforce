//Linux OpenGL

jboolean glGetFunction(void **funcPtr, const char *name)
{
  void *func;
  func = (void*)(*_glXGetProcAddress)(name);  //get OpenGL 1.x function
  if (func == NULL) {
    func = (void*)dlsym(xgl, name);  //get OpenGL 2.0+ function
  }
  if (func != NULL) {
    *funcPtr = func;
    return JNI_TRUE;
  } else {
    printf("OpenGL:Error:Can not find function:%s\n", name);
    return JNI_FALSE;
  }
}

JNIEXPORT jboolean JNICALL GLinit(const char* libgl_so)
{
  if (xgl == NULL && libgl_so != NULL) {
    xgl = dlopen(libgl_so, RTLD_LAZY | RTLD_GLOBAL);
    if (xgl == NULL) {
      printf("Warning:dlopen(libGL.so) unsuccessful\n");
    } else {
      getFunction(xgl, (void**)&_glXCreateContext, "glXCreateContext");
      getFunction(xgl, (void**)&_glXDestroyContext, "glXDestroyContext");
      getFunction(xgl, (void**)&_glXMakeCurrent, "glXMakeCurrent");
      getFunction(xgl, (void**)&_glXGetProcAddress, "glXGetProcAddress");
      getFunction(xgl, (void**)&_glXSwapBuffers, "glXSwapBuffers");
      getFunction(xgl, (void**)&_glXChooseVisual, "glXChooseVisual");
    }
  }
  GL_get_functions();
  return TRUE;
}
