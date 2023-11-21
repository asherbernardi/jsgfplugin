package com.asherbernardi.jsgfplugin.codeStyle;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions;
import javax.swing.JCheckBox;
import org.jetbrains.annotations.NotNull;

public class JsgfIndentOptionsEditor extends IndentOptionsEditor {

  private JCheckBox indentSingleGroupRulesCB;
  private JCheckBox indentSingleAlternativesRulesCB;

  @Override
  protected void addComponents() {
    super.addComponents();
    indentSingleGroupRulesCB = new JCheckBox("Indent single group rules");
    add(indentSingleGroupRulesCB);

    indentSingleAlternativesRulesCB = new JCheckBox("Indent single alternatives rules");
    add(indentSingleAlternativesRulesCB);
  }

  @Override
  public void apply(CodeStyleSettings settings, IndentOptions options) {
    super.apply(settings, options);
    JsgfCodeStyleSettings mySettings = settings.getCustomSettings(JsgfCodeStyleSettings.class);
    mySettings.INDENT_SINGLE_GROUP_RULES = indentSingleGroupRulesCB.isSelected();
    mySettings.INDENT_SINGLE_ALTERNATIVES_RULES = indentSingleAlternativesRulesCB.isSelected();
  }

  @Override
  public void reset(@NotNull CodeStyleSettings settings, @NotNull IndentOptions options) {
    super.reset(settings, options);
    JsgfCodeStyleSettings mySettings = settings.getCustomSettings(JsgfCodeStyleSettings.class);
    indentSingleGroupRulesCB.setSelected(mySettings.INDENT_SINGLE_GROUP_RULES);
    indentSingleAlternativesRulesCB.setSelected(mySettings.INDENT_SINGLE_ALTERNATIVES_RULES);
  }
}
