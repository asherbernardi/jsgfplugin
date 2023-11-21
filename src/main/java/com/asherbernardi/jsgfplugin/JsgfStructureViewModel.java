package com.asherbernardi.jsgfplugin;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import org.jetbrains.annotations.NotNull;

public class JsgfStructureViewModel extends StructureViewModelBase implements
    StructureViewModel.ElementInfoProvider {
  public JsgfStructureViewModel(PsiFile psiFile) {
    super(psiFile, new JsgfStructureViewElement(psiFile));
  }

  @NotNull
  public Sorter[] getSorters() {
    return new Sorter[]{Sorter.ALPHA_SORTER};
  }

  // Since rules don't contain substructure
  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
    return false;
  }

  // All elements are leafs
  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement element) {
    return element instanceof JsgfFile;
  }
}