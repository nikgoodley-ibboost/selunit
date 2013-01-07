package org.selunit.webdriver.executor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.selunit.webdriver.Command;
import org.selunit.webdriver.CommandInvocation;
import org.selunit.webdriver.ExecutionContext;
import org.selunit.webdriver.InvalidCommandException;
import org.selunit.webdriver.TestException;
import org.selunit.webdriver.commands.annotations.CommandGenerator;
import org.selunit.webdriver.commands.annotations.CommandMapping;
import org.selunit.webdriver.commands.annotations.CommandMappings;
import org.selunit.webdriver.commands.annotations.TargetParam;
import org.selunit.webdriver.commands.annotations.ValueParam;

public class CommandMappingProvider {
	private Log log = LogFactory.getLog(getClass());

	private class CommandInvocationDefinition {
		protected Object methodObject;
		protected Method method;
		protected String name;
	}

	private class CommandDefintion extends CommandInvocationDefinition {
		private GeneratorDefinition generator;
		private String derivedName;

		@Override
		public String toString() {
			return name
					+ (generator != null ? ": generator="
							+ generator.annotation.name() : "") + ": "
					+ method.toGenericString();
		}
	}

	private class GeneratorDefinition extends CommandInvocationDefinition {
		private CommandGenerator annotation;

		@Override
		public String toString() {
			return annotation.name() + ", prefix=" + annotation.prefix()
					+ ", suffix=" + annotation.suffix() + ": "
					+ method.toGenericString();
		}
	}

	private HashMap<String, CommandDefintion> commandMethods = new HashMap<String, CommandDefintion>();
	private HashMap<String, GeneratorDefinition> generators = new HashMap<String, GeneratorDefinition>();
	private HashMap<Class<?>, Object> methodObjects = new HashMap<Class<?>, Object>();

	protected Object getMethodObject(Method m) throws InvalidCommandException {
		if (!methodObjects.containsKey(m.getDeclaringClass())) {
			try {
				methodObjects.put(m.getDeclaringClass(), m.getDeclaringClass()
						.newInstance());
			} catch (Exception e) {
				throw new InvalidCommandException(
						"Failed to instantiate class object of type "
								+ m.getDeclaringClass());
			}
		}
		return methodObjects.get(m.getDeclaringClass());
	}

	/**
	 * Sets class instance for command methods. For test issues only! Call it
	 * before scanning.
	 * 
	 * @param clazz
	 * @param instance
	 */
	protected void setMethodObject(Class<?> clazz, Object instance) {
		methodObjects.put(clazz, instance);
	}

	public void scanForCommands(String packagePrefix)
			throws InvalidCommandException {
		HashMap<String, CommandDefintion> localCommandMethods = new HashMap<String, CommandDefintion>();
		log.debug("Start scanning for generators in package: " + packagePrefix);
		final Reflections reflections = new Reflections(packagePrefix,
				new MethodAnnotationsScanner());

		for (Method m : reflections
				.getMethodsAnnotatedWith(CommandGenerator.class)) {
			if (m.isAnnotationPresent(CommandGenerator.class)) {
				CommandGenerator generator = m
						.getAnnotation(CommandGenerator.class);
				GeneratorDefinition gd = new GeneratorDefinition();
				gd.name = generator.name();
				gd.annotation = generator;
				gd.method = m;
				gd.methodObject = getMethodObject(m);
				generators.put(generator.name(), gd);
				log.debug("Registered new generator: " + gd);
			}
		}
		log.debug("Start scanning for commands in package: " + packagePrefix);
		HashSet<Method> commands = new HashSet<Method>();
		commands.addAll(reflections
				.getMethodsAnnotatedWith(CommandMappings.class));
		commands.addAll(reflections
				.getMethodsAnnotatedWith(CommandMapping.class));
		for (Method m : commands) {
			if (m.isAnnotationPresent(CommandMappings.class)) {
				for (CommandMapping mapping : m.getAnnotation(
						CommandMappings.class).mappings()) {
					for (CommandDefintion cd : createCommandDefinitionsFor(
							mapping, m)) {
						localCommandMethods.put(cd.name, cd);
						log.debug("Registered new command: " + cd);
					}
				}
			}
			if (m.isAnnotationPresent(CommandMapping.class)) {
				CommandMapping mapping = m.getAnnotation(CommandMapping.class);
				for (CommandDefintion cd : createCommandDefinitionsFor(mapping,
						m)) {
					localCommandMethods.put(cd.name, cd);
					log.debug("Registered new command: " + cd);
				}
			}
		}
		commandMethods.putAll(localCommandMethods);
		log.debug("Found " + localCommandMethods.size()
				+ " commands/wrappers in package: " + packagePrefix);
	}

	protected List<CommandDefintion> createCommandDefinitionsFor(
			CommandMapping mapping, Method m) throws InvalidCommandException {
		if (mapping.generators() != null && mapping.generators().length > 0) {
			ArrayList<CommandDefintion> commands = new ArrayList<CommandMappingProvider.CommandDefintion>();
			for (String generatorName : mapping.generators()) {
				GeneratorDefinition generator = generators.get(generatorName);
				if (generator != null) {
					CommandDefintion cd = new CommandDefintion();
					cd.derivedName = mapping.name();
					cd.name = mapping.name();
					if (StringUtils.isNotBlank(generator.annotation.prefix())) {
						cd.name = generator.annotation.prefix()
								+ StringUtils.capitalize(cd.name);
					}
					if (StringUtils.isNotBlank(generator.annotation.suffix())) {
						cd.name += generator.annotation.suffix();
					}
					cd.method = m;
					cd.generator = generator;
					cd.methodObject = getMethodObject(m);
					commands.add(cd);
					log.debug("Generated new command from '" + mapping.name()
							+ "': " + cd);
				} else {
					throw new InvalidCommandException(
							"Command generator with name '" + generatorName
									+ "' not found for command: "
									+ mapping.name());
				}
			}
			return commands;
		} else {
			CommandDefintion cd = new CommandDefintion();
			cd.derivedName = mapping.name();
			cd.name = mapping.name();
			cd.method = m;
			cd.methodObject = getMethodObject(m);
			log.debug("Created new command: " + cd);
			return Collections.singletonList(cd);
		}
	}

