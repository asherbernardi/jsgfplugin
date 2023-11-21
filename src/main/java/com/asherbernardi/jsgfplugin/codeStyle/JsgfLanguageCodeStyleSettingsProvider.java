package com.asherbernardi.jsgfplugin.codeStyle;

import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.IndentOptions;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsgfLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

  private static final String SPACING_CODE_SAMPLE =
        "grammar test;\n"
      + "<rule_1> = [these are brackets] (these | are | parentheses);\n"
      + "public <rule_2> = this rule is (public|visible);\n"
      + "<rule_3> = (<rule_1>*\n"
      + "  star* plus+\n"
      + "  {here is a tag}\n"
      + ");"
      ;

  private static final String BLANK_LINES_CODE_SAMPLE =
        "#JSGF V1.0 ASCII en_US;\n"
      + "grammar test;\n"
      + "import <path.to.another.rule>;\n"
      + "\n"
      + "<rule_1> = (\n"
      + "  rule definition\n"
      + ");\n"
      + "public <rule_2> = this rule is (public|visible);\n"
      + "<rule_3> = refers to <rule_1>;\n"
      + "/**\n"
      + " * documentation comment\n"
      + " */\n"
      + "<rule_4> = (some have tags) {This is a tag};\n"
      ;

  private static final String INDENT_CODE_SAMPLE =
      "grammar test;\n"
    + "<rule_1> = (\n"
    + "  same line\n"
    + "  |\n"
    + "  single\n"
    + "  group\n"
    + ");\n"
    + "<rule_2> =\n"
    + "// comment\n"
    + "(\n"
    + "  new line\n"
    + "  | single\n"
    + "  group\n"
    + ");\n"
    + "<rule_3> = same line\n"
    + "  | single\n"
    + "  | alternatives\n"
    + ";\n"
    + "<rule_4> =\n"
    + "  /* comment */\n"
    + "  new line\n"
    + "  |\n"
    + "  single\n"
    + "  |\n"
    + "  alternatives\n"
    + ";\n"
      ;

  @Override
  public @Nullable String getCodeSample(@NotNull SettingsType settingsType) {
    switch (settingsType) {
      case SPACING_SETTINGS:
        return SPACING_CODE_SAMPLE;
      case BLANK_LINES_SETTINGS:
        return BLANK_LINES_CODE_SAMPLE;
      case INDENT_SETTINGS:
        return INDENT_CODE_SAMPLE;
      default:
        return "";
    }
  }

  @Override
  public @NotNull Language getLanguage() {
    return JsgfLanguage.INSTANCE;
  }

  @Override
  public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer,
      @NotNull SettingsType settingsType) {
    switch (settingsType) {
      case SPACING_SETTINGS:
        consumer.showStandardOptions(
            "SPACE_AROUND_ASSIGNMENT_OPERATORS",
            "SPACE_AROUND_UNARY_OPERATOR",
            "SPACE_AROUND_LOGICAL_OPERATORS",
            "SPACE_WITHIN_PARENTHESES",
            "SPACE_WITHIN_BRACES");
        consumer.renameStandardOption("SPACE_WITHIN_PARENTHESES", "Parentheses or brackets");
        consumer.renameStandardOption("SPACE_WITHIN_BRACES", "Tags");
        consumer.renameStandardOption("SPACE_AROUND_UNARY_OPERATOR", "Unary operators (*, +)");
        consumer.renameStandardOption("SPACE_AROUND_LOGICAL_OPERATORS", "Vertical bar '|'");
        break;
      case BLANK_LINES_SETTINGS:
        consumer.showStandardOptions(
            "BLANK_LINES_BEFORE_PACKAGE",
            "BLANK_LINES_AFTER_PACKAGE",
            "BLANK_LINES_AFTER_IMPORTS",
            "BLANK_LINES_AROUND_METHOD"
            );
        consumer.renameStandardOption("BLANK_LINES_BEFORE_PACKAGE", "Before grammar declaration");
        consumer.renameStandardOption("BLANK_LINES_AFTER_PACKAGE", "After grammar declaration");
        consumer.renameStandardOption("BLANK_LINES_AROUND_METHOD", "Around rule definitions");
        break;
    }
  }

  @Override
  protected void customizeDefaults(@NotNull CommonCodeStyleSettings commonSettings,
      @NotNull IndentOptions indentOptions) {
    commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
    commonSettings.SPACE_AROUND_UNARY_OPERATOR = false;
    commonSettings.SPACE_AROUND_LOGICAL_OPERATORS = true;
    commonSettings.SPACE_WITHIN_PARENTHESES = false;
    commonSettings.SPACE_WITHIN_BRACES = false;
    commonSettings.BLANK_LINES_BEFORE_PACKAGE = 0;
    commonSettings.BLANK_LINES_AFTER_PACKAGE = 1;
    commonSettings.BLANK_LINES_AFTER_IMPORTS = 1;
    commonSettings.BLANK_LINES_AROUND_METHOD = 1;
  }

  @Override
  public @Nullable IndentOptionsEditor getIndentOptionsEditor() {
    return new JsgfIndentOptionsEditor();
  }
}
