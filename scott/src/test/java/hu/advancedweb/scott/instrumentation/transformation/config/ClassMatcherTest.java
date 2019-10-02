package hu.advancedweb.scott.instrumentation.transformation.config;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ClassMatcherTest {

	@Test
	public void test_matchesAsClass() {
		assertTrue(ClassMatcher.matchesAsClass("hu.awm.MyClass", Collections.singletonList("hu.awm.MyClass")));
		assertTrue(ClassMatcher.matchesAsClass("hu.awm.MyClass", Arrays.asList("hu.awm.SomethingElse", "hu.awm.MyClass")));
		assertFalse(ClassMatcher.matchesAsClass("hu.awm.MyClass$InnerClass", Collections.singletonList("hu.awm.MyClass")));
		assertFalse(ClassMatcher.matchesAsClass("hu.awm.other.package.MyClass", Collections.singletonList("hu.awm.MyClass")));
		assertFalse(ClassMatcher.matchesAsClass("hu.awm.MyClass", Collections.singletonList("hu.awm.SomethingElse")));
		assertFalse(ClassMatcher.matchesAsClass("hu.awm.MyClass", new ArrayList<String>()));
	}
	
	@Test
	public void test_matchesAsInnerClass() {
		assertTrue(ClassMatcher.matchesAsInnerClass("hu.awm.MyClass$InnerClass", Collections.singletonList("hu.awm.MyClass")));
		assertTrue(ClassMatcher.matchesAsInnerClass("hu.awm.MyClass$InnerClass$InnerInnerClass", Collections.singletonList("hu.awm.MyClass")));
		assertTrue(ClassMatcher.matchesAsInnerClass("hu.awm.MyClass$InnerClass", Arrays.asList("hu.awm.SomethingElse", "hu.awm.MyClass")));
		assertFalse(ClassMatcher.matchesAsInnerClass("hu.awm.MyClass", Collections.singletonList("hu.awm.MyClass")));
		assertFalse(ClassMatcher.matchesAsInnerClass("hu.awm.other.package.MyClass$InnerClass", Collections.singletonList("hu.awm.MyClass")));
		assertFalse(ClassMatcher.matchesAsInnerClass("hu.awm.MyClass$InnerClass", Collections.singletonList("hu.awm.SomethingElse")));
		assertFalse(ClassMatcher.matchesAsInnerClass("hu.awm.MyClass$InnerClass", new ArrayList<String>()));
	}

	@Test
	public void test_matchesAsPackage() {
		assertTrue(ClassMatcher.matchesAsPackage("hu.awm.MyClass", Collections.singletonList("hu.awm")));
		assertTrue(ClassMatcher.matchesAsPackage("hu.awm.MyClass$InnerClass", Collections.singletonList("hu.awm")));
		assertTrue(ClassMatcher.matchesAsPackage("hu.awm.MyClass$InnerClass$InnerInnerClass", Collections.singletonList("hu.awm")));
		assertTrue(ClassMatcher.matchesAsPackage("hu.awm.MyClass", Arrays.asList("hu.something.else", "hu.awm")));
		assertFalse(ClassMatcher.matchesAsPackage("hu.something.else.MyClass$InnerClass", Collections.singletonList("hu.awm")));
		assertFalse(ClassMatcher.matchesAsPackage("hu.awm.MyClass", Collections.singletonList("hu.something.else")));
		assertFalse(ClassMatcher.matchesAsPackage("hu.awm.MyClass", new ArrayList<String>()));
	}

	@Test
	public void test_anyMatchesAsClassOrPackage() {
		assertTrue(ClassMatcher.anyMatchesAsClassOrPackage(Arrays.asList("hu.awm.MyClass"), Collections.singletonList("hu.awm")));
		assertTrue(ClassMatcher.anyMatchesAsClassOrPackage(Arrays.asList("hu.awm.MyClass$InnerClass"), Collections.singletonList("hu.awm.MyClass")));
		assertTrue(ClassMatcher.anyMatchesAsClassOrPackage(Arrays.asList("hu.awm.MyClass"), Collections.singletonList("hu.awm.MyClass")));
	}

}
