/*
 * Sly Technologies Free License
 * 
 * Copyright 2023 Sly Technologies Inc.
 *
 * Licensed under the Sly Technologies Free License (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.slytechs.com/free-license-text
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.slytechs.protocol.runtime.internal.foreign;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.ValueLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class PaddedLayout.
 *
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 * @author Mark Bednarczyk
 */
public class PaddedLayout implements PaddedField {

	/**
	 * The Enum Type.
	 */
	public enum Type {
		
		/** The struct. */
		STRUCT,
		
		/** The union. */
		UNION,
		
		/** The sequence. */
		SEQUENCE,
		
		/** The value. */
		VALUE,
	}

	/**
	 * Padded sequence.
	 *
	 * @param size    the size
	 * @param element the element
	 * @return the padded layout
	 */
	public static PaddedLayout paddedSequence(long size, Object element) {
		return new PaddedLayout(size, toStructuredFields(element));
	}

	/**
	 * Padded struct.
	 *
	 * @param fields the fields
	 * @return the padded layout
	 */
	public static PaddedLayout paddedStruct(Object... fields) {
		return new PaddedLayout(Type.STRUCT, toStructuredFields(fields));
	}

	/**
	 * Padded union.
	 *
	 * @param fields the fields
	 * @return the padded layout
	 */
	public static PaddedLayout paddedUnion(Object... fields) {
		return new PaddedLayout(Type.UNION, toStructuredFields(fields));
	}

	/**
	 * To structured fields.
	 *
	 * @param fields the fields
	 * @return the list
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private static List<PaddedField> toStructuredFields(Object... fields) throws IllegalArgumentException {
		List<String> rejects = Arrays.stream(fields)
				.filter(f -> !(f instanceof MemoryLayout) && !(f instanceof PaddedLayout))
				.map(Object::getClass)
				.map(Class::getSimpleName)
				.collect(Collectors.toList());

		if (!rejects.isEmpty())
			throw new IllegalArgumentException(rejects.toString());

		List<PaddedField> listOfStructuredFields = new ArrayList<>();

		for (Object fieldObjectToCheck : fields) {

			PaddedField paddedField = switch (fieldObjectToCheck) {

			case PaddedField field -> field;
			case MemoryLayout layout -> new PaddedLayout.ToStructuredField(layout);

			default -> throw new IllegalArgumentException("Unexpected value: " + fieldObjectToCheck);
			};

			listOfStructuredFields.add(paddedField);
		}

		return listOfStructuredFields;
	}

	/** The list. */
	private final List<PaddedField> list;
	
	/** The type. */
	private final Type type;
	
	/** The sequence size. */
	private final long sequenceSize;
	
	/** The bit alignment. */
	private final long bitAlignment;
	
	/** The compact. */
	private boolean compact;
	
	/** The debug name. */
	private String debugName;
	
	/** The debug. */
	private boolean debug;

	/**
	 * Instantiates a new padded layout.
	 *
	 * @param size       the size
	 * @param oneElement the one element
	 */
	private PaddedLayout(long size, List<PaddedField> oneElement) {
		this.type = Type.SEQUENCE;
		this.list = oneElement;
		this.sequenceSize = size;
		this.bitAlignment = list.stream()
				.mapToLong(PaddedField::bitAlignment)
				.filter(s -> s <= MAX_ALIGMENT_SIZE)
				.max()
				.orElse(8);

		assert false

				|| bitAlignment == 8
				|| bitAlignment == 16
				|| bitAlignment == 32
				|| bitAlignment == 64

		;
	}

