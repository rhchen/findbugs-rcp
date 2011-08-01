/*
 * FindBugs - Find Bugs in Java programs
 * Copyright (C) 2003-2008 University of Maryland
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author pugh
 */
public class FindBugsMain {
    
    public FindBugsMain(Class<?> mainClass, String cmd, String kind, boolean analysis) 
            throws SecurityException, NoSuchMethodException {
        this.mainClass = mainClass;
        mainMethod = mainClass.getMethod("main", String[].class);
        this.cmd = cmd;
        this.kind = kind;
        this.analysis = analysis;
    }
    final Class<?> mainClass;
    final Method mainMethod;
    final String cmd;
    final String kind;
    final boolean analysis;
    
    public void invoke(String [] args) throws Exception {
        if (!analysis)
            FindBugs.setNoAnalysis();
        mainMethod.invoke(null, (Object) args);
    }
    

}
