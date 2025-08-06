package com.littlegig.app;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = LittleGigApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface LittleGigApplication_GeneratedInjector {
  void injectLittleGigApplication(LittleGigApplication littleGigApplication);
}
