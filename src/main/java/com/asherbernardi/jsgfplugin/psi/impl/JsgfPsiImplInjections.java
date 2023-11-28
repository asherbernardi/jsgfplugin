package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.reference.GrammarNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.LocalReferencePair;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileGrammarNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileReferencePair;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileRuleNameReference;
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

  /*
   **********   ImportStatement methods  **********
   */

  @Nullable
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
    return unqualifiedRuleNameFromFQRN(importName.getFQRN());
  }

  static String getFullyQualifiedGrammarName(JsgfRuleImportName importName) {
    return fullyQualifiedGrammarNameFromFQRN(importName.getFQRN());
  }

  static String getSimpleGrammarName(JsgfRuleImportName importName) {
    return simpleGrammarNameFromFQRN(importName.getFQRN());
  }

  static String getPackageName(JsgfRuleImportName importName) {
    return packageNameFromFQRN(importName.getFQRN());
  }

  static OtherFileReferencePair getReferencePair(JsgfRuleImportName importName) {
    String fqrn = importName.getFQRN();
    int lastDot = fqrn.lastIndexOf('.');
    OtherFileGrammarNameReference grammarReference;
    if (lastDot != -1) {
      grammarReference = new OtherFileGrammarNameReference(importName, new TextRange(0, lastDot));
    } else {
      grammarReference = null;
    }
    OtherFileRuleNameReference ruleReference = new OtherFileRuleNameReference(importName, new TextRange(lastDot + 1, fqrn.length()), grammarReference);
    return new OtherFileReferencePair(grammarReference, ruleReference);
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
      TextRange grammarRange = new TextRange(0, lastDot);
      grammarReference = new GrammarNameReference(ruleReferenceName, grammarRange);
    }
    TextRange ruleRange = new TextRange(lastDot + 1, fqrn.length());
    ruleReference = new RuleNameReference(ruleReferenceName, ruleRange);
    return new LocalReferencePair(grammarReference, ruleReference);
  }

  @Nullable
  static String labelFromFQRN(String fqrn) {
    // This is for the unique rule type that we use where you specify a field of the rule
    int atIndex = fqrn.indexOf("@");
    if (atIndex != -1) {
      return fqrn.substring(atIndex + 1);
    }
    return null;
  }

  @Nullable
  static String getLabel(JsgfRuleReferenceName ruleReferenceName) {
    return labelFromFQRN(ruleReferenceName.getFQRN());
  }


  /*
   **********   RuleDeclarationName methods  **********
   */

  static boolean isPublicRule(JsgfRuleDeclarationName ruleDeclarationName) {
    return stubify(ruleDeclarationName, RuleDeclarationStub::isPublicRule,
        rdn -> ((JsgfRuleDefinition) rdn.getParent().getParent()).isPublicRule());
  }

  /*
   **********   String methods  **********
   */

  static String getStringText(JsgfStringExp string) {
    PsiElement stringText = string.getFirstChild().getNextSibling();
    if (stringText == null || stringText.getNode().getElementType() != JsgfBnfTypes.STRING_TEXT) return "";
    return stringText.getText()
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
   **********   Unary methods  **********
   */

  static boolean isStar(JsgfUnaryOperationExp unary) {
    return unary.getNode().getLastChildNode().getElementType() == JsgfBnfTypes.STAR;
  }

  static boolean isPlus(JsgfUnaryOperationExp unary) {
    return unary.getNode().getLastChildNode().getElementType() == JsgfBnfTypes.PLUS;
  }

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
