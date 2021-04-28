package com.asherbernardi.jsgfplugin;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Allows for project-wide searching of rule names
 * @author asherbernardi
 */
public class JsgfChooseByNameContributor implements ChooseByNameContributor {
  @NotNull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<RuleName> rules = JsgfUtil.findAllRules(project);
    List<String> names = new ArrayList<>(rules.size());
    for (RuleName rule : rules) {
      String name = rule.getRuleName();
      if (name != null && name.length() > 0) {
        names.add(name);
      }
    }
    return names.toArray(new String[0]);
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    List<RuleName> rules = JsgfUtil.findAllRules(project, name);
    return rules.toArray(new NavigationItem[0]);
  }
}