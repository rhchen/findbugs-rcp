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

package edu.umd.cs.findbugs.detect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.JavaClass;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.NonReportingDetector;
import edu.umd.cs.findbugs.SystemProperties;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.XFactory;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.ba.bcp.Invoke;
import edu.umd.cs.findbugs.bcel.BCELUtil;
import edu.umd.cs.findbugs.charsets.UTF8;
import edu.umd.cs.findbugs.util.Util;
import edu.umd.cs.findbugs.visitclass.AnnotationVisitor;

/**
 * @author William Pugh
 */

public class NoteCheckReturnValue extends AnnotationVisitor implements Detector, NonReportingDetector {

    // XXX: Hack, for now
    private static final String LOAD_TRAINING = SystemProperties.getProperty("findbugs.checkreturn.loadtraining");

    private static final String SAVE_TRAINING = SystemProperties.getProperty("findbugs.checkreturn.savetraining");

    private BugReporter bugReporter;

    private boolean checkLoad;

    private Set<XMethod> checkReturnValueDatabase;

    public NoteCheckReturnValue(BugReporter bugReporter) {
        AnalysisContext.currentAnalysisContext().getCheckReturnAnnotationDatabase(); // force
                                                                                     // creation
        this.bugReporter = bugReporter;
        if (SAVE_TRAINING != null) {
            checkReturnValueDatabase = new HashSet<XMethod>();
        }
    }

    public void visitClassContext(ClassContext classContext) {
        if (LOAD_TRAINING != null && !checkLoad) {
            loadTraining();
            checkLoad = true;
        }
        JavaClass javaClass = classContext.getJavaClass();
        if (!BCELUtil.preTiger(javaClass))
            javaClass.accept(this);
    }

    @Override
    public void visitAnnotation(String annotationClass, Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!annotationClass.endsWith("CheckReturnValue"))
            return;
        if (!visitingMethod())
            return;
        BCPMethodReturnCheck.addMethodWhoseReturnMustBeChecked("+" + getDottedClassName(), getMethodName(), getMethodSig(),
                getThisClass().isStatic() ? Invoke.STATIC : Invoke.ANY);

        if (SAVE_TRAINING != null) {
            checkReturnValueDatabase.add(XFactory.createXMethod(this));
        }
    }

    public void report() {
        if (SAVE_TRAINING != null) {
            saveTraining();
        }
    }

    private void loadTraining() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(Util.getFileReader(LOAD_TRAINING));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tuple = line.split(",");
                if (tuple.length != 4)
                    continue;
                BCPMethodReturnCheck.addMethodWhoseReturnMustBeChecked(tuple[0], tuple[1], tuple[2], Boolean.valueOf(tuple[3])
                        .booleanValue() ? Invoke.STATIC : Invoke.ANY);
            }
        } catch (IOException e) {
            bugReporter.logError("Couldn't load check return database");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    private void saveTraining() {
        BufferedWriter writer = null;
        try {
            writer = UTF8.bufferedWriter(SAVE_TRAINING);
            for (XMethod xmethod : checkReturnValueDatabase) {
                writer.write(xmethod.getClassName());
                writer.write(",");
                writer.write(xmethod.getName());
                writer.write(",");
                writer.write(xmethod.getSignature());
                writer.write(",");
                writer.write(String.valueOf(xmethod.getAccessFlags()));
                writer.write("\n");
            }
        } catch (IOException e) {
            bugReporter.logError("Couldn't write check return value training data", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

}
