package com.ngeen.component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @hidden
 * @author gbenmansour
 *
 */
public final class DynaCode {

	private static class LoadedClass {
		File binFile;

		String className;

		Class clazz;

		long lastModified;

		SourceDir srcDir;

		File srcFile;

		LoadedClass(String className, SourceDir src) throws Exception {
			this.className = className;
			this.srcDir = src;

			String path = className.replace('.', '/');
			this.srcFile = new File(src.srcDir, path + ".java");
			this.binFile = new File(src.binDir, path + ".class");

			compileAndLoadClass();
		}

		void compileAndLoadClass() throws Exception {

			if (clazz != null) {
				return; // class already loaded
			}

			// compile, if required
			String error = null;
			if (binFile.lastModified() < srcFile.lastModified()) {
				error = srcDir.javac.compile(new File[] { srcFile });
			}

			if (error != null) {
				Debugger.log("Failed to compile " + srcFile.getAbsolutePath() + ". Error: " + error);
				// return;
			}

			try {
				// load class
				clazz = srcDir.classLoader.loadClass(className);

				// load class success, remember timestamp
				lastModified = srcFile.lastModified();

			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Failed to load class " + srcFile.getAbsolutePath());
			}

			info("Init " + clazz);
		}

		boolean isChanged() {
			return srcFile.lastModified() != lastModified;
		}
	}

	private class MyInvocationHandler implements MethodInterceptor {

		Object backend;

		String backendClassName;

