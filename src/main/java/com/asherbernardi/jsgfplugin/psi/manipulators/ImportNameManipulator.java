package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.asherbernardi.jsgfplugin.JsgfFileType;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.RuleNameSplit;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImportNameManipulator extends AbstractElementManipulator<JsgfRuleImportName> {

  @Nullable
  @Override
  public JsgfRuleImportName handleContentChange(@NotNull JsgfRuleImportName element,
      @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    String fqrn = element.getFQRN();
    RuleNameSplit split = RuleNameSplit.fromFQRN(fqrn);
    String newQualifiedName;
    if (split.hasFQGN() && range.equals(split.getFQGNRange())) {
      List<FileNameMatcher> fileMatchers = FileTypeManager.getInstance().getAssociations(
          JsgfFileType.INSTANCE);
      String simpleGrammarName = newContent;
      if (fileMatchers.stream().anyMatch(fm -> fm.acceptsCharSequence(newContent))) {
        simpleGrammarName = JsgfUtil.stripExtension(simpleGrammarName);
      }
      // New content is a simple grammar name
      newQualifiedName = split.replaceSimpleGrammarName(simpleGrammarName);
    } else if (range.equals(split.getUQRNRange())) {
      // New content is an unqualified rule name
      if (element.isStarImport()) {
        throw new IncorrectOperationException("Cannot rename a '*' import");
      }
      newQualifiedName = split.replaceUQRN(newContent);
    } else {
      throw new IncorrectOperationException("Wrong range: " + range + " to replace import name: " + element.getText());
    }
    return (JsgfRuleImportName) element.setFQRN(newQualifiedName);
  }
}
