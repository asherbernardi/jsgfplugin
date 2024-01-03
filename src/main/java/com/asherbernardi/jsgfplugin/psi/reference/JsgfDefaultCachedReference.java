package com.asherbernardi.jsgfplugin.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JsgfDefaultCachedReference<R extends PsiElement> extends PsiReferenceBase<R>
    implements PsiPolyVariantReference {


  public JsgfDefaultCachedReference(@NotNull R element, TextRange rangeInElement) {
    super(element, rangeInElement);
  }

  public JsgfDefaultCachedReference(@NotNull R element, TextRange rangeInElement, boolean soft) {
    super(element, rangeInElement, soft);
  }

  @NotNull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    return CachedValuesManager.getCachedValue(myElement, getCachedValueProvider(myElement));
  }

  protected abstract CachedValueProvider<ResolveResult[]> getCachedValueProvider(R element);

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    if (resolveResults.length == 1)
      return resolveResults[0].getElement();
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj.getClass() != getClass()) return false;
    JsgfDefaultCachedReference other = (JsgfDefaultCachedReference) obj;
    return getElement().equals(other.getElement());
  }
}
