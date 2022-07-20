import java.util.ArrayList;
import java.util.List;

public class Group {
  private final List<List<String>> group;

  private void addUniqueInGroup(List<List<String>> uncheckedGroup) {
    for (List<String> uncheckedGroupRow : uncheckedGroup) {
      boolean equal = false;
      for (List<String> checkedGroupRow : group) {
        if (uncheckedGroupRow.equals(checkedGroupRow)) {
          equal = true;
          break;
        }
      }
      if (!equal) {
        group.add(uncheckedGroupRow);
      }
    }
  }

  public Group(List<List<String>> rowsList, int startIndex, int endIndex) {
    this.group = new ArrayList<>(endIndex - startIndex);
    List<List<String>> uncheckedGroup = rowsList.subList(startIndex, endIndex + 1);
    addUniqueInGroup(uncheckedGroup);
  }

  public int size() {
    return this.group.size();
  }

  public boolean merge(Group b, int column) {
    for (int i = 0; i < this.size(); i++) {
      for (int j = 0; j < b.size(); j++) {
        if (!this.group.get(i).get(column).isEmpty() && !b.group.get(j).get(column).isEmpty()
            && this.group.get(i).get(column).equals(b.group.get(j).get(column))) {
          addUniqueInGroup(b.group);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    group.forEach(row -> {
      row.forEach(string -> stringBuilder.append("\"").append(string).append("\";"));
      stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
      stringBuilder.append("\n");
    });
    return stringBuilder.toString();
  }
}
