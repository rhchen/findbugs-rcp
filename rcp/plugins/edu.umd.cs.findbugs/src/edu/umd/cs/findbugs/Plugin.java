/*
 * FindBugs - Find bugs in Java programs
 * Copyright (C) 2004, University of Maryland
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edu.umd.cs.findbugs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.CheckForNull;

import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.classfile.IAnalysisEngineRegistrar;
import edu.umd.cs.findbugs.cloud.CloudPlugin;
import edu.umd.cs.findbugs.plan.DetectorOrderingConstraint;
import edu.umd.cs.findbugs.util.DualKeyHashMap;

/**
 * A FindBugs plugin. A plugin contains executable Detector classes, as well as
 * meta information describing those detectors (such as human-readable detector
 * and bug descriptions).
 *
 * @see PluginLoader
 * @author David Hovemeyer
 */
public class Plugin {
    
    static final Map<String, String> globalOptions = new HashMap<String,String>();
    static final Map<String, Plugin> globalOptionsSetter = new HashMap<String,Plugin>();

    
    public static Map<String, String>  getGlobalOptions() {
        return Collections.unmodifiableMap(globalOptions);
    }

    private static final String USE_FINDBUGS_VERSION = "USE_FINDBUGS_VERSION";
    private final String pluginId;

    private final String version;

    private String provider;

    private URI website;
    private @CheckForNull URI usageTracker;

    private String shortDescription;
    private String detailedDescription;

    private final ArrayList<DetectorFactory> detectorFactoryList;
    private final Map<String, FindBugsMain> mainPlugins;

    private final LinkedHashSet<BugPattern> bugPatterns;

    private final LinkedHashSet<BugCode> bugCodeList;

    private final LinkedHashSet<BugCategory> bugCategoryList;
    private final LinkedHashSet<CloudPlugin> cloudList;

    private final DualKeyHashMap<Class, String, ComponentPlugin> componentPlugins;

    private BugRanker bugRanker;

    // Ordering constraints
    private final ArrayList<DetectorOrderingConstraint> interPassConstraintList;

    private final ArrayList<DetectorOrderingConstraint> intraPassConstraintList;

    // Optional: engine registrar class
    private Class<? extends IAnalysisEngineRegistrar> engineRegistrarClass;

    // PluginLoader that loaded this plugin
    private final PluginLoader pluginLoader;

    private final boolean enabledByDefault;

    static Map<URI, Plugin> allPlugins = new LinkedHashMap<URI, Plugin>();

    enum EnabledState { PLUGIN_DEFAULT, ENABLED, DISABLED};

    private EnabledState enabled;


    /**
     * Constructor. Creates an empty plugin object.
     *
     * @param pluginId
     *            the plugin's unique identifier
     * @param version TODO
     * @param enabled TODO
     */
    public Plugin(String pluginId, String version, PluginLoader pluginLoader, boolean enabled) {
        this.pluginId = pluginId;
        if (version == null) {
            version = "";
        } else if (version.equals(USE_FINDBUGS_VERSION)) {
            version = Version.COMPUTED_RELEASE;
        }
        cloudList = new LinkedHashSet<CloudPlugin>();
        componentPlugins = new DualKeyHashMap<Class, String, ComponentPlugin> ();
        this.version = version;
        this.detectorFactoryList = new ArrayList<DetectorFactory>();
        this.bugPatterns = new LinkedHashSet<BugPattern>();
        this.bugCodeList = new LinkedHashSet<BugCode>();
        this.bugCategoryList = new LinkedHashSet<BugCategory>();
        this.interPassConstraintList = new ArrayList<DetectorOrderingConstraint>();
        this.intraPassConstraintList = new ArrayList<DetectorOrderingConstraint>();
        this.mainPlugins = new HashMap<String, FindBugsMain>();
        this.pluginLoader = pluginLoader;
        this.enabledByDefault = enabled;
        this.enabled = EnabledState.PLUGIN_DEFAULT;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + pluginId;
    }

    /**
     * Return whether or not the Plugin is enabled.
     *
     * @return true if the Plugin is enabled, false if not
     */
    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    /**
     * Set plugin provider.
     *
     * @param provider
     *            the plugin provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Get the plugin provider.
     *
     * @return the provider, or null if the provider was not specified
     */
    public @CheckForNull String getProvider() {
        return provider;
    }

    
    public void setUsageTracker(String tracker) throws URISyntaxException {
        this.usageTracker = new URI(tracker);
    }
    
