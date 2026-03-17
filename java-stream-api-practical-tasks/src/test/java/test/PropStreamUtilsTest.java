package test;

import com.example.Prop;
import com.example.PropStreamUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropStreamUtilsTest {
    @Test
    public void testGenerateProps(){
        int n = 5;
        List<Prop> props = PropStreamUtils.generateProps(n).toList();
        assertEquals(n, props.size(), "Stream should contain n elements");
        Assertions.assertTrue(
                IntStream.range(0, n)
                        .allMatch(idx -> props.get(idx).value() == idx)
        );
    }


    @Test
    public void removePropsWithDuplicateIdsTest() {
        UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID id2 = id1;
        List<Prop> props = PropStreamUtils.removePropsWithDuplicateIds(
                Stream.of(new Prop(null, "Prop1", 1),
                        new Prop(null, "Prop2", 2),
                        new Prop(id2, "Prop4", 4),
                        new Prop(id1, "Prop3", 3),
                        new Prop(UUID.randomUUID(), "Prop5", 5)));
        assertEquals(2, props.size());

        long uniqueIds = props.stream()
                .map(Prop::id)
                .distinct()
                .count();
        assertEquals(props.size(), uniqueIds);
    }


    @Test
    public void nameTotalValuesMapTest(){
        Map<String, Integer>map =  PropStreamUtils.nameTotalValuesMap(Stream.of(
                                new Prop(null, "NameProp1", 3),
                                new Prop(null, "NameProp1", 10),
                                new Prop(null, "NameProp2", 5),
                                new Prop(null, "NameProp2", 20),
                                new Prop(null, "NameProp3", 2),
                                new Prop(null, "NameProp4", 4)
                        ));
        assertEquals(4, map.size());
        assertEquals(13, map.get("NameProp1"));
        assertEquals(25, map.get("NameProp2"));
        assertEquals(2, map.get("NameProp3"));
        assertEquals(4, map.get("NameProp4"));
    }


    @Test
    public void sortPropsTest(){
        List<Prop>propList = PropStreamUtils.sortProps(
                Stream.of(
                                            new Prop(UUID.randomUUID(), "AProp1", 3),
                                            new Prop(UUID.randomUUID(), "ZProp2", 1),
                                            new Prop(UUID.randomUUID(), "BProp3", 2),
                                            new Prop(UUID.randomUUID(), "KProp4", 1),
                                            new Prop(UUID.randomUUID(), "KProp4", 1)
                                    ));
        assertEquals(5, propList.size());

        assertEquals("KProp4", propList.get(0).name());
        assertEquals(1, propList.get(0).value());

        assertEquals("KProp4", propList.get(1).name());
        assertEquals(1, propList.get(1).value());

        assertEquals("ZProp2", propList.get(2).name());
        assertEquals(1, propList.get(2).value());

        assertEquals("BProp3", propList.get(3).name());
        assertEquals(2, propList.get(3).value());

        assertEquals("AProp1", propList.get(4).name());
        assertEquals(3, propList.get(4).value());

    }


    @Test
    public void getFirstWordTest(){
        Stream<Character>resultStream = PropStreamUtils.getFirstWord(Stream.of('B', 'a', 'l', 'l', ' ', 'F', 'u', 'n'));
        String result = resultStream
                .map(String::valueOf)
                .collect(Collectors.joining());
        assertEquals("Ball", result);
    }


    @Test
    public void getNonNullPropertiesTest(){
        List<String>nonNullPropsList = PropStreamUtils.getNonNullProperties(Stream.of(
                new Prop(null, "Prop1", 1),
                new Prop(UUID.randomUUID(), null, 2),
                new Prop(UUID.randomUUID(), "Prop3", 0),
                new Prop(UUID.randomUUID(), "Prop4",4 )
        ))
                .toList();
        assertTrue(
                nonNullPropsList
                        .stream()
                        .noneMatch(Objects::isNull));
        assertTrue(nonNullPropsList.contains("Prop1"));
        assertTrue(nonNullPropsList.contains("Prop3"));
        assertTrue(nonNullPropsList.contains("Prop4"));
    }


    @Test
    public void foldTest(){
        Function<String,String> f1 = s -> s + "!";
        Function<String,String> f2 = String::toUpperCase;
        Function <String, String> f3 = s -> "[My lovely: " +  s + "]:)";

        String result = PropStreamUtils.fold(Stream.of(f1, f2, f3)).apply("Happy codding!");
        assertEquals(result,"[My lovely: HAPPY CODDING!!]:)" );
    }


    @Test
    public void toIdsTest(){
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();

        List<List<Prop>> listListProp = List.of(
                List.of(new Prop(id1, "Prop1", 1), new Prop(id2, "Prop2", 2)),
                List.of(new Prop(id3, "Prop3", 3), new Prop(id4, "Prop4", 4))
        );

        List<UUID>uuidList = PropStreamUtils.toIds(listListProp)
                .toList();
        assertEquals(4, uuidList.size());
        assertTrue(uuidList.contains(id1));
        assertTrue(uuidList.contains(id2));
        assertTrue(uuidList.contains(id3));
        assertTrue(uuidList.contains(id4));
    }


    @Test
    public void getNotNullNameProps() {
        List<Prop> propList = PropStreamUtils.getNotNullNameProps(
                Stream.of(
                        new Prop(null, "Prop1", 1),
                        new Prop(null, null, 2),
                        new Prop(null, "Prop3", 0),
                        new Prop(null, "Prop4", 4)
                ));
        assertEquals(3, propList.size());
        assertEquals("Prop1", propList.get(0).name());
        assertEquals("Prop3", propList.get(1).name());
        assertEquals("Prop4", propList.get(2).name());
    }


    @Test
    public void nameHighestLowestValueTest() {
        List<String> list = PropStreamUtils.nameHighestLowestValue(Stream.of(
                new Prop(null, "Prop1", 3),
                new Prop(null, "Prop2", 5),
                new Prop(null, "Prop3", 2),
                new Prop(null, "Prop4", 0)
        ));
        assertEquals(2, list.size());
        assertEquals("Prop4", list.get(0));
        assertEquals("Prop2", list.get(1));
    }


    @Test
    public void evenNumbersTest(){
        Integer number = PropStreamUtils.evenNumbers(Stream.of(1,5,8,12,180,45,7));
        assertEquals(3, number);
    }


    @Test
    public void defectNamesTest(){
        UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID id2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID id3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

        List<Prop> propObjects = Stream.of(
                new Prop(id1, "NameProp1", 1),
                new Prop(id1, "NameProp1", 2),
                new Prop(id2, "NameProp1", 3),
                new Prop(id3, "NameProp4", 4),
                new Prop(null, "NameProp1", 5)
        ).toList();

        Map<String, List<UUID>> defectNamesMap = PropStreamUtils.defectNames(propObjects);

        assertEquals(1, defectNamesMap.size());
        assertTrue(defectNamesMap.containsKey("NameProp1"));

        List<UUID> expectedIds = List.of(id1, id2);
        List<UUID> actualIds = defectNamesMap.get("NameProp1");

        assertTrue(actualIds.containsAll(expectedIds) && expectedIds.containsAll(actualIds));
    }


    @Test
    public void evenOddSumsMapTest(){
        Map<String, Integer> evenOddSumsMap = PropStreamUtils.evenOddSumsMap(
                Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        assertTrue(evenOddSumsMap.containsKey("Even"));
        assertTrue(evenOddSumsMap.containsKey("Odd"));

        assertEquals(30, evenOddSumsMap.get("Even"));
        assertEquals(25, evenOddSumsMap.get("Odd"));
    }


    @Test
    public void getNameWithHighestValueTest(){
        String nameHighestValue = PropStreamUtils.getNameWithHighestValue(
                Stream.of(
                                        new Prop(null, "Prop1", 3),
                                        new Prop(null, "Prop2", 5),
                                        new Prop(null, "Prop3", 2),
                                        new Prop(null, "Prop4", 4)
                                ));
        assertEquals("Prop2", nameHighestValue, "The name should match the Prop with the highest value");
    }


    @Test
    public void testPartitioningCollectorNumbers() {
        String result = Stream.of(1, null, 2, 3, null, 4, 5, 6)
                .collect(PropStreamUtils.partitioningCollector(
                        elem -> elem != null,
                        Collectors.summarizingInt(Integer::intValue),
                        Collectors.counting(),
                        (sum, numberNulls) -> "sum = " + sum.getSum() + ", nulls = " + numberNulls
                ));

        assertEquals("sum = 21, nulls = 2", result);
    }


    @Test
    public void testPartitioningCollectorStrings() {
        String result = Stream.of("one", "two_valid", "three", "four_valid", null, "five", null, "eight_valid")
                .collect(PropStreamUtils.partitioningCollector(
                        elem -> elem != null && elem.contains("valid"),
                        Collectors.joining(", "),
                        Collectors.counting(),
                        (validStr, invalidCount) -> "valid = " + validStr + ", invalid count = " + invalidCount
                ));

        assertEquals("valid = two_valid, four_valid, eight_valid, invalid count = 5", result);
    }

    @Test
    public void testPartitioningCollectorWithGroupMap() {
        String result = Stream.of("one", "two_valid", "three", "four_valid", null, "five", null, "eight_valid")
                .collect(PropStreamUtils.partitioningCollector(
                        elem -> elem != null && elem.contains("valid"),
                        Collectors.joining(", "),
                        Collectors.groupingBy(Objects::nonNull, Collectors.counting()),
                        (validStr, invaliStr) -> "valid = " + validStr + ", invalid count = " + invaliStr
                ));

        assertEquals("valid = two_valid, four_valid, eight_valid, invalid count = {false=2, true=3}", result);
    }
}