		MyInvocationHandler(String className) {
			backendClassName = className;

			try {
				Class clz = loadClass(backendClassName);
				backend = newDynaCodeInstance(clz);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object intercept(Object proxy, Method method, Object[] args, MethodProxy arg3) throws Throwable {

			// check if class has been updated
			Class clz = loadClass(backendClassName);
			if (backend.getClass() != clz) {
				backend = newDynaCodeInstance(clz);
			}
			Script scr = ((Script) backend);
			scr.holder = holder;
			scr.ng = ng;
			scr.onInit();

			try {
				// invoke on backend
				return method.invoke(backend, args);

			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

		private Object newDynaCodeInstance(Class clz) {
			try {
				return clz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to new instance of class " + clz.getName(), e);
			}
		}

	}

	private class SourceDir {
		File binDir;

		URLClassLoader classLoader;

		Javac javac;

		File srcDir;

		SourceDir(File srcDir) {
			this.srcDir = srcDir;

			String subdir = srcDir.getAbsolutePath().replace(':', '_').replace('/', '_').replace('\\', '_');
			this.binDir = new File(System.getProperty("java.io.tmpdir"), "scripts/" + subdir);
			this.binDir.mkdirs();

			// prepare compiler
			this.javac = new Javac(compileClasspath, binDir.getAbsolutePath());

			// class loader
			recreateClassLoader();
		}

		void recreateClassLoader() {
			try {
				classLoader = new URLClassLoader(new URL[] { binDir.toURL() }, parentClassLoader);
			} catch (MalformedURLException e) {
				// should not happen
			}
		}

	}

	/**
	 * Extracts a classpath string from a given class loader. Recognizes only
	 * URLClassLoader.
	 */
	private static String extractClasspath(ClassLoader cl) {
		StringBuffer buf = new StringBuffer();

		while (cl != null) {
			if (cl instanceof URLClassLoader) {
				URL urls[] = ((URLClassLoader) cl).getURLs();
				for (int i = 0; i < urls.length; i++) {
					if (buf.length() > 0) {
						buf.append(File.pathSeparatorChar);
					}
					buf.append(urls[i].getFile().toString());
				}
			}
			cl = cl.getParent();
		}

		return buf.toString();
	}

	/**
	 * Log a message.
	 */
	private static void info(String msg) {
		System.out.println(msg);
	}
	private String compileClasspath;

	private final Entity holder;

	// class name => LoadedClass
	private HashMap loadedClasses = new HashMap();

	private final Ngeen ng;

	private ClassLoader parentClassLoader;

	private ArrayList sourceDirs = new ArrayList();

	public DynaCode(ClassLoader parentClassLoader, Ngeen ng, Entity ent) {
		this(extractClasspath(parentClassLoader), parentClassLoader, ng, ent);

	}

	public DynaCode(Ngeen ng, Entity ent) {
		this(Thread.currentThread().getContextClassLoader(), ng, ent);
	}

	/**
	 * @param compileClasspath
	 *            used to compile dynamic classes
	 * @param parentClassLoader
	 *            the parent of the class loader that loads all the dynamic
	 *            classes
	 */
	public DynaCode(String compileClasspath, ClassLoader parentClassLoader, Ngeen ng, Entity ent) {
		this.compileClasspath = compileClasspath;
		this.parentClassLoader = parentClassLoader;
		this.ng = ng;
		this.holder = ent;
	}

	/**
	 * Add a directory that contains the source of dynamic java code.
	 * 
	 * @param srcDir
	 * @return true if the add is successful
	 */
	public boolean addSourceDir(File srcDir) {

		try {
			srcDir = srcDir.getCanonicalFile();
		} catch (IOException e) {
			// ignore
		}

		synchronized (sourceDirs) {

			// check existence
			for (int i = 0; i < sourceDirs.size(); i++) {
				SourceDir src = (SourceDir) sourceDirs.get(i);
				if (src.srcDir.equals(srcDir)) {
					return false;
				}
			}

			// add new
			SourceDir src = new SourceDir(srcDir);
			sourceDirs.add(src);

			// info("Add source dir " + srcDir);
		}

		return true;
	}

	/**
	 * Returns the up-to-date dynamic class by name.
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 *             if source file not found or compilation error
	 */
	public Class loadClass(String className) throws Exception {

		LoadedClass loadedClass = null;
		synchronized (loadedClasses) {
			loadedClass = (LoadedClass) loadedClasses.get(className);
		}

		// first access of a class
		if (loadedClass == null) {

			String resource = className.replace('.', '/') + ".java";
			SourceDir src = locateResource(resource);
			if (src == null) {
				throw new ClassNotFoundException("Class not found " + className);
			}

			synchronized (this) {

				// compile and load class
				loadedClass = new LoadedClass(className, src);

				synchronized (loadedClasses) {
					loadedClasses.put(className, loadedClass);
				}
			}

			return loadedClass.clazz;
		}

		// subsequent access
		if (loadedClass.isChanged()) {
			// unload and load again
			unload(loadedClass.srcDir);
			return loadClass(className);
		}

		return loadedClass.clazz;
	}

	private SourceDir locateResource(String resource) {
		for (int i = 0; i < sourceDirs.size(); i++) {
			SourceDir src = (SourceDir) sourceDirs.get(i);
			if (new File(src.srcDir, resource).exists()) {
				return src;
			}
		}
		return null;
	}

	/**
	 * Create a proxy instance that implements the specified access interface
	 * and delegates incoming invocations to the specified dynamic
	 * implementation. The dynamic implementation may change at run-time, and
	 * the proxy will always delegates to the up-to-date implementation.
	 * 
	 * @param className
	 *            the access interface
	 * @param implClassName
	 *            the backend dynamic implementation
	 * @return
	 * @throws RuntimeException
	 *             if an instance cannot be created, because of class not found
	 *             for example
	 */
	public Object newClassInstance(Class className, String implClassName) throws RuntimeException {

		final Enhancer e = new Enhancer();
		MyInvocationHandler handler = new MyInvocationHandler(implClassName);
		e.setSuperclass(className);
		e.setCallback(handler);
		Object obj = e.create();

		return obj;
	}

	private void unload(SourceDir src) {
		// clear loaded classes
		synchronized (loadedClasses) {
			for (Iterator iter = loadedClasses.values().iterator(); iter.hasNext();) {
				LoadedClass loadedClass = (LoadedClass) iter.next();
				if (loadedClass.srcDir == src) {
					iter.remove();
				}
			}
		}

		// create new class loader
		src.recreateClassLoader();
	}

}