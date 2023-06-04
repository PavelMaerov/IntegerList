import org.example.IntegerList;
import org.example.IntegerListImpl;
import org.example.ItemNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IntegerListImplTest {
    IntegerList out;
    @BeforeEach
    void createIntegerList() {
        out = new IntegerListImpl(1);
        out.add(11);
        out.add(22);
        out.add(33);
        out.add(-1);
        out.add(0);
        out.add(-123);
    }
    @Test
    void addTest() {
        Integer returnValue=out.add(123);
        assertEquals(123,returnValue);
        Integer[] expected = {11,22,33,-1,0,-123,123};
        assertArrayEquals(expected,out.toArray());
        assertEquals(7,out.size());
        assertThrows(NullPointerException.class, ()->out.add(null));
    }

    @Test
    void addByIndexTest() {
        Integer resultValue = out.add(0,1000);
        assertEquals(1000,resultValue);
        out.add(6,6000);
        out.add(7,7000); //приводит к расширению массива с 8 до 16 элементов
        Integer[] expected = {1000,11,22,33,-1,0,6000,7000,-123};
        assertArrayEquals(expected,out.toArray());
        assertEquals(9,out.size());
        assertThrows(NullPointerException.class, ()->out.add(0,null));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.add(-1,1234));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.add(9,1234));
    }

    @Test
    void setTest() {
        Integer resultValue = out.set(1,12345);
        assertEquals(22,resultValue);
        Integer[] expected = {11,12345,33,-1,0,-123};
        assertArrayEquals(expected,out.toArray());
        assertThrows(NullPointerException.class, ()->out.set(0,null));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.set(-1,1234));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.set(9,1234));

        out.clear();
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.set(0,1234));  // в пустом списке set не работает
    }

    @Test
    void removeTest() {
        Integer resultValue = out.remove(22);
        assertEquals(22,resultValue);
        Integer[] expected = {11,33,-1,0,-123};
        assertArrayEquals(expected,out.toArray());
        assertEquals(5,out.size());
        assertThrows(NullPointerException.class, ()->out.remove(null));
        assertThrows(ItemNotFound.class, ()->out.remove(1234));
    }

    @org.junit.jupiter.api.Test
    void removeByIndexTest() {
        Integer resultValue = out.removeByIndex(0);
        assertEquals(11,resultValue);
        out.removeByIndex(4);
        Integer[] expected = {22,33,-1,0};
        assertArrayEquals(expected,out.toArray());
        assertEquals(4,out.size());
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.removeByIndex(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.removeByIndex(4));
    }

    @Test
    void containsTest() {
        assertTrue(out.contains(22));
        assertFalse(out.contains(321));
        assertFalse(out.contains(null));

        for (Integer i : out.toArray()) {
            assertTrue(out.contains(i));
        }

    }

    @Test
    void indexOfTest() {
        out.add(0);
        assertEquals(1,out.indexOf(22));
        assertEquals(4,out.indexOf(0));
        assertEquals(-1,out.indexOf(321));
        assertEquals(-1, out.indexOf(null));
    }

    @Test
    void lastIndexOfTest() {
        out.add(0); //в седьмую позицию, индекс 6
        //System.out.println(out);
        assertEquals(1,out.lastIndexOf(22));
        assertEquals(6,out.lastIndexOf(0));
        assertEquals(-1,out.lastIndexOf(321));
        assertEquals(-1, out.lastIndexOf(null));
    }

    @Test
    void get() {
        assertEquals(22,out.get(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, ()->out.get(6));
    }

    @Test
    void equalsTest() {
        assertTrue(out.equals(out));          //вызывается мой метод
        assertTrue(out.equals((Object)out));  //вызывается метод для Object

        IntegerList other = new IntegerListImpl(15);  //специально с другим размером
        Arrays.stream(out.toArray()).forEach(e->other.add(e)); //наполняем other из out
        assertTrue(out.equals(other));          //вызывается мой метод
        assertFalse(out.equals((Object)other)); //так вызывается метод для Object

        other.set(0,1234);
        assertFalse(out.equals(other));

        assertThrows(NullPointerException.class,()->out.equals(null));  //так вызывается мой метод
        assertFalse(out.equals((Object)null));  //так вызывается метод для Object
    }

    @Test
    void sizeTest() {
        assertEquals(6,out.size());
        out.add(0);
        assertEquals(7,out.size());
        out.remove(0);
        assertEquals(6,out.size());
        out.clear();
        assertEquals(0,out.size());
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
        assertFalse(out.isEmpty());
        IntStream.range(0, out.size()).forEach((e)->out.removeByIndex(0));
        assertTrue(out.isEmpty());

        out = new IntegerListImpl(100);
        assertTrue(out.isEmpty());
    }

    @Test
    void clearTest() {
        out.clear();
        assertTrue(out.isEmpty());
        assertEquals(0,out.size());
    }

    @Test
    void toArrayTest() {
        Integer[] expected = {11,22,33,-1,0,-123};
        assertArrayEquals(expected,out.toArray());

        out.clear();  //сравним пустые массивы
        Integer[] expected2 = {};
        assertArrayEquals(expected2,out.toArray());
    }

    @Test
    void toStringTest() {
        out.add(0);
        assertEquals("[11, 22, 33, -1, 0, -123, 0, null, null]",out.toString());
    }

    Integer[] expectedForSortTest = {-123,-1,0,11,22,33};
    @Test
    void sortBubbleTest() {
        out.sortBubble();
        assertArrayEquals(expectedForSortTest,out.toArray());
    }
    @Test
    void sortSelectionTest() {
        out.sortSelection();
        assertArrayEquals(expectedForSortTest,out.toArray());
    }
    @Test
    void sortInsertionTest() {
        out.sortInsertion();
        assertArrayEquals(expectedForSortTest,out.toArray());
    }
    @Test
    void sortMergeTest() {
        out.sortMerge();
        assertArrayEquals(expectedForSortTest,out.toArray());
    }
    @Test
    void sortQuickTest() {
        out.sortQuick();
        assertArrayEquals(expectedForSortTest,out.toArray());
    }

}