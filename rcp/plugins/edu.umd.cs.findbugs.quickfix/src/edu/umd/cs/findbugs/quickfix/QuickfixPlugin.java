package edu.umd.cs.findbugs.quickfix;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.quickfix.resolution.BugResolutionAssociations;
import edu.umd.cs.findbugs.quickfix.resolution.BugResolutionLoader;

public class QuickfixPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.umd.cs.findbugs.quickfix"; //$NON-NLS-1$

	// The shared instance
	private static QuickfixPlugin plugin;

	public static boolean DEBUG = true;
	
	public static final QualifiedName SESSION_PROPERTY_BUG_COLLECTION_DIRTY = new QualifiedName(QuickfixPlugin.PLUGIN_ID
            + ".sessionprops", "bugcollection.dirty");
	
	// Persistent and session property keys
    public static final QualifiedName SESSION_PROPERTY_BUG_COLLECTION = new QualifiedName(QuickfixPlugin.PLUGIN_ID
            + ".sessionprops", "bugcollection");
	/**
	 * The constructor
	 */
	public QuickfixPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static QuickfixPlugin getDefault() {
		return plugin;
	}
	
	/**
     * Log an exception.
     *
     * @param e
     *            the exception
     * @param message
     *            message describing how/why the exception occurred
     */
    public void logException(Throwable e, String message) {
        logMessage(IStatus.ERROR, message, e);
    }

    /**
     * Log an error.
     *
     * @param message
     *            error message
     */
    public void logError(String message) {
        logMessage(IStatus.ERROR, message, null);
    }

    /**
     * Log a warning.
     *
     * @param message
     *            warning message
     */
    public void logWarning(String message) {
        logMessage(IStatus.WARNING, message, null);
    }

    /**
     * Log an informational message.
     *
     * @param message
     *            the informational message
     */
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
    
