package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    // Loading data using async on a seperate thread (as opposed to from the UI thread) , but we are still loading all the data in a sequential manner
    val users = async(Dispatchers.Default) {
        loadContributorsSuspend(service,req)
    }
    users.await()
}
