package utiltest;

import com.oceanview.resort.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for JsonUtil.
 * Covers failure scenarios: null values, edge cases, and special character handling.
 */
public class JsonUtilTest {

    /**
     * TDD failure test 1: toJson with null returns "null" string.
     */
    @Test
    public void testToJson_WithNull_ReturnsNullString() {
        String result = JsonUtil.toJson(null);

        assertEquals("null", result, "toJson with null should return 'null' string");
    }

    /**
     * TDD failure test 2: quote with null returns "null" string.
     */
    @Test
    public void testQuote_WithNull_ReturnsNullString() {
        String result = JsonUtil.quote(null);

        assertEquals("null", result, "quote with null should return 'null' string");
    }

    /**
     * TDD failure test 3: toJson with empty map returns empty JSON object.
     */
    @Test
    public void testToJson_WithEmptyMap_ReturnsEmptyJsonObject() {
        Map<String, Object> map = new HashMap<>();

        String result = JsonUtil.toJson(map);

        assertEquals("{}", result, "toJson with empty map should return empty JSON object");
    }

    /**
     * TDD failure test 4: toJson with empty list returns empty JSON array.
     */
    @Test
    public void testToJson_WithEmptyList_ReturnsEmptyJsonArray() {
        List<Object> list = new ArrayList<>();

        String result = JsonUtil.toJson(list);

        assertEquals("[]", result, "toJson with empty list should return empty JSON array");
    }

    /**
     * TDD failure test 5: quote with string containing backslash escapes it.
     */
    @Test
    public void testQuote_WithBackslash_EscapesBackslash() {
        String result = JsonUtil.quote("path\\to\\file");

        assertEquals("\"path\\\\to\\\\file\"", result, "quote should escape backslashes");
        assertTrue(result.contains("\\\\"), "Backslash should be escaped");
    }

    /**
     * TDD failure test 6: quote with string containing double quote escapes it.
     */
    @Test
    public void testQuote_WithDoubleQuote_EscapesDoubleQuote() {
        String result = JsonUtil.quote("Say \"Hello\"");

        assertEquals("\"Say \\\"Hello\\\"\"", result, "quote should escape double quotes");
        assertTrue(result.contains("\\\""), "Double quote should be escaped");
    }

    /**
     * TDD failure test 7: quote with string containing newline escapes it.
     */
    @Test
    public void testQuote_WithNewline_EscapesNewline() {
        String result = JsonUtil.quote("line1\nline2");

        assertEquals("\"line1\\nline2\"", result, "quote should escape newline");
        assertTrue(result.contains("\\n"), "Newline should be escaped");
    }

    /**
     * TDD failure test 8: quote with string containing carriage return escapes it.
     */
    @Test
    public void testQuote_WithCarriageReturn_EscapesCarriageReturn() {
        String result = JsonUtil.quote("line1\rline2");

        assertEquals("\"line1\\rline2\"", result, "quote should escape carriage return");
        assertTrue(result.contains("\\r"), "Carriage return should be escaped");
    }

    /**
     * TDD failure test 9: quote with string containing tab escapes it.
     */
    @Test
    public void testQuote_WithTab_EscapesTab() {
        String result = JsonUtil.quote("col1\tcol2");

        assertEquals("\"col1\\tcol2\"", result, "quote should escape tab");
        assertTrue(result.contains("\\t"), "Tab should be escaped");
    }

    /**
     * TDD failure test 10: quote with string containing multiple special characters escapes all.
     */
    @Test
    public void testQuote_WithMultipleSpecialCharacters_EscapesAll() {
        String input = "text\"with\nmultiple\tspecial\rchars\\";
        String result = JsonUtil.quote(input);

        assertTrue(result.contains("\\\""), "Should escape double quote");
        assertTrue(result.contains("\\n"), "Should escape newline");
        assertTrue(result.contains("\\t"), "Should escape tab");
        assertTrue(result.contains("\\r"), "Should escape carriage return");
        assertTrue(result.contains("\\\\"), "Should escape backslash");
        assertTrue(result.startsWith("\""), "Should start with quote");
        assertTrue(result.endsWith("\""), "Should end with quote");
    }

    /**
     * TDD failure test 11: toJson with map containing null value handles null.
     */
    @Test
    public void testToJson_WithMapContainingNullValue_HandlesNull() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", null);

        String result = JsonUtil.toJson(map);

