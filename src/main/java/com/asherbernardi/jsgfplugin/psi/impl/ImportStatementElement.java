package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;

public class ImportStatementElement extends IdentifierDefSubtree {

  public ImportStatementElement(@NotNull ASTNode node, @NotNull IElementType idElementTyp) {
    super(node, idElementTyp);
  }

  public ImportNameElement getImportName() {
    return PsiTreeUtil.findChildrenOfType(this, ImportNameElement.class)
        .toArray(new ImportNameElement[1])[0];
  }

  @Override
  public String toString() {
    return "ImportStatement: <" + getImportName().getName() + ">";
  }
}
