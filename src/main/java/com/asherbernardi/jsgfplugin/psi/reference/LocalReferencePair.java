package com.asherbernardi.jsgfplugin.psi.reference;

public class LocalReferencePair extends ReferencePair<GrammarNameReference, RuleNameReference> {

  public LocalReferencePair(GrammarNameReference grammarReference, RuleNameReference ruleReference) {
    super(grammarReference, ruleReference);
  }
}
