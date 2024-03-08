@file:Suppress("LocalVariableName", "FunctionName")

package com.realityexpander.derivedstateof_vs_remember

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.realityexpander.derivedstateof_vs_remember.ui.theme.DerivedStateOf_vs_RememberTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

// Based on the StackOverflow question:
// https://stackoverflow.com/questions/70144298/compose-remember-with-keys-vs-derivedstateof

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DerivedStateOf_vs_RememberTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Example1()  // ❇️ Common use-case for optimizing `LazyLists`.

                    //Example2()  // ❇️ For non-lazy-lists, there is seems to be no need to use `derivedStateOf`.
                }
            }
        }
    }
}

// ❇️ For LazyLists, use `derivedStateOf` to avoid unnecessary recompositions.
@OptIn(ExperimentalFoundationApi::class) // for `stickyHeader`
@Composable
fun Example1() {
    val state = rememberLazyListState()

    var isEnabled by remember {
        mutableStateOf(true)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            ScrollToTopButton(
                state = state,
                isEnabled = isEnabled
            )
        }
    ) { padding ->

        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            stickyHeader("Header") {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { isEnabled = !isEnabled },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = if (isEnabled) "Disable ScrollToTop" else "Enable ScrollToTop",
                        )
                    }
                }
            }

            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            isEnabled = false
                        }
                )
            }
        }
    }
}

@Composable
fun ScrollToTopButton(
    state: LazyListState,
    isEnabled: Boolean
) {
    val scope = rememberCoroutineScope()

    // ⚠️ Before - updates on every scroll recomposition, does not matter if anything changed.
    val showScrollToTopButton = remember(state.firstVisibleItemIndex, isEnabled) {
        state.firstVisibleItemIndex >= 3
    }

    // `derivedStateOf` "De-bounces" all changes until user finishes scrolling, then recomposes only once.
    /*
    // ☑️ Better - Only recompose when the RESULTS of the `derivedStateOf` changes.
    //           - ⚠️ The problem is NO recompose when `isEnabled` changes.
    val showScrollToTopButton by remember {
        derivedStateOf {
            state.firstVisibleItemIndex >= 3 && isEnabled
        }
    }
    */

    // Reactive to changes in `isEnabled`.
    /*
    // ☑️ Better - Add another parameter to recompose when `isEnabled` changes.
//    val showScrollToTopButton by remember(isEnabled) {
//        derivedStateOf {
//            state.firstVisibleItemIndex >= 3 && isEnabled
//        }
//    }
     */

    if(showScrollToTopButton) {
        FloatingActionButton(
            onClick = { scope.launch { state.animateScrollToItem(0) } }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Scroll to top"
            )
        }
    }
}


// ❇️ Except for lazy-lists and "bursty" (rapidly changing) values, there seems to be no need
//    to use `derivedStateOf`.
@Composable
fun Example2() {
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        var value by remember { mutableIntStateOf(0) }

        // 🔸 using `remember`
        val remember_isValueEven = remember(value) {
            value % 2 == 0
        }

        // 🔸 using `derivedStateOf`
        val derived_isValueEven by remember {
            derivedStateOf {
                value % 2 == 0
            }
        }

        // ➕ Increment
        Button(
            onClick = {
                scope.launch {
                    repeat(10) {
                        value++
                        yield()
//                        delay(5)
                    }
                }
            }
        ) {
            Text("Increment value: $value")
        }
        // ➖ Decrement
        Button(
            onClick = {
                scope.launch {
                    repeat(10) {
                        value--
                        yield()
//                        delay(5)
                    }
                }
            }
        ) {
            Text("Decrement")
        }

        Text("remember_isValueEven: $remember_isValueEven")
        Text("derived_isValueEven: $derived_isValueEven")
    }
}
