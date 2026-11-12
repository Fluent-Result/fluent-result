# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- **Breaking:** `BooleanResult.orElse`, `orElseGet` and `orElseThrow` now take and return primitive `boolean` instead of `Boolean`. `orElseGet` throws `NullPointerException` if the given function returns `null`.
- Widened functional parameters consistently across `Result`, `OptionalResult`, `BooleanResult` and `VoidResult` to accept `? super` input types and `? extends` return types. This includes the exception mapper of `handle`, all success-side parameters of `BooleanResult`, the `flatMapValueWith*` methods of `OptionalResult`, and `OptionalResult.orElse`/`orElseGet` which now accept `Optional<? extends T>`.

## [1.8.0] - 2026-11-03
### Changed
- **Package name change** base package changed from `com.fluentresult.fluentresult` to `com.fluentresult`.

## [1.7.0] - 2026-10-31
### Changed
- **Namespace Migration:** This project has been moved to a new GitHub repository and a new Maven Central group ID:
    - **Old Group ID:** `no.gorandalum.fluent-result`
    - **New Group ID:** `com.fluent-result.fluent-result`
    - **Old Repository:** [gorandalum/fluent-result](https://github.com/gorandalum/fluent-result)
    - **New Repository:** [fluent-result/fluent-result](https://github.com/fluent-result/fluent-result)

### Added

- Pass validated value to error constructor for `verify`.

## [1.6.0] - 2025-04-29

### Added

- New methods `recover` and `flatRecover`
- New methods `flatReplaceEmpty` and `flatReplaceEmptyWithResult`

## [1.5.0] - 2020-01-16

### Added

- New method `flatConsume`
- New method `flatRunIfSuccess`
- New method `runAlways`, same as `run`

### Changed

- Made method `run` deprecated

## [1.4.0] - 2019-12-20

### Changed

- Compile with jdk8

## [1.3.0] - 2019-12-11

### Added

- Method `handle` for initializing Result-objects

## [1.2.0] - 2018-10-30

### Changed

- Compile with jdk8

## [1.1.0] - 2018-10-14

### Added

- Verification by using VoidResult

### Changed

- Renamed `flatten` to `toResult`

[1.8.0]: https://github.com/fluent-result/fluent-result/compare/v1.7.0...v1.8.0
[1.7.0]: https://github.com/fluent-result/fluent-result/compare/v1.6.0...v1.7.0
[1.6.0]: https://github.com/gorandalum/fluent-result/compare/v1.5.0...v1.6.0
[1.5.0]: https://github.com/gorandalum/fluent-result/compare/v1.4.0...v1.5.0
[1.4.0]: https://github.com/gorandalum/fluent-result/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/gorandalum/fluent-result/compare/v1.2.0...v1.3.0
[1.2.0]: https://github.com/gorandalum/fluent-result/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/gorandalum/fluent-result/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/gorandalum/fluent-result/releases/tag/v1.0.0
