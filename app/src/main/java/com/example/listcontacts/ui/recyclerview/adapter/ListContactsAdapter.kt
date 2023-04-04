package com.example.listcontacts.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listcontacts.R
import com.example.listcontacts.databinding.ContactItemBinding
import com.example.listcontacts.model.Contact
import com.example.listcontacts.ui.list.ListContactsFragment
import com.example.listcontacts.utils.Utils.loadImageFromUrl
import kotlinx.coroutines.flow.combineTransform

class ListContactsAdapter(
    private val context: Context,
    private val contacts: List<Contact>,
    private val deleteContact: (Contact) -> Unit,
   // private val editContact: (Contact) -> Unit
):
    RecyclerView.Adapter<ListContactsAdapter.ContactViewHolder>() {

//    companion object {
//        val SELECT_EDIT: Int = 1
//        val SELECT_REMOVE: Int = 2
//    }

    class ContactViewHolder(val binding: ContactItemBinding):
    RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            val contactSelected:(Contact,Int) -> Unit

            binding.contactItemName.text = contact.name
            binding.contactItemPhoneNumber.text = contact.numberPhone

            if(contact.imgUrl != null) loadImageFromUrl(contact.imgUrl,binding.imageView)
            else binding.imageView.setImageResource(R.drawable.person_add)

        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
       // holder.binding.btnDeleteContact.setOnClickListener { contactSelected(contact, SELECT_REMOVE) }
       // holder.binding.btnEditContact.setOnClickListener { contactSelected(contact, SELECT_EDIT) }
//        holder.binding.btnEditContact.setOnClickListener {
//            editContact(contact) // chama a função de retorno de chamada para exclusão de contato
//        }
        holder.binding.btnDeleteContact.setOnClickListener {
            deleteContact(contact)
        }
        holder.bind(contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)

        return ContactViewHolder(binding)
    }





}
