import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Main {

  private static class Group {
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
          if (this.group.get(i).get(column).equals(b.group.get(j).get(column))) {
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

  public static void main(String[] args) {
    final int ELEMENTS_COUNT_IN_ROW = 3;
    final String SEPARATOR = File.separator;
    final String INPUT_FILE_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "lng.csv";
    final String OUTPUT_FILE_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "out.csv";

    long startTime = System.currentTimeMillis();

    List<List<String>> rowsList = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
      String line = reader.readLine();

      while (line != null) {
        if (!Pattern.matches("^\"([0-9]*\";\")+[0-9]*\"$", line)) {
          line = reader.readLine();
          continue;
        }

        List<String> row = Arrays.asList(line.split("\";\""));
        if (row.size() != ELEMENTS_COUNT_IN_ROW) {
          line = reader.readLine();
          continue;
        }
        int lastIndex = row.size() - 1;
        row.set(0, row.get(0).substring(1));
        row.set(lastIndex, row.get(lastIndex).substring(0, row.get(lastIndex).length() - 1));

        rowsList.add(row);

        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("Incorrect input");
    }

    if (rowsList.isEmpty()) {
      System.out.println("Incorrect input");
      return;
    }

    List<List<Group>> groups = new ArrayList<>(ELEMENTS_COUNT_IN_ROW);
    for (int column = 0; column < ELEMENTS_COUNT_IN_ROW; column++) {
      int finalColumn = column;
      rowsList.sort(Comparator.comparing(str -> str.get(finalColumn)));

      groups.add(new ArrayList<>());

      String startGroupString = rowsList.get(0).get(column);
      int startIndex = 0;
      int endIndex = 0;
      for (int i = 1; i < rowsList.size(); i++) {
        String otherString = rowsList.get(i).get(column);
        if (!startGroupString.isEmpty() && !otherString.isEmpty()
            && startGroupString.equals(otherString)) {
          endIndex = i;
        } else {
          if (endIndex - startIndex != 0) {
            Group group = new Group(rowsList, startIndex, endIndex);
            if (group.size() != 1) {
              groups.get(column).add(group);
            }
          }
          startGroupString = otherString;
          startIndex = i;
          endIndex = startIndex;
        }
      }
    }

    int mergeColumn = 0;
    for (int column = 1; column < ELEMENTS_COUNT_IN_ROW; column++) {
      for (int mergeGroupIndex = 0; mergeGroupIndex < groups.get(mergeColumn).size(); mergeGroupIndex++) {
        for (int otherGroupIndex = 0; otherGroupIndex < groups.get(column).size(); otherGroupIndex++) {
          if (groups.get(mergeColumn).get(mergeGroupIndex).merge(groups.get(column).get(otherGroupIndex), column)) {
            groups.get(column).remove(otherGroupIndex);
          }
        }
      }
      groups.get(mergeColumn).addAll(groups.get(column));
    }

    System.out.println("Group count: " + groups.get(mergeColumn).size());

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH, false))) {
      for (int i = 0; i < groups.get(mergeColumn).size(); i++) {
        writer.write("Group " + (i + 1) + "\n" + groups.get(mergeColumn).get(i) + "\n");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    System.out.println("Run time: " + (System.currentTimeMillis() - startTime) / 1000);
  }
}
