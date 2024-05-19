package com.github.beeflang.dairy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ParseOptions(List<Argument> arguments) {
    public ParseOptions(Argument... arguments) {
        this(Arrays.asList(arguments));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Argument> arguments = new ArrayList<>();

        private Builder() {
        }

        public Builder argument(Argument argument) {
            arguments.add(argument);

            return this;
        }

        public Builder argument(Argument.Builder builder) {
            return argument(builder.build());
        }

        public ParseOptions build() {
            return new ParseOptions(arguments);
        }
    }
}