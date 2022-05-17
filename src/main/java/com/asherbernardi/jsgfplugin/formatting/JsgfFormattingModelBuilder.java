package com.asherbernardi.jsgfplugin.formatting;

import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.*;
import static com.asherbernardi.jsgfplugin.psi.JsgfTypes.*;

import com.asherbernardi.jsgfplugin.psi.JsgfExpansion;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.JsgfAlternativesExp;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.asherbernardi.jsgfplugin.psi.JsgfGroupExp;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDefinition;
import com.asherbernardi.jsgfplugin.psi.JsgfSequenceExp;
import com.asherbernardi.jsgfplugin.psi.JsgfTag;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;

public class JsgfFormattingModelBuilder implements FormattingModelBuilder {

  private SpacingBuilder createSpacingBuilder(CodeStyleSettings settings) {
    return new SpacingBuilder(settings, JsgfLanguage.INSTANCE)
        .around(TokenSet.create(EQUALS))
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_AROUND_ASSIGNMENT_OPERATORS)
        .around(TokenSet.create(OR))
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_AROUND_LOGICAL_OPERATORS)
        .between(
            TokenSet.create(RULE_REFERENCE_EXP, TERMINAL_EXP, STRING_EXP, PARENTHESES_GROUP_EXP, OPTIONAL_GROUP_EXP),
            TokenSet.create(STAR, PLUS))
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_AROUND_UNARY_OPERATOR)
        .between(
            TokenSet.create(RULE_REFERENCE_EXP, TERMINAL_EXP, STRING_EXP, PARENTHESES_GROUP_EXP, OPTIONAL_GROUP_EXP),
            TokenSet.create(RULE_REFERENCE_EXP, TERMINAL_EXP, STRING_EXP, PARENTHESES_GROUP_EXP, OPTIONAL_GROUP_EXP))
        .spaces(1)
        .between(
            TokenSet.create(LPAREN, LBRACK),
            FormattingUtil.SEQUENCE_CHILDREN)
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_WITHIN_PARENTHESES)
        .between(
            FormattingUtil.SEQUENCE_CHILDREN,
            TokenSet.create(RPAREN, RBRACK))
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_WITHIN_PARENTHESES)
        .between(
            TokenSet.create(LBRACE),
            TokenSet.create(TAG_TOKEN))
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_WITHIN_BRACES)
        .between(
            TokenSet.create(TAG_TOKEN),
            TokenSet.create(RBRACE))
        .spaceIf(settings.getCommonSettings(JsgfLanguage.INSTANCE).SPACE_WITHIN_BRACES)
        .between(
            HEADER,
            TokenSet.create(
                GRAMMAR_DECLARATION,
                DOC_COMMENT, LINE_COMMENT, BLOCK_COMMENT
            ))
        .blankLines(settings.getCommonSettings(JsgfLanguage.INSTANCE).BLANK_LINES_BEFORE_PACKAGE)
        .between(
            GRAMMAR_DECLARATION,
            TokenSet.create(
                IMPORT_STATEMENT,
                RULE_DEFINITION,
                DOC_COMMENT, LINE_COMMENT, BLOCK_COMMENT
            ))
        .blankLines(settings.getCommonSettings(JsgfLanguage.INSTANCE).BLANK_LINES_AFTER_PACKAGE)
        .between(
            IMPORT_STATEMENT,
            TokenSet.create(
                RULE_DEFINITION,
                DOC_COMMENT, LINE_COMMENT, BLOCK_COMMENT
            ))
        .blankLines(settings.getCommonSettings(JsgfLanguage.INSTANCE).BLANK_LINES_AFTER_IMPORTS)
        .between(
            RULE_DEFINITION,
            TokenSet.create(
                RULE_DEFINITION,
                DOC_COMMENT, LINE_COMMENT, BLOCK_COMMENT
            ))
        .blankLines(settings.getCommonSettings(JsgfLanguage.INSTANCE).BLANK_LINES_AROUND_METHOD);
  }

  @Override
  public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
    Block block;
    PsiElement element = formattingContext.getPsiElement();
    CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
    // Whole file
    if (element instanceof JsgfFile) {
      block = new WholeFileBlock((JsgfFile) element, createSpacingBuilder(settings), settings);
    }
    // Rule declaration
    else if (element instanceof JsgfRuleDefinition) {
      block = new RuleDefinitionBlock((JsgfRuleDefinition) element, createSpacingBuilder(settings), settings);
    }
    // Expansions
    else if (element instanceof JsgfExpansion) {
      block = new ExpansionBlock((JsgfExpansion) element, createSpacingBuilder(settings), settings,
          Indent.getNoneIndent());
    }
    // Tag
    else if (element instanceof JsgfTag) {
      block = new TagBlock((JsgfTag) element, createSpacingBuilder(settings), settings);
    }
    // Other
    else {
      block = FormattingUtil.createSimpleBlock(element, Indent.getNoneIndent(), createSpacingBuilder(settings));
    }
    return FormattingModelProvider.
        createFormattingModelForPsiFile(formattingContext.getContainingFile(),
            block, settings);
  }
}
