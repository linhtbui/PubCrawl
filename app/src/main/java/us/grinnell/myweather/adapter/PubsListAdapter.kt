package us.grinnell.myweather.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_row.view.*
import us.grinnell.myweather.R
import us.grinnell.myweather.data.AppDatabase
import us.grinnell.myweather.data.Item
import us.grinnell.myweather.MainActivity

class PubsListAdapter : RecyclerView.Adapter<PubsListAdapter.ViewHolder> {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon = itemView.ivIcon
        val tvName = itemView.tvName
        val tvDescription = itemView.tvDescription
        val tvDrink = itemView.tvDrink
        val rating = itemView.rating
        val tvDollarSign = itemView.tvDollarSign
        val cbWent = itemView.cbWent
        val btnDelete = itemView.btnDelete
        val card_view = itemView.card_view
    }

    var items = mutableListOf<Item>()

    val context : Context

    constructor(context: Context, items: List<Item>, routeNumber: Int) : super() {
        this.context = context
        if (routeNumber == 1) {
            Log.d("LMAO", "number 1")
            add_route_one()
        } else if (routeNumber == 2) {
            Log.d("LMAO", "number 2")
            add_route_two()
        }

        this.items.addAll(items)

        updateTvNoItemsVisibility()
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_row, parent, false
        )
        return ViewHolder(view)
    }

    private fun add_route_one() {
        items.clear()
        items.add(Item(null,
            "Palinka",
            "Instant",
            false,
            11,
            5,
            "Good for dancing",
            "$$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Pina Colada",
            "Kandalló Kézműves Pub",
            false,
            11,
            5,
            "Good for dancing",
            "$$$$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Long Island Iced Tea",
            "Blokk Bar",
            false,
            11,
            5,
            "Good for dancing",
            "$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Mojito",
            "Martt Bar",
            false,
            11,
            5,
            "Good for dancing",
            "$$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Margarita",
            "Doblo Wine Bar",
            false,
            11,
            5,
            "Good for dancing",
            "$$$"))
        notifyItemInserted(items.lastIndex)
    }


    private fun add_route_two() {
        items.clear()
        items.add(Item(null,
            "Palinka",
            "Champs Sports Pubs ",
            false,
            11,
            5,
            "Good for dancing",
            "$$$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Pina Colada",
            "Szimpla",
            false,
            11,
            5,
            "Good for dancing",
            "$$$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Vodka",
            "Fuge Udvar",
            false,
            11,
            5,
            "Good for dancing",
            "$$$"))
        notifyItemInserted(items.lastIndex)

        items.add(Item(null,
            "Gin and Tonic",
            "Hetke Pub",
            false,
            11,
            5,
            "Good for dancing",
            "$$$"))
        notifyItemInserted(items.lastIndex)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        linkItemPropertiesToViewHolder(holder, item)

        //update item's bought property based on checkbox value
        holder.cbWent.setOnClickListener {
            item.went = holder.cbWent.isChecked

            //update item in the database
            Thread {
                AppDatabase.getInstance(context).pubsListDao().updatePub(item)
                highlightItem(item.went,holder)

            }.start()

        }

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        //if you click anywhere on the card, edit the item
        holder.itemView.setOnClickListener {
            (context as MainActivity).showEditItemDialog(item, holder.adapterPosition)
        }
    }

    private fun linkItemPropertiesToViewHolder(holder: ViewHolder, item: Item) {
        holder.tvName.text = item.name
        holder.tvDescription.text = item.pubDescription
        holder.tvDrink.text = item.drink
        holder.tvDollarSign.text = item.price
        holder.cbWent.isChecked = item.went
        holder.rating.numStars = item.rating
        highlightItem(item.went,holder)


        updateEstPriceViewVisibility(item, holder)
        chooseIconForCategory(item, holder)
    }

    //if no estimated price given for the item, hide the estimated price and dollar sign views
    private fun updateEstPriceViewVisibility(item: Item, holder: ViewHolder) {
        if (item.price == "") {
            holder.tvDollarSign.visibility = View.GONE
        } else {
            holder.tvDollarSign.visibility = View.VISIBLE
        }
    }

    //update the icon of the shopping list item based on item category
    private fun chooseIconForCategory(item: Item, holder: ViewHolder) {
        holder.ivIcon.setImageResource(R.drawable.instant)
    }

    private fun deleteItem(adapterPosition: Int) {
        //remove from the database
        Thread {
            AppDatabase.getInstance(context).pubsListDao().deletePub(items[adapterPosition])

            //remove from the recycler view
            items.removeAt(adapterPosition)
            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
                updateTvNoItemsVisibility()
            }
        }.start()
    }

    private fun highlightItem(highlighted: Boolean, holder: ViewHolder) {
        (context as MainActivity).runOnUiThread {
            if (highlighted) {
                holder.card_view.setCardBackgroundColor(Color.argb(255, 255, 228, 225))
            } else {
                holder.card_view.setCardBackgroundColor(Color.WHITE)
            }
        }
    }

    //show 'no items' label only if the shopping list is empty
    fun updateTvNoItemsVisibility() {
        if (items.size != 0) {
            //(context as MainActivity).tvNoItems.visibility = View.GONE
        }

        if (items.size == 0) {
            //(context as MainActivity).tvNoItems.visibility = View.VISIBLE
        }
    }

    fun addItem(item: Item) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
        //hide the 'no items' label
//        if ((context as ShoppingListActivity).tvNoItems.visibility == View.VISIBLE) {
//            (context as ShoppingListActivity).tvNoItems.visibility = View.GONE
//        }
    }

    fun updateItem(item: Item, idx: Int) {
        items[idx] = item
        notifyItemChanged(idx)
    }

    fun removeAll() {
        Thread {
            //delete all from database
            AppDatabase.getInstance(context).pubsListDao().deleteAll()

            //update the recycler view
            items.clear()
            (context as MainActivity).runOnUiThread {
                notifyDataSetChanged()

                //show the 'no items' label
                //(context as MainActivity).tvNoItems.visibility = View.VISIBLE
            }
        }.start()
    }

    fun clearChecked() {
        Thread {
            //delete checked items from database
            AppDatabase.getInstance(context).pubsListDao().deleteAllBought()

            //update the recycler view
            items.clear()
            items.addAll(AppDatabase.getInstance(context).pubsListDao().findAllPubs())

            (context as MainActivity).runOnUiThread {
                notifyDataSetChanged()
                updateTvNoItemsVisibility()
            }
        }.start()
    }
}