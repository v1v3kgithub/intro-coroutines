package tasks

import contributors.*

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()
    var allUsers = emptyList<User>()
    // All the calls are performed sequentially on a single thread, thus blocking this single thread.
    // Ideally the calls need to be concurrent and updating the UI only on complition of each coroutine.
    for ((index,repo) in repos.withIndex()) {
        val users = service.getRepoContributors(req.org,repo.name)
            .also { logUsers(repo,it) }
            .bodyList()
        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers,index == repos.lastIndex)
    }
}
