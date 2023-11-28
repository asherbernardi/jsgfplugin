package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.AbstractJsgfPsiElement;
import com.asherbernardi.jsgfplugin.psi.reference.RuleNameReference;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceName;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.AbstractJsgfPsiElement;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceName;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

/**
 * @author asherbernardi
 */
public abstract class RuleReferenceNameMixin extends AbstractJsgfPsiElement implements JsgfRuleReferenceName {

  public RuleReferenceNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * Returns an array of length 1 or 2. If the rule is unqualified, it will be length 1 and the
   * element will be a single RuleNameReference. If the rule is qualified or fully-qualified, the
   * first element will be a GrammarNameReference, and the second a RuleNameReference.
   *
   * @return either just a RuleNameReference, or a RulenameReference and a GrammarNameReference
   */
  @NotNull
  public PsiReference @NotNull [] refs() {
    return getReferencePair().getReferenceArray();
  }

  @Override
  public String getFQRN() {
    return getText();
  }

  @Override
  public String toString() {
    return "RuleReferenceName: <" + getText() + ">";
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {

      @Override
      public String getPresentableText() {
        LeafElement firstLeaf = JsgfUtil.descendToFirstLeaf(getNode());
        if (firstLeaf == null) return "";
        StringBuilder pres = new StringBuilder(firstLeaf.getText());
        final int wsCuttoff = 1;
        int wsCount = 0;
        for (LeafElement prev : JsgfUtil.leafIterablePrev(firstLeaf)) {
          if (prev instanceof PsiWhiteSpace) {
            if (++wsCount > wsCuttoff || prev.textContains('\n')) {
              break;
            }
          }
          pres.insert(0, prev.getText());
        }
        wsCount = 0;
        for (LeafElement next : JsgfUtil.leafIterableNext(firstLeaf)) {
          if (next instanceof PsiWhiteSpace) {
            if (++wsCount > wsCuttoff || next.textContains('\n')) {
              break;
            }
          }
          pres.append(next.getText());
        }
        return pres.toString();
      }

      @Override
      public String getLocationString() {
        VirtualFile vFile = getContainingFile().getVirtualFile();
        if (vFile == null) return null;
        Document d = FileDocumentManager.getInstance().getDocument(vFile);
        if (d == null) return null;
        int line = d.getLineNumber(getTextOffset()) + 1;
        return vFile.getName() + ":" + line;
      }

      @Override
      public Icon getIcon(boolean unused) {
        return JsgfIcons.FILE;
      }
    };
  }

  public @NotNull SearchScope getUseScope() {
    JsgfRuleDeclarationName resolve = (JsgfRuleDeclarationName) getReference().resolve();
    if (resolve != null && resolve.isPublicRule()) {
      return GlobalSearchScope.allScope(getProject());
    }
    return new LocalSearchScope(getContainingFile());
  }
}
