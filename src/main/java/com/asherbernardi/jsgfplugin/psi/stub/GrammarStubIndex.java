package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.module.Module;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class GrammarStubIndex extends StringStubIndexExtension<JsgfGrammarName> {

  public static final GrammarStubIndex INSTANCE = new GrammarStubIndex();

  private GrammarStubIndex() { }

  @Override
  public @NotNull StubIndexKey<String, JsgfGrammarName> getKey() {
    return JsgfStubElementTypes.GRAMMAR_INDEX_KEY;
  }

  public static Collection<JsgfGrammarName> getGrammarsByName(@NotNull String fqgn,
      @NotNull final Project project, @NotNull final GlobalSearchScope scope) {
    return INSTANCE.get(fqgn, project, scope);
  }

  public static Collection<JsgfGrammarName> getAllGrammarsInProject(@NotNull final Project project) {
    Set<JsgfGrammarName> imports = new HashSet<>();
    for (String key : INSTANCE.getAllKeys(project)) {
      imports.addAll(INSTANCE.get(key, project, GlobalSearchScope.projectScope(project)));
    }
    return imports;
  }

  public static Collection<JsgfGrammarName> getAllGrammarsInModule(@NotNull final Project project,
      @NotNull final Module module) {
    Set<JsgfGrammarName> imports = new HashSet<>();
    for (String key : INSTANCE.getAllKeys(project)) {
      imports.addAll(INSTANCE.get(key, project, GlobalSearchScope.moduleScope(module)));
    }
    return imports;
  }
}
