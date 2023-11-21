package com.asherbernardi.jsgfplugin;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;

public class RenameTest extends BasePlatformTestCase {

  public void testRenameRuleName_1() {
    myFixture.configureByFiles("RenameRuleTestMain_1.jsgf", "RenameRuleTestOther.jsgf", "package/RenameRuleTestPackage.jsgf");
    myFixture.renameElementAtCaret("enormous");
    myFixture.checkResultByFile("RenameRuleTestMain_1.jsgf", "renameAfter/RenameRuleTestMain_1_after.jsgf", false);
    myFixture.checkResultByFile("RenameRuleTestOther.jsgf", "renameAfter/RenameRuleTestOther_1_after.jsgf", false);
    myFixture.checkResultByFile("package/RenameRuleTestPackage.jsgf", "renameAfter/RenameRuleTestPackage_1_after.jsgf", false);
  }

  public void testRenameRuleName_2() {
    myFixture.configureByFiles("RenameRuleTestMain_2.jsgf", "RenameRuleTestOther.jsgf", "package/RenameRuleTestPackage.jsgf");
    myFixture.renameElementAtCaret("gastly");
    myFixture.checkResultByFile("RenameRuleTestMain_2.jsgf", "renameAfter/RenameRuleTestMain_2_after.jsgf", false);
    myFixture.checkResultByFile("RenameRuleTestOther.jsgf", "renameAfter/RenameRuleTestOther_2_after.jsgf", false);
    myFixture.checkResultByFile("package/RenameRuleTestPackage.jsgf", "renameAfter/RenameRuleTestPackage_2_after.jsgf", false);
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return "src/test/testData";
  }
}
