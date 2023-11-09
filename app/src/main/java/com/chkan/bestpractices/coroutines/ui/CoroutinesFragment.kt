package com.chkan.bestpractices.coroutines.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.chkan.bestpractices.R
import com.chkan.bestpractices.core.BaseFragment
import com.chkan.bestpractices.databinding.FragmentCoroutinesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*

@AndroidEntryPoint
class CoroutinesFragment : BaseFragment<FragmentCoroutinesBinding>(FragmentCoroutinesBinding::inflate) {

    val context = Job() + Dispatchers.Default
    val scope = CoroutineScope(context)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button5.setOnClickListener {
            findNavController().navigate(R.id.action_coroutinesFragment_to_coroutinesExampleFragment)
        }

        binding.button.setOnClickListener {
            // пример работы передачи контекста
          val userData = UserData(1, "name1", 10)

          scope.launch {
              log("coroutine, level1, ${contextToString(coroutineContext)}")

              launch (Dispatchers.Main + userData) {
                  log("coroutine, level2, ${contextToString(coroutineContext)}")
                  val innerUserData = coroutineContext[UserData]
                  log("coroutine, userData, $innerUserData")

                  launch {
                      log("coroutine, level3, ${contextToString(coroutineContext)}")
                  }
              }
          }
        }

        binding.button2.setOnClickListener {
            val scopeDispatchers = CoroutineScope(Dispatchers.Default)
            /*
       примеры работы c диспетчерами
       https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/606-urok-11-korutiny-dispatcher.html

       Если стартануть с Main то результат тоже переключиться на Main
       start coroutine [main]
       suspend function, start [main]
       suspend function, background work [Thread-2]
       end coroutine [main]

       Если стартануть с Default или IO то результат тоже переключиться на другой не стартовый поток
       start coroutine [DefaultDispatcher-worker-1]
       suspend function, start [DefaultDispatcher-worker-1]
       suspend function, background work [Thread-5]
       end coroutine [DefaultDispatcher-worker-3]

       Если стартануть с Unconfined то рабочий поток не переключиться для результата
       start coroutine [DefaultDispatcher-worker-1]
       suspend function, start [DefaultDispatcher-worker-1]
       suspend function, background work [Thread-5]
       end coroutine [Thread-5]
       */
            scopeDispatchers.launch {
                log("start coroutine")
                val data = getData()
                log("end coroutine")
            }
        }

        binding.button3.setOnClickListener {
            // пример связи между родительской и дочерней корутиной
            // код родительской корутины выполняется сразу и не ждет выполнения дочерней
            // связь в том, что даже если родительская корутина выполнила весь свой код, она будет НЕ завершена (job.isActive - true), пока не завершились ли ее дочерние корутины
            // Вызов метода cancel для родительской корутины каскадно отменит и все ее дочерние корутины. Но метод cancel сработает только для корутины, которая еще не завершена.
            // Поэтому родительская корутина и ждет завершения дочерних, чтобы мы всегда могли отменить дочерние с помощью родительской.
            scope.launch {
                log("parent start")
                launch {
                    log("child start")
                    delay(1000)
                    log("child end")
                }
                log("parent end")
            }
        }

