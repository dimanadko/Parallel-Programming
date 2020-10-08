import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.math.ceil
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

fun getSplits(vectorLength: Int, numberOfThreads: Int): List<Int> {
    val splites:MutableList<Int> = mutableListOf();
    val perKorutine = ceil(vectorLength.toDouble() / numberOfThreads).toInt()
    var currentIndex = perKorutine;
    splites.add(0)
    while(currentIndex<vectorLength){
        splites.add(currentIndex)
        currentIndex+=perKorutine
    }
    splites.add(vectorLength)
    return splites
}

fun calculateEuclideanNorm(vector: List<Any>, begin:Int, end:Int):BigInteger {
    var euclideanNormSqr:BigInteger = (0).toBigInteger()
    for (param in begin until end){
        val value = vector[param]
        if (value is BigInteger) {
            euclideanNormSqr = euclideanNormSqr.plus(value.times(value))
        }
        if (value is Int) {
            euclideanNormSqr = euclideanNormSqr.plus((value*value).toBigInteger())
        }
    }
    return euclideanNormSqr.sqrt()
}

fun main(args: Array<String>) {
    val ran = java.util.Random()
    val NUMBER_OF_TESTS = 1000;
    val NUMBER_OF_KORUTINES = 4;
    val VECTOR_LEGTH:Int = 10_000;
    val sequentialTimes = mutableListOf<Long>()
    val concurentTimes = mutableListOf<Long>()
    val vector= ran.ints(VECTOR_LEGTH.toLong(), -10, 10).toList()
    val splitsList = getSplits(VECTOR_LEGTH, NUMBER_OF_KORUTINES)
    for( test in 0 until NUMBER_OF_TESTS) {
        sequentialTimes.add(
            measureTimeMillis {
                val result = calculateEuclideanNorm(vector, 0, VECTOR_LEGTH)
//                println("Result: $result")
            }
        )
    }
    for( test in 0 until NUMBER_OF_TESTS) {
        concurentTimes.add(
            measureTimeMillis {
                val subNormsDeferred: MutableList<Deferred<BigInteger>> = mutableListOf();
                for (i in 0 until NUMBER_OF_KORUTINES) {
                    val current = GlobalScope.async { calculateEuclideanNorm(vector, splitsList[i], splitsList[i + 1]) }
                    subNormsDeferred.add(current)
                }
                runBlocking {
                    val subNorms = subNormsDeferred.awaitAll()
                    val result = calculateEuclideanNorm(subNorms, 0, subNorms.size)
//                    println("Result: $result")
                }
            }
        )
    }
    println("Sequential completed in ${sequentialTimes.average()} ms")
    println("Concurrent completed in ${concurentTimes.average()} ms")
    val speedupFactor = sequentialTimes.average()/concurentTimes.average()
    println("Speedup Factor $speedupFactor")
    println("Efficiency ${speedupFactor/NUMBER_OF_KORUTINES}")
}