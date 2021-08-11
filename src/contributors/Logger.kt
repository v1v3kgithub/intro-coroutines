package contributors

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Response

val log: Logger = LoggerFactory.getLogger("Contributors")

fun log(msg: String?) {
    log.info(msg)
}

fun logRepos(req: RequestData, response: Response<List<Repo>>) {
    val repos = response.body()
    if (!response.isSuccessful || repos == null) {
        log.error("Failed loading repos for ${req.org} with response: '${response.code()}: ${response.message()}'")
    }
    else {
        val repoNames = StringBuilder().append(":")
        repos.forEach { repoNames.append(it.name).append(",") }
        log.info("${req.org}: loaded ${repos.size} repos;  $repoNames");
        log.info("${req.org}: Repo Names;  $repoNames");
        log.info("=================================================");
    }
}

fun logUsers(repo: Repo, response: Response<List<User>>) {
    val users = response.body()
    if (!response.isSuccessful || users == null) {
        log.error("Failed loading contributors for ${repo.name} with response '${response.code()}: ${response.message()}'")
    }
    else {
        log.info("${repo.name}: loaded ${users.size} contributors")
    }
}