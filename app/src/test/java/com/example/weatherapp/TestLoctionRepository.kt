package com.example.weatherapp

import android.app.ActivityManager
import android.content.Context
import android.location.LocationManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.api.LocationRepository
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.model.JSONResponse
import com.example.weatherapp.model.persistence.Database
import com.example.weatherapp.model.persistence.entity.WeatherInfo
import com.example.weatherapp.utility.PreferenceManager
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.*
import retrofit2.Response


class TestLoctionRepository {


    lateinit var locationRepository: LocationRepository

    @MockK
    lateinit var application: Context

    @MockK
    lateinit var apiService: WeatherApiService

    @MockK
    lateinit var database: Database

    @MockK
    lateinit var preferenceManager: PreferenceManager

    /*  @Mock
      lateinit var activityService: ActivityManager*/

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        //preferenceManager = PreferenceManager(application)

        locationRepository =
            LocationRepository(application, apiService, database, preferenceManager)
    }


    @Test
    fun makeAPIRequest_test() {

        testDispatcher.runBlockingTest {
            coEvery {
                apiService.fetchDataByLocation(
                    "10",
                    "10",
                    "abc"
                )
            } returns Response.success(getResponsedata())

            locationRepository.makeAPIRequest(10.0, 12.0)

            delay(10)

            assert(locationRepository.jsonResponse == null)
        }

    }

    fun getResponsedata(): JSONResponse {

        val data =
            "{'coord':{'lon':-6.25,'lat':53.36},'weather':[{'id':803,'main':'Clouds','description':'broken clouds','icon':'04n'}],'base':'stations','main':{'temp':278.98,'feels_like':272.07,'temp_min':278.15,'temp_max':279.82,'pressure':1031,'humidity':70},'visibility':10000,'wind':{'speed':7.2,'deg':70},'clouds':{'all':75},'dt':1584732075,'sys':{'type':1,'id':1565,'country':'IE','sunrise':1584685597,'sunset':1584729484},'timezone':0,'id':7778677,'name':'Dublin City','cod':200}"

        return Gson().fromJson(data, JSONResponse::class.java)

    }

}