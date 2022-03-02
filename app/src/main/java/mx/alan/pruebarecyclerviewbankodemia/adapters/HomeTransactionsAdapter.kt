package mx.alan.pruebarecyclerviewbankodemia.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import mx.alan.pruebarecyclerviewbankodemia.MainActivity
import mx.alan.pruebarecyclerviewbankodemia.R
import mx.alan.pruebarecyclerviewbankodemia.model.TransactionEntity
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeTransactionsAdapter(val context: Context, val items: List<MainActivity.TransactionListItem>) : RecyclerView.Adapter<HomeTransactionsAdapter.TransactionItemViewHolder>() {

    private val DATE = 0
    private val TRANSACTION_INFO = 1


    abstract class TransactionItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setInfo(item: MainActivity.TransactionListItem)
        fun isEven(context: Context, isEven: Boolean) {
            view.background = if (isEven)
                ColorDrawable(context.getColor(R.color.my_gray))
                //ColorDrawable(Color.GRAY)
            else
                ColorDrawable(context.getColor(R.color.white))
                //ColorDrawable(Color.WHITE)
        }
    }

    class DateViewHolder(view: View) : TransactionItemViewHolder(view) {
        val dateText = view.findViewById<TextView>(R.id.date_recycler)

        override fun setInfo(item: MainActivity.TransactionListItem) {
            dateText.text = (item as MainActivity.TransactionListItem.DateItem).date
        }
    }

    class TransactionViewHolder(view: View) : TransactionItemViewHolder(view) {
        val conceptText = view.findViewById<TextView>(R.id.concepto_transaction)
        val horaText = view.findViewById<TextView>(R.id.hora_transaction)
        val cantidadText = view.findViewById<TextView>(R.id.cantidad_transaction)

        @RequiresApi(Build.VERSION_CODES.O)
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        val currencyFormatter = NumberFormat.getCurrencyInstance()

        @RequiresApi(Build.VERSION_CODES.O)
        override fun setInfo(item: MainActivity.TransactionListItem) {
            val transItem = (item as MainActivity.TransactionListItem.TransactionItem)
            conceptText.text = transItem.transaction.concept
            horaText.text = ZonedDateTime.parse(transItem.transaction.createdAt).format(timeFormatter)
            currencyFormatter.maximumFractionDigits = 2
            currencyFormatter.currency = Currency.getInstance("USD")
            cantidadText.text = currencyFormatter.format(transItem.transaction.amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            DATE -> DateViewHolder(inflater.inflate(R.layout.layout_recycler_date, parent, false))
            else -> TransactionViewHolder(inflater.inflate(R.layout.layout_recycler_transactions, parent , false))
        }
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        holder.setInfo(items[position])
        holder.isEven(context, position % 2 == 0)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is MainActivity.TransactionListItem.DateItem ->
                DATE
            is MainActivity.TransactionListItem.TransactionItem ->
                TRANSACTION_INFO
        }
    }
}