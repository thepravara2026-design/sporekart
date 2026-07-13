package com.sporekart.ai.policy.application;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PolicyCompilerImplTest {
    private final PolicyCompilerImpl compiler = new PolicyCompilerImpl();

    @Test void testCompile() { assertNotNull(compiler.compile("expr", Map.of())); }
    @Test void testValidate() { assertTrue(compiler.validate("expr")); assertFalse(compiler.validate("")); }
    @Test void testParse() { assertNotNull(compiler.parse("expr")); }
    @Test void testIsCompiled() { assertTrue(compiler.isCompiled(compiler.compile("e", Map.of()))); assertFalse(compiler.isCompiled(null)); }
}
