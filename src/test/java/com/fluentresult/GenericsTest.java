package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    @Test
    void handle_exceptionMapperAcceptingThrowable_canBeUsed() {
        Function<Throwable, String> mapper = Throwable::getMessage;
        Result<String, String> result = Result.handle(() -> "value", mapper);
        OptionalResult<String, String> optionalResult = OptionalResult.handle(() -> Optional.of("value"), mapper);
        BooleanResult<String> booleanResult = BooleanResult.handle(() -> true, mapper);
        VoidResult<String> voidResult = VoidResult.handle(() -> {}, mapper);
    }

    @Test
    void booleanResult_functionsAcceptingSupertypeOfBoolean_canBeUsed() {
        BooleanResult<String> result = BooleanResult.successTrue();
        Consumer<Object> consumer = o -> {};
        Function<Object, String> function = Object::toString;
        Predicate<Object> predicate = Objects::nonNull;
        result.consume(consumer);
        result.consumeEither(consumer, e -> {});
        Result<String, String> mapped = result.map(function);
        String folded = result.fold(function, e -> e);
        result.verify(predicate, () -> "error");
        result.verify(predicate, function);
        Function<Boolean, Optional<SubType>> optionalFunction = b -> Optional.of(new SubType());
        OptionalResult<Type, String> optionalResult = result.mapToOptional(optionalFunction);
    }

    @Test
    void optionalResult_flatMapValue_functionsReturningSubtypes_canBeUsed() {
        OptionalResult<Type, Type> result = OptionalResult.success(new SubType());
        Function<ParentType, Result<SubType, SubType>> resultFunction = p -> Result.success(new SubType());
        OptionalResult<Type, Type> mappedWithResult = result.flatMapValueWithResult(resultFunction);
        Function<ParentType, OptionalResult<SubType, SubType>> optionalResultFunction = p -> OptionalResult.success(new SubType());
        OptionalResult<Type, Type> mappedWithOptionalResult = result.flatMapValueWithOptionalResult(optionalResultFunction);
        Function<ParentType, BooleanResult<SubType>> booleanResultFunction = p -> BooleanResult.successTrue();
        OptionalResult<Boolean, Type> mappedWithBooleanResult = result.flatMapValueWithBooleanResult(booleanResultFunction);
    }

    @Test
    void optionalResult_orElse_optionalOfSubtype_canBeUsed() {
        OptionalResult<Type, String> result = OptionalResult.empty();
        Optional<SubType> other = Optional.of(new SubType());
        Optional<Type> orElse = result.orElse(other);
        Function<String, Optional<SubType>> function = e -> Optional.of(new SubType());
        Optional<Type> orElseGet = result.orElseGet(function);
    }

    private static class ParentType {}
    private static class Type extends ParentType {}
    private static class SubType extends Type {}

}
