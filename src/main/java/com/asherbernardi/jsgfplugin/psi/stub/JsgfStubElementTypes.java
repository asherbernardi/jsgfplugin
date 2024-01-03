package com.asherbernardi.jsgfplugin.psi.stub;

import com.asherbernardi.jsgfplugin.psi.JsgfFileElementType;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;

/**
 * Some static types which can be easily referenced.
 * @author asherbernardi
 */
public interface JsgfStubElementTypes {

  JsgfFileElementType FILE_ELEMENT_TYPE = new JsgfFileElementType();

  RuleDeclarationStubElementType RULE_DECLARATION_STUB_TYPE = new RuleDeclarationStubElementType();
  GrammarNameStubElementType GRAMMAR_NAME_STUB_TYPE = new GrammarNameStubElementType();
  ImportStubElementType IMPORT_STUB_TYPE = new ImportStubElementType();

  StubIndexKey<String, JsgfRuleDeclarationName> RULE_INDEX_KEY =
      StubIndexKey.createIndexKey(RULE_DECLARATION_STUB_TYPE.getExternalId());
  StubIndexKey<String, JsgfGrammarName> GRAMMAR_INDEX_KEY =
      StubIndexKey.createIndexKey(GRAMMAR_NAME_STUB_TYPE.getExternalId());
  StubIndexKey<String, JsgfRuleImportName> IMPORT_INDEX_KEY =
      StubIndexKey.createIndexKey(IMPORT_STUB_TYPE.getExternalId());

}
