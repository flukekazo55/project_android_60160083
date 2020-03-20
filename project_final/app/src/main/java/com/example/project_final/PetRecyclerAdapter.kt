package com.example.project_final

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONArray
import java.security.AccessController.getContext

class PetRecyclerAdapter(context: Context, val dataSource: JSONArray) : RecyclerView.Adapter<PetRecyclerAdapter.Holder>() {

    private val thiscontext : Context = context

    class Holder(view : View) : RecyclerView.ViewHolder(view) {

        private val View = view;

        lateinit var layout : LinearLayout
        lateinit var nameTextView: TextView
        lateinit var yearTextView: TextView
        lateinit var typeNameTextView: TextView
        lateinit var image: ImageView
        lateinit var btn_del: Button
        lateinit var btn_detail: Button
        lateinit var btn_edit: Button

        fun Holder(){

            layout = View.findViewById<View>(R.id.recyview_layout) as LinearLayout
            nameTextView = View.findViewById<View>(R.id.name) as TextView
            typeNameTextView = View.findViewById<View>(R.id.type_name) as TextView
            yearTextView = View.findViewById<View>(R.id.year) as TextView
            image = View.findViewById<View>(R.id.imgV) as ImageView
            btn_del = View.findViewById<View>(R.id.btn_del) as Button
            btn_detail = View.findViewById<View>(R.id.btn_detail) as Button
            btn_edit = View.findViewById<View>(R.id.btn_edit) as Button
        }

    }


    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.recy_layout, parent, false))
    }


    override fun getItemCount(): Int {
        return dataSource.length()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.Holder()

        holder.nameTextView.setText(dataSource.getJSONObject(position).getString("name").toString())
        holder.typeNameTextView.setText(dataSource.getJSONObject(position).getString("type_name").toString())
        holder.yearTextView.setText(dataSource.getJSONObject(position).getString("year").toString() + " years")

        Glide.with(thiscontext)
            .load(dataSource.getJSONObject(position).getString("type_pet").toString())
            .into(holder.image)

        holder.btn_del.setOnClickListener{

            val mRootRef = FirebaseDatabase.getInstance().getReference()
            val mMessagesRef = mRootRef.child("data_pet").child(dataSource.getJSONObject(position).getString("key").toString())

            mMessagesRef.setValue(null)
            val fm = (holder.itemView.context as FragmentActivity).supportFragmentManager
            fm.popBackStack("profile", FragmentManager.POP_BACK_STACK_INCLUSIVE)

        }

        holder.btn_detail.setOnClickListener{

            val detail_pet = detail_pet().set_detail(
                dataSource.getJSONObject(position).getString("name_fb").toString(),
                dataSource.getJSONObject(position).getString("img_fb").toString(),
                dataSource.getJSONObject(position).getString("name").toString(),
                dataSource.getJSONObject(position).getString("type_pet").toString(),
                dataSource.getJSONObject(position).getString("year").toString(),
                dataSource.getJSONObject(position).getString("arrive_date").toString(),
                dataSource.getJSONObject(position).getString("type_name").toString()
            )

            val manager = (holder.itemView.context as FragmentActivity).supportFragmentManager
            val transaction : FragmentTransaction = manager!!.beginTransaction()
            transaction.replace(R.id.layout, detail_pet,"detail_pet")
            transaction.addToBackStack("detail_pet")
            transaction.commit()
        }

        holder.btn_edit.setOnClickListener{

            val Edit_data = Edit_data().set_edit_data(
                dataSource.getJSONObject(position).getString("key").toString(),
                dataSource.getJSONObject(position).getString("name_fb").toString(),
                dataSource.getJSONObject(position).getString("img_fb").toString(),
                dataSource.getJSONObject(position).getString("name").toString(),
                dataSource.getJSONObject(position).getString("year").toString()
            )

            val manager = (holder.itemView.context as FragmentActivity).supportFragmentManager
            val transaction : FragmentTransaction = manager!!.beginTransaction()
            transaction.replace(R.id.layout, Edit_data,"Edit_data")
            transaction.addToBackStack("Edit_data")
            transaction.commit()
        }

    }



}
