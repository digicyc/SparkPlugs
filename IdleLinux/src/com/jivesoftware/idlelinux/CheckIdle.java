package com.jivesoftware.idlelinux;

import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.status.StatusItem;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.smack.packet.Presence;

import java.util.Timer;
import java.util.TimerTask;


public class CheckIdle implements Plugin {

    private int previousPriority = -1;
    private boolean unavailable = false;

    public void initialize() {
        try {
            setIdleListener();
        } catch (Exception e) {
            Log.error("[PLUGIN]: Can't set idle listener.", e);
        }
    }

    public void shutdown() { }

    public void uninstall() { }

    public boolean canShutDown() {
        return true;
    }

    private void setIdleListener() throws Exception {

        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                LocalPreferences localPref = SettingsManager.getLocalPreferences();
                int delay = 0;
                if (localPref.isIdleOn()) {
                    delay = localPref.getIdleTime() * 60000;
                }
                else {
                    return;
                }

                long idleTime = LinuxIdle.getIdleTime();
                boolean isLocked = false;
                if (idleTime > delay) {
                    try {
                        // Handle if spark is not connected to the server.
                        if (SparkManager.getConnection() == null 
                        || !SparkManager.getConnection().isConnected()) {
                            return;
                        }

                        // Change Status
                        Workspace workspace = SparkManager.getWorkspace();
                        Presence presence = workspace.getStatusBar().getPresence();
                        if (workspace != null && presence.getMode() == Presence.Mode.available) {
                            unavailable = true;
                            StatusItem away = workspace.getStatusBar().getStatusItem("Away");
                            Presence p = away.getPresence();
                            if (isLocked) {
                                p.setStatus(Res.getString("message.locked.workstation"));
                            }
                            else {
                                p.setStatus(Res.getString("message.away.idle"));
                            }

                            previousPriority = presence.getPriority();

                            p.setPriority(0);
                            SparkManager.getSessionManager().changePresence(p);
                        }
                    }
                    catch (Exception e) {
                        Log.error("Error with IDLE status.", e);
                        timer.cancel();
                    }
                }
                else {
                    if (unavailable) {
                        setAvailableIfActive();
                    }
                }
            }
        }, 1000, 1000);
    }

    private void setAvailableIfActive() {

        // Handle if spark is not connected to the server.
        if (SparkManager.getConnection() == null || !SparkManager.getConnection().isConnected()) {
            return;
        }

        // Change Status
        Workspace workspace = SparkManager.getWorkspace();
        if (workspace != null) {
            Presence presence = workspace.getStatusBar().getStatusItem(Res.getString("available")).getPresence();
            if (previousPriority != -1) {
                presence.setPriority(previousPriority);
            }

            SparkManager.getSessionManager().changePresence(presence);
            unavailable = false;
        }
    }
}
