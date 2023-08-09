import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Usage: java SortFiles [-a|-d] [-s|-i] <output_file> <input_files...>");
            return;
        }
        List<String> list = Arrays.stream(args).collect(Collectors.toList());

        if (!args[0].equals("-a") && !args[0].equals("-d")) {
            list.add(0,"-a");
        }

        String sortMode = list.get(0); // Режим сортировки: -a или -d
        String dataType =list.get(1); // Тип данных: -s или -i
        String outputFileName = list.get(2); // Имя выходного файла

        List<String> inputFiles = getInputFiles(list); // Получение списка входных файлов
        if (inputFiles.isEmpty()) { // Проверка наличия входных файлов
            System.out.println("No input files provided.");
            return;
        }

        List<Comparable> mergedData = readInputData(inputFiles, dataType); // Чтение данных из входных файлов
        if (mergedData.isEmpty()) {  // Проверка успешного считывания файлов
            System.out.println("No input files could be read.");
            return;
        }

        List<Comparable> sortedData = merge_sort(mergedData); // Сортировка данных

        writeOutputData(sortedData, outputFileName, sortMode); // Запись отсортированных данных в выходной файл
    }

    private static List<String> getInputFiles(List<String> args) {
        List<String> inputFiles = new ArrayList<>();
        for (int i = 3; i < args.size(); i++) {
            inputFiles.add(args.get(i));
        }
        return inputFiles;
    }

    private static List<Comparable> readInputData(List<String> inputFiles, String dataType) {
        List<Comparable> mergedData = new ArrayList<>(); // Содержит данные входных файлов
        for (String inputFile : inputFiles) {
            try (Scanner scanner = new Scanner(new File(inputFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty() && !line.contains(" ")) {
                        try {
                            if (dataType.equals("-i")) {    // Проверка типа данных
                                mergedData.add(Integer.parseInt(line));
                            } else if (dataType.equals("-s")) {
                                mergedData.add(line);
                            }
                        } catch (Exception e) { // Случай, когда аргумент: -i, а входные файлы: -s или наоборот
                            System.out.println("Error reading input file: " + inputFile + ", invalid data found");
                        }
                    } else {
                        System.out.println("Error reading input file: " + inputFile);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading input file: " + inputFile);
            }
        }
        return mergedData;
    }

    public static List<Comparable> merge_sort(List<Comparable> list) {
        if (list.size() == 1) {
            return list;
        }
        int middle = list.size() / 2;
//        System.out.println("######### Вызов левой части рекурсии ##################");
//        System.out.println("left: " + list.subList(0, middle));
//        System.out.println("right: " + list.subList(middle, list.size()));

        List<Comparable> left = merge_sort(list.subList(0, middle));
//        System.out.println("######### Вызов правой части рекурсии ##################");
        List<Comparable> right = merge_sort(list.subList(middle, list.size()));

//        System.out.println("left: " + left);
//        System.out.println("right: " + right);

        return merge_two_list(left, right);
    }

    private static List<Comparable> merge_two_list(List<Comparable> left, List<Comparable> right) {
//        System.out.println("Функция merge_two_list");
//        System.out.println("left: " + left);
//        System.out.println("right: " + right);
        List<Comparable> sort_list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {

            Comparable leftElement = left.get(i);
            Comparable rightElement = right.get(j);

            if (leftElement.compareTo(rightElement) < 0) {
                sort_list.add(leftElement);
                i++;
            } else {
                sort_list.add(rightElement);
                j++;
            }
        }

        // Добавляем оставшиеся элементы из левой и правой половины, если они есть
        if (i < left.size()) {
            sort_list.addAll(left.subList(i, left.size()));
        } else if (j < right.size()) {
            sort_list.addAll(right.subList(j, right.size()));
        }
//        System.out.println("Конец функции merge_two_list");
        return sort_list;
    }

    private static void writeOutputData(List<Comparable> sortedData, String outputFileName, String sortMode) {
        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            if (sortMode.equals("-d")) { // Сортировка по убыванию
                for (int i = sortedData.size() - 1; i >= 0; i--) {
                    writer.println(sortedData.get(i).toString());
                }
            } else {
                for (Comparable element : sortedData) {
                    writer.println(element.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error writing to output file: " + outputFileName);
        }
    }
}
