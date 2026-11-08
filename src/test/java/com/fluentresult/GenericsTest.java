package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

class GenericsTest {

    @Test
    void result_map() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, SubType> function = p -> new SubType();
        Result<Type, Type> mapped = result.map(function);
    }

    @Test
    void result_successToSuccess_flatMap() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, Result<SubType, Type>> function = p -> Result.success(new SubType());
        Result<Type, Type> mapped = result.flatMap(function);
    }

    @Test
    void result_errorToSuccess_flatMap() {
        Result<Type, Type> result = Result.error(new SubType());
        Function<ParentType, Result<SubType, Type>> function = p -> Result.success(new SubType());
        Result<Type, Type> mapped = result.flatMap(function);
    }

    @Test
    void result_successToError_flatMap() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, Result<SubType, Type>> function = p -> Result.error(new SubType());
        Result<Type, Type> mapped = result.flatMap(function);
    }

    @Test
    void optionalResult_successToSuccess_map() {
        OptionalResult<Type, Type> result = OptionalResult.success(new SubType());
        Function<Optional<Type>, Optional<SubType>> function = p -> Optional.of(new SubType());
        OptionalResult<Type, Type> mapped = result.mapToOptional(function);
    }

    @Test
    void result_mapToOptional() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, Optional<SubType>> function = p -> Optional.of(new SubType());
        OptionalResult<Type, Type> mapped = result.mapToOptional(function);
    }

    @Test
    void result_flatMapToOptionalResult() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, OptionalResult<SubType, SubType>> function = p -> OptionalResult.success(new SubType());
        OptionalResult<Type, Type> mapped = result.flatMapToOptionalResult(function);
    }

    @Test
    void result_handle_callable() {
        Callable<SubType> callable = SubType::new;
        Result<Type, Exception> handled = Result.handle(callable);
        Result<Type, Type> mapped = Result.handle(callable, e -> new SubType());
    }

    @Test
    void booleanResult_recover() {
        BooleanResult<SubType> result = BooleanResult.error(new SubType());
        Function<ParentType, Boolean> function = p -> true;
        BooleanResult<SubType> recovered = result.recover(function);
    }

    @Test
    void optionalResult_recover() {
        OptionalResult<Type, SubType> result = OptionalResult.error(new SubType());
        Function<ParentType, Optional<SubType>> function = p -> Optional.of(new SubType());
        OptionalResult<Type, SubType> recovered = result.recover(function);
    }

    @Test
    void optionalResult_flatMapToOptionalResult() {
        OptionalResult<Type, Type> result = OptionalResult.success(new SubType());
        Function<Object, OptionalResult<SubType, SubType>> function = o -> OptionalResult.empty();
        OptionalResult<Type, Type> mapped = result.flatMapToOptionalResult(function);
    }

    @Test
    void optionalResult_handle_callable() {
        Callable<Optional<SubType>> callable = () -> Optional.of(new SubType());
        OptionalResult<Type, Exception> handled = OptionalResult.handle(callable);
        OptionalResult<Type, Type> mapped = OptionalResult.handle(callable, e -> new SubType());
    }

    private static class ParentType {}
    private static class Type extends ParentType {}
    private static class SubType extends Type {}

}
