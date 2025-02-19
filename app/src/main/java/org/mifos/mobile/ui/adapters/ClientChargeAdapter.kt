package org.mifos.mobile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife

import org.mifos.mobile.R
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.Charge
import org.mifos.mobile.ui.getThemeAttributeColor
import org.mifos.mobile.utils.CurrencyUtil.formatCurrency
import org.mifos.mobile.utils.DateHelper.getDateAsString

import java.util.*
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
class ClientChargeAdapter (
    val onItemClick: (itemPosition: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clientChargeList: List<Charge?>? = ArrayList()
    fun setClientChargeList(clientChargeList: List<Charge?>?) {
        this.clientChargeList = clientChargeList
    }

    fun getItem(position: Int): Charge? {
        return clientChargeList?.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.row_client_charge, parent, false)
        vh = ViewHolder(v)
        v.setOnClickListener {
            onItemClick(vh.bindingAdapterPosition)
        }
        return vh
    }

    // Binds the values for each of the stored variables to the view
    // Also changes the color of the circle depending on whether the charge is active or not
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val charge = getItem(position)
        var currencyRepresentation = charge?.currency?.displaySymbol
        if (currencyRepresentation == null) {
            currencyRepresentation = charge?.currency?.code
        }
        val context = holder.itemView.context
        (holder as ViewHolder).tvAmountDue?.text = context.getString(R.string.string_and_string,
                currencyRepresentation, formatCurrency(context,
                charge?.amount))
        holder.tvAmountPaid?.text = context.getString(R.string.string_and_string,
                currencyRepresentation, formatCurrency(context,
                charge?.amountPaid))
        holder.tvAmountWaived?.text = context.getString(R.string.string_and_string, currencyRepresentation,
                formatCurrency(context, charge?.amountWaived))
        holder.tvAmountOutstanding?.text = context.getString(R.string.string_and_string, currencyRepresentation,
                formatCurrency(context, charge?.amountOutstanding))
        holder.tvClientName?.text = charge?.name
        if (charge?.dueDate?.isNotEmpty() == true) {
            holder.tvDueDate?.text = getDateAsString(charge.dueDate)
        }
        if (charge?.isPaid == true || charge?.isWaived == true || charge?.paid == true || charge?.waived == true) {
            holder.circle_status?.setBackgroundColor(holder.itemView.context.getThemeAttributeColor(R.attr.colorError))
        } else {
            holder.circle_status?.setBackgroundColor(context.getThemeAttributeColor(R.attr.colorSuccess))
        }
    }

    override fun getItemCount(): Int {
        return if (clientChargeList != null) clientChargeList!!.size
        else 0
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.tv_clientName)
        var tvClientName: TextView? = null

        @JvmField
        @BindView(R.id.tv_due_date)
        var tvDueDate: TextView? = null

        @JvmField
        @BindView(R.id.tv_amount)
        var tvAmountDue: TextView? = null

        @JvmField
        @BindView(R.id.tv_amount_paid)
        var tvAmountPaid: TextView? = null

        @JvmField
        @BindView(R.id.tv_amount_waived)
        var tvAmountWaived: TextView? = null

        @JvmField
        @BindView(R.id.tv_amount_outstanding)
        var tvAmountOutstanding: TextView? = null

        @JvmField
        @BindView(R.id.circle_status)
        var circle_status: View? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }
}