/*
 * FindBugs - Find Bugs in Java programs
 * Copyright (C) 2003-2007 University of Maryland
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

package edu.umd.cs.findbugs.ba;

import edu.umd.cs.findbugs.SystemProperties;

/**
 * @author David Hovemeyer
 */
public class DefaultNullnessAnnotations {
    public static final boolean ICSE10_NULLNESS_PAPER = SystemProperties.getBoolean("icse10");

    /**
     * Add default NullnessAnnotations to given INullnessAnnotationDatabase.
     *
     * @param database
     *            an INullnessAnnotationDatabase
     */
    public static void addDefaultNullnessAnnotations(INullnessAnnotationDatabase database) {
        if (AnnotationDatabase.IGNORE_BUILTIN_ANNOTATIONS) {
            return;
        }

        boolean missingClassWarningsSuppressed = AnalysisContext.currentAnalysisContext().setMissingClassWarningsSuppressed(true);

        database.addDefaultAnnotation(AnnotationDatabase.Target.METHOD, "java.lang.String", NullnessAnnotation.NONNULL);
        database.addFieldAnnotation("java.lang.System", "out", "Ljava/io/PrintStream;", true, NullnessAnnotation.NONNULL);
        database.addFieldAnnotation("java.lang.System", "err", "Ljava/io/PrintStream;", true, NullnessAnnotation.NONNULL);
        database.addFieldAnnotation("java.lang.System", "in", "Ljava/io/InputStream;", true, NullnessAnnotation.NONNULL);

        database.addMethodAnnotation("java.lang.ref.ReferenceQueue", "poll", "()Ljava/lang/ref/Reference;", false,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodAnnotation("java.lang.ref.Reference", "get", "()Ljava/lang/Object;", false,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodAnnotation("java.lang.Class", "newInstance", "()Ljava/lang/Object;", false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", true,
                NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.lang.reflect.Method", "getParameterTypes", "()[Ljava/lang/Class;", false,
                NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.lang.Object", "clone", "()Ljava/lang/Object;", false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.lang.Object", "toString", "()Ljava/lang/String;", false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.lang.Object", "getClass", "()Ljava/lang/Class;", false, NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("java.lang.Object", "equals", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;", true, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.lang.Integer", "<init>", "(Ljava/lang/String;)V", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.lang.Integer", "parseInt", "(Ljava/lang/String;I)I", true, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.lang.Integer", "parseInt", "(Ljava/lang/String;)I", true, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.nio.channels.SocketChannel", "open", "()Ljava/nio/channels/SocketChannel;", true,
                NullnessAnnotation.NONNULL);

        database.addMethodAnnotation("java.sql.Statement", "executeQuery", "(Ljava/lang/String;)Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.sql.PreparedStatement", "executeQuery", "()Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.sql.Connection", "prepareStatement",
                "(Ljava/lang/String;)Ljava/sql/PreparedStatement;", false, NullnessAnnotation.NONNULL);
        database.addDefaultAnnotation(AnnotationDatabase.Target.METHOD, "java.sql.DatabaseMetaData", NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getConnection", "()Ljava/sql/Connection;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getAttributes",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getColumns",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getSuperTables",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getSuperTypes",
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getTimeDateFunctions", "()Ljava/lang/String;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getTypeInfo", "()Ljava/sql/ResultSet;", false,
                NullnessAnnotation.NULLABLE);
        database.addMethodAnnotation("java.sql.DatabaseMetaData", "getURL", "()Ljava/lang/String;", false,
                NullnessAnnotation.NULLABLE);

        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.package-info",
                NullnessAnnotation.NONNULL);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.CopyOnWriteArrayList",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.CopyOnWriteArraySet",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.ConcurrentLinkedQueue$Node",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.Exchanger",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.FutureTask",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.LinkedBlockingQueue$Node",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER,
                "java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask", NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.SynchronousQueue$WaitQueue",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.SynchronousQueue$Node",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.ThreadPoolExecutor$Worker",
                NullnessAnnotation.UNKNOWN_NULLNESS);

        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.AbstractExecutorService",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER,
                "java.util.concurrent.ConcurrentSkipListMap$ConcurrentSkipListSubMap", NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER,
                "java.util.concurrent.ConcurrentSkipListMap$HeadIndex", NullnessAnnotation.UNKNOWN_NULLNESS);

        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.ConcurrentSkipListMap$Index",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.ConcurrentSkipListMap$Node",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.ConcurrentSkipListMap$SubMap",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER,
                "java.util.concurrent.ConcurrentSkipListSet$ConcurrentSkipListSubSet", NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.LinkedBlockingDeque$Node",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.SynchronousQueue$TransferQueue",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER,
                "java.util.concurrent.SynchronousQueue$TransferQueue$QNode", NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.SynchronousQueue$TransferStack",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addDefaultAnnotation(AnnotationDatabase.Target.PARAMETER, "java.util.concurrent.SynchronousQueue$Transferer",
                NullnessAnnotation.UNKNOWN_NULLNESS);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "containsKey", "(Ljava/lang/Object;)Z",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "containsValue", "(Ljava/lang/Object;)Z",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "remove",
                "(Ljava/lang/Object;)Ljava/lang/Object;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 1, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentHashMap", "remove",
                "(Ljava/lang/Object;Ljava/lang/Object;)Z", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentMap", "remove",
                "(Ljava/lang/Object;Ljava/lang/Object;)Z", false, 1, NullnessAnnotation.NULLABLE);

        database.addMethodParameterAnnotation("java.util.concurrent.FutureTask", "<init>",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)V", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.Executors", "callable",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Callable;", true, 1,
                NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.ThreadPoolExecutor", "addWorker", "(Ljava/lang/Runnable;Z)Z",
                false, 0, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentHashMap$Segment", "remove",
                "(Ljava/lang/Object;ILjava/lang/Object;)Ljava/lang/Object;", false, 2, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.CyclicBarrier", "<init>", "(ILjava/lang/Runnable;)V", false,
                1, NullnessAnnotation.CHECK_FOR_NULL);
        
        

        database.addMethodParameterAnnotation("java.util.concurrent.BrokenBarrierException", "<init>",
                "(Ljava/lang/String;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.CancellationException", "<init>",
                "(Ljava/lang/String;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.ExecutionException", "<init>", "(Ljava/lang/String;)V",
                false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ExecutionException", "<init>",
                "(Ljava/lang/String;Ljava/lang/Throwable;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ExecutionException", "<init>",
                "(Ljava/lang/String;Ljava/lang/Throwable;)V", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ExecutionException", "<init>",
                "(Ljava/lang/Throwable;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.RejectedExecutionException", "<init>",
                "(Ljava/lang/String;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.RejectedExecutionException", "<init>",
                "(Ljava/lang/String;Ljava/lang/Throwable;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.RejectedExecutionException", "<init>",
                "(Ljava/lang/String;Ljava/lang/Throwable;)V", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.RejectedExecutionException", "<init>",
                "(Ljava/lang/Throwable;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.TimeoutException", "<init>", "(Ljava/lang/String;)V",
                false, 0, NullnessAnnotation.CHECK_FOR_NULL);

        
        database.addMethodParameterAnnotation("java.util.concurrent.Executors$RunnableAdapter", "<init>",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)V", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        
        
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentSkipListMap", "<init>",
                "(Ljava/util/Comparator;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ConcurrentSkipListMap", "doRemove",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 1, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinPool", "casBarrierStack",
                "(Ljava/util/concurrent/ForkJoinPool$WaitQueueNode;Ljava/util/concurrent/ForkJoinPool$WaitQueueNode;)Z", false,
                0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinPool", "casBarrierStack",
                "(Ljava/util/concurrent/ForkJoinPool$WaitQueueNode;Ljava/util/concurrent/ForkJoinPool$WaitQueueNode;)Z", false,
                1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinPool", "casSpareStack",
                "(Ljava/util/concurrent/ForkJoinPool$WaitQueueNode;Ljava/util/concurrent/ForkJoinPool$WaitQueueNode;)Z", false,
                1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinTask", "adapt",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/ForkJoinTask;", true, 1,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinTask", "awaitDone",
                "(Ljava/util/concurrent/ForkJoinWorkerThread;J)I", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinTask", "awaitDone",
                "(Ljava/util/concurrent/ForkJoinWorkerThread;Z)I", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinTask$AdaptedRunnable", "<init>",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)V", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinWorkerThread", "onTermination",
                "(Ljava/lang/Throwable;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ForkJoinWorkerThread", "setSlot",
                "([Ljava/util/concurrent/ForkJoinTask;ILjava/util/concurrent/ForkJoinTask;)V", true, 2,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue", "casCleanMe",
                "(Ljava/util/concurrent/LinkedTransferQueue$Node;Ljava/util/concurrent/LinkedTransferQueue$Node;)Z", false, 0,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue", "casCleanMe",
                "(Ljava/util/concurrent/LinkedTransferQueue$Node;Ljava/util/concurrent/LinkedTransferQueue$Node;)Z", false, 1,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue", "casHead",
                "(Ljava/util/concurrent/LinkedTransferQueue$Node;Ljava/util/concurrent/LinkedTransferQueue$Node;)Z", false, 0,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue", "xfer",
                "(Ljava/lang/Object;ZIJ)Ljava/lang/Object;", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue$Itr", "advance",
                "(Ljava/util/concurrent/LinkedTransferQueue$Node;)V", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue$Node", "casItem",
                "(Ljava/lang/Object;Ljava/lang/Object;)Z", false, 1, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.LinkedTransferQueue$Node", "casNext",
                "(Ljava/util/concurrent/LinkedTransferQueue$Node;Ljava/util/concurrent/LinkedTransferQueue$Node;)Z", false, 0,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.Phaser", "<init>", "(Ljava/util/concurrent/Phaser;)V", false,
                0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.Phaser", "<init>", "(Ljava/util/concurrent/Phaser;I)V",
                false, 0, NullnessAnnotation.CHECK_FOR_NULL);

        if (ICSE10_NULLNESS_PAPER) {
            database.addMethodAnnotation("java.util.HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false,
                    NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.Hashtable", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false,
                    NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false,
                    NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.SortedMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false,
                    NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.TreeMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false,
                    NullnessAnnotation.CHECK_FOR_NULL);
        }

        if (false) {
            database.addMethodAnnotation("java.util.concurrent.ConcurrentMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;",
                    false, NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.concurrent.ConcurrentMap", "remove",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.concurrent.ConcurrentMap", "putIfAbsent",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.concurrent.ConcurrentMap", "replace",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.concurrent.ConcurrentHashMap", "get",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.concurrent.ConcurrentHashMap", "remove",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
            database.addMethodAnnotation("java.util.concurrent.ConcurrentHashMap", "putIfAbsent",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
        }

        database.addMethodAnnotation("java.util.concurrent.locks.ReadWriteLock", "readLock",
                "()Ljava/util/concurrent/locks/Lock;", false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.util.concurrent.locks.ReadWriteLock", "writeLock",
                "()Ljava/util/concurrent/locks/Lock;", false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.util.concurrent.locks.ReentrantReadWriteLock", "readLock",
                "()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;", false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("java.util.concurrent.locks.ReentrantReadWriteLock", "writeLock",
                "()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;", false, NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("java.util.concurrent.ExecutorService", "submit",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Future;", false, 1,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.AbstractExecutorService", "submit",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Future;", false, 1,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ExecutorCompletionService", "submit",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Future;", false, 1,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.AbstractExecutorServiceNullnessAnnotationDatabase",
                "newTaskFor", "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Future;", false, 1,
                NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ExecutorCompletionService", "newTaskFor",
                "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/RunnableFuture;", false, 1,
                NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.concurrent.ThreadPoolExecutor", "addIfUnderCorePoolSize",
                "(Ljava/lang/Runnable;)Z", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ThreadPoolExecutor", "addThread",
                "(Ljava/lang/Runnable;)Ljava/lang/Thread;", false, 0, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodParameterAnnotation("java.util.concurrent.ThreadPoolExecutor", "afterExecute",
                "(Ljava/lang/Runnable;Ljava/lang/Throwable;)V", false, 1, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.EnumMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.EnumMap", "containsKey", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.EnumMap", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.EnumMap", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", false, 0,
                NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("java.util.SortedMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.SortedMap", "containsKey", "(Ljava/lang/Object;)Ljava/lang/Object;",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.SortedMap", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.SortedMap", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", false,
                0, NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("java.util.SortedSet", "add", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.SortedSet", "remove", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.SortedSet", "contains", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("java.util.Hashtable", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Hashtable", "containsKey", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Hashtable", "containsValue", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Hashtable", "contains", "(Ljava/lang/Object;)Z", false, 0,
                NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Hashtable", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Hashtable", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 1, NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("javax.swing.UIDefaults", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false, 1, NullnessAnnotation.CHECK_FOR_NULL);

        database.addMethodParameterAnnotation("java.util.Properties", "getProperty", "(Ljava/lang/String;)Ljava/lang/String;",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Properties", "setProperty",
                "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false, 1, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.util.Properties", "setProperty",
                "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false, 0, NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("org.w3c.dom.Element", "setAttribute", "(Ljava/lang/String;Ljava/lang/String;)V",
                false, 0, NullnessAnnotation.NONNULL);

        database.addMethodParameterAnnotation("java.text.DateFormat", "parse",
                "(Ljava/lang/String;Ljava/text/ParsePosition;)Ljava/util/Date;", false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("java.text.DateFormat", "parse", "(Ljava/lang/String;)Ljava/util/Date;", false, 0,
                NullnessAnnotation.NONNULL);

        // addMethodAnnotation("java.util.Queue", "poll",
        // "()Ljava/lang/Object;", false, NullnessAnnotation.CHECK_FOR_NULL);
        database.addMethodAnnotation("java.io.BufferedReader", "readLine", "()Ljava/lang/String;", false,
                NullnessAnnotation.CHECK_FOR_NULL);
        
        database.addMethodParameterAnnotation("com.google.common.base.Preconditions","checkNotNull","(Ljava/lang/Object;)Ljava/lang/Object;",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("com.google.common.base.Preconditions","checkNotNull","(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodParameterAnnotation("com.google.common.base.Preconditions","checkNotNull","(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;",
                false, 0, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("com.google.common.base.Preconditions","checkNotNull","(Ljava/lang/Object;)Ljava/lang/Object;",
                false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("com.google.common.base.Preconditions","checkNotNull","(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                false, NullnessAnnotation.NONNULL);
        database.addMethodAnnotation("com.google.common.base.Preconditions","checkNotNull","(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;",
                false, NullnessAnnotation.NONNULL);

        AnalysisContext.currentAnalysisContext().setMissingClassWarningsSuppressed(missingClassWarningsSuppressed);

    }
}
