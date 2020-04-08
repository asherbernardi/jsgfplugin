package com.asherbernardi.jsgfplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Intellij file type for Jsgf.
 * INSTANCE is the singleton to be used when referring to this file type.
 * @author asherbernardi
 */
public class JsgfFileType extends LanguageFileType {
  public static final JsgfFileType INSTANCE = new JsgfFileType();

  private JsgfFileType() {
    super(JsgfLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Jsgf file";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Jsgf language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "jsgf";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return JsgfIcons.FILE;
  }
}