Click for Video Explainer:

[<img src="assets/splash.png" width="500"/>](https://youtu.be/XXX)

# The Difference between `derivedStateOf` and `remember` in JetBrains Compose

> [!TIP]
> ### $\textcolor{yellow}{THE\ BIG\ IDEA}$ - Can we debounce and throttle recompositions based on the _RESULTS_ of a calculation, and not it's inputs (like with `remember`)?

## Introduction
In this article, we will discuss the difference between `derivedStateOf` and `remember` in JetBrains Compose.

Note: This is an 
Android app, but the same principles apply to Compose for Desktop and iOS.

## `remember`
- `remember` is used to store a value that will not change across recompositions.
- When the `key` is changed, the value is changed and initiates a recomposition.

## `derivedStateOf`
- `derivedStateOf` is used to store a value that will not change across recompositions, and only change
when the _RESULT_ of a calculation changes.
- When the `key` is changed, a recomposition is not initiated unless the _RESULT_ of the calculation changes.
- This is useful for optimizing changes in the UI, like for `LazyLists` needing to update after the user stops scrolling.

## THIS IS ONLY AN OPTIMIZATION TECHNIQUE
- Instead of allowing a recomposition to occur every time a calculation is made, we can use `derivedStateOf` to
only allow a recomposition to occur when the _RESULT_ of a calculation changes.

# Sample Code
- There are 2 examples in the sample code.
- The first shows the common use-case using a LazyColumn.
- The second shows that for non-lazyList use-cases, there is no difference between `remember` and `derivedStateOf`.

[Click for Sample Code](app/src/main/java/com/realityexpander/derivedstateof_vs_remember/MainActivity.kt)
