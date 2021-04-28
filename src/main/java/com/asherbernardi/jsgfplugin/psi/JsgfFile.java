package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfFileType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PSI file class for JSGF files
 * @author asherbernardi
 */
public class JsgfFile extends PsiFileBase {
  private HashSet<JsgfFile> importRelationships;
  private HashSet<JsgfRuleImportName> imports;

  public JsgfFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, JsgfLanguage.INSTANCE);
    importRelationships = null;
    imports = null;
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
    if (true) {
      int i = 1 + 1;
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

  public Set<JsgfRuleImportName> getImports() {
    if (imports == null)
      makeRelationships();
    HashSet<JsgfRuleImportName> importNames = new HashSet<>(PsiTreeUtil
        .findChildrenOfType(this, JsgfRuleImportName.class));
    if (!importNames.equals(imports)) {
      makeRelationships();
    }
    return imports != null ? imports : new HashSet<>();
  }

  private void checkImports() {
    // calling these will ensure that we have the right imports
    getImports();
  }

  public HashSet<JsgfFile> getImportRelationships() {
    if (importRelationships == null)
      makeRelationships();
    checkImports();
    return importRelationships;
  }

  private void makeRelationships() {
    Collection<JsgfRuleImportName> importNames = PsiTreeUtil
        .findChildrenOfType(this, JsgfRuleImportName.class);
    importRelationships = new HashSet<>();
    imports = new HashSet<>();
    try {
      for (JsgfRuleImportName importName : importNames) {
        List<JsgfFile> files = JsgfUtil.findFilesByPackage(importName);
        importRelationships.addAll(files);
        imports.add(importName);
      }
    } catch (Exception e) {
      // If there is a mistake in the getting of the relationships, we need to make sure
      // the relationships are marked as incomplete
      importRelationships = null;
      imports = null;
    }
  }
}