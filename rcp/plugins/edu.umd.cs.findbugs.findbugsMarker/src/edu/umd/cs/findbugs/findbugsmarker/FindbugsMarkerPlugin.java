package edu.umd.cs.findbugs.findbugsmarker;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.umd.cs.findbugs.quickfix.QuickfixPlugin;
import edu.umd.cs.findbugs.quickfix.resolution.BugResolutionAssociations;
import edu.umd.cs.findbugs.quickfix.resolution.BugResolutionLoader;

public class FindbugsMarkerPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "edu.umd.cs.findbugs.findbugsMarker";
	
	private static FindbugsMarkerPlugin plugin;
	
	public static boolean DEBUG = true;
	
	private BugResolutionAssociations bugResolutions;

    private boolean bugResolutionsLoaded;
    
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static FindbugsMarkerPlugin getDefault() {
		return plugin;
	}
	
	public void logException(Throwable e, String message) {
        logMessage(IStatus.ERROR, message, e);
    }

    public void logError(String message) {
        logMessage(IStatus.ERROR, message, null);
    }

    public void logWarning(String message) {
        logMessage(IStatus.WARNING, message, null);
    }

    public void logInfo(String message) {
        logMessage(IStatus.INFO, message, null);
    }

    public void logMessage(int severity, String message, Throwable e) {
        if (DEBUG) {
            String what = (severity == IStatus.ERROR) ? (e != null ? "Exception" : "Error") : "Warning";
            System.out.println(what + " in FindBugs plugin: " + message);
            if (e != null) {
                e.printStackTrace();
            }
        }
        IStatus status = createStatus(severity, message, e);
        getLog().log(status);
    }

    public static IStatus createStatus(int severity, String message, Throwable e) {
        return new Status(severity, QuickfixPlugin.PLUGIN_ID, 0, message, e);
    }

    public static IStatus createErrorStatus(String message, Throwable e) {
        return new Status(IStatus.ERROR, QuickfixPlugin.PLUGIN_ID, 0, message, e);
    }
    
    public BugResolutionAssociations getBugResolutions() {
        if (!bugResolutionsLoaded) {
            bugResolutionsLoaded = true;
            try {
                bugResolutions = loadBugResolutions();
            } catch (Exception e) {
            	FindbugsMarkerPlugin.getDefault().logException(e, "Could not read load bug resolutions");
            }
        }
        return bugResolutions;
    }

    private BugResolutionAssociations loadBugResolutions() {
        BugResolutionLoader loader = new BugResolutionLoader();
        return loader.loadBugResolutions();
    }
}
