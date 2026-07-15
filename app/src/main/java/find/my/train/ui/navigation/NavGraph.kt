package find.my.train.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import find.my.train.ui.betweenstations.BetweenStationsScreen
import find.my.train.ui.coach.CoachPositionScreen
import find.my.train.ui.home.HomeScreen
import find.my.train.ui.livestatus.LiveStatusScreen
import find.my.train.ui.pnr.PnrScreen
import find.my.train.ui.saved.SavedScreen
import find.my.train.ui.schedule.ScheduleScreen
import find.my.train.ui.search.SearchScreen
import find.my.train.ui.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(Screen.TrainSearch.route) },
                onNavigateToLiveStatus = { number -> navController.navigate(Screen.LiveStatus.createRoute(number)) },
                onNavigateToBetweenStations = { source, dest -> navController.navigate(Screen.BetweenStations.createRoute(source, dest)) },
                onNavigateToPnr = { navController.navigate(Screen.PnrStatus.route) },
                onNavigateToSchedule = { number -> navController.navigate(Screen.TrainSchedule.createRoute(number)) },
                onNavigateToCoach = { number -> navController.navigate(Screen.CoachPosition.createRoute(number)) },
                onNavigateToSaved = { navController.navigate(Screen.SavedTrains.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.TrainSearch.route) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onTrainClick = { number -> navController.navigate(Screen.LiveStatus.createRoute(number)) }
            )
        }

        composable(
            route = Screen.LiveStatus.route,
            arguments = listOf(navArgument("trainNumber") { type = NavType.StringType })
        ) {
            LiveStatusScreen(
                onBackClick = { navController.popBackStack() },
                onScheduleClick = { number -> navController.navigate(Screen.TrainSchedule.createRoute(number)) },
                onCoachClick = { number -> navController.navigate(Screen.CoachPosition.createRoute(number)) }
            )
        }

        composable(
            route = Screen.BetweenStations.route,
            arguments = listOf(
                navArgument("source") { type = NavType.StringType; defaultValue = "" },
                navArgument("destination") { type = NavType.StringType; defaultValue = "" }
            )
        ) {
            BetweenStationsScreen(
                onBackClick = { navController.popBackStack() },
                onTrainClick = { number -> navController.navigate(Screen.LiveStatus.createRoute(number)) }
            )
        }

        composable(Screen.PnrStatus.route) {
            PnrScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TrainSchedule.route,
            arguments = listOf(navArgument("trainNumber") { type = NavType.StringType })
        ) {
            ScheduleScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CoachPosition.route,
            arguments = listOf(navArgument("trainNumber") { type = NavType.StringType })
        ) {
            CoachPositionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SavedTrains.route) {
            SavedScreen(
                onBackClick = { navController.popBackStack() },
                onTrainClick = { number -> navController.navigate(Screen.LiveStatus.createRoute(number)) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
