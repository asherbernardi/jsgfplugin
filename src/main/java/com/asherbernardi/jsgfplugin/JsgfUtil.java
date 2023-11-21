package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarStubIndex;
import com.asherbernardi.jsgfplugin.psi.stub.RuleStubIndex;
import com.google.common.collect.AbstractIterator;
import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeElementVisitor;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helper methods for Jsgf PSI elements
 * @author asherbernardi
 */
public class JsgfUtil {

  @Nullable
  public static PsiReference getReference(PsiElement element) {
    PsiReference ref = element.getReference();
    if (ref != null) return ref;
    PsiReference[] refs = element.getReferences();
    if (refs.length == 0) return null;
    return refs[0];
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findRulesInFile(JsgfFile file, String ruleText) {
    return findRulesInFile(file, ruleText, false);
  }

  @NotNull
  public static List<JsgfRuleDeclarationName> findRulesInFile(JsgfFile file, String ruleText, boolean publicOnly) {
    Collection<JsgfRuleDeclarationName> rules =
        RuleStubIndex.getRulesByQualifiedName(ruleText, file.getProject(), GlobalSearchScope.fileScope(file.getOriginalFile()));
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
    if (importName == null) return Collections.emptyList();
    JsgfFile currentFile = (JsgfFile) importName.getContainingFile();
    VirtualFile currentVirtualFile = currentFile.getOriginalFile().getVirtualFile();
    if (currentVirtualFile == null) return Collections.emptyList();
    Project project = currentFile.getProject();
    // Matching by file path
    List<JsgfFile> result = new ArrayList<>();
    Module module = ModuleUtil.findModuleForPsiElement(importName);
    if (module == null) return Collections.emptyList();
    for (VirtualFile folder : ModuleRootManager.getInstance(module).getSourceRoots()) {
      addFileFromContentRoot(folder, importName, project, result);
    }
    return result;
  }

  private static void addFileFromContentRoot(VirtualFile root, JsgfRuleImportName importName, Project project, List<JsgfFile> matches) {
    String FQGN = importName.getFullyQualifiedGrammarName();
    String[] dotSplit = FQGN.split("\\s*\\.\\s*");
    VirtualFile currentFolder = root;
    int i = 0;
    F: while (currentFolder != null && i < dotSplit.length) {
      VirtualFile[] children = currentFolder.getChildren();
      if (children == null) break;
      String targetNextName = dotSplit[i];
      for (VirtualFile child : children) {
        if (child.getNameWithoutExtension().equals(targetNextName)) {
          currentFolder = child;
          i++;
          continue F;
        }
      }
      break;
    }
    // If we've gotten all the way through the package and grammar name
    if (i == dotSplit.length && currentFolder != null) {
      JsgfFile file = (JsgfFile) PsiManager.getInstance(project).findFile(currentFolder);
      if (file != null) {
        matches.add(file);
      }
    }
  }

  public static boolean importNameMatchesFilePath(JsgfRuleImportName importName, JsgfFile targetFile, Set<VirtualFile> contentRoots) {
    String FQGN = importName.getFullyQualifiedGrammarName();
    String[] dotSplit = FQGN.split("\\s*\\.\\s*");
    VirtualFile currentFile = targetFile.getVirtualFile();
    int i = dotSplit.length - 1;
    while (currentFile != null && i >= 0) {
      String targetNextName = dotSplit[i];
      if (!currentFile.getNameWithoutExtension().equals(targetNextName)) {
        return false;
      }
      currentFile = currentFile.getParent();
      i--;
    }
    // If we've gotten all the way through the package and grammar name
    if (i != -1) {
      return false;
    }
    return contentRoots.contains(currentFile);
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

  public static String replaceSeparatorWithDot(String string) {
    return string.replaceAll("\\s*([\\\\/]|://)\\s*", ".");
  }

  public static String stripExtension(String fileName) {
    int lastDot = fileName.lastIndexOf('.');
    return fileName.substring(0, lastDot > -1 ? lastDot : fileName.length());
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

  private static boolean packageNameCouldMatch(JsgfRuleImportName importName, JsgfFile file) {
    String fqgn = importName.getFullyQualifiedGrammarName();
    return fqgn != null && (
        (file.getGrammarName() != null && fqgn.equals(file.getGrammarName().getName()))
            || JsgfPsiImplInjections.simpleGrammarNameFromFQGN(fqgn).equals(stripExtension(file.getName()))
    );
  }

  public static List<LookupElement> allFilesAsImportVariants(JsgfRuleImportName importName) {
    Project project = importName.getProject();
    Module module = ModuleUtil.findModuleForPsiElement(importName);
    if (module == null) return Collections.emptyList();
    List<LookupElement> result = new ArrayList<>();
    // Add by pure grammar name
    String importGrammarName = importName.getFullyQualifiedGrammarName();
    JsgfFile myFile = (JsgfFile) importName.getContainingFile();
    JsgfGrammarName myGrammarName = myFile.getGrammarName();
    String myGrammarNameString = myGrammarName != null ? myGrammarName.getName() : "";
    Collection<JsgfGrammarName> allMatchingGrammars = GrammarStubIndex.getAllGrammarsInModule(project, module);
    allMatchingGrammars.stream()
        .filter(gn -> !gn.getName().equals(myGrammarNameString)) // exclude self
        .filter(gn -> gn.getName().startsWith(importGrammarName))
        .forEach(gn -> result.add(
            LookupElementBuilder.create(
                    importGrammarName.isEmpty() ? gn.getFQGN()
                        : gn.getFQGN().replace(importGrammarName + ".", "")
                )
                .withLookupString(gn.getSimpleGrammarName())
                .withIcon(JsgfIcons.FILE)
                .withTypeText("Grammar " + gn.getContainingFile().getName())
                .withInsertHandler(GRAMMAR_NAME_HANDLER)
        ));
    // Add by file path
    String fqgn = importName.getFullyQualifiedGrammarName();
    VirtualFile[] roots = ModuleRootManager.getInstance(module).getSourceRoots();
    for (VirtualFile root : roots) {
      addVariantsFromRoot(root, myFile.getOriginalFile().getVirtualFile(), fqgn, result);
    }
    return result;
  }

  private static final InsertHandler<LookupElement> GRAMMAR_NAME_HANDLER = (context, item) -> {
    int offset = context.getTailOffset();
    Project project = context.getProject();
    Editor editor = context.getEditor();
    WriteCommandAction.runWriteCommandAction(project, () -> editor.getDocument().insertString(offset, "."));
    context.getEditor().getCaretModel().moveToOffset(offset + 1);
    AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
  };

  private static void addVariantsFromRoot(VirtualFile root, VirtualFile excludeFile, String fqgn, List<LookupElement> variants) {
    VirtualFile currentFolder = root;
    if (!fqgn.isEmpty()) {
      String[] dotSplit = fqgn.split("\\s*\\.\\s*");
      int i = 0;
      F: while (currentFolder != null && i < dotSplit.length) {
        VirtualFile[] children = currentFolder.getChildren();
        if (children == null)
          break;
        String targetNextName = dotSplit[i];
        for (VirtualFile child : children) {
          if (child.getNameWithoutExtension().equals(targetNextName)) {
            currentFolder = child;
            i++;
            continue F;
          }
        }
        break;
      }
      if (i != dotSplit.length || currentFolder == null) return;
    }
    // If we've gotten all the way through the splits of the grammar name so far
    VirtualFile[] children = currentFolder.getChildren();
    if (children != null) {
      // now we add all the subfolders as variants
      for (VirtualFile file : children) {
        if (file.isDirectory()) {
          variants.add(LookupElementBuilder.create(file.getNameWithoutExtension())
              .withIcon(JsgfIcons.FILE)
              .withTypeText("Package " + file.getName())
              .withInsertHandler(GRAMMAR_NAME_HANDLER)
          );
        }
      }
      // now we breadth-first search all the folders for Jsgf files
      // Arbitrarily stopping after visiting 100 files to prevent slow resolution
      int max = 100;
      Deque<VirtualFile> queue = new LinkedList<>(Arrays.asList(children));
      int i = 0;
      while (!queue.isEmpty()) {
        if (i++ > max) break;
        VirtualFile current = queue.poll();
        if (current.isDirectory()) {
          Arrays.asList(current.getChildren()).forEach(queue::offer);
        } else {
          if (!current.getFileType().equals(JsgfFileType.INSTANCE)) continue;
          if (current.equals(excludeFile)) continue;
          LinkedList<String> variant = new LinkedList<>();
          variant.add(current.getNameWithoutExtension());
          for (VirtualFile parent = current.getParent(); parent != null && !parent.equals(currentFolder); parent = parent.getParent()) {
            variant.addFirst(parent.getNameWithoutExtension());
          }
          String variantString = String.join(".", variant);
          variants.add(LookupElementBuilder.create(variantString)
              .withIcon(JsgfIcons.FILE)
              .withLookupString(current.getNameWithoutExtension())
              .withTypeText("Grammar " + current.getName())
              .withInsertHandler(GRAMMAR_NAME_HANDLER)
          );
        }
      }
    }
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

  static void revertHighlighterIterator(HighlighterIterator iterator, int targetStart) {
    while (!iterator.atEnd() && iterator.getStart() < targetStart) {
      iterator.advance();
    }
    while (!iterator.atEnd() && iterator.getStart() > targetStart) {
      iterator.retreat();
    }
  }

  private static class StartsWithVisitor extends TreeElementVisitor {

    final byte[] prefix;
    int i;

    StartsWithVisitor(String prefix) {
      this.prefix = prefix.getBytes();
      this.i = 0;
    }

    @Override
    public void visitLeaf(LeafElement leaf) {
      byte[] text = leaf.getText().getBytes();
      int j = 0;
      while (i < prefix.length && j < text.length) {
        if (text[j++] != prefix[i++]) {
          throw new StartsWithEndedException(false);
        }
      }
      if (i >= prefix.length) {
        throw new StartsWithEndedException(true);
      }
    }

    @Override
    public void visitComposite(CompositeElement composite) {
      for (TreeElement child : iterableOfChildren(composite)) {
        child.acceptTree(this);
      }
    }
  }

  private static class StartsWithEndedException extends RuntimeException {
    final boolean success;
    StartsWithEndedException(boolean success) {
      this.success = success;
    }
  }

  static boolean textStartsWith(ASTNode element, String prefix) {
    if (element instanceof TreeElement) {
      try {
        ((TreeElement) element).acceptTree(new StartsWithVisitor(prefix));
      } catch (StartsWithEndedException e) {
        return e.success;
      }
    }
    return element.getText().startsWith(prefix);
  }

  public static Iterable<TreeElement> iterableOfChildren(TreeElement parent) {
    return () -> new Iterator<>() {
      TreeElement current = parent.getFirstChildNode();

      @Override
      public boolean hasNext() {
        return current != null;
      }

      @Override
      public TreeElement next() {
        TreeElement oldCurrent = current;
        current = current.getTreeNext();
        return oldCurrent;
      }
    };
  }

  public static Iterable<LeafElement> leafIterableNext(LeafElement node) {
    return () -> leafIteratorNext(node);
  }

  public static Iterator<LeafElement> leafIteratorNext(LeafElement node) {
    return new AbstractIterator<LeafElement>() {
      LeafElement current = node;

      @Override
      protected LeafElement computeNext() {
        TreeElement sibling = current.getTreeNext();
        if (sibling instanceof LeafElement) {
          return current = (LeafElement) sibling;
        } if (sibling != null) {
          LeafElement nephew = descendToFirstLeaf(sibling);
          if (nephew != null) {
            return current = nephew;
          }
        }
        for (TreeElement parent = current.getTreeParent(); parent != null; parent = parent.getTreeParent()) {
          for (TreeElement parentSibling = parent.getTreeNext(); parentSibling != null; parentSibling = parentSibling.getTreeNext()) {
            LeafElement firstLeaf = descendToFirstLeaf(parentSibling);
            if (firstLeaf != null) {
              return current = firstLeaf;
            }
          }
        }
        return endOfData();
      }
    };
  }

  @Nullable
  public static LeafElement descendToFirstLeaf(ASTNode node) {
    if (node == null) {
      return null;
    }
    if (node instanceof LeafElement) {
      return (LeafElement) node;
    }
    return descendToFirstLeaf(node.getFirstChildNode());
  }

  public static Iterable<LeafElement> leafIterablePrev(LeafElement node) {
    return () -> leafIteratorPrev(node);
  }

  public static Iterator<LeafElement> leafIteratorPrev(LeafElement node) {
    return new AbstractIterator<LeafElement>() {
      LeafElement current = node;

      @Override
      protected LeafElement computeNext() {
        TreeElement sibling = current.getTreePrev();
        if (sibling instanceof LeafElement) {
          return current = (LeafElement) sibling;
        } if (sibling != null) {
          LeafElement nephew = descendToLastLeaf(sibling);
          if (nephew != null) {
            return current = nephew;
          }
        }
        for (TreeElement parent = current.getTreeParent(); parent != null; parent = parent.getTreeParent()) {
          for (TreeElement parentSibling = parent.getTreePrev(); parentSibling != null; parentSibling = parentSibling.getTreePrev()) {
            LeafElement firstLeaf = descendToLastLeaf(parentSibling);
            if (firstLeaf != null) {
              return current = firstLeaf;
            }
          }
        }
        return endOfData();
      }
    };
  }

  @Nullable
  public static LeafElement descendToLastLeaf(ASTNode node) {
    if (node == null) {
      return null;
    }
    if (node instanceof LeafElement) {
      return (LeafElement) node;
    }
    return descendToLastLeaf(node.getLastChildNode());
  }
}