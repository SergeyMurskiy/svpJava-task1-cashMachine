package spbu.murskiy;

import java.util.*;

/**
 * Created by Sergey Murskiy on 16.10.2018.
 */
public class CashMachine {
    public static ArrayList<Integer> newCombination;
    public static int[] arrayOfNumbersInclusion;
    public static int[] maxInclusion;

    public static boolean rise(int index) {
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

    public static ArrayList<Integer> addCombination(ArrayList<Integer> exchange) {
        newCombination = new ArrayList<Integer>();
        for (int i = arrayOfNumbersInclusion.length - 1; i >=0; i--) {
            for (int j = 0; j < arrayOfNumbersInclusion[i]; j++) {
                newCombination.add(exchange.get(i));
            }
        }
        return newCombination;
    }

    public static void checkValue(int value) {
        if (value <= 0) {
            throw new InputMismatchException();
        }
    }

    public static ArrayList<ArrayList> cashMachine(int banknote, ArrayList<Integer> exchange) {
        ArrayList<ArrayList> result = new ArrayList<ArrayList>();
        ArrayList<Integer> newCombination = new ArrayList<Integer>();
        Collections.sort(exchange);
        if (exchange.get(0) > banknote) {
            return null;
        }
        if (exchange.get(0) == exchange.get(exchange.size() - 1)) {
            for (int i = 0; i < banknote / exchange.get(0); i++) {
                newCombination.add(exchange.get(0));
            }
            result.add(newCombination);
            return result;
        }
        while (exchange.get(exchange.size() - 1) >= banknote) {
            if (exchange.get(exchange.size() - 1) == banknote) {
                newCombination.clear();
                newCombination.add(exchange.get(exchange.size() - 1));
            }
            exchange.remove(exchange.size() - 1);
        }
        arrayOfNumbersInclusion = new int[exchange.size()];
        maxInclusion = new int[exchange.size()];
        for (int i = 0; i < exchange.size(); i++) {
            maxInclusion[i] = banknote / exchange.get(i);
        }
        int sum;
        boolean isChange;
        boolean isEnd = false;
        while(!isEnd) {
            sum = 0;
            isChange = false;
            for (int i = 0; i < exchange.size(); i++) {
                sum += arrayOfNumbersInclusion[i] * exchange.get(i);
                if (sum == banknote) {
                    result.add(addCombination(exchange));
                    isEnd = rise(i);
                    isChange = true;
                    break;
                }
                if (i < exchange.size() - 1 && banknote - sum < arrayOfNumbersInclusion[i+1] * exchange.get(i+1)) {
                    isEnd = rise(i+1);
                    isChange = true;
                    break;
                }
            }
            if (!isChange) {
                isEnd = rise(arrayOfNumbersInclusion.length);
            }
        }
        return result;
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Введите купюру");
        int banknote = 0;
        try {
            banknote = input.nextInt();
            checkValue(banknote);
        } catch (InputMismatchException e) {
            System.out.println("Введено не допустимое значение");
            System.exit(-1);
        }
        int size = 0;
        System.out.println("Введите число купюр размена");
        try {
            size = input.nextInt();
            checkValue(size);
        } catch (InputMismatchException e) {
            System.out.println("Введено не допустимое значение");
            System.exit(-1);
        }
        ArrayList<Integer> exchange = new ArrayList<Integer>();
        System.out.println("Введите купюры размена");
        try {
            for (int i = 0; i < size; i++) {
                exchange.add(input.nextInt());
                checkValue(exchange.get(i));
            }
        } catch (InputMismatchException e) {
            System.out.println("Введено недопустимое значение");
            System.exit(-1);
        }

        ArrayList<ArrayList> result = cashMachine(banknote, exchange);
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
}
