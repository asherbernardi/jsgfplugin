package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import com.intellij.ui.IconManager;
import com.intellij.util.PlatformIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public abstract class RuleDeclarationNameMixin extends StubBasedPsiElementBase<RuleDeclarationStub>
    implements JsgfRuleDeclarationName, PsiNameIdentifierOwner {

  public RuleDeclarationNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public RuleDeclarationNameMixin(@NotNull RuleDeclarationStub stub,
      @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  /**
   * This is the icon which gets called when searching for symbols
   * @param flags unused...
   * @return an icon
   */
  @NotNull
  @Override
  protected Icon getElementIcon(int flags) {
    if (isPublicRule()) {
      return IconManager.getInstance().createRowIcon(JsgfIcons.FILE, PlatformIcons.PUBLIC_ICON);
    }
    return JsgfIcons.FILE;
  }

  @Override
  public String toString() {
    return "RuleDeclarationName: <" + getText() + ">";
  }
}
