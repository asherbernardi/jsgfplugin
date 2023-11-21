package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfSyntaxHighlighter.JsgfHighlightType;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Allows the creation of a designated page in the color scheme settings
 * for changing the color scheme for JSGF syntax.
 * @author asherbernardi
 */
public class JsgfColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
      new AttributesDescriptor("Rule", JsgfHighlightType.RULE_NAME.getTextAttributesKey()),
      new AttributesDescriptor("String", JsgfHighlightType.STRING.getTextAttributesKey()),
      new AttributesDescriptor("Simple tokens", JsgfHighlightType.TERMINAL.getTextAttributesKey()),
      new AttributesDescriptor("Identifier", JsgfHighlightType.ID.getTextAttributesKey()),
      new AttributesDescriptor("Keyword", JsgfHighlightType.KEYWORD.getTextAttributesKey()),
      new AttributesDescriptor("Tag", JsgfHighlightType.TAG.getTextAttributesKey()),
      new AttributesDescriptor("Weight", JsgfHighlightType.WEIGHT.getTextAttributesKey()),
      new AttributesDescriptor("Line comment", JsgfHighlightType.LINE_COMMENT.getTextAttributesKey()),
      new AttributesDescriptor("Block comment", JsgfHighlightType.BLOCK_COMMENT.getTextAttributesKey()),
      new AttributesDescriptor("Bad value", JsgfHighlightType.BAD_CHARACTER.getTextAttributesKey()),
      new AttributesDescriptor("Bad reference", JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
  };

  @Nullable
  @Override
  public Icon getIcon() {
    return JsgfIcons.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new JsgfSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return "<kw>#JSGF V1.0 ASCII en_US;</kw>\n"
        + "<kw>grammar</kw> testGrammar;\n"
        + "<kw>import</kw> <path.to.another.rule>;\n"
        + "\n"
        + "<rule_1> = [this is the] (/3/ first|/2/ primary) rule;\n"
        + "<kw>public</kw> <rule_2> = this rule is (public|visible);\n"
        + "<rule_3> = refers to <rule_1> and to <<bad_reference>non_existent_rule</bad_reference>>;\n"
        + "// Here is a line comment\n"
        + "/* And here is\n"
        + "   a block comment */\n"
        + "/**\n"
        + " * documentation comment\n"
        + " */\n"
        + "<rule_4> = (some have tags) <tag>{This is a tag}</tag>;\n"
        + "Syntax error";
  }

  /**
   * Replaces the XML attributes inserted into the demo text to have
   * the specified syntax highlighting.
   * @return a map between the name of the tax and the key of the
   *         syntax highlighting.
   */
  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    Map<String, TextAttributesKey> tags = new HashMap<>();
    tags.put("bad_reference", JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey());
    tags.put("tag", JsgfHighlightType.TAG.getTextAttributesKey());
    tags.put("kw", JsgfHighlightType.KEYWORD.getTextAttributesKey());
    return tags;
  }

  @NotNull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return DESCRIPTORS;
  }

  @NotNull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "JSGF";
  }
}