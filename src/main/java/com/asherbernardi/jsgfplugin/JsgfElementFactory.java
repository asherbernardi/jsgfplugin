package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.ImportName;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;

public class JsgfElementFactory {

  private static final String RULE_FILE_TEXT =
      "#JSGF V1.0;\n"
      + "grammar file;"
      + "<rule> = <%s>;";
  private static final String RULE_IMPORT_FILE_TEXT =
      "#JSGF V1.0;\n"
          + "grammar file;"
          + "import <%s>;";

  public static RuleName createRule(Project project, String name) {
    final JsgfFile file = createFile(project, String.format(RULE_FILE_TEXT, name));
    return file.getRuleDefinitions().get(0)
        .getAlternatives()
        .getSequenceList().get(0)
        .getRuleReferenceList().get(0).getRuleReferenceName();
  }

  public static ImportName createRuleImport(Project project, String importName) {
    final JsgfFile file = createFile(project, String.format(RULE_IMPORT_FILE_TEXT, importName));
    return file.getImportNames().get(0);
  }

  public static JsgfFile createFile(Project project, String text) {
    String name = "dummy.simple";
    return (JsgfFile) PsiFileFactory.getInstance(project).
        createFileFromText(name, JsgfFileType.INSTANCE, text);
  }

}