	/**
	 * Instantiates a new padded layout.
	 *
	 * @param type the type
	 * @param list the list
	 */
	private PaddedLayout(Type type, List<PaddedField> list) {
		this.type = type;
		this.list = list;
		this.sequenceSize = -1;
		this.bitAlignment = list.stream()
				.mapToLong(PaddedField::bitAlignment)
				.filter(s -> s <= MAX_ALIGMENT_SIZE)
				.max()
				.orElse(8);

		assert false

				|| bitAlignment == 8
				|| bitAlignment == 16
				|| bitAlignment == 32
				|| bitAlignment == 64

		;

		/*
		 * java.lang.IllegalStateException: can not invoke native C function
		 * 'sizeofNtStatGroupcolor_s()J' at
		 * com.slytechs.jnet.jnapatech/com.slytechs.internal.jnapatech.ForeignDowncall.handle
		 * (ForeignDowncall.java:102) at
		 * com.slytechs.jnet.jnapatech/com.slytechs.internal.jnapatech.ForeignDowncall.
		 * invokeLong(ForeignDowncall.java:210) at
		 * com.slytechs.jnet.jnapatech/jnapatech.tests.StructuredLayoutTest.test(
		 * StructuredLayoutTest.java:218) at
		 * java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(
		 * DirectMethodHandleAccessor.java:104) at
		 * java.base/java.lang.reflect.Method.invoke(Method.java:578) at
		 * org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.
		 * java:727) at
		 * org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.
		 * java:60) at org.junit.jupiter.engine.execution.
		 * InvocationInterceptorChain$ValidatingInvocation.proceed(
		 * InvocationInterceptorChain.java:131) at
		 * org.junit.jupiter.engine.extension.TimeoutExtension.intercept(
		 * TimeoutExtension.java:156) at
		 * org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(
		 * TimeoutExtension.java:147) at
		 * org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(
		 * TimeoutExtension.java:86) at org.junit.jupiter.engine.execution.
		 * InterceptingExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0
		 * (InterceptingExecutableInvoker.java:103) at
		 * org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.
		 * lambda$invoke$0(InterceptingExecutableInvoker.java:93) at
		 * org.junit.jupiter.engine.execution.
		 * InvocationInterceptorChain$InterceptedInvocation.proceed(
		 * InvocationInterceptorChain.java:106) at
		 * org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(
		 * InvocationInterceptorChain.java:64) at
		 * org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(
		 * InvocationInterceptorChain.java:45) at
		 * org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(
		 * InvocationInterceptorChain.java:37) at
		 * org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(
		 * InterceptingExecutableInvoker.java:92) at
		 * org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(
		 * InterceptingExecutableInvoker.java:86) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.
		 * lambda$invokeTestMethod$7(TestMethodTestDescriptor.java:217) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod
		 * (TestMethodTestDescriptor.java:213) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(
		 * TestMethodTestDescriptor.java:138) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(
		 * TestMethodTestDescriptor.java:68) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$6(NodeTestTask.java:151) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$8(NodeTestTask.java:141) at
		 * org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$9(NodeTestTask.java:139) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * executeRecursively(NodeTestTask.java:138) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(
		 * NodeTestTask.java:95) at
		 * java.base/java.util.ArrayList.forEach(ArrayList.java:1511) at
		 * org.junit.platform.engine.support.hierarchical.
		 * SameThreadHierarchicalTestExecutorService.invokeAll(
		 * SameThreadHierarchicalTestExecutorService.java:41) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$6(NodeTestTask.java:155) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$8(NodeTestTask.java:141) at
		 * org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$9(NodeTestTask.java:139) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * executeRecursively(NodeTestTask.java:138) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(
		 * NodeTestTask.java:95) at
		 * java.base/java.util.ArrayList.forEach(ArrayList.java:1511) at
		 * org.junit.platform.engine.support.hierarchical.
		 * SameThreadHierarchicalTestExecutorService.invokeAll(
		 * SameThreadHierarchicalTestExecutorService.java:41) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$6(NodeTestTask.java:155) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$8(NodeTestTask.java:141) at
		 * org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * lambda$executeRecursively$9(NodeTestTask.java:139) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.
		 * executeRecursively(NodeTestTask.java:138) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(
		 * NodeTestTask.java:95) at org.junit.platform.engine.support.hierarchical.
		 * SameThreadHierarchicalTestExecutorService.submit(
		 * SameThreadHierarchicalTestExecutorService.java:35) at
		 * org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.
		 * execute(HierarchicalTestExecutor.java:57) at
		 * org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute
		 * (HierarchicalTestEngine.java:54) at
		 * org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(
		 * EngineExecutionOrchestrator.java:147) at
		 * org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(
		 * EngineExecutionOrchestrator.java:127) at
		 * org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(
		 * EngineExecutionOrchestrator.java:90) at
		 * org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0
		 * (EngineExecutionOrchestrator.java:55) at
		 * org.junit.platform.launcher.core.EngineExecutionOrchestrator.
		 * withInterceptedStreams(EngineExecutionOrchestrator.java:102) at
		 * org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(
		 * EngineExecutionOrchestrator.java:54) at
		 * org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java
		 * :114) at
		 * org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java
		 * :95) at
		 * org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.
		 * execute(DefaultLauncherSession.java:91) at
		 * org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(
		 * SessionPerRequestLauncher.java:60) at
		 * org.eclipse.jdt.internal.junit5.runner.JUnit5TestReference.run(
		 * JUnit5TestReference.java:98) at
		 * org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:
		 * 40) at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(
		 * RemoteTestRunner.java:529) at
		 * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(
		 * RemoteTestRunner.java:756) at
		 * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.
		 * java:452) at
		 * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.
		 * java:210) Caused by: java.util.NoSuchElementException: native C symbol
		 * "sizeofNtStatGroupcolor_s" not found at
		 * com.slytechs.jnet.jnapatech/com.slytechs.internal.jnapatech.ForeignInitializer.
		 * resolveSymbol(ForeignInitializer.java:483) at
		 * com.slytechs.jnet.jnapatech/com.slytechs.internal.jnapatech.ForeignInitializer.
		 * downcall(ForeignInitializer.java:435) at
		 * com.slytechs.jnet.jnapatech/jnapatech.tests.StructuredLayoutTest.<clinit>(
		 * StructuredLayoutTest.java:94) at
		 * java.base/jdk.internal.misc.Unsafe.ensureClassInitialized0(Native Method) at
		 * java.base/jdk.internal.misc.Unsafe.ensureClassInitialized(Unsafe.java:1160)
		 * at java.base/jdk.internal.reflect.MethodHandleAccessorFactory.
		 * ensureClassInitialized(MethodHandleAccessorFactory.java:300) at
		 * java.base/jdk.internal.reflect.MethodHandleAccessorFactory.
		 * newConstructorAccessor(MethodHandleAccessorFactory.java:103) at
		 * java.base/jdk.internal.reflect.ReflectionFactory.newConstructorAccessor(
		 * ReflectionFactory.java:201) at
		 * java.base/java.lang.reflect.Constructor.acquireConstructorAccessor(
		 * Constructor.java:547) at
		 * java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.
		 * java:497) at
		 * java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:484) at
		 * org.junit.platform.commons.util.ReflectionUtils.newInstance(ReflectionUtils.
		 * java:552) at
		 * org.junit.jupiter.engine.execution.ConstructorInvocation.proceed(
		 * ConstructorInvocation.java:56) at org.junit.jupiter.engine.execution.
		 * InvocationInterceptorChain$ValidatingInvocation.proceed(
		 * InvocationInterceptorChain.java:131) at
		 * org.junit.jupiter.api.extension.InvocationInterceptor.
		 * interceptTestClassConstructor(InvocationInterceptor.java:73) at
		 * org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.
		 * lambda$invoke$0(InterceptingExecutableInvoker.java:93) at
		 * org.junit.jupiter.engine.execution.
		 * InvocationInterceptorChain$InterceptedInvocation.proceed(
		 * InvocationInterceptorChain.java:106) at
		 * org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(
		 * InvocationInterceptorChain.java:64) at
		 * org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(
		 * InvocationInterceptorChain.java:45) at
		 * org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(
		 * InvocationInterceptorChain.java:37) at
		 * org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(
		 * InterceptingExecutableInvoker.java:92) at
		 * org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(
		 * InterceptingExecutableInvoker.java:62) at
		 * org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.
		 * invokeTestClassConstructor(ClassBasedTestDescriptor.java:363) at
		 * org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.
		 * instantiateTestClass(ClassBasedTestDescriptor.java:310) at
		 * org.junit.jupiter.engine.descriptor.ClassTestDescriptor.instantiateTestClass(
		 * ClassTestDescriptor.java:79) at
		 * org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.
		 * instantiateAndPostProcessTestInstance(ClassBasedTestDescriptor.java:286) at
		 * org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.
		 * lambda$testInstancesProvider$4(ClassBasedTestDescriptor.java:278) at
		 * java.base/java.util.Optional.orElseGet(Optional.java:364) at
		 * org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.
		 * lambda$testInstancesProvider$5(ClassBasedTestDescriptor.java:277) at
		 * org.junit.jupiter.engine.execution.TestInstancesProvider.getTestInstances(
		 * TestInstancesProvider.java:31) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$prepare$0
		 * (TestMethodTestDescriptor.java:105) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.prepare(
		 * TestMethodTestDescriptor.java:104) at
		 * org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.prepare(
		 * TestMethodTestDescriptor.java:68) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$prepare$2(
		 * NodeTestTask.java:123) at
		 * org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(
		 * ThrowableCollector.java:73) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.prepare(
		 * NodeTestTask.java:123) at
		 * org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(
		 * NodeTestTask.java:90) ... 39 more
		 * 
		 * 
		 */

	}

