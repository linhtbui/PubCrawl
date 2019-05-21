package us.grinnell.myweather

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.dialog_add_new_item.view.*

import us.grinnell.myweather.data.Item

class AddItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    //attach the ItemHandler to the dialog fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        //check if the activity is implementing the interface - if it is, then link it to the dialog fragment
        if (context is ItemHandler) {
            itemHandler = context
        }
        else {
            throw RuntimeException("The Activity does not implement the ItemHandler interface")
        }
    }

    private lateinit var etPubName: EditText
    //private lateinit var etDrink: EditText
    //private lateinit var etDistance: EditText
    private lateinit var etPubDescription: EditText
    private lateinit var etPrice: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        initializeDialogBuilder(builder)

        val arguments = this.arguments
        //if editing an existing item, pre-fill all dialog fields with the current info about the item
        if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            val item = arguments.getSerializable(MainActivity.KEY_ITEM_TO_EDIT) as Item
            initializeEditDialogFromExistingItem(item, builder)
        }

        builder.setPositiveButton(getString(R.string.ok)) {
                dialog, witch -> //this event handler gets set in onResume()
        }

        return builder.create()
    }

    private fun initializeDialogBuilder(builder: AlertDialog.Builder) {
        builder.setTitle(getString(R.string.new_item))

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_new_item, null)

        etPubDescription = rootView.etItemDescription
        etPubName = rootView.etItemName
        etPrice = rootView.etEstimatedPrice

        builder.setView(rootView)
    }

    private fun initializeEditDialogFromExistingItem(item: Item, builder: AlertDialog.Builder) {
        etPubName.setText(item.name)
        etPubDescription.setText(item.pubDescription)

        etPrice.setText(item.price)

        builder.setTitle(getString(R.string.edit_item))
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        //update the current item being edited or create a new Item based on the values in the dialog
        positiveButton.setOnClickListener {
            if (etPubName.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) handleItemEdit()
                else handleItemCreate()
                dialog.dismiss()
            } else {
                etPubName.error = getString(R.string.field_empty_error)
            }
        }

    }

    //create a new Item object with instance variable values extracted from the dialog
    private fun handleItemCreate() {
        itemHandler.itemCreated(
            Item(null,
                "pina colada",
                "Instant",
                false,
                11,
                5,
                etPubDescription.text.toString(),
                etPrice.text.toString()
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            MainActivity.KEY_ITEM_TO_EDIT
        ) as Item
        //update values of the given item instance variables from values in the dialog
        itemToEdit.name = etPubName.text.toString()
        itemToEdit.pubDescription = etPubDescription.text.toString()

        itemToEdit.price = etPrice.text.toString()

        itemHandler.itemUpdated(itemToEdit)
    }
}