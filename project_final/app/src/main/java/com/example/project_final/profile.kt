package com.example.project_final

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass.
 */
class profile : Fragment() {

    var PhotoURL : String = ""
    var Name : String = ""

    //ประกาศตัวแปร DatabaseReference รับค่า Instance และอ้างถึง path ที่เราต้องการใน database
    val mRootRef = FirebaseDatabase.getInstance().getReference()
    //อ้างอิงไปที่ path ที่เราต้องการจะจัดการข้อมูล ตัวอย่างคือ users และ messages
    val mUsersRef = mRootRef.child("users")
    val mMessagesRef = mRootRef.child("messages")


    fun newInstance(url: String,name : String): profile {

        val profile = profile()
        val bundle = Bundle()
        bundle.putString("PhotoURL", url)
        bundle.putString("Name", name)
        profile.setArguments(bundle)

        return profile
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val ivProfilePicture = view.findViewById(R.id.iv_profile) as ImageView
        val tvName = view.findViewById(R.id.tv_name) as TextView
        val login_button2 = view.findViewById(R.id.login_button2) as Button
        val btn_add = view.findViewById(R.id.btn_add) as Button
        val mRootRef = FirebaseDatabase.getInstance().reference
        val mMessagesRef = mRootRef.child("data_pet")

        mMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = JSONArray()

                for (ds in dataSnapshot.children) {
                    val jObject = JSONObject()
                    val name = ds.child("name").getValue(String::class.java)!!
                    val type_pet = ds.child("type_pet").getValue(String::class.java)!!
                    val year = ds.child("year").getValue(String::class.java)!!
                    val type_name = ds.child("type_name").getValue(String::class.java)!!
                    val arrive_date = ds.child("arrive_date").getValue(String::class.java)!!

                    jObject.put("key",ds.key)
                    jObject.put("name",name)
                    jObject.put("type_pet",type_pet)
                    jObject.put("year",year)
                    jObject.put("type_name",type_name)
                    jObject.put("arrive_date",arrive_date)
                    jObject.put("name_fb",Name)
                    jObject.put("img_fb",PhotoURL)
                    list.put(jObject)
                }

                val recyclerView: RecyclerView = view.findViewById(R.id.recyLayout)
                //ตั้งค่า Layout
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!.baseContext)
                recyclerView.layoutManager = layoutManager
                //ตั้งค่า Adapter
                val adapter = PetRecyclerAdapter(activity!!.baseContext,list)
                recyclerView.adapter = adapter

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        Glide.with(activity!!.baseContext)
            .load(PhotoURL)
            .into(ivProfilePicture)
        tvName.setText(Name)

        login_button2.setOnClickListener{

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setTitle("You want to logout?")
            alertDialog.setMessage("Please Confirm Logout!")

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm"
            ) { dialog, which -> dialog.dismiss()
                LoginManager.getInstance().logOut()
                activity!!.supportFragmentManager.popBackStack()
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()

            val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams


        }

        btn_add.setOnClickListener{

            var  name = "";
            var  age = "";

            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setTitle("You want to add data?")
            alertDialog.setMessage("Please Confirm to add data!")

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm"
            ) { dialog, which -> dialog.dismiss()
                val items = arrayOf("Dog", "Cat", "Rabbit", "Hamster", "Bird", "Fish", "etc.")
                AlertDialog.Builder(context)
                    .setTitle("Choose type of pet")
                    .setSingleChoiceItems(items, 0, null)
                    .setPositiveButton(
                        "Next",
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            dialog.dismiss()
                            val selectedPosition =
                                (dialog as AlertDialog).listView
                                    .checkedItemPosition

                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Input name of pet")
                            builder.setMessage("Please input your name of pet!")
                            val input_name = EditText(context)
                            val input_age = EditText(context)

                            input_name.inputType = InputType.TYPE_CLASS_TEXT
                            builder.setView(input_name)

                            builder.setPositiveButton(
                                "Next"
                            ) { dialog, which -> name =
                                input_name.text.toString()

                                val builder_2 = AlertDialog.Builder(context)
                                builder_2.setTitle("Input age of pet")
                                builder_2.setMessage("Please input your age of pet!")
                                input_age.inputType = InputType.TYPE_CLASS_NUMBER
                                builder_2.setView(input_age)
                                builder_2.setPositiveButton(
                                    "Insert"
                                ) {
                                        dialog, which ->
                                    age =  input_age.text.toString()
                                    var url_type = "";
                                    var type_name = "";
                                    if(selectedPosition == 0){
                                        type_name = "Dog"
                                        url_type = "https://graphicriver.img.customer.envatousercontent.com/files/270440720/CartoonDogPointer%20p.jpg?auto=compress%2Cformat&q=80&fit=crop&crop=top&max-h=8000&max-w=590&s=d7ccf47eef9f9a8f679c134cc70bffa5"
                                    }else if(selectedPosition == 1){
                                        type_name = "Cat"
                                        url_type = "https://www.pngitem.com/pimgs/m/45-452940_cute-sit-cat-yang-kitten-drawing-clipart-drawing.png"
                                    }else if(selectedPosition == 2){
                                        type_name = "Rabbit"
                                        url_type = "https://img.welovesolo.com/wp-content/image/01/Rabbit-cute-cartoon-vector-0227.jpg"
                                    }else if(selectedPosition == 3){
                                        type_name = "Hamster"
                                        url_type = "https://thumbs.dreamstime.com/z/hamster-rodent-white-background-hamster-rodent-white-background-cartoon-animal-illustration-cute-hamster-sit-smiling-143281171.jpg"
                                    }else if(selectedPosition == 4){
                                        type_name = "Bird"
                                        url_type = "https://www.kindpng.com/picc/m/168-1685436_cartoon-bird-cute-bird-fly-png-transparent-png.png"
                                    }else if(selectedPosition == 5){
                                        type_name = "Fish"
                                        url_type = "https://image.freepik.com/free-vector/goldfish-cartoon_1366-254.jpg"
                                    }else{
                                        type_name = "Etc."
                                        url_type = "https://lh3.googleusercontent.com/proxy/dzJsUsvo2pRTyw_ZiAiYYSFqTPaKV4xJ2BD7Ul46UptaDVQfTg_ODk4N0-Vg7O6o_4lzgy_PH8Kj930cMaP_heiiQc286OA"
                                    }

                                    val mMessagesRef2 = mRootRef.child("data_pet")
                                    val key = mMessagesRef.push().key
                                    val postValues: HashMap<String, Any> = HashMap()
                                    postValues["username"] = Name

                                    val localDateTime = LocalDateTime.now()
                                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    val output: String = formatter.format(localDateTime)
                                    postValues["arrive_date"] = output
                                    postValues["release_date"] = output
                                    postValues["name"] = name
                                    postValues["year"] = age
                                    postValues["type_pet"] = url_type
                                    postValues["type_name"] = type_name
                                    val childUpdates: MutableMap<String, Any> = HashMap()
                                    childUpdates["$key"] = postValues
                                    mMessagesRef2.updateChildren(childUpdates)
                                    // Do something useful withe the position of the selected radio button

                                }
                                builder_2.show()
                            }

                            builder.show()
                        })
                    .show()
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()

            val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams


        }

        // Inflate the layout for this fragment
        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            PhotoURL = bundle.getString("PhotoURL").toString()
            Name = bundle.getString("Name").toString()

        }

    }

}
