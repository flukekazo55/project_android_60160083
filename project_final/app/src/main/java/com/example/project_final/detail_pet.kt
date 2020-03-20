package com.example.project_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide

/**
 * A simple [Fragment] subclass.
 */
class detail_pet : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_pet, container, false)
        // Inflate the layout for this fragment

        val btn_back : Button = view.findViewById(R.id.btn_back);
        val img : ImageView = view.findViewById(R.id.imgV);

        val ivProfilePicture = view.findViewById(R.id.iv_profile) as ImageView
        val tvName = view.findViewById(R.id.tv_name) as TextView
        Glide.with(activity!!.baseContext)
            .load(msg_img_fb)
            .into(ivProfilePicture)
        tvName.setText(msg_name_fb)

        val name : TextView = view.findViewById(R.id.name)
        name.setText(msg_name)
        val type_name : TextView = view.findViewById(R.id.type_name)
        type_name.setText("Type of pet : "  +msg_type_name)
        val year : TextView = view.findViewById(R.id.year)
        year.setText("Age of pet : "  + msg_year + " years")
        val arrive_date : TextView = view.findViewById(R.id.arrive_date)
        arrive_date.setText("Arrive date of pet : "  + msg_arrive_date)


        Glide.with(activity!!.baseContext)
            .load(msg_type_img)
            .into(img)

        btn_back.setOnClickListener {
            val profile = profile().newInstance(msg_img_fb, msg_name_fb)
            val fm = fragmentManager
            val transaction : FragmentTransaction = fm!!.beginTransaction()
            transaction.replace(R.id.layout, profile,"profile")
            transaction.addToBackStack("profile")
            transaction.commit()
        }

        return view
    }


    private var msg_name : String = ""
    private var msg_type_img : String = ""
    private var msg_name_fb : String = ""
    private var msg_img_fb : String = ""
    private var msg_year : String = ""
    private var msg_arrive_date : String = ""
    private var msg_type_name : String = ""


    fun set_detail(name_fb: String, img_fb: String, name: String, type_img: String, year: String, arrive_date: String, type_name: String): detail_pet {

        val fragment = detail_pet()
        val bundle = Bundle()

        bundle.putString("msg_name_fb", name_fb)
        bundle.putString("msg_img_fb", img_fb)
        bundle.putString("msg_name", name)
        bundle.putString("msg_type_img", type_img)
        bundle.putString("msg_year", year)
        bundle.putString("msg_arrive_date", arrive_date)
        bundle.putString("msg_type_name", type_name)

        fragment.setArguments(bundle)

        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {

            msg_name_fb = bundle.getString("msg_name_fb").toString()
            msg_img_fb = bundle.getString("msg_img_fb").toString()
            msg_name = bundle.getString("msg_name").toString()
            msg_type_img = bundle.getString("msg_type_img").toString()
            msg_year = bundle.getString("msg_year").toString()
            msg_arrive_date = bundle.getString("msg_arrive_date").toString()
            msg_type_name = bundle.getString("msg_type_name").toString()

        }
    }


}
