package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfFileIndex.ReindexingForcer;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleReferenceElement;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CodeInsightTest extends BasePlatformTestCase {

  public void testCodeCompletion() {
    myFixture.configureByFile("CodeCompletionTest.jsgf");
    (new ReindexingForcer((JsgfFile) myFixture.getFile())).forceReindexing();
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> strings = myFixture.getLookupElementStrings();
    assertTrue(strings.containsAll(Arrays.asList("rule1", "rule2", "rule3", "rule4", "5Rule", "6leru", "anotherRule")));
    assertEquals(7, strings.size());
  }

  public void testReferenceBroken() {
    myFixture.configureByFile("CodeCompletionTest.jsgf");
    (new ReindexingForcer((JsgfFile) myFixture.getFile())).forceReindexing();
    PsiElement element = myFixture.getFile().findElementAt(myFixture.getCaretOffset()).getPrevSibling();
    assertNull(element.getReference().resolve());
  }

  public void testReferences() {
    myFixture.configureByFiles("ReferenceTest.jsgf", "ReferenceTestOther.jsgf");
    (new ReindexingForcer((JsgfFile) myFixture.getFile())).forceReindexing();
    PsiElement element, resolve;
    PsiElement[] importElements = PsiTreeUtil.findChildrenOfType(myFixture.getFile(), ImportNameElement.class).toArray(new PsiElement[0]);
    // <other1> import should work
    element = importElements[0];
    resolve = element.getReference().resolve();
    assertTrue("import of <other.ReferenceTestOther.other1> failed", resolve instanceof RuleDeclarationNameElement);
    assertEquals("import of <other.ReferenceTestOther.other1> imported wrong thing", "other1", ((RuleDeclarationNameElement) resolve).getName());
    // <other2> import should be broken
    element = importElements[1];
    resolve = element.getReference().resolve();
    assertNull("import of <other.ReferenceTestOther.other2> should be broken", resolve);

    PsiElement[] ruleRefElements = PsiTreeUtil.findChildrenOfType(myFixture.getFile(), RuleReferenceElement.class).toArray(new PsiElement[0]);
    // <rule1> reference should work
    element = ruleRefElements[0];
    resolve = element.getReference().resolve();
    assertTrue("reference to <rule1> failed", resolve instanceof RuleDeclarationNameElement);
    assertEquals("reference to <rule1> failed", "rule1", ((RuleDeclarationNameElement) resolve).getName());
    // <rule3> reference should work
    element = ruleRefElements[1];
    resolve = element.getReference().resolve();
    assertTrue("reference to <rule3> failed", resolve instanceof RuleDeclarationNameElement);
    assertEquals("reference to <rule3> failed", "rule3", ((RuleDeclarationNameElement) resolve).getName());
    // <other1> reference should work
    element = ruleRefElements[2];
    resolve = element.getReference().resolve();
    assertTrue("reference to imported rule <other1> failed", resolve instanceof RuleDeclarationNameElement);
    assertEquals("reference to imported rule <other1> failed", "other1", ((RuleDeclarationNameElement) resolve).getName());
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }
}