	/** The Constant MAX_ALIGMENT_SIZE. */
	private static final long MAX_ALIGMENT_SIZE = 64; // Bits

	/**
	 * Apply padding.
	 */
	private void applyPadding() {

		long offset = 0;

		// B SS IIII
		// B IIII SS

		for (int i = 0; i < list.size(); i++) {
			PaddedField element = list.get(i);
			long alignment = element.bitAlignment();
			long alignmentDelta = (offset % alignment);
			boolean isAligned = (alignmentDelta == 0);

			long padding = isAligned ? 0 : (alignment - alignmentDelta);
			if (!isAligned && padding < MAX_ALIGMENT_SIZE) {
				PaddedField padLayout = paddingLayout(padding);
				list.add(i, padLayout);

				i++; // Skip the just added pad layout
			}

			offset += element.bitSize() + padding;
		}

		/* Now check make sure entire structure is properly aligned */
		long alignmentDelta = (offset % bitAlignment);
		long padding = (alignmentDelta == 0) ? 0 : bitAlignment - (offset % bitAlignment);
		if (padding > 0 && padding < MAX_ALIGMENT_SIZE) {
			PaddedField padLayout = paddingLayout(padding);

			list.add(padLayout);
		}
	}

	/**
	 * As memory layout.
	 *
	 * @return the memory layout
	 * @see com.slytechs.protocol.runtime.internal.foreign.PaddedField#asMemoryLayout()
	 */
	@Override
	public MemoryLayout asMemoryLayout() {

		if (debug && !compact)
			applyPadding(); // Allows us to set a breakpoint while debugging

		else if (!compact)
			applyPadding();

		if (debug)
			System.out.printf("%s size=%,d bytes, align=%d, list=%s%n",
					debugName, byteSize(), bitAlignment(), list);

		MemoryLayout[] array = list.stream()
				.map(PaddedField::asMemoryLayout)
				.toArray(MemoryLayout[]::new);

		MemoryLayout layout = switch (type) {
		case SEQUENCE -> MemoryLayout.sequenceLayout(sequenceSize, array[0]);
		case STRUCT -> MemoryLayout.structLayout(array);
		case UNION -> MemoryLayout.unionLayout(array);
		case VALUE -> array[0];
		};

		return layout.withByteAlignment(bitAlignment / 8);
	}

