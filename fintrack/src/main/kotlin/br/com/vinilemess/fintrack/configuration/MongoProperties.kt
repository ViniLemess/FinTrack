package br.com.vinilemess.fintrack.configuration

data class MongoProperties(
    val host: String,
    val database: String,
    val username: String,
    val password: String,
    val authenticateAsAdmin: Boolean = false,
    var connectionString: String?
) {

    fun getConnectionStr(): String {
        this.connectionString?.let { return it }
        return if (authenticateAsAdmin) {
            "mongodb://$username:$password@$host/$database?authSource=admin"
        } else {
            "mongodb://$username:$password@$host/$database"
        }
    }
}