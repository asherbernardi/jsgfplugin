package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfSyntaxHighlighter.JsgfHighlightType;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CodeInsightTest extends BasePlatformTestCase {

  public void testCodeCompletion() {
    myFixture.configureByFiles("CodeCompletionTest.jsgf", "CodeCompletionTestOther.jsgf");
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings,
        Arrays.asList("rule1", "rule2", "rule3", "rule4", "5Rule", "6leru", "anotherRule"));
  }

  public void testCodeCompletion_2() {
    myFixture.configureByFiles("CodeCompletionTest_2.jsgf", "CodeCompletionTestOther.jsgf");
    myFixture.complete(CompletionType.BASIC, 2);
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    assertSameElements("Wrong lookup elements", strings, Arrays.asList("anotherRule", "anotherLocal"));
  }

  public void testReferenceBroken() {
    myFixture.configureByFile("CodeCompletionTest.jsgf");
    PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset()).getPrevSibling();
    assertNull(element.getReference().resolve());
  }

  public void testReferences() {
    {
      // <other1> import should work
      PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest.jsgf", "ReferenceTestOther.jsgf");
      JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(),
          JsgfRuleDeclarationName.class);
      assertEquals("import of <other.ReferenceTestOther.other1> failed", "other1",
          resolve.getRuleName());
    }
    {
      // <other2> reference should work even though it is private
      PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_2.jsgf", "ReferenceTestOther.jsgf");
      JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
      assertEquals("reference to <other2> failed", "other2", resolve.getName());
      // but the highlighting should be an error
      assertTrue(myFixture.doHighlighting(HighlightSeverity.ERROR).stream()
          .anyMatch(h -> new TextRange(h.startOffset, h.endOffset).equals(reference.getElement().getNode().getTextRange())
              && h.forcedTextAttributesKey == JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey()
              && h.getDescription().equals("<other2> does not have public access in ReferenceTestOther.jsgf")));
    }
    {
      // <rule1> reference should work
      PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_3.jsgf",
          "ReferenceTestOther.jsgf");
      JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
      assertEquals("reference to <rule1> failed", "rule1", resolve.getName());
    }
    {
      // <rule3> reference should work
      PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_4.jsgf", "ReferenceTestOther.jsgf");
      JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
      assertEquals("reference to <rule3> failed", "rule3", resolve.getName());
    }
    {
      // <other1> reference should work
      PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion("ReferenceTest_5.jsgf", "ReferenceTestOther.jsgf");
      JsgfRuleDeclarationName resolve = assertInstanceOf(reference.resolve(), JsgfRuleDeclarationName.class);
      assertEquals("reference to <other1> failed", "other1", resolve.getName());
    }
    {
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
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }
}
