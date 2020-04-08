package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationSubtree;
import com.asherbernardi.jsgfplugin.psi.impl.RuleNameElement;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Class to keep track of the rules in all files, so that we don't have
 * to do to a project-wide search every time we resolve a reference.
 * For proper use, if a reference to a rule cannot be resolved with
 * the data retrieved from this index, call reindex() just to be sure
 * that no rules are missing from the index. This is not a significant
 * performance hit, because you can only reindex once every 3 seconds.
 * So if a file has a lot of unresolved references, it will use the
 * first reindexing to check for almost all the rules.
 * The searching in this index happens on a module-wide basis, so
 * files in separate modules cannot refer to each other.
 * TODO: Use the intellij plugin provided indexing framework
 * @author asherbernardi
 */
public class JsgfFileIndex {
  private static Logger logger = Logger.getInstance(JsgfFileIndex.class);
  // maps every file to the rules in that file
  private HashMap<JsgfFile, List<RuleNameElement>> index;
  // maps every file to just the public rules in that file
  private HashMap<JsgfFile, List<RuleNameElement>> publicRulesIndex;
  private Module myModule;
  private long time;
  // static instances of the index for each module in the project which has
  // Jsgf files
  private static final HashMap<Module, JsgfFileIndex> _modules = new HashMap<>();

  /**
   * Creates a new instance of the index for a given module.
   * This constructor assumes that the module exists, so only call it
   * inside the <code>ensureExistence</code> method.
   */
  private JsgfFileIndex(Module module) {
    this.myModule = module;
    this.index = new HashMap<>();
    this.publicRulesIndex = new HashMap<>();
    this.time = 0;
    _modules.put(module, this);
    reindex(module);
  }

  /**
   * Get all Jsgf files which have been index from a module
   * @param module the module to query
   * @return a list of all JsgfFiles
   */
  @NotNull
  public static List<JsgfFile> getFiles(Module module) {
    if (!ensureExistence(module))
      return new ArrayList<>();
    return new ArrayList<>(_modules.get(module).index.keySet());
  }

  /**
   * Get the rules which have been indexed for a file
   * @param file the file to search
   * @return a list of PSI rule elements
   */
  @NotNull
  public static List<RuleNameElement> getRulesInFile(JsgfFile file) {
    Module module = ModuleUtil.findModuleForFile(file.getOriginalFile());
    if (!ensureExistence(module)) {
      return new ArrayList<>();
    }
    if (!_modules.get(module).index.containsKey(file))
      reindex(file);
    List<RuleNameElement> rules = _modules.get(module).index.get(file);
    return rules != null ? rules : new ArrayList<>();
  }

  /**
   * Get the public rules which have been indexed for a file
   * @param file the file to search
   * @return a list of PSI rule elements
   */
  @NotNull
  public static List<RuleNameElement> getPublicRulesInFile(JsgfFile file) {
    Module module = ModuleUtil.findModuleForFile(file.getOriginalFile());
    if (!ensureExistence(module))
      return new ArrayList<>();
    if (!_modules.get(module).index.containsKey(file))
      reindex(file);
    List<RuleNameElement> rules = _modules.get(module).publicRulesIndex.get(file);
    return rules != null ? rules : new ArrayList<>();
  }

  /**
   * Reindexes this index if it has been more than 3 seconds since the last one
   * @param module the module to reindex
   */
  private static void checkReindexing(Module module) {
    JsgfFileIndex index = _modules.get(module);
    if (System.currentTimeMillis() - index.time > 3000) {
      index.reindexRules();
      index.reindexFiles();
      index.time = System.currentTimeMillis();
    }
  }

  private void forceReindexing() {
    reindexRules();
    reindexFiles();
    time = System.currentTimeMillis();
  }

