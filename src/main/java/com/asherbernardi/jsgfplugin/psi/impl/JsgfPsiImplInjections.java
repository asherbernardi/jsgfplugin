package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.reference.GrammarNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.ImportRuleNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.LocalReferencePair;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileGrammarNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileReferencePair;
import com.asherbernardi.jsgfplugin.psi.reference.RuleNameReference;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.StubElement;
import com.intellij.ui.IconManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.*;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import java.util.List;
import java.util.function.Function;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JsgfPsiImplInjections {

  /*
   **********   GrammarName methods  **********
   */

  @NotNull
  static String getName(JsgfGrammarName grammarName) {
    return stubify(grammarName, GrammarNameStub::getFQGN, JsgfGrammarName::getFQGN);
  }

  static PsiElement setName(JsgfGrammarName grammarName, @NotNull String newName) {
    return grammarName.setFQGN(newName);
  }

  static @NotNull SearchScope getUseScope(JsgfGrammarName grammarName) {
    return GlobalSearchScope.allScope(grammarName.getProject());
  }

  static ItemPresentation getPresentation(JsgfGrammarName grammarName) {
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        return grammarName.getName();
      }

      @Override
      public String getLocationString() {
        PsiFile file = grammarName.getContainingFile();
        return file == null ? "" : file.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return JsgfIcons.FILE;
      }
    };
  }

  @Nullable
  static PsiElement getNameIdentifier(JsgfGrammarName grammarName) {
    return grammarName;
  }

  /*
   **********   ImportStatement methods  **********
   */

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
    return simpleGrammarNameFromFQGN(fullyQualifiedGrammarNameFromFQRN(fqrn));
  }

  static String simpleGrammarNameFromFQGN(String fqgn) {
    int lastDot = fqgn.lastIndexOf('.');
    return fqgn.substring(lastDot + 1);
  }

  static String packageNameFromFQRN(String fqrn) {
    return packageNameFromFQGN(fullyQualifiedGrammarNameFromFQRN(fqrn));
  }

  static String packageNameFromFQGN(String fqgn) {
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

  static String getFullyQualifiedRuleName(JsgfRuleImportName importName) {
    return stubify(importName, ImportStub::getFullyQualifiedRuleName, JsgfRuleImportName::getFQRN);
  }

  /**
   * Returns an array of length 2. The first element in the array is a reference to the
   * grammar name in the import, and the second is a reference to rule itself. If this is
   * a star import, both references will resolve to the grammar name.
   * @param importName The import name
   * @return 2 references, one to the grammar and another to the rule for the given import
   */
  @NotNull
  static PsiReference[] getReferences(JsgfRuleImportName importName) {
    return getReferencePair(importName).getReferenceArray();
  }

  static OtherFileReferencePair getReferencePair(JsgfRuleImportName importName) {
    String fqgn = importName.getFullyQualifiedGrammarName();
    String uqrn = importName.getUnqualifiedRuleName();
    TextRange grammarRange = new TextRange(0, fqgn.length());
    TextRange ruleRange = new TextRange(0, fqgn.length() + 1 + uqrn.length());
    return new OtherFileReferencePair(
        new OtherFileGrammarNameReference(importName, grammarRange),
        new ImportRuleNameReference(importName, ruleRange));
  }

  static @NotNull SearchScope getUseScope(JsgfRuleImportName importName) {
    // an import is by definition global
    return GlobalSearchScope.allScope(importName.getProject());
  }

  /*
   **********   RuleReferenceName methods  **********
   */

  @NotNull
  static LocalReferencePair getReferencePair(JsgfRuleReferenceName ruleReferenceName) {
    String fqrn = ruleReferenceName.getFQRN();
    // This is for the unique rule type that we use where you specify a field of the rule
    if (fqrn.contains("@")) {
      fqrn = fqrn.substring(0, fqrn.indexOf("@"));
    }
    RuleNameReference ruleReference = null;
    GrammarNameReference grammarReference = null;
    int lastDot = fqrn.lastIndexOf('.');
    if (lastDot != -1) {
      String grammarName = fullyQualifiedGrammarNameFromFQRN(fqrn);
      TextRange grammarRange = new TextRange(0, lastDot);
      grammarReference = new GrammarNameReference(ruleReferenceName, grammarRange, fqrn);
      TextRange ruleRange = new TextRange(0, fqrn.length());
      ruleReference = new RuleNameReference(ruleReferenceName, ruleRange, fqrn, grammarReference);
    } else {
      TextRange ruleRange = new TextRange(0, fqrn.length());
      ruleReference = new RuleNameReference(ruleReferenceName, ruleRange, fqrn, null);
    }
    return new LocalReferencePair(grammarReference, ruleReference);
  }

  /**
   * Returns an array of length 1 or 2. If the rule is unqualified, it will be length 1 and the
   * element will be a single RuleNameReference. If the rule is qualified or fully-qualified, the
   * first element will be a GrammarNameReference, and the second a RuleNameReference.
   *
   * @param ruleReferenceName the rule reference name element
   * @return either just a RuleNameReference, or a RulenameReference and a GrammarNameReference
   */
  @NotNull
  static PsiReference[] getReferences(JsgfRuleReferenceName ruleReferenceName) {
    return getReferencePair(ruleReferenceName).getReferenceArray();
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

  @NotNull
  static String getName(JsgfRuleDeclarationName ruleDeclarationName) {
    return stubify(ruleDeclarationName, RuleDeclarationStub::getName, JsgfRuleDeclarationName::getRuleName);
  }

  static PsiElement setName(JsgfRuleDeclarationName ruleDeclarationName,
      @NotNull String name) throws IncorrectOperationException {
    return ruleDeclarationName.setRuleName(name);
  }

  static boolean isPublicRule(JsgfRuleDeclarationName ruleDeclarationName) {
    return stubify(ruleDeclarationName, RuleDeclarationStub::isPublicRule,
        rdn -> ((JsgfRuleDefinition) rdn.getParent().getParent()).isPublicRule());
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

  static String getStringText(JsgfStringExp string) {
    return string.getText().substring(1, string.getTextLength())
        .replace("\\\\", "\\")
        .replace("\\\"", "\"");
  }

  /*
   **********   Alternatives methods  **********
   */

  static List<PsiElement> getOrSymbols(JsgfAlternativesExp alternatives) {
    return ((AlternativesMixin) alternatives).getOrSymbols_();
  }

  /*
   **********   Sequence methods  **********
   */

  /*
   **********   Group methods  **********
   */

  static boolean isOptionalGroup(JsgfGroupExp group) {
    return group instanceof JsgfOptionalGroupExp;
  }

  /*
   **********   RuleDefinition methods  **********
   */

  static String getRuleName(JsgfRuleDefinition ruleDefinition) {
    JsgfRuleDeclarationName ruleDeclarationName = ruleDefinition.getRuleDeclaration().getRuleDeclarationName();
    return ruleDeclarationName != null ? ruleDeclarationName.getRuleName() : null;
  }

  static boolean isPublicRule(JsgfRuleDefinition ruleDeclaration) {
    return ruleDeclaration.getFirstChild().getNode().getElementType() == JsgfBnfTypes.PUBLIC;
  }

  static <R, E extends StubBasedPsiElement<T>, T extends StubElement<E>> R
  stubify(E element, Function<T, R> stubFunction, Function<E, R> psiFunction) {
    T stub = element.getStub();
    if (stub != null) {
      return stubFunction.apply(stub);
    }
    return psiFunction.apply(element);
  }

}
