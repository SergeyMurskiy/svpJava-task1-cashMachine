package spbu.murskiy;

import java.util.*;

/**
 * Created by Sergey Murskiy on 16.10.2018.
 */

public class CashMachine {
    private static ArrayList<Integer> newCombination = new ArrayList<Integer>();
    private static int[] arrayOfNumbersInclusion;
    private static int[] maxInclusion;

    private static boolean nextCombination(int index) {
        if (index == 0 && arrayOfNumbersInclusion[0] == maxInclusion[0]) {
            return true;
        }
        int tmp = 1;
        while (true) {
            if (index - tmp == 0 && arrayOfNumbersInclusion[0] == maxInclusion[0]) {
                return true;
            }
            if (arrayOfNumbersInclusion[index - tmp] + 1 <= maxInclusion[index - tmp]) {
                arrayOfNumbersInclusion[index - tmp]++;
                for (int i = index - tmp + 1; i < arrayOfNumbersInclusion.length; i++) {
                    arrayOfNumbersInclusion[i] = 0;
                }
                break;
            }
            tmp++;
        }
        return false;
    }

    private static ArrayList<Integer> addCombination(int[] exchange) {
        newCombination = new ArrayList<Integer>();
        for (int i = arrayOfNumbersInclusion.length - 1; i >=0; i--) {
            for (int j = 0; j < arrayOfNumbersInclusion[i]; j++) {
                newCombination.add(exchange[i]);
            }
        }
        return newCombination;
    }

    private static ArrayList<Integer> checkTrivialCombination(int banknote, int[] exchange) {
        newCombination = new ArrayList<Integer>();
        if (exchange[0] == exchange[exchange.length - 1]) {
            for (int i = 0; i < banknote / exchange[0]; i++) {
                newCombination.add(exchange[0]);
            }
            return newCombination;
        }
        return null;
    }

    private static void findMaxInclusion(int banknote, int[] exchange) {
        for (int i = 0; i < exchange.length; i++) {
            maxInclusion[i] = banknote / exchange[i];
        }
    }

    private static ArrayList<ArrayList<Integer>> findCombinations(int banknote, int[] exchange) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        int sum;
        boolean isChange;
        boolean isEnd = false;
        while(!isEnd) {
            sum = 0;
            isChange = false;
            for (int i = 0; i < exchange.length; i++) {
                sum += arrayOfNumbersInclusion[i] * exchange[i];
                if (sum == banknote) {
                    result.add(addCombination(exchange));
                    isEnd = nextCombination(i);
                    isChange = true;
                    break;
                }
                if (i < exchange.length - 1 && banknote - sum < arrayOfNumbersInclusion[i+1] * exchange[i+1]) {
                    isEnd = nextCombination(i+1);
                    isChange = true;
                    break;
                }
            }
            if (!isChange) {
                isEnd = nextCombination(arrayOfNumbersInclusion.length); }
        }
        return result;
    }

    public static ArrayList<ArrayList<Integer>> exchangeSumToCoins(int banknote, int[] exchange) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        Arrays.sort(exchange);
        ArrayList<Integer> trivialCombination = checkTrivialCombination(banknote, exchange);
        if (trivialCombination != null) {
            result.add(trivialCombination);
            return result;
        }
        arrayOfNumbersInclusion = new int[exchange.length];
        maxInclusion = new int[exchange.length];
        findMaxInclusion(banknote, exchange);
        return findCombinations(banknote, exchange);
    }

    public static void checkInputValue(int value) {
        if (value <= 0) {
            throw new InputMismatchException();
        }
    }

    public static void printResult(ArrayList<ArrayList<Integer>> result) {
        if (result != null) {
            System.out.println("Найдено " + result.size() + " комбинаций");
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < result.get(i).size(); j++) {
                    System.out.print(result.get(i).get(j) + " ");
                }
                System.out.println("");
            }
        } else {
            System.out.println("Найдено 0 комбинаций");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Введите купюру");
        int banknote = 0;
        try {
            banknote = input.nextInt();
            checkInputValue(banknote);
        } catch (InputMismatchException e) {
            System.out.println("Введено не допустимое значение");
            System.exit(-1);
        }
        int size = 0;
        System.out.println("Введите число купюр размена");
        try {
            size = input.nextInt();
            checkInputValue(size);
        } catch (InputMismatchException e) {
            System.out.println("Введено не допустимое значение");
            System.exit(-1);
        }
        int[] exchange = new int[size];
        System.out.println("Введите купюры размена");
        try {
            for (int i = 0; i < size; i++) {
                exchange[i] = input.nextInt();
                checkInputValue(exchange[i]);
            }
        } catch (InputMismatchException e) {
            System.out.println("Введено недопустимое значение");
            System.exit(-1);
        }
        printResult(exchangeSumToCoins(banknote, exchange));
    }
}
