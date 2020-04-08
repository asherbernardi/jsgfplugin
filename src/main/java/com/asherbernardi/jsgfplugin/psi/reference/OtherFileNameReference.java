package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.impl.GrammarNameElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleNameElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class OtherFileNameReference extends PsiReferenceBase<ImportNameElement> implements
    PsiPolyVariantReference {

  private String unqualifiedName;
  private String qualifiedName;
  private String packageName;
  /**
   * Keeps track of the resolved elements, so that we don't go looking throughout the project
   * for grammars everytime it resolves (which destroys performance). The boolean refers to
   * <code>incompleteCode</code> from <code>multiResolve()</code>
   */
  private HashMap<Boolean, List<ResolveResult>> cachedResolve = new HashMap<>();
  /**
   * Keeps track of the text of the resolved elements at the time they were resolved. This way,
   * if you change an element in another file, the old cache will not resolve to that element.
   */
  private HashMap<ResolveResult, String> cachedResolveNames = new HashMap<>();

  public OtherFileNameReference(ImportNameElement element, TextRange range) {
    super(element, range);
    // Careful of "IntellijIdeaRulezzz" which is used to make sure the autocomplete takes context into account
    // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
    qualifiedName = element.getName();
    unqualifiedName = element.getUnqualifiedName();
    packageName = element.getFullyQualifiedGrammarName();
  }

  protected abstract List<PsiElement> getRulesByPackage();

  protected abstract List<PsiElement> getRules();

  public boolean canResolve() {
    return resolve() != null;
  }

  @Nullable
  public ResolveResult[] retrieveCache(boolean incompleteCode) {
    // if the element has been edited, we can't use the the cache
    if (!qualifiedName.equals(myElement.getName())) {
      qualifiedName = myElement.getName();
      packageName = myElement.getFullyQualifiedGrammarName();
      unqualifiedName = myElement.getUnqualifiedName();
      setRangeInElement(new TextRange(0, qualifiedName.length()));
      return null;
    }
    if (cachedResolve.containsKey(incompleteCode)) {
      List<ResolveResult> cache = cachedResolve.get(incompleteCode);
      for (ResolveResult resolve : cache) {
        // We make sure that the cache we have is not pointing to an outdated element by comparing text
        if (!((RuleNameElement) resolve.getElement()).getName().equals(cachedResolveNames.get(resolve))) {
          return null;
        }
        // We make sure the element we have matches the actual reference
        if (!isValidResolveResult(resolve)) {
          return null;
        }
      }
      return cache.toArray(new ResolveResult[cache.size()]);
    }
    return null;
  }

  /**
   * Ensures that a given ResolveResult is valid for this Reference
   *
   * @param resolve The ResolveResult to check
   * @return Can this reference resolve to the given ResolveResult <code>resolve</code>
   */
  private boolean isValidResolveResult(ResolveResult resolve) {
    PsiElement element = resolve.getElement();
    return element instanceof RuleNameElement &&
        (((RuleNameElement) element).getName().equals(unqualifiedName) || myElement.isStarImport());
  }

  @NotNull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    return multiResolve(incompleteCode, true);
  }

  @NotNull
  public ResolveResult[] multiResolve(boolean incompleteCode, boolean useCache) {
    if (useCache) {
      // if cache is empty, we had better search again, make sure nothing new was created
      ResolveResult[] cache = retrieveCache(incompleteCode);
      if (cache != null && cache.length != 0)
        return cache;
    }
    final List<PsiElement> refs = getRules();
    List<ResolveResult> results = new ArrayList<>();
    if (refs != null) {
      for (PsiElement ref : refs) {
        if (ref instanceof JsgfFile) {
          GrammarNameElement grammarName = ((JsgfFile) ref).getGrammarName();
          if (grammarName != null) {
            ResolveResult result = new PsiElementResolveResult(grammarName);
            results.add(result);
            cachedResolveNames.put(result, grammarName.getName());
          }
        } else {
          ResolveResult result = new PsiElementResolveResult(ref);
          results.add(result);
          cachedResolveNames.put(result, ((RuleNameElement) ref).getName());
        }
      }
    }
    cachedResolve.put(incompleteCode, results);
    return cachedResolve.get(incompleteCode).toArray(new ResolveResult[results.size()]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    if (resolveResults.length > 0 && myElement.isStarImport())
      return ((JsgfFile) resolveResults[0].getElement().getContainingFile()).getGrammarName();
    if (resolveResults.length == 1)
      return resolveResults[0].getElement();
    return null;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    List<PsiElement> names = getRulesByPackage();
    List<LookupElement> variants = new ArrayList<>();
    ImportNameElement element = (ImportNameElement) myElement;
    String unqualifiedName = element.getUnqualifiedName();
    String packageName = element.getFullyQualifiedGrammarName();
    if (names != null) {
      for (final PsiElement ref : names) {
        if (ref instanceof RuleDeclarationNameElement) {
          RuleNameElement name = (RuleNameElement) ref;
          if (name.getName() != null && !name.getName().isEmpty()) {
            variants.add(LookupElementBuilder
                .create(packageName + '.' + name.getName()).withIcon(JsgfIcons.FILE)
                .withTypeText(name.getContainingFile().getName())
            );
          }
        }
      }
    }
    return variants.toArray();
  }
}
