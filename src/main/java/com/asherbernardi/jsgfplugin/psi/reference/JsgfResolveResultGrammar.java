package com.asherbernardi.jsgfplugin.psi.reference;

import com.intellij.psi.PsiElementResolveResult;
import com.asherbernardi.jsgfplugin.psi.GrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import org.jetbrains.annotations.NotNull;

public class JsgfResolveResultGrammar extends PsiElementResolveResult {

  private final JsgfRuleImportName importer;

  public JsgfResolveResultGrammar(@NotNull GrammarName element, JsgfRuleImportName importer) {
    super(element);
    this.importer = importer;
  }

  public JsgfResolveResultGrammar(@NotNull JsgfFile element, JsgfRuleImportName importer) {
    super(element);
    this.importer = importer;
  }

  public JsgfRuleImportName getImporter() {
    return importer;
  }
}