        binding.button4.setOnClickListener {
            // обработка ошибок
            /*
            try {
                scope.launch {
                    Integer.parseInt("a")
                }...
    Так НЕ словится, потому что try "видит" только работу запуска лаунча, а сам код уходит в другой поток, который try уже не "видит"

    Рабочий вариант - поместить в try-catch код, который выбрасывает ошибку:
    scope.launch {
    try {
        Integer.parseInt("a")
    } catch (e: Exception) {
        log("exception $e")
    }
}
или CoroutineExceptionHandler

ВАЖНО:
* Даже несмотря на обработчик ошибок, корутина, в которой произошло исключение, сообщит об ошибке в родительский scope,
а тот отменит все свои дочерние корутины. Это может быть удобным, если вам при возникновении ошибки в одной операции,
надо отменить другие операции. Вы просто помещаете корутины с этими операциями в один scope.
*/

            val handler = CoroutineExceptionHandler { context, exception ->
                log("handled $exception")
            }

            scope.launch(handler) {
                Integer.parseInt("a")
            }

            val handlerSupervisor = CoroutineExceptionHandler { context, exception ->
                log("first coroutine exception $exception")
            }

            /*
            Scope отменяет своих детей, когда в одном из них происходит ошибка. Пусть даже эта ошибка и была передана в обработчик.
            Такое поведение родителя далеко не всегда может быть удобным. Поэтому у нас есть возможность это отключить через SupervisorJob()

            лог:

14:34:36.641 second coroutine isActive true
14:34:36.943 second coroutine isActive true
14:34:37.244 second coroutine isActive true
14:34:37.280 first coroutine exception java.lang.NumberFormatException: For input string: "a"
14:34:37.546 second coroutine isActive true
14:34:37.955 second coroutine isActive true

            */


            val scopeSupervisor = CoroutineScope(SupervisorJob() + Dispatchers.Default + handlerSupervisor)

            scopeSupervisor.launch {
                TimeUnit.MILLISECONDS.sleep(1000)
                Integer.parseInt("a")
            }

            scopeSupervisor.launch {
                repeat(5) {
                    TimeUnit.MILLISECONDS.sleep(300)
                    log("second coroutine isActive $isActive")
                }
            }

            //Обработка исключений во вложенных корутинах
            /*
            https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/609-urok-14-korutiny-obrabotka-isklyucheniy-vlozhennye-korutiny.html

            Когда корутина сообщает родителю об ошибке, она передает ему эту ошибку и спрашивает, сможет ли родитель ошибку обработать. Ответ родителя зависит от того, кем этот родитель является - скоупом или корутиной.
            Если родитель - скоуп, то он отвечает, что не сможет. Поэтому корутина пытается это сделать сама - в обработчик CoroutineExceptionHandler или краш.

            А если родитель - корутина, то она отвечает, что сможет. И дочерняя корутина вместо всевозможных обработчиков передает ошибку родительской корутине.
            Родительская корутина, хоть и сказала, что обработать сможет, но сначала сама спрашивает у своего родителя, сможет ли он обработать. И если может, то передает ему ошибку.
            Ошибка поднимается наверх по иерархии вложенных корутин, пока не достигнет самой верхней корутины. Ее родитель - это скоуп.
            Когда самая верхняя корутина спросит его, он скажет, что ошибку обработать не сможет. И в итоге самая верхняя корутина отправляет ошибку в обработчики.

            При этом каждый родитель, через который прошла ошибка, будет отменяться сам и отменять все свои дочерние корутины.

            Т.е. CoroutineExceptionHandler сработает только в самой верхней корутине. Остальные корутины просто передают ошибку родительской корутине и сами ничего с ней не делают.

            Здесь SupervisorJob в scope помогает только частично. Он останавливает распространение ошибки на другие корутины этого scope.
            Если нужно чтобы некоторые корутины не останавливали другие при ошибке - https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/612-urok-17-korutiny-coroutinescope-supervisorscope-withcontext-runblocking.html

            */

        }

        binding.button6.setOnClickListener {
            findNavController().navigate(R.id.action_coroutinesFragment_to_coroutinesScenariosFragment)
        }

        binding.btnSync.setOnClickListener {
            testSync()
        }

    }
    private fun testSync() {
        log("START")
        var result  = ""
        mainScope.launch {
            log("parent coroutine, start")

            val deferred = async() {
                log("child coroutine, start")
                TimeUnit.MILLISECONDS.sleep(1000)
                log("child coroutine, end")
                "async result"
            }

            log("parent coroutine, wait until child returns result")
            result = deferred.await()
            log("parent coroutine, child returns: $result")

            log("parent coroutine, end")
        }
        log("FINISHED + result: $result")
    }

    private suspend fun getData(): String =
        suspendCoroutine {
            log("suspend function, start")
            thread {
                log("suspend function, background work")
                TimeUnit.MILLISECONDS.sleep(1000)
                it.resume("Data!")
            }
        }

    private fun contextToString(context: CoroutineContext): String =
        "Job = ${context[Job]}, Dispatcher = ${context[ContinuationInterceptor]}"

    private fun log(text: String){
        Log.d("CHKAN", "$text [${Thread.currentThread().name}]")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}

//чтобы иметь возможность поместить объект этого класса в контекст
data class UserData(
    val id: Long,
    val name: String,
    val age: Int
): AbstractCoroutineContextElement(UserData) {
    companion object Key : CoroutineContext.Key<UserData>
}