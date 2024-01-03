package com.asherbernardi.jsgfplugin.codeStyle;

import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsgfCodeStyleSettingsProvider extends CodeStyleSettingsProvider {

  @Override
  public @Nullable CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
    return new JsgfCodeStyleSettings(settings);
  }

  @Override
  @Nullable
  public String getConfigurableDisplayName() {
    return "JSGF";
  }

  @Override
  public @NotNull CodeStyleConfigurable createConfigurable(@NotNull CodeStyleSettings settings,
      @NotNull CodeStyleSettings modelSettings) {
    return new CodeStyleAbstractConfigurable(settings, modelSettings, this.getConfigurableDisplayName()) {
      @Override
      protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
        return new JsgfCodeStyleMainPanel(getCurrentSettings(), settings);
      }
    };
  }

  private static class JsgfCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {

    public JsgfCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
      super(JsgfLanguage.INSTANCE, currentSettings, settings);
    }

    @Override
    protected void initTabs(CodeStyleSettings settings) {
      addIndentOptionsTab(settings);
      addBlankLinesTab(settings);
      addSpacesTab(settings);
    }
  }
}