	/**
	 * Bit alignment.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.jnapatech.internal.foreign.PaddedField#bitAlignment()
	 */
	@Override
	public long bitAlignment() {
		return bitAlignment;
	}

	/**
	 * Bit size.
	 *
	 * @return the long
	 * @see com.slytechs.jnet.jnapatech.internal.foreign.PaddedField#bitSize()
	 */
	@Override
	public long bitSize() {
		return list.stream()
				.mapToLong(PaddedField::bitSize)
				.sum();
	}

	/**
	 * Compact.
	 *
	 * @return the padded layout
	 */
	public PaddedLayout compact() {
		this.compact = true;

		return this;
	}

	/**
	 * Debug.
	 *
	 * @param structName the struct name
	 * @return the padded layout
	 */
	public PaddedLayout debug(String structName) {
		return debug(structName, true);

	}

	/**
	 * Debug.
	 *
	 * @param structName the struct name
	 * @param enable     the enable
	 * @return the padded layout
	 */
	public PaddedLayout debug(String structName, boolean enable) {
		this.debug = enable;
		this.debugName = structName;

		return this;
	}

	/**
	 * Padded.
	 *
	 * @return the padded layout
	 */
	public PaddedLayout padded() {
		this.compact = false;

		return this;
	}

	/**
	 * Padding layout.
	 *
	 * @param bitSize the bit size
	 * @return the padded field
	 */
	private PaddedField paddingLayout(long bitSize) {
		if ((bitSize % 8) != 0)
			throw new IllegalArgumentException("invalid padding bitsize, only multiples of 8 bits are allowed [%d]"
					.formatted(bitSize));

		long byteSize = (bitSize >> 3);

		return new ToStructuredField(MemoryLayout.sequenceLayout(byteSize, ValueLayout.JAVA_BYTE));
	}

}
