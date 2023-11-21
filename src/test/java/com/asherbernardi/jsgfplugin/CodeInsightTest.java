package com.asherbernardi.jsgfplugin;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.asherbernardi.jsgfplugin.JsgfSyntaxHighlighter.JsgfHighlightType;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceName;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CodeInsightTest extends BasePlatformTestCase {

  public void testCodeCompletion_localRule() {
    myFixture.configureByFiles("CodeCompletionTest_localRule.jsgf", "CodeCompletionTestOther.jsgf");
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings,
        Arrays.asList("rule1", "rule2", "rule3", "rule4", "5Rule", "6leru", "anotherRule"));
  }

  public void testCodeCompletion_importedRuleReference() {
    myFixture.configureByFiles("CodeCompletionTest_importedRuleReference.jsgf", "CodeCompletionTestOther.jsgf");
    myFixture.complete(CompletionType.BASIC, 2);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("anotherRule", "anotherLocal"));
  }

  public void testCodeCompletion_otherGrammarName() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarName.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf");
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNull(strings); // auto-complete
    myFixture.complete(CompletionType.BASIC, 1);
    strings = myFixture.getLookupElementStrings();
    assertNotNull(strings); // then complete the rules
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("*", "anotherRule", "notThis"));
  }

  public void testCodeCompletion_otherGrammarName_Inner() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarName_Inner.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf", "package/InnerPackageGrammar_2.jsgf");
    myFixture.complete(CompletionType.BASIC, 2);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("package.InnerPackageGrammar", "com.package.Inner", "package.InnerPackageGrammar_2", "com.package.Inner_2"));
  }

  public void testCodeCompletion_otherGrammarName_Inner_2() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarName_Inner_2.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf", "package/InnerPackageGrammar_2.jsgf");
    LookupElement[] els = myFixture.complete(CompletionType.BASIC, 1);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("InnerPackageGrammar", "InnerPackageGrammar_2"));
  }

  public void testCodeCompletion_otherGrammarName_Inner_3() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarName_Inner_3.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf", "package/InnerPackageGrammar_2.jsgf");
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("package.Inner", "package.Inner_2"));
  }

  public void testCodeCompletion_otherGrammarAllNames() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarAllNames.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf");
    myFixture.complete(CompletionType.BASIC, 2);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("other", "CodeCompletionTestOther", "package.InnerPackageGrammar", "com.package.Inner", "package"));
  }

  public void testCodeCompletion_otherGrammarRuleImport() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarRuleImport.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf");
    myFixture.complete(CompletionType.BASIC, 2);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("*", "anotherRule", "notThis"));
  }

  public void testCodeCompletion_otherGrammarRuleImport_Inner() {
    myFixture.configureByFiles("CodeCompletionTest_otherGrammarRuleImport_Inner.jsgf", "CodeCompletionTestOther.jsgf", "package/InnerPackageGrammar.jsgf");
    myFixture.complete(CompletionType.BASIC, 2);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("*", "first", "second", "third", "fourth"));
  }

  public void testReferenceBroken() {
    myFixture.configureByFile("CodeCompletionTest_localRule.jsgf");
    PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset()).getPrevSibling();
    assertInstanceOf(element, JsgfRuleReferenceName.class);
    assertNull(((JsgfRuleReferenceName) element).getReferencePair().getRuleReference().resolve());
  }

  public void testReferences_importPublic() {
    // <other1> import should work
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest.jsgf", "ReferenceTestOther.jsgf");
    JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(),
        JsgfRuleDeclarationName.class);
    assertEquals("import of <other.ReferenceTestOther.other1> failed", "other1",
        resolve.getRuleName());
  }

  public void testReferences_importPrivate() {
    // <other2> reference should work even though it is private
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_2.jsgf", "ReferenceTestOther.jsgf");
    JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
    assertEquals("reference to <other2> failed", "other2", resolve.getName());
    // but the highlighting should be an error
    assertTrue(myFixture.doHighlighting(HighlightSeverity.ERROR).stream()
        .anyMatch(h -> new TextRange(h.startOffset, h.endOffset).equals(reference.getAbsoluteRange())
            && h.forcedTextAttributesKey == JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey()
            && h.getDescription().equals("<other2> does not have public access in ReferenceTestOther.jsgf")));
  }

  public void testReferences_local() {
    // <rule1> reference should work
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_3.jsgf",
        "ReferenceTestOther.jsgf");
    JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
    assertEquals("reference to <rule1> failed", "rule1", resolve.getName());
  }

  public void testReferences_recursion() {
    // <rule3> reference should work
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_4.jsgf", "ReferenceTestOther.jsgf");
    JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
    assertEquals("reference to <rule3> failed", "rule3", resolve.getName());
  }

  public void testReferences_multiImportPublic() {
    // <other1> reference should work
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_5.jsgf", "ReferenceTestOther.jsgf");
    JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
    assertEquals("reference to <other1> failed", "other1", resolve.getName());
  }

  public void testReferences_multiImportPrivate() {
    // <other2> reference should work even though it is private
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_6.jsgf", "ReferenceTestOther.jsgf");
    JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
    assertEquals("reference to <other2> failed", "other2", resolve.getName());
    // but the highlighting should be an error
    assertTrue(myFixture.doHighlighting(HighlightSeverity.ERROR).stream()
        .anyMatch(h -> new TextRange(h.startOffset, h.endOffset).equals(reference.getElement().getNode().getTextRange())
            && h.forcedTextAttributesKey == JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey()
            && h.getDescription().equals("<other2> does not have public access in ReferenceTestOther.jsgf")));
  }

  public void testReferences_importGrammar() {
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_7.jsgf", "ReferenceTestOther.jsgf");
    JsgfGrammarName resolve = assertInstanceOf(reference.resolve(), JsgfGrammarName.class);
    assertEquals("reference to other grammar ReferenceTestOther failed", "other.ReferenceTestOther", resolve.getName());
  }

  public void testReferences_importGrammarByFilePath() {
    PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_8.jsgf", "ReferenceTestOther.jsgf", "package/InnerPackageGrammar.jsgf");
    ResolveResult[] resolves = ((PsiPolyVariantReference) reference).multiResolve(false);
    assertEquals("Expected only one resolution to other grammar InnerPackageGrammar", 1, resolves.length);
    JsgfFile resolve = assertInstanceOf(resolves[0].getElement(), JsgfFile.class);
    assertEquals("reference to other grammar InnerPackageGrammar failed", "InnerPackageGrammar.jsgf", resolve.getName());
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }
}
