package samples

import kotlinx.coroutines.*

fun main() = runBlocking {
    val deferreds: List<Deferred<Int>> = (1..4).map {
        async(Dispatchers.Default) {
            delay(1000L * it)
            log("Loading $it")
            it
        }
    }
    val sum = deferreds.awaitAll().sum();
    log("$sum")
}