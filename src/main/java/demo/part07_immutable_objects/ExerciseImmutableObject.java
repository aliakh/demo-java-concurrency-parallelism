package demo.part07_immutable_objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class ExerciseImmutableObject {

    private final String immutableField;
    private final List<String> mutableField;

    public ExerciseImmutableObject(String immutableField, List<String> mutableField) {
        this.immutableField = immutableField;
        this.mutableField = Collections.unmodifiableList(new ArrayList<>(mutableField));
    }

    public String getImmutableField() {
        return immutableField;
    }

    public List<String> getMutableField() {
        return Collections.unmodifiableList(mutableField);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add("immutableField='" + immutableField + "'")
                .add("mutableField=" + mutableField)
                .toString();
    }

    public static void main(String[] args) {
        String value = "Alpha";

        List<String> values = new ArrayList<>();
        values.add("Bravo");

        ExerciseImmutableObject object = new ExerciseImmutableObject(value, values);
        System.out.println("object: " + object);

        values.add("Charlie");
        System.out.println("object still the same: " + object);

        object.getMutableField().add("Delta"); // java.lang.UnsupportedOperationException
    }
}

/*
5. Immutability and Defensive Copies
5.1. Immutability
The simplest way to avoid problems with concurrency is to share only immutable data between threads. Immutable data is data which cannot changed.

To make a class immutable make

all its fields final

the class declared as final

the this reference is not allowed to escape during construction

Any fields which refer to mutable data objects are

private

have no setter method

they are never directly returned of otherwise exposed to a caller

if they are changed internally in the class this change is not visible and has no effect outside of the class

An immutable class may have some mutable data which is uses to manages its state but from the outside this class nor any attribute of this class can get changed.

For all mutable fields, e.g. Arrays, that are passed from the outside to the class during the construction phase, the class needs to make a defensive-copy of the elements to make sure that no other object from the outside still can change the data */