package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import java.util.Collection;
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
}
