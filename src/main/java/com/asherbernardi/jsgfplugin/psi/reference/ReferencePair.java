package com.asherbernardi.jsgfplugin.psi.reference;

import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.Nullable;

public abstract class ReferencePair<G extends PsiReference, R extends PsiReference> {

  private final G grammarReference;
  private final R ruleReference;
  private static final PsiReference[] EMPTY = new PsiReference[0];

  public ReferencePair(@Nullable G grammarReference, @Nullable R ruleReference) {
    this.grammarReference = grammarReference;
    this.ruleReference = ruleReference;
  }

  @Nullable
  public G getGrammarReference() {
    return grammarReference;
  }

  @Nullable
  public R getRuleReference() {
    return ruleReference;
  }

  public PsiReference[] getReferenceArray() {
    if (getGrammarReference() == null && getRuleReference() == null) {
      return EMPTY;
    } else if (getGrammarReference() == null) {
      return new PsiReference[]{getRuleReference()};
    } else if (getRuleReference() == null) {
      return new PsiReference[]{getGrammarReference()};
    } else {
      return new PsiReference[]{getGrammarReference(), getRuleReference()};
    }
  }
}
