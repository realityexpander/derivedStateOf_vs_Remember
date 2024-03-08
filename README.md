# The Difference between `derivedStateOf` and `remember` in JetBrains Compose

## Introduction
In this article, we will discuss the difference between `derivedStateOf` and `remember` in JetBrains Compose.

### BIG IDEA - What if we only want to recompose if the RESULT of a calculation changes, not the inputs like in `remember`?

## `remember`
- `remember` is used to store a value that will not change across recompositions.
- When the `key` is changed, the value is changed and initiates a recomposition.

## `derivedStateOf`
- `derivedStateOf` is used to store a value that will not change across recompositions, and only change
when the _RESULT_ of a calculation changes.
- When the `key` is changed, a recomposition is not initiated unless the _RESULT_ of the calculation changes.

## THIS IS ONLY AN OPTIMIZATION TECHNIQUE
- Instead of allowing a recomposition to occur every time a calculation is made, we can use `derivedStateOf` to
only allow a recomposition to occur when the _RESULT_ of a calculation changes.

# Sample Code
