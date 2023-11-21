package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processors;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class RuleStubIndex extends StringStubIndexExtension<JsgfRuleDeclarationName> {

  public static RuleStubIndex INSTANCE = new RuleStubIndex();

  private RuleStubIndex() { }

  @Override
  public @NotNull StubIndexKey<String, JsgfRuleDeclarationName> getKey() {
    return JsgfStubElementTypes.RULE_INDEX_KEY;
  }

  public static Collection<JsgfRuleDeclarationName> getRulesByQualifiedName(@NotNull String ruleText,
      @NotNull final Project project, @NotNull final GlobalSearchScope scope) {
    return INSTANCE.get(ruleText, project, scope);
  }

  @NotNull
  public static Collection<String> getRuleNamesInFile(PsiFile file) {
    if (!(file instanceof JsgfFile)) return new HashSet<>();
    Set<String> allKeys = new HashSet<>();
    StubIndex.getInstance().processAllKeys(JsgfStubElementTypes.RULE_INDEX_KEY,
        Processors.cancelableCollectProcessor(allKeys), GlobalSearchScope.fileScope(file.getOriginalFile()), null);
    return allKeys;
  }

  @NotNull
  public static Collection<JsgfRuleDeclarationName> getRulesInFile(PsiFile file) {
    Set<JsgfRuleDeclarationName> imports = new HashSet<>();
    for (String key : getRuleNamesInFile(file)) {
      imports.addAll(INSTANCE.get(key, file.getProject(), GlobalSearchScope.fileScope(file.getOriginalFile())));
    }
    return imports;
  }
}
