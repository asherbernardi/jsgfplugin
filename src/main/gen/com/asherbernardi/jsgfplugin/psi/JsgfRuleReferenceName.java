// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.asherbernardi.jsgfplugin.psi.reference.RuleReferenceReference;
import com.intellij.psi.search.SearchScope;

public interface JsgfRuleReferenceName extends RuleName {

  @NotNull
  RuleReferenceReference getReference();

  @NotNull
  SearchScope getUseScope();

}