	/**
	 * Creates an invocation chain for given command.
	 * 
	 * @param c
	 *            command
	 * @param ec
	 *            execution context
	 * @return invocation chain
	 * @throws InvalidCommandException
	 *             indicates an configuration error during compounding command
	 *             invocations and passing arguments to these.
	 */
	public CommandInvocation<?> create(Command c, ExecutionContext ec)
			throws InvalidCommandException {
		return create(c, ec, null);
	}

	/**
	 * Creates an invocation chain for given command and an inner invocation.
	 * Relevant specially for generator tests.
	 * 
	 * @param c
	 *            command
	 * @param ec
	 *            execution context
	 * @param innerCI
	 *            inner invocation chain
	 * @return invocation chain
	 * @throws InvalidCommandException
	 *             indicates an configuration error during compounding command
	 *             invocations and passing arguments to these.
	 */
	public CommandInvocation<?> create(Command c, ExecutionContext ec,
			CommandInvocation<?> innerCI) throws InvalidCommandException {
		CommandDefintion cd = commandMethods.get(c.getName());
		if (cd == null) {
			throw new InvalidCommandException("Command not found: " + c);
		} else {
			return create(c, cd, ec, innerCI);
		}
	}

	/**
	 * Creates an invocation chain for given command.
	 * 
	 * @param c
	 *            command
	 * @param cid
	 *            command invocation definition
	 * @param ec
	 *            execution context
	 * @param innerCI
	 *            inner invocation chain
	 * @return invocation chain
	 * @throws InvalidCommandException
	 *             indicates an configuration error during compounding command
	 *             invocations and passing arguments to these.
	 */
	protected CommandInvocation<?> create(final Command c,
			final CommandInvocationDefinition cid, final ExecutionContext ec,
			CommandInvocation<?> innerCI) throws InvalidCommandException {
		final Object[] args = new Object[cid.method.getParameterAnnotations().length];
		for (int i = 0; i < args.length; i++) {
			Object arg = null;
			boolean filled = false;
			for (Annotation a : cid.method.getParameterAnnotations()[i]) {
				if (a.annotationType().equals(TargetParam.class)) {
					arg = c.getParams() != null ? c.getParams().get(
							Command.PARAM_TARGET) : null;
					if (arg != null && ((TargetParam) a).evaluate()
							&& ec.getParamEvaluator() != null) {
						arg = ec.getParamEvaluator().evaluate(
								ec.getPropertiesContext(((TargetParam) a)
										.evaluationScope()), arg.toString());
					}
					filled = true;
				} else if (a.annotationType().equals(ValueParam.class)) {
					arg = c.getParams() != null ? c.getParams().get(
							Command.PARAM_VALUE) : null;
					if (arg != null && ((ValueParam) a).evaluate()
							&& ec.getParamEvaluator() != null) {
						arg = ec.getParamEvaluator().evaluate(
								ec.getPropertiesContext(((ValueParam) a)
										.evaluationScope()), arg.toString());
					}
					filled = true;
				}
			}
			Class<?> paramType = cid.method.getParameterTypes()[i];
			if (filled && paramType.equals(By.class)) {
				arg = LocatorFactory.getLocator(arg.toString());
			}
			if (!filled) {
				if (paramType.equals(CommandInvocation.class)) {
					if (innerCI == null) {
						throw new InvalidCommandException(
								"Can't pass inner command invocation for wrapper '"
										+ cid.name
										+ "' because there is no inner invocation inside "
										+ c.getName());
					}
					arg = innerCI;
					filled = true;
				} else if (paramType.equals(ExecutionContext.class)) {
					arg = ec;
					filled = true;
				}
			}
			if (!filled) {
				throw new InvalidCommandException(
						"Can't detect type of parameter " + i + " for command "
								+ c.getName() + ": "
								+ cid.method.toGenericString());
			}
			args[i] = arg;
		}

		CommandInvocation<?> ci = new CommandInvocation<Object>() {
			@Override
			public Object invoke() throws TestException {
				try {
					if (log.isDebugEnabled()) {
						if (cid instanceof CommandDefintion) {
							CommandDefintion cd = (CommandDefintion) cid;
							if (cd.generator != null) {
								log.debug("Executing derived command: "
										+ ((CommandDefintion) cid).derivedName);
							} else {
								log.debug("Executing command: "
										+ ((CommandDefintion) cid).derivedName);
							}
						} else if (cid instanceof GeneratorDefinition) {
							log.debug("Executing generator wrapper: "
									+ cid.name);
						}
					}
					return cid.method.invoke(cid.methodObject, args);
				} catch (InvocationTargetException e) {
					throw new TestException("Error during executing command: "
							+ c, e.getCause());
				} catch (Exception e) {
					throw new TestException("Error during executing command: "
							+ c, e);
				}
			}
		};

		if (cid instanceof CommandDefintion) {
			CommandDefintion cd = (CommandDefintion) cid;
			if (cd.generator != null) {
				ci = create(c, cd.generator, ec, ci);
			}
		}

		return ci;
	}

}
