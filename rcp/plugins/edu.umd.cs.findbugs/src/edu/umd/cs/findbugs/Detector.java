/*
 * FindBugs - Find bugs in Java programs
 * Copyright (C) 2003,2004 University of Maryland
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

import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.core.Priorities;

/**
 * The interface which all bug pattern detectors must implement.
 */
public interface Detector extends Priorities {

    /**
     * Visit the ClassContext for a class which should be analyzed for instances
     * of bug patterns.
     * 
     * @param classContext
     *            the ClassContext
     */
    public void visitClassContext(ClassContext classContext);

    /**
     * This method is called after all classes to be visited. It should be used
     * by any detectors which accumulate information over all visited classes to
     * generate results.
     */
    public void report();
}

// vim:ts=4