  /**
   * Ensures that there is an index associated with this module.
   * @param module the module to check
   * @return <code>true</code> if we successfully ensured the existence of the module
   *         <code>false</code> if module is null, meaning that we cannot create an index
   */
  private static boolean ensureExistence(Module module) {
    if (module == null) {
      logger.warn("Cannot index a null module.");
      return false;
    }
    if (_modules.get(module) == null)
      new JsgfFileIndex(module);
    return true;
  }

  /**
   * Search the module again to index the files and rules associated with the given file.
   * @param file the file which we are concerned with. We will reindex the
   *             whole module within which this file is saved.
   */
  public static void reindex(PsiFile file) {
    Module module = ModuleUtil.findModuleForFile(file.getOriginalFile());
    reindex(module);
  }

  /**
   * Search the module again to index the files and rules
   * @param module the module to reindex
   */
  public static void reindex(Module module) {
    if (!ensureExistence(module))
      return;
    checkReindexing(module);
  }

  /**
   * Search for rules in a file
   * @param file the file to search
   */
  private void reindexRulesInFile(JsgfFile file) {
    List<RuleNameElement> names = new ArrayList<>();
    List<RuleNameElement> publicNames = new ArrayList<>();
    Collection<RuleDeclarationSubtree> ruleDecs = PsiTreeUtil.findChildrenOfType(file,
        RuleDeclarationSubtree.class);
    for (RuleDeclarationSubtree ruleDec : ruleDecs) {
      RuleNameElement ruleName = ruleDec.getRuleNameElement();
      if (ruleName != null && ruleName.getName() != null) {
        names.add(ruleName);
        if (ruleDec.isPublicRule())
          publicNames.add(ruleName);
      }
    }
    index.put(file, names);
    publicRulesIndex.put(file, publicNames);
  }

  /**
   * Search again for all the rules in every file which has been indexed for this module
   * @param module the module to reindex
   */
  public static void reindexRules(Module module) {
    if (!ensureExistence(module))
      return;
    JsgfFileIndex index = _modules.get(module);
    index.reindexRules();
  }

  private void reindexRules() {
    // We duplicate the HashSet to ensure safe adding of elements
    for (JsgfFile file : new HashSet<>(index.keySet())) {
      reindexRulesInFile(file);
    }
  }

  /**
   * Search again for all the files in a given module, then reindex those for rules
   * @param module the module to reindex
   */
  public static void reindexFiles(Module module) {
    if (!ensureExistence(module))
      return;
    JsgfFileIndex index = _modules.get(module);
    index.reindexFiles();
  }

  private void reindexFiles() {
    Collection<VirtualFile> virtualFiles =
        FileTypeIndex.getFiles(JsgfFileType.INSTANCE, GlobalSearchScope.allScope(myModule.getProject()));
    HashSet<JsgfFile> foundFiles = new HashSet<>();
    for (VirtualFile virtualFile : virtualFiles) {
      JsgfFile file = (JsgfFile) PsiManager.getInstance(myModule.getProject()).findFile(virtualFile);
      if (ModuleUtil.findModuleForFile(file) == myModule) {
        if (!index.containsKey(file)) {
          reindexRulesInFile(file);
        }
        foundFiles.add(file);
      }
    }
    // Make sure that any files that were deleted are removed from the index
    // we duplicate the HashSet to ensure safe removal
    for (JsgfFile file : new HashSet<>(index.keySet())) {
      if (!foundFiles.contains(file)) {
        index.remove(file);
      }
    }
  }

  /**
   * A class which allows an index to be reindexed more than once every 3 seconds. Used mostly for
   * testing, when the files need to changed rapidly.
   */
  public static class ReindexingForcer {
    private JsgfFileIndex myIndex;

    public ReindexingForcer(JsgfFile file) {
      Module module = ModuleUtil.findModuleForFile(file.getOriginalFile());
      if (!ensureExistence(module))
        throw new IllegalArgumentException("File provided does not exist within a module");
      myIndex = _modules.get(module);
    }

    public void forceReindexing() {
      myIndex.forceReindexing();
    }
  }
}
