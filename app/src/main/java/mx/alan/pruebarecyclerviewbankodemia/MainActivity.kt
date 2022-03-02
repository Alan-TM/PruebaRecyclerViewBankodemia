package mx.alan.pruebarecyclerviewbankodemia

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.TranslateAnimation
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import mx.alan.pruebarecyclerviewbankodemia.adapters.HomeTransactionsAdapter
import mx.alan.pruebarecyclerviewbankodemia.databinding.ActivityMainBinding
import mx.alan.pruebarecyclerviewbankodemia.model.TransactionEntity
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val dates = mutableSetOf<String>()
    val transactions = mutableListOf<TransactionEntity>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        llenarTransactionsAndDates()
        val list = sortTransactions(transactions)
        val itemsForRV = buildItems(list)
        itemsForRV.forEach {
            when  (it) {
                is TransactionListItem.DateItem ->
                    Log.e("Trans", it.date)
                is TransactionListItem.TransactionItem ->
                    Log.e("Trans", it.transaction.concept + " " + it.transaction.createdAt)
            }

        }

        binding.recycler.adapter = HomeTransactionsAdapter(applicationContext, itemsForRV)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.hasFixedSize()

    }

    fun parseDate(date: String): String{
        var newDate: String = ""
        for(c in date){
            if(c == 'T'){
                break
            } else{
                newDate += c
            }
        }
        return newDate
    }

    fun llenarTransactionsAndDates(){
        transactions.add(TransactionEntity(136.00, "DEPOSIT", "Pastel de mama", "2019-08-25T09:54:34Z",
        "Alan", "Gaby", true))
        transactions.add(TransactionEntity(89.99, "DEPOSIT", "Pago a OXXO", "2019-08-24T18:32:00Z",
            "Alan", "OXXO SA de CV", true))
        transactions.add(TransactionEntity(77.00, "DEPOSIT", "Ballenas", "2019-08-25T20:20:52Z",
            "Alan", "Doña Mary", true))
        transactions.add(TransactionEntity(120.50, "DEPOSIT", "Orden de tacos", "2019-08-24T14:15:22Z",
            "Alan", "Poio", true))

        for(items in transactions){
            val parsedDate = parseDate(items.createdAt)
            dates.add(parsedDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortTransactions(transactions: List<TransactionEntity>) : List<TransactionEntity>{
        return transactions.sortedBy {
            ZonedDateTime.parse(it.createdAt)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buildItems(list: List<TransactionEntity>) : List<TransactionListItem> {
        val result = mutableListOf<TransactionListItem>()

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        if (list.isNotEmpty()) {

            var date = ZonedDateTime.parse(list.first().createdAt)
            result.add(TransactionListItem.DateItem(date.format(formatter)))

            list.forEach {
                val date2 = ZonedDateTime.parse(it.createdAt)
                if (date.dayOfMonth != date2.dayOfMonth || date.month != date2.month || date.year != date2.year) {
                    result.add(TransactionListItem.DateItem(date2.format(formatter)))
                    date = date2
                }
                result.add(TransactionListItem.TransactionItem(it))
            }
        }

        return result
    }

    sealed class TransactionListItem {
        class DateItem(val date: String) : TransactionListItem()
        class TransactionItem(val transaction: TransactionEntity) : TransactionListItem()
    }
}