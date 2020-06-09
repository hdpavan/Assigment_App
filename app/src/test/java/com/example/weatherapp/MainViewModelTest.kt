package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.api.LocationRepository
import com.example.weatherapp.api.Repository
import com.example.weatherapp.model.persistence.Database
import com.example.weatherapp.model.persistence.entity.WeatherInfo
import com.example.weatherapp.viewmodel.MainActivityViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock


@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var repository: Repository

    lateinit var mainViewModel: MainActivityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        mainViewModel = MainActivityViewModel(repository)
    }

    @Test
    fun testFetchRepositories_Positive() {

        testDispatcher.runBlockingTest {
            coEvery { repository.getALL() } returns listOf(
                WeatherInfo(
                    1,
                    "{'coord':{'lon':-6.25,'lat':53.36},'weather':[{'id':803,'main':'Clouds','description':'broken clouds','icon':'04n'}],'base':'stations','main':{'temp':278.98,'feels_like':272.07,'temp_min':278.15,'temp_max':279.82,'pressure':1031,'humidity':70},'visibility':10000,'wind':{'speed':7.2,'deg':70},'clouds':{'all':75},'dt':1584732075,'sys':{'type':1,'id':1565,'country':'IE','sunrise':1584685597,'sunset':1584729484},'timezone':0,'id':7778677,'name':'Dublin City','cod':200}"
                )
            )

            mainViewModel.jsonResponse.observeForever {}

            mainViewModel.fetchFromData()
            assert(mainViewModel.jsonResponse.value != null)
        }
    }

    @Test
    fun testFetchRepositories_Error() {

        testDispatcher.runBlockingTest {
            coEvery { repository.getALL() } returns listOf()

            mainViewModel.jsonResponse.observeForever {}

            mainViewModel.fetchFromData()
            assert(mainViewModel.jsonResponse.value == null)
        }
    }
}