package find.my.train.domain.model

data class SearchHistory(
    val id: Long = 0,
    val query: String,      // Train Number/Name or "NDLS - BSB"
    val type: String,       // "TRAIN" or "STATIONS"
    val timestamp: Long
)
