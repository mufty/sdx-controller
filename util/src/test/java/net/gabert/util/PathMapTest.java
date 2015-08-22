package net.gabert.util;

import net.gabert.util.PathMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PathMapTest {
    @Test
    public void setGetSimple() {
        PathMap<String> pm = new PathMap<>();
        pm.put("/a/b/c", "VAL1");
        pm.put("/a/b/c/d", "VAL2");
        pm.put("/x/y/z", "VAL3");

        assertEquals("VAL1", pm.get("/a/b/c"));
        assertEquals("VAL2", pm.get("/a/b/c/d"));
        assertEquals("VAL3", pm.get("/x/y/z"));
    }

    @Test
    public void setGetMultiPath() {
        PathMap<String> pm = new PathMap<>();
        pm.put("/a/b/c", "VAL1");
        pm.put("/a/b/c/d", "VAL2");
        pm.put("/a/b/x", "VAL3");

        assertEquals("VAL1", pm.get("/a/b/c"));
        assertEquals("VAL2", pm.get("/a/b/c/d"));
        assertEquals("VAL3", pm.get("/a/b/x"));
    }

    @Test
    public void setGetMostSpecific() {
        PathMap<String> pm = new PathMap<>();
        pm.put("/a/b", "VAL1");
        pm.put("/a/b/c/d", "VAL2");

        assertEquals("VAL2", pm.get("/a/b/c/d/e"));
        assertEquals("VAL1", pm.get("/a/b/d/e"));
        assertEquals(null, pm.get("/a"));
    }
}
