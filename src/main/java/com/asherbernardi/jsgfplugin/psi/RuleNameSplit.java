package com.asherbernardi.jsgfplugin.psi;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleNameSplit {

  String uqrn;
  int uqrnStart;
  int uqrnEnd;
  String simpleGrammarName;
  int simpleGrammarNameStart;
  int simpleGrammarNameEnd;
  String fqgn;
  int fqgnStart;
  int fqgnEnd;
  String packageName;
  int packageNameStart;
  int packageNameEnd;

  private RuleNameSplit(String ruleText) {
    // uqrn
    int lastDot = ruleText.lastIndexOf('.');
    uqrnStart = lastDot + 1;
    uqrnEnd = ruleText.length();
    uqrn = ruleText.substring(uqrnStart, uqrnEnd);
    // fqgn
    if (lastDot != -1) {
      fqgnStart = 0;
      fqgnEnd = lastDot;
      fqgn = ruleText.substring(fqgnStart, fqgnEnd);
      // simpleGrammarName
      int secondToLastDot = fqgn.lastIndexOf('.');
      simpleGrammarNameStart = secondToLastDot + 1;
      simpleGrammarNameEnd = fqgn.length();
      simpleGrammarName = ruleText.substring(simpleGrammarNameStart, simpleGrammarNameEnd);
      // packageName
      if (secondToLastDot != -1) {
        packageNameStart = 0;
        packageNameEnd = secondToLastDot;
        packageName = ruleText.substring(packageNameStart, packageNameEnd);
      }
    }
  }

  public static RuleNameSplit fromFQRN(String fqrn) {
    return new RuleNameSplit(fqrn);
  }

  @NotNull
  public String getUQRN() {
    return uqrn;
  }

  public TextRange getUQRNRange() {
    return new TextRange(uqrnStart, uqrnEnd);
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
    if (!hasFQGN()) return null;
    return new TextRange(fqgnStart, fqgnEnd);
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
    if (!hasSimpleGrammarName()) return null;
    return new TextRange(simpleGrammarNameStart, simpleGrammarNameEnd);
  }

  public boolean hasSimpleGrammarName() {
    return getSimpleGrammarName() != null;
  }

  public String replaceSimpleGrammarName(String otherSimpleGrammarName) {
    return buildString(getPackageName(), otherSimpleGrammarName, getUQRN());
  }

  @Nullable
  public String getPackageName() {
    return packageName;
  }

  @Nullable
  public TextRange getPackageNameRange() {
    if (!hasPackageName()) return null;
    return new TextRange(packageNameStart, packageNameEnd);
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
}
