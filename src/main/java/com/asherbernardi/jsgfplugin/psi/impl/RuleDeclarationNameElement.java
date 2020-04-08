package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.ui.IconManager;
import com.intellij.util.PlatformIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleDeclarationNameElement extends RuleNameElement implements
    PsiNameIdentifierOwner {

  public RuleDeclarationNameElement(@NotNull ASTNode node) {
    super(node);
  }

  public boolean isPublicRule() {
    return ((RuleDeclarationSubtree) getParent()).isPublicRule();
  }

  /**
   * @return An object representing how to display this element when listed
   */
  @Override
  public ItemPresentation getPresentation() {
    RuleDeclarationNameElement oldThis = this;
    return new ItemPresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        return "<" + getName() + ">";
      }

      @Nullable
      @Override
      public String getLocationString() {
        PsiFile file = getContainingFile();
        return file == null ? "" : file.getName();
      }

      /**
       * This is the icon which gets called when viewing the structure, we
       * want to make sure the public icon is accurate here
       * @param unused unused...
       * @return an icon, with an extra 'public' icon added if the rule is public
       */
      @Nullable
      @Override
      public Icon getIcon(boolean unused) {
        if (isPublicRule())
          return IconManager.getInstance().createRowIcon(JsgfIcons.FILE, PlatformIcons.PUBLIC_ICON);
        return JsgfIcons.FILE;
      }
    };
  }

  /**
   * This is the icon which gets called when searching for symbols
   * @param flags unused...
   * @return an icon
   */
  @Nullable
  @Override
  protected Icon getElementIcon(int flags) {
    if (isPublicRule())
      return IconManager.getInstance().createRowIcon(JsgfIcons.FILE, PlatformIcons.PUBLIC_ICON);
    return JsgfIcons.FILE;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return this;
  }

  @Override
  public String toString() {
    return "RuleDeclarationName: <" + getName() + ">";
  }
}