//    public BugResolutionAssociations getBugResolutions() {
//        if (!bugResolutionsLoaded) {
//            bugResolutionsLoaded = true;
//            try {
//                bugResolutions = loadBugResolutions();
//            } catch (Exception e) {
//            	QuickfixPlugin.getDefault().logException(e, "Could not read load bug resolutions");
//            }
//        }
//        return bugResolutions;
//    }
//
//    private BugResolutionAssociations loadBugResolutions() {
//        BugResolutionLoader loader = new BugResolutionLoader();
//        return loader.loadBugResolutions();
//    }
//    
//    public static SortedBugCollection getBugCollection(IProject project, IProgressMonitor monitor) throws CoreException {
//        return getBugCollection(project, monitor, true);
//    }
//    
//    /**
//     * Get the stored BugCollection for project. If there is no stored bug
//     * collection for the project, or if an error occurs reading the stored bug
//     * collection, a default empty collection is created and returned.
//     *
//     * @param project
//     *            the eclipse project
//     * @param monitor
//     *            a progress monitor
//     * @return the stored BugCollection, never null
//     * @throws CoreException
//     */
//    public static SortedBugCollection getBugCollection(IProject project, IProgressMonitor monitor, boolean useCloud)
//            throws CoreException {
//        SortedBugCollection bugCollection = (SortedBugCollection) project.getSessionProperty(SESSION_PROPERTY_BUG_COLLECTION);
//        if (bugCollection == null) {
//            try {
//                readBugCollectionAndProject(project, monitor, useCloud);
//                bugCollection = (SortedBugCollection) project.getSessionProperty(SESSION_PROPERTY_BUG_COLLECTION);
//            } catch (IOException e) {
//            	QuickfixPlugin.getDefault().logException(e, "Could not read bug collection for project");
//                bugCollection = createDefaultEmptyBugCollection(project);
//            } catch (DocumentException e) {
//            	QuickfixPlugin.getDefault().logException(e, "Could not read bug collection for project");
//                bugCollection = createDefaultEmptyBugCollection(project);
//            }
//        }
//        return bugCollection;
//    }
//    
//    /**
//     * Read saved bug collection and findbugs project from file. Will populate
//     * the bug collection and findbugs project session properties if successful.
//     * If there is no saved bug collection and project for the eclipse project,
//     * then FileNotFoundException will be thrown.
//     *
//     * @param project
//     *            the eclipse project
//     * @param monitor
//     *            a progress monitor
//     * @throws java.io.FileNotFoundException
//     *             the saved bug collection doesn't exist
//     * @throws IOException
//     * @throws DocumentException
//     * @throws CoreException
//     */
//    private static void readBugCollectionAndProject(IProject project, IProgressMonitor monitor, boolean useCloud)
//            throws IOException, DocumentException, CoreException {
//        SortedBugCollection bugCollection;
//
//        IPath bugCollectionPath = getBugCollectionFile(project);
//        // Don't turn the path to an IFile because it isn't local to the
//        // project.
//        // see the javadoc for org.eclipse.core.runtime.Plugin
//        File bugCollectionFile = bugCollectionPath.toFile();
//        if (!bugCollectionFile.exists()) {
//            // throw new
//            // FileNotFoundException(bugCollectionFile.getLocation().toOSString());
//            getDefault().logInfo("creating new bug collection: " + bugCollectionPath.toOSString());
//            createDefaultEmptyBugCollection(project); // since we no longer
//                                                      // throw, have to do this
//                                                      // here
//            return;
//        }
//
//        bugCollection = new SortedBugCollection();
//        bugCollection.getProject().setGuiCallback(new EclipseGuiCallback());
//        bugCollection.setDoNotUseCloud(!useCloud);
//
//        bugCollection.readXML(bugCollectionFile);
//
//        cacheBugCollectionAndProject(project, bugCollection, bugCollection.getProject());
//    }
//    
//    private static SortedBugCollection createDefaultEmptyBugCollection(IProject project) throws CoreException {
//        SortedBugCollection bugCollection = new SortedBugCollection();
//        Project fbProject = new Project();
//
//        cacheBugCollectionAndProject(project, bugCollection, fbProject);
//
//        return bugCollection;
//    }
//    
//    private static void cacheBugCollectionAndProject(IProject project, SortedBugCollection bugCollection, Project fbProject)
//            throws CoreException {
//        project.setSessionProperty(SESSION_PROPERTY_BUG_COLLECTION, bugCollection);
//        markBugCollectionDirty(project, false);
//    }
//    
//    public static void markBugCollectionDirty(IProject project, boolean isDirty) throws CoreException {
//        project.setSessionProperty(SESSION_PROPERTY_BUG_COLLECTION_DIRTY, isDirty ? Boolean.TRUE : Boolean.FALSE);
//    }
//    
//    /**
//     * Get the file resource used to store findbugs warnings for a project.
//     *
//     * @param project
//     *            the project
//     * @return the IPath to the file (which may not actually exist in the
//     *         filesystem yet)
//     */
//    public static IPath getBugCollectionFile(IProject project) {
//        // IPath path = project.getWorkingLocation(PLUGIN_ID); //
//        // project-specific but not user-specific?
//        IPath path = getDefault().getStateLocation(); // user-specific but not
//                                                      // project-specific
//        return path.append(project.getName() + ".fbwarnings");
//    }
    
    /**
     * Returns the SWT Shell of the active workbench window or <code>null</code>
     * if no workbench window is active.
     *
     * @return the SWT Shell of the active workbench window, or
     *         <code>null</code> if no workbench window is active
     */
    public static Shell getShell() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        if (window == null) {
            return null;
        }
        return window.getShell();
    }
    /**
     * @return active window instance, never null
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        if (Display.getCurrent() != null) {
            return getDefault().getWorkbench().getActiveWorkbenchWindow();
        }
        // need to call from UI thread
        final IWorkbenchWindow[] window = new IWorkbenchWindow[1];
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                window[0] = getDefault().getWorkbench().getActiveWorkbenchWindow();
            }
        });
        return window[0];
    }
    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        /*
         * Rhchen Remark 
         *
    	for (FindBugsMarker.Priority prio : FindBugsMarker.Priority.values()) {
            ImageDescriptor descriptor = getImageDescriptor(prio.iconName());
            if (descriptor != null) {
                reg.put(prio.iconName(), descriptor);
            }
        }
        */
    }

   
}
