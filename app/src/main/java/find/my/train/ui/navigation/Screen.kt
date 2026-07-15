package find.my.train.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TrainSearch : Screen("train_search")
    
    object LiveStatus : Screen("live_status/{trainNumber}") {
        fun createRoute(trainNumber: String) = "live_status/$trainNumber"
    }
    
    object BetweenStations : Screen("between_stations?source={source}&destination={destination}") {
        fun createRoute(source: String, destination: String) = "between_stations?source=$source&destination=$destination"
    }
    object PnrStatus : Screen("pnr_status")
    
    object TrainSchedule : Screen("train_schedule/{trainNumber}") {
        fun createRoute(trainNumber: String) = "train_schedule/$trainNumber"
    }
    
    object CoachPosition : Screen("coach_position/{trainNumber}") {
        fun createRoute(trainNumber: String) = "coach_position/$trainNumber"
    }
    
    object SavedTrains : Screen("saved_trains")
    object Settings : Screen("settings")
}
