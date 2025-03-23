package ru.javapractice.dailylunchvoting.service;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MatcherFactory {

    private MatcherFactory() {
    }

    public static <T>Matcher<T> usingIgnoringFieldsComparator(String... fieldsToIgnore){
        return new Matcher<T>(fieldsToIgnore);
    }

    public static class Matcher<T> {

        private final String[] fieldsToIgnore;

        public Matcher(String... fieldsToIgnore) {
            this.fieldsToIgnore = fieldsToIgnore;
        }

        public void assertMatch(T actual, T expected) {
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(expected);
        }

        @SafeVarargs
        public final void assertMatch(Iterable<T> actual, T... expected) {
            assertMatch(actual, Arrays.asList(expected));
        }

        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(expected);
        }
    }
}
