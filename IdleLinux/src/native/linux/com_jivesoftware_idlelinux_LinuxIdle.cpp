/*
 * This library gets idle calls from X and screensavers installed.
 */

#include "com_jivesoftware_idlelinux_LinuxIdle.h"

#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <X11/X.h>
#include <X11/extensions/scrnsaver.h>

Display *display = NULL;
Window window = 0;
XScreenSaverInfo *mit_info = NULL;

JNIEXPORT jlong JNICALL Java_com_jivesoftware_idlelinux_LinuxIdle_idleTime(JNIEnv *, jclass) 
{
  
    if(display == NULL) 
    {
        display = XOpenDisplay(NULL);
        if (display == NULL)
            return 0;
    }
    if (window == 0) 
    {
        window = DefaultRootWindow(display);
        if (window == 0)
            return 0;
    }
        
    int event_base, error_base;

    if (XScreenSaverQueryExtension(display, &event_base, &error_base)) 
    {
            
        if (mit_info == NULL) 
        {
            mit_info = XScreenSaverAllocInfo();
            if (mit_info == NULL)
                return 0;
        }
        XScreenSaverQueryInfo(display, window, mit_info);
        
        // Return the idle time in milliseconds as type long
        return mit_info->idle;
    } else {
         return 0;
    }
}
