package tpi.panicbutton

import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class CustomAdapter(val phoneNumberList: ArrayList<PhoneNumber>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.phone_numbers_layout, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return phoneNumberList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val phoneNumber : PhoneNumber = phoneNumberList[p1]

        p0.textViewNumber.text = phoneNumber.number

        p0.buttonDelete.setOnClickListener {
            phoneNumberList.removeAt(p1);  // remove the item from list
            notifyItemRemoved(p1); // notify the adapter about the removed item
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        val textViewNumber = itemView.findViewById(R.id.textViewNumber) as TextView
        val buttonDelete = itemView.findViewById(R.id.buttonDelete) as Button
    }
}