package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> =
    // This method itself defines a scope (parent), the coroutineScope method creates a new scope without actually starting a scope
    coroutineScope {
    // Why do you need "= coroutineScope {" in the method signature ;This is because async can only be called within CoroutineScope
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList();
    val deffered = repos.map { repo ->
        async {
            // Child Scope of the outer scope (child)
            log("starting loading for ${repo.name}") // This logging will clearly show that the coroutine has stated, but suspended due to the following call.
            delay(3000) // Artifical delay to allow for cancellation.
            service.getRepoContributors(req.org,repo.name)
                .also { logUsers(repo,it) }
                .bodyList()
        }
    }
    deffered.awaitAll().flatten().aggregate();

}
