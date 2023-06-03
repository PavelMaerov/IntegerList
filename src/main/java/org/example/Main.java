package org.example;

import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        IntegerList out1 = new IntegerListImpl(100000);
        Random random = new Random(1);
        //size еще равен 0
        IntStream.range(0, 100000).forEach(e -> out1.add(random.nextInt()));

        IntegerList out2 = new IntegerListImpl(out1.toArray().clone());
        IntegerList out3 = new IntegerListImpl(out1.toArray().clone());

        long start = System.currentTimeMillis();
        out1.sortBubble();
        System.out.println("Сортировка пузырьком: " + (System.currentTimeMillis() - start));
        //Сортировка пузырьком seed=1: 55349,54385
        //Сортировка пузырьком seed=10: 53815,53751
        //Сортировка пузырьком seed=100: 52843,54305

        start = System.currentTimeMillis();
        out2.sortSelection();
        System.out.println("Сортировка выбором: " + (System.currentTimeMillis() - start));
        //Сортировка выбором seed=1: 13309, 12768
        //Сортировка выбором seed=10: 12624, 12561
        //Сортировка выбором seed=100: 12743, 12758

        start = System.currentTimeMillis();
        out3.sortInsertion();
        System.out.println("Сортировка вставкой: " + (System.currentTimeMillis() - start));
        //Сортировка вставкой seed=1: 8907, 9563
        //Сортировка вставкой seed=10: 9250, 9529
        //Сортировка вставкой seed=100: 9346, 9371

        //сортировка вставкой - самая быстрая
    }
}