    public @CheckForNull URI getUsageTracker() {
        return usageTracker;
    }
    /**
     * Set plugin website.
     *
     * @param website
     *            the plugin website
     * @throws URISyntaxException 
     */
    public void setWebsite(String website) throws URISyntaxException {
        this.website = new URI(website);
    }

    /**
     * Get the plugin website.
     *
     * @return the website, or null if the was not specified
     */
    public  @CheckForNull String getWebsite() {
        if (website == null)
            return null;
        return website.toASCIIString();
    }
    
    public  @CheckForNull URI getWebsiteURI() {
        return website;
    }

    /** Get the version of the plugin */
    public String getVersion() {
        return version;
    }

    /**
     * Set plugin short (one-line) text description.
     *
     * @param shortDescription
     *            the plugin short text description
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Get the plugin short (one-line) description.
     *
     * @return the short description, or null if the short description was not
     *         specified
     */
    public String getShortDescription() {
        return shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    /**
     * Add a DetectorFactory for a Detector implemented by the Plugin.
     *
     * @param factory
     *            the DetectorFactory
     */
    public void addDetectorFactory(DetectorFactory factory) {
        detectorFactoryList.add(factory);
    }

    public void addCloudPlugin(CloudPlugin cloudPlugin) {
        cloudList.add(cloudPlugin);
    }

    /**
     * Add a BugPattern reported by the Plugin.
     *
     * @param bugPattern
     */
    public void addBugPattern(BugPattern bugPattern) {
        bugPatterns.add(bugPattern);
    }

    /**
     * Add a BugCode reported by the Plugin.
     *
     * @param bugCode
     */
    public void addBugCode(BugCode bugCode) {
        bugCodeList.add(bugCode);
    }

    /**
     * Add a BugCategory reported by the Plugin.
     *
     * @param bugCode
     */
    public void addBugCategory(BugCategory bugCategory) {
        bugCategoryList.add(bugCategory);
    }
    /**
     * Add an inter-pass Detector ordering constraint.
     *
     * @param constraint
     *            the inter-pass Detector ordering constraint
     */
    public void addInterPassOrderingConstraint(DetectorOrderingConstraint constraint) {
        interPassConstraintList.add(constraint);
    }

    /**
     * Add an intra-pass Detector ordering constraint.
     *
     * @param constraint
     *            the intra-pass Detector ordering constraint
     */
    public void addIntraPassOrderingConstraint(DetectorOrderingConstraint constraint) {
        intraPassConstraintList.add(constraint);
    }

    /**
     * Look up a DetectorFactory by short name.
     *
     * @param shortName
     *            the short name
     * @return the DetectorFactory
     */
    public DetectorFactory getFactoryByShortName(final String shortName) {
        return findFirstMatchingFactory(new FactoryChooser() {
            public boolean choose(DetectorFactory factory) {
                return factory.getShortName().equals(shortName);
            }
        });
    }

    /**
     * Look up a DetectorFactory by full name.
     *
     * @param fullName
     *            the full name
     * @return the DetectorFactory
     */
    public DetectorFactory getFactoryByFullName(final String fullName) {
        return findFirstMatchingFactory(new FactoryChooser() {
            public boolean choose(DetectorFactory factory) {
                return factory.getFullName().equals(fullName);
            }
        });
    }

    /**
     * Get Iterator over DetectorFactory objects in the Plugin.
     *
     * @return Iterator over DetectorFactory objects
     */
    public Collection<DetectorFactory> getDetectorFactories() {
        return detectorFactoryList;
    }

    /**
     * Get the set of BugPatterns
     *
     */
    public Set<BugPattern> getBugPatterns() {
        return bugPatterns;
    }

    /**
     * Get Iterator over BugCode objects in the Plugin.
     *
     * @return Iterator over BugCode objects
     */
    public Set<BugCode> getBugCodes() {
        return bugCodeList;
    }
    /**
     * Get Iterator over BugCategories objects in the Plugin.
     *
     * @return Iterator over BugCategory objects
     */
    public Set<BugCategory> getBugCategories() {
        return bugCategoryList;
    }

    /**
     * @param id may be null
     * @return return bug category with given id, may return null if the bug category is unknown
     */
    @CheckForNull
    public BugCategory getBugCategory(String id) {
        for (BugCategory bc : bugCategoryList) {
            if(bc.getCategory().equals(id)){
                return bc;
            }
        }
        return null;
    }

    public Set<CloudPlugin> getCloudPlugins() {
        return cloudList;
    }
    /**
     * Return an Iterator over the inter-pass Detector ordering constraints.
     */
    public Iterator<DetectorOrderingConstraint> interPassConstraintIterator() {
        return interPassConstraintList.iterator();
    }

    /**
     * Return an Iterator over the intra-pass Detector ordering constraints.
     */
    public Iterator<DetectorOrderingConstraint> intraPassConstraintIterator() {
        return intraPassConstraintList.iterator();
    }

    /**
     * @return Returns the pluginId.
     */
    public String getPluginId() {
        return pluginId;
    }
    /**
     * @return Returns the short pluginId.
     */
    public String getShortPluginId() {
        int i = pluginId.lastIndexOf('.');
        return pluginId.substring(i+1);
    }
    /**
     * Set the analysis engine registrar class that, when instantiated, can be
     * used to register the plugin's analysis engines with the analysis cache.
     *
     * @param engineRegistrarClass
     *            The engine registrar class to set.
     */
    public void setEngineRegistrarClass(Class<? extends IAnalysisEngineRegistrar> engineRegistrarClass) {
        this.engineRegistrarClass = engineRegistrarClass;
    }

    /**
     * Get the analysis engine registrar class that, when instantiated, can be
     * used to register the plugin's analysis engines with the analysis cache.
     *
     * @return Returns the engine registrar class.
     */
    public Class<? extends IAnalysisEngineRegistrar> getEngineRegistrarClass() {
        return engineRegistrarClass;
    }

    /**
     * @return Returns the pluginLoader.
     */
    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    private interface FactoryChooser {
        public boolean choose(DetectorFactory factory);
    }

    private @CheckForNull
    DetectorFactory findFirstMatchingFactory(FactoryChooser chooser) {
        for (DetectorFactory factory : getDetectorFactories()) {
            if (chooser.choose(factory))
                return factory;
        }
        return null;
    }

    /**
     * @param ranker
     */
    public void setBugRanker(BugRanker ranker) {
        this.bugRanker = ranker;
    }

    public BugRanker getBugRanker() {
        return bugRanker;
    }

    <T> void addFindBugsMain(Class<?> mainClass, String cmd, String kind, boolean analysis) throws SecurityException, NoSuchMethodException {
        FindBugsMain main = new FindBugsMain(mainClass, cmd, kind, analysis);
        mainPlugins.put(cmd, main);
    }
    
    public @CheckForNull FindBugsMain getFindBugsMain(String cmd) {
        return mainPlugins.get(cmd);
        
    }
    
    <T> void addComponentPlugin(Class<T> componentClass, ComponentPlugin<T> plugin) {
        if (!componentClass.isAssignableFrom(plugin.getComponentClass()))
                throw new IllegalArgumentException();
        componentPlugins.put(componentClass, plugin.getId(), plugin);
    }

    public <T> Iterable<ComponentPlugin<T>> getComponentPlugins(Class<T> componentClass) {
        Collection values = componentPlugins.get(componentClass).values();
        return values;
    }

    public <T>  ComponentPlugin<T> getComponentPlugin(Class<T> componentClass, String name) {
        return componentPlugins.get(componentClass, name);
    }

    public static synchronized @CheckForNull Plugin getByPluginId(String name) {
        for(Plugin plugin : allPlugins.values()) {
            if (name.equals(plugin.getPluginId()) || name.equals(plugin.getShortPluginId()))
                return plugin;
        }
        return null;
    }

    /**
     * @return a copy of the internal plugins collection
     */
    public static synchronized Collection<Plugin> getAllPlugins() {
        return new ArrayList<Plugin>(allPlugins.values());
    }

    public static synchronized Set<URI> getAllPluginsURIs() {
        Collection<Plugin> plugins = getAllPlugins();
        Set<URI> uris = new HashSet<URI>();
        for (Plugin plugin : plugins) {
            
            try {
                URI uri = plugin.getPluginLoader().getURL().toURI();
                if(uri != null) {
                    uris.add(uri);
                }
            } catch (URISyntaxException e) {
                AnalysisContext.logError("Unable to get URI", e);
            }
           
        }
        return uris;
    }

    /**
     * @return may return null
     */
    @CheckForNull
    static synchronized Plugin getPlugin(URI uri) {
        return allPlugins.get(uri);
    }

    /**
     * @return may return null
     */
    @CheckForNull
    static synchronized Plugin putPlugin(URI uri, Plugin plugin) {
        return allPlugins.put(uri, plugin);
    }

    public boolean isCorePlugin() {
        return pluginLoader.isCorePlugin();
    }

    public boolean isGloballyEnabled() {
        if (isCorePlugin())
            return true;
        switch (enabled) {
        case ENABLED:
            return true;
        case DISABLED:
            return false;
        case PLUGIN_DEFAULT:
            return isEnabledByDefault();
        default:
            throw new IllegalStateException("Unknown state : " + enabled);
        }
    }

    /**
     * @return
     */
    public void setGloballyEnabled(boolean enabled) {
        if (isCorePlugin()) {
            if (!enabled)
                throw new IllegalArgumentException("Can't disable core plugin");
            return;
        }
        EnabledState oldState = this.enabled;

        if (enabled) {
            if (isEnabledByDefault())
                this.enabled = EnabledState.PLUGIN_DEFAULT;
            else
                this.enabled = EnabledState.ENABLED;
        } else {
            if (isEnabledByDefault())
                this.enabled = EnabledState.DISABLED;
            else
                this.enabled = EnabledState.PLUGIN_DEFAULT;
        }
        if(oldState != this.enabled) {
            // TODO update detector factory collection?
        }
    }

    public boolean isInitialPlugin() {
        return getPluginLoader().initialPlugin;
    }

    public URL getResource(String name) {
        return getPluginLoader().getResource(name);
    }

    public ClassLoader getClassLoader() {
        return getPluginLoader().getClassLoader();
    }

    /**
     * Loads the given plugin and enables it for the given project.
     */
    public static Plugin loadCustomPlugin(File f, @CheckForNull Project project)
            throws PluginException {
        URL urlString;
        try {
            urlString = f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        return loadCustomPlugin(urlString, project);
    }

    /**
     * Loads the given plugin and enables it for the given project.
     */
    public static Plugin loadCustomPlugin(URL urlString, @CheckForNull Project project) throws PluginException {
        Plugin plugin = addCustomPlugin(urlString);
        if (project != null) {
            project.setPluginStatus(plugin.getPluginId(), true);
        }
        return plugin;
    }

    public static Plugin addCustomPlugin(URL u) throws PluginException {
        return addCustomPlugin(u, PluginLoader.class.getClassLoader());
    }
    public static Plugin addCustomPlugin(URI u) throws PluginException {
        return addCustomPlugin(u, PluginLoader.class.getClassLoader());
    }
    public static Plugin addCustomPlugin(URL u, ClassLoader parent) throws PluginException {
        PluginLoader pluginLoader = PluginLoader.getPluginLoader(u, parent, false, true);
        Plugin plugin = pluginLoader.loadPlugin();
        // register new clouds
        DetectorFactoryCollection.instance().loadPlugin(plugin);
        return plugin;
    }

    public static Plugin addCustomPlugin(URI u, ClassLoader parent) throws PluginException {
        try {
            PluginLoader pluginLoader = PluginLoader.getPluginLoader(u.toURL(), parent, false, true);

            Plugin plugin = pluginLoader.loadPlugin();
            // register new clouds
            DetectorFactoryCollection.instance().loadPlugin(plugin);
            return plugin;
        } catch (MalformedURLException e) {
            throw new PluginException("Unable to convert uri to url:" + u, e);
        }
    }
    public static synchronized void removeCustomPlugin(Plugin plugin) {
        Set<Entry<URI, Plugin>> entrySet = Plugin.allPlugins.entrySet();
        for (Entry<URI, Plugin> entry : entrySet) {
            if(entry.getValue() == plugin) {
                Plugin.allPlugins.remove(entry.getKey());
                PluginLoader.loadedPluginIds.remove(plugin.getPluginId());
                break;
            }
        }
        DetectorFactoryCollection.instance().unLoadPlugin(plugin);
    }

}
