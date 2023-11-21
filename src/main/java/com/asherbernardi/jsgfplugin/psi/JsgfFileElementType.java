package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.tree.IStubFileElementType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.stub.JsgfPsiFileStub;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JsgfFileElementType extends IStubFileElementType<JsgfPsiFileStub> {

  public static final String ID_PREFIX = "jsgf.";

  public JsgfFileElementType() {
    super("JSGF_FILE", JsgfLanguage.INSTANCE);
  }

  @Override
  public @NonNls @NotNull String getExternalId() {
    return ID_PREFIX + super.getExternalId();
  }

  @Override
  public int getStubVersion() {
    return super.getStubVersion() + 1;
  }
}
