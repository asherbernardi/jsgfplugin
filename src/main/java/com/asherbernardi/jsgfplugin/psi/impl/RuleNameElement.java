package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import javax.swing.Icon;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.intellij.adaptor.psi.Trees;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RuleNameElement extends ANTLRPsiNode implements PsiNamedElement {

  public RuleNameElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String getName() {
    return getText();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    int ttype = JsgfLexer.RULE_NAME_IDENTIFIER;
    PsiElement parsed = Trees.createLeafFromText(getProject(), JsgfLanguage.INSTANCE,
        getContext(), '<' + name + '>', JsgfTypes.getTokenElementType(ttype));
    ASTNode newNameNode = parsed.getNextSibling().getNode();
    if (newNameNode != null) {
      // Only replace if the text was parsed properly
      if (name.equals(newNameNode.getText()))
        getNode().replaceAllChildrenToChildrenOf(newNameNode);
    }
    return this;
  }

  /**
   * @return An object representing how to display this element when listed
   */
  @Override
  public ItemPresentation getPresentation() {
    RuleNameElement oldThis = this;
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

      @Nullable
      @Override
      public Icon getIcon(boolean unused) {
        return JsgfIcons.FILE;
      }
    };
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    RuleDeclarationNameElement resolve;
    if (this instanceof RuleDeclarationNameElement)
      resolve = (RuleDeclarationNameElement) this;
    // an import is by definition global
    else if (this instanceof ImportNameElement)
      return GlobalSearchScope.allScope(getProject());
    // regular rule references always resolve to a RuleDeclarationNameElement
    else
      resolve = (RuleDeclarationNameElement) getReference().resolve();
    if (resolve != null && resolve.isPublicRule()) {
      return GlobalSearchScope.allScope(getProject());
    }
    return new LocalSearchScope(getContainingFile());
  }

  public abstract String toString();
}