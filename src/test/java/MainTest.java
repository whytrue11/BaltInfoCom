import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {
  @Test
  public void incorrectRows() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "incorrectRows.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 1;

    assertEquals(expected, actual);
  }

  @Test
  public void groupsHaveMoreThanOneElement() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "groupsHaveMoreThanOneElement.txt";
    try {
      Main.mainAlgorithm(inputFilePath).forEach(group -> assertTrue(group.size() > 1));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void unique1() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "unique1.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 0;

    assertEquals(expected, actual);
  }

  @Test
  public void unique2() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "unique2.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 1;

    assertEquals(expected, actual);
  }

  @Test
  public void mergeEmpty() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "mergeEmpty.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 0;

    assertEquals(expected, actual);
  }

  @Test
  public void merge1() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "merge1.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 1;

    assertEquals(expected, actual);
  }

  @Test
  public void merge2() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "merge2.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 1;

    assertEquals(expected, actual);
  }

  @Test
  public void merge3() {
    final String inputFilePath = "src" + Main.SEPARATOR + "test" + Main.SEPARATOR
        + "inputFiles" + Main.SEPARATOR + "merge3.txt";
    int actual = -1;
    try {
      actual = Main.mainAlgorithm(inputFilePath).size();
    } catch (IOException e) {
      e.printStackTrace();
    }
    int expected = 2;

    assertEquals(expected, actual);
  }
}