        assertTrue(result.contains("\"key\""), "Result should contain key");
        assertTrue(result.contains("null"), "Result should contain null value");
    }

    /**
     * TDD failure test 12: toJson with list containing null value handles null.
     */
    @Test
    public void testToJson_WithListContainingNullValue_HandlesNull() {
        List<Object> list = new ArrayList<>();
        list.add(null);

        String result = JsonUtil.toJson(list);

        assertEquals("[null]", result, "toJson with list containing null should return [null]");
    }

    /**
     * TDD failure test 13: toJson with nested null values handles them.
     */
    @Test
    public void testToJson_WithNestedNullValues_HandlesNulls() {
        Map<String, Object> map = new HashMap<>();
        List<Object> list = new ArrayList<>();
        list.add(null);
        map.put("items", list);
        map.put("value", null);

        String result = JsonUtil.toJson(map);

        assertTrue(result.contains("null"), "Result should contain null values");
    }

    /**
     * TDD pass test: toJson with string returns quoted string.
     */
    @Test
    public void testToJson_WithString_ReturnsQuotedString() {
        String result = JsonUtil.toJson("hello");

        assertEquals("\"hello\"", result, "toJson with string should return quoted string");
    }

    /**
     * TDD pass test: toJson with number returns number string.
     */
    @Test
    public void testToJson_WithNumber_ReturnsNumberString() {
        String result = JsonUtil.toJson(123);

        assertEquals("123", result, "toJson with number should return number string");
    }

    /**
     * TDD pass test: toJson with boolean returns boolean string.
     */
    @Test
    public void testToJson_WithBoolean_ReturnsBooleanString() {
        String result1 = JsonUtil.toJson(true);
        String result2 = JsonUtil.toJson(false);

        assertEquals("true", result1, "toJson with true should return 'true'");
        assertEquals("false", result2, "toJson with false should return 'false'");
    }

    /**
     * TDD pass test: toJson with map returns JSON object.
     */
    @Test
    public void testToJson_WithMap_ReturnsJsonObject() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", 30);

        String result = JsonUtil.toJson(map);

        assertTrue(result.startsWith("{"), "Result should start with {");
        assertTrue(result.endsWith("}"), "Result should end with }");
        assertTrue(result.contains("\"name\""), "Result should contain name key");
        assertTrue(result.contains("\"John\""), "Result should contain name value");
        assertTrue(result.contains("\"age\""), "Result should contain age key");
        assertTrue(result.contains("30"), "Result should contain age value");
    }

    /**
     * TDD pass test: toJson with list returns JSON array.
     */
    @Test
    public void testToJson_WithList_ReturnsJsonArray() {
        List<Object> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");

        String result = JsonUtil.toJson(list);

        assertTrue(result.startsWith("["), "Result should start with [");
        assertTrue(result.endsWith("]"), "Result should end with ]");
        assertTrue(result.contains("\"item1\""), "Result should contain item1");
        assertTrue(result.contains("\"item2\""), "Result should contain item2");
    }

    /**
     * TDD pass test: mapOf creates singleton map.
     */
    @Test
    public void testMapOf_CreatesSingletonMap() {
        Map<String, Object> map = JsonUtil.mapOf("key", "value");

        assertNotNull(map, "mapOf should return non-null map");
        assertEquals(1, map.size(), "mapOf should create singleton map");
        assertEquals("value", map.get("key"), "mapOf should contain the key-value pair");
    }

    /**
     * TDD pass test: quote with normal string returns quoted string.
     */
    @Test
    public void testQuote_WithNormalString_ReturnsQuotedString() {
        String result = JsonUtil.quote("hello");

        assertEquals("\"hello\"", result, "quote with normal string should return quoted string");
    }

    /**
     * TDD pass test: toJson with nested structures handles them correctly.
     */
    @Test
    public void testToJson_WithNestedStructures_HandlesCorrectly() {
        Map<String, Object> outer = new HashMap<>();
        List<Object> inner = new ArrayList<>();
        inner.add("item1");
        inner.add("item2");
        outer.put("items", inner);
        outer.put("count", 2);

        String result = JsonUtil.toJson(outer);

        assertTrue(result.startsWith("{"), "Result should start with {");
        assertTrue(result.endsWith("}"), "Result should end with }");
        assertTrue(result.contains("\"items\""), "Result should contain items key");
        assertTrue(result.contains("[\""), "Result should contain array");
    }
}
