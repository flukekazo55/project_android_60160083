package com.example.project_final

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_edit_data.*

/**
 * A simple [Fragment] subclass.
 */
class Edit_data : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_data, container, false)
        // Inflate the layout for this fragment

        val btn_back : Button = view.findViewById(R.id.btn_back);
        val btn_update : Button = view.findViewById(R.id.btn_update);

        val ivProfilePicture = view.findViewById(R.id.iv_profile) as ImageView
        val tvName = view.findViewById(R.id.tv_name) as TextView
        Glide.with(activity!!.baseContext)
            .load(msg_img_fb)
            .into(ivProfilePicture)
        tvName.setText(msg_name_fb)

        val name : EditText = view.findViewById(R.id.name_ed)
        name.setText(msg_name)
        val year : EditText = view.findViewById(R.id.year_ed)
        year.setText(msg_year)


        btn_back.setOnClickListener {
            val profile = profile().newInstance(msg_img_fb, msg_name_fb)
            val fm = fragmentManager
            val transaction : FragmentTransaction = fm!!.beginTransaction()
            transaction.replace(R.id.layout, profile,"profile")
            transaction.addToBackStack("profile")
            transaction.commit()
        }

        btn_update.setOnClickListener {

            val mRootRef = FirebaseDatabase.getInstance().getReference()
            val mMessagesRef = mRootRef.child("data_pet")
            Log.i("name", name.text.toString())
            Log.i("year", year.text.toString())
            mMessagesRef.child(msg_key).child("name").setValue(name.text.toString())
            mMessagesRef.child(msg_key).child("year").setValue(year.text.toString())

            val profile = profile().newInstance(msg_img_fb, msg_name_fb)
            val fm2 = fragmentManager
            val transaction : FragmentTransaction = fm2!!.beginTransaction()
            transaction.replace(R.id.layout, profile,"profile")
            transaction.addToBackStack("profile")
            transaction.commit()


        }

        return view
    }


    private var msg_key : String = ""
    private var msg_name : String = ""
    private var msg_img_fb : String = ""
    private var msg_name_fb : String = ""
    private var msg_year : String = ""


    fun set_edit_data(key: String, name_fb: String, img_fb: String, name: String, year: String): Edit_data {

        val fragment = Edit_data()
        val bundle = Bundle()

        bundle.putString("msg_key", key)
        bundle.putString("msg_name_fb", name_fb)
        bundle.putString("msg_img_fb", img_fb)
        bundle.putString("msg_name", name)
        bundle.putString("msg_year", year)

        fragment.setArguments(bundle)

        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {

            msg_key = bundle.getString("msg_key").toString()
            msg_name_fb = bundle.getString("msg_name_fb").toString()
            msg_img_fb = bundle.getString("msg_img_fb").toString()
            msg_name = bundle.getString("msg_name").toString()
            msg_year = bundle.getString("msg_year").toString()

        }
    }

}
