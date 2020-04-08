package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.impl.GrammarNameElement;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.impl.FileTypeManagerImpl;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleNameElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper methods for Jsgf PSI elements
 * @author asherbernardi
 */
public class JsgfUtil {

  public static List<RuleNameElement> findRulesInFile(JsgfFile file, String key) {
    return findRulesInFile(file, key, false);
  }

  public static List<RuleNameElement> findRulesInFile(JsgfFile file, String name, boolean publicOnly) {
    List<RuleNameElement> result = retrieveRulesFromIndex(file, name, publicOnly);
    // if the first pass through the index fails, try re-indexing and searching again
    if (result.size() != 1) {
      JsgfFileIndex.reindex(file);
      result = retrieveRulesFromIndex(file, name, publicOnly);
    }
    return result;
  }

  private static List<RuleNameElement> retrieveRulesFromIndex(JsgfFile file, String name, boolean publicOnly) {
    List<RuleNameElement> result = new ArrayList<>();
    List<RuleNameElement> rules;
    if (publicOnly)
      rules = JsgfFileIndex.getPublicRulesInFile((JsgfFile) file);
    else
      rules = JsgfFileIndex.getRulesInFile((JsgfFile) file);
    for (RuleNameElement rule : rules) {
      if (name.equals(rule.getName())) {
        result.add(rule);
      }
    }
    return result;
  }

  private static List<RuleNameElement> retrieveRulesFromIndex(JsgfFile file, boolean publicOnly) {
    if (publicOnly)
      return JsgfFileIndex.getPublicRulesInFile((JsgfFile) file);
    else
      return JsgfFileIndex.getRulesInFile((JsgfFile) file);
  }

  public static List<RuleNameElement> findRulesInFile(JsgfFile file) {
    return findRulesInFile(file, false);
  }

  public static List<RuleNameElement> findRulesInFile(JsgfFile file, boolean publicOnly) {
    List<RuleNameElement> result = retrieveRulesFromIndex(file, publicOnly);
    return result;
  }

  public static boolean isFirstDeclarationInFile(JsgfFile file, RuleNameElement ruleName) {
    List<RuleNameElement> result = retrieveRulesFromIndex(file, ruleName.getName(), false);
    // if the first pass through the index fails, try re-indexing and searching again
    if (result.isEmpty() || result.get(0) != ruleName) {
      JsgfFileIndex.reindex(file);
      result = retrieveRulesFromIndex(file, ruleName.getName(), false);
    }
    return !result.isEmpty() && result.get(0) == ruleName;
  }

  /**
   * Finds rules in all project files that the match the import name.
   * If '*' is imported, it adds all public rules in the file
   *
   * @param project the project within which to search
   * @param importName the importNameElement which we are trying to resolve
   * @return a list of PsiFiles or RuleNameElements
   */
  public static List<PsiElement> findImportRules(Project project, ImportNameElement importName) {
    List<PsiElement> result = new ArrayList<>();
    for (JsgfFile file : findFilesByPackage(project, importName)) {
      if ("*".equals(importName.getUnqualifiedName()))
        result.addAll(findRulesInFile(file, true));
      else
        result.addAll(findRulesInFile(file, importName.getUnqualifiedName(), true));
    }
    return result;
  }

  /**
   * Finds rules in all project files that the match the package of importName
   * It is different from findImportRules() in that it finds all the rules in each file,
   * not just rules which match.
   *
   * @param project the project within which to search
   * @param importName the importNameElement which we are trying to resolve
   * @return a list of PsiFiles or RuleNameElements
   */
  public static List<PsiElement> findImportRulesByPackage(Project project, ImportNameElement importName) {
    List<PsiElement> result = new ArrayList<>();
    for (JsgfFile file : findFilesByPackage(project, importName)) {
      result.addAll(findRulesInFile(file, true));
    }
    return result;
  }

  /**
   * Finds a list of files that match the import name. Ideally, it only finds one file.
   * It finds matches by searching through all the files of the project and comparing them
   * to importName. If the grammar name of the file matches exactly the fully qualified version
   * of the import name, it's a match.
   * e.g. "#import <com.example.grammars.myGrammar.rule1>;" would reference to a rule in a grammar
   * with "grammar com.example.grammars.myGrammar;" in the header.<br>
   * Alternatively, if the path to the file ends with the path version of the import package, this
   * also could be a match.<br>
   * e.g. "#import &lt;path.to.myGrammar.rule1&gt;;" would reference to a rule in a grammar in a file
   * called myGrammar.gram in the "Users/me/projects/resources/5/path/to/" directory.
   * It only find files that are in the same module as the file from which this is being called,
   * because the "find usages" functionality of Intellij also only searches within a module.
   *
   * @param project the project within which to search
   * @param importName the importNameElement which we are trying to resolve
   * @return a list of JsgfFiles
   */
  public static List<JsgfFile> findFilesByPackage(Project project, ImportNameElement importName) {
    String fullyQualifiedGrammarName = importName.getFullyQualifiedGrammarName();
    List<FileNameMatcher> extensions = FileTypeManagerImpl.getInstance().getAssociations(JsgfFileType.INSTANCE);
    String importDirectoryPath = importName.getPackageName().replace('.', '/');
    String importFileName = fullyQualifiedGrammarName.substring(fullyQualifiedGrammarName.lastIndexOf('.') + 1);
    List<JsgfFile> result = new ArrayList<>();
    Collection<VirtualFile> virtualFiles =
        FileTypeIndex.getFiles(JsgfFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      File file = new File(virtualFile.getCanonicalPath());
      JsgfFile simpleFile = (JsgfFile) PsiManager.getInstance(project).findFile(virtualFile);
      // We only search in our module, because find usages only searches within the module
      if (ModuleUtil.findModuleForFile(simpleFile) !=
          ModuleUtil.findModuleForFile(importName.getContainingFile().getOriginalFile()))
        continue;
      GrammarNameElement grammarName = simpleFile.getGrammarName();
      // Match on the grammar name
      if (grammarName != null && grammarName.getName().equals(fullyQualifiedGrammarName)) {
        result.add(simpleFile);
      }
      // Match on the directory and file name
      if (!importDirectoryPath.isEmpty() && file.getParentFile().toPath().endsWith(importDirectoryPath)) {
        for (FileNameMatcher matcher : extensions) {
          String fileName = file.getName();
          String noExtension = fileName.substring(0, (fileName.lastIndexOf('.')>-1?fileName.lastIndexOf('.'):fileName.length()));
          if (noExtension.equals(importFileName) &&
              matcher.acceptsCharSequence(fileName)) {
            result.add(simpleFile);
          }
        }
      }
    }
    return result;
  }

  public static List<RuleNameElement> findAllRules(Project project) {
    List<RuleNameElement> result = new ArrayList<>();
    Collection<VirtualFile> virtualFiles =
        FileTypeIndex.getFiles(JsgfFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      JsgfFile simpleFile = (JsgfFile) PsiManager.getInstance(project).findFile(virtualFile);
      result.addAll(findRulesInFile(simpleFile));
    }
    return result;
  }

  public static List<RuleNameElement> findAllRules(Project project, String name) {
    List<RuleNameElement> result = new ArrayList<>();
    Collection<VirtualFile> virtualFiles =
        FileTypeIndex.getFiles(JsgfFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      JsgfFile simpleFile = (JsgfFile) PsiManager.getInstance(project).findFile(virtualFile);
      for (RuleNameElement rule : findRulesInFile(simpleFile)) {
        if (name.equals(rule.getName())) {
          result.add(rule);
        }
      }
    }
    return result;
  }
}