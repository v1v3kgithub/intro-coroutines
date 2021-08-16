package samples

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val channel = Channel<String>()
    launch {
        channel.send("A1")
        channel.send("A2")
        log("A Done")
    }
    launch {
        channel.send("B1")
        log("B Done")
    }
    launch {
        repeat(3) {
            val x = channel.receive();
            log(x)
        }
    }
}