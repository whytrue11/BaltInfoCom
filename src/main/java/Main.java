import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Main {

  public static final String SEPARATOR = File.separator;

  private static final int ELEMENTS_COUNT_IN_ROW = 3;
  private static final String INPUT_FILE_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "lng.csv";
  private static final String OUTPUT_FILE_PATH = "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "out.csv";

  public static void main(String[] args) {

    long startTime = System.currentTimeMillis();
    List<Group> groups = null;
    try {
      groups = mainAlgorithm(INPUT_FILE_PATH);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return;
    }

    System.out.println("Group count: " + groups.size());

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH, false))) {
      for (int i = 0; i < groups.size(); i++) {
        writer.write("Group " + (i + 1) + "\n" + groups.get(i) + "\n");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    System.out.println("Run time: " + (System.currentTimeMillis() - startTime) / 1000);
  }

  public static List<Group> mainAlgorithm(String inputFilePath) throws IOException {

    List<List<String>> rowsList = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
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
      throw new IOException("Incorrect input");
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
          if (i == rowsList.size() - 1) {
            Group group = new Group(rowsList, startIndex, endIndex);
            if (group.size() != 1) {
              groups.get(column).add(group);
            }
          }
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

    for (int column = 0; column < ELEMENTS_COUNT_IN_ROW; column++) {
      for (int mergeGroupIndex = 0; mergeGroupIndex < groups.get(mergeColumn).size(); mergeGroupIndex++) {
        for (int otherGroupIndex = mergeGroupIndex + 1; otherGroupIndex < groups.get(mergeColumn).size(); otherGroupIndex++) {
          if (groups.get(mergeColumn).get(mergeGroupIndex).merge(groups.get(mergeColumn).get(otherGroupIndex), column)) {
            groups.get(mergeColumn).remove(otherGroupIndex);
          }
        }
      }
    }

    groups.get(mergeColumn).sort(Comparator.comparingInt(Group::size).reversed());

    return groups.get(mergeColumn);
  }
}
