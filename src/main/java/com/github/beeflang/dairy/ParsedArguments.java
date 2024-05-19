package com.github.beeflang.dairy;

import java.util.Arrays;
import java.util.List;

public record ParsedArguments(List<ParsedArgument> arguments) {
    public ParsedArguments(ParsedArgument... arguments) {
        this(Arrays.asList(arguments));
    }

    public ParsedArgument get(String key) {
        return arguments.stream().filter(arg -> arg.from().name().equals(key)).findAny().orElse(null);
    }

    public String getContent(String key) {
        ParsedArgument arg = get(key);

        if (arg == null) throw new ArgumentNotPresentException("Argument not present: " + key);

        return arg.content();
    }

    @SuppressWarnings("unchecked")
    public <T> T getContentInType(String key) throws ArgumentNotPresentException {
        ParsedArgument arg = get(key);

        if (arg == null) throw new ArgumentNotPresentException("Argument not present: " + key);

        DataType type = arg.from().dataType();
        String content = arg.content();
        Object result = type.tryParse(content);

        if (result == null) throw new IllegalArgumentException("Invalid data type");

        return (T) result;
    }
}