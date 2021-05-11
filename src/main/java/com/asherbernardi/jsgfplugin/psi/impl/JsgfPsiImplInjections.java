package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.reference.ImportNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileNameReference;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.ui.IconManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.*;
import com.asherbernardi.jsgfplugin.psi.reference.RuleReferenceReference;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import java.util.List;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JsgfPsiImplInjections {

  /*
   **********   GrammarName methods  **********
   */

  static String getName(JsgfGrammarName grammarName) {
    final GrammarNameStub stub = grammarName.getStub();
    if (stub != null) {
      return stub.getName();
    }
    return grammarName.getText();
  }

  /*
   **********   ImportStatement methods  **********
   */

  static boolean isInclude(JsgfImportStatement importStatement) {
    return importStatement.getNode().getFirstChildNode().getElementType() == JsgfBnfTypes.INCLUDE;
  }

  static JsgfRuleImportName getRuleImportName(JsgfImportStatement importStatement) {
    JsgfRuleImport ruleImport = importStatement.getRuleImport();
    return ruleImport != null ? ruleImport.getRuleImportName() : null;
  }

  /*
   **********   ImportName methods  **********
   */

  static boolean isStarImport(JsgfRuleImportName importName) {
    return importName.getUnqualifiedRuleName().equals("*");
  }

  static String unqualifiedRuleNameFromFQRN(String fqrn) {
    int lastDot = fqrn.lastIndexOf('.');
    return fqrn.substring(lastDot + 1);
  }

  static String fullyQualifiedGrammarNameFromFQRN(String fqrn) {
    int lastDot = fqrn.lastIndexOf('.');
    return fqrn.substring(0, lastDot == -1 ? 0 : lastDot);
  }

  static String simpleGrammarNameFromFQRN(String fqrn) {
    String fqgn = fullyQualifiedGrammarNameFromFQRN(fqrn);
    int lastDot = fqgn.lastIndexOf('.');
    return fqgn.substring(lastDot + 1);
  }

  static String packageNameFromFQRN(String fqrn) {
    String fqgn = fullyQualifiedGrammarNameFromFQRN(fqrn);
    int lastDot = fqgn.lastIndexOf('.');
    return fqgn.substring(0, lastDot == -1 ? 0 : lastDot);
  }

  static String getUnqualifiedRuleName(JsgfRuleImportName importName) {
    return unqualifiedRuleNameFromFQRN(importName.getFullyQualifiedRuleName());
  }

  static String getFullyQualifiedGrammarName(JsgfRuleImportName importName) {
    return fullyQualifiedGrammarNameFromFQRN(importName.getFullyQualifiedRuleName());
  }

  static String getSimpleGrammarName(JsgfRuleImportName importName) {
    return simpleGrammarNameFromFQRN(importName.getFullyQualifiedRuleName());
  }

  static String getPackageName(JsgfRuleImportName importName) {
    return packageNameFromFQRN(importName.getFullyQualifiedRuleName());
  }

  @NotNull
  static OtherFileNameReference getReference(JsgfRuleImportName importName) {
    TextRange range = new TextRange(0, importName.getRuleName().length());
    return new ImportNameReference(importName, range);
  }

  @NotNull
  static SearchScope getUseScope(JsgfRuleImportName importName) {
    // an import is by definition global
    return GlobalSearchScope.allScope(importName.getProject());
  }

  /*
   **********   RuleReferenceName methods  **********
   */

  @NotNull
  static RuleReferenceReference getReference(JsgfRuleReferenceName ruleReferenceName) {
    TextRange range = new TextRange(0, ruleReferenceName.getRuleName().length());
    return new RuleReferenceReference(ruleReferenceName, range);
  }

  static @NotNull SearchScope getUseScope(JsgfRuleReferenceName ruleReferenceName) {
    JsgfRuleDeclarationName resolve = (JsgfRuleDeclarationName) ruleReferenceName.getReference().resolve();
    if (resolve != null && resolve.isPublicRule()) {
      return GlobalSearchScope.allScope(ruleReferenceName.getProject());
    }
    return new LocalSearchScope(ruleReferenceName.getContainingFile());
  }

  /*
   **********   RuleDeclarationName methods  **********
   */

  static String getName(JsgfRuleDeclarationName ruleDeclarationName) {
    final RuleDeclarationStub stub = ruleDeclarationName.getStub();
    if (stub != null) {
      return stub.getName();
    }
    return ruleDeclarationName.getRuleName();
  }

  static PsiElement setName(JsgfRuleDeclarationName ruleDeclarationName,
      @NotNull String name) throws IncorrectOperationException {
    return ruleDeclarationName.setRuleName(name);
  }

  static boolean isPublicRule(JsgfRuleDeclarationName ruleDeclarationName) {
    final RuleDeclarationStub stub = ruleDeclarationName.getStub();
    if (stub != null) {
      return stub.isPublicRule();
    }
    return ((JsgfRuleDefinition) ruleDeclarationName.getParent().getParent()).isPublicRule();
  }

  static ItemPresentation getPresentation(JsgfRuleDeclarationName ruleDeclarationName) {
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        return "<" + ruleDeclarationName.getRuleName() + ">";
      }

      @Override
      public String getLocationString() {
        PsiFile file = ruleDeclarationName.getContainingFile();
        return file == null ? "" : file.getName();
      }

      /**
       * This is the icon which gets called when viewing the structure, we
       * want to make sure the public icon is accurate here
       * @param unused unused...
       * @return an icon, with an extra 'public' icon added if the rule is public
       */
      @Override
      public Icon getIcon(boolean unused) {
        if (ruleDeclarationName.isPublicRule()) {
          return IconManager.getInstance().createRowIcon(JsgfIcons.FILE, PlatformIcons.PUBLIC_ICON);
        }
        return JsgfIcons.FILE;
      }
    };
  }

  /**
   * This is the icon which gets called when searching for symbols
   * @param flags unused...
   * @return an icon
   */
  @NotNull
  @SuppressWarnings("unused")
  static Icon getElementIcon(JsgfRuleDeclarationName ruleDeclarationName, int flags) {
    if (ruleDeclarationName.isPublicRule()) {
      return IconManager.getInstance().createRowIcon(JsgfIcons.FILE, PlatformIcons.PUBLIC_ICON);
    }
    return JsgfIcons.FILE;
  }

  @Nullable
  static PsiElement getNameIdentifier(JsgfRuleDeclarationName ruleDeclarationName) {
    return ruleDeclarationName;
  }

  @NotNull
  static SearchScope getUseScope(JsgfRuleDeclarationName ruleDeclarationName) {
    return ruleDeclarationName.isPublicRule()
        ? GlobalSearchScope.allScope(ruleDeclarationName.getProject())
        : new LocalSearchScope(ruleDeclarationName.getContainingFile());
  }

  /*
   **********   String methods  **********
   */

  static String getStringText(JsgfString string) {
    return string.getText().substring(1, string.getTextLength())
        .replace("\\\\", "\\")
        .replace("\\\"", "\"");
  }

  /*
   **********   Alternatives methods  **********
   */

  static List<PsiElement> getOrSymbols(JsgfAlternatives alternatives) {
    return ((AlternativesMixin) alternatives).getOrSymbols_();
  }

  /*
   **********   Sequence methods  **********
   */

  /**
   * @return the weight applied to the sequence if it exists, otherwise null
   */
  static Double getWeight(JsgfSequence sequence) {
    ASTNode weight = sequence.getNode().getTreeParent().findChildByType(JsgfBnfTypes.WEIGHT);
    if (weight != null) {
      return Double.parseDouble(weight.getText().substring(1, weight.getTextLength() - 1));
    }
    return null;
  }

  /*
   **********   Sequence methods  **********
   */

  static boolean isOptionalGroup(JsgfGroup group) {
    return group.getNode().getFirstChildNode().getElementType() == JsgfBnfTypes.LBRACK;
  }

  /*
   **********   RuleDefinition methods  **********
   */

  static String getRuleName(JsgfRuleDefinition ruleDefinition) {
    RuleDeclarationName ruleDeclarationName = ruleDefinition.getRuleDeclaration().getRuleDeclarationName();
    return ruleDeclarationName != null ? ruleDeclarationName.getRuleName() : null;
  }

  static boolean isPublicRule(JsgfRuleDefinition ruleDeclaration) {
    return ruleDeclaration.getFirstChild().getNode().getElementType() == JsgfBnfTypes.PUBLIC;
  }

}
