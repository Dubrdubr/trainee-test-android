package ru.dubr.traineetestandroid.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.dubr.traineetestandroid.data.network.dto.CoinDto
import ru.dubr.traineetestandroid.domain.Coin
import ru.dubr.traineetestandroid.domain.repository.CoinRepository
import ru.dubr.traineetestandroid.utils.Resource
import java.io.IOException
import javax.inject.Inject

class GetAllCoinsUseCase @Inject constructor(
    private val repository: CoinRepository,
) {

    operator fun invoke(currency: String): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())
        try {
            val coinList = repository.getAllCoins(currency).map { it.toCoin(currency) }
            emit(Resource.Success(coinList))
        } catch (e: HttpException) {
            emit(Resource.Error("${e.code()}. ${e.message}")) // TODO: обернуть ошибку
        } catch (e: IOException) {
            emit(Resource.Error(""))
        }
    }

    private fun CoinDto.toCoin(currency: String): Coin {
        return Coin(
            id = id,
            symbol = symbol,
            name = name,
            image = image,
            currentPrice = currentPrice,
            priceChangePercentage24h = priceChangePercentage24h,
            currency = currency
        )
    }

}