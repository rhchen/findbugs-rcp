/*
 * FindBugs - Find bugs in Java programs
 * Copyright (C) 2003-2005 University of Maryland
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.CheckForNull;

/**
 * Version number and release date information.
 */
public class Version {
    /**
     * Major version number.
     */
    public static final int MAJOR = 2;

    /**
     * Minor version number.
     */
    public static final int MINOR = 0;

    /**
     * Patch level.
     */
    public static final int PATCHLEVEL = 0;

    /**
     * Development version or release candidate?
     */
    public static final boolean IS_DEVELOPMENT = true;

    /**
     * Release candidate number. "0" indicates that the version is not a release
     * candidate.
     */
    public static final int RELEASE_CANDIDATE = 0;

    /**
     * Release date.
     */
    public static final String COMPUTED_DATE;

    public static final String DATE;

    private static final String COMPUTED_ECLIPSE_DATE;

    private static String applicationName = "<unknown>";

    private static String applicationVersion = "<unknown>";

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss z, dd MMMM, yyyy");
        SimpleDateFormat eclipseDateFormat = new SimpleDateFormat("yyyyMMdd");

        COMPUTED_DATE = dateFormat.format(new Date());
        COMPUTED_ECLIPSE_DATE = eclipseDateFormat.format(new Date());
    }

    /**
     * Preview release number. "0" indicates that the version is not a preview
     * release.
     */
    public static final int PREVIEW = 0;

    private static final String RELEASE_SUFFIX_WORD = (RELEASE_CANDIDATE > 0 ? "rc" + RELEASE_CANDIDATE
            : (PREVIEW > 0 ? "preview" + PREVIEW : "dev-" + COMPUTED_ECLIPSE_DATE));

    public static final String RELEASE_BASE = MAJOR + "." + MINOR + "." + PATCHLEVEL;

    /**
     * Release version string.
     */
    public static final String COMPUTED_RELEASE = RELEASE_BASE + (IS_DEVELOPMENT ? "-" + RELEASE_SUFFIX_WORD : "");

    /**
     * Release version string.
     */
    public static final String RELEASE;

    /**
     * Version of Eclipse plugin.
     */
    public static final String COMPUTED_ECLIPSE_UI_VERSION = RELEASE_BASE + "." + COMPUTED_ECLIPSE_DATE;

    static {
        InputStream in = null;
        String release, date;
        try {
            Properties versionProperties = new Properties();
            in = Version.class.getResourceAsStream("version.properties");
            if (in != null) {
                versionProperties.load(in);
            }
            release = (String) versionProperties.get("release.number");
            date = (String) versionProperties.get("release.date");
            if (release == null)
                release = COMPUTED_RELEASE;
            if (date == null)
                date = COMPUTED_DATE;
        } catch (RuntimeException e) {
            release = COMPUTED_RELEASE;
            date = COMPUTED_DATE;
        } catch (IOException e) {
            release = COMPUTED_RELEASE;
            date = COMPUTED_DATE;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                assert true; // nothing to do here
            }
        }
        RELEASE = release;
        DATE = date;
    }

    /**
     * FindBugs website.
     */
    public static final String WEBSITE = "http://findbugs.sourceforge.net";

    /**
     * Downloads website.
     */
    public static final String DOWNLOADS_WEBSITE = "http://prdownloads.sourceforge.net/findbugs";

    /**
     * Support email.
     */
    public static final String SUPPORT_EMAIL = "http://findbugs.sourceforge.net/reportingBugs.html";

    public static void registerApplication(String name, String version) {
        applicationName = name;
        applicationVersion = version;
    }

    public static @CheckForNull String getApplicationName() {
        return applicationName;
    }

    public static @CheckForNull String getApplicationVersion() {
        return applicationVersion;
    }

    public static void main(String[] argv) {
        if (argv.length != 1)
            usage();

        String arg = argv[0];
        if (!IS_DEVELOPMENT && RELEASE_CANDIDATE != 0) {
            throw new IllegalStateException("Non developmental version, but is release candidate " + RELEASE_CANDIDATE);
        }
        if (arg.equals("-release"))
            System.out.println(RELEASE);
        else if (arg.equals("-date"))
            System.out.println(DATE);
        else if (arg.equals("-props")) {
            System.out.println("release.base=" + RELEASE_BASE);
            System.out.println("release.number=" + COMPUTED_RELEASE);
            System.out.println("release.date=" + COMPUTED_DATE);
            System.out.println("eclipse.ui.version=" + COMPUTED_ECLIPSE_UI_VERSION);
            System.out.println("findbugs.website=" + WEBSITE);
            System.out.println("findbugs.downloads.website=" + DOWNLOADS_WEBSITE);
        } else {
            usage();
            System.exit(1);
        }
    }

    private static void usage() {
        System.err.println("Usage: " + Version.class.getName() + "  (-release|-date|-props)");
    }

    public static String getReleaseWithDateIfDev() {
        if (IS_DEVELOPMENT)
            return RELEASE + " (" + DATE + ")";
        return RELEASE;
    }
}

// vim:ts=4
