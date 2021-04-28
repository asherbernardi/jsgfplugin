package com.asherbernardi.jsgfplugin.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public interface RuleName extends PsiElement, NavigatablePsiElement {

  default String getRuleName() {
    return getText();
  }

  default PsiElement setRuleName(@NotNull String name) throws IncorrectOperationException {
    //TODO
//    int ttype = JsgfBnfTypes.RULE_NAME_IDENTIFIER;
//    if (this instanceof ImportNameElement &&((ImportNameElement) this).isStarImport())
//        throw new IncorrectOperationException("Cannot rename an import of all rules in a grammar");
//    PsiElement parsed = Trees.createLeafFromText(getProject(), JsgfLanguage.INSTANCE,
//        getContext(), '<' + name + '>', JsgfTypes.getTokenElementType(ttype));
//    ASTNode newNameNode = parsed.getNextSibling().getNode();
//    if (newNameNode != null) {
//      // Only replace if the text was parsed properly
//      if (name.equals(newNameNode.getText()))
//        getNode().replaceAllChildrenToChildrenOf(newNameNode);
//    }
    return this;
  }

  default ItemPresentation getPresentation() {
    RuleName oldThis = this;
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        return "<" + getRuleName() + ">";
      }

      @Override
      public String getLocationString() {
        PsiFile file = getContainingFile();
        return file == null ? "" : file.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return JsgfIcons.FILE;
      }
    };
  }

  @NotNull
  @Override
  SearchScope getUseScope();

  String toString();
}