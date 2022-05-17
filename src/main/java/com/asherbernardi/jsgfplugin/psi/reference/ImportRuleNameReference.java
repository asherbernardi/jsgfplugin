package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.openapi.util.TextRange;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ImportRuleNameReference extends OtherFileRuleNameReference {

  public ImportRuleNameReference(@NotNull JsgfRuleImportName element, TextRange range) {
    super(element, range);
  }

  @NotNull
  @Override
  protected List<JsgfRuleDeclarationName> getRules(boolean publicOnly) {
    return JsgfUtil.findImportRules(myElement, publicOnly);
  }

  @NotNull
  @Override
  protected List<JsgfRuleDeclarationName> getRulesByPackage(boolean publicOnly) {
    return JsgfUtil.findImportRulesByPackage(myElement, publicOnly);
  }
}