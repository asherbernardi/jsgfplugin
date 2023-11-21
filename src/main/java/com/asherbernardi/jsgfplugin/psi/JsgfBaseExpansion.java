package com.asherbernardi.jsgfplugin.psi;

public interface JsgfBaseExpansion extends JsgfExpansion {

  default boolean isTerminal() {
    return this instanceof JsgfTerminalExp;
  }

  default boolean isString() {
    return this instanceof JsgfStringExp;
  }

  default boolean isRuleReference() {
    return this instanceof JsgfRuleReferenceExp;
  }

}
