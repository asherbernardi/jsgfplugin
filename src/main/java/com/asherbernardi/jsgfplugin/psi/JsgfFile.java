package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfFileType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PSI file class for JSGF files
 * @author asherbernardi
 */
public class JsgfFile extends PsiFileBase {

  public JsgfFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, JsgfLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return JsgfFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return JsgfFileType.INSTANCE.getName();
  }

  @Nullable
  public JsgfGrammarName getGrammarName() {
    JsgfGrammarDeclaration grammarDeclaration = getGrammarDeclaration();
    if (grammarDeclaration != null) {
      return grammarDeclaration.getGrammarName();
    }
    return null;
  }

  @Nullable
  public JsgfGrammarDeclaration getGrammarDeclaration() {
    return PsiTreeUtil.getChildOfType(this, JsgfGrammarDeclaration.class);
  }

  @Nullable
  public JsgfHeader getHeader() {
    return PsiTreeUtil.getChildOfType(this, JsgfHeader.class);
  }

  @NotNull
  public List<JsgfRuleImportName> getImportNames() {
    return getImportStatements().stream().map(JsgfImportStatement::getRuleImportName).collect(
        Collectors.toList());
  }

  @NotNull
  public List<JsgfImportStatement> getImportStatements() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfImportStatement.class);
  }

  @NotNull
  public List<JsgfRuleDefinition> getRuleDefinitions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfRuleDefinition.class);
  }
}