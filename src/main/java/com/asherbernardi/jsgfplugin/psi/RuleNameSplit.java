package com.asherbernardi.jsgfplugin.psi;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleNameSplit {

  @NotNull
  final String uqrn;
  @NotNull
  final TextRange uqrnRange;
  @Nullable
  final String simpleGrammarName;
  @Nullable
  final TextRange simpleGrammarNameRange;
  @Nullable
  final String fqgn;
  @Nullable
  final TextRange fqgnRange;
  @Nullable
  final String packageName;
  @Nullable
  final TextRange packageNameRange;

  private RuleNameSplit(String fqrn, @NotNull TextRange uqrnRange, @Nullable TextRange sgnRange, @Nullable TextRange pnRange) {
    this.uqrnRange = uqrnRange;
    this.uqrn = uqrnRange.substring(fqrn);
    this.simpleGrammarNameRange = sgnRange;
    this.simpleGrammarName = sgnRange != null ? sgnRange.substring(fqrn) : null;
    this.packageNameRange = pnRange;
    this.packageName = pnRange != null ? pnRange.substring(fqrn) : null;
    this.fqgnRange = sgnRange != null ? new TextRange(0, sgnRange.getEndOffset()) : null;
    this.fqgn = this.fqgnRange != null ? this.fqgnRange.substring(fqrn) : null;
  }

  @NotNull
  private static TextRange getUqrnRange(String fqrn) {
    int uqrnEnd = fqrn.length();
    int lastDot = fqrn.lastIndexOf('.');
    int uqrnStart = lastDot + 1;
    return new TextRange(uqrnStart, uqrnEnd);
      }

  @Nullable
  private static TextRange getSimpleGrammarNameRange(String fqrn, @NotNull TextRange uqrnRange) {
    int sgnEnd = uqrnRange.getStartOffset() - 1;
    if (sgnEnd == -1) return null;
    int lastDot = fqrn.lastIndexOf('.', sgnEnd - 1);
    int uqgnStart = lastDot + 1;
    return new TextRange(uqgnStart, sgnEnd);
    }

  @Nullable
  private static TextRange getPackageNameRange(String fqrn, @Nullable TextRange sgnRange) {
    if (sgnRange == null) return null;
    int pnEnd = sgnRange.getStartOffset() - 1;
    if (pnEnd == -1) return null;
    int pnStart = 0;
    return new TextRange(pnStart, pnEnd);
  }

  public static RuleNameSplit fromFQRN(String fqrn) {
    TextRange uqrn = getUqrnRange(fqrn);
    TextRange sgn = getSimpleGrammarNameRange(fqrn, uqrn);
    TextRange pn = getPackageNameRange(fqrn, sgn);
    return new RuleNameSplit(fqrn, uqrn, sgn, pn);
  }

  @NotNull
  public String getUQRN() {
    return uqrn;
  }

  @NotNull
  public TextRange getUQRNRange() {
    return uqrnRange;
  }

  public String replaceUQRN(String otherUQRN) {
    return buildString(getPackageName(), getSimpleGrammarName(), otherUQRN);
  }

  @Nullable
  public String getFQGN() {
    return fqgn;
  }

  @Nullable
  public TextRange getFQGNRange() {
    return fqgnRange;
  }

  public boolean hasFQGN() {
    return getFQGN() != null;
  }

  @Nullable
  public String getSimpleGrammarName() {
    return simpleGrammarName;
  }

  @Nullable
  public TextRange getSimpleGrammarNameRange() {
    return simpleGrammarNameRange;
  }

  public boolean hasSimpleGrammarName() {
    return getSimpleGrammarName() != null;
  }

  public String replaceSimpleGrammarName(String otherSimpleGrammarName) {
    return buildString(getPackageName(), otherSimpleGrammarName, getUQRN());
  }

  public String replaceFQGN(String otherFQGN) {
    return buildString(otherFQGN, getUQRN());
  }

  @Nullable
  public String getPackageName() {
    return packageName;
  }

  @Nullable
  public TextRange getPackageNameRange() {
    return packageNameRange;
  }

  public boolean hasPackageName() {
    return getPackageName() != null;
  }

  public String replacePackageName(String otherPackageName) {
    return buildString(otherPackageName, getSimpleGrammarName(), getUQRN());
  }

  private static String buildString(@Nullable String packageName, @Nullable String simpleGrammarName, @NotNull String uqrn) {
    StringBuilder builder = new StringBuilder();
    if (packageName != null) {
      builder.append(packageName).append('.');
    }
    if (simpleGrammarName != null) {
      builder.append(simpleGrammarName).append('.');
    }
    builder.append(uqrn);
    return builder.toString();
  }

  private static String buildString(@Nullable String fqgn, @NotNull String uqrn) {
    StringBuilder builder = new StringBuilder();
    if (fqgn != null) {
      builder.append(fqgn).append('.');
    }
    builder.append(uqrn);
    return builder.toString();
  }
}
