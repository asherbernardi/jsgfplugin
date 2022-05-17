package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarStubIndex;
import com.asherbernardi.jsgfplugin.psi.stub.RuleStubIndex;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfPsiImplInjections;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarStubIndex;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStubIndex;
import com.asherbernardi.jsgfplugin.psi.stub.RuleStubIndex;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Helper methods for Jsgf PSI elements
 * @author asherbernardi
 */
public class JsgfUtil {

  @NotNull
  public static List<JsgfRuleDeclarationName> findRulesInFile(JsgfFile file, String ruleText) {
    return findRulesInFile(file, ruleText, false);
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findRulesInFile(JsgfFile file, String ruleText, boolean publicOnly) {
    Collection<JsgfRuleDeclarationName> rules =
        RuleStubIndex.getRulesByQualifiedName(ruleText, file.getProject(), GlobalSearchScope.fileScope(file));
    if (publicOnly) {
      return rules.stream().filter(JsgfRuleDeclarationName::isPublicRule)
          .collect(Collectors.toList());
    }
    return new ArrayList<>(rules);
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findRulesInFile(JsgfFile file) {
    return findRulesInFile(file, false);
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findRulesInFile(JsgfFile file, boolean publicOnly) {
    Collection<JsgfRuleDeclarationName> rules = RuleStubIndex.getRulesInFile(file);
    if (publicOnly) {
      return rules.stream().filter(JsgfRuleDeclarationName::isPublicRule)
          .collect(Collectors.toList());
    }
    return new ArrayList<>(rules);
  }

  public static boolean isFirstDeclarationInFile(JsgfRuleDeclarationName ruleDecName) {
    return isFirstDeclarationInFile((JsgfFile) ruleDecName.getContainingFile().getOriginalFile(), ruleDecName);
  }

  public static boolean isFirstDeclarationInFile(JsgfFile file, JsgfRuleDeclarationName ruleDecName) {
    List<JsgfRuleDeclarationName> rules = findRulesInFile(
        file, ruleDecName.getRuleName());
    JsgfRuleDeclarationName first = rules.stream().min(Comparator.comparingInt(PsiElement::getTextOffset)).orElse(null);
    return first == null
        || (first.getTextOffset() == ruleDecName.getFirstChild().getTextOffset()
        && first.getRuleName().equals(ruleDecName.getRuleName()));
  }

  /**
   * Finds rules in all project files that the match the import name.
   * If '*' is imported, it adds all public rules in the file
   *
   * @param importName the importNameElement which we are trying to resolve
   * @param publicOnly Whether or not the private rules should be added in the result
   * @return a list of RuleDeclarationNames
   */
  @NotNull
  public static List<JsgfRuleDeclarationName> findImportRules(JsgfRuleImportName importName,
      boolean publicOnly) {
    List<JsgfRuleDeclarationName> result = new ArrayList<>();
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
   * @return a list of RuleDeclarationNames
   */
  @NotNull
  public static List<JsgfRuleDeclarationName> findImportRulesByPackage(JsgfRuleImportName importName,
      boolean publicOnly) {
    List<JsgfRuleDeclarationName> result = new ArrayList<>();
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
   * It only finds files that are in the same module as the file from which this is being called,
   * because the "find usages" functionality of Intellij also only searches within a module.
   *
   * @param importName the importNameElement which we are trying to resolve
   * @return a list of JsgfFiles which match the given importName
   */
  @NotNull
  public static List<JsgfFile> findFilesByPackage(JsgfRuleImportName importName) {
    List<JsgfFile> result = findFilesByPackageGrammarName(importName);
    result.addAll(findFilesByPackageFilePath(importName));
    return result;
  }

  @NotNull
  public static List<JsgfFile> findFilesByPackageFilePath(JsgfRuleImportName importName) {
    List<JsgfFile> result = new ArrayList<>();
    if (importName == null) return result;
    JsgfFile currentFile = (JsgfFile) importName.getContainingFile();
    VirtualFile currentVirtualFile = currentFile.getOriginalFile().getVirtualFile();
    if (currentVirtualFile == null) return result;
    String currentFilePath = currentVirtualFile.getCanonicalPath();
    Project project = currentFile.getProject();
    // Matching by file path
    Collection<VirtualFile> virtualFiles =
        FileTypeIndex.getFiles(JsgfFileType.INSTANCE, GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : findFilePathMatches(currentFilePath, importName.getFullyQualifiedGrammarName(), virtualFiles)) {
      result.add((JsgfFile) PsiManager.getInstance(project).findFile(virtualFile));
    }
    return result;
  }

  @NotNull
  public static List<JsgfFile> findFilesByPackageGrammarName(JsgfRuleImportName importName) {
    List<JsgfFile> result = new ArrayList<>();
    if (importName == null) return result;
    JsgfFile currentFile = (JsgfFile) importName.getContainingFile();
    VirtualFile currentVirtualFile = currentFile.getOriginalFile().getVirtualFile();
    if (currentVirtualFile == null) return result;
    Project project = currentFile.getProject();
    // Matching by grammar name
    String fullyQualifiedGrammarName = importName.getFullyQualifiedGrammarName();
    Module module = ModuleUtil.findModuleForPsiElement(importName);
    if (module == null) return result;
    Collection<JsgfGrammarName> allMatchingGrammars = GrammarStubIndex.getGrammarsByName(
        fullyQualifiedGrammarName, project, GlobalSearchScope.moduleScope(module));
    for (JsgfGrammarName grammarName : allMatchingGrammars) {
      result.add((JsgfFile) grammarName.getContainingFile());
    }
    return result;
  }

  public static String replaceDotWithSeparator(String string) {
    return string.replaceAll("\\s*\\.\\s*",
        Matcher.quoteReplacement(FileSystems.getDefault().getSeparator()));
  }

  public static String stripExtension(String fileName) {
    int lastDot = fileName.lastIndexOf('.');
    return fileName.substring(0, lastDot > -1 ? lastDot : fileName.length());
  }

  public static List<VirtualFile> findFilePathMatches(String currentPathString, String fullyQualifiedGrammarNameImport, Collection<VirtualFile> otherVirtualFiles) {
    TreeMap<Integer, List<VirtualFile>> result = new TreeMap<>();
    for (VirtualFile otherVirtualFile : otherVirtualFiles) {
      String otherPathString = otherVirtualFile.getCanonicalPath();
      if (otherPathString == null) continue;
      Path otherPath = Path.of(otherPathString);
      Path currentPath = Path.of(currentPathString);
      Path commonParent = deepestCommonParent(otherPath, currentPath);
      if (commonParent == null) continue;
      Path otherDirectory = otherPath.getParent();
      Path importDirectoryPath = Path.of(replaceDotWithSeparator(
          JsgfPsiImplInjections.packageNameFromFQGN(fullyQualifiedGrammarNameImport)));
      if (otherDirectory.endsWith(importDirectoryPath)) {
        String noExtension = stripExtension(otherPath.getFileName().toString());
        String importFileNameString = JsgfPsiImplInjections.simpleGrammarNameFromFQGN(
            fullyQualifiedGrammarNameImport);
        if (noExtension.equals(importFileNameString)) {
          result.computeIfAbsent(commonParent.getNameCount(), k -> new ArrayList<>()).add(otherVirtualFile);
        }
      }
    }
    return result.isEmpty() ? Collections.emptyList() : result.lastEntry().getValue();
  }

  public static Path deepestCommonParent(Path path1, Path path2) {
    Path root = path1.getRoot();
    if (root == null || !root.equals(path2.getRoot())) {
      return null;
    }
    int i = 0;
    Iterator<Path> path1It = path1.iterator();
    Iterator<Path> path2It = path2.iterator();
    Path path1Next = path1It.next();
    Path path2Next = path2It.next();
    while (path1It.hasNext() && path2It.hasNext() && path1Next.equals(path2Next)) {
      path1Next = path1It.next();
      path2Next = path2It.next();
      i++;
    }
    return root.resolve(path1.subpath(0, i));
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findAllRules(Project project) {
    List<JsgfRuleDeclarationName> allRules = new ArrayList<>();
    for (String ruleName : RuleStubIndex.INSTANCE.getAllKeys(project)) {
      allRules.addAll(
          RuleStubIndex.getRulesByQualifiedName(ruleName, project, GlobalSearchScope.allScope(project)));
    }
    return allRules;
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findAllRules(Project project, String name) {
    return new ArrayList<>(
        RuleStubIndex.getRulesByQualifiedName(name, project, GlobalSearchScope.allScope(project)));
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