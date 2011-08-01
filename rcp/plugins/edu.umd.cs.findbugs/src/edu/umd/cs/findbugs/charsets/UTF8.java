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

package edu.umd.cs.findbugs.charsets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.annotation.WillCloseWhenClosed;

/**
 * @author pugh
 */
public class UTF8 {

    public static final Charset charset;

    static {
        charset = Charset.forName("UTF-8");
    }

    public static Writer writer(OutputStream out) throws IOException {
        return new OutputStreamWriter(out, charset);
    }
    public static Writer fileWriter(File fileName) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(fileName), charset);
    }
    public static BufferedWriter bufferedWriter(File fileName) throws IOException {
        return new BufferedWriter(fileWriter(fileName));
    }
    public static PrintWriter printWriter(File fileName) throws IOException {
        return new PrintWriter(bufferedWriter(fileName));
    }

    public static Writer fileWriter(String fileName) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(fileName), charset);
    }
    public static BufferedWriter bufferedWriter(String fileName) throws IOException {
        return new BufferedWriter(fileWriter(fileName));
    }
    public static PrintWriter printWriter(String fileName) throws IOException {
        return new PrintWriter(bufferedWriter(fileName));
    }

    public static Reader reader(@WillCloseWhenClosed InputStream in) {
        return new InputStreamReader(in, charset);
    }

    public static BufferedReader bufferedReader(@WillCloseWhenClosed InputStream in) {
        return new BufferedReader(reader(in));
    }

}
