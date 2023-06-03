package org.example;

import java.util.Arrays;

public class IntegerListImpl implements IntegerList{
    private int size;   //размер массива, заполненный данными, без свободного места в конце
    private int limitSize; //размер массива array, включая свободное место в конце
    private Integer[] array;  //массив для хранения строк

    public IntegerListImpl(int limitSize) {  //конструктор
        if (limitSize<=0) limitSize=1;
        size=0;  //необязательно
        this.limitSize = limitSize;
        array=new Integer[limitSize];
    }
    public IntegerListImpl(Integer[] source) {
        //конструктор, будет использован во второй домашке, для создания клона для сортировки
        size=source.length;
        this.array = source;
    }
    //Служебная процедура. Вызывается при добавлении элемента.
    //Проверяет наличие свободного места и при необходимости увеличивает массив
    private void checkSize() {
        if (size == limitSize) {
            //если не хватает места в массиве - удваиваем его
            //для простоты, чтобы не придумывать коэффициентов расширения
            limitSize = 2*limitSize;
            array = Arrays.copyOf(array, limitSize);  //копирование старого массива в новый больший
        }
    }
    //Служебная процедура. Вызывается для проверки переданного индекса.
    //Проверяет, что индекс находится в пределах допустимого диапазона
    //Если нет, то выбрасывает исключение
    private void checkIndex(int index) {
        if (index>=size || index<0) throw new ArrayIndexOutOfBoundsException();
    }
    @Override
    public Integer add(Integer item) {
        if (item==null) throw new NullPointerException();
        checkSize();
        array[size++] = item; //добавление в конец по индексу size
        return item;
    }

    @Override
    public Integer add(int index, Integer item) {
        if (item==null) throw new NullPointerException();
        checkIndex(index);
        checkSize();
        //сдвиг участка после вставляемого элемента вправо
        System.arraycopy(array, index, array, index+1, size-index);
        array[index] = item;  //замена элемента по индексу size
        size++;
        return item;
    }

    @Override
    public Integer set(int index, Integer item) {
        if (item==null) throw new NullPointerException();
        checkIndex(index);
        Integer oldValue = array[index]; //запоминаем старое значение. В задании это не задано
        array[index] = item;
        return oldValue;
    }

    @Override
    public Integer remove(Integer item) {
        if (item==null) throw new NullPointerException();
        int index = indexOf(item); //определяем индекс удаляемого элемента
        if (index==-1) throw new ItemNotFound(); //самодельное RunTime исключение
        return removeByIndex(index);  //удалить по найденному индексу
    }

    @Override
    public Integer removeByIndex(int index) {
        checkIndex(index);
        Integer removedItem = array[index];  //запомним удаляемый элемент
        //сдвиг участка после удаляемого элемента влево
        System.arraycopy(array, index+1, array, index, size-index-1);
        array[size-1] = null; //очистка последнего элемента
        size--;
        return removedItem;
    }

    @Override
    public boolean contains(Integer item) {
        //реализация первого урока- return indexOf(item) != -1; //определяем индекс искомого элемента. Если не -1, значит найден
        //для реализации по заданию второго урока - создаем клон
        IntegerListImpl copy = new IntegerListImpl(toArray());
        copy.sortInsertion();
        return copy.binarySearch(item);
    }

    @Override
    public int indexOf(Integer item) {
        //--------------- вариант заимствования метода у List--------
        //в Arrays нет indexOf, но есть в List
        //Поведение метода List точно соответствует требуемому от IntegerList
        //int index = Arrays.asList(array).indexOf(item);
        //if (index>=size) index=-1; //если параметр null, то можем найти null на свободном месте массива
        //return index;
        //---------------------самодельный метод---------------------
        for (int i=0; i<size; i++) {
            if (array[i].equals(item)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Integer item) {
        int index = Arrays.asList(array).lastIndexOf(item);
        if (index>=size) index=-1; //если параметр null, то можем найти null на свободном месте массива
        return index;
    }

    @Override
    public Integer get(int index) {
        checkIndex(index);
        return array[index];
    }

    @Override
    //этот метод не переопределяет Object.equals, т.к. имеет другую сигнатуру
    public boolean equals(IntegerList otherList) {
        //array.equals() сравнивает ссылки, пользоваться им нельзя
        //Если передан параметр = null, то ничего делать не надо. otherList.toArray() вызовет NullPointerException
        //на сравнение подаем не полный array, а только его значимую часть
        return Arrays.equals(toArray(), otherList.toArray());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public void clear() {
        size=0;
        //делать shrink не задали, поэтому массив останется большим. Да и исходный limitSize мы уже потеряли
        array=new Integer[limitSize];
    }

    @Override
    public Integer[] toArray() {
        //return array.clone(); возвратит с nullами на свободном конце
        return Arrays.copyOf(array, size);
    }
    //toString заданием не определено. Вывожу полный массив со свободным концом
    public String toString() {
        return Arrays.toString(array);
    }

    //Далее из шпаргалки второго урока
    public void swapElements(int indexA, int indexB) {
        Integer tmp = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = tmp;
    }
    public void sortBubble() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    swapElements(j, j + 1);
                }
            }
        }
    }
    public void sortSelection() {
        for (int i = 0; i < size - 1; i++) {
            int minElementIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (array[j] < array[minElementIndex]) {
                    minElementIndex = j;
                }
            }
            swapElements(i, minElementIndex);
        }
    }
    public void sortInsertion() {
        for (int i = 1; i < size; i++) {
            Integer temp = array[i];
            int j = i;
            while (j > 0 && array[j - 1] >= temp) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = temp;
        }
    }
    public boolean binarySearch(Integer element) {  //работает, только если массив отсортирован
        if (element==null) return false;

        int min = 0;
        int max = size - 1;

        while (min <= max) {
            int mid = (min + max) / 2;

            if (element.equals(array[mid])) {
                return true;
            }

            if (element < array[mid]) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return false;
    }
}
