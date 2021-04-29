package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.RuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarStubIndex;
import com.asherbernardi.jsgfplugin.psi.stub.RuleStubIndex;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Helper methods for Jsgf PSI elements
 * @author asherbernardi
 */
public class JsgfUtil {

  @NotNull
  public static List<RuleDeclarationName> findRulesInFile(JsgfFile file, String ruleName) {
    // TODO change type of RuleName
    return findRulesInFile(file, ruleName, false);
  }

  @NotNull
  public static List<RuleDeclarationName> findRulesInFile(JsgfFile file, String ruleName, boolean publicOnly) {
    Collection<JsgfRuleDeclarationName> rules =
        RuleStubIndex.getRulesByName(ruleName, file.getProject(), GlobalSearchScope.fileScope(file));
    if (publicOnly) {
      return rules.stream().filter(JsgfRuleDeclarationName::isPublicRule)
          .collect(Collectors.toList());
    }
    return new ArrayList<>(rules);
  }

  @NotNull
  public static List<RuleDeclarationName> findRulesInFile(JsgfFile file) {
    return findRulesInFile(file, false);
  }

  @NotNull
  public static List<RuleDeclarationName> findRulesInFile(JsgfFile file, boolean publicOnly) {
    Collection<JsgfRuleDeclarationName> rules = RuleStubIndex.getRulesInFile(file);
    if (publicOnly) {
      return rules.stream().filter(JsgfRuleDeclarationName::isPublicRule)
          .collect(Collectors.toList());
    }
    return new ArrayList<>(rules);
  }

  public static boolean isFirstDeclarationInFile(JsgfFile file, RuleName ruleName) {
    List<RuleDeclarationName> rules = findRulesInFile(file, ruleName.getRuleName());
    for (RuleName rule : rules) {
      if (rule.getTextOffset() < ruleName.getTextOffset()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Finds rules in all project files that the match the import name.
   * If '*' is imported, it adds all public rules in the file
   *
   * @param importName the importNameElement which we are trying to resolve
   * @param publicOnly Whether or not the private rules should be added in the result
   * @return a list of PsiFiles or RuleNames
   */
  @NotNull
  public static List<RuleDeclarationName> findImportRules(JsgfRuleImportName importName,
      boolean publicOnly) {
    List<RuleDeclarationName> result = new ArrayList<>();
    for (JsgfFile file : findFilesByPackage(importName)) {
      if (importName.isStarImport())
        result.addAll(findRulesInFile(file, publicOnly));
      else
        result.addAll(findRulesInFile(file, importName.getUnqualifiedRuleName(), publicOnly));
    }
    return result;
  }

  /**
   * Finds rules in all project files that the match the package of importName
   * It is different from findImportRules() in that it finds all the rules in each file,
   * not just rules which match.
   *
   * @param importName the importNameElement which we are trying to resolve
   * @param publicOnly Whether or not the private rules should be added in the result
   * @return a list of PsiFiles or RuleNames
   */
  @NotNull
  public static List<RuleDeclarationName> findImportRulesByPackage(JsgfRuleImportName importName,
      boolean publicOnly) {
    List<RuleDeclarationName> result = new ArrayList<>();
    for (JsgfFile file : findFilesByPackage(importName)) {
      result.addAll(findRulesInFile(file, publicOnly));
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
   * @param importName the importNameElement which we are trying to resolve
   * @return a list of JsgfFiles
   */
  @NotNull
  public static List<JsgfFile> findFilesByPackage(JsgfRuleImportName importName) {
    List<JsgfFile> result = new ArrayList<>();
    Project project = importName.getProject();
    // Matching by grammar name
    String fullyQualifiedGrammarName = importName.getFullyQualifiedGrammarName();
    Module module = ModuleUtil.findModuleForPsiElement(importName);
    if (module == null) return result;
    Collection<JsgfGrammarName> allMatchingGrammars = GrammarStubIndex
        .getGrammarsByName(
            fullyQualifiedGrammarName, project, GlobalSearchScope.moduleScope(module));
    for (JsgfGrammarName grammarName : allMatchingGrammars) {
      result.add((JsgfFile) grammarName.getContainingFile());
    }
    // Matching by file path
    String importDirectoryPath = importName.getPackageName().replace('.', '/');
    String importFileName = fullyQualifiedGrammarName.substring(fullyQualifiedGrammarName.lastIndexOf('.') + 1);
    Collection<VirtualFile> virtualFiles =
        FileTypeIndex.getFiles(JsgfFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      String path = virtualFile.getCanonicalPath();
      if (path == null) continue;
      File file = new File(path);
      if (!importDirectoryPath.isEmpty() && file.getParentFile().toPath().endsWith(importDirectoryPath)) {
        String fileName = file.getName();
        int lastDot = fileName.lastIndexOf('.');
        String noExtension = fileName.substring(0, lastDot > -1 ? lastDot : fileName.length());
        if (noExtension.equals(importFileName)) {
          JsgfFile simpleFile = (JsgfFile) PsiManager.getInstance(project).findFile(virtualFile);
          result.add(simpleFile);
        }
      }
    }
    return result;
  }

  @NotNull
  public static List<RuleName> findAllRules(Project project) {
    List<RuleName> allRules = new ArrayList<>();
    for (String ruleName : RuleStubIndex.INSTANCE.getAllKeys(project)) {
      allRules.addAll(
          RuleStubIndex.getRulesByName(ruleName, project, GlobalSearchScope.allScope(project)));
    }
    return allRules;
  }

  @NotNull
  public static List<RuleName> findAllRules(Project project, String name) {
    return new ArrayList<>(
        RuleStubIndex.getRulesByName(name, project, GlobalSearchScope.allScope(project)));
  }

  @NotNull
  public static List<ASTNode> findChildrenByType(ASTNode node, IElementType... types) {
    return findChildrenByType(node, TokenSet.create(types));
  }

  @NotNull
  public static List<ASTNode> findChildrenByType(ASTNode node, TokenSet types) {
    List<ASTNode> children = new ArrayList<>();
    ASTNode currentChild = node.findChildByType(types);
    while (currentChild != null) {
      children.add(currentChild);
      ASTNode nextChild = currentChild.getTreeNext();
      if (nextChild != null) {
        currentChild = node.findChildByType(types, nextChild);
      } else {
        currentChild = null;
      }
    }
    return children;
  }
}