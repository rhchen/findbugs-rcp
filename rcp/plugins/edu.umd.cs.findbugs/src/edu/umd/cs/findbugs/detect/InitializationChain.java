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

package edu.umd.cs.findbugs.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.SystemProperties;

public class InitializationChain extends BytecodeScanningDetector {
    Set<String> requires = new TreeSet<String>();

    Map<String, Set<String>> classRequires = new TreeMap<String, Set<String>>();

    Set<String> staticFieldsAccessedInConstructor = new HashSet<String>();

    Map<String, BugInstance> staticFieldWritten = new HashMap<String, BugInstance>();

    private BugReporter bugReporter;

    private boolean instanceCreated;

    private int instanceCreatedPC;

    private boolean instanceCreatedWarningGiven;

    private static final boolean DEBUG = SystemProperties.getBoolean("ic.debug");

    public InitializationChain(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visit(Code obj) {
        instanceCreated = false;
        instanceCreatedWarningGiven = false;
        if (!getMethodName().equals("<clinit>") && !getMethodName().equals("<init>"))
            return;
        super.visit(obj);
        requires.remove(getDottedClassName());
        if (getDottedClassName().equals("java.lang.System")) {
            requires.add("java.io.FileInputStream");
            requires.add("java.io.FileOutputStream");
            requires.add("java.io.BufferedInputStream");
            requires.add("java.io.BufferedOutputStream");
            requires.add("java.io.PrintStream");
        }
        if (!requires.isEmpty()) {
            classRequires.put(getDottedClassName(), requires);
            requires = new TreeSet<String>();
        }
    }

    @Override
    public void visitAfter(JavaClass obj) {
        for (String name : staticFieldsAccessedInConstructor) {
            BugInstance bug = staticFieldWritten.get(name);
            if (bug != null)
                bugReporter.reportBug(bug);
        }
        staticFieldWritten.clear();
        staticFieldsAccessedInConstructor.clear();

    }

    @Override
    public void sawOpcode(int seen) {

        if (getMethodName().equals("<init>")) {
            if (seen == GETSTATIC && getClassConstantOperand().equals(getClassName())) {
                staticFieldsAccessedInConstructor.add(getNameConstantOperand());
            }
            return;
        }

        if (seen == PUTSTATIC && getClassConstantOperand().equals(getClassName())) {
            // Don't do this check; it generates too many false
            // positives. We need to do a more detailed check
            // of which variables could be seen.
            if (instanceCreated && !instanceCreatedWarningGiven && !getSuperclassName().equals("java.lang.Enum")) {
                String okSig = "L" + getClassName() + ";";
                if (!okSig.equals(getSigConstantOperand())) {
                    staticFieldWritten.put(getNameConstantOperand(),
                            new BugInstance(this, "SI_INSTANCE_BEFORE_FINALS_ASSIGNED", NORMAL_PRIORITY).addClassAndMethod(this)
                                    .addReferencedField(this).addSourceLine(this, instanceCreatedPC));
                    instanceCreatedWarningGiven = true;
                }
            }
        } else if (seen == NEW && getClassConstantOperand().equals(getClassName())) {
            instanceCreated = true;
            instanceCreatedPC = getPC();
        } else if (seen == PUTSTATIC || seen == GETSTATIC || seen == INVOKESTATIC || seen == NEW)
            if (getPC() + 6 < codeBytes.length)
                requires.add(getDottedClassConstantOperand());
    }

    public void compute() {
        Set<String> allClasses = classRequires.keySet();
        Set<String> emptyClasses = new TreeSet<String>();
        for (String c : allClasses) {
            Set<String> needs = classRequires.get(c);
            needs.retainAll(allClasses);
            Set<String> extra = new TreeSet<String>();
            for (String need : needs)
                extra.addAll(classRequires.get(need));
            needs.addAll(extra);
            needs.retainAll(allClasses);
            classRequires.put(c, needs);
            if (needs.isEmpty())
                emptyClasses.add(c);
        }
        for (String c : emptyClasses) {
            classRequires.remove(c);
        }
    }

    @Override
    public void report() {

        if (DEBUG)
            System.out.println("Finishing computation");
        compute();
        compute();
        compute();
        compute();
        compute();
        compute();
        compute();
        compute();
        Set<String> allClasses = classRequires.keySet();

        for (String c : allClasses) {
            if (DEBUG)
                System.out.println("Class " + c + " requires:");
            for (String needs : (classRequires.get(c))) {
                if (DEBUG)
                    System.out.println("  " + needs);
                Set<String> s = classRequires.get(needs);
                if (s != null && s.contains(c) && c.compareTo(needs) < 0)
                    bugReporter.reportBug(new BugInstance(this, "IC_INIT_CIRCULARITY", NORMAL_PRIORITY).addClass(c).addClass(
                            needs));
            }
        }
    }

}
