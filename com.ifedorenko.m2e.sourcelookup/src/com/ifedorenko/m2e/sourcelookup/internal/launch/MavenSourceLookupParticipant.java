/*******************************************************************************
 * Copyright (c) 2011-2016 Igor Fedorenko
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Igor Fedorenko - initial API and implementation
 *******************************************************************************/
package com.ifedorenko.m2e.sourcelookup.internal.launch;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.sourcelookup.ISourceLookupDirector;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectChangedListener;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;

import com.ifedorenko.jdt.launching.sourcelookup.advanced.AdvancedSourceLookupParticipant;


public class MavenSourceLookupParticipant extends AdvancedSourceLookupParticipant implements IMavenProjectChangedListener {

  @Override
  public void init(ISourceLookupDirector director) {
    super.init(director);
    MavenPlugin.getMavenProjectRegistry().addMavenProjectChangedListener(this);
  }

  @Override
  public void dispose() {
    MavenPlugin.getMavenProjectRegistry().removeMavenProjectChangedListener(this);
    super.dispose();
  }

  @Override
  public void mavenProjectChanged(MavenProjectChangedEvent[] events, IProgressMonitor monitor) {
    disposeContainers();
  }
}
