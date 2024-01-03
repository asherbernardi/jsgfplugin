package com.asherbernardi.jsgfplugin.psi.stub;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.intellij.notebook.editor.BackedVirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processors;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class ImportStubIndex extends StringStubIndexExtension<JsgfRuleImportName> {

  public static final ImportStubIndex INSTANCE = new ImportStubIndex();

  private ImportStubIndex() { }

  @Override
  public @NotNull StubIndexKey<String, JsgfRuleImportName> getKey() {
    return JsgfStubElementTypes.IMPORT_INDEX_KEY;
  }

  public static Collection<String> getImportNamesInFile(PsiFile file) {
    if (!(file instanceof JsgfFile)) return new HashSet<>();
    Set<String> allKeys = new HashSet<>();
    StubIndex.getInstance().processAllKeys(JsgfStubElementTypes.IMPORT_INDEX_KEY,
        Processors.cancelableCollectProcessor(allKeys), GlobalSearchScope.fileScope(file.getOriginalFile()), null);
    return allKeys;
  }

  public static Collection<JsgfRuleImportName> getImportsInFile(PsiFile file) {
    Set<JsgfRuleImportName> imports = new HashSet<>();
    for (String key : getImportNamesInFile(file)) {
      imports.addAll(INSTANCE.get(key, file.getProject(), GlobalSearchScope.fileScope(file.getOriginalFile())));
    }
    return imports;
  }

  public static Collection<JsgfRuleImportName> getAllImportsInProject(Project project) {
    Set<JsgfRuleImportName> imports = new HashSet<>();
    for (String key : INSTANCE.getAllKeys(project)) {
      imports.addAll(INSTANCE.get(key, project, GlobalSearchScope.projectScope(project)));
    }
    return imports;
  }

  public static Collection<JsgfRuleImportName> getAllImportsInProjectExceptInFiles(Project project, Set<JsgfFile> excluding) {
    Set<JsgfRuleImportName> imports = new HashSet<>();
    for (String key : INSTANCE.getAllKeys(project)) {
      imports.addAll(INSTANCE.get(key, project, GlobalSearchScope.projectScope(project)
          .intersectWith(GlobalSearchScope.notScope(
              GlobalSearchScope.filesScope(project, excluding.stream().map(PsiFile::getVirtualFile)
                  .map(vf -> vf != null ? BackedVirtualFile.getOriginFileIfBacked(vf) : null).collect(
                      Collectors.toSet()))))));
    }
    return imports;
  }
}
