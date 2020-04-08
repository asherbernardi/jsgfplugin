package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.reference.ImportNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileNameReference;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import net.sf.cglib.asm.$FieldVisitor;
import org.jetbrains.annotations.NotNull;

public class ImportNameElement extends RuleNameElement {
  private OtherFileNameReference reference;

  public ImportNameElement(@NotNull ASTNode node) {
    super(node);
  }

  public boolean isStarImport() {
    return getUnqualifiedName().equals("*");
  }

  public String getUnqualifiedName() {
    int lastDot = getName().lastIndexOf('.');
    return getName().substring(lastDot + 1);
  }

  public String getFullyQualifiedGrammarName() {
    int lastDot = getName().lastIndexOf('.');
    return getName().substring(0, lastDot == -1 ? 0 : lastDot);
  }

  public String getSimpleGrammarName() {
    String fqgn = getFullyQualifiedGrammarName();
    int lastDot = fqgn.lastIndexOf('.');
    return fqgn.substring(lastDot + 1);
  }

  public String getPackageName() {
    String fqgn = getFullyQualifiedGrammarName();
    int lastDot = fqgn.lastIndexOf('.');
    return fqgn.substring(0, lastDot == -1 ? 0 : lastDot);
  }

  @Override
  public PsiReference getReference() {
    TextRange range = new TextRange(0, getName().length());
    if (reference == null)
      reference = new ImportNameReference(this, range);
    return reference;
  }

  @Override
  public String toString() {
    return "ImportName: <" + getName() + ">";
  }
}
