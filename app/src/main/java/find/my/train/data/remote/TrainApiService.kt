package find.my.train.data.remote

import find.my.train.data.remote.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrainApiService {
    @GET("trains/search")
    suspend fun searchTrains(@Query("q") query: String): List<TrainResponse>

    @GET("trains/{number}/live-status")
    suspend fun getLiveStatus(@Path("number") number: String): LiveStatusResponse

    @GET("trains/between-stations")
    suspend fun getTrainsBetweenStations(
        @Query("source") source: String,
        @Query("destination") destination: String,
        @Query("date") date: String
    ): List<TrainResponse>

    @GET("pnr/{pnrNumber}")
    suspend fun getPnrStatus(@Path("pnrNumber") pnrNumber: String): PnrResponse

    @GET("trains/{number}/coach-position")
    suspend fun getCoachPosition(@Path("number") number: String): CoachLayoutResponse
}
