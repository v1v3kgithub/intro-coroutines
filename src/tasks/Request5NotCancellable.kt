package tasks

import contributors.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> {
    // Why do you need "= coroutineScope {" in the method signature ;This is because async can only be called within CoroutineScope
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList();
    val deffered = repos.map { repo ->
        GlobalScope.async {
            // Child Scope of the outer scope (child)
            log("starting loading for ${repo.name}") // This logging will clearly show that the coroutine has stated, but suspended due to the following call.
            delay(3000)
            service.getRepoContributors(req.org,repo.name)
                .also { logUsers(repo,it) }
                .bodyList()
        }
    }
    return deffered.awaitAll().flatten().aggregate();
}