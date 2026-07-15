package find.my.train.domain.model

data class Train(
    val number: String,
    val name: String,
    val source: String,
    val destination: String,
    val sourceCode: String,
    val destinationCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val runningDays: List<String>,
    val trainType: String,
    val travelTime: String,
    val classes: List<String> = listOf("SL", "3A", "2A", "1A"),
    val isSaved: Boolean = false
)
