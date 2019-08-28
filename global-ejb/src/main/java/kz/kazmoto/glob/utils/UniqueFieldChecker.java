package kz.kazmoto.glob.utils;

import kz.kazmoto.glob.exceptions.UniqueFieldCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UniqueFieldChecker<T> {
    private List<Checker> checkers = new ArrayList<>();

    public UniqueFieldChecker<T> addChecker(String name, Function<T,T> function){
        checkers.add(new Checker(name, function));
        return this;
    }

    public void validate(T t, boolean update){
        for (Checker checker : checkers) {
            T exist = checker.function.apply(t);
            if(exist != null && (!update || !t.equals(exist))){
                throw new UniqueFieldCodeException("field "+checker.name+" already exist");
            }
        }
    }

    private class Checker{
        Checker(String name, Function<T, T> function) {
            this.name = name;
            this.function = function;
        }
        String name;
        Function<T,T> function;
    }
}
