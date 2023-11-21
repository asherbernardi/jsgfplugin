package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.PsiFileStubImpl;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;

public class JsgfPsiFileStubImpl extends PsiFileStubImpl<JsgfFile> implements JsgfPsiFileStub {

  public JsgfPsiFileStubImpl(JsgfFile file) {
    super(file);
  }
}
