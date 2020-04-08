package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfFileType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.impl.GrammarNameElement;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.intellij.adaptor.psi.ScopeNode;
import org.antlr.intellij.adaptor.xpath.XPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PSI file class for Jsgf files
 * @author asherbernardi
 */
public class JsgfFile extends PsiFileBase {
  private HashSet<JsgfFile> importRelationships;
  private HashSet<ImportNameElement> imports;

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
    return "Jsgf File";
  }

  @Override
  public ScopeNode getContext() {
    return null;
  }

  @Nullable
  public GrammarNameElement getGrammarName() {
    Collection<? extends PsiElement> names = XPath
        .findAll(JsgfLanguage.INSTANCE, this, "/getGrammar/initGram/grammarName");
    if (names == null || names.isEmpty()) {
      return null;
    }
    PsiElement name = names.toArray(new PsiElement[1])[0];
    if (!(name instanceof GrammarNameElement))
      return null;
    return (GrammarNameElement) name;
  }

  public Set<ImportNameElement> getImports() {
    if (imports == null)
      makeRelationships();
    HashSet<ImportNameElement> importNames = new HashSet<>(PsiTreeUtil
        .findChildrenOfType(this, ImportNameElement.class));
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
    Collection<ImportNameElement> importNames = PsiTreeUtil
        .findChildrenOfType(this, ImportNameElement.class);
    importRelationships = new HashSet<>();
    imports = new HashSet<>();
    try {
      for (ImportNameElement importName : importNames) {
        List<JsgfFile> files = JsgfUtil.findFilesByPackage(getProject(), importName);
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