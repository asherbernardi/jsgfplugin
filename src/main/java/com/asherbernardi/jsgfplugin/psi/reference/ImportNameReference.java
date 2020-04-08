package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ImportNameReference extends OtherFileNameReference {

  public ImportNameReference(@NotNull ImportNameElement element, TextRange range) {
    super(element, range);
  }

  @Override
  protected List<PsiElement> getRules() {
    return JsgfUtil.findImportRules(myElement.getProject(), myElement);
  }

  @Override
  protected List<PsiElement> getRulesByPackage() {
    return JsgfUtil.findImportRulesByPackage(myElement.getProject(), myElement);
  }
}