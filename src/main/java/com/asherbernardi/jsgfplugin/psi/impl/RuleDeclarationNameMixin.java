package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import com.intellij.ui.IconManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RuleDeclarationNameMixin extends StubBasedPsiElementBase<RuleDeclarationStub>
    implements JsgfRuleDeclarationName, PsiNameIdentifierOwner {

  public RuleDeclarationNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public RuleDeclarationNameMixin(@NotNull RuleDeclarationStub stub,
      @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String getFQRN() {
    RuleDeclarationStub stub = getStub();
    if (stub != null) return stub.getName();
    return getText();
  }

  @Override
  public String getName() {
    return getFQRN();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return setFQRN(name);
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

  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        return "<" + getRuleName() + ">";
      }

      @Override
      public String getLocationString() {
        PsiFile file = getContainingFile();
        return file.getName();
      }

      /**
       * This is the icon which gets called when viewing the structure, we
       * want to make sure the public icon is accurate here
       * @param unused unused...
       * @return an icon, with an extra 'public' icon added if the rule is public
       */
      @Override
      public Icon getIcon(boolean unused) {
        if (isPublicRule()) {
          return IconManager.getInstance().createRowIcon(JsgfIcons.FILE, PlatformIcons.PUBLIC_ICON);
        }
        return JsgfIcons.FILE;
      }
    };
  }

  @Nullable
  public PsiElement getNameIdentifier() {
    return this;
  }

  @Override
  public @NotNull SearchScope getUseScope() {
    return isPublicRule()
        ? GlobalSearchScope.allScope(getProject())
        : new LocalSearchScope(getContainingFile());
  }

  @Override
  public String toString() {
    return "RuleDeclarationName: <" + getText() + ">";
  }
}
