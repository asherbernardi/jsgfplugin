package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.GrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceExp;
import com.asherbernardi.jsgfplugin.psi.JsgfStringExp;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;

public class JsgfElementFactory {

  private static final String RULE_FILE_TEXT =
      "#JSGF V1.0;\n"
      + "grammar file;"
      + "<rule> = <%s>;";
  private static final String GRAMMAR_FILE_TEXT =
      "#JSGF V1.0;\n"
          + "grammar %s;";
  private static final String RULE_IMPORT_FILE_TEXT =
      "#JSGF V1.0;\n"
          + "grammar file;"
          + "import <%s>;";
  private static final String STRING_EXP_FILE_TEXT =
      "#JSGF V1.0;\n"
          + "grammar f;"
          + "<r> = \"%s\";";

  public static RuleName createRule(Project project, String name) {
    final JsgfFile file = createFile(project, String.format(RULE_FILE_TEXT, name));
    return ((JsgfRuleReferenceExp) file.getRuleDefinitions().get(0)
        .getExpansion())
        .getRuleReferenceName();
  }

  public static GrammarName createGrammarName(Project project, String name) {
    final JsgfFile file = createFile(project, String.format(GRAMMAR_FILE_TEXT, name));
    return file.getGrammarName();
  }

  public static JsgfRuleImportName createRuleImport(Project project, String importName) {
    final JsgfFile file = createFile(project, String.format(RULE_IMPORT_FILE_TEXT, importName));
    return file.getImportNames().get(0);
  }

  public static JsgfStringExp createStringExpansion(Project project, String string) {
    final JsgfFile file = createFile(project, String.format(STRING_EXP_FILE_TEXT, string));
    return (JsgfStringExp) file.getRuleDefinitions().get(0)
        .getExpansion();
  }

  public static JsgfFile createFile(Project project, String text) {
    String name = "dummy.simple";
    return (JsgfFile) PsiFileFactory.getInstance(project).
        createFileFromText(name, JsgfFileType.INSTANCE, text);
  }

}
