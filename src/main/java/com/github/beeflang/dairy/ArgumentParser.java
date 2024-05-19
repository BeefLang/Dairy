package com.github.beeflang.dairy;

import java.util.*;

public class ArgumentParser {
    private final List<String> arguments;

    public ArgumentParser(String... arguments) {
        this(Arrays.asList(arguments));
    }

    public ArgumentParser(List<String> arguments) {
        this.arguments = arguments;
    }

    public ParsedArguments parse(ParseOptions options) {
        return new ParsedArguments(parseRaw(options));
    }

    public List<ParsedArgument> parseRaw(ParseOptions options) throws NotEnoughArgumentsException, IllegalArgumentException, ArgumentNotPresentException {
        Deque<String> argumentQueue = new ArrayDeque<>(arguments);
        List<ParsedArgument> parsed = new ArrayList<>();
        String nextArg;

        while ((nextArg = argumentQueue.poll()) != null) {
            if (!nextArg.startsWith("-")) throw new IllegalArgumentException("Invalid argument start: " + nextArg);

            String possiblyNext = nextArg.substring(1);

            options.arguments().stream().filter(arg -> arg.name().equals(possiblyNext)).forEach(arg -> {
                int expectedLength = arg.splitLength(), current = 0;
                DataType dataType = arg.dataType();
                StringBuilder content = new StringBuilder();

                while (current != expectedLength) {
                    try {
                        String next = argumentQueue.remove();

                        if (next.startsWith("-")) {
                            argumentQueue.push(next);

                            break;
                        }

                        if (!dataType.isApplicable(next))
                            throw new IllegalArgumentException("Data type " + dataType + " is not applicable for " + next);

                        content.append(next);
                    } catch (NoSuchElementException ex) {
                        throw new NotEnoughArgumentsException();
                    }

                    ++current;
                }

                parsed.add(new ParsedArgument(arg, content.toString()));
            });
        }

        options.arguments().stream().filter(Argument::optional).filter(arg -> parsed.stream().noneMatch(par -> par.from().hashCode() == arg.hashCode())).filter(arg -> arg.defaultValue() != null).forEach(arg -> parsed.add(new ParsedArgument(arg, arg.defaultValue())));

        options.arguments().stream().filter(arg -> !arg.optional()).filter(arg -> parsed.stream().noneMatch(par -> par.from().hashCode() == arg.hashCode())).forEach(arg -> {
            throw new ArgumentNotPresentException("Argument not found: " + arg.name());
        });

        return parsed;
    }
}