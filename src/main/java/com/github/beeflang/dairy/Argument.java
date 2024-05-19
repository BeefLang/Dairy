package com.github.beeflang.dairy;

public record Argument(String name, String defaultValue, DataType dataType, int splitLength, boolean infiniteLength,
                       boolean optional) {
    public Argument(String name, DataType dataType, int length, boolean optional) {
        this(name, null, dataType, length, false, optional);
    }

    public Argument(String name, String defaultValue, DataType dataType, int length, boolean optional) {
        this(name, defaultValue, dataType, length, false, optional);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = null, defaultValue = null;
        private DataType dataType = DataType.STRING;
        private int length = 1;
        private boolean infiniteLength, optional = false;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;

            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;

            return this;
        }

        public Builder dataType(DataType dataType) {
            this.dataType = dataType;

            return this;
        }

        public Builder length(int length) {
            this.length = length;

            return this;
        }

        public Builder infiniteLength() {
            this.infiniteLength = true;

            return this;
        }

        public Builder optional() {
            return optional(true);
        }

        public Builder optional(boolean optional) {
            this.optional = optional;

            return this;
        }

        public Argument build() {
            if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null nor empty");

            return new Argument(name, defaultValue, dataType, length, infiniteLength, optional);
        }
    }
}