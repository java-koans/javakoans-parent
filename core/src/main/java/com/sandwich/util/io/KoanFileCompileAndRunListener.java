/**
 * Copyright 2015 Java Koans
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.classloader.DynamicClassLoader;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.filecompiler.CompilerConfig;
import com.sandwich.util.io.filecompiler.FileCompiler;

public class KoanFileCompileAndRunListener implements FileListener {
	
	private Map<ArgumentType, CommandLineArgument> args = Collections.emptyMap();
	private KoanSuiteCompilationListener listener = new KoanSuiteCompilationListener();
	
	public KoanFileCompileAndRunListener(Map<ArgumentType, CommandLineArgument> args) throws IOException{
		this.args = args;
	}
	
	public void fileSaved(File file) {
		String absolutePath = file.getAbsolutePath();
		if(CompilerConfig.isSourceFile(absolutePath)){
			ApplicationUtils.getPresenter().displayMessage(KoanConstants.EOL+"loading: "+absolutePath);
			File[] jars = new File(DirectoryManager.getProjectLibraryDir()).listFiles();
            String[] classPath = new String[jars.length];
            for (int i = 0; i < jars.length; i++) {
                String jarPath = jars[i].getAbsolutePath();
                String jarPathLowerCase = jarPath.toLowerCase();
                if(jarPathLowerCase.endsWith(".jar") || jarPathLowerCase.endsWith(".war")){
                	classPath[i] = jarPath;
                }
            }
			try {
				FileCompiler.compile(file, 
					new File(DirectoryManager.getBinDir()),
					listener,
					ApplicationSettings.getFileCompilationTimeoutInMs(),
					classPath);
				DynamicClassLoader.remove(FileCompiler.sourceToClass(
						DirectoryManager.getSourceDir(), DirectoryManager.getBinDir(), file).toURI().toURL());
				if(!listener.isLastCompilationAttemptFailure()){
					ApplicationUtils.getPresenter().clearMessages();
					new CommandLineArgumentRunner(args).run();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void newFile(File file) {
		
	}

	public void fileDeleted(File file) {
		
	}